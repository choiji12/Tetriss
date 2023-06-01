package com.rose.tetris;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindIdActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 변수
    private DatabaseReference mDatabaseRef; // 실시간 DB
    private Button btnFindId;
    private EditText txtName, txtPhone;
    private TextView txtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tetrisapp").child("UserAccount");

        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindIdActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // 김재엽 코드
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtID = findViewById(R.id.txtId);
        btnFindId = findViewById(R.id.btnFindId);
        btnFindId.setOnClickListener(find_info_click);
    }

    View.OnClickListener find_info_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txtID.setText("");
            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean emailCheck = false; // 이메일 존재 여부 체크 변수

                    String strName = txtName.getText().toString().trim();
                    String strPhone = txtPhone.getText().toString().trim();

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        UserAccount accounts = snapshot1.getValue(UserAccount.class);

                        if(strName.equals(accounts.getName()) && strPhone.equals(accounts.getPhone())){
                            txtID.setVisibility(View.VISIBLE);
//                            txtID.setText(accounts.getEmailId());
                            String targetText = accounts.getEmailId();
                            String fullText = "사용자의 ID는\n" + targetText + "\n입니다";

                            SpannableString spannableString = new SpannableString(fullText);
                            int colorBlue = Color.rgb(108,145,250);
                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colorBlue);
                            spannableString.setSpan(colorSpan,9,9+targetText.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                            spannableString.setSpan(styleSpan, 9, 9+targetText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            txtID.setVisibility(View.VISIBLE);
                            txtID.setText(spannableString, TextView.BufferType.SPANNABLE);

                            emailCheck = true;
                        }
                    }

                    if(!emailCheck){
                        txtID.setVisibility(View.VISIBLE);
                        txtID.setText("존재하지 않는 정보 입니다.");
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FindIdActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    };

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindIdActivity.this,FindAccountActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }
}