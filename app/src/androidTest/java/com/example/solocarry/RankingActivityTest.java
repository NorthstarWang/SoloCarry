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
        solo.clickOnView(solo.getView(R.id.switch_rank));
        solo.waitForView(solo.getView(R.id.first_score));
        solo.sleep(5000);
        TextView name = (TextView) solo.getView(R.id.third_name);
        TextView score = (TextView) solo.getView(R.id.third_score);
        String third_name = name.getText().toString();
        String third_score = score.getText().toString();
        assertEquals(third_name,"");
        assertEquals(third_score,"");

    }

}