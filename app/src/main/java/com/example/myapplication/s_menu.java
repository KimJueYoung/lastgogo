package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class s_menu extends AppCompatActivity {

    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_menu_a);

        Intent idget = getIntent();

        str = idget.getStringExtra("iddata");

        TextView name = (TextView) findViewById(R.id.studentid);

        name.setText("학생 메뉴");

        Button check = (Button) findViewById(R.id.button1);
        Button sugang = (Button) findViewById(R.id.button2);
        Button myclass = (Button) findViewById(R.id.button3);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask3().execute("http://192.168.200.152:4000/auth/culsuk");//AsyncTask 시작시킴
            }
        });
        sugang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute("http://192.168.200.152:4000/auth/sugang");//AsyncTask 시작시킴
            }
        });
        myclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask2().execute("http://192.168.200.152:4000/auth/myclass");//AsyncTask 시작시킴
            }
        });
    }

    public void toast() {
        Toast.makeText(getApplicationContext(), "학생 로그인 성공", Toast.LENGTH_SHORT).show();
    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("withpd", "Process");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("str", str);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }


                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject2 = (JSONObject)jsonParser.parse(buffer.toString());

                    JSONArray classs = (JSONArray)jsonObject2.get("aa");

                    String receive[] = new String[classs.size()];

                    for (int i=0; i<classs.size(); i++){
                        JSONObject test = (JSONObject)classs.get(i);
                        receive[i] = (String)test.get("class_name");
                    }

                    Intent intent3 = new Intent(getApplicationContext(), s_sugang.class);
                    int class_size = classs.size();
                    intent3.putExtra("size", class_size);
                    intent3.putExtra("test", receive);
                    intent3.putExtra("str", str);

                    if(buffer.toString().contains("수강페이지로")) {
                        startActivity(intent3);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    Log.e("withpd", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    //씨발
    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("withpd", "Process");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("str", str);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject2 = (JSONObject)jsonParser.parse(buffer.toString());

                    JSONArray classs = (JSONArray)jsonObject2.get("aa");

                    String receive[] = new String[classs.size()];

                    for (int i=0; i<classs.size(); i++){
                        JSONObject test = (JSONObject)classs.get(i);
                        receive[i] = (String)test.get("class_name");
                    }

                    Intent intent3 = new Intent(getApplicationContext(), s_class.class);
                    int class_size = classs.size();
                    intent3.putExtra("size", class_size);
                    intent3.putExtra("test", receive);
                    intent3.putExtra("str", str);


                    if(buffer.toString().contains("내가수강하는과목으로")) {
                        startActivity(intent3);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    Log.e("withpd", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class JSONTask3 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("withpd", "Process");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("str", str);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    String kk = buffer.toString();

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject2 = (JSONObject)jsonParser.parse(kk);
                    JSONObject adsf = (JSONObject)jsonParser.parse(buffer.toString());

                    JSONArray classs = (JSONArray)jsonObject2.get("aa");

                    String receive[] = new String[classs.size()];

                    for (int i=0; i<classs.size(); i++){
                        JSONObject test = (JSONObject)classs.get(i);
                        receive[i] = (String)test.get("class_name");
                    }

                    Intent intent3 = new Intent(getApplicationContext(), s_check.class);
                    int class_size = classs.size();
                    intent3.putExtra("size", class_size);
                    intent3.putExtra("test", receive);
                    intent3.putExtra("str", str);


                    if(buffer.toString().contains("출석체크페이지로")) {
                        startActivity(intent3);
                    }

                    //테스트
                    return buffer.toString();

                } catch (MalformedURLException e){
                    Log.e("withpd", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}