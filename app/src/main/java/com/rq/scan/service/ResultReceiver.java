package com.rq.scan.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import cn.rq.pda.sdk.RqPDAClient;

public class ResultReceiver  extends BroadcastReceiver {
    public RqPDAClient client;
    Context mContext;
    static Handler handler;
    Runnable runnable;
    public ResultReceiver(Handler handler,Runnable runnable,RqPDAClient client){
        this.handler=handler;
        this.runnable=runnable;
        this.client=client;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        mContext=context;
        Log.e("ZTO", "收到的结果广播----" + action);
        SharedPreferences settings = mContext.getSharedPreferences("delay_interval", 0);
        if (action.equals("com.rq.cenon.receivescanaction")) {
            String data = intent.getStringExtra("data");
            Intent intentZto = new Intent("com.android.zto.pda.action.rec.start_scan");
            intentZto.putExtra("cmd_id", ZtoReceiver.uuid1);
            intentZto.putExtra("code", data);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);

            if (TextUtils.isEmpty(data) && client.isContinueScanEnabled()) {
                if (settings.getLong("delay_interval_time", 10221) != 10221L) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, settings.getLong("delay_interval_time", 0));
                }
            }
            //扫描结果
        } else if (action.equals("com.android.SimulationOfKeyboarOperation")) {
            String data = intent.getStringExtra("scanresults");
            Intent intentZto = new Intent("com.android.zto.pda.action.rec.start_scan");
            intentZto.putExtra("cmd_id", ZtoReceiver.uuid1);
            intentZto.putExtra("code", data);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            if (TextUtils.isEmpty(data) && client.isContinueScanEnabled()) {
                if (settings.getLong("delay_interval_time", 10221) != 10221L) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, settings.getLong("delay_interval_time", 0));
                }
            }
        }
    }
}
