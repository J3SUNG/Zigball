package com.example.geonwoo.zigball;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GetData extends AsyncTask<String, String, String> {
    private HashMap<String, String> GData = null;// post data // 우리가 전송할 post data 저장될곳
    public GetData(HashMap<String, String> data) {
        GData = data;
    }

    @Override
    protected String doInBackground(String... params) {  // 접속동안 실행될 코드 (나도 이거 진행방식은 확실히 몰러)

        byte[] result = null;
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
        int TIMEOUT_VALUE = 30000;   // 30, 타임아웃 시간 결정
        try {

            // set up post data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = GData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, GData.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (Exception e) {
            Log.i("kim", e.toString());
        }
        return str;
    }
    @Override
    protected  void onPreExecute(){ // 서버로 접속을 하기전 수행할 코드 작성하는 곳 (ex 로딩창 띄우기)
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {  // 서버로 접속을 하고 난뒤 수행할 코드 작성하는 곳 (ex 로딩창 끄기)
        super.onPostExecute(result);
    }
}


