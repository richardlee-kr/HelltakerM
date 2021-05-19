package com.example.floatingwidget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonAddWidget; //Buttono to Add Widget

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddWidget = (Button) findViewById(R.id.button_widget); //find the button in layout


        getPermission(); //try to get overlay permission

        buttonAddWidget.setOnClickListener(new View.OnClickListener() { //Button Click Event
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