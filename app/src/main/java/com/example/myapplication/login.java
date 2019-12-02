package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

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

public class login extends AppCompatActivity {

    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_a);

        Button loginBtn = (Button)findViewById(R.id.login);

        final EditText idEt = findViewById(R.id.editText1) ;
        final EditText pwEt = findViewById(R.id.editText2) ;

        toast();

        Button student = (Button)findViewById(R.id.student);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //클릭되기를 기다림 (join 버튼)
                Intent intent = new Intent(getApplicationContext(), s_join.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) { //클릭되기를 기다림 (login 버튼)
                login.this.id = idEt.getText().toString();
                pw = pwEt.getText().toString();
                new JSONTask(login.this).execute("http://192.168.200.152:4000/auth/login");//AsyncTask 시작시킴
            }
        });
}

    public void toast() {
        Toast.makeText(getApplicationContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
    }

    public class JSONTask extends AsyncTask<String, String, String>{

        login context;

        public JSONTask(login context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("withpd", "Process");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", id);
                jsonObject.accumulate("password", pw);

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

                    if(buffer.toString().contains("로그인")) { //로그인이 버퍼에 포함될경우 학생페이지로 넘어감
                        Intent intent = new Intent(getApplicationContext(), s_menu.class);
                        intent.putExtra("iddata", id); //테스트
                        startActivity(intent);
                    }
                    else if(buffer.toString().contains("교수")) { //교수가 버퍼에 포함될경우 교수페이지로 넘어감
                        Intent intent2 = new Intent(getApplicationContext(), p_menu.class);
                        intent2.putExtra("iddata", id);
                        startActivity(intent2);
                    }

                  if(buffer.toString().contains("반갑습니다")) {
                        Log.e("withpd", "TOAST");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "환영합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();

                    } else {
                        Log.e("withpd", "일치하지 않습니다!");
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


    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }


}
