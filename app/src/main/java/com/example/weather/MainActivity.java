package com.example.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.etext);
        tv=findViewById(R.id.tv);
    }

    public void getit(View view) {

        Dtask task = new Dtask();
        try {
            String encodedCityName = URLEncoder.encode(et.getText().toString(), "UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + ",&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(et.getWindowToken(), 0);

        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class Dtask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader raeader = new InputStreamReader(in);
                int data = raeader.read();

                while (data != -1) {
                    char current = (char) data;
                    result = result + current;
                    data = raeader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String message="";
                JSONObject obj = new JSONObject(s);
                String Weatherinfo = obj.getString("weather");
                Log.i("weathercontent", Weatherinfo);
                JSONArray arr = new JSONArray(Weatherinfo);
                for (int i = 0; i < arr.length(); ++i) {
                    JSONObject obj2;
                    obj2 = arr.getJSONObject(i);
                    String main=obj2.getString("main");
                    String description=obj2.getString("description");
                    message="main="+main+"\n"+"description="+description;
                }
                if(message!=""){
                    tv.setText(message);
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
