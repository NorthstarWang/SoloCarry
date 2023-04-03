package com.example.solocarry;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.solocarry.view.AuthActivity;
import com.example.solocarry.view.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AuthActivityTest {
    private Solo solo;

    public AuthActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<AuthActivity> rule =
            new ActivityTestRule<>(AuthActivity.class, true, true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(),rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testLogin() {
        String username = "admin@gmail.com";
        String pwd = "123456";


        // Find the username and password EditText views and enter test credentials
        solo.enterText((EditText) solo.getView(R.id.input_field_user_name), username);
        solo.enterText((EditText) solo.getView(R.id.input_field_password), pwd);

        // Find and click the login button
        solo.clickOnView(solo.getView(R.id.button));

        solo.waitForActivity(MainActivity.class);
        // Wait for the MainActivity to be launched
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        SharedPreferences preferences = getInstrumentation().getTargetContext().getSharedPreferences("test_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("auth_token","test_token");
        editor.apply();
    }
}

