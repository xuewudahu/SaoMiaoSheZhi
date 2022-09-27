package com.rq.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.ComponentName;
import android.content.Context;

import android.content.ClipData;
import android.content.ClipboardManager;


import android.content.SharedPreferences;
import android.app.ProgressDialog;


import android.widget.ImageView;

import java.util.List;

//DEBUG

//END

import cn.rq.pda.sdk.ScanReceiveMode;




public class ScanSettingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ScanSettingFragment";
    private RelativeLayout relOnScan;
    private RelativeLayout relOutput;
	//private RelativeLayout relCallback;
    private RelativeLayout relReadcode;
    private RelativeLayout relTrigger;
    private RelativeLayout relPrefixSuffix;
    private RelativeLayout relSuffix;
    private RelativeLayout relDecoding;
  //  private RelativeLayout relDecodeType;
    private RelativeLayout relDecodeMode;
    private RelativeLayout relBroadcast;
	private RelativeLayout relPkgSetting;
	private RelativeLayout relScanCenter;
	private RelativeLayout relScanLight;
	private RelativeLayout relScanKeyEvent;
	private RelativeLayout relPhoneNum;
	private TextView txtPkgSetting; //for BEST
    private CheckBox startScan;
	private CheckBox scanCallback;
    private TextView txtOutputMode;
    private String txtOutput;
	//private String txtCallback;
    private TextView txtReadcodeMode;
    private String txtReadcode;
    private TextView txtPrefixValue;
	private TextView txtSuffixValue;
	
   // private TextView txtDecodeTypeMode;
    //private String txtDecodeType;
    //选择输出模式时赋值，默认为直接填充
    private String outMode = "rbDirect_filling";

    //选择输出模式时赋值，默认为自动回车
    private String setting_output_autoreturn = "chAutomatic";
    //选择读码模式时赋值，默认为连续扫描
    public String mOutMode;
    private String setting_readMode = "radioRelease";  //eric-zhao changed  radioContinuous -> radioRelease

    private ScanSettingActivity mActivity;
    private boolean isScanChecked = false;
    private boolean isscanreadcontinuenotimeout = false;
    private boolean isFloatingScanEnabled = false;
    private boolean isScanCenterEnabled = false;  //added by eric-zhao

	private boolean isIllLightEnabled = true;  //added by eric-zhao
	private boolean isAimLightEnabled = true;  //added by eric-zhao

	private boolean isKeyEventEnabled = false;

	private boolean isPhoneNumberEnabled = false;
	
    /*private static final int OUTPUT_DIRECT = 0;
    private static final int OUTPUT_BROADCAST = 1;
	private static final int OUTPUT_OVERWRITE = 2;
    private static final int OUTPUT_CLIP = 3;*/

    private static final String OUTPUT_DIRECT = "OUTPUT_DIRECT";
    private static final String OUTPUT_BROADCAST = "OUTPUT_BROADCAST";
	private static final String OUTPUT_OVERWRITE = "OUTPUT_OVERWRITE";
    private static final String OUTPUT_CLIP = "OUTPUT_CLIP";
	
    private static final int READ_MODE_ONE_TIME = 0;
    private static final int READ_MODE_CONTINUE = 1;
    private final static int HSM_DECODE_LIB = 0;
    private final static int ZBAR_DECODE_LIB = 1;
    private final static int SHENGYUAN_DECODE_LIB = 2;
    private final static int CORTEX_DECODE_LIB = 3;

	/*private final static int CHAR_NULL = 0;
    private final static int CHAR_SPACE = 1;
    private final static int CHAR_ENTER = 2;
    private final static int CHAR_TAB = 3;
	private final static int CHAR_NO = 4;*/

	private final static String CHAR_NULL = "CHAR_NULL";
    private final static String CHAR_SPACE = "CHAR_SPACE";
    private final static String CHAR_ENTER = "CHAR_ENTER";
    private final static String CHAR_TAB = "CHAR_TAB";
	private final static String CHAR_NO = "CHAR_NO";
	
    private Intent intent;
    Handler handler;

    //from fragmentActivity
    public static String PREF_KEY_SCAN_OUTPUT_MODE = "key_scan_output_mode";
    public static String PREF_KEY_SCAN_OUTPUT_OVERRIDE = "key_scan_output_override";
    public static String PREF_KEY_SCAN_OUTPUT_AUTORETURN = "key_scan_output_autoreturn";
	public static String PREF_KEY_SCAN_PREFIX_CHAR = "key_scan_prefix_char";
	public static String PREF_KEY_SCAN_SUFFIX_CHAR = "key_scan_suffix_char";
	public static String PREF_KEY_SCAN_BROADCAST_ACTION = "key_scan_broadcast_action";
    public static String PREF_KEY_SCAN_BROADCAST_EXTRA = "key_scan_broadcast_extra";
	public static String PREF_KEY_SCAN_ENABLE = "key_scan_enable";  //eric-zhao added
	public static String PREF_KEY_SCAN_SOUND_ALERT = "key_scan_sound_alert";
    public static String PREF_KEY_SCAN_VIBRATE_ALERT = "key_scan_vibrate_alert";
    public static String PREF_KEY_SCAN_CONTINUE_NOTIMEOUT = "scan_continue_notimeout";
	public static String PREF_KEY_SCAN_CENTER_ENABLE = "key_scan_center_enable";
	public static String PREF_KEY_SCAN_ILLLight_ENABLE = "key_scan_ill_enable";
	public static String PREF_KEY_SCAN_AIMLight_ENABLE = "key_scan_aim_enable";
	public static String PREF_KEY_SCAN_KEYEVENT_ENABLE = "key_scan_keyevent_enable";
    public static String PREF_KEY_SCAN_PHONENUMBER_ENABLE = "key_scan_phonenumber_enable";


    public RelativeLayout relSetting;
    public RelativeLayout relTest;
    public ImageView imgSetting;
    public ImageView imgTest;
    private static Fragment[] framents;

    //public RqPDAClient client;

   // public boolean isFloatingScanEnabled = false;
    public boolean isScanOpened = false;
	


   // public int outMode;
    public boolean output_autoreturn = false;
   // public boolean test_output_autoreturn = false;

    public int readMode;
    public int scan_read_interval;
    public int scan_read_timeout=5;
    public int scan_read_continue_notimeout = 0;//eric-zhao

    public boolean scan_read_repeat = false;
    public boolean scan_read_downscanner = false;

    public String mPrefixChar="";
	public String mSuffixChar="";
    public String scan_prefix_code="";
    public String scan_suffix_code="";

	private String curStrPrefix;
	private String curStrSuffix;

    public boolean isEnterOn;
	public boolean isTabOn;


	public String  broadcast_Action;
	public String  broadcast_Extra;

	
    public int scan_brightness;

    public boolean scan_alert_sound = true;
    public boolean scan_alert_vibrate = true;

    public boolean isImageViewShow = false;


    public int scanner_decode_type = CORTEX_DECODE_LIB;

    public long scanStartTime;
    public long scanTime = 0;

    public static final String myPref = "ScanControlPreference";

    public static final boolean DEBUG = true;

    public SharedPreferences pref;  //eric-zhao

	//public SharedPreferences scanPref;  //eric-zhao

	public String LOGIN_CODE = "176993";  //for JR008
	public String JR_ACTION = "jr.android.server.scannerservice.broadcast";
	public String JR_EXTRA = "barcodedata";

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private int OVERLAY_PERMISSION_REQ_CODE = 2;
    private Intent intentTakePic;
    private static String[] PERMISSIONS_REQUIRED = {
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.SYSTEM_ALERT_WINDOW"};

    
    //for DEBUG
	private ClipboardManager mClipboardManager;
    private ClipData mClipData;


    public  ProgressDialog pd;



    @Override
    public void onAttach(Activity activity) {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onAttach");
        super.onAttach(activity);
        mActivity = (ScanSettingActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (ScanSettingActivity.DEBUG) Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.scan_setting_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // if (ScanSettingActivity.DEBUG)
			Log.d(TAG, "onActivityCreated");

        initPara();//扫描中的接口jar包
        mClipboardManager = (ClipboardManager)mActivity.getSystemService(Context.CLIPBOARD_SERVICE);  //for DEBUG

        //begin
        handler = new Handler();
        relOnScan = (RelativeLayout)  getActivity().findViewById(R.id.relOnScan);
        relOutput = (RelativeLayout)  getActivity().findViewById(R.id.relOutput);
        //relCallback = (RelativeLayout) findViewById(R.id.relCallback);
        relReadcode = (RelativeLayout)  getActivity().findViewById(R.id.relReadcode);
        relTrigger = (RelativeLayout) getActivity().findViewById(R.id.relTrigger);
        relPrefixSuffix = (RelativeLayout)  getActivity().findViewById(R.id.relPrefixSuffix);
        relSuffix = (RelativeLayout)  getActivity().findViewById(R.id.relSuffix);
        relDecoding = (RelativeLayout)  getActivity().findViewById(R.id.relDecoding);
        //relDecodeType = (RelativeLayout) findViewById(R.id.relDecodeType);
        relDecodeMode = (RelativeLayout)  getActivity().findViewById(R.id.relDecodeMode);
        relBroadcast = (RelativeLayout)  getActivity().findViewById(R.id.relBroadcast);
		relPkgSetting = (RelativeLayout)  getActivity().findViewById(R.id.relBestPkg);
		txtPkgSetting= (TextView)  getActivity().findViewById(R.id.txtPkgSetting); //for BEST
		

		relScanCenter = (RelativeLayout) getActivity().findViewById(R.id.relScanCenter);
		relScanLight = (RelativeLayout) getActivity().findViewById(R.id.relScanLight);
        relScanKeyEvent = (RelativeLayout) getActivity().findViewById(R.id.relKeyEvent);

		relPhoneNum= (RelativeLayout)  getActivity().findViewById(R.id.relphoneSheet);

        txtReadcodeMode = (TextView)  getActivity().findViewById(R.id.txtReadcodeMode);
        Log.e("UUU","-----"+readMode);

        txtOutputMode = (TextView)  getActivity().findViewById(R.id.txtOutputMode);
        txtOutputMode.setText(getStringOfOutput(mOutMode));

        txtPrefixValue = (TextView)  getActivity().findViewById(R.id.txtPrefixValue);
        txtPrefixValue.setText(getStringOfPrefix());

        txtSuffixValue = (TextView)  getActivity().findViewById(R.id.txtSuffixValue);
        txtSuffixValue.setText(getStringOfSuffix());

        //txtDecodeTypeMode = (TextView) findViewById(R.id.txtDecodeTypeMode);
        //txtDecodeTypeMode.setText(getStringOfDecodeType(scanner_decode_type));



        relOnScan.setOnClickListener(this);
        relOutput.setOnClickListener(this);
        relReadcode.setOnClickListener(this);
        relTrigger.setOnClickListener(this);
        relPrefixSuffix.setOnClickListener(this);
        relSuffix.setOnClickListener(this);
        relDecoding.setOnClickListener(this);
        // relDecodeType.setOnClickListener(this);
        relDecodeMode.setOnClickListener(this);
        relBroadcast.setOnClickListener(this);
		relPkgSetting.setOnClickListener(this); //for BEST
		relScanCenter.setOnClickListener(this);
		relScanLight.setOnClickListener(this);
		relScanKeyEvent.setOnClickListener(this);
		relPhoneNum.setOnClickListener(this);

        startScan = (CheckBox)  getActivity().findViewById(R.id.startScan);//开启扫描框
        if (isScanOpened) {
            startScan.setChecked(true);//框框为true
        } else {
            startScan.setChecked(false);//框框为false
        }
        isScanChecked = isScanOpened;  //added by eric-zhao


        onUIState(isScanOpened);

         startScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG) Log.d(TAG, "startScan onCheckedChanged isChecked:="+isChecked);

				if(pd == null){

					pd = ProgressDialog.show(mActivity,"",getResources().getString(R.string.str_wait));  //  "waiting..."
					pd.setProgressStyle(R.style.custDialog);
					pd.setCancelable(false);	
					startScan.setClickable(false);

				}else{
				  
                   return;
				}	

                mActivity.client.enableSystemScan(isChecked);//上下电扫描头的设置client.enableSystemScan(true);
                isScanOpened = isChecked;
                mActivity.writeToPreference(PREF_KEY_SCAN_ENABLE, isChecked == true? "true":"false");//eric-zhao added
                if (isChecked) {//悬浮按钮开关的设置
                    //悬浮框的状态为true、现在的悬浮框不在
                    if (isFloatingScanEnabled) {
                        intent = new Intent(mActivity, FloatViewService.class);
                        mActivity.startService(intent);
                    }
                } else {
                    if (isFloatingScanEnabled) {
                        mActivity.stopService(intent);
                    }
                }

                onUIState(isChecked);//扫描开启键的状态来让各种字体颜色变化
                
                handler.postDelayed(dismissDialogRunnable, 800); //added by eric-zhao for switching the button rapidly
            }
        });  
    }

	 Runnable dismissDialogRunnable = new Runnable() { //added by eric-zhao
        public void run() {
            if (DEBUG) Log.d(TAG, "dismissDialogRunnable.");
            if(pd != null){
               pd.dismiss();
			   pd =null;
			  //relOnScan.setClickable(true);
			   startScan.setClickable(true);
            }	
        }
    };


 /*
   // @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // verifyPermissions(this);
        super.onCreate(savedInstanceState);

        client = RqPDAClient.newClient(this);
        //client.enableSystemScan(true);   // eric-zhao

		

        setContentView(R.layout.scan_setting_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//横屏显示


        ((MyApplication)getApplication()).setmActivity(this);
        pref = getSharedPreferences("scan_continue_notimeout", Context.MODE_MULTI_PROCESS); //eric-zhao
       
        
        initPara();//扫描中的接口jar包
       
       //eric-zhao  DEBUG no NOTED
       //
         //IntentFilter intentFilter = new IntentFilter();
       // intentFilter.addAction("com.android.receive_scan_action"); //||action.equals("com.android.receive_scan_action")
       // registerReceiver(mReceiver, intentFilter);
      //
	//end of DEBUG	

		mClipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  //for DEBUG

		//scanPref = getSharedPreferences(myPref,Context.MODE_MULTI_PROCESS);
		//scanPref.registerOnSharedPreferenceChangeListener(listener);

    }
    */


	
    //for DEBUG
  /*  private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
         
            if (action.equals("com.android.receive_scan_action")) {

				 String barcode = intent.getStringExtra("data");

				 Log.d(TAG, "mReceiver com.android.receive_scan_action  barcode:= "+barcode );

			 }
        }
    };  
	*/


    private void onUIState(boolean state) {
        if (DEBUG) Log.d(TAG, "onUIState. state = " + state);

       /* relOnScan.setEnabled(false);
        startScan.setEnabled(false);
        handler.removeCallbacks(doScanEnableRunnable);
        handler.postDelayed(doScanEnableRunnable, 200); */   //DEBUG eric-zhao TODO
        
        relOnScan.setEnabled(true);
        startScan.setEnabled(true);
		
        relOutput.setEnabled(state);
		// relCallback.setEnabled(state);
        relReadcode.setEnabled(state);
        relTrigger.setEnabled(state);
        relPrefixSuffix.setEnabled(state);
	    relSuffix.setEnabled(state);
        relDecoding.setEnabled(state);
		relScanCenter.setEnabled(state);
		relScanLight.setEnabled(state);
        relScanKeyEvent.setEnabled(state);
		relPhoneNum.setEnabled(state);

		

       // relDecodeType.setEnabled(state);

        relDecodeMode.setEnabled(state);
	    relBroadcast.setEnabled(state);
		 relPkgSetting.setEnabled(state);

        // startScan.setClickable(false);  //for R.id.relOnScan


		 

		 if(getDeviceName().equals("BL-798E"))  //for BEST
		 	 relPkgSetting.setVisibility(View.VISIBLE);

        if (state) {
            ((TextView)  getActivity().findViewById(R.id.txtOutput)).setTextColor(Color.parseColor("#000000"));
			// ((TextView) findViewById(R.id.txtCallback)).setTextColor(Color.parseColor("#000000"));
            ((TextView)  getActivity().findViewById(R.id.txtReadcode)).setTextColor(Color.parseColor("#000000"));
            ((TextView) getActivity().findViewById(R.id.txtTrigger)).setTextColor(Color.parseColor("#000000"));
            ((TextView)  getActivity().findViewById(R.id.txtPrefix)).setTextColor(Color.parseColor("#000000"));
            ((TextView)  getActivity().findViewById(R.id.txtSuffix)).setTextColor(Color.parseColor("#000000"));
            ((TextView)  getActivity().findViewById(R.id.txtDecoding)).setTextColor(Color.parseColor("#000000"));

          //  ((TextView) findViewById(R.id.txtDecodeType)).setTextColor(Color.parseColor("#000000"));

            ((TextView)  getActivity().findViewById(R.id.txtDecodeMode)).setTextColor(Color.parseColor("#000000"));  //
            ((TextView)  getActivity().findViewById(R.id.txtDecodeBroadcast)).setTextColor(Color.parseColor("#000000"));
        } else {
            ((TextView)  getActivity().findViewById(R.id.txtOutput)).setTextColor(Color.parseColor("#ffa9a9a9"));
			// ((TextView) findViewById(R.id.txtCallback)).setTextColor(Color.parseColor("#ffa9a9a9"));
            ((TextView)  getActivity().findViewById(R.id.txtReadcode)).setTextColor(Color.parseColor("#ffa9a9a9"));
            ((TextView) getActivity().findViewById(R.id.txtTrigger)).setTextColor(Color.parseColor("#ffa9a9a9"));
            ((TextView)  getActivity().findViewById(R.id.txtPrefix)).setTextColor(Color.parseColor("#ffa9a9a9"));
            ((TextView)  getActivity().findViewById(R.id.txtSuffix)).setTextColor(Color.parseColor("#ffa9a9a9"));
            ((TextView)  getActivity().findViewById(R.id.txtDecoding)).setTextColor(Color.parseColor("#ffa9a9a9"));
           // ((TextView) findViewById(R.id.txtDecodeType)).setTextColor(Color.parseColor("#ffa9a9a9"));

            ((TextView)  getActivity().findViewById(R.id.txtDecodeMode)).setTextColor(Color.parseColor("#ffa9a9a9"));
		    ((TextView)  getActivity().findViewById(R.id.txtDecodeBroadcast)).setTextColor(Color.parseColor("#ffa9a9a9"));
        }
		
         ((TextView)  getActivity().findViewById(R.id.txtOnScan)).setTextColor(Color.parseColor("#000000"));  // #ffa9a9a9
    }

    Runnable doScanEnableRunnable = new Runnable() {
        public void run() {
            if (DEBUG) Log.d(TAG, "doScanEnableRunnable.");
            relOnScan.setEnabled(true);
            startScan.setEnabled(true);
            ((TextView)  getActivity().findViewById(R.id.txtOnScan)).setTextColor(Color.parseColor("#000000"));
        }
    };

    @Override
    public void onClick(View v) {

	 Log.d(TAG, "onClick.");
        switch (v.getId()) {
         //eric-zhao removed
		/*	case R.id.relOnScan:
				if (DEBUG) Log.d(TAG, "onClick  R.id.relOnScan isScanChecked:= "+isScanChecked);
                if (isScanChecked) {
                    isScanChecked = false;
                    startScan.setChecked(false);//框
                } else {
                    isScanChecked = true;
                    startScan.setChecked(true);
                }
				if(pd == null){

					pd = ProgressDialog.show(mActivity,"",getResources().getString(R.string.str_wait));  //  "waiting..."
					pd.setProgressStyle(R.style.custDialog);
					pd.setCancelable(false);	
					relOnScan.setClickable(false);

				}else{
				  
                   return;
				}	

				 mActivity.client.enableSystemScan(isScanChecked);//上下电扫描头的设置client.enableSystemScan(true);
                isScanOpened = isScanChecked;
                mActivity.writeToPreference(PREF_KEY_SCAN_ENABLE, isScanChecked == true? "true":"false");//eric-zhao added
                if (isScanChecked) {//悬浮按钮开关的设置
                    //悬浮框的状态为true、现在的悬浮框不在
                    if (isFloatingScanEnabled) {
                        intent = new Intent(mActivity, FloatViewService.class);
                        mActivity.startService(intent);
                    }
                } else {
                    if (isFloatingScanEnabled) {
                        mActivity.stopService(intent);
                    }
                }

                onUIState(isScanChecked);//扫描开启键的状态来让各种字体颜色变化
                
                handler.postDelayed(dismissDialogRunnable, 800); //added by eric-zhao for switching the button rapidly
				
                break;
				*/
		 		
            case R.id.relOutput:
               dialogOutput();
               
               break;
            case R.id.relReadcode:
                dialogReadcode();
                break;
             case R.id.relTrigger:
                dialogTrigger();
                break;
            case R.id.relPrefixSuffix:
                dialogPrefix();
                break;
			case R.id.relSuffix:
                dialogSuffix();
                break;	
            case R.id.relDecoding:
                dialogDecoding();
                break;

           /* case R.id.relDecodeType:
                dialogScannerDecodeType();
                break;*/

            case R.id.relDecodeMode: 
                 Intent intent = new Intent();
                 intent.setClassName("com.rq.scan", "com.rq.scan.ConfigSettingsActivity");
                 startActivity(intent);
                break;

			case R.id.relBroadcast:
                //dialogBroadcast();
                dialogLogin(0);
                break;

			case R.id.relBestPkg:
                
                dialogPkgSet();
                break;	

			 case R.id.relScanCenter:  //for ScanCenter
                 dialogLogin(2);
                break;	

			 case R.id.relScanLight:  //for ScanCenter
                dialogScanLight();
                break;		


			case R.id.relKeyEvent:  //for ScanCenter
                dialogKeyEvent();
                break;	

			case R.id.relphoneSheet:
               
                dialogLogin(1);
                break;	
				
            
        }
    }

   

    @Override
    public void onResume() {
        super.onResume();
        startScan.setChecked(mActivity.client.getSystemScanState());
      // mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST);
       if(getDeviceName().equals("JR008")|| getDeviceName().equals("BL-798E")){ //for BEST
	       mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST); //  for JR008
	       outMode = "rbBroadcast_output";
        }else{
           mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT); //  for  others
           outMode = "rbDirect_filling";
        }
	   txtOutputMode.setText(getStringOfOutput(mOutMode));

	  

       Log.d(TAG,"onResume  mOutMode:="+mOutMode);
        //added by wxw start
        //连续扫描状态
        if (mActivity.client.isContinueScanEnabled()){
            readMode = READ_MODE_CONTINUE;
            setting_readMode = "radioContinuous";
        }
        else{
            readMode = READ_MODE_ONE_TIME;
            setting_readMode = "radioRelease";
        }
        txtReadcodeMode.setText(getStringOfReadMode(readMode));
        //added by wxw end
       /* scanCallback = (CheckBox) findViewById(R.id.ScanCallback);     //reserved
        if (mOutMode == OUTPUT_BROADCAST) {
             scanCallback.setChecked(true); 
         } else {
            scanCallback.setChecked(false); 
        }
		scanCallback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG) Log.d(TAG, "scanCallback onCheckedChanged isChecked:="+isChecked);
               
                if (isChecked) { 
                     
                   client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                   mOutMode = OUTPUT_BROADCAST;
                } else {
                    client.setScanReceiveMode(ScanReceiveMode.AUTO_ENTER_IN_FOCUS);
                        mOutMode = OUTPUT_DIRECT;
                }
               
            }
        });  */  //eric-zhao
        //end

	   
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // for FloatView
            requestDrawOverLaysPermission();
        }*/

        if (DEBUG) Log.d(TAG, "onResume");
    }

   @Override
    public void onHiddenChanged(boolean hidden) {
        if (DEBUG) Log.d(TAG, "onHiddenChanged. hidden = " + hidden);
        super.onHiddenChanged(hidden);

		if(getDeviceName().equals("JR008")|| getDeviceName().equals("BL-798E")){ //for BEST
	       mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST); //  for JR008
	       outMode = "rbBroadcast_output";
        }else{
           mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT); //  for  others
           outMode = "rbDirect_filling";
        }
	   txtOutputMode.setText(getStringOfOutput(mOutMode));

   	}

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "onDestroy.");
		//scanPref.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
        if (intent != null) {
            mActivity.stopService(intent);
        }
    }

    private int getStringOfOutput(String mode) {
        if (mode.equals(OUTPUT_BROADCAST))
            return R.string.broadcast_output;
        else if (mode.equals(OUTPUT_DIRECT))
            return R.string.direct_filling;

		 else if (mode.equals(OUTPUT_OVERWRITE))
            return R.string.override_Output;
        else
            return R.string.clipboard_out;
    }

    private int getStringOfReadMode(int val) {
        if (val == READ_MODE_ONE_TIME) {
            if (scan_read_downscanner) {
                return R.string.press_enable_light;
            } else {
                return R.string.press_reading;
            }
        } else if (val == READ_MODE_CONTINUE)
            return R.string.continuous_reading;
        else
            return R.string.press_reading;
    }

    private int getStringOfDecodeType(int val) {
        if (val == CORTEX_DECODE_LIB)
            return R.string.scanner_decode_cortex;
        else if (val == SHENGYUAN_DECODE_LIB)
            return R.string.scanner_decode_shengyuan;
        else
            return R.string.scanner_decode_none;
    }


	 private String getStringOfPrefix() {
       String val;

       if(scan_prefix_code.equals(""))
	   	  val = getString(R.string.str_no_prefix_char);
	   else
	     val = getString(R.string.str_suffix_value,  scan_prefix_code);

	   return val;
    }

	 private String getStringOfSuffix() {
       String val="";
       Log.d(TAG,"getStringOfSuffix mSuffixChar:="+mSuffixChar+" scan_suffix_code:="+scan_suffix_code);
	   if(scan_suffix_code.equals("")){
		   if(mSuffixChar.equals(CHAR_NULL))
		   	 val = getString(R.string.str_no_suffix_char);
		   else if(mSuffixChar.equals(CHAR_SPACE))
		   	 val = getString(R.string.str_suffix_value,  getString(R.string.str_suffix_Space));
		   else if(mSuffixChar.equals(CHAR_ENTER))
		   	 val = getString(R.string.str_suffix_value,  getString(R.string.str_suffix_Enter));
		   else if(mSuffixChar.equals(CHAR_TAB))
		   	 val = getString(R.string.str_suffix_value,  "Tab");
		   /*else //for CHAR_NO
		   	 //val = getString(R.string.str_suffix_value,  scan_suffix_code);
		   	 val = getString(R.string.str_no_suffix_char);*/
	   }else{
             if(mSuffixChar.equals(CHAR_NULL))
		   	 val = getString(R.string.str_suffix_value,  scan_suffix_code);
		   else if(mSuffixChar.equals(CHAR_SPACE))
		   	 val = getString(R.string.str_suffix_value,  (scan_suffix_code+getString(R.string.str_suffix_Space)));
		   else if(mSuffixChar.equals(CHAR_ENTER))
		   	 val = getString(R.string.str_suffix_value,  (scan_suffix_code+getString(R.string.str_suffix_Enter)));
		   else if(mSuffixChar.equals(CHAR_TAB))
		   	 val = getString(R.string.str_suffix_value,  (scan_suffix_code+"Tab"));
		   /*else //for CHAR_NO
		   	 //val = getString(R.string.str_suffix_value,  scan_suffix_code);
		   	  val = getString(R.string.str_suffix_value,  scan_suffix_code);*/
	   	}
	   return val;
    } 

    private void dialogOutput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.output_dialog, null);
        RadioGroup radioGroupOutput = (RadioGroup) v.findViewById(R.id.radioGroupOutput);
        RadioButton rbBroadcast_output = (RadioButton) v.findViewById(R.id.rbBroadcast_output);
        RadioButton rbDirect_filling = (RadioButton) v.findViewById(R.id.rbDirect_filling);

       // RadioGroup radioGroupOutputType = (RadioGroup) v.findViewById(R.id.radioGroupOutputType);
        RadioButton rbOverride = (RadioButton) v.findViewById(R.id.rbOverride);
      // RadioButton chAutomatic = (RadioButton) v.findViewById(R.id.chAutomatic);
         RadioButton rbClipBoard = (RadioButton) v.findViewById(R.id.rbClipboard);

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        txtOutput = txtOutputMode.getText().toString();

        builder.setView(v);

        switch (mOutMode) {
            case OUTPUT_BROADCAST:
                rbBroadcast_output.setChecked(true);
                rbDirect_filling.setChecked(false);
				rbOverride.setChecked(false);
                rbClipBoard.setChecked(false);
                break;

            case OUTPUT_DIRECT:
                rbBroadcast_output.setChecked(false);
                rbDirect_filling.setChecked(true);
				rbOverride.setChecked(false);
                rbClipBoard.setChecked(false);
                break;


			 case OUTPUT_OVERWRITE:
                rbBroadcast_output.setChecked(false);
                rbDirect_filling.setChecked(false);
				rbOverride.setChecked(true);
                rbClipBoard.setChecked(false);
                break;

             case OUTPUT_CLIP:
                rbBroadcast_output.setChecked(false);
                rbDirect_filling.setChecked(false);
				rbOverride.setChecked(false);
                rbClipBoard.setChecked(true);
                break;	
        }

        /*if (output_autoreturn) {
            chOverride.setChecked(false);
            chAutomatic.setChecked(true);
        } else {
            chOverride.setChecked(true);
            chAutomatic.setChecked(false);
        }*/

        final AlertDialog dialog = builder.show();

		radioGroupOutput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbBroadcast_output:
                        outMode = "rbBroadcast_output";
                        break;
                    case R.id.rbDirect_filling:
                        outMode = "rbDirect_filling";
                        break;
					 case R.id.rbOverride:
                        outMode = "rbOverride_output";
                        break;
                     case R.id.rbClipboard:
                        outMode = "rbClipboard_output";
                        break;	
                }
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (outMode) {
                    case "rbBroadcast_output":
                        txtOutput = getResources().getString(R.string.broadcast_output);
                        mActivity.client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                        mActivity.client.enableOverWriteOutput(false);
                        mOutMode = OUTPUT_BROADCAST;
                        mActivity.writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST);
						 Log.d(TAG,"rbBroadcast_output setScanReceiveMode ");
                        break;
                    case "rbDirect_filling":
                        txtOutput = getResources().getString(R.string.direct_filling);
                        mActivity.client.setScanReceiveMode(ScanReceiveMode.AUTO_ENTER_IN_FOCUS);
                        mActivity.client.enableOverWriteOutput(false);
                        mOutMode = OUTPUT_DIRECT;
                        mActivity.writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT);
                        break;

					case "rbOverride_output":    
                        txtOutput = getResources().getString(R.string.override_Output);
                        mActivity.client.setScanReceiveMode(ScanReceiveMode.AUTO_ENTER_IN_FOCUS);  //NOTED
                        mActivity.client.enableOverWriteOutput(true);
                        mOutMode = OUTPUT_OVERWRITE;
                        mActivity.writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_OVERWRITE);
						 Log.d(TAG,"rbOverride_output setScanReceiveMode ");
                        break;
                    case "rbClipboard_output":
                        txtOutput = getResources().getString(R.string.clipboard_out);
                        mActivity.client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                        mActivity.client.enableOverWriteOutput(false);
                        mOutMode = OUTPUT_CLIP;
                        mActivity.writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_CLIP);
                        break;	
                    default:
                        break;
                }
               
               
                txtOutputMode.setText(txtOutput);

                //for DEBUG
				/*Intent custIntent = new Intent();
				 custIntent.setAction("jr.android.scanner.service_settings");
                 custIntent.putExtra("barcode_send_mode", "CLIPBOARD");
				 sendBroadcast(custIntent);*/
   
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
       

     
    }

    private void dialogReadcode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.readcode_dialog, null);

        final RadioGroup radioGroupReadcode = (RadioGroup) v.findViewById(R.id.radioGroupReadcode);
        RadioButton radioRelease = (RadioButton) v.findViewById(R.id.radioRelease);
        //RadioButton radioReleaseLight = (RadioButton) v.findViewById(R.id.radioReleaseLight); //reserved
        RadioButton radioContinuous = (RadioButton) v.findViewById(R.id.radioContinuous);

       CheckBox radioContinuousNotimeout = (CheckBox) v.findViewById(R.id.radioContinuousNotimeout);

        final EditText edit_scanning_interval = (EditText) v.findViewById(R.id.edit_scanning_interval);
        final EditText edit_reading_timeout = (EditText) v.findViewById(R.id.edit_reading_timeout);

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        txtReadcode = txtReadcodeMode.getText().toString();

        builder.setView(v);

        edit_scanning_interval.setText(String.valueOf(scan_read_interval));
        edit_reading_timeout.setText(String.valueOf(scan_read_timeout));

        if (DEBUG)
            Log.d(TAG, "dialogReadcode  readMode: " + readMode);
        switch (readMode) {
            case READ_MODE_ONE_TIME: {
               /* if (scan_read_downscanner) {
                    radioRelease.setChecked(false);
                    radioReleaseLight.setChecked(true);
                    radioContinuous.setChecked(false);
                    edit_scanning_interval.setEnabled(false);
                } else*/ {
                    radioRelease.setChecked(true);
                   // radioReleaseLight.setChecked(false);
                    radioContinuous.setChecked(false);
                    edit_scanning_interval.setEnabled(false);
                }
            }
            break;

            case READ_MODE_CONTINUE:
                radioRelease.setChecked(false);
               // radioReleaseLight.setChecked(false);
                radioContinuous.setChecked(true);
                edit_scanning_interval.setEnabled(true);
                break;
        }
        Log.d(TAG,"scan_read_continue_notimeout:="+scan_read_continue_notimeout);
         if (scan_read_continue_notimeout ==1)
             radioContinuousNotimeout.setChecked(true);
         else
             radioContinuousNotimeout.setChecked(false);

        final AlertDialog dialog = builder.show();
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroupReadcode.getCheckedRadioButtonId() == R.id.radioContinuous) {
                    if (TextUtils.isEmpty(edit_scanning_interval.getText().toString())) {
                          scan_read_interval = 0;
                    } else {
                        scan_read_interval = Integer.parseInt(edit_scanning_interval.getText().toString());
                    }
                    if (scan_read_interval <= 0)
                          scan_read_interval = 0;
                    else if (scan_read_interval > 255000)
                        scan_read_interval = 255000;

                    mActivity.client.setContinueScanInterval(scan_read_interval);
                    Log.d(TAG, "onClick2:" + scan_read_timeout);

                }
                Log.d(TAG, "onClick3:" + scan_read_timeout);
                if (!TextUtils.isEmpty(edit_reading_timeout.getText().toString())) {
                    scan_read_timeout = Integer.parseInt(edit_reading_timeout.getText().toString());
                    if (scan_read_timeout < 5)
                        scan_read_timeout = 5;
                    if (DEBUG)
                        Log.d(TAG, "dialogReadcode  scan_read_timeout: " + scan_read_timeout);

                    mActivity.client.setScannerTimeout(scan_read_timeout * 1000);
                }
				if (DEBUG)
                  Log.d(TAG, "dialogReadcode  setting_readMode: " + setting_readMode);
                switch (setting_readMode) {
                    case "radioRelease":
                        txtReadcode = getResources().getString(R.string.press_reading);
                        readMode = READ_MODE_ONE_TIME;
                        mActivity.client.enableDownScanUntilTimeout(false);
                        mActivity.client.enableContinueScan(false);
                        scan_read_downscanner = false;
                        break;
                   /* case "radioReleaseLight":
                        Log.d("wxw","radioReleaseLight");
                        txtReadcode = getResources().getString(R.string.press_enable_light);
                        readMode = READ_MODE_ONE_TIME;
                        client.enableDownScanUntilTimeout(true);
                        client.enableContinueScan(false);
                        scan_read_downscanner = true;
                        break;*/
                    case "radioContinuous":
                        txtReadcode = getResources().getString(R.string.continuous_reading);
                        readMode = READ_MODE_CONTINUE;
                        mActivity.client.enableContinueScan(true);
                        break;
                }
                txtReadcodeMode.setText(txtReadcode);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        radioGroupReadcode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioRelease:
                        edit_scanning_interval.setEnabled(false);
                        setting_readMode = "radioRelease";
                        break;
                   /* case R.id.radioReleaseLight:
                        edit_scanning_interval.setEnabled(false);
                        setting_readMode = "radioReleaseLight";
                        break;*/
                    case R.id.radioContinuous:
                        edit_scanning_interval.setEnabled(true);
                        setting_readMode = "radioContinuous";
                        break;
                }
                txtReadcodeMode.setText(txtReadcode);
            }
        });


         radioContinuousNotimeout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // mActivity.client.setContinueScanningNoTimeout(isChecked);
                 //isscanreadcontinuenotimeout = isChecked;
                 int value = isChecked? 1:0;   //added by eric-zhao

                 Log.d(TAG, "enableContinueScanNoTimeout  isChecked:" + isChecked);
                 mActivity.client.enableContinueScanNoTimeout(isChecked);
                 /*Intent intent = new Intent();
                 intent.setAction("com.rq.open_continue_scan_notimeout");
                 intent.putExtra("open_continue_scan_notimeout",isChecked);
                 sendBroadcast(intent);*/

               
                 scan_read_continue_notimeout =value;
                 mActivity.writeToPreference(PREF_KEY_SCAN_CONTINUE_NOTIMEOUT,String.valueOf(value));
                 /*Editor editor = pref.edit();
                 editor.putInt("key", value);
                 editor.commit();*/
             }
         });
    }

     private void dialogTrigger() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);  //eric debug
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.trigger_dialog, null);

        CheckBox floatSCANtrigger = (CheckBox) v.findViewById(R.id.floatSCANtrigger);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

        floatSCANtrigger.setChecked(isFloatingScanEnabled);

	

		 Log.d(TAG, "dialogTrigger isFloatingScanEnabled :" + isFloatingScanEnabled);

        final AlertDialog dialog = builder.show();

        floatSCANtrigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFloatingScanEnabled = isChecked;
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFloatingScanEnabled) {
                    intent = new Intent(mActivity, FloatViewService.class);
                    mActivity.startService(intent);
                    isFloatingScanEnabled = isFloatingScanEnabled;
                } else {
                    if (intent != null) {
                        mActivity.stopService(intent);
                    }
                    isFloatingScanEnabled = isFloatingScanEnabled;
                }
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void dialogPrefix() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.prefix_dialog, null);

        final EditText editPrefix = (EditText) v.findViewById(R.id.editPrefix);
       // final EditText editSuffix = (EditText) v.findViewById(R.id.editSuffix);


	   /* RadioGroup radioGroupChar = (RadioGroup) v.findViewById(R.id.radioGroupPrefixChar);
        RadioButton rbChar_Null = (RadioButton) v.findViewById(R.id.CharNull);
        RadioButton rbChar_Space = (RadioButton) v.findViewById(R.id.CharSpace);
		RadioButton rbChar_Enter = (RadioButton) v.findViewById(R.id.CharEnter);
		RadioButton rbChar_Tab = (RadioButton) v.findViewById(R.id.CharTab);*/
		

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

		Log.d(TAG, "dialogPrefix scan_prefix_code:= " + scan_prefix_code);

	/*	if(!scan_prefix_code.equals("")){
            rbChar_Null.setChecked(false);
            rbChar_Space.setChecked(false);
			rbChar_Enter.setChecked(false);
            rbChar_Tab.setChecked(false);
            editPrefix.setText(AsciiStr2Str(scan_prefix_code));
		}else{	

				 switch (mPrefixChar) {
		            case CHAR_NULL:
		                rbChar_Null.setChecked(true);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

		            case CHAR_SPACE:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(true);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

					case CHAR_ENTER:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(true);
		                rbChar_Tab.setChecked(false);
		                break;	

					case CHAR_TAB:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(true);
		                break;	
		        }
			}*/
           
      
        if(!scan_prefix_code.equals("")){
           
            editPrefix.setText(scan_prefix_code);//AsciiStr2Str(scan_prefix_code)
		}

        final AlertDialog dialog = builder.show();

	  /*  radioGroupChar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.CharNull:
                        mPrefixChar= CHAR_NULL;
                        break;
                    case R.id.CharSpace:
                        mPrefixChar= CHAR_SPACE;
                        break;

					case R.id.CharEnter:
                        mPrefixChar= CHAR_ENTER;
                        break;
                    case R.id.CharTab:
                        mPrefixChar= CHAR_TAB;
                        break;	
                }
				writeIntToPreference(PREF_KEY_SCAN_PREFIX_CHAR, mPrefixChar);
				scan_prefix_code = "";
				curStrPrefix = getString(R.string.str_prefix_value,  getStrChar(mPrefixChar));
                txtPrefixValue.setText(curStrPrefix);
				dialog.dismiss();
            }
        }); */

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStr = editPrefix.getText().toString();
				//if(!mStr.equals(""))
				 {
					if (DEBUG)
	                    Log.d(TAG, "before  mStr:" + mStr + " old: " + scan_prefix_code);
					
	                scan_prefix_code = mStr;//Str2AsciiStr(mStr);

                     mActivity.client.setScanResultPrefix(mStr);

					curStrPrefix = getStringOfPrefix(); //getString(R.string.str_prefix_value, mStr);
                    txtPrefixValue.setText(curStrPrefix);
	            } 
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                dialog.dismiss();
            }
        });
    }

    private String getStrChar(String val){

	   String str = "NULL";

	   switch (val) {
            case CHAR_NULL:
                str= "NULL";
                break;
            case CHAR_SPACE:
                str= "SPACE";
                break;

			case CHAR_ENTER:
                str= "ENTER";
                break;
            case CHAR_TAB:
                str= "TAB";
                break;
        }
	   return str; //eric-zhao
	   

    }	

    private void dialogSuffix() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.suffix_dialog, null);

         
        final EditText editSuffix = (EditText) v.findViewById(R.id.editSuffix);


	    RadioGroup radioGroupChar = (RadioGroup) v.findViewById(R.id.radioGroupSuffixChar);
        RadioButton rbChar_Null = (RadioButton) v.findViewById(R.id.CharNull);
        RadioButton rbChar_Space = (RadioButton) v.findViewById(R.id.CharSpace);
		RadioButton rbChar_Enter = (RadioButton) v.findViewById(R.id.CharEnter);
		RadioButton rbChar_Tab = (RadioButton) v.findViewById(R.id.CharTab);
		

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

        Log.d(TAG, "dialogSuffix  scan_suffix_code:= " + scan_suffix_code+" mSuffixChar:="+mSuffixChar);
        if(!scan_suffix_code.equals("")){
            editSuffix.setText(scan_suffix_code);
        }	
		switch (mSuffixChar) {
		            case CHAR_NULL:
		                rbChar_Null.setChecked(true);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

		            case CHAR_SPACE:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(true);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

					case CHAR_ENTER:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(true);
		                rbChar_Tab.setChecked(false);
		                break;	

					case CHAR_TAB:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(true);
		                break;	
		        }
		
		/*if(!scan_suffix_code.equals("")&& !mSuffixChar.equals(CHAR_SPACE)){
            rbChar_Null.setChecked(false);
            rbChar_Space.setChecked(false);
			rbChar_Enter.setChecked(false);
            rbChar_Tab.setChecked(false);
            editSuffix.setText(scan_suffix_code);  //AsciiStr2Str(scan_suffix_code)
		}else{	

				 switch (mSuffixChar) {
		            case CHAR_NULL:
		                rbChar_Null.setChecked(true);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

		            case CHAR_SPACE:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(true);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(false);
		                break;

					case CHAR_ENTER:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(true);
		                rbChar_Tab.setChecked(false);
		                break;	

					case CHAR_TAB:
		                rbChar_Null.setChecked(false);
		                rbChar_Space.setChecked(false);
						rbChar_Enter.setChecked(false);
		                rbChar_Tab.setChecked(true);
		                break;	
		        }
			}*/

       


        final AlertDialog dialog = builder.show();

	    radioGroupChar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.CharNull:
                        mSuffixChar= CHAR_NULL;
                        break;
                    case R.id.CharSpace:
                        mSuffixChar= CHAR_SPACE;
                        break;

					case R.id.CharEnter:
                        mSuffixChar= CHAR_ENTER;
                        break;
                    case R.id.CharTab:
                        mSuffixChar= CHAR_TAB;
                        break;	
                }
                mActivity.writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, mSuffixChar);
				//scan_suffix_code="";

				
				curStrSuffix = getStringOfSuffix();//getString(R.string.str_suffix_value,  getStrChar(mSuffixChar));
                txtSuffixValue.setText(curStrSuffix);

				if(mSuffixChar.equals(CHAR_ENTER)){
                    mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_ENTER);
					// client.setScanResultSuffix("");
				}		 
				else if(mSuffixChar.equals(CHAR_TAB)){
                    mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_TAB);
					 //client.setScanResultSuffix("");
				}else if(mSuffixChar.equals(CHAR_NULL)){
                    mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);//clear additionKey
					   
					 if(scan_suffix_code.equals(""))
                         mActivity.client.setScanResultSuffix("");
					 else
                         mActivity.client.setScanResultSuffix(scan_suffix_code.trim());
				}else{// for if(mSuffixChar == CHAR_SPACE)
				   if(scan_suffix_code.equals(""))
                       mActivity.client.setScanResultSuffix(" ");
				   else
                       mActivity.client.setScanResultSuffix(scan_suffix_code+" ");

                    mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);  //clear additionKey
				}

				// for debug

				/* Intent custIntent = new Intent();
				 custIntent.setAction("jr.android.scanner.service_settings");
                 custIntent.putExtra("endchar", "ENTER");
				 sendBroadcast(custIntent);*/

				dialog.dismiss();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                String mStr = editSuffix.getText().toString();
				//if(!mStr.equals(""))
				{
				   if (DEBUG)
	                    Log.d(TAG, "end  mStr:" + mStr + " old: " + scan_suffix_code+" mSuffixChar:="+mSuffixChar );

				   if(mSuffixChar.equals("CHAR_SPACE"))
                       scan_suffix_code = mStr+" ";
				   else
	                  scan_suffix_code = mStr;//Str2AsciiStr(mStr);	

                    mActivity.client.setScanResultSuffix(scan_suffix_code);
					
					//curStrSuffix = getString(R.string.str_suffix_value, mStr);
					curStrSuffix = getStringOfSuffix();
	                txtSuffixValue.setText(curStrSuffix);

					//writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, CHAR_NO); //clear 

				}
               
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                dialog.dismiss();
            }
        });
    }

    private void dialogDecoding() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.decoding_dialog, null);

        CheckBox decodingSound = (CheckBox) v.findViewById(R.id.decodingSound);
        CheckBox decodingVibration = (CheckBox) v.findViewById(R.id.decodingVibration);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);
	

        Log.d(TAG, "dialogDecoding   scan_alert_sound:=  " + scan_alert_sound+"  scan_alert_vibrate:= "+scan_alert_vibrate);
        decodingSound.setChecked(scan_alert_sound);
        decodingVibration.setChecked(scan_alert_vibrate);

        final AlertDialog dialog = builder.show();

        decodingSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "decodingSound onCheckedChanged isChecked:  " + isChecked);
                mActivity.client.enableScanSound(isChecked);
                scan_alert_sound = isChecked;
                mActivity.writeToPreference(PREF_KEY_SCAN_SOUND_ALERT, isChecked == true? "true":"false");
            }
        });

        decodingVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "decodingVibration onCheckedChanged isChecked: " + isChecked);
                mActivity.client.enableScanVibrator(isChecked);
                scan_alert_vibrate = isChecked;
                mActivity.writeToPreference(PREF_KEY_SCAN_VIBRATE_ALERT, isChecked == true? "true":"false");
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

			  //Log.d(TAG,"CLIP data:="+getClipData());
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void dialogLogin(int option) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.login_dialog, null);

        final EditText editCode = (EditText) v.findViewById(R.id.editCode);
     

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

		Log.d(TAG, "dialogLogin " );
	
        final AlertDialog dialog = builder.show();

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStr = editCode.getText().toString();
				//if(!mStr.equals(""))
					 
	               
	                if (DEBUG)
	                    Log.d(TAG, "dialogLogin  mStr:" + mStr );
					
                    if(mStr.equals(LOGIN_CODE)){
					   switch(option) {
                           case 0:
                               dialogBroadcast();
                               break;

                           case 1:
                               dialogPhoneNum();
                               break;
                           case 2:
                               dialogScanCenter();
                               break;
                       }
                    }	
				
                    dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                dialog.dismiss();
            }
        });

	
    }
	 private void dialogBroadcast() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.decode_broadcast_dialog, null);

        final EditText edit_broadcast_action = (EditText) v.findViewById(R.id.edit_broadcast_action);
        final EditText edit_broadcast_extra = (EditText) v.findViewById(R.id.edit_broadcast_extra);
        //final EditText edit_scanner_timelimit = (EditText) v.findViewById(R.id.edit_scanner_timelimit);
        //final EditText edit_scanner_number = (EditText) v.findViewById(R.id.edit_scanner_number);     

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

		broadcast_Action = mActivity.readFromPreference(PREF_KEY_SCAN_BROADCAST_ACTION, "");
		broadcast_Extra = mActivity.readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, "");

		 Log.d(TAG, "ScanService dialogBroadcast  broadcast_Action:" + broadcast_Action+" broadcast_Extra:= "+broadcast_Extra );

       // mActivity.initParameter();
      

	   if( !broadcast_Action.equals("")){   //for JR008
	      if(getDeviceName().equals("JR008")) 
             edit_broadcast_action.setText("***");
		  else
		  	 edit_broadcast_action.setText(broadcast_Action);
	   	}
	   
	   if( !broadcast_Extra.equals("")){//for JR008
	      if(getDeviceName().equals("JR008")) 
            edit_broadcast_extra.setText("***");
		  else
		  	edit_broadcast_extra.setText(broadcast_Extra);
	   	}
    

        edit_broadcast_action.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

			  // Log.d(TAG, " dialogBroadcast. cust action:="+s.toString());
			  // broadcast_Action=s.toString();
               // mActivity.writeToPreference(PREF_KEY_SCAN_BROADCAST_ACTION, s.toString());
            }
        });
        edit_broadcast_extra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                /*if (!TextUtils.isEmpty(s.toString())){
                    if(Integer.parseInt(s.toString()) > 20)
                        s.replace(0, s.length(), "20");
                    else if(Integer.parseInt(s.toString()) <1)
                        s.replace(0, s.length(), "1");
                }*/

				//Log.d(TAG, " dialogBroadcast. cust extra:="+s.toString());
               // broadcast_Extra=s.toString();
               // mActivity.writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, s.toString()); //DEBUG
            }
        });


        final AlertDialog dialog = builder.show();
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                

                /* Intent custIntent = new Intent();
				 custIntent.setAction("jr.android.service_settings");
                 custIntent.putExtra("action_barcode_broadcast", broadcast_Action);
				 custIntent.putExtra("key_barcode_broadcast", broadcast_Extra);
				 sendBroadcast(custIntent);*/

                String action = edit_broadcast_action.getText().toString();
				String extra = edit_broadcast_extra.getText().toString();

				Log.d(TAG, " dialogBroadcast. btn_sure  action:= "+action +" extra:= "+extra);
				
				if(!TextUtils.isEmpty(action)){
					  broadcast_Action=action;
					mActivity.writeToPreference(PREF_KEY_SCAN_BROADCAST_ACTION, action);
				}

				if(!TextUtils.isEmpty(extra)){
					broadcast_Extra= extra;
					mActivity.writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, extra);
				}
					
				
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Log.d(TAG, " dialogBroadcast. btn_cancel ");
                dialog.dismiss();
            }
        });
    }


  

    private void dialogPhoneNum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.phonenum_dialog, null);

        final CheckBox cbPhoneNum = (CheckBox) v.findViewById(R.id.cbPhoneNum);
       
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);
	

        Log.d(TAG, "dialogPhoneNum   isPhoneNumberEnabled:=  " + isPhoneNumberEnabled);
        cbPhoneNum.setChecked(isPhoneNumberEnabled);
       

        final AlertDialog dialog = builder.show();

        cbPhoneNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "cbKeyevent onCheckedChanged isChecked:  " + isChecked);
            }
        });

       

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

			     if(cbPhoneNum.isChecked())
				 	isPhoneNumberEnabled = true;
				 else
				 	isPhoneNumberEnabled = false;

				
			       mActivity.writeToPreference(PREF_KEY_SCAN_PHONENUMBER_ENABLE, isPhoneNumberEnabled == true? "true":"false");

			               
				 Intent intent = new Intent();
                 intent.setAction("com.qxj.scan.phonenumber.enable");
                 intent.putExtra("enable",isPhoneNumberEnabled);
                 mActivity.sendBroadcast(intent);

                				
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

   
	 private void dialogPkgSet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.pkg_setting_dialog, null);

        final EditText edit_pkg_name = (EditText) v.findViewById(R.id.edit_pkg_name);
         

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);    

      


        final AlertDialog dialog = builder.show();
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				String pkgName  = edit_pkg_name.getText().toString();
				
                Log.d(TAG, " dialogPkgSet. btn_sure  pkgName:= "+pkgName);

				if (!TextUtils.isEmpty(pkgName)){

                    Intent toIntent = new Intent();
			        toIntent.setAction("com.android.action.privileged_permissions_packageName_update");
					toIntent.putExtra("packageName", pkgName);
			        //toIntent.setPackage("com.android.scantest");
			        mActivity.sendBroadcast(toIntent);
				}	 
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Log.d(TAG, " dialogPkgSet. btn_cancel ");
                dialog.dismiss();
            }
        });
    }


	  private void dialogScanCenter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);  //eric debug
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.scancenter_dialog, null);

        final CheckBox cbScanCenter = (CheckBox) v.findViewById(R.id.cbScanCenter);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);

        cbScanCenter.setChecked(isScanCenterEnabled);

		Log.d(TAG, " dialogScanCenter. isScanCenterEnabled:= "+isScanCenterEnabled+" param-ScanCenter:= "+mActivity.client.getScannerParameter("decode_centering_mode"));

        final AlertDialog dialog = builder.show();

        cbScanCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // isScanCenterEnabled = isChecked;
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				 Log.d(TAG, " btn_sure.setOnClickListener. isScanCenterEnabled:= "+isScanCenterEnabled);

				 if(cbScanCenter.isChecked())
				 	isScanCenterEnabled = true;
				 else
				 	isScanCenterEnabled = false;
				 
				 mActivity.writeToPreference(PREF_KEY_SCAN_CENTER_ENABLE, isScanCenterEnabled == true? "true":"false");

				 if(isScanCenterEnabled)
				     mActivity.client.setScannerParameter("decode_centering_mode","1");
				 else
				 	mActivity.client.setScannerParameter("decode_centering_mode","0");
				 
                 dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }


     private void dialogScanLight() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.light_dialog, null);

        final CheckBox cbIll = (CheckBox) v.findViewById(R.id.cbIllLight);
        final CheckBox cbAim = (CheckBox) v.findViewById(R.id.cbAimLight);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);
	

        Log.d(TAG, "dialogScanLight   isIllLightEnabled:=  " + isIllLightEnabled+"  isAimLightEnabled:= "+isAimLightEnabled);
        cbIll.setChecked(!isIllLightEnabled);
        cbAim.setChecked(!isAimLightEnabled);

        final AlertDialog dialog = builder.show();

        cbIll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "cbIll onCheckedChanged isChecked:  " + isChecked);
				
                
                //isIllLightEnabled = !isChecked;
                
            }
        });

        cbAim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "cbAim onCheckedChanged isChecked: " + isChecked);
               
               // isAimLightEnabled = !isChecked;
               
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

			     if(cbIll.isChecked())
				 	isIllLightEnabled = false;
				 else
				 	isIllLightEnabled = true;

				  if(cbAim.isChecked())
				 	isAimLightEnabled = false;
				 else
				 	isAimLightEnabled = true;

			    mActivity.writeToPreference(PREF_KEY_SCAN_ILLLight_ENABLE, isIllLightEnabled == true? "true":"false");

				 if(isIllLightEnabled)
				     mActivity.client.setScannerParameter("decode_ill_enable","1");
				 else
				 	mActivity.client.setScannerParameter("decode_ill_enable","0");
				 

				 mActivity.writeToPreference(PREF_KEY_SCAN_AIMLight_ENABLE, isAimLightEnabled == true? "true":"false");

				 if(isAimLightEnabled)
				     mActivity.client.setScannerParameter("decode_aim_enable","1");
				 else
				 	mActivity.client.setScannerParameter("decode_aim_enable","0");
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }


	 private void dialogKeyEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.keyevent_dialog, null);

        final CheckBox cbKeyevent = (CheckBox) v.findViewById(R.id.cbKeyevent);
       
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        builder.setView(v);
	

        Log.d(TAG, "dialogKeyEvent   isKeyEventEnabled:=  " + isKeyEventEnabled);
        cbKeyevent.setChecked(isKeyEventEnabled);
       

        final AlertDialog dialog = builder.show();

        cbKeyevent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DEBUG)
                    Log.d(TAG, "cbKeyevent onCheckedChanged isChecked:  " + isChecked);
            }
        });

       

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

			     if(cbKeyevent.isChecked())
				 	isKeyEventEnabled = true;
				 else
				 	isKeyEventEnabled = false;

				
			    mActivity.writeToPreference(PREF_KEY_SCAN_KEYEVENT_ENABLE, isKeyEventEnabled == true? "true":"false");

			               
				 Intent intent = new Intent();
                 intent.setAction("com.rq.enable_keycode_callback");
                 intent.putExtra("enable_keycode_callback",isKeyEventEnabled);
                 mActivity.sendBroadcast(intent);

                 if(isKeyEventEnabled ){
				    mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_ENTER);
					
				 }else{
				     mActivity.client.setAdditionalKeyCode(KeyEvent.KEYCODE_0); //clear
				 	 //mActivity.client.setScanResultSuffix("");
				 }

				 

				
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }



 /*   private void dialogScannerDecodeType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);  //eric debug
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.scannerdecodetype_dialog, null);

        RadioGroup radioGroupOutputType = (RadioGroup) v.findViewById(R.id.radioDecodeType);
        RadioButton decodeshengyuan = (RadioButton) v.findViewById(R.id.decodeshengyuan);
        RadioButton decodecortex = (RadioButton) v.findViewById(R.id.decodecortex);

        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

        txtDecodeType = txtDecodeTypeMode.getText().toString();

        builder.setView(v);

        if (scanner_decode_type == 3) {
            decodeshengyuan.setChecked(false);
            decodecortex.setChecked(true);
        } else if (mActivity.scanner_decode_type == 2) {
            decodeshengyuan.setChecked(true);
            decodecortex.setChecked(false);
        } else {
            decodeshengyuan.setChecked(false);
            decodecortex.setChecked(false);
        }

        final AlertDialog dialog = builder.show();

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        radioGroupOutputType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.decodeshengyuan:
                        txtDecodeType = getResources().getString(R.string.scanner_decode_shengyuan);
                        //mActivity.client.setScannerDecodeType(2);
                        mActivity.scanner_decode_type = 2;
                        break;
                    case R.id.decodecortex:
                        txtDecodeType = getResources().getString(R.string.scanner_decode_cortex);
                        //mActivity.client.setScannerDecodeType(3);
                        mActivity.scanner_decode_type = 3;
                        break;
                }
                txtDecodeTypeMode.setText(txtDecodeType);
            }
        });
    }
*/
 
    public static String Str2AsciiStr(String Str) {
        if (TextUtils.isEmpty(Str))
            return "";

        Log.d(TAG,"Suffixe Str2AsciiStr str:="+Str);
        byte[] bytes = new byte[1];
        int num = Integer.parseInt(Str);

        if (num < 0)
            num = 0;
        else if (num > 127)
            num = 127;

        bytes[0] = (byte) num;
        Log.d(TAG,"Str2AsciiStr code:="+bytes[0] );
        return new String(bytes);
    }

    public static String AsciiStr2Str(String AsciiStr) {
        if (TextUtils.isEmpty(AsciiStr))
            return "";
		Log.d(TAG,"Suffixe AsciiStr2Str str:="+AsciiStr);
        StringBuilder sb = new StringBuilder("");
        byte[] bs = AsciiStr.getBytes();
        int bit = (int) bs[0];
        return Integer.toString(bit);
    }



    public void initPara() {
        //系统扫描状态
        isScanOpened = mActivity.client.getSystemScanState();

       // mOutMode = client.getReceiveMode() == ScanReceiveMode.BROADCAST_CALLBACK? OUTPUT_BROADCAST : OUTPUT_DIRECT;  //eric-zhao DEBUG
        if(getDeviceName().equals("JR008")|| getDeviceName().equals("BL-798E")){ //for BEST
	       mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST); //  for JR008
	       outMode = "rbBroadcast_output";
        }else{
           mOutMode =mActivity.readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT); //  for  others
           outMode = "rbDirect_filling";
        }
		
       // test_output_autoreturn = readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, PREF_KEY_SCAN_OUTPUT_OVERRIDE).equals(PREF_KEY_SCAN_OUTPUT_AUTORETURN);
        output_autoreturn = mActivity.client.getAdditionalKeyCode() == KeyEvent.KEYCODE_ENTER? true:false; //eric-zhao

		mPrefixChar = mActivity.readFromPreference(PREF_KEY_SCAN_PREFIX_CHAR, CHAR_NULL);
		mSuffixChar = mActivity.readFromPreference(PREF_KEY_SCAN_SUFFIX_CHAR, CHAR_NULL);

		curStrPrefix = getString(R.string.str_prefix_default);
		curStrSuffix = getString(R.string.str_suffix_default);

		isEnterOn = mActivity.client.getAdditionalKeyCode()==KeyEvent.KEYCODE_ENTER? true:false ;
		isTabOn = mActivity.client.getAdditionalKeyCode()==KeyEvent.KEYCODE_TAB? true:false ;

		scan_prefix_code = mActivity.client.getScanResultPrefix();
        scan_suffix_code = mActivity.client.getScanResultSuffix();


        broadcast_Action = mActivity.readFromPreference(PREF_KEY_SCAN_BROADCAST_ACTION, "");
		broadcast_Extra = mActivity.readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, "");

//        //连续扫描状态
//        if (mActivity.client.isContinueScanEnabled()){
//            readMode = READ_MODE_CONTINUE;
//			setting_readMode = "radioContinuous";
//        }
//        else{
//            readMode = READ_MODE_ONE_TIME;
//			setting_readMode = "radioRelease";
//        }
		
        String tScanCenter = mActivity.readFromPreference(PREF_KEY_SCAN_CENTER_ENABLE, "false"); //eric-zhao 
		 if(tScanCenter.equals("true"))  //added by eric-zhao
		    isScanCenterEnabled =true; 
	     else
	  	   isScanCenterEnabled =false;  	

        scan_read_interval = mActivity.client.getContinueScanInterval();
//        scan_read_repeat = client.getDisallowSameresult();
//        scan_read_timeout = client.getScannerTimerOut();

        scan_read_continue_notimeout = Integer.valueOf( mActivity.readFromPreference(PREF_KEY_SCAN_CONTINUE_NOTIMEOUT, "0")); //pref.getInt("key", 0);//Settings.Global.getInt(this.getContentResolver(), "continue_scan_notimeout",0);//client.getContinueScanningNoTimeout();


//        scan_read_downscanner = client.getDownScannerStatus();
//
//        isFloatingScanEnabled = client.getFloatViewScannerStatus();
//        if(isFloatingScanEnabled){
//            client.TurnOnOffFloatViewScanner(isFloatingScanEnabled);
//        }
        
        scan_alert_sound = mActivity.client.isScanSoundEnabled();
        scan_alert_vibrate = mActivity.client.isVibratorEnabled();


		 String tIll = mActivity.readFromPreference(PREF_KEY_SCAN_ILLLight_ENABLE, "true"); //eric-zhao 
		 if(tIll.equals("true"))  //added by eric-zhao
		    isIllLightEnabled =true; 
	     else
	  	   isIllLightEnabled =false;  

		 String tAim = mActivity.readFromPreference(PREF_KEY_SCAN_AIMLight_ENABLE, "true"); //eric-zhao 
		 if(tAim.equals("true"))  //added by eric-zhao
		    isAimLightEnabled =true; 
	     else
	  	   isAimLightEnabled =false; 


		  String tKeyevent = mActivity.readFromPreference(PREF_KEY_SCAN_KEYEVENT_ENABLE, "false"); //eric-zhao 
		 if(tKeyevent.equals("true"))  //added by eric-zhao
		    isKeyEventEnabled =true; 
	     else
	  	   isKeyEventEnabled =false; 


		   String tPhonenum = mActivity.readFromPreference(PREF_KEY_SCAN_PHONENUMBER_ENABLE, "false"); //eric-zhao 
		 if(tPhonenum.equals("true"))  //added by eric-zhao
		    isPhoneNumberEnabled =true; 
	     else
	  	    isPhoneNumberEnabled =false;
		 

//        isImageViewShow = client.getImageViewStatus();//
//        scanner_decode_type =  client.getScannerDecodeType();//
//        scan_brightness =  client.getIllBrightness();


         Log.d(TAG, "initPara. isScanOpened:= " + isScanOpened+"  scan_alert_sound:= "+scan_alert_sound+" scan_alert_vibrate:= "+scan_alert_vibrate+" scan_prefix_code:="+scan_prefix_code+" scan_suffix_code:="+scan_suffix_code+" mSuffixChar:="+mSuffixChar+" setting_readMode:= "+setting_readMode
                  +" readMode:= "+readMode +" mOutMode:= "+mOutMode);

  
    }



	
 /*
    public void writeToPreference(String name, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit();  //Context.MODE_PRIVATE  -- 0
        editor.putString(name, value);
        editor.commit();
    }

	public void writeIntToPreference(String name, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public String readFromPreference(String name, String defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getString(name, defaultvalue);  //  
    }

	 public int readIntFromPreference(String name, int defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getInt(name, defaultvalue);
    }*/


    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

	public String getClipData(){  //for DEBUG
        String res="";
        if(mClipboardManager.hasPrimaryClip()){
            Log.d(TAG,"CLIP getClipData");
			mClipData = mClipboardManager.getPrimaryClip();
            ClipData.Item item = mClipData.getItemAt(0);
            res = item.getText().toString();
		}	
		return res;

	}	

	private String getDeviceName(){

       String name = android.os.Build.MODEL;

      if(android.os.SystemProperties.get("ro.rq.baishi.version").equals("yes")) //for BEST
	        name = "BL-798E";  //eric-zhao  
	  

	   Log.d(TAG,"getDeviceName :="+name);
	   return name;
    }

 


    }
