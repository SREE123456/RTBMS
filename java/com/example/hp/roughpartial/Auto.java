package com.example.hp.roughpartial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;


public class Auto extends Activity {
    String[] stops = {"Vadakke Stand", "Sakthan Stand", "Naduvilal", "Kuruppam Road",
            "Kairali Sree Theatre Complex", "Sankarankulangara Temple", "Kerala Varma College",
            "Sankaraiyyar Road", "Punkunnam", "Paaturaikkal", "East Fort", "Municipal Corporation",
            "Paramekkavu Temple", "Power House","Ashwini Junction","LH Block","MG Road",
            "Railway Station","Sapna Theatre"
    };
    protected Button submit;
    Intent i;
    String bread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);

// Check if onboarding_complete is false
        if(!preferences.getBoolean("onboarding_complete",false)) {
            // Start the onboarding Activity
            Intent onboarding = new Intent(this, Onboarding.class);
            startActivity(onboarding);

            // Close the main Activity
            finish();
            return;
        }
        setContentView(R.layout.auto);
        final TextView warn = (TextView) findViewById(R.id.warning);
        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        actv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_detail, stops));
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = actv.getText().toString();
                if (check.contentEquals("Vadakke Stand") || check.contentEquals("Sakthan Stand") ||
                        check.contentEquals("Kairali Sree Theatre Complex") || check.contentEquals("Kuruppam Road")
                        || check.contentEquals("Sankarankulangara Temple") || check.contentEquals("Kerala Varma College")
                        || check.contentEquals("Sankaraiyyar Road") || check.contentEquals("Punkunnam")
                        || check.contentEquals("Paaturaikkal") || check.contentEquals("East Fort")
                        || check.contentEquals("Municipal Corporation") || check.contentEquals("Paramekkavu Temple")
                        || check.contentEquals("PowerHouse")|| check.contentEquals("LH Block")
                        || check.contentEquals("Ashwini Junction")|| check.contentEquals("MG Road")||check.contentEquals("Naduvilal")
                        || check.contentEquals("Sapna Theatre")|| check.contentEquals("Railway Station")) {
                    warn.setText(" ");
                    warn.setGravity(Gravity.CENTER);
                    //Intent i;
                    //SharedPreferences editor=getSharedPreferences("prior_file",MODE_PRIVATE);
                    //editor.edit().putInt("mode", 0).apply();

                    Bundle basket = new Bundle();
                    basket.putString("key", check);

                    i = new Intent(getApplicationContext(), Navigation.class);
                    i.putExtras(basket);
                    startActivity(i);
                    //i= new Intent("com.example.hp.rough.Splash");
                    //startActivity(i);

                } else {
                    warn.setText("Invalid Stop!");
                    warn.setGravity(Gravity.CENTER);
                    warn.setTextColor(Color.RED);

                }

            }
        });
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}