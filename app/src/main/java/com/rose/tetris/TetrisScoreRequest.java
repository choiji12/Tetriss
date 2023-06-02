package com.rose.tetris;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TetrisScoreRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://wlgur0914.dothome.co.kr/TetrisScore.php";;
    private Map<String, String> map;


    public TetrisScoreRequest(String userID,String userScore, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userScore", userScore);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}