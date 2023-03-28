package com.example.solocarry;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.solocarry.view.CameraActivity;
import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.MainActivity;
import com.example.solocarry.view.ProfileSelfActivity;
import com.example.solocarry.view.RankingActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 */
public class MainActivityTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Try to click on the rank, see if rank appear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkRank(){
        // Launch RankingActivity before running the test
        Intent rankingIntent = new Intent(solo.getCurrentActivity(), RankingActivity.class);
        solo.getCurrentActivity().startActivity(rankingIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the rank button
        solo.clickOnView(solo.getView(R.id.ranking_button));
        // Asserts that the current activity is the RankingActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RankingActivity.class);
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.imageView_first));

        TextView firstName = (TextView) solo.getView(R.id.first_name);
        String name = firstName.getText().toString();
        // hard coding to see if the top player disappear, will be changed later
        assertEquals(name,"YU Jiahao");
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Try to click on the rank, see if rank appear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkChat() {
        // Launch ChatActivity before running the test
        Intent chatIntent = new Intent(solo.getCurrentActivity(), ChatActivity.class);
        solo.getCurrentActivity().startActivity(chatIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check if we successfully access the chat activity
        solo.clickOnView(solo.getView(R.id.chat_button));
        solo.assertCurrentActivity("Wrong Activity",ChatActivity.class);

        // see if the "message" title appear
        TextView title = (TextView) solo.getView(R.id.username);
        solo.clickOnView(title);
        assertEquals(title.getText().toString(),"Message");

        // check if we can go back from chat activity
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.clickOnView(solo.getView(android.R.id.content));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Try to click on the profile, see if self profile appear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkSelfProfile() {
        // Launch SelfProfileActivity before running the test
        Intent profileIntent = new Intent(solo.getCurrentActivity(), ProfileSelfActivity.class);
        solo.getCurrentActivity().startActivity(profileIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check if we successfully access a correct activity
        solo.clickOnView(solo.getView(R.id.userPhoto));
        solo.assertCurrentActivity("Wrong Activity",ProfileSelfActivity.class);

        // check if profile picture view and user name appear
        solo.clickOnView(solo.getView(R.id.profile_pic2));
        TextView name = (TextView) solo.getView(R.id.username_info);
        String profile_name = name.getText().toString();
        assertEquals(profile_name,"YU Jiahao");

        //check whether we can go back to the main activity
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.clickOnView(solo.getView(android.R.id.content));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Try to click on the profile, see if self profile appear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkCamera() {
        // Launch CameraActivity before running the test
        Intent cameraIntent = new Intent(solo.getCurrentActivity(), CameraActivity.class);
        solo.getCurrentActivity().startActivity(cameraIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.camera_button));
        solo.assertCurrentActivity("Wrong Activity",CameraActivity.class);

        //
        solo.clickOnView(solo.getView(R.id.button_back_camera));
        solo.clickOnView(solo.getView(android.R.id.content));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
}
