package com.example.williamdking.homework_9;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private EditText confirmCode;
    private Button create;
    private static final int UPDATE = 0;
    private ImageView image;
    private ProgressDialog progressDialog = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE:
                    progressDialog.cancel();
                    byte[] data = Base64.decode((message.obj.toString()).getBytes(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ImageView image = (ImageView)findViewById(R.id.image);
                    image.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }
        }
    };

    public class Download implements Runnable {
        private final String NAMESPACE = "http://WebXml.com.cn/";
        private final String METHODNAME = "enValidateByte";
        private final String SOAPACTION = "http://WebXml.com.cn/enValidateByte";
        private final String URL = "http://webservice.webxml.com.cn/WebServices/ValidateCodeWebService.asmx";
        Download() {
            super();
        }
        @Override
        public void run() {
            SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
            //Log.e("str", MainActivity.str);
            request.addProperty("byString", ((EditText)findViewById(R.id.confirm_code)).getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(URL, 120000);
            try {
                transportSE.call(SOAPACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("fault", envelope.bodyIn.toString());
            SoapObject result = (SoapObject)envelope.bodyIn;
            SoapPrimitive detail = (SoapPrimitive)result.getProperty("enValidateByteResult");

            Message message = new Message();
            message.what = UPDATE;
            message.obj = detail;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmCode = (EditText)findViewById(R.id.confirm_code);
        create = (Button)findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendHttpRequest();
                if (progressDialog == null) progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Requesting");
                progressDialog.setMessage("requesting....");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        });
    }

    private void sendHttpRequest() {
        (new Thread(new Download())).start();
    }
}
