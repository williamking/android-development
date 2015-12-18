package com.example.williamdking.homework_8;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private static final String url = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/" + "getMobileCodeInfo";
    private EditText phone_number;
    private EditText content;
    private static final int UPDATE_CONTENT = 0;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message message)   {
            switch (message.what) {
                case UPDATE_CONTENT:
                    content.setText(message.obj.toString());
                    Toast.makeText(MainActivity.this, "Search Success", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };
    private HttpURLConnection connection = null;

    private void sendRequestHttpURLConnection() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = (HttpURLConnection)((new URL(url.toString() + "?mobileCode=" + phone_number.getText().toString() + "&userID=").openConnection()));

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(40000);
                    connection.setReadTimeout(40000);

                    //DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                    //out.writeBytes("mobileCode=" + phone_number.getText().toString() + "&userID=");

//                    if (connection.getResponseCode() == 200) {
//                        handler.hand
//                    } else {
//                        handler.pp("Failed");
//                    }

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Message message = new Message();

                    message.what = UPDATE_CONTENT;
                    message.obj = parseXMLWithPull(response.toString());
                    handler.sendMessage(message);

                }  catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        })).start();
    }

    private String parseXMLWithPull(String xml) {
        String str = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            str = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone_number = (EditText)findViewById(R.id.pnumber);
        content = (EditText)findViewById(R.id.content);

        Button search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestHttpURLConnection();
            }
        });
    }
}
