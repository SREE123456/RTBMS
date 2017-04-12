package com.example.hp.roughpartial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ChangeStop extends Activity {
    String[] stops = { "Vadakke Stand", "Sakthan Stand", "Naduvilal", "Kuruppam Road",
            "Kairali Sree Theatre Complex", "Sankarankulangara Temple", "Kerala Varma College",
            "Sankaraiyyar Road", "Punkunnam", "Paaturaikkal", "East Fort", "Municipal Corporation",
            "Paramekkavu Temple", "Power House","Ashwini Junction","LH Block","MG Road",
            "Railway Station","Sapna Theatre"  };
    protected Button submit;
    Intent i;
    String gotBread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_stop);
        Bundle gotBasket = getIntent().getExtras();
        gotBread = gotBasket.getString("key");

        final TextView warn = (TextView) findViewById(R.id.warning);
        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        actv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_detail, stops));
        submit = (Button) findViewById(R.id.submit);
        Toast.makeText(ChangeStop.this,
                "Yor initial stop will be rejected while you attempt to change.",
                Toast.LENGTH_SHORT).show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = actv.getText().toString();
                if (check.contentEquals("Vadakke Stand") || check.contentEquals("Sakthan Stand") ||
                        check.contentEquals("Kairali Sree Theatre Complex") || check.contentEquals("Kuruppam Road")
                        || check.contentEquals("Sankarankulangara Temple") || check.contentEquals("Kerala Varma College")
                        || check.contentEquals("Sankaraiyyar Road") || check.contentEquals("Punkunnam")
                        || check.contentEquals("Paaturaikkal") || check.contentEquals("East Fort")||check.contentEquals("Naduvilal")
                        || check.contentEquals("Municipal Corporation") || check.contentEquals("Paramekkavu Temple")
                        || check.contentEquals("PowerHouse")|| check.contentEquals("LH Block")
                        || check.contentEquals("Ashwini Junction")|| check.contentEquals("MG Road")
                        || check.contentEquals("Sapna Theatre")|| check.contentEquals("Railway Station")) {

                    if (check.contentEquals(gotBread)) {
                        warn.setText("Previously entered stop. Try again.");
                        warn.setGravity(Gravity.CENTER);
                        warn.setTextColor(Color.RED);
                    } else {
                        warn.setText(" ");
                        warn.setGravity(Gravity.CENTER);
                        Bundle basket = new Bundle();
                        basket.putString("key", check);
                        i = new Intent(getApplicationContext(), Navigation.class);
                        i.putExtras(basket);
                        startActivity(i);
                    }
                }
                else {
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
