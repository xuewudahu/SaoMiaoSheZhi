package com.rq.scan.service;


import android.accounts.AuthenticatorException;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;

import com.alibaba.android.mind.duguang.AuthInfoStore;
import com.alibaba.android.mind.duguang.AuthTypeEnum;
import com.alibaba.android.mind.duguang.OcrConfig;
import com.alibaba.sail.duguang.ocr.SailOcrConfig;
import com.alibaba.sail.duguang.ocr.SailOcrSdk;
import com.alibaba.sail.duguang.ocr.auth.AuthInfoBuilder;
import com.alibaba.sail.duguang.ocr.ocr.ISailScanOcr;
import com.alibaba.sail.duguang.ocr.ocr.result.IRecognizeCallback;
import com.alibaba.sail.duguang.ocr.ocr.result.ImageResult;
import com.google.gson.Gson;

import java.io.File;

import cn.rq.pda.sdk.RqPDAClient;
import cn.rq.pda.sdk.ScanReceiveMode;

 //import android.app.Instrumentation;
 //import android.view.KeyEvent;

public class ScanService extends Service {
 
	private static final String TAG = "ScanService";


    private  Context mContext;
	
    private Handler mHandler =null;

	private String action;

	public RqPDAClient client;

	private SharedPreferences sp;

    public String mSendMode;
    public String  broadcast_cust_action;
	public String  broadcast_cust_key;
	
	public String  broadcast_cust_key_datatype; //for BEST
	public String  broadcast_cust_key_dataextra; //for BEST

	private ClipboardManager mClipboardManager;
    private ClipData mClipData;

	private String sym="";
	private  String barcode;
	private String  imgPath="";
	private byte[]  imgData;
    private int[] points;

	private String deviceName="";

	public String  mPhoneNumberEnabled= "false";

//	public Instrumentation inst = new Instrumentation() ;

  //for aliOCR
   //SailScanOCR ocr;
   //IRecognizeCallback callback;
   
    IRecognizeCallback callback;
    ISailScanOcr ocr;
	

    public static String QXJ_SCAN_START_ACTION = "com.qxj.decode.start"; //added for uniapp
    
    public static String QXJ_SCAN_IMAGE_ACTION = "com.qxj.scan.image";

	 public static String QXJ_OCR_PHONENUM_ACTION = "com.qxj.scan.phonenumber";
	 public static  String QXJ_OCR_NUMBER_EXTRA = "ocr_phonenumber";
	 public static  String QXJ_OCR_TIME_EXTRA = "ocr_time";
	 public static  String QXJ_OCR_BARCODE_EXTRA = "ocr_barcode";
	 public static  String QXJ_OCR_IMAGE_EXTRA = "ocr_image";

	public static String JR_SCAN_SETTING_ACTION = "jr.android.scanner.service_settings";
	public static String JR_SCAN_SETTING_EXTRA_END = "endchar";
	public static String JR_SCAN_SETTING_EXTRA_SEND_MODE = "barcode_send_mode";

	public static final String JR_SCAN_SETTING_END_CHAR_ENTER = "ENTER";
	public static final String JR_SCAN_SETTING_END_CHAR_TAB = "TAB";
	public static final String JR_SCAN_SETTING_END_CHAR_SPACE = "SPACE";
	public static final String JR_SCAN_SETTING_END_CHAR_NONE = "NONE";

	public static final String JR_SCAN_SETTING_SEND_MODE_FOCUS = "FOCUS";
	public static final String JR_SCAN_SETTING_SEND_MODE_FOCUS_OVERWRITE = "FOCUS_OVERWRITE";
	public static final String JR_SCAN_SETTING_SEND_MODE_BROADCAST = "BROADCAST";
	public static final String JR_SCAN_SETTING_SEND_MODE_CLIPBOARD = "CLIPBOARD";

	

	public static final String JR_SCAN_BROADCAST_SETTING_ACTION = "jr.android.service_settings";
	public static final String JR_SCAN_BROADCAST_SETTING_EXTRA_NAEM = "action_barcode_broadcast";
	public static final String JR_SCAN_BROADCAST_SETTING_EXTRA_KEY = "key_barcode_broadcast";

	public static final String JR_ACTION = "jr.android.server.scannerservice.broadcast";
	public static final String JR_EXTRA = "barcodedata";


	//for Best Baishi

    public static final String BEST_ACTION = "com.best.android.receivescanaction";
	public static final String BEST_KEY_DATA = "data";
	public static final String BEST_KEY_DATATYPE = "datatype";
	public static final String BEST_KEY_EXTRA = "extra";

	public static String BEST_SCAN_SETTING_ACTION = "com.best.android.scansettings";	
	public static String BEST_SCAN_SETTING_SEND_MODE = "send_mode";
	public static final String BEST_SCAN_BROADCAST_SETTING_ACTION = "scan_broadcast_action";
	public static final String BEST_SCAN_BROADCAST_SETTING_KEY_DATA = "scan_broadcast_key_data";
    public static final String BEST_SCAN_BROADCAST_SETTING_KEY_DATATYPE = "scan_broadcast_key_datatype";
	public static final String BEST_SCAN_BROADCAST_SETTING_KEY_EXTRA = "scan_broadcast_key_extra";
	public static String BEST_SCAN_SETTING_QRCODE = "scan_support_qrcode";	
	public static String BEST_SCAN_SETTING_PREFIX = "scan_data_prefix";
	public static String BEST_SCAN_SETTING_SUFFIX = "scan_data_suffix";
	public static String BEST_SCAN_SETTING_ENDCHAR = "scan_data_endchar";
	public static String BEST_SCAN_SETTING_SOUND_PLAY = "sound_play";
	public static String BEST_SCAN_SETTING_VIBERATE = "viberate";
	public static String BEST_SCAN_SETTING_CONTINUE_SCAN = "scan_continue";

	public static final String BEST_SCAN_SETTING_SEND_MODE_BROADCAST = "BROADCAST";


  

	//end of add Best
	
   

   /* private static final int OUTPUT_DIRECT = 0;
    private static final int OUTPUT_BROADCAST = 1;
	private static final int OUTPUT_OVERWRITE = 2;
    private static final int OUTPUT_CLIP = 3;*/

    private static final String OUTPUT_DIRECT = "OUTPUT_DIRECT";
    private static final String OUTPUT_BROADCAST = "OUTPUT_BROADCAST";
	private static final String OUTPUT_OVERWRITE = "OUTPUT_OVERWRITE";
    private static final String OUTPUT_CLIP = "OUTPUT_CLIP";

/*
	private final static int CHAR_NULL = 0;
    private final static int CHAR_SPACE = 1;
    private final static int CHAR_ENTER = 2;
    private final static int CHAR_TAB = 3;
	private final static int CHAR_NO = 4;*/

    private final static String CHAR_NULL = "CHAR_NULL";
    private final static String CHAR_SPACE = "CHAR_SPACE";
    private final static String CHAR_ENTER = "CHAR_ENTER";
    private final static String CHAR_TAB = "CHAR_TAB";
	private final static String CHAR_NO = "CHAR_NO"; 
	

    public static final String myPref = "ScanControlPreference";
	public static String PREF_KEY_SCAN_OUTPUT_MODE = "key_scan_output_mode";
    public static String PREF_KEY_SCAN_OUTPUT_OVERRIDE = "key_scan_output_override";
    public static String PREF_KEY_SCAN_OUTPUT_AUTORETURN = "key_scan_output_autoreturn";
	public static String PREF_KEY_SCAN_PREFIX_CHAR = "key_scan_prefix_char";
	public static String PREF_KEY_SCAN_SUFFIX_CHAR = "key_scan_suffix_char";
	public static String PREF_KEY_SCAN_BROADCAST_ACTION = "key_scan_broadcast_action";
    public static String PREF_KEY_SCAN_BROADCAST_EXTRA = "key_scan_broadcast_extra";
	public static String PREF_KEY_SCAN_BROADCAST_EXTRA_DATATYPE = "key_scan_broadcast_extra_datatype"; //for BEST
	public static String PREF_KEY_SCAN_BROADCAST_EXTRA_DATAEXTRA = "key_scan_broadcast_extra_dataextra"; //for BEST
	public static String PREF_KEY_SCAN_ENABLE = "key_scan_enable";
	public static String PREF_KEY_SCAN_SOUND_ALERT = "key_scan_sound_alert";
    public static String PREF_KEY_SCAN_VIBRATE_ALERT = "key_scan_vibrate_alert";
	public static String PREF_KEY_SCAN_CENTER_ENABLE = "key_scan_center_enable";
	public static String PREF_KEY_SCAN_PHONENUMBER_ENABLE = "key_scan_phonenumber_enable";
	


	private static final String DISABLE_INSTALL_PACKAGE = "com.rq.disable.install.package";


	private static final String mformatType = "yyyy-MM-dd HH:mm:ss.SSS";


	//DEBUG 
	 private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

     @Override
    public void onCreate() {
        super.onCreate();
		 Log.e("TAG", "onCreate: ");
	   
		mHandler = new Handler();

		client = RqPDAClient.newClient(this);
        Log.d(TAG, "BOOT_COMPLETED onCreate" );

		sp = getSharedPreferences(myPref,Context.MODE_MULTI_PROCESS); //   MODE_PRIVATE
		//sp.registerOnSharedPreferenceChangeListener(listener);


		mClipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

		mContext = this; //DEBUG
		 if( Settings.Global.getInt(mContext.getContentResolver(), "rq.broadcast.geek",1)==1) {
			 Log.e("scanner1", "-----shou");
			 IntentFilter intentFilter = new IntentFilter();
			 intentFilter.addAction("com.geenk.action.SCAN_SWITCH");
			 intentFilter.addAction("com.geenk.action.START_SCAN");
			 intentFilter.addAction("com.geenk.action.STOP_SCAN");
			 mContext.registerReceiver(new BootReceiver(), intentFilter);
		 }
		 if(android.os.SystemProperties.get("ro.rq.zhongtong").equals("ZTO")) {
			 //added by wxw start
			  Handler handler = new Handler();
			 Runnable runnable = new Runnable() {
				 @Override
				 public void run() {
					 client.startScan();
				 }
			 };
			 ResultReceiver resultReceiver =new ResultReceiver(handler,runnable,client);
			 IntentFilter intentFilterResult = new IntentFilter();
			 ZtoReceiver ztoReceiver =new ZtoReceiver(handler,runnable,client);

			 IntentFilter intentFilterZto = new IntentFilter();
			 intentFilterZto.addAction("com.android.zto.pda.action.get_sn");
			 intentFilterZto.addAction("com.android.zto.pda.action.start_scan");
			 intentFilterZto.addAction("com.android.zto.pda.action.stop_scan");
			 intentFilterZto.addAction("com.android.zto.pda.action.set_systime");
			 intentFilterZto.addAction("com.android.zto.pda.action.set_scan_interval");
			 intentFilterZto.addAction("com.android.zto.pda.action.set_scan_delay_interval");
			 intentFilterZto.addAction("com.android.zto.pda.action.start_recycle_scan");
			 intentFilterZto.addAction("com.android.zto.pda.action.allow_pull_statusbar");
			 intentFilterZto.addAction("com.android.zto.pda.action.allow_click_home_key");
			 intentFilterZto.addAction("com.android.zto.pda.action.set_scan_light");
			 intentFilterZto.addAction("com.android.zto.pda.action.scan_output_model");
			 intentFilterZto.addAction("com.android.zto.pda.action.allow_click_switch_task_key");
			 intentFilterZto.addAction("android.intent.action.SCAN_keycode");

			 mContext.registerReceiver(ztoReceiver, intentFilterZto);

			 intentFilterResult.addAction("com.rq.cenon.receivescanaction");
			 intentFilterResult.addAction("com.android.SimulationOfKeyboarOperation");
			 mContext.registerReceiver(resultReceiver, intentFilterResult);
			 client.setScannerTimeout(3000);
			 //added by wxw end
		 }
    }

	
 
	  @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "BOOT_COMPLETED onBind");
		  Log.e("TAG", "onBind: ");
        return null;
    }
 
       @RequiresApi(api = Build.VERSION_CODES.O)
	   @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		   Log.e("TAG", "onStartCommand: ");
		action =(intent != null)? intent.getAction():null;
		
		Log.d(TAG, "BOOT_COMPLETED onStartCommand  action:="+action );


        //if(action != null && action.equals("com.qxj.scan.service.start"))
		{
			startService();


			/* mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                   startService();
	                }
               }, 1000);*/
        }

		setForeground();
        
		
        return START_STICKY  ;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "BOOT_COMPLETED onUnbind");
		Log.e("TAG", "onUnbind: ");
        return super.onUnbind(intent);
    }

 
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("TAG", "onDestroy: ");
		stopForeground(true);
		//sp.unregisterOnSharedPreferenceChangeListener(listener);
		Log.d(TAG, " onDestroy()");
	} 

	/*private SharedPreferences.OnSharedPreferenceChangeListener listener = new   SharedPreferences.OnSharedPreferenceChangeListener() {
       @Override
       public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

	        String value =sharedPreferences.getString(key, "");;
			//int mkey;  
	        Log.d(TAG, "OnSharedPreferenceChangeListener key: " + key +"<<value:="+value);
			if(key.equals(PREF_KEY_SCAN_OUTPUT_MODE)){
			   //mkey =sharedPreferences.getInt(key,1) ;	 // 1- broadcast
	           mSendMode = value;

			}else if(key.equals(PREF_KEY_SCAN_BROADCAST_ACTION)){
			  // value = sharedPreferences.getString(key, "");
	           broadcast_cust_action = value;

			}else if(key.equals(PREF_KEY_SCAN_BROADCAST_EXTRA)){
			   // value = sharedPreferences.getString(key, "");
	           broadcast_cust_key = value;

			}		
        

    }
};*/



	@RequiresApi(api = Build.VERSION_CODES.O)
	private void setForeground() {

        NotificationChannel channel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW);//IMPORTANCE_MIN IMPORTANCE_LOW

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(this, "id").build();

        startForeground(1, notification);
    }


	public void startService(){
        
        IntentFilter intentFilter = new IntentFilter();   
		intentFilter.addAction("cn.rq.pda.action.receivescan");
		intentFilter.addAction("com.qxj.scan.image");		
		intentFilter.addAction(JR_SCAN_SETTING_ACTION);
		intentFilter.addAction(JR_SCAN_BROADCAST_SETTING_ACTION);
		intentFilter.addAction(BEST_SCAN_SETTING_ACTION); //for BEST
		intentFilter.addAction(QXJ_SCAN_START_ACTION); //for uniapp
		intentFilter.addAction("com.qxj.scan.phonenumber.enable"); //for aliOCR

		intentFilter.addAction("com.android.scanner.service_settings"); //for sdk of eastaeon
        registerReceiver(mReceiver, intentFilter);

		deviceName = getDeviceName();

        if(deviceName.equals("JR008")|| deviceName.equals("BL-798E")){ //for BEST
          mSendMode =readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST); //  for JR008
        }else{
          mSendMode =readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT); //  for  others
        }
		broadcast_cust_action = readFromPreference(PREF_KEY_SCAN_BROADCAST_ACTION, "");
		broadcast_cust_key = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, "");
		broadcast_cust_key_datatype = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATATYPE, "");//for BEST
		broadcast_cust_key_dataextra = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATAEXTRA, "");//for BEST

		final String scannerID = android.os.SystemProperties.get("vendor.boogoob.scan.type");

        Log.d(TAG, "startService mSendMode:="+mSendMode+" scannerID := "+scannerID+" deviceName:= "+deviceName);


       if(scannerID.equals("-1") ){ // scannerID  not ready

		  Log.d(TAG, "scannerID -1 startService delay 5s InitScan");
          mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   InitScan();
                }
           }, 5000); 

       }else{ // scannerID: 10 - N6603    23- R100
	 
		 //InitScan();
		    Log.d(TAG, "startService delay 3s InitScan");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   InitScan();
                }
           }, 3000);

       	}	 
    
		//default enable systemScan sound vibrator
		/*client.enableSystemScan(true);		 
		client.enableScanSound(true);
		client.enableScanVibrator(true);*/
		
       // client.setScanReceiveMode(getSendMode(mSendMode));	   		
	 

		//disableInstallPackage();  //DEBUG eric-zhao TODO


	}	


	private void InitScan(){

      String scannerEnable  =  readFromPreference(PREF_KEY_SCAN_ENABLE, "true");
      String soundAlert = readFromPreference(PREF_KEY_SCAN_SOUND_ALERT, "false");
	  String vibrateAlert = readFromPreference(PREF_KEY_SCAN_VIBRATE_ALERT, "false");
	  
      String scancenter = readFromPreference(PREF_KEY_SCAN_CENTER_ENABLE, "false");

	  mPhoneNumberEnabled = readFromPreference(PREF_KEY_SCAN_PHONENUMBER_ENABLE, "false");
	  
	  
	  Log.e(TAG,"InitScan scannerEnable:= "+scannerEnable+" soundAlert:= "+soundAlert+" vibrateAlert:= "+vibrateAlert+" scannerID := "+android.os.SystemProperties.get("vendor.boogoob.scan.type")+" scancenter:="+scancenter);
		Log.e("ZTO","InitScan---enableSystemScan---"+client.getSystemScanState());
      if(scannerEnable.equals("true")) {
		  if (!client.getSystemScanState()) {
			  Log.e("ZTO","InitScan---enableSystemScan");
			  client.enableSystemScan(true);
		  }
		  if (SystemProperties.get("vendor.boogoob.scan.type").equals("10") || SystemProperties.get("vendor.boogoob.scan.type").equals("25")) {
			  if (SystemProperties.getInt("persist.decode_use_ae_param", 1) == 0) {
				  client.setScannerParameter("decode_aim_enable","0");
				  client.startScan();
				  client.stopScan();
				  client.setScannerParameter("decode_aim_enable","1");
				  Handler handler = new Handler();
				  handler.postDelayed(new Runnable() {
					  @Override
					  public void run() {
						  client.enableSystemScan(false);
						  client.enableSystemScan(true);
					  }
				  }, 2000);
			  }
		  }

	  }else
	  	client.enableSystemScan(false);	

	  if(soundAlert.equals("true"))
		client.enableScanSound(true);
	  else
	  	client.enableScanSound(false);

	  if(vibrateAlert.equals("true"))
		client.enableScanVibrator(true); 
	  else
	  	client.enableScanVibrator(false); 	

      /*
	  if(scancenter.equals("true"))  //added by eric-zhao
		client.setScanCenterEnable(true); 
	  else
	  	client.setScanCenterEnable(false); 	
	  */
	  
	 

	  client.setScanReceiveMode(getSendMode(mSendMode));	
	  writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
	  

      if( deviceName.equals("BL-798E")){ //for BEST
	   	
	        if(client.isScanImgSaveSupported()){
				client.enableScanImgSave(true);
				client.setScanImgMode("saveImg");

	        }	
				
       	} 

      if(mPhoneNumberEnabled.equals("true")){  

		   initAliOcrSdk();

		  if(client.isScanImgSaveSupported()){
				client.enableScanImgSave(true);
				client.setScanImgMode("broadcast");  //for aliOCR

	        }

		  if(!readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT).equals(OUTPUT_BROADCAST)){

					    client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                        client.enableOverWriteOutput(false);
                        mSendMode = OUTPUT_BROADCAST;
                        writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST);		 	
                        Log.d(TAG, " InitScan setScanReceiveMode BROADCAST_CALLBACK ");
		  }

      }

	}	

    private String getDeviceName(){

       String name = Build.MODEL;

      if(android.os.SystemProperties.get("ro.rq.baishi.version").equals("yes"))
	        name = "BL-798E";  // for BAISHI express  DEBUGTODO  TEST-BL-798E

	    Log.d(TAG,"getDeviceName :="+name);
	    return name;
    }	

	 private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
			Log.d(TAG, "mReceiver onReceive action:="+action);
Log.d("www","----"+action);
			if (action.equals(QXJ_SCAN_START_ACTION)) {   //for uniapp 


				 Log.d(TAG, "mReceiver QXJ_SCAN_START_ACTION mSendMode:= "+mSendMode);

				 if(!readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT).equals(OUTPUT_BROADCAST)){

					    client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                        client.enableOverWriteOutput(false);
                        mSendMode = OUTPUT_BROADCAST;
                        writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST);		 	
                        Log.d(TAG, "mReceiver QXJ_SCAN_START_ACTION setScanReceiveMode BROADCAST_CALLBACK ");
				 }

				 client.startScan();

			}
			else if (action.equals("com.qxj.scan.phonenumber.enable")) {   //for aliOCR 			 

				 boolean isPhoneEnabled = intent.getBooleanExtra("enable", false);

				 Log.d(TAG, "mReceiver com.qxj.scan.phonenumber.enable isPhoneEnabled:= "+isPhoneEnabled);

				 if(isPhoneEnabled){
				 	
				 	initAliOcrSdk();
					
					if(client.isScanImgSaveSupported()){
						client.enableScanImgSave(true);
						client.setScanImgMode("broadcast");  //for aliOCR

			        }	

					 if(!readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_DIRECT).equals(OUTPUT_BROADCAST)){

					    client.setScanReceiveMode(ScanReceiveMode.BROADCAST_CALLBACK);
                        client.enableOverWriteOutput(false);
                        mSendMode = OUTPUT_BROADCAST;
                        writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST);		 	
                        Log.d(TAG, "mReceiver com.qxj.scan.phonenumber.enable setScanReceiveMode BROADCAST_CALLBACK ");
				    }
				 }else{
                      client.enableScanImgSave(false);
				 }

			}

			

			else if (action.equals("com.android.scanner.service_settings")) {  //for sdk of eastaeon

			     String sAction = intent.getStringExtra("action_barcode_broadcast");
				 String sData = intent.getStringExtra("key_barcode_broadcast");

				 Log.d(TAG, "mReceiver service_settings action:="+sAction+" data:= "+sData);

				 if(!TextUtils.isEmpty(action)&&!TextUtils.isEmpty(sData)){

					 writeToPreference(PREF_KEY_SCAN_BROADCAST_ACTION, sAction);
					 writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, sData);
				 	
				 }

			}
			
            else if (action.equals("cn.rq.pda.action.receivescan")) {//for scan result cn.rq.pda.action.receivescan //com.rq.cenon.receivescanaction

			     

				   sym = intent.getStringExtra("symbology");  //  symbology  type
				   
				  // if(sym.indexOf('_') != -1)
				      //sym = sym.substring(sym.indexOf('_')+1);
				   
 				   barcode = intent.getStringExtra("barcode"); //  barcode  data
				Log.e("www","----"+barcode);
               if( Settings.Global.getInt(mContext.getContentResolver(), "rq.broadcast.geek",1)==1) {
				   sendDefaultgeekBroadcast(barcode, sym);//added by wxw for geek
				   Log.e("www1","----"+barcode);
			   }
				  mSendMode =readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST); //  for JR008  TODO
		          broadcast_cust_action = readFromPreference(PREF_KEY_SCAN_BROADCAST_ACTION, "");
		          broadcast_cust_key = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, "");
				  broadcast_cust_key_datatype = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATATYPE, ""); //for BEST
				  broadcast_cust_key_dataextra = readFromPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATAEXTRA, "");//for BEST

				  mPhoneNumberEnabled = readFromPreference(PREF_KEY_SCAN_PHONENUMBER_ENABLE, "false");  //for aliOCR
				  
                  Log.d(TAG, "mReceiver cn.rq.pda.action.receivescan  barcode:="+ barcode+" sym:="+sym+" mSendMode:= "+mSendMode
				  	           +" <<broadcast_cust_action:="+broadcast_cust_action+" <<broadcast_cust_key:="+broadcast_cust_key+" mPhoneNumberEnabled:= "+mPhoneNumberEnabled);
                
				  if(mSendMode.equals(OUTPUT_BROADCAST)){

                      /*  new Thread(new Runnable() { //DEBUGTODO
			                @Override
			                public void run() {
			                  try{
			                     //inst.sendStringSync(barcode);  //DEBUG	
			                      inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_A));
								  inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_A));

								  inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));
								  inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_ENTER));
			                   }catch (Exception e) {
                                  e.printStackTrace();
							   }
			                }
			            }).start(); */
					 

                      sendDefaultEastaeonBroadcast(barcode);// for sdk of eastaeon  
                       
                      if(getDeviceName().equals("BL-798E")){ //for BEST
						   
                           Log.d(TAG,"cn.rq.pda.action.receivescan imgPath:= "+imgPath);
						    if(client.isScanImgSaveSupported()&&client.isScanImgSaveEnabled()&&TextUtils.isEmpty(imgPath))  //  QXJ_SCAN_IMAGE_ACTION received  before cn.rq.pda.action.receivescan
								return;
                      	}
					
				  	 Intent custIntent = new Intent();

					 if(!broadcast_cust_action.equals("")){
					 	custIntent.setAction(broadcast_cust_action);

					 }else{
                        if(deviceName.equals("JR008")){  //for JROO8
					      custIntent.setAction(JR_ACTION);
                         
                       	}else if(deviceName.equals("BL-798E")){  //for BEST
					       custIntent.setAction(BEST_ACTION);
                          
                       	}
					    
					 }

					 if(!broadcast_cust_key.equals("")){
					 	custIntent.putExtra(broadcast_cust_key, barcode);
					 }else{

					    if(deviceName.equals("JR008")){  //for JROO8
					       custIntent.putExtra(JR_EXTRA, barcode);
                       	}else if(deviceName.equals("BL-798E")){  //for BEST					        
                           custIntent.putExtra(BEST_KEY_DATA, barcode);
						  
                       	}

					 }

					 if(!broadcast_cust_key_datatype.equals("")){
					 	if(deviceName.equals("BL-798E"))
					 	   custIntent.putExtra(broadcast_cust_key_datatype, sym);
					 }else{

					   if(deviceName.equals("BL-798E")){  //for BEST					        
                          
						   custIntent.putExtra(BEST_KEY_DATATYPE, sym);   // sym  eric-zhao  
                       	}

					 }
					

					 if(deviceName.equals("BL-798E")&&client.isScanImgSaveSupported()&&client.isScanImgSaveEnabled()){
					
						 long currTime = System.currentTimeMillis();

						
						  File file = new File(imgPath);  // DEBUG  

						/*  //for DEBUG
						 long fileTime = file.lastModified();						 
						 SimpleDateFormat formatter = new SimpleDateFormat(mformatType);						   
						 Log.d(TAG,"BEST fileTime:= "+formatter.format(fileTime)+" currTime:= "+currTime+" isExist:= "+file.exists());
						 */
						 
						  Log.d(TAG,"BEST currTime:= "+currTime +" isExist:= "+file.exists());

                        
						  BestScanExtraBean  json = new BestScanExtraBean(Long.toString(currTime),imgPath   //imgPath   filePath
	                                          ,"1280","800",points); 
						  
					      Gson gson = new Gson();						
						  String bestExtra = gson.toJson(json);	
										   

						  if(!broadcast_cust_key_dataextra.equals("")){
	                          custIntent.putExtra(broadcast_cust_key_dataextra,bestExtra );
						  }else{
	                          custIntent.putExtra(BEST_KEY_EXTRA,bestExtra );
						  }

						  Log.d(TAG,"BEST Extra:= "+bestExtra);
						  
					 }

					 if(!TextUtils.isEmpty(custIntent.getAction())){
					    sendBroadcast(custIntent);  

						  // sendDefaultEastaeonBroadcast(barcode);//  moved
					    Log.d(TAG,"sendBroadcast  action:=  "+custIntent.getAction() );
					 }

                     if(deviceName.equals("BL-798E")){  //added by eric-zhao
					   sym = "";
					   barcode ="";
					   imgPath = "";
                     }
					 
					   

					  
					 
                    /* if(!broadcast_cust_action.equals("")&&!broadcast_cust_key.equals("")){
                         
                          Log.d(TAG, "mReceiver cn.rq.pda.action.receivescan  broadcast_Action:="+ broadcast_cust_action+" broadcast_Extra:="+broadcast_cust_key);
                          custIntent.setAction(broadcast_cust_action);
                          custIntent.putExtra(broadcast_cust_key, barcode);
						 // custIntent.addFlags(0x01000000);   //DEBUG  eric-zhao  0x01000000- Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND
						 
                     }else{
                       if(getDeviceName().equals("JR008")){  //for JROO8
					      custIntent.setAction(JR_ACTION);
                          custIntent.putExtra(JR_EXTRA, barcode);
                       	}else if(getDeviceName().equals("BL-798E")){  //for BEST
					       custIntent.setAction(BEST_ACTION);
                           custIntent.putExtra(BEST_KEY_DATA, barcode);
						   custIntent.putExtra(BEST_KEY_DATATYPE, sym);
                       	}
					   Log.d(TAG, "recevie cn.rq.pda.action.receivescan  " );
                     }*/
					 
				  }else if(mSendMode.equals(OUTPUT_CLIP)){

				      mClipData = ClipData.newPlainText("text",barcode);
                      mClipboardManager.setPrimaryClip(mClipData);

				  }/*else if(mSendMode.equals(OUTPUT_OVERWRITE)){

                         clearFocusEditText();
				  }*/
				  
				  
            }else if(action.equals(JR_SCAN_SETTING_ACTION)){

			   String endChar = intent.getStringExtra(JR_SCAN_SETTING_EXTRA_END);
			   String outMode = intent.getStringExtra(JR_SCAN_SETTING_EXTRA_SEND_MODE);
            
               Log.d(TAG, "mReceiver JR_SCAN_SETTING_ACTION endChar:="+endChar+" outMode:="+outMode);

               if(endChar!= null&&!endChar.equals("")){
					   switch(endChar){

						   case JR_SCAN_SETTING_END_CHAR_ENTER:
						   	 client.setAdditionalKeyCode(KeyEvent.KEYCODE_ENTER);
							 client.setScanResultSuffix("");
							 writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));

							  break;

						   case JR_SCAN_SETTING_END_CHAR_TAB:
		                       client.setAdditionalKeyCode(KeyEvent.KEYCODE_TAB);
							   client.setScanResultSuffix("");
							   writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
							  break;	 


						   case JR_SCAN_SETTING_END_CHAR_SPACE:
		                       client.setScanResultSuffix(" ");
						       client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);  //clear additionKey
						       writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
							  break;	

						   case JR_SCAN_SETTING_END_CHAR_NONE:
		                        client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);
							    client.setScanResultSuffix("");
								writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
							  break;	   

					    }
            	}

				if(outMode!= null&&!outMode.equals("")){
					    switch(outMode){

						   case JR_SCAN_SETTING_SEND_MODE_FOCUS:

							  mSendMode = OUTPUT_DIRECT;
						   	  //writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
							  //client.setScanReceiveMode(getSendMode(mSendMode));

							  break;

						   case JR_SCAN_SETTING_SEND_MODE_FOCUS_OVERWRITE:

							  mSendMode = OUTPUT_OVERWRITE;
						   	  //writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
							  //client.setScanReceiveMode(getSendMode(mSendMode));

							  break;	 


						   case JR_SCAN_SETTING_SEND_MODE_BROADCAST:

							  mSendMode = OUTPUT_BROADCAST;
						   	  //writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
							  //client.setScanReceiveMode(getSendMode(mSendMode));

							  break;	

						   case JR_SCAN_SETTING_SEND_MODE_CLIPBOARD:

							  mSendMode = OUTPUT_CLIP;
						   	  //writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
							  //client.setScanReceiveMode(getSendMode(mSendMode));

							  break;	  

					   }

					   writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
					   client.setScanReceiveMode(getSendMode(mSendMode));	
				}	
			   
            }else if(action.equals(JR_SCAN_BROADCAST_SETTING_ACTION)){

			   String name = intent.getStringExtra(JR_SCAN_BROADCAST_SETTING_EXTRA_NAEM);
			   String key = intent.getStringExtra(JR_SCAN_BROADCAST_SETTING_EXTRA_KEY);
               Log.d(TAG, "mReceiver JR_SCAN_BROADCAST_SETTING_ACTION  new action:="+name+" key:="+key);

               broadcast_cust_action = name;
			   broadcast_cust_key = key;
			   writeToPreference(PREF_KEY_SCAN_BROADCAST_ACTION, name);
               writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, key);
            }
			else if(action.equals(BEST_SCAN_SETTING_ACTION)){ //for BEST

                Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  received " );
				
				if (intent.hasExtra(BEST_SCAN_SETTING_SEND_MODE)){
				    String outMode = intent.getStringExtra(BEST_SCAN_SETTING_SEND_MODE);
					Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_SEND_MODE := "+ outMode);
					if(outMode!= null&&!outMode.equals("")){
						    switch(outMode){	
								
							   case BEST_SCAN_SETTING_SEND_MODE_BROADCAST:
								   mSendMode = OUTPUT_BROADCAST;
								  break;						    

						   }

						   writeToPreference(PREF_KEY_SCAN_OUTPUT_MODE, mSendMode);
						   client.setScanReceiveMode(getSendMode(mSendMode));	
					}
				}

				if (intent.hasExtra(BEST_SCAN_BROADCAST_SETTING_ACTION)){

				   String actionName = intent.getStringExtra(BEST_SCAN_BROADCAST_SETTING_ACTION);
				   Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_BROADCAST_SETTING_ACTION := "+ actionName);
				   if(actionName!= null&&!actionName.equals("")){
	                  broadcast_cust_action = actionName;			   
				      writeToPreference(PREF_KEY_SCAN_BROADCAST_ACTION, actionName);                  
					}
				}
				if (intent.hasExtra(BEST_SCAN_BROADCAST_SETTING_KEY_DATA)){
			        String data = intent.getStringExtra(BEST_SCAN_BROADCAST_SETTING_KEY_DATA);
					 Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_BROADCAST_SETTING_KEY_DATA := "+ data);
					if(data!= null&&!data.equals("")){
	                  broadcast_cust_key = data;	
	                   writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA, data);
					}
				}

				if (intent.hasExtra(BEST_SCAN_BROADCAST_SETTING_KEY_DATATYPE)){
				   String dataType = intent.getStringExtra(BEST_SCAN_BROADCAST_SETTING_KEY_DATATYPE);
				    Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_BROADCAST_SETTING_KEY_DATATYPE := "+ dataType);
				   if(dataType!= null&&!dataType.equals("")){
	                   broadcast_cust_key_datatype = dataType;	
	                   writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATATYPE, dataType);
					}
				}

				if (intent.hasExtra(BEST_SCAN_BROADCAST_SETTING_KEY_EXTRA)){
				   String dataExtra = intent.getStringExtra(BEST_SCAN_BROADCAST_SETTING_KEY_EXTRA);
				    Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_BROADCAST_SETTING_KEY_EXTRA := "+ dataExtra);
				   if(dataExtra!= null&&!dataExtra.equals("")){
	                   broadcast_cust_key_dataextra = dataExtra;	
	                   writeToPreference(PREF_KEY_SCAN_BROADCAST_EXTRA_DATAEXTRA, dataExtra);
					}
				}				
				


               if (intent.hasExtra(BEST_SCAN_SETTING_QRCODE)){
					boolean qrcodeOn = intent.getBooleanExtra(BEST_SCAN_SETTING_QRCODE,false);
					Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_QRCODE := "+ qrcodeOn);
					client.setScannerParameter("sym_qr_enable",qrcodeOn == true? "1":"0");
               	}
				
                if (intent.hasExtra(BEST_SCAN_SETTING_PREFIX)){ 
					String prefixData = intent.getStringExtra(BEST_SCAN_SETTING_PREFIX); 
					Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_PREFIX := "+ prefixData);
					 if(prefixData!= null&&!prefixData.equals("")){
	                     client.setScanResultPrefix(prefixData);
					 }
                }

				 if (intent.hasExtra(BEST_SCAN_SETTING_SUFFIX)){ 
					String suffixData = intent.getStringExtra(BEST_SCAN_SETTING_SUFFIX); 
					Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_SUFFIX := "+ suffixData);
					if(suffixData!= null&&!suffixData.equals("")){
	                     client.setScanResultSuffix(suffixData);
					 }
				 }

				if (intent.hasExtra(BEST_SCAN_SETTING_ENDCHAR)){
					 String endChar = intent.getStringExtra(BEST_SCAN_SETTING_ENDCHAR);

					 Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_ENDCHAR := "+ endChar);
					 if(endChar!= null&&!endChar.equals("")){
						   switch(endChar){

							   case JR_SCAN_SETTING_END_CHAR_ENTER:
							   	 client.setAdditionalKeyCode(KeyEvent.KEYCODE_ENTER);
								 client.setScanResultSuffix("");
								 writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));

								  break;

							   case JR_SCAN_SETTING_END_CHAR_TAB:
			                       client.setAdditionalKeyCode(KeyEvent.KEYCODE_TAB);
								   client.setScanResultSuffix("");
								   writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
								  break;	 


							   case JR_SCAN_SETTING_END_CHAR_SPACE:
			                       client.setScanResultSuffix(" ");
							       client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);  //clear additionKey
							       writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
								  break;	

							   case JR_SCAN_SETTING_END_CHAR_NONE:
			                        client.setAdditionalKeyCode(KeyEvent.KEYCODE_0);
								    client.setScanResultSuffix("");
									writeToPreference(PREF_KEY_SCAN_SUFFIX_CHAR, getCharFromSetting(endChar));
								  break;	   

						    }
	            	}
				}

				if (intent.hasExtra(BEST_SCAN_SETTING_SOUND_PLAY)){
	                boolean soundOn = intent.getBooleanExtra(BEST_SCAN_SETTING_SOUND_PLAY,false); 
					 Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_SOUND_PLAY := "+ soundOn);
				    client.enableScanSound(soundOn);
	                writeToPreference(PREF_KEY_SCAN_SOUND_ALERT, soundOn == true? "true":"false");  
				}

				if (intent.hasExtra(BEST_SCAN_SETTING_VIBERATE)){
					boolean viberateOn = intent.getBooleanExtra(BEST_SCAN_SETTING_VIBERATE,false);
					 Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_VIBERATE := "+ viberateOn);
					client.enableScanVibrator(viberateOn);                
	                writeToPreference(BEST_SCAN_SETTING_VIBERATE, viberateOn == true? "true":"false");
				}

                if (intent.hasExtra(BEST_SCAN_SETTING_CONTINUE_SCAN)){
					boolean continueScanOn = intent.getBooleanExtra(BEST_SCAN_SETTING_CONTINUE_SCAN,false);
					 Log.d(TAG, "mReceiver BEST_SCAN_SETTING_ACTION  BEST_SCAN_SETTING_CONTINUE_SCAN := "+ continueScanOn);
					client.enableContinueScan(continueScanOn);
                }				 
	

			}else if(action.equals(QXJ_SCAN_IMAGE_ACTION)){ 

			     
				  barcode = intent.getStringExtra("scanNumber");
				  imgData = intent.getByteArrayExtra("scanImage");
				  
				  imgPath = intent.getStringExtra("save_path");
				  if(!TextUtils.isEmpty(imgPath)&&imgPath.indexOf("sdcard") != -1)
					     imgPath = imgPath.replace("sdcard","storage/emulated/0");  //eric-zhao DEBUG
					     
				 int x1 = intent.getIntExtra("x1", 0);
				 int y1 = intent.getIntExtra("y1", 0);
				 int x2 = intent.getIntExtra("x2", 0);
				 int y2 = intent.getIntExtra("y2", 0);
				 int x3 = intent.getIntExtra("x3", 0);
				 int y3 = intent.getIntExtra("y3", 0);
				 int x4 = intent.getIntExtra("x4", 0);
				 int y4 = intent.getIntExtra("y4", 0);

				 points =new int[]{x1,y1,x2,y2,x3,y3,x4,y4};          

				 if(mPhoneNumberEnabled.equals("true") &&(imgData != null)){ //for aliOCR
                    Log.d(TAG,"scanBitmap aliOCR start");
					scanBitmap(imgData);
				 }	
			  
			  
                 if(!TextUtils.isEmpty(imgPath)&&getDeviceName().equals("BL-798E")){ //for BEST
                     File file = new File(imgPath);
					 
					 Log.d(TAG,"QXJ_SCAN_IMAGE_ACTION sym:= "+sym+" imgPath:= "+imgPath+" file exists:= "+file.exists());  

					 if(TextUtils.isEmpty(sym)) { //  QXJ_SCAN_IMAGE_ACTION received  before cn.rq.pda.action.receivescan                    

						return;
                 	}

				  if(readFromPreference(PREF_KEY_SCAN_OUTPUT_MODE, OUTPUT_BROADCAST).equals(OUTPUT_BROADCAST)){
				  	 Intent custIntent = new Intent();

					 if(!broadcast_cust_action.equals("")){
					 	custIntent.setAction(broadcast_cust_action);

					 }else{                    
					    custIntent.setAction(BEST_ACTION);                         
					    
					 }

					 if(!broadcast_cust_key.equals("")){
						custIntent.putExtra(broadcast_cust_key, barcode);
						
					 }else{					 				        
                        custIntent.putExtra(BEST_KEY_DATA, barcode);  
               		 }

					 if(!broadcast_cust_key_datatype.equals("")){
					 
					 	custIntent.putExtra(broadcast_cust_key_datatype, sym);
					 }else{
                          
						custIntent.putExtra(BEST_KEY_DATATYPE, sym);   // sym  
                      

					 }			  

					
					  long currTime = System.currentTimeMillis();

                     /* if(!TextUtils.isEmpty(imgPath)){
						   File tmpfile = new File(imgPath);
						   long fileTime = tmpfile.lastModified();
						   Log.d(TAG,"QXJ_SCAN_IMAGE_ACTION fileTime:= "+fileTime+" currTime:= "+currTime);
				  	 }*/
                    

                      BestScanExtraBean  json1 = new BestScanExtraBean(Long.toString(currTime),imgPath
                                          ,"1280","800",points);
					  Gson gson = new Gson();
					  String bestExtra = gson.toJson(json1);

					  if(!broadcast_cust_key_dataextra.equals("")){
                          custIntent.putExtra(broadcast_cust_key_dataextra,bestExtra );
					  }else{
                          custIntent.putExtra(BEST_KEY_EXTRA,bestExtra );
					  }
					  
					  sendBroadcast(custIntent);  

					  Log.d(TAG,"sendBroadcast  222 " );

                 /*
                      //start ygf
					    //byte[] bmpData = intent.getByteArrayExtra("scanImage");

                   byte[] bmpData = new byte[1280*800*3];
				   Bitmap img  = getLoacalBitmap(imgPath);
				   if(img != null)
				    bmpData = Bitmap2Bytes(img);

			       Log.d(TAG, "mReceiver YGF QXJ_SCAN_IMAGE_ACTION 222  save_path := "+ imgPath+" barcode:= "+barcode);

				   Intent imgIntent = new Intent();
				   imgIntent.setAction("com.ygf.scanner.image"); 
				   imgIntent.putExtra("barcode", barcode);  
				   imgIntent.putExtra("jpegData", bmpData);  				 
                   sendBroadcast(imgIntent);
				   //end of ygf
			 */		

					   sym = "";
					   barcode ="";
					   imgPath = "";
					 
        
					 
				  }

				 

                }	
			}
        }
    };

    public void sendDefaultEastaeonBroadcast(String code){// for sdk of eastaeon
             Intent custIntent = new Intent();

			 custIntent.setAction("com.android.server.scannerservice.broadcast");// the default broadcast of eastaeon
			 custIntent.putExtra("scannerdata",code );

		     sendBroadcast(custIntent); 

			 Log.d(TAG,"sendDefaultEastaeonBroadcast  111 " );
			 
    }
	public void sendDefaultgeekBroadcast(String code,String sym){// for sdk of geek
		Intent custIntent = new Intent();

		custIntent.setAction("com.geenk.action.GET_SCANDATA");
		if(!TextUtils.isEmpty(code)){
			byte[] bytes = code.getBytes();
			custIntent.putExtra("data",bytes);
			custIntent.putExtra("codeType",sym);
			sendBroadcast(custIntent);
			Log.d(TAG,"sendDefaultgeekBroadcast  111 " );
		}




	}
	public ScanReceiveMode getSendMode(String value){
         ScanReceiveMode res = ScanReceiveMode.BROADCAST_CALLBACK;

	     switch (value) {
            case OUTPUT_DIRECT:
                res= ScanReceiveMode.AUTO_ENTER_IN_FOCUS;
                break;
            case OUTPUT_BROADCAST:
                res= ScanReceiveMode.BROADCAST_CALLBACK;
                break;

			 case OUTPUT_OVERWRITE:
                res= ScanReceiveMode.AUTO_ENTER_IN_FOCUS;
                break;
            case OUTPUT_CLIP:
                res= ScanReceiveMode.BROADCAST_CALLBACK;
                break;
        }
	   return res; //eric-zhao

	}	

	public String getCharFromSetting(String  value){
          String res = CHAR_NULL;

		  switch(value){

			   case JR_SCAN_SETTING_END_CHAR_ENTER:
			   	  res=CHAR_ENTER;
				  break;

			   case JR_SCAN_SETTING_END_CHAR_TAB:
                   res=CHAR_TAB;
				  break;	 


			   case JR_SCAN_SETTING_END_CHAR_SPACE:
                   res=CHAR_SPACE;  
				  break;	

			   case JR_SCAN_SETTING_END_CHAR_NONE:
                   res=CHAR_NULL;
				  break;	  

				   }
		   return res; //eric-zhao

	}	


	private void disableInstallPackage() {  //eric-zhao added
		Intent intent = new Intent(DISABLE_INSTALL_PACKAGE);
		intent.putExtra(DISABLE_INSTALL_PACKAGE, 0);
		sendBroadcast(intent); 
		 
	}
	
  


	 public void writeToPreference(String name, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit();  //Context.MODE_PRIVATE  -- 0
        editor.putString(name, value);
        editor.commit();
    }

    public String readFromPreference(String name, String defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getString(name, defaultvalue);
    }

	public void writeIntToPreference(String name, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).edit(); //  
        editor.putInt(name, value);
        editor.commit();
    }

	 public int readIntFromPreference(String name, int defaultvalue) {
        return getSharedPreferences(myPref, Context.MODE_MULTI_PROCESS).getInt(name, defaultvalue);
    }


    //for aliOCR
    
      /*public void initAliOCR(){ 
            AuthInfoStore authInfoStore = new AuthInfoStore();
	        authInfoStore.deviceId = "862493340090566";  // DEBUG
	        authInfoStore.akId = "LTAI5tH3TBxtFtGL4sA5N9kw";
	        authInfoStore.akSecret = "T7SjTqoEIbzdrhn3bwSGtL2HQxAoow";
	        authInfoStore.appKey = "VFdh6SvJdg2gelZv";
	        authInfoStore.sdkCode = "OCR-sheet";

	       
	        SailScanOCR.Builder builder = SailScanOCR.Builder.phoneSheet().setContext(mContext);
	        ocr = builder.build();
	        callback = (s) -> {
	            Log.d(TAG," phoneNum: = "+s.data+" decodeTime:= "+String.valueOf(s.time));
	        };

            Log.d(TAG," initAliOCR cache:= "+mContext.getCacheDir().getAbsolutePath());
		   
	        OcrConfig ocrConfig = new OcrConfig();
	        ocrConfig.numThread = 6;
	        if (ocr != null)
	            ocr.initAsync(
	                    authInfoStore.deviceId, mContext.getCacheDir().getAbsolutePath(),
	                    authInfoStore.akId,
	                    authInfoStore.akSecret,
	                    authInfoStore.appKey,
	                    authInfoStore.sdkCode, ocrConfig,code -> {
	                        if(code!=DuguangErrCode.D_SUCCESS){
	                             Log.d(TAG,"aliOCR init ERROR " );
	                        }
	                    });


			 Log.d(TAG,"  aliOCR init finish " );
			 
    }*/
    public void initAliOcrSdk(){
		
		    AuthInfoBuilder mAuthInfo = new AuthInfoBuilder(mContext);
            mAuthInfo.setAkId("LTAI5tH3TBxtFtGL4sA5N9kw");
            mAuthInfo.setAkSecret("T7SjTqoEIbzdrhn3bwSGtL2HQxAoow");
            mAuthInfo.setAppKey("VFdh6SvJdg2gelZv");
            mAuthInfo.setSdkCode("OCR-sheet");
            mAuthInfo.setStorePath(mContext.getCacheDir().getAbsolutePath());
            mAuthInfo.setAuthType(AuthTypeEnum.AUTH_TYPE_DEVICE);
            AuthInfoStore mAuthInfoStore= mAuthInfo.build();

			OcrConfig ocrConfig = new OcrConfig();
	        ocrConfig.numThread = 6;// number of threads
			
            SailOcrConfig.Builder sailConfigBuilder = new SailOcrConfig.Builder(mContext);
            sailConfigBuilder.setAuthInfoStore(mAuthInfoStore);
            sailConfigBuilder.setOcrConfig(ocrConfig) ; 
			try{
                 SailOcrConfig sailConfig = sailConfigBuilder.build();
				 SailOcrSdk.init(sailConfig, SailOcrSdk.FLAG_OCR_PHONE_SHEET);
			}catch(AuthenticatorException e){

			}catch (Exception e){
			}

	        

			 ocr= SailOcrSdk.phoneSheet();
			 callback = (s) -> {
	            Log.d(TAG," OCR phoneNum: = "+s.data+" decodeTime:= "+String.valueOf(s.time)+"barcode: = "+barcode);
				
				Intent numIntent = new Intent();
				numIntent.setAction(QXJ_OCR_PHONENUM_ACTION); 
				numIntent.putExtra(QXJ_OCR_NUMBER_EXTRA, (String)s.data);
				numIntent.putExtra(QXJ_OCR_TIME_EXTRA, String.valueOf(s.time));
				numIntent.putExtra(QXJ_OCR_BARCODE_EXTRA, barcode);

				barcode = "";
				  				 
                mContext.sendBroadcast(numIntent);
				
	        };	
	       
			 Log.d(TAG,"  aliOCR init finish " );
			 
    }
	


	 public void scanBitmap(byte[] data){ 
           Log.d(TAG,"scanBitmap 000");
		   
           Bitmap scanImg = BitmapFactory.decodeByteArray(data, 0, data.length);
     
		   /*if(scanImg != null )
		       callback.onResult(ocr.recognizeBitmap(scanImg));*/

             if(scanImg != null && ocr != null ){

				ImageResult s = ocr.recognizeBitmap(scanImg);
		        Log.d(TAG,"OCR Scan phoneNum: = "+s.data+" decodeTime:= "+String.valueOf(s.time)+" barcode: = "+barcode);
				
				Intent numIntent = new Intent();
				numIntent.setAction(QXJ_OCR_PHONENUM_ACTION); 
				numIntent.putExtra(QXJ_OCR_NUMBER_EXTRA, (String)s.data);
				numIntent.putExtra(QXJ_OCR_TIME_EXTRA, String.valueOf(s.time));
				numIntent.putExtra(QXJ_OCR_BARCODE_EXTRA, barcode);
				numIntent.putExtra(QXJ_OCR_IMAGE_EXTRA, data);  //added

				barcode = "";
				  				 
                mContext.sendBroadcast(numIntent);
             }		
       
		 
	 }

/*
	  private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

				Log.d(TAG,"getMacFromHardware := "+res1.toString());
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
  */
	

	//for BEST

 /*
    public static void verifyStoragePermissions(Activity activity) {

        try {
            
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                 
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  Bitmap getLoacalBitmap(String url) {
        try {
			Log.d(TAG,"getLoacalBitmap := "+url);
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
           e.printStackTrace();
            return null;
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                  bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                 return baos.toByteArray();
     }*/

	public class BestScanExtraBean {

    private String scan_time;
	private String image_path;
	private String image_width;
	private String image_height;
    private int[] data_location_points;
     

    public BestScanExtraBean(String scan_time, String image_path, String image_width,
		                                  String image_height, int[] points) {
        this.scan_time = scan_time;
		this.image_path = image_path;
		
        this.image_width = image_width;
		this.image_height = image_height;
		this.data_location_points = points;
    }

    @Override
    public String toString() {
        return "BestScanExtraBean{" +
                "scan_time=" + scan_time + 
                ", image_path=" + image_path +
                ", image_width=" + image_width +
                ", image_height=" + image_height +
                ", data_location_points=" + "["+ data_location_points[0] +","+ data_location_points[1] +","
                                          + data_location_points[2] +","+ data_location_points[3] +","
                                          + data_location_points[4] +","+ data_location_points[5] +","
                                          + data_location_points[6] +","+ data_location_points[7] + 
                                          "]"+                
                '}';
    }

}
 
}
