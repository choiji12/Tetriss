package com.rose.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FindAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        Button findId = findViewById(R.id.btnFindId);
        Button findPw = findViewById(R.id.btnFindPw);

        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindAccountActivity.this,FindIdActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);
            }
        });

        findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindAccountActivity.this,FindPwActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);
            }
        });
    }
    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindAccountActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }

}