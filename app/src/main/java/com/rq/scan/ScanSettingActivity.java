package com.rq.scan;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.provider.Settings;
import android.content.SharedPreferences;

import android.content.Context;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;


import java.util.ArrayList;

import cn.rq.pda.sdk.RqPDAClient;


public class ScanSettingActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "ScanSettingActivity";

    public static String PREF_KEY_SCAN_OUTPUT_MODE = "key_scan_output_mode";
    public static String PREF_KEY_SCAN_OUTPUT_OVERRIDE = "key_scan_output_override";
    public static String PREF_KEY_SCAN_OUTPUT_AUTORETURN = "key_scan_output_autoreturn";
    public static String PREF_KEY_SCAN_SUFFIX_CHAR = "key_scan_suffix_char";

    public RelativeLayout relSetting;
    public RelativeLayout relTest;
    public ImageView imgSetting;
    public ImageView imgTest;
    private static Fragment[] framents;

    public RqPDAClient client;

    public boolean isFloatingScanEnabled = false;
    public boolean isScanOpened = false;

    private static final int OUTPUT_DIRECT = 0;
    private static final int OUTPUT_BROADCAST = 1;

    private static final int READ_MODE_ONE_TIME = 0;
    private static final int READ_MODE_CONTINUE = 1;

    public int outMode;
    public boolean output_autoreturn = false;
    public boolean test_output_autoreturn = false;
    public String test_SuffixChar="";

    public int readMode;
    public int scan_read_interval;
    public int scan_read_timeout=5;
    public int scan_read_continue_notimeout = 0;//eric-zhao

    public boolean scan_read_repeat = false;
    public boolean scan_read_downscanner = false;

    public String scan_prefix_code;
    public String scan_suffix_code;
    public int scan_brightness;

    public boolean scan_alert_sound = false;
    public boolean scan_alert_vibrate = false;

    public boolean isImageViewShow = false;

    private final static int HSM_DECODE_LIB = 0;
    private final static int ZBAR_DECODE_LIB = 1;
    private final static int SHENGYUAN_DECODE_LIB = 2;
    private final static int CORTEX_DECODE_LIB = 3;
    public int scanner_decode_type = CORTEX_DECODE_LIB;

    public long scanStartTime;
    public long scanTime = 0;

    public static final String myPref = "ScanControlPreference";

    public static final boolean DEBUG =  true;// true;

    public SharedPreferences pref;  //eric-zhao

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private int OVERLAY_PERMISSION_REQ_CODE = 2;
    private Intent intentTakePic;
    private static String[] PERMISSIONS_REQUIRED = {
            //"android.permission.CAMERA",
            //"android.permission.READ_PHONE_STATE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            //"android.permission.SYSTEM_ALERT_WINDOW"
            };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // verifyPermissions(this);
        super.onCreate(savedInstanceState);

        client = RqPDAClient.newClient(this);
       // client.enableSystemScan(true);  //eric-zhao removed

        setContentView(R.layout.scan_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//横屏显示

        relSetting = (RelativeLayout) findViewById(R.id.relSetting);//扫描设置
        relSetting.setOnClickListener(this);
        imgSetting = (ImageView) findViewById(R.id.imgSetting);//设置横线

        relTest = (RelativeLayout) findViewById(R.id.relTest);//扫描测试
        relTest.setOnClickListener(this);
        imgTest = (ImageView) findViewById(R.id.imgTest);//测试横线

        ((MyApplication)getApplication()).setmActivity(this);
        pref = getSharedPreferences("scan_continue_notimeout", MODE_PRIVATE); //eric-zhao


       // initPara();//扫描中的接口jar包   //eric-zhao removed
        setFrament();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // for FloatView
            requestDrawOverLaysPermission();
        }
        //若服务没有开启，则开启服务，防止清楚储存空间等关闭服务的情况
        if (!isServiceRunning("com.rq.scan.service.ScanService")) {
            Intent intent = new Intent();
            intent.setAction("com.qxj.scan.service.start");
            intent.setPackage("com.rq.scan");
            startForegroundService(intent);
        }

    }
    public boolean isServiceRunning(String serviceName) {
        try {
            if (TextUtils.isEmpty(serviceName)) {
                return false;
            }

            ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList) myManager.getRunningServices(Integer.MAX_VALUE);
            for (int i = 0; i < runningService.size(); i++) {

                if (runningService.get(i).service.getClassName().toString()
                        .equals(serviceName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setFrament() {
        framents = new Fragment[2];
        framents[0] = getFragmentManager().findFragmentById(R.id.scanSettingFragment);
        framents[1] = getFragmentManager().findFragmentById(R.id.scanTestFragment);

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(framents[0]).hide(framents[1]).show(framents[0]).commit();
        fragmentTransaction.addToBackStack(null);
    }

    public void resetDisplayBuffer() {
        ((ScanTestFragment) framents[1]).resetDisplayBuffer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relSetting:
                getFragmentManager().beginTransaction().hide(framents[0]).hide(framents[1]).show(framents[0]).commit();
                imgSetting.setVisibility(View.VISIBLE);
                imgTest.setVisibility(View.GONE);
                break;
            case R.id.relTest:
                getFragmentManager().beginTransaction().hide(framents[0]).hide(framents[1]).show(framents[1]).commit();
                imgSetting.setVisibility(View.GONE);
                imgTest.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //得到Fragment的管理器，从管理器中得到Fragment当前已加入Fragment回退栈中的fragment的数量
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "OnKeyDown.");
        switch (keyCode) {
            case KeyEvent.KEYCODE_BUTTON_L1:
            case KeyEvent.KEYCODE_BUTTON_R1:
            case KeyEvent.KEYCODE_TV_ANTENNA_CABLE:
				
                scanStartTime = System.currentTimeMillis();
				
                if (ScanSettingActivity.DEBUG)
                    Log.d(TAG, "onKeyDown scanStartTime: " + scanStartTime);
                break;

            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (ScanSettingActivity.DEBUG) Log.d("ScanSettingActivity", "OnKeyUp");
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
       // initPara();   //eric-zhao removed
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onPause.");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onPause.");
        super.onDestroy();
    }

  /*public void initPara() {
        //系统扫描状态
        isScanOpened = client.getSystemScanState();

        //       outMode = client.getUseBroadcastCallbackStatus() == true ? OUTPUT_BROADCAST : OUTPUT_DIRECT;
        //test_output_autoreturn = readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, "OUTPUT_DIRECT").equals(PREF_KEY_SCAN_OUTPUT_AUTORETURN);
        test_SuffixChar = readFromPreference(PREF_KEY_SCAN_SUFFIX_CHAR, "CHAR_NULL");
        output_autoreturn = client.getAdditionalKeyCode() == KeyEvent.KEYCODE_ENTER? true:false; //eric-zhao

        //连续扫描状态
        if (client.isContinueScanEnabled())
            readMode = READ_MODE_CONTINUE;
        else
            readMode = READ_MODE_ONE_TIME;

        scan_read_interval = client.getContinueScanInterval();
//        scan_read_repeat = client.getDisallowSameresult();
//        scan_read_timeout = client.getScannerTimerOut();
        scan_read_continue_notimeout = pref.getInt("key", 0);//Settings.Global.getInt(this.getContentResolver(), "continue_scan_notimeout",0);//client.getContinueScanningNoTimeout();
//        scan_read_downscanner = client.getDownScannerStatus();
//
//        isFloatingScanEnabled = client.getFloatViewScannerStatus();
//        if(isFloatingScanEnabled){
//            client.TurnOnOffFloatViewScanner(isFloatingScanEnabled);
//        }
        scan_prefix_code = client.getScanResultPrefix();
        scan_suffix_code = client.getScanResultSuffix();

        scan_alert_sound = client.isScanSoundEnabled();
        scan_alert_vibrate = client.isVibratorEnabled();

//        isImageViewShow = client.getImageViewStatus();
//
//        scanner_decode_type =  client.getScannerDecodeType();
//
//        scan_brightness =  client.getIllBrightness();

    }
	*/

    public void writeToPreference(String name, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit();
        editor.putString(name, value);
        editor.commit();
    }

    public String readFromPreference(String name, String defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getString(name, defaultvalue);
    }

    public void writeIntToPreference(String name, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public int readIntFromPreference(String name, int defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getInt(name, defaultvalue);
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verifyPermissions(Activity activity) {
        try {
            for (int i = 0; i < PERMISSIONS_REQUIRED.length; i++) {
                int permission = ActivityCompat.checkSelfPermission(activity,
                        PERMISSIONS_REQUIRED[i]);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLaysPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Toast.makeText(this, "权限已经授予", Toast.LENGTH_SHORT).show();
        }
    }

  
}
