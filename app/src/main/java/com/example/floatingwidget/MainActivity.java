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

    Button buttonAddWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddWidget = (Button) findViewById(R.id.button_widget);


        getPermission();

        buttonAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.canDrawOverlays(MainActivity.this))
                    {
                        getPermission();
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, WidgetService.class);
                        startService(intent);
                        finish();
                    }
                }
            }
        });
    }

    public void getPermission()
    {
        //check for alert window permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, 1);
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