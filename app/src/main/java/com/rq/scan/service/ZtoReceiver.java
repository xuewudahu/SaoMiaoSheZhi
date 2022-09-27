package com.rq.scan.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import cn.rq.pda.sdk.RqPDAClient;
import cn.rq.pda.sdk.ScanReceiveMode;

public class ZtoReceiver extends BroadcastReceiver {
    public RqPDAClient client;
    static String uuid1;
    Context mContext;
    Handler handler;
    int waitStopTimeout = 500;
    int waitStopTime = 0;
    int waitTime = 100;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(client.isScanning()){
                handler.removeCallbacks(runnable);
                if (waitStopTime >= waitStopTimeout) {
                    waitStopTime = 0;
                    return;
                }
                waitStopTime += waitTime;
                Log.i("WXW","wait ..."+waitStopTime);
                handler.postDelayed(runnable,waitTime);
            } else {
                Log.i("WXW","startScan ...");
                handler.removeCallbacks(runnable);
                //waitStopTime = 0;
                client.startScan();
            }
        }
    };
    public ZtoReceiver(Handler handler,Runnable runnable,RqPDAClient client){
        this.handler=handler;
        //this.runnable=runnable;
        this.client=client;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mContext = context;
        //client = RqPDAClient.newClient(mContext);

        Log.d("ZTO", "扫描设置收到的广播----" + action);
        SharedPreferences settings = mContext.getSharedPreferences("delay_interval", 0);
        SharedPreferences.Editor editor = settings.edit();
        SharedPreferences settings2 =mContext.getSharedPreferences("ScanControlPreference", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor2 = settings2.edit();
        //获取SN
        if (action.equals("com.android.zto.pda.action.get_sn")) {
            String uuid = intent.getStringExtra("cmd_id");
            String deviceName = SystemProperties.get("ro.product.name");
            String imei = SystemProperties.get("sys.slot.index.1.imei");

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.get_sn");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("sn", deviceName + imei);
            mContext.sendBroadcast(intentZto);

            //开始出光扫描
        } else if (action.equals("com.android.zto.pda.action.start_scan")) {
            uuid1 = intent.getStringExtra("cmd_id");
            int continueTime = intent.getIntExtra("scan_continue_time", 3000);
            Log.d("ZTO", "开始出光扫描----" + (continueTime - waitStopTime));
            client.setScannerTimeout(continueTime - waitStopTime);
            waitStopTime = 0;
            handler.post(runnable);
            //client.startScan();
            //停止出光扫描
        }else if (action.equals("com.android.zto.pda.action.stop_scan")) {
            String uuid = intent.getStringExtra("cmd_id");
            client.stopScan();
            if (handler.hasCallbacks(runnable)) {
                waitStopTime = 0;
                handler.removeCallbacks(runnable);
            }
            Intent intentZto = new Intent("com.android.zto.pda.action.rec.stop_scan");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //按下扫描键
        } else if (action.equals("android.intent.action.SCAN_keycode")) {

            Boolean down = intent.getBooleanExtra("down", false);
            Log.e("ZTO","---SCAN_keycode-"+down);
            if (!down && handler.hasCallbacks(runnable)) {
                Log.e("ZTO","---SCAN_keycode-");
                waitStopTime = 0;
                handler.removeCallbacks(runnable);
            }
            //设置系统时间
        } else if (action.equals("com.android.zto.pda.action.set_systime")) {
            String uuid = intent.getStringExtra("cmd_id");
            Long sysTime = intent.getLongExtra("cmd_value", 1514736000000L);

            Intent intent_time = new Intent("com.rq.setsystemtime");
            intent_time.putExtra("time", String.valueOf(sysTime));
            intent_time.setPackage("com.android.scantest");
            if (mContext != null) {
                mContext.startService(intent_time);
            }
            client.enableSystemScan(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    client.enableSystemScan(true);
                }
            }, 500);


            Intent intentZto = new Intent("com.android.zto.pda.action.rec.set_systime");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置连扫间隔
        } else if (action.equals("com.android.zto.pda.action.set_scan_interval")) {
            String uuid = intent.getStringExtra("cmd_id");
            Long intervalTime = intent.getLongExtra("cmd_value", 100L);
            client.setContinueScanInterval(Integer.parseInt(String.valueOf(intervalTime)));

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.set_scan_interval");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置扫不到条码时的超时时间
        } else if (action.equals("com.android.zto.pda.action.set_scan_delay_interval")) {
            String uuid = intent.getStringExtra("cmd_id");
            Long intervalTime = intent.getLongExtra("cmd_value", 100L);
            //client.setScannerTimeout(Integer.parseInt(String.valueOf(intervalTime)));

            editor.putLong("delay_interval_time", intervalTime);
            editor.commit();

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.set_scan_delay_interval");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //是否开启连扫
        } else if (action.equals("com.android.zto.pda.action.start_recycle_scan")) {
            String uuid = intent.getStringExtra("cmd_id");
            Boolean isRecycle = intent.getBooleanExtra("cmd_value", false);
            if (isRecycle) {
                client.enableContinueScan(isRecycle);
                client.startScan();
            }else{
                client.stopScan();
                client.enableContinueScan(isRecycle);
            }
            Intent intentZto = new Intent("com.android.zto.pda.action.rec.start_recycle_scan");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置允许下拉状态栏
        } else if (action.equals("com.android.zto.pda.action.allow_pull_statusbar")) {
            String uuid = intent.getStringExtra("cmd_id");
            Boolean isStatusbar = intent.getBooleanExtra("cmd_value", true);

            Intent intent_statusbar = new Intent("com.rq.disable_statusbar_expand");
            intent_statusbar.putExtra("com.rq.disable_statusbar_expand", !isStatusbar);
            mContext.sendBroadcast(intent_statusbar);


            Intent intentZto = new Intent("com.android.zto.pda.action.rec.allow_pull_statusbar");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置禁用home键
        } else if (action.equals("com.android.zto.pda.action.allow_click_home_key")) {
            String uuid = intent.getStringExtra("cmd_id");
            Boolean isHome = intent.getBooleanExtra("cmd_value", true);

            Intent intent_time = new Intent("com.rq.disable.virtual.home.key");
            intent_time.putExtra("home_key_disable",!isHome);
            intent_time.setPackage("com.android.scantest");
            mContext.startService(intent_time);


            Intent intent_home_s8c=new Intent("android.intent.action.RQ_NAVIGATIONBAR_CHANGED");
            intent_home_s8c.putExtra("android.intent.action.EXTRA_RQ_NAVIGATIONBAR_DISABLE_HOME_KEY",isHome?0:1);//0 show 1 hide
            mContext.sendBroadcast(intent_home_s8c);

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.allow_click_home_key");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置扫描时是否补光(闪光灯)
        } else if (action.equals("com.android.zto.pda.action.set_scan_light")) {
            String uuid = intent.getStringExtra("cmd_id");
            Boolean islight = intent.getBooleanExtra("cmd_value", true);
            client.setScannerParameter("decode_ill_enable", islight ? "1" : "0");

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.set_scan_light");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置扫描输出模式
        } else if (action.equals("com.android.zto.pda.action.scan_output_model")) {
            String uuid = intent.getStringExtra("cmd_id");
            int model = intent.getIntExtra("cmd_value", 1);
            if (model == 1) {
                client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                editor2.putString("key_scan_output_mode", "OUTPUT_BROADCAST");
                editor2.commit();
            } else {
                client.setScanReceiveMode(ScanReceiveMode.AUTO_ENTER_IN_FOCUS);
                editor2.putString("key_scan_output_mode", "OUTPUT_DIRECT");
                editor2.commit();
            }
            client.enableOverWriteOutput(false);

            Intent intentZto = new Intent("com.android.zto.pda.action.rec.scan_output_model");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
            //设置开启禁用多任务切换键
        } else if (action.equals("com.android.zto.pda.action.allow_click_switch_task_key")) {
            String uuid = intent.getStringExtra("cmd_id");
            Boolean isTask = intent.getBooleanExtra("cmd_value", true);

            Intent intent_task = new Intent("com.rq.disable.virtual.menu.key");
            intent_task.putExtra("menu_key_disable",!isTask);
            intent_task.setPackage("com.android.scantest");
            mContext.startService(intent_task);

            Intent intent_task_s8c=new Intent("android.intent.action.RQ_NAVIGATIONBAR_CHANGED");
            intent_task_s8c.putExtra("android.intent.action.EXTRA_RQ_NAVIGATIONBAR_DISABLE_SWITCH_KEY",isTask?0:1);//0 show 1 hide
            mContext.sendBroadcast(intent_task_s8c);


            Intent intentZto = new Intent("com.android.zto.pda.action.rec.allow_click_switch_task _key");
            intentZto.putExtra("cmd_id", uuid);
            intentZto.putExtra("result", true);
            mContext.sendBroadcast(intentZto);
        }
    }
}
