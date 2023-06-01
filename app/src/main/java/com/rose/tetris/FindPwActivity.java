package com.rose.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindPwActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 변수
    private DatabaseReference mDatabaseRef; // 실시간 DB
    private Button btnFindPw;
    private EditText txtName, txtPhone;
    private TextView txtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tetrisapp").child("UserAccount");

        // 김재엽 코드
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtID = findViewById(R.id.txtId);

        btnFindPw = findViewById(R.id.btnFindPw);
        btnFindPw.setOnClickListener(find_info_click);
    }

    View.OnClickListener find_info_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean emailCheck = false; // 이메일 존재 여부 체크 변수

                    String strName = txtName.getText().toString();
                    String strPhone = txtPhone.getText().toString();
                    String strEmail = txtID.getText().toString();

                    if(strName.equals("")){
                        Toast.makeText(FindPwActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(strPhone.equals("")){
                        Toast.makeText(FindPwActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(strEmail.equals("")){
                        Toast.makeText(FindPwActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        UserAccount accounts = snapshot1.getValue(UserAccount.class);

                        if(strName.equals(accounts.getName()) && strPhone.equals(accounts.getPhone()) && strEmail.equals(accounts.getEmailId())){
                            emailCheck = true;

                            mFirebaseAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(FindPwActivity.this, "비밀번호 재설정 메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FindPwActivity.this,StartActivity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);

                                    }else{
                                        Toast.makeText(FindPwActivity.this, "비밀번호 재설정 메일 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }

                    if(!emailCheck){
                        Toast.makeText(FindPwActivity.this, "존재하지 않는 정보 입니다.", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FindPwActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    };



    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindPwActivity.this,FindAccountActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }
}