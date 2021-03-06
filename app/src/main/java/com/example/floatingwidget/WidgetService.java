package com.example.floatingwidget;

import android.app.Service;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class WidgetService extends Service {

    int LAYOUT_FLAG;
    View mFloatingView;
    WindowManager windowManager;
    ImageView imageClose;

    ImageView img;
    AnimationDrawable ani_image;

    float height, width;

    int duration = 25;
    String selected;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //if current version is higher than Oreo
        {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else
        {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //inflate widget layout

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget,null);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //initial position
        layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        //layout params for close button
        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,
                140,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        imageParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
        imageParams.y = 100;

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        imageClose = new ImageView(this);
        imageClose.setImageResource(R.drawable.close_white);
        imageClose.setVisibility(View.INVISIBLE);
        windowManager.addView(imageClose, imageParams);
        windowManager.addView(mFloatingView, layoutParams);
        mFloatingView.setVisibility(View.VISIBLE);

        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();

        duration = 1000/(intent.getIntExtra("speed", -1));
        selected = intent.getStringExtra("character");

        img = (ImageView)mFloatingView.findViewById((R.id.gif_image));

        ani_image = new AnimationDrawable();

        //Log.e("character:",selected);
        switch(selected)
        {
            case "Azazel":
                selectAzazel();
                break;
            case "Cerberus":
                selectCerberus();
                break;
            case "Judgement":
                selectJudgement();
                break;
            case "Justice":
                selectJustice();
                break;
            case "Lucifer":
                selectLucifer();
                break;
            case "Lucifer_Apron":
                selectLucifer_Apron();
                break;
            case "Malina":
                selectMalina();
                break;
            case "Modeus":
                selectModeus();
                break;
            case "Pandemonica":
                selectPandemonica();
                break;
            case "Zdrada":
                selectZdrada();
                break;
        }


        img.setBackgroundDrawable(ani_image);
        ani_image.start();


        //show&update current time in textview
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //tvWidget.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                handler.postDelayed(this,1000);
            }
        }, 10);

        //drag movement for widget
        img.setOnTouchListener(new View.OnTouchListener() {
            int initialX, initialY;
            float initialTouchX, initialTouchY;
            long startClickTime;

            int MAX_CLICK_DURATION=200;

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        imageClose.setVisibility(View.VISIBLE);

                        initialX = layoutParams.x;
                        initialY = layoutParams.y;

                        //touch posision
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                        return true;
                    case MotionEvent.ACTION_UP:
                        long clickDuration = Calendar.getInstance().getTimeInMillis()-startClickTime;
                        imageClose.setVisibility(View.GONE);

                        layoutParams.x = initialX+(int)(initialTouchX-motionEvent.getRawX());
                        layoutParams.y = initialY+(int)(motionEvent.getRawY()-initialTouchY);

                        if(clickDuration < MAX_CLICK_DURATION)
                        {
                            //Toast.makeText(WidgetService.this, "Time: "+tvWidget.getText().toString(),Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //remove widget
                            if(layoutParams.y>(height*0.6))
                            {
                                stopSelf();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //calculate x&Y coordinates of view
                        layoutParams.x = initialX+(int)(initialTouchX-motionEvent.getRawX());
                        layoutParams.y = initialY+(int)(motionEvent.getRawY()-initialTouchY);

                        //update layout with new coordinates
                        windowManager.updateViewLayout(mFloatingView, layoutParams);
                        if(layoutParams.y>(height*0.6))
                        {
                            imageClose.setImageResource(R.drawable.close_white);
                        }
                        else
                        {
                            imageClose.setImageResource(R.drawable.close_white);
                        }
                        return true;
                }
                return false;
            }
        });

        return START_STICKY;
    }

    private void selectAzazel() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.azazel);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectCerberus() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.cerberus);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectJudgement() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.judgement);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectJustice() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.justice);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectLucifer() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.lucifer);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectLucifer_Apron() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.lucifer_apron);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectMalina() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.malina);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectModeus() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.modeus);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectPandemonica() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.pandemonica);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }

    private void selectZdrada() {

        TypedArray typedArray = getResources().obtainTypedArray(R.array.zdrada);

        for (int i =0; i< 12; i++)
        {
            ani_image.addFrame(typedArray.getDrawable(i),duration);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null)
        {
            windowManager.removeView(mFloatingView);
        }

        if(imageClose!=null)
        {
            windowManager.removeView(imageClose);
        }
    }
}
