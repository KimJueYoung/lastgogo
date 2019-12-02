package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

public class p_class extends AppCompatActivity {

    String str;

    int size;

    String check;

    TextView[] tx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_class_a);

        Intent intent = getIntent();

        String test[] = intent.getExtras().getStringArray("test");
        str = intent.getStringExtra("str");
        size = intent.getIntExtra("size", 1);

        tx = new TextView[size];
        final Button[] bt = new Button[size];

        LinearLayout aa = (LinearLayout)findViewById(R.id.aa);
        LinearLayout bb = (LinearLayout)findViewById(R.id.bb);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0 ; i <= (size-1); i++) {
            tx[i] = new TextView(this);
            tx[i].setText(test[i]);
            tx[i].setLayoutParams(layoutParams);
            tx[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
            aa.addView(tx[i]);
        }
        for (int i = 0 ; i <= (size-1); i++) {
            bt[i] = new Button(this);
            bt[i].setText("출결현황");
            bt[i].setLayoutParams(layoutParams);
            bt[i].setTag(i);
            bt[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            bb.addView(bt[i]);
            final int cur = i;
            bt[i].setOnClickListener(new View.OnClickListener() { //전화하면서 될까요?
                @Override
                public void onClick(View v) {
                    check = "" + (cur+1);
                    Log.i("good", ""+check);

                    new JSONTask().execute("http://192.168.200.152/auth/last");
                }
            });
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", str);

                int count = Integer.parseInt(check);

                jsonObject.put("class", tx[count-1].getText().toString());

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

                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

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
                        receive[i] = (String)test.get("student_id");
                    }

                    int class_size = classs.size();

                    Intent intent1 = new Intent(getApplicationContext(), p_master.class);
                    intent1.putExtra("size", class_size);
                    intent1.putExtra("test", receive);
                    intent1.putExtra("str", str);
                    //tx[count-1].getText().toString()
                    intent1.putExtra("jjj", tx[count-1].getText().toString()); //주영

                    if(buffer.toString().contains("현황")) {
                        startActivity(intent1);
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
}
