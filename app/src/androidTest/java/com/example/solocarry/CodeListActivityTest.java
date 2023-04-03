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
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.ChatActivity;
import com.example.solocarry.view.CodeDetailActivity;
import com.example.solocarry.view.CodeListActivity;
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

public class CodeListActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CodeListActivity> rule =
            new ActivityTestRule<>(CodeListActivity.class, true, true);

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
    public void testGetList(){
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity",CodeListActivity.class);
        ListView name = (ListView) solo.getView(R.id.code_list);
        solo.clickOnView(name);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong activity",CodeDetailActivity.class);
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", CodeListActivity.class);
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