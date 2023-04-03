package com.example.solocarry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.CameraActivity;
import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.MainActivity;
import com.example.solocarry.view.ProfileSelfActivity;
import com.example.solocarry.view.RankingActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        SharedPreferences preferences = getInstrumentation().getTargetContext()
                .getSharedPreferences("test_preferences", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);

        if (authToken == null) {
            // User is not logged in, programmatically log in the test user

            solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
            solo.enterText((EditText) solo.getView(R.id.input_field_user_name), "admin@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.input_field_password), "123456");
            solo.clickOnView(solo.getView(R.id.button));
            solo.waitForActivity(MainActivity.class);
        } else {
            // User is already logged in, proceed with testing the ranking system
            // ...
        }
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        rule.getActivity();

    }

    /**
     * Try to click on the rank, see if rank appear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkRank() {


        // Launch RankingActivity before running the test
        Intent rankingIntent = new Intent(solo.getCurrentActivity(), RankingActivity.class);
        solo.getCurrentActivity().startActivity(rankingIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the rank button
        solo.clickOnView(solo.getView(R.id.ranking_button));
        solo.sleep(3000);
        // Asserts that the current activity is the RankingActivity. Otherwise, show “Wrong Activity”
        solo.waitForActivity(RankingActivity.class);
        solo.assertCurrentActivity("Wrong Activity", RankingActivity.class);

        solo.clickOnView(solo.getView(R.id.imageView_first));

        TextView firstName = (TextView) solo.getView(R.id.first_name);
        String name = firstName.getText().toString();
        // hard coding to see if the top player disappear, will be changed later
        assertEquals(name, "North Star");

        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(MainActivity.class);
        solo.sleep(3000);
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

        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        // check if we successfully access the chat activity
        solo.clickOnView(solo.getView(R.id.chat_button));
        solo.waitForActivity(ChatActivity.class);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", ChatActivity.class);

        // see if the "message" title appear
        TextView title = (TextView) solo.getView(R.id.username);
        solo.clickOnView(title);
        assertEquals(title.getText().toString(),"Message");

        // check if we can go back from chat activity
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(MainActivity.class);
        solo.sleep(3000);
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
        solo.waitForActivity(ProfileSelfActivity.class);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",ProfileSelfActivity.class);

        // check if profile picture view and user name appear
        solo.clickOnView(solo.getView(R.id.profile_pic2));
        solo.sleep(3000);
        TextView name = (TextView) solo.getView(R.id.username_info);
        String profile_name = name.getText().toString();
        assertEquals(profile_name,"Based");

        //check whether we can go back to the main activity
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(MainActivity.class);

        //solo.clickOnView(solo.getView(android.R.id.content));
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
        solo.waitForActivity(CameraActivity.class);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",CameraActivity.class);

        //
        solo.clickOnView(solo.getView(R.id.button_back_camera));
        solo.waitForActivity(ProfileSelfActivity.class);
        solo.sleep(3000);
        //solo.clickOnView(solo.getView(android.R.id.content));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
}


