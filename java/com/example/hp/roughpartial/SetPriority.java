package com.example.hp.roughpartial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SetPriority extends Activity {

    ToggleButton tog;
    private static Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pri);
        tog=(ToggleButton)findViewById(R.id.toggleButton);
        tog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if (tog.isChecked()) {
                    showTwoButtonDialog();
                }
                else {
                    Toast.makeText(SetPriority.this,"Text notifications are activated",Toast.LENGTH_SHORT).show();
                    SharedPreferences editor=getSharedPreferences("prior_file",MODE_PRIVATE);
                    editor.edit().putInt("mode", 0).apply();

                }
            }
        });
    }
    private void showTwoButtonDialog(){
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Change Profile to Outdoor");
        dialogBuilder.setMessage("Change your audio profile setting to Outdoor to activate voice notifications.");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences editor=getSharedPreferences("prior_file",MODE_PRIVATE);
                editor.edit().putInt("mode",1).apply();

                Toast.makeText(SetPriority.this, "Voice notifications are activated", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //flag=1;
                Toast.makeText(SetPriority.this, "Voice notifications are not activated yet.", Toast.LENGTH_SHORT).show();


            }
        });
        AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onPause() {
        super.onPause();
        bundle.putBoolean("ToggleButtonState", tog.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        tog.setChecked(bundle.getBoolean("ToggleButtonState",false));
    }
}
