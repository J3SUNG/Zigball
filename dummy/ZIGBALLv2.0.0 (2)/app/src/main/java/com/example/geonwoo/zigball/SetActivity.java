package com.example.geonwoo.zigball;

import android.arch.lifecycle.AndroidViewModel;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class SetActivity extends AppCompatActivity {
    ImageView img_circle;       // 원판을 나타내는 원
    ImageView img_dot;          // 스틱을 나타내는 점
    PointF centerOfTheCircle;
    PointF centerOfTheDot;

    String sendText = "";
    ImageButton btn_spin;
    ImageButton btn_Rspin;
    Button btn_push;
    //Button btn_Rmove;
    //Button btn_Lmove;
    Button btn_save;

    RadioGroup group;
    RadioButton radio_switch;
    RadioButton radio_valve;
    RadioButton radio_doorlock;

    RadioGroup roomType;
    RadioButton radio_type1;
    RadioButton radio_type2;
    RadioButton radio_type3;
    RadioButton radio_type4;

    EditText text_spin;

    TextView textPattern;
    TextView textview_add;
    TextView textview_push;
    //TextView textview_move;
    TextView textview_pattern;
    TextView textType;
    TextView textRoomType;

    BottomNavigationView bottomNavigationView;

    //post 방식으로 보내기 위해서 해쉬맵 선언
    HashMap<String, String> mdata = new HashMap<String, String>();
    private int getQuadant(float x1, float y1, float x2, float y2){
        int quadant;
        double x_difference, y_difference;
        x_difference = x2 - x1;
        y_difference = y2 - y1;
        if(x_difference > 0 && y_difference > 0)    quadant = 4;
        else if (x_difference < 0 && y_difference > 0)  quadant = 3;
        else if (x_difference < 0 && y_difference < 0)  quadant = 2;
        else quadant = 1;

        return quadant;
    }
    private double getMoveDegree(float circleX, float circleY, float dotX, float dotY, int degree){
        int _quadant = getQuadant(circleX, circleY, dotX, dotY);


        switch (_quadant){
            case 1:
                return degree;
            case 2:
                return 18 - degree;
            case 3:
                return 18 - degree;
            default:
                return degree - 36;
        }
    }
    private int getMoveDistance(float circleX, float circleY, float dotX, float dotY){
        int _quadant = getQuadant(circleX, circleY, dotX, dotY);
        switch (_quadant){
            case 1:
                return 1;
            case 2:
                return -1;
            case 3:
                return -1;
            default:
                return 1;
        }
    }

    //점 사이 거리 구하는 함수
    double getDistance(float x1, float y1, float x2, float y2) {
        double distance = Math.sqrt(Math.pow(Math.abs(x2 - x1), 2) + Math.pow(Math.abs(y2 - y1), 2));
        return distance;
    }
    //원점과 좌표 사이 거리를 구하는 함수
    double getDegree(double y, double x){
        double degree = -Math.atan2(y,x) * 180 / Math.PI;
        if(0 <= degree && degree <= 180)
            return degree;
        else{
            return degree + 360;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);// 이것도 마찬가지


        img_circle = (ImageView) findViewById(R.id.circle) ;
        img_dot = (ImageView) findViewById(R.id.dot);

        centerOfTheCircle = new PointF();
        centerOfTheDot = new PointF();

        group = (RadioGroup)findViewById(R.id.group);
        radio_doorlock = (RadioButton)findViewById(R.id.radio_doorlock);
        radio_valve = (RadioButton)findViewById(R.id.radio_valve);
        radio_switch = (RadioButton)findViewById(R.id.radio_switch);

        roomType = (RadioGroup)findViewById(R.id.RoomType);
        radio_type1 = (RadioButton)findViewById(R.id.radio_type1);
        radio_type2 = (RadioButton)findViewById(R.id.radio_type2);
        radio_type3 = (RadioButton)findViewById(R.id.radio_type3);
        radio_type4 = (RadioButton)findViewById(R.id.radio_type4);

        radio_type1.setText("안방");
        radio_type2.setText("거실");
        radio_type3.setText("주방");
        radio_type4.setText("현관");

        btn_spin = (ImageButton)findViewById(R.id.btn_spin);
        btn_push = (Button)findViewById(R.id.btn_push);
        btn_Rspin = (ImageButton)findViewById(R.id.btn_Rspin);
        //btn_Rmove = (Button)findViewById(R.id.btn_Rmove);
        //btn_Lmove = (Button)findViewById(R.id.btn_Lmove);
        btn_save = (Button)findViewById(R.id.btn_save);

        //text_spin = (EditText)findViewById(R.id.text_spin);

        textview_add = findViewById(R.id.textview_add);
        textview_push = findViewById(R.id.textview_push);
        //textview_move = findViewById(R.id.textview_move);
        textview_pattern = findViewById(R.id.textview_pattern);
        textPattern = findViewById(R.id.TextPattern);
        textType = findViewById(R.id.textType);
        textRoomType = findViewById(R.id.textRoomType);

        textview_add.setText("ADD");
        textview_push.setText("PUSH");
        //textview_move.setText("MOVE");
        textview_pattern.setText("");
        textPattern.setText("PATTERN :");
        textType.setText("Type");
        textRoomType.setText("RoomType");

        btn_push.setText("PUSH");
        //btn_Lmove.setText("LEFT MOVE");
        //btn_Rmove.setText("RIGHT MOVE");

        btn_save.setText("SAVE");


        /*Intent intent = getIntent();
        if(intent.getExtras()!=null) {
            String context = intent.getExtras().getString("context");
            String[] value = context.split(",");
            text_spin.setText(value[0]);
            text_push.setText(value[1]);
            text_move.setText(value[2]);
        }*/
        //이미지뷰의 크기를 구하기 위해 필요한 부분
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        img_circle.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        img_dot.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final float circleHeight, circleWidth;          //원의 높이와 너비
        final float dotHeight, dotWidth;                //점의 높이와 너비
        final float centerOfCircleX, centerOfCircleY;   //점을 원의 중심에 놓기 위한 변수
        //원과 점의 너비 높이 설정
        circleHeight = img_circle.getMeasuredHeight();
        circleWidth = img_circle.getMeasuredWidth();

        dotHeight = img_dot.getMeasuredHeight();
        dotWidth = img_dot.getMeasuredWidth();
        //점을 원의 중심에 놓기
        centerOfCircleX = circleWidth / 2 - dotWidth/8;
        centerOfCircleY = circleHeight / 2 - dotHeight/8;

        centerOfTheCircle.set(circleWidth / 2, circleHeight / 2);

        img_dot.setX(centerOfCircleX);
        img_dot.setY(centerOfCircleY);
//원을 놓았을때 점의 좌표 이동
        img_circle.setOnTouchListener(new OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event)
            {
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                centerOfTheDot.x = touchX - dotWidth /4;
                centerOfTheDot.y = touchY - dotHeight /4;

                //Log.d("X & Y",touchX +" "+ touchY);
                //Log.d("c height c Width",circleHeight +" "+ circleWidth);
                //Log.d("dot Height & dot Width",dotHeight +" "+ dotWidth);
                //Log.d("X & Y",centerOfCircleX +" "+ centerOfCircleY);
                //Log.d("distance", String.valueOf(getDistance(circleWidth /2, circleHeight / 2, touchX, touchY)));
                //Log.d("degree", String.valueOf(getDegree(touchY - dotHeight/4 - circleHeight/2 , touchX - dotWidth/4 - circleWidth /2)));
                //점이 원을 넘어가지 않게 함
                if(getDistance(centerOfTheCircle.x, centerOfTheCircle.y, touchX, touchY) < centerOfTheCircle.x){
                    img_dot.setX(centerOfTheDot.x);
                    img_dot.setY(centerOfTheDot.y);
                }

                return true; // indicate event was handled
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder modify_alert = new AlertDialog.Builder(SetActivity.this);
                    final EditText modify_text = new EditText(SetActivity.this);      // 메시지 박스
                    modify_alert.setTitle("Title");
                    modify_alert.setMessage("Input about Title.");
                    modify_alert.setView(modify_text);
                    modify_alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String value = modify_text.getText().toString();
                            try {
                                mdata.put("mac", "1234");
                                sendText = sendText.substring(0, sendText.length() - 1);
                                mdata.put("func", sendText); //해쉬맵에 data 라는 이름으로 sendText 전송
                                mdata.put("name", value);
                                if (radio_switch.isChecked()) {
                                    mdata.put("img_type", "0");
                                } else if (radio_valve.isChecked()) {
                                    mdata.put("img_type", "1");
                                } else if (radio_doorlock.isChecked()) {
                                    mdata.put("img_type", "2");
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "이미지중 하나를 선택해주세요.", Toast.LENGTH_LONG)
                                            .show();
                                }
                                if (radio_type1.isChecked()) {
                                    mdata.put("roomtype", "0");  //안방
                                } else if (radio_type2.isChecked()) {
                                    mdata.put("roomtype", "1");  //거실
                                } else if (radio_type3.isChecked()) {
                                    mdata.put("roomtype", "2");  //주방
                                } else if (radio_type4.isChecked()) {
                                    mdata.put("roomtype", "3"); //현관
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "방중 하나를 선택해주세요.", Toast.LENGTH_LONG)
                                            .show();
                                }
                                GetData task = new GetData(mdata); //post 정보 넣고
                                String tmp = task.execute(MainActivity.URL + "insert_db.php").get(); //url로 접속(서버로 접속) , url은 MainActivity class 에서 static 으로 선언한거 가져온거
                                Log.i("kim", tmp);
                            } catch (Exception e) {
                                Log.i("kim", e.toString());
                            }
                            dialog.dismiss();     //닫기
                        }
                    });
                    modify_alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    modify_alert.show();

                } catch (Exception e) {
                    Log.i("kim", e.toString());
                }
            }
        });
        btn_spin.setOnClickListener(new View.OnClickListener() { //여기서부턴 각각 버튼 이벤트 만들어준거(틀만 짜놓는거라 대충 해놨음)

            @Override
            public void onClick(View v) {
                int degree = (int)getDegree(centerOfTheDot.y - centerOfTheCircle.y , centerOfTheDot.x - centerOfTheCircle.x) / 10;
                degree= (int)getMoveDegree(centerOfTheCircle.x, centerOfTheCircle.y, centerOfTheDot.x, centerOfTheDot.y, degree);
                //원점에서 점까지의 거리를 10등분 시킴( 왼쪽 오른쪽 구분 x)
                int distance = getMoveDistance(centerOfTheCircle.x, centerOfTheCircle.y, centerOfTheDot.x, centerOfTheDot.y);
                //distance = (int)getDistance(circleWidth /2, circleHeight /2, img_dot.getX() - dotWidth/4,img_dot.getY() - dotHeight/4)
                //       / (img_circle.getMeasuredWidth()/20);
                try {
                    sendText += "H" + distance +"," + "S" + degree + "," ; // ooo,ooo,ooo, 이런식으로 보낼예정
                    textview_pattern.setText(sendText);
                    img_dot.setX(centerOfCircleX);
                    img_dot.setY(centerOfCircleY);
                } catch (Exception e) {
                    Log.i("kim", e.toString());
                }
            }
        });
        btn_Rspin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int degree = (int)getDegree(centerOfTheDot.y - centerOfTheCircle.y , centerOfTheDot.x - centerOfTheCircle.x) / 10;
                degree = (int)getMoveDegree(centerOfTheCircle.x, centerOfTheCircle.y, centerOfTheDot.x, centerOfTheDot.y, degree);
                //원점에서 점까지의 거리를 10등분 시킴( 왼쪽 오른쪽 구분 x)
                int distance = getMoveDistance(centerOfTheCircle.x, centerOfTheCircle.y, centerOfTheDot.x, centerOfTheDot.y);
                //distance = (int)getDistance(circleWidth /2, circleHeight /2, img_dot.getX() - dotWidth/4,img_dot.getY() - dotHeight/4)
                //       / (img_circle.getMeasuredWidth()/20);
                try {
                    sendText += "H" + distance +"," + "S" + degree + "," ; // ooo,ooo,ooo, 이런식으로 보낼예정
                    textview_pattern.setText(sendText);
                    img_dot.setX(centerOfCircleX);
                    img_dot.setY(centerOfCircleY);
                } catch (Exception e) {
                    Log.i("kim", e.toString());
                }
            }
        });
        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                sendText += "P1,"; // ooo,ooo,ooo, 이런식으로 보낼예정
                textview_pattern.setText(sendText);
            } catch (Exception e) {
                Log.i("kim", e.toString());
            }
            }
        });
/*
        btn_Rmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                sendText += "H1,"; // ooo,ooo,ooo, 이런식으로 보낼예정
                textview_pattern.setText(sendText);
            } catch (Exception e) {
                Log.i("kim", e.toString());
            }
            }
        });

        btn_Lmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendText += "H0,"; // ooo,ooo,ooo, 이런식으로 보낼예정
                    textview_pattern.setText(sendText);
                } catch (Exception e) {
                    Log.i("kim", e.toString());
                }
            }
        });
*/

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        try {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); //다음 화면으로 넘기는 코드
                            startActivity(intent); //다음화면으로 넘기는 코드
                            finish();
                        } catch (Exception e) {
                            Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                        }
                        break;
                    }
                    case R.id.navigation_run: {
                        try {
                            Intent intent = new Intent(getApplicationContext(), RunActivity.class); //다음 화면으로 넘기는 코드
                            startActivity(intent); //다음화면으로 넘기는 코드
                            finish();
                        } catch (Exception e) {
                            Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                        }
                        break;
                    }
                }

                return true;
            }
        });
    }
}
