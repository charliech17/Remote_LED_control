package com.example.joe.remote_led_control;

import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2;
    TextView tt1;
    ImageView iv;
    public HttpURLConnection http;

    String[] var =new String[1];
    String[] var1=new String[1];

    String text1=" ";  //Enter an URL you want to connect to. This  url should turn on the light . For example:     http://192.168.0.104/gpio/1
    String text2=" ";  //Enter an URL you want to connect to. This  url should turn off the light . For example:     http://192.168.0.104/gpio/0



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=(Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        tt1=(TextView)findViewById(R.id.textView);
        iv=(ImageView) findViewById(R.id.imageView2);


/////////////////////////////////////////////Button1/////////////////////////////////////////////
       btn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Thread t1 = new MyThread();
               t1.start();



           }
       });

/////////////////////////////////////////////Button1//////////////////////////////////////////////


 /////////////////////////////////////////////Button2/////////////////////////////////////////////
        btn2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Thread t2 = new MyThread2();
                t2.start();



            }
        });

/////////////////////////////////////////////Button2//////////////////////////////////////////////



    }

    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            String MsgString = (String)message.obj;
            tt1.setText("*網頁回傳訊息:"+"\n\n"+MsgString);
            if(message.arg1==49){
                iv.setImageResource(R.drawable.transtion);
                ((TransitionDrawable) iv.getDrawable()).startTransition(500);
                Toast.makeText(MainActivity.this,"電燈已被開啟",Toast.LENGTH_SHORT).show();
            }else if(message.arg1==48){

                iv.setImageResource(R.drawable.transtion_off);
                ((TransitionDrawable) iv.getDrawable()).startTransition(500);
                Toast.makeText(MainActivity.this,"電燈已被關閉",Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    });

    class MyThread extends Thread {
        public void run(){
            try {
                URL url = new URL(text1);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");

                InputStream input = http.getInputStream();
                byte[] data = new byte[1024];
                int idx = input.read(data);
                String str = new String(data, 0, idx);


                String obj =str;
                Message message;
                message = handler.obtainMessage(1,obj);
                message.arg1=idx;
                handler.sendMessage(message);

                sleep(200);



                input.close();
                http.disconnect();


            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }


    class MyThread2 extends Thread {
        public void run(){
           try{
               URL url = new URL(text2);
               HttpURLConnection http = (HttpURLConnection) url.openConnection();
               http.setRequestMethod("GET");

               InputStream input = http.getInputStream();
               byte[] data = new byte[1024];
               int idx = input.read(data);
               String str = new String(data, 0, idx);
               var1[0] =str;

               String obj =str;
               Message message;
               message = handler.obtainMessage(1,obj);
               message.arg1=idx;
               handler.sendMessage(message);
               sleep(200);


               input.close();
               http.disconnect();
           }catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (ProtocolException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

        }
    }






}

