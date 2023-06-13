package com.example.geonwoo.zigball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    Button btn_set;
    Button btn_run;
    Button btn_set_ip;
    TextView text_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_set = (Button) findViewById(R.id.btn_set);
        btn_run = (Button) findViewById(R.id.btn_run);
        btn_set_ip = (Button) findViewById(R.id.btn_set_ip);
        text_ip = (TextView) findViewById(R.id.text_ip);

        btn_set.setOnClickListener(new View.OnClickListener() { //버튼이벤트 익명메소드로 작성
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), SetActivity.class); //다음 화면으로 넘기는 코드
                    startActivity(intent); //다음화면으로 넘기는 코드
                } catch (Exception e) {
                    Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                }
            }
        });
        btn_run.setOnClickListener(new View.OnClickListener() { //버튼이벤트 익명메소드로 작성
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), RunActivity.class); //다음 화면으로 넘기는 코드
                    startActivity(intent); //다음화면으로 넘기는 코드
                } catch (Exception e) {
                    Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                }
            }
        });
        btn_set_ip.setOnClickListener(new View.OnClickListener() { //버튼이벤트 익명메소드로 작성
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class); //다음 화면으로 넘기는 코드
                    startActivity(intent); //다음화면으로 넘기는 코드
                } catch (Exception e) {
                    Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                }
            }
        });
    }
}
