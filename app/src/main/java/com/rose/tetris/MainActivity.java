package com.rose.tetris;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.rose.tetris.models.GameModelFactory;
import com.rose.tetris.models.GameType;
import com.rose.tetris.presenter.GamePresenter;
import com.rose.tetris.presenter.GameTurn;
import com.rose.tetris.request.TetrisAllRequest;
import com.rose.tetris.request.TetrisRequest;
import com.rose.tetris.request.TetrisScoreRequest;
import com.rose.tetris.views.GameFrame;
import com.rose.tetris.views.GameViewFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String userID;

    private Button myScore;
    private Button allScore;
    private Button storeScore;
    private TextView gameScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
        userID = getIntent().getStringExtra("userID");
        TextView userText = findViewById(R.id.userId);
        userText.setText(userID);


        gameScore = findViewById(R.id.game_score);
        myScore = findViewById(R.id.myScore);
        allScore = findViewById(R.id.allScore);
        storeScore = findViewById(R.id.storeScore);

        storeScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gscore = gameScore.getText().toString();
                String scoreValue = gscore.replaceAll("\\D+", "");
                Log.d("scor","seer"+scoreValue);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Log.d("dkss","sdasda");
                                Toast.makeText(getApplicationContext(),"점수가 등록되었습니다.",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(),"점수등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                // 서버로 Volley를 이용해서 요청을 하는 코드
                TetrisScoreRequest tetrisScoreRequest = new TetrisScoreRequest(userID, scoreValue, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(tetrisScoreRequest);

            }
        });


        myScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업을 띄우는 코드
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_myscore, null);
                builder.setView(popupView);

                TextView[] textViewArray = new TextView[10];
                textViewArray[0] = popupView.findViewById(R.id.textViewUser1);
                textViewArray[1] = popupView.findViewById(R.id.textViewUser2);
                textViewArray[2] = popupView.findViewById(R.id.textViewUser3);
                textViewArray[3] = popupView.findViewById(R.id.textViewUser4);
                textViewArray[4] = popupView.findViewById(R.id.textViewUser5);
                textViewArray[5] = popupView.findViewById(R.id.textViewUser6);
                textViewArray[6] = popupView.findViewById(R.id.textViewUser7);
                textViewArray[7] = popupView.findViewById(R.id.textViewUser8);
                textViewArray[8] = popupView.findViewById(R.id.textViewUser9);
                textViewArray[9] = popupView.findViewById(R.id.textViewUser10);

                TextView[] textScoreArray = new TextView[10];
                textScoreArray[0] = popupView.findViewById(R.id.textViewScore1);
                textScoreArray[1] = popupView.findViewById(R.id.textViewScore2);
                textScoreArray[2] = popupView.findViewById(R.id.textViewScore3);
                textScoreArray[3] = popupView.findViewById(R.id.textViewScore4);
                textScoreArray[4] = popupView.findViewById(R.id.textViewScore5);
                textScoreArray[5] = popupView.findViewById(R.id.textViewScore6);
                textScoreArray[6] = popupView.findViewById(R.id.textViewScore7);
                textScoreArray[7] = popupView.findViewById(R.id.textViewScore8);
                textScoreArray[8] = popupView.findViewById(R.id.textViewScore9);
                textScoreArray[9] = popupView.findViewById(R.id.textViewScore10);
                Response.Listener<String> infoResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray tuples = jsonObject.getJSONArray("tuples"); // 튜플 배열을 가져옵니다.


                            if (success) {
                                for (int i = 0; i < tuples.length(); i++) {
                                    JSONObject tuple = tuples.getJSONObject(i);
                                    String ID= tuple.getString("userID");
                                    String userScore = tuple.getString("userScore");
                                    TextView textID = textViewArray[i];
                                    textID.setText(ID);
                                    TextView textScore = textScoreArray[i];
                                    textScore.setText(userScore);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                TetrisRequest tetrisRequest = new TetrisRequest(userID, infoResponseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(tetrisRequest);

                // 확인 버튼을 눌렀을 때 팝업 닫기
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 팝업 닫기
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        allScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업을 띄우는 코드
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_allscore, null);
                builder.setView(popupView);

                TextView[] textViewArray = new TextView[10];
                textViewArray[0] = popupView.findViewById(R.id.textViewUser1);
                textViewArray[1] = popupView.findViewById(R.id.textViewUser2);
                textViewArray[2] = popupView.findViewById(R.id.textViewUser3);
                textViewArray[3] = popupView.findViewById(R.id.textViewUser4);
                textViewArray[4] = popupView.findViewById(R.id.textViewUser5);
                textViewArray[5] = popupView.findViewById(R.id.textViewUser6);
                textViewArray[6] = popupView.findViewById(R.id.textViewUser7);
                textViewArray[7] = popupView.findViewById(R.id.textViewUser8);
                textViewArray[8] = popupView.findViewById(R.id.textViewUser9);
                textViewArray[9] = popupView.findViewById(R.id.textViewUser10);

                TextView[] textScoreArray = new TextView[10];
                textScoreArray[0] = popupView.findViewById(R.id.textViewScore1);
                textScoreArray[1] = popupView.findViewById(R.id.textViewScore2);
                textScoreArray[2] = popupView.findViewById(R.id.textViewScore3);
                textScoreArray[3] = popupView.findViewById(R.id.textViewScore4);
                textScoreArray[4] = popupView.findViewById(R.id.textViewScore5);
                textScoreArray[5] = popupView.findViewById(R.id.textViewScore6);
                textScoreArray[6] = popupView.findViewById(R.id.textViewScore7);
                textScoreArray[7] = popupView.findViewById(R.id.textViewScore8);
                textScoreArray[8] = popupView.findViewById(R.id.textViewScore9);
                textScoreArray[9] = popupView.findViewById(R.id.textViewScore10);
                Response.Listener<String> allResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray tuples = jsonObject.getJSONArray("tuples"); // 튜플 배열을 가져옵니다.




                            if (success) {
                                for (int i = 0; i < tuples.length(); i++) {
                                    JSONObject tuple = tuples.getJSONObject(i);
                                    String ID= tuple.getString("userID");
                                    String userScore = tuple.getString("userScore");
                                    TextView textID = textViewArray[i];
                                    textID.setText(ID);
                                    TextView textScore = textScoreArray[i];
                                    textScore.setText(userScore);



                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                TetrisAllRequest tetrisAllRequest= new TetrisAllRequest(userID, allResponseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(tetrisAllRequest);

                // 확인 버튼을 눌렀을 때 팝업 닫기
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 팝업 닫기
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        GameFrame gameFrame = findViewById(R.id.game_container);
        TextView gameScoreText = findViewById(R.id.game_score);
        TextView gameStatusText = findViewById(R.id.game_status);
        Button gameCtlBtn = findViewById(R.id.game_ctl_btn);

        GamePresenter gamePresenter = new GamePresenter();
        gamePresenter.setGameModel(GameModelFactory.newGameModel(GameType.TETRIS));
        gamePresenter.setGameView(GameViewFactory.newGameView(gameFrame, gameScoreText, gameStatusText, gameCtlBtn));

        String score = String.valueOf(gameScoreText.getText());
        Log.d("score","score"+score);

        Button upBtn = findViewById(R.id.up_btn);
        Button downBtn = findViewById(R.id.down_btn);
        Button leftBtn = findViewById(R.id.left_btn);
        Button rightBtn = findViewById(R.id.right_btn);
        Button fireBtn = findViewById(R.id.fire_btn);

        upBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.UP));
        downBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.DOWN));
        leftBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.LEFT));
        rightBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.RIGHT));
        fireBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.FIRE));

        gameCtlBtn.setOnClickListener(v -> gamePresenter.changeStatus());
        Log.d("score1","score1"+score);
        gamePresenter.init();
        Log.d("score2","score2"+score);



    }

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }

}

