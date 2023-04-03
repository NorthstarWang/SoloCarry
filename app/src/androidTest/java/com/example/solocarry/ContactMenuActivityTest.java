package com.example.solocarry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.ContactMenuActivity;
import com.example.solocarry.view.MainActivity;
import com.example.solocarry.view.RankingActivity;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

public class ContactMenuActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ContactMenuActivity> rule =
            new ActivityTestRule<>(ContactMenuActivity.class, true, true);

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
    public void testAddFriends(){
        solo.assertCurrentActivity("Wrong Activity",ContactMenuActivity.class);
        solo.sleep(3000);

        // Click the search button
        View btnSearchAdd = solo.getView(R.id.bt_search_add);
        solo.clickOnView(btnSearchAdd);

        // Wait for the dialog to appear
        solo.waitForDialogToOpen();
        solo.sleep(1000);

        solo.enterText(0, "yujiahao.lawrence@gmail.com");
        solo.sendKey(Solo.ENTER);
        solo.sendKey(Solo.ENTER);

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