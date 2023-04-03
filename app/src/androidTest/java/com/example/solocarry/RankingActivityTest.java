package com.example.solocarry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.MainActivity;
import com.example.solocarry.view.RankingActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

public class RankingActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RankingActivity> rule =
            new ActivityTestRule<>(RankingActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//        solo.clickOnView(solo.getView(R.id.ranking_button)); // assuming chat button id is chat_button
//        solo.sleep(5000);
//        solo.assertCurrentActivity("Wrong Activity", RankingActivity.class);
    }

    @Test
    public void start() throws Exception {
        rule.getActivity();
    }

    @Test
    public void testSwitchRank(){
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.switch_rank));
        solo.waitForView(solo.getView(R.id.first_score));
        solo.sleep(5000);
        TextView score = (TextView) solo.getView(R.id.first_score);
        String first_score = score.getText().toString();
        assertEquals(first_score,"3524");
        solo.clickOnView(solo.getView(R.id.switch_rank));
        solo.waitForView(solo.getView(R.id.imageView_first));
        solo.sleep(5000);
        TextView firstName = (TextView) solo.getView(R.id.first_name);
        String name = firstName.getText().toString();
        assertEquals(name, "North Star");
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}