package com.example.hp.roughpartial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent openAuto;
                    openAuto = new Intent("com.example.hp.roughpartial.Auto");
                    startActivity(openAuto);
                }

            }
        };
        timer.start();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
