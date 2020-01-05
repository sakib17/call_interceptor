package com.example.instorage_caller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.instorage_caller.R;
import com.example.instorage_caller.activity.PopUpActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public class PhoneStateReceiver extends BroadcastReceiver {

    private boolean isAlreadyShown = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    System.out.println("incomingNumber : "+incomingNumber);
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            System.out.println("Receiver start");
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

                Toast.makeText(context,"Ringing State Number is -",Toast.LENGTH_SHORT).show();

                if(!isAlreadyShown){
                    isAlreadyShown = true;
                    showPopUp(context);
                }
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                isAlreadyShown = false;
                Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                isAlreadyShown = false;
                Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
            }
           // Toast.makeText(context," Receiver start ", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /*private void showPopUp(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.TOP;

        LinearLayout ly = new LinearLayout(context);
        ly.setBackgroundColor(Color.RED);
        ly.setOrientation(LinearLayout.VERTICAL);

        wm.addView(ly, params);
    }*/

    private void showPopUp(Context context){

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
               500,
               400,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;


        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.caller_dialog, null);
        Button button = myView.findViewById(R.id.close_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wm.removeView(myView);
            }
        });

        // Add layout to window manager
        wm.addView(myView, params);
    }
}
