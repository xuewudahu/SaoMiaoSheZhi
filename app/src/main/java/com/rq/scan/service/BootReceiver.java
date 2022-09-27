package com.rq.scan.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import android.util.Log;

import androidx.annotation.RequiresApi;

import cn.rq.pda.sdk.RqPDAClient;


public class BootReceiver extends BroadcastReceiver {
    public RqPDAClient client;
    Context mContext = null;
    boolean isStarted = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
	@Override
    public void onReceive(Context context, Intent mIntent) {
        String action = mIntent.getAction();
        mContext = context;
        client = RqPDAClient.newClient(mContext);
        Log.e("scanner1","----"+action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals("android.intent.action.USER_PRESENT")) {   //Intent.ACTION_BOOT_COMPLETED   "android.intent.action.BOOT_COMPLETED" ||action.equals("com.android.receive_scan_action")

            Log.e("scan", "receiver BOOT_COMPLETED isStarted:=" + isStarted + " action:= " + action);
            if (!isStarted) {
                isStarted = true;
                Intent intent = new Intent();
                intent.setAction("com.qxj.scan.service.start");
                intent.setPackage("com.rq.scan");
                //mContext.startService(intent);
                mContext.startForegroundService(intent);
            }

        } else if (action.equals("com.geenk.action.SCAN_SWITCH")) {
        	int isScan=mIntent.getIntExtra("extra",1);
            Log.e("scanner1","----"+isScan);
			client.enableSystemScan(isScan==1);

        } else if (action.equals("com.geenk.action.START_SCAN")) {
            client.startScan();
        } else if (action.equals("com.geenk.action.STOP_SCAN")) {
            client.stopScan();
        }
    }


}

	
      
     


		


