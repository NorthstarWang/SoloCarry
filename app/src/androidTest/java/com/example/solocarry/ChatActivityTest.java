package com.example.solocarry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ChatActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ChatActivity> rule =
            new ActivityTestRule<>(ChatActivity.class, true, true);

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
    public void testAddChat(){
        solo.waitForView(R.id.button_add_chat);
        solo.clickOnView(solo.getView(R.id.button_add_chat));

        assertTrue(solo.waitForText("You do not have friend yet!",1,8000));

    }
}
