package com.unc.reactionlight;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.unc.reactionlight.service.BluetoothService;

public class ClientInterface {
    private ClientInterface mPosition;
    private String mMacadrr;
    private ToggleButton mSend;
    private EditText mtext;
    private int id;
    private BluetoothService bt;
    private Activity v;
    public View.OnClickListener sendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            send();
        }
    };
    private boolean On;
    public  Runnable mButton = new Runnable() {
        @Override
        public void run() {
            mSend.setText("Send 1");
            mSend.setVisibility(View.VISIBLE);
            mSend.setOnClickListener(sendOnClickListener);
        }
    };

    public ClientInterface(String m, int id, Activity v , BluetoothService bt,Context c) {
        this.On = true;
        this.bt = bt;
        this.v = v;
        String s = "toggleButton" + id;
        this.mMacadrr = m;
        int resID = c.getResources().getIdentifier(s, "id", c.getPackageName());
        this.id = resID;
        mSend = (ToggleButton) v.findViewById(resID);

    }



    private void send(){
        if(this.On) {
            bt.sendMessageString(mMacadrr, "2");
            Log.d("client_inteface","===> Send : "+ mMacadrr + "2");
            this.On = false;
        }else
        {
            bt.sendMessageString(mMacadrr,"1");
            this.On = true;
            Log.d("client_inteface","===> Send : "+ mMacadrr + " 1");
        }


    }

    public ToggleButton getmSend() {
        return mSend;
    }

    public int getId() {
        return id;
    }


}
