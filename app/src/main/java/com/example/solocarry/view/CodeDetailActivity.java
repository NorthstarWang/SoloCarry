package com.example.solocarry.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.solocarry.databinding.PinCodeInfoBinding;

public class CodeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = PinCodeInfoBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);
    }
}