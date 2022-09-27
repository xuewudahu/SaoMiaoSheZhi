package com.rq.scan;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.content.pm.ActivityInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;

import java.util.*;


import cn.rq.pda.sdk.RqPDAClient;
import cn.rq.pda.sdk.Symbology;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//for aliOCR


public class ScanTestFragment extends Fragment implements EditText.OnTouchListener, RqPDAClient.ScanObserver {//, ScanControl.ScanCallback
    private static final String TAG = "ScanTestFragment";
    private EditText editText_1;
    public EditText editText_2;
    public Button btnClearScan;
    public ScanSettingActivity mActivity;
    public View mScanTestView;

    public String firstResult;
    public String mResult;
    public String mEditResult;
    public int resultCount = 0;
    public int errorResultCount = 0;
    public StringBuffer resultBuffer = new StringBuffer("Content:");
    public TextView result = null;
    public String mResultStr = "";

    public long time;
    public long timeCost;

    public String mOCRPhoneEnabled = "false";


    //private static final int OUTPUT_DIRECT = 0;
    //private static final int OUTPUT_BROADCAST = 1;

    private boolean isFirst = true;

    private boolean isRegister = false;
    private CheckBox remoteCheck;
    private boolean needDo = true;
    private String mOutMode = "OUTPUT_DIRECT";
    private String mOldMode = "OUTPUT_DIRECT";
    SharedPreferences settings ;
    SharedPreferences.Editor editor ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScanTestView = inflater.inflate(R.layout.scan_test_item, container, false);
        return mScanTestView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("www","---"+remoteCheck);
        remoteCheck = (CheckBox) getActivity().findViewById(R.id.remote_check);
        editText_1 = (EditText) getActivity().findViewById(R.id.editText_1);
        editText_2 = (EditText) getActivity().findViewById(R.id.editText_2);
        settings = getActivity().getSharedPreferences("fanrunqi", 0);
        editor= settings.edit();
        editText_1.setOnTouchListener(this);
        editText_2.setOnTouchListener(this);
        Log.e("www","---"+editText_1);
        remoteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("www","--"+isChecked);
                editor.putBoolean("isRemote", isChecked);
                editor.commit();
            }
        });
        editText_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (ScanSettingActivity.DEBUG) Log.d(TAG, "beforeTextChanged s: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ScanSettingActivity.DEBUG)
                    Log.d(TAG, "onTextChanged s:" + s.toString() + ", start:" + start + ", before:" + before + ", count:" + count + " needDo:= " + needDo);
                if (TextUtils.isEmpty(s))
                    return;

                if (!needDo) {
                    needDo = true;
                    return;
                }
                mEditResult = s.toString();
                mResult = mEditResult.substring(start, count);
                // mResult = mEditResult.substring(start);

                decodeResult();

                editText_1.removeTextChangedListener(this);
                if (editText_1 != null && mResultStr != null) {
                    Log.e("www", "onTextChanged------");
                    editText_1.setText(mResultStr);
                }
                editText_1.addTextChangedListener(this);
                if (settings.getBoolean("isRemote",true)) {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://www.baidu.com") //设置网络请求的Url地址
                            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                            .build();

                // 创建 网络请求接口 的实例
                GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

                //对 发送请求 进行封装

                retrofit2.Call<ResponseBody> call = request.getCall();
                //发送网络请求(异步)
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                        Log.e("www", "连接  成功" + call.request().url().toString());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.e("www", "连接  失败" + call.request().url().toString());
                    }

                });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if (ScanSettingActivity.DEBUG)  Log.d(TAG,"afterTextChanged s: "+s.toString().trim());
                editText_1.setSelection(0);
            }
        });

        result = (TextView) getActivity().findViewById(R.id.result_1);
        resetDisplayBuffer();
        btnClearScan = (Button) getActivity().findViewById(R.id.btnClearScan);
        btnClearScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDisplayBuffer();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "action = " + event.getAction());
        return true;
    }


    BroadcastReceiver resultBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "resultBroadcast. intent = " + intent.getAction());

            if ("start_Scan_time".equals(intent.getAction())) {
                mActivity.scanStartTime = System.currentTimeMillis();
            } else if ("com.qxj.scan.phonenumber".equals(intent.getAction())) { //for aliOCR
                String number = intent.getStringExtra("ocr_phonenumber");
                String code = intent.getStringExtra("ocr_barcode");
                String ocrTime = intent.getStringExtra("ocr_time");
                //if(!TextUtils.isEmpty(number))
                {
				   /*String hint = "Phone: "+number;			
				   Toast.makeText(mActivity, hint, Toast.LENGTH_SHORT).show();*/
                    displayResult4OCR(code, number, ocrTime);
                    if (editText_2 != null && mResultStr != null) {
                        editText_2.setText(mResultStr);
                    }
                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onAttach ");
        super.onAttach(activity);
        mActivity = (ScanSettingActivity) activity;

        mOldMode = mActivity.readFromPreference("key_scan_output_mode", "OUTPUT_DIRECT"); //added
        mOutMode = mOldMode;
        //PDALog.enableDebug(true);
        if (mActivity.client == null)
            mActivity.client = RqPDAClient.newClient(getActivity()); //eric-zhao changed

        mActivity.client.create();
        mActivity.client.registerScanObserver(this);
        //mActivity.client.enableSystemScan(true);  //eric-zhao  removed

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("start_Scan_time");
        intentFilter.addAction("com.qxj.scan.phonenumber"); //for aliOCR DEBUG
        mActivity.registerReceiver(resultBroadcast, intentFilter);
        isRegister = true;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onHiddenChanged. hidden = " + hidden);
        super.onHiddenChanged(hidden);

        mOCRPhoneEnabled = mActivity.readFromPreference("key_scan_phonenumber_enable", "false");
        if (hidden) {
            mScanTestView.setKeepScreenOn(false);

            mOldMode = mOutMode;  //

            mActivity.client.unregisterScanObserver(this); //added

            if (isRegister) {
                mActivity.unregisterReceiver(resultBroadcast);
                isRegister = false;
            }
            //TODO now invisible to user
        } else {
            //TODO now visible to user
            mScanTestView.setKeepScreenOn(true);

            mActivity.client.registerScanObserver(this);

            mOutMode = mActivity.readFromPreference("key_scan_output_mode", "OUTPUT_DIRECT");
            if (ScanSettingActivity.DEBUG)
                Log.d(TAG, "onHiddenChanged. outMode = " + mOutMode + " mOldMode:= " + mOldMode);

            if (!mOldMode.equals(mOutMode)) {

                resetDisplayBuffer();
                mOldMode = mOutMode;

            }
            // switch (mActivity.outMode)
            {
                if (mOutMode.equals("OUTPUT_DIRECT")) { //OUTPUT_DIRECT:
                    editText_1.setFocusable(true);
                    editText_1.setFocusableInTouchMode(true);
                    editText_1.requestFocus();
                    editText_2.setFocusableInTouchMode(false);
                    editText_2.setFocusable(false);
                } else if (mOutMode.equals("OUTPUT_BROADCAST")) {
                    //  break;

                    //case OUTPUT_BROADCAST:
                    editText_2.setFocusable(true);
                    editText_2.setFocusableInTouchMode(true);
                    editText_2.requestFocus();
                    editText_1.setFocusableInTouchMode(false);
                    editText_1.setFocusable(false);
                }
                //break;
            }


            if (!isRegister) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("start_Scan_time");
                intentFilter.addAction("com.qxj.scan.phonenumber"); //for aliOCR DEBUG
                mActivity.registerReceiver(resultBroadcast, intentFilter);
                mActivity.client.registerScanObserver(this);
                isRegister = true;
            }


        }


    }

    @Override
    public void onResume() {


        mOutMode = mActivity.readFromPreference("key_scan_output_mode", "OUTPUT_DIRECT");
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onResume. mOutMode: = " + mOutMode);
        super.onResume();
        // switch (mActivity.outMode)
        {
            //case OUTPUT_DIRECT:
            if (mOutMode.equals("OUTPUT_DIRECT")) {
                editText_1.setFocusable(true);
                editText_1.setFocusableInTouchMode(true);
                //editText_1.requestFocus();
                editText_2.setFocusableInTouchMode(false);
                editText_2.setFocusable(false);
            }
            //break;

            // case OUTPUT_BROADCAST:
            else if (mOutMode.equals("OUTPUT_BROADCAST")) {
                editText_2.setFocusable(true);
                editText_2.setFocusableInTouchMode(true);
                //editText_2.requestFocus();
                editText_1.setFocusableInTouchMode(false);
                editText_1.setFocusable(false);
            }
            // break;
        }

        if (!isRegister) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("start_Scan_time");
            intentFilter.addAction("com.qxj.scan.phonenumber"); //for aliOCR DEBUG
            mActivity.registerReceiver(resultBroadcast, intentFilter);
            mActivity.client.registerScanObserver(this);
            isRegister = true;
        }
        remoteCheck.setChecked(settings.getBoolean("isRemote",true));
        //initAliOCR();
    }


    @Override
    public void onDestroy() {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onDestroy.");

        /*mActivity.client.unregisterScanObserver(this); //added 
        resetDisplayBuffer();
        mActivity.unregisterReceiver(resultBroadcast);*/


        //RqPDAClient.getInstance(getActivity()).setScanCallback(null);


        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onStop.");

        super.onStop();

        mActivity.client.unregisterScanObserver(this); //added
        resetDisplayBuffer();

        if (isRegister) {
            mActivity.unregisterReceiver(resultBroadcast);
            isRegister = false;
        }
    }

    public void resetDisplayBuffer() {
        resultCount = 0;
        errorResultCount = 0;
        firstResult = "";
        mActivity.scanTime = 0;
        resultBuffer = new StringBuffer("");

        //resultBuffer.append(getResources().getString(R.string.test_barcode)).append(firstResult).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_count)).append(resultCount).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_error)).append(errorResultCount).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_time)).append(mActivity.scanTime).append("ms").append("\n");
        result.setText(resultBuffer.toString());
        mResultStr = "";
        Log.e("www", "resetDisplayBuffer------");
        editText_1.setText("");
        editText_2.setText("");
    }

    public void decodeResult() {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "decodeResult mResult: " + mResult);

        if (TextUtils.isEmpty(mResult)) {  //  eric-zhao
            mResult = "No Read:null";
            errorResultCount++;
        }

        time = System.currentTimeMillis();
        timeCost = time - mActivity.scanStartTime;
        if (ScanSettingActivity.DEBUG)
            Log.d(TAG, "decodeResult scanStartTime:" + mActivity.scanStartTime + ", time: " + time + ", timeCost:" + timeCost);

        mActivity.scanStartTime = time;
        mActivity.scanTime = mActivity.scanTime + timeCost;

        resultCount++;


        if (TextUtils.isEmpty(firstResult)) {
            firstResult = mResult;
        }

       /* if (!mActivity.test_output_autoreturn)  //eric-zhao output_autoreturn
            mResultStr = mResult;
        else */
        {
            if (resultCount % 50 == 0)
                mResultStr = mResult + "\n";
            else
                mResultStr = mResult + "\n" + mResultStr;
        }

        resultBuffer = new StringBuffer("");

        // resultBuffer.append(getResources().getString(R.string.test_barcode)).append(firstResult).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_count)).append(resultCount).append("\n");

        if (ScanSettingActivity.DEBUG)
            Log.d(TAG, "decodeResult mResult: " + mResult + " firstResult:= " + firstResult + " errorResultCount:= " + errorResultCount);

		/*if (TextUtils.isEmpty(mResult) || !mResult.equals(firstResult)) { //DEBUGTODO  eric-zhao
            errorResultCount++;
        }*/
        resultBuffer.append(getResources().getString(R.string.test_error)).append(errorResultCount).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_time)).append(mActivity.scanTime).append("ms").append("(").append(timeParse(mActivity.scanTime)).append("s)").append("\n");
        // resultBuffer.append("CurrentTime:").append(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss.SSS秒").format(time)).append("\n");
        result.setText(resultBuffer.toString());
    }

    public void displayResult4OCR(String barcode, String phoneNo, String ocrtime) {
        if (ScanSettingActivity.DEBUG)
            Log.d(TAG, "displayResult4OCR mResult: " + mResult + " barcode:= " + barcode);

        if (TextUtils.isEmpty(mResult)) {  //  eric-zhao
            mResult = "No Read:null";
            errorResultCount++;
        }

        time = System.currentTimeMillis();
        timeCost = time - mActivity.scanStartTime;
        if (ScanSettingActivity.DEBUG)
            Log.d(TAG, "decodeResult scanStartTime:" + mActivity.scanStartTime + ", time: " + time + ", timeCost:" + timeCost);

        mActivity.scanStartTime = time;
        mActivity.scanTime = mActivity.scanTime + timeCost;

        resultCount++;


        if (TextUtils.isEmpty(firstResult)) {
            firstResult = mResult;
        }

       /* if (!mActivity.test_output_autoreturn)  //eric-zhao output_autoreturn
            mResultStr = mResult;
        else */
        {
            if (resultCount % 50 == 0)
                mResultStr = mResult + " " + phoneNo + "\n";
            else
                mResultStr = mResult + " " + phoneNo + "\n" + mResultStr;
        }

        resultBuffer = new StringBuffer("");

        // resultBuffer.append(getResources().getString(R.string.test_barcode)).append(firstResult).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_count)).append(resultCount).append("\n");

        if (ScanSettingActivity.DEBUG)
            Log.d(TAG, "decodeResult mResult: " + mResult + " firstResult:= " + firstResult + " errorResultCount:= " + errorResultCount);

		/*if (TextUtils.isEmpty(mResult) || !mResult.equals(firstResult)) { //DEBUGTODO  eric-zhao
            errorResultCount++;
        }*/
        resultBuffer.append(getResources().getString(R.string.test_error)).append(errorResultCount).append("\n");
        resultBuffer.append(getResources().getString(R.string.test_time)).append(mActivity.scanTime).append("ms").append("(").append(timeParse(mActivity.scanTime)).append("s)").append("\n");
        resultBuffer.append(getResources().getString(R.string.ocr_time)).append(ocrtime).append("ms").append("\n");
        // resultBuffer.append("CurrentTime:").append(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss.SSS秒").format(time)).append("\n");
        result.setText(resultBuffer.toString());
    }


    public static String timeParse(long duration) {
        String time = "";
        long hour = duration / 3600000;
        long minutes = duration % 3600000;
        long minute = minutes / 60000;
        long seconds = minutes % 60000;
        long second = seconds / 1000;
        long milliseconds = seconds % 1000;

        if (hour > 0) {
            time += hour + ":";

            if (minute < 10) {
                time += "0";
            }
            time += minute + ":";

            if (second < 10) {
                time += "0";
            }
            time += second + ".";

            if (milliseconds < 10) {
                time += "00";
            } else if (milliseconds < 100) {
                time += "0";
            }

            time += milliseconds;
        } else if (minute > 0) {
            time += minute + ":";

            if (second < 10) {
                time += "0";
            }
            time += second + ".";

            if (milliseconds < 10) {
                time += "00";
            } else if (milliseconds < 100) {
                time += "0";
            }

            time += milliseconds;
        } else if (second > 0) {
            time += second + ".";

            if (milliseconds < 10) {
                time += "00";
            } else if (milliseconds < 100) {
                time += "0";
            }

            time += milliseconds;
        } else {
            time += second + "0.";

            if (milliseconds < 10) {
                time += "00";
            } else if (milliseconds < 100) {
                time += "0";
            }

            time += milliseconds;
        }
        return time;
    }

//    @Override
//    public void onScanResult(String barcode, int type, byte[] dataBytes) {
//        mResult = barcode;
//        decodeResult();
//        if (editText_2!=null&& mResultStr != null) {
//            editText_2.setText(mResultStr);
//        }
//    }

    @Override
    public void onScanResult(String barcode, Symbology symbology) {

        Log.d(TAG, "onScanResult 000 mOCRPhoneEnabled:= " + mOCRPhoneEnabled);
        Log.d("www", "onScanResult---" + barcode);
        mResult = barcode;
        if (mOCRPhoneEnabled.equals("false")) {     //for OCR

            decodeResult();

            if (TextUtils.isEmpty(barcode)) {

                if (mOutMode.equals("OUTPUT_DIRECT")) {
                    if (editText_1 != null && mResultStr != null) {
                        needDo = false;
                        // editText_1.setText("");
                        Log.e("www", "onScanResult------");
                        editText_1.setText(mResultStr);

                    }
                }

            }

            if (mOutMode.equals("OUTPUT_BROADCAST")) {
                if (editText_2 != null && mResultStr != null) {
                    editText_2.setText(mResultStr);
                }
            }
        } else {//for no barcode

            if (TextUtils.isEmpty(barcode)) {
                displayResult4OCR(barcode, "", " ");

                if (mOutMode.equals("OUTPUT_BROADCAST")) {
                    if (editText_2 != null && mResultStr != null) {
                        editText_2.setText(mResultStr);
                    }
                }
            }
        }

    }


  


/*
    @Override
    public void onScanResult(String barcode, Symbology symbology) {
        System.out.println(symbology);
        Editable edit = editText_2.getEditableText();//获取EditText的文字

       // Log.d(TAG, "onScanResult 000");
       // if(!isCurrentTop())
			//return;
		
		Log.d(TAG, "onScanResult 111");
	   
        if (TextUtils.isEmpty(edit)) {
            editText_2.setText(barcode);
        } else {
           // if (!mActivity.output_autoreturn)
              //  mResultStr = barcode;
           // else
            {
                mResultStr = barcode + "\n" + edit;
            }
            editText_2.setText(mResultStr);
        }
		
        mResult = barcode ;
		decodeResult();  //added 


    }
*/

    private void closeInputMethod(Context context, EditText tv_works_name) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        Log.d(TAG, "closeInputMethod. isOpen = " + isOpen);
        //if (isOpen)
        {
            imm.hideSoftInputFromWindow(tv_works_name.getWindowToken(), 0); //强制隐藏键盘
            isOpen = false;
        }

    }

    private boolean isCurrentTop() {
        final ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityInfo aInfo = null;
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list.size() != 0) {
            RunningTaskInfo topRunningTask = list.get(0);
            Log.d(TAG, "currert:" + topRunningTask.topActivity.getPackageName() + "  " + topRunningTask.topActivity.getClassName());
            return "com.rq.scan".equals(topRunningTask.topActivity.getPackageName())
                    && ("com.rq.scan.ScanSettingActivity".equals(topRunningTask.topActivity.getClassName()) || "com.mediatek.camera.CameraActivity".equals(topRunningTask.topActivity.getClassName()));
        }
        return false;
    }
}
