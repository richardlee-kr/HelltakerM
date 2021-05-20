package com.example.floatingwidget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SeekBar frame_bar; //frame speed controller
    TextView frame_text; //frame speed text

    Spinner character_select; //dropdown menu

    Button addButtonWidget; //Button to Add Widget

    int frameSpeed = 30;
    String character = "Lucifer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame_bar = (SeekBar) findViewById(R.id.frame_speed);
        frame_text = (TextView) findViewById(R.id.frame_speed_text);

        character_select = (Spinner) findViewById(R.id.character_list);

        addButtonWidget = (Button) findViewById(R.id.button_widget); //find the button in layout

        frame_bar.incrementProgressBy(1);

        getPermission(); //try to get overlay permission

        frame_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 0)
                {
                    progress = 1;
                }
                frame_text.setText("FrameSpeed: "+progress);
                frameSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        character_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                character = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addButtonWidget.setOnClickListener(new View.OnClickListener() { //Button Click Event
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //if current version is higher than Marshmallow (6.0)
                    if(!Settings.canDrawOverlays(MainActivity.this)) //if no overlay permission
                    {
                        getPermission(); //try to get overlay permission
                    }
                    else //if overlay permission
                    {
                        Intent intent = new Intent(MainActivity.this, WidgetService.class); //create intent of WidgetService
                        intent.putExtra("speed", frameSpeed); //frame Speed transmit
                        intent.putExtra("character", character);
                        startService(intent); //start Widget
                        finish(); //end MainActivity
                    }
                }
            }
        });
    }

    public void getPermission()
    {
        //check for alert window permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) //if current version is higher than Marshmallow and on overlay permission
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())); //create permission allow window
            startActivityForResult(intent, 1); //show allow window
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(!Settings.canDrawOverlays(this))
            {
                Toast.makeText(this, "Permssion denied by user.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}