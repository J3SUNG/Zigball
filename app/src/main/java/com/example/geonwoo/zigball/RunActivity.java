package com.example.geonwoo.zigball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RunActivity extends Activity {

    private ListView listview ;
    private ListViewAdapter adapter;
    private ArrayList<Integer> img = new ArrayList<Integer>(); //{R.drawable.switch_3_3,R.drawable.switch_1_2,R.drawable.valve};
    private ArrayList<String> title = new ArrayList<String>(); //{"거실","안방","가스밸브"};
    private ArrayList<String> context = new ArrayList<String>(); //{"0,1,30","0,1,60","90,0,0"};
    private ArrayList<Integer> room = new ArrayList<Integer>();
    static int roomtype = -1;


    public int ImgLoad(int i)
    {
        switch(i){
            case 0:
                return R.drawable.bed;
            case 1:
                return R.drawable.tvtable;
            case 2:
                return R.drawable.kitchen;
            case 3:
                return R.drawable.door;
            //case 5:
             //   return R.drawable.door;
        }
        return R.drawable.tvtable; // 디폴트
    }

    public void test(){
        inputData(ImgLoad(0), "안방", " ", 0);     // 임시 값
        inputData(ImgLoad(1), "거실", " ", 1);     // 임시 값
        inputData(ImgLoad(2), "주방", " ", 2);     // 임시 값
        inputData(ImgLoad(3), "현관", " ", 3);     // 임시 값
    }

    protected void inputData(int img, String title, String context, int roomType){ // 값 입력
        this.img.add(img);
        this.title.add(title);
        this.context.add(context);
        this.room.add(roomType);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        test();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        adapter = new ListViewAdapter();
        listview  = (ListView) findViewById(R.id.List_view); //어뎁터 할당
        listview.setAdapter(adapter); //adapter를 통한 값 전달


        for(int i=0; i<img.size();i++){
            adapter.addVO(ContextCompat.getDrawable(this,img.get(i)),title.get(i),context.get(i),room.get(i));
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {        // 메시지박스
            if(context.isEmpty()){        // 비었으면 실행 X
                return;
            }
            Intent intent = new Intent(getApplicationContext(), DetailRunActivity.class); // 전송
            intent.putExtra("RoomType", room.get(position));
            roomtype = position;
            startActivity(intent);
                //radio.get(position).isChecked();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        try {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); //다음 화면으로 넘기는 코드
                            startActivity(intent); //다음화면으로 넘기는 코드
                        } catch (Exception e) {
                            Log.i("kim", e.toString()); //예외 처리날시 출력 (어디 출력되는지는 난중에 알려줄게)
                        }
                        break;
                    }
                    case R.id.navigation_set: {
                        try {
                            Intent intent = new Intent(getApplicationContext(), SetActivity.class); //다음 화면으로 넘기는 코드
                            startActivity(intent); //다음화면으로 넘기는 코드
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