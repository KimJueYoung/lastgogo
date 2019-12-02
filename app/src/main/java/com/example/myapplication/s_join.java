package com.example.myapplication;

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

public class s_join extends AppCompatActivity {

    String id;
    String pw;
    String na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_join_a);

        Button joinBtn = (Button)findViewById(R.id.join);
        //Button loginBtn = (Button)findViewById(R.id.login);

        final EditText idEt = findViewById(R.id.editText1) ;
        final EditText pwEt = findViewById(R.id.editText2) ;
        final EditText name = findViewById(R.id.editText3) ;



        joinBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                id = idEt.getText().toString();
                pw = pwEt.getText().toString();
                na = name.getText().toString();
                Log.e("myid", id);
                toast();
                new JSONTask().execute("http://192.168.200.152:4000/auth/student");//AsyncTask 시작시킴
            }
        });

        //요처자체가 안가니까 저부분이 문제가있을것같음
        // 아마도 JSONTASK생성자 만들때 안에들어가야하는 값이 피요한거같아
        // 그 이거 그 탈잉이 해준건데
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                id = idEt.getText().toString();
//                pw = pwEt.getText().toString();
//                new JSONTask(s_join.this).execute("http://10.0.2.2:4000/auth/login");//AsyncTask 시작시킴
//            }
//        });


    } //이거 아저씨가 하

    public void toast() {
        Toast.makeText(getApplicationContext(), "학생등록완료", Toast.LENGTH_SHORT).show();
    }

    // 자바 객체생성, 생성자, 메서드 호출, 상속, 예외
    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.e("withpd", "Process");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", id);
                jsonObject.accumulate("password", pw);
                jsonObject.accumulate("name", na);
                Log.e("myid2",id);
                Log.e("mypw",pw);


//                jsonObject.accumulate("email", "hhhhhhh");
//                jsonObject.accumulate("password", "qqqq");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
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

                    //여기까지 같음  !! 인터넷이랑

                    Log.e("withpd", buffer.toString());

                    //여기서 부터 테스트
                    if(buffer.toString().contains("몰랑")) {
//                        Intent intent = new Intent(getApplicationContext(), s_menu.class);
//                        startActivity(intent);
//                        final EditText idEt = findViewById(R.id.teditTex);
//                        id = idEt.getText().toString();
//                        intent.putExtra("idid",id);
                    }

                    else {
                        Log.e("withpd", "일치하지 않습니다!");
                    }
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

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
//            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }
    }
}