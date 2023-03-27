package com.example.solocarry;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.MainActivity;
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
     * Try to click on the rank, see if rank disappear
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
     * Try to click on the rank, see if rank disappear
     * Also click on the back button to see if user can go back to the main page
     */
    @Test
    public void checkChat() {
        // Launch RankingActivity before running the test
        Intent chatIntent = new Intent(solo.getCurrentActivity(), ChatActivity.class);
        solo.getCurrentActivity().startActivity(chatIntent);

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.chat_button));
        solo.assertCurrentActivity("Wrong Activity",ChatActivity.class);
    }


}
