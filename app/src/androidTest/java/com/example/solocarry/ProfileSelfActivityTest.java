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
import com.example.solocarry.view.ProfileActivity;
import com.example.solocarry.view.ProfileSelfActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

public class ProfileSelfActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ProfileSelfActivity> rule =
            new ActivityTestRule<>(ProfileSelfActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //solo.clickOnView(solo.getView(R.id.chat_button)); // assuming chat button id is chat_button
        //solo.sleep(5000);
        //solo.assertCurrentActivity("Wrong Activity", ChatActivity.class);
    }

    @Test
    public void start() throws Exception {
        rule.getActivity();
    }

    @Test
    public void testUserName(){
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.profile_pic2));
        TextView name = (TextView) solo.getView(R.id.username_info);
        String profile_name = name.getText().toString();
        assertEquals(profile_name,"Based");
    }

    @Test
    public void testSumScore(){
        solo.sleep(3000);
        TextView name = (TextView) solo.getView(R.id.sum_score_number);
        String profile_name = name.getText().toString();
        assertEquals(profile_name,"3524");
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