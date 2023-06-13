package com.example.geonwoo.zigball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailRunActivity extends Activity {

    public int pos = 0;
    Boolean flag_delete = false;
    Boolean flag_modify = false;

    Button btn_delete;
    Button btn_modify;

    private ListView listview;
    private ListViewAdapter adapter;
    private ArrayList<Integer> img = new ArrayList<Integer>(); //{R.drawable.switch_3_3,R.drawable.switch_1_2,R.drawable.valve};
    private ArrayList<String> title = new ArrayList<String>(); //{"거실","안방","가스밸브"};
    private ArrayList<String> context = new ArrayList<String>(); //{"0,1,30","0,1,60","90,0,0"};
    private ArrayList<Integer> room = new ArrayList<Integer>();
    HashMap<String, String> mdata = new HashMap<String, String>();
    String mJsonString = "";

    private int ImgLoad(int i) {
        switch (i) {
            case 0:
                return R.drawable.img_light;
            case 1:
                return R.drawable.valve;
            case 2:
                return R.drawable.doorlock;
        }
        return R.drawable.light; // 디폴트
    }

    protected void inputData(int img, String title, String context, int roomType) { // 값 입력
        this.img.add(img);
        this.title.add(title);
        this.context.add(context);
        this.room.add(roomType);
    }

    private void RunCode() {
        Log.i("kim","1");
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DetailRunActivity.this);
        alert_confirm.setMessage("Do you want to run?").setCancelable(false).setPositiveButton("Run", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mdata.put("mac", "1234");
                    mdata.put("name",title.get(pos));

                    GetData task = new GetData(mdata); //post 정보 넣고
                    mJsonString = task.execute(MainActivity.URL + "teest.php").get();
                    Log.i("kim", mJsonString);
                }
                catch(Exception i) {
                    Log.i("Kim", "RunCode Error : " + i.toString());
                }
                return;
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() { // 취소 눌렀을 시 return
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 'No'
                return;
            }
        });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    private void ModifyCode() {
        AlertDialog.Builder modify_alert = new AlertDialog.Builder(DetailRunActivity.this);
        final EditText modify_text = new EditText(DetailRunActivity.this);      // 메시지 박스
        modify_alert.setTitle("Title Modify");
        modify_alert.setMessage("Input about new Title.");
        modify_alert.setView(modify_text);
        modify_alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = modify_text.getText().toString();

                title.set(pos, value);

                /*for (int i = img.size() - 1; i >= 0; i--) {
                    adapter.delete(i);
                }
                for (int i = 0; i < img.size(); i++) {
                    adapter.addVO(ContextCompat.getDrawable(DetailRunActivity.this, img.get(i)), title.get(i), context.get(i), room.get(i));
                }*/

                try{
                    mdata.clear();
                    mJsonString="";
                    mdata.put("pre_name",title.get(pos));
                    mdata.put("post_name",value);
                    mdata.put("roomtype",Integer.toString(RunActivity.roomtype));//유일성이 보장되게 하려고 넣음

                    GetData task = new GetData(mdata); //post 정보 넣고
                    mJsonString = task.execute(MainActivity.URL + "update_db.php").get();
                    Log.i("kim",mJsonString);

                    showResult(RunActivity.roomtype);

                    for (int i = 0; i < img.size(); i++) {
                        try{
                            adapter.delete(i);
                            adapter.addVO(ContextCompat.getDrawable(DetailRunActivity.this, ImgLoad(img.get(i))), title.get(i), context.get(i), room.get(i));
                            //ContextCompat.getd
                        }catch (Exception e){
                            Log.i("kim",e.toString());
                        }
                    }

                }catch (Exception e){
                    Log.i("kim",e.toString());
                }

                adapter.notifyDataSetChanged();
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
    }

    private void DeleteCode() {
        AlertDialog.Builder alert_delete = new AlertDialog.Builder(DetailRunActivity.this);
        alert_delete.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // 확인 눌렀을 시 삭제

                try{
                    mdata.clear();
                    mJsonString="";
                    mdata.put("name",title.get(pos));
                    mdata.put("roomtype",Integer.toString(RunActivity.roomtype));

                    GetData task = new GetData(mdata); //post 정보 넣고
                    mJsonString = task.execute(MainActivity.URL + "delete_db.php").get();
                    Log.i("kim",mJsonString);
                }catch (Exception e){
                    Log.i("kim",e.toString());
                }

                adapter.delete(pos);
                img.remove(pos);
                context.remove(pos);
                title.remove(pos);
                adapter.notifyDataSetChanged();

                return;
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // 취소 눌렀을 시 return
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 'No'
                return;
            }
        });
        AlertDialog alert = alert_delete.create();
        alert.show();
        // pattern Index PHP로 보내서 삭제, index == position
    }

    private void showResult(int roomtype) {
        String TAG_JSON = "result";
        String TAG_MAC = "mac";
        String TAG_FUNC = "func";
        String TAG_NAME = "name";
        String TAG_IMAGE_TYPE = "img_type";
        String mac = "";
        String func = "";
        String name = "";
        String img_type = "";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                mac = item.getString(TAG_MAC);
                func = item.getString(TAG_FUNC);
                name = item.getString(TAG_NAME);
                img_type = item.getString(TAG_IMAGE_TYPE);


                inputData(Integer.parseInt(img_type), name, func, roomtype);

            }

            //test(roomType);

        } catch (Exception e) {
            Log.i("kim", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrun);
        try{
            mdata.clear();
            mJsonString="";
            mdata.put("roomtype", Integer.toString(RunActivity.roomtype));
            GetData task = new GetData(mdata); //post 정보 넣고
            // 에러
            Log.i("kim",MainActivity.URL + "select_all_db.php");
            mJsonString = task.execute(MainActivity.URL + "select_all_db.php").get(); //

            Log.i("kim", Integer.toString(RunActivity.roomtype) + "6");


            showResult(RunActivity.roomtype);
            Intent intent = getIntent();
            int roomType = intent.getExtras().getInt("RoomType");

            adapter = new ListViewAdapter();
            listview = (ListView) findViewById(R.id.List_view); //어뎁터 할당
            listview.setAdapter(adapter); //adapter를 통한 값 전달

            for (int i = 0; i < img.size(); i++) {
                    try{
                        adapter.addVO(ContextCompat.getDrawable(DetailRunActivity.this, ImgLoad(img.get(i))), title.get(i), context.get(i), room.get(i));
                        //ContextCompat.getd
                    }catch (Exception e){
                        Log.i("kim",e.toString());
                    }
            }

        }catch (Exception e){
            Log.i("kim",e.toString());
        }

        btn_delete = (Button) findViewById(R.id.btn_delete); // 삭제 버튼
        btn_modify = (Button) findViewById(R.id.btn_modify); // 수정 버튼
        btn_delete.setText("DELETE");
        btn_modify.setText("MODIFY");

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.isEmpty()) {        // 비었으면 실행 X
                    return;
                }
                if (flag_modify == true) {
                    btn_modify.setBackgroundColor(Color.rgb(200, 200, 200));
                    flag_modify = false;
                } else {
                    btn_modify.setBackgroundColor(Color.rgb(0, 150, 150));
                    btn_delete.setBackgroundColor(Color.rgb(200, 200, 200));
                    flag_modify = true;
                    flag_delete = false;
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.isEmpty()) {        // 비었으면 실행 X
                    return;
                }
                if (flag_delete == true) {
                    btn_delete.setBackgroundColor(Color.rgb(200, 200, 200));
                    flag_delete = false;
                } else {
                    btn_modify.setBackgroundColor(Color.rgb(200, 200, 200));
                    btn_delete.setBackgroundColor(Color.rgb(255, 100, 100));
                    flag_delete = true;
                    flag_modify = false;
                }
            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {        // 메시지박스
                pos = position;

                // run
                if (context.isEmpty()) {        // 비었으면 실행 X
                    return;
                }
                if (flag_delete == false && flag_modify == false) {
                    RunCode();
                }
                // modify
                else if (flag_modify == true) {
                    ModifyCode();
                }
                // delete
                else if (flag_delete == true) {
                    DeleteCode();
                }
            }
        });
    }
}