package com.rose.tetris;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // 이메일 발송 메소드
    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static boolean isValid(String password){
        if(password.length() < 8){
            return false;
        }
//        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$"
        String pattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        if(!Pattern.matches(pattern, password)){
            return false;
        }

        if(password.contains(" ")){
            return false;
        }

        return true;
    }

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd, mEtPwdCheck, mEtName, mEtPhone;
    private Button mBtnRegister;
    private LottieAnimationView aniRegisterSuccess;
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton btnCancle = findViewById(R.id.btnCancle);
        aniRegisterSuccess = findViewById(R.id.aniRegisterSuccess);
        aniRegisterSuccess.setVisibility(View.INVISIBLE);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
            }
        });

        // 비밀번호 보여주는 버튼

        LottieAnimationView aniShowPw = findViewById(R.id.aniShowPw);
        LottieAnimationView aniShowPw2 = findViewById(R.id.aniShowPw2);

        aniShowPw.setAnimation(R.raw.password_show);

        EditText txtPasswd = findViewById(R.id.txtPasswd);
        EditText txtPasswdCheck = findViewById(R.id.txtPasswdCheck);
        aniShowPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputType = txtPasswd.getInputType();
                if(isPasswordVisible){
                    //비밀번호가 보여질 때 안보이게
                    txtPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    aniShowPw.setMinAndMaxProgress(0.5f,1.0f);
                    aniShowPw.setSpeed(-1.0f);
                    aniShowPw.playAnimation();
                } else{
                    //비밀번호가 숨겨질 때 보이게
                    txtPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    aniShowPw.setMinAndMaxProgress(0.5f,1.0f);
                    aniShowPw.setSpeed(1.0f);
                    aniShowPw.playAnimation();
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        aniShowPw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputType = txtPasswdCheck.getInputType();
                if(isPasswordVisible){
                    //비밀번호가 보여질 때 안보이게
                    txtPasswdCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    aniShowPw2.setMinAndMaxProgress(0.5f,1.0f);
                    aniShowPw2.setSpeed(-1.0f);
                    aniShowPw2.playAnimation();
                } else{
                    //비밀번호가 숨겨질 때 보이게
                    txtPasswdCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    aniShowPw2.setMinAndMaxProgress(0.5f,1.0f);
                    aniShowPw2.setSpeed(1.0f);
                    aniShowPw2.playAnimation();
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        // 회원가입 부분
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tetrisapp");

        mEtEmail = findViewById(R.id.txtEmail);
        mEtPwd = findViewById(R.id.txtPasswd);
        mEtPwdCheck = findViewById(R.id.txtPasswdCheck);
        mEtName = findViewById(R.id.txtName);
        mEtPhone = findViewById(R.id.txtPhone);

        mBtnRegister = findViewById(R.id.btnRegister);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtEmail.getText().toString().trim();
                String strPwd = mEtPwd.getText().toString().trim();
                String strPwdCheck = mEtPwdCheck.getText().toString().trim();
                String strName = mEtName.getText().toString().trim();
                String strPhone = mEtPhone.getText().toString().trim();

                //빈칸 알림
                if(strEmail.equals("")){
                    Toast.makeText(RegisterActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strPwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strPwdCheck.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strName.equals("")){
                    Toast.makeText(RegisterActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strPhone.equals("")){
                    Toast.makeText(RegisterActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!(strPhone.length() == 11)){
                    Toast.makeText(RegisterActivity.this, "전화번호를 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(strPwd.equals(strPwdCheck))){
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(isValid(strPwd))){
                    Toast.makeText(RegisterActivity.this, "비밀번호가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            sendVerificationEmail(); // 이메일 발송

                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setName(strName);
                            account.setPhone(strPhone); // 확인 필요함 입력이 안되어 있을 시 빈문자열이 들어가는지 확인

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            // 회원가입 성공 애니메이션
                            TextView txtName = findViewById(R.id.txtOption4);
                            TextView txtPhone = findViewById(R.id.txtOption5);
                            mBtnRegister.setVisibility(View.INVISIBLE);
                            mEtName.setVisibility(View.INVISIBLE);
                            mEtPhone.setVisibility(View.INVISIBLE);
                            txtName.setVisibility(View.INVISIBLE);
                            txtPhone.setVisibility(View.INVISIBLE);
                            aniRegisterSuccess.setVisibility(View.VISIBLE);
                            aniRegisterSuccess.setAnimation(R.raw.register_success);
                            aniRegisterSuccess.playAnimation();
                            aniRegisterSuccess.addAnimatorListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(@NonNull Animator animator) {                                }
                                @Override
                                public void onAnimationEnd(@NonNull Animator animator) {
                                    Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onAnimationCancel(@NonNull Animator animator) {                                }
                                @Override
                                public void onAnimationRepeat(@NonNull Animator animator) {                               }
                            });
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, "이미 회원가입 된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }

}