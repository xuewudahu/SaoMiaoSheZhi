package com.rq.scan;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

//悬浮框的制作
public class FloatViewService extends Service {

    private static final String TAG = "FloatViewService";
    // 定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private ImageButton mFloatView;
    private long starttime;
    private long endTime;

    private boolean isScanning = false;
    private boolean isContinueScanEnabled = false;


    Handler handle;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");


        handle = new Handler() {
        };
        createFloatView();
        Log.d("ffas", "dfasdf");
    }


    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    private void createFloatView() {
        wmParams = new LayoutParams();
        // 通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = LayoutParams.TYPE_PHONE;
        }
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;

        // 设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        // 获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.alert_window_menu, null);
        // 添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        // 浮动窗口按钮
        mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.alert_window_imagebtn);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {
            int x1 = 0;
            int y1 = 0;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isContinueScanEnabled = ((MyApplication)getApplication()).getmActivity().client.isContinueScanEnabled();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        Log.d(TAG,"ACTION_DOWN isScanning:= "+isScanning+ " isContinueScanEnabled:= "+isContinueScanEnabled);

                        ((MyApplication)getApplication()).getmActivity().client.startScan();
                        if( !isContinueScanEnabled){
                           ((MyApplication)getApplication()).getmActivity().client.startScan();
						    Intent intent = new Intent();
                            intent.setAction("start_Scan_time");
                            sendBroadcast(intent);
                        }   						   
                        else{
                            if(!isScanning){
                                ((MyApplication)getApplication()).getmActivity().client.startScan();
                                isScanning = true;
								
								Intent intent = new Intent();
	                            intent.setAction("start_Scan_time");
	                            sendBroadcast(intent);
                            }else{
                                ((MyApplication)getApplication()).getmActivity().client.stopScan();
                                isScanning = false;
                            }
                        }
                        starttime = System.currentTimeMillis();
                        x1 = (int) event.getRawX();
                        y1 = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG,"ACTION_MOVE   " );
                        int x3 = (int) event.getRawX();
                        int y3 = (int) event.getRawY();
                        if (!(Math.abs(x3 - x1) < 50 && Math.abs(y3 - y1) < 50)) {
                            wmParams.x = (int) event.getRawX() - 70;
                            // - mFloatView.getMeasuredWidth()/4;
                            wmParams.y = (int) event.getRawY() - 100;
                            // - mFloatView.getMeasuredHeight();
                            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        }
                        return false;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"ACTION_UP   " );

                        if(!isContinueScanEnabled)
                           ((MyApplication)getApplication()).getmActivity().client.stopScan();
                        endTime = System.currentTimeMillis();
                        break;
                }
                return false;
            }
        });
//        mFloatView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MyApplication)getApplication()).getmActivity().client.startScan();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mFloatLayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*private void startContinueScan() {
        Intent toIntent = new Intent();
        toIntent.setAction("com.rq.startcontinue_scan");
        toIntent.setPackage("com.android.scantest");
        sendBroadcast(toIntent);
    }
    private void stopContinueScan() {
        Intent toIntent = new Intent();
        toIntent.setAction("com.rq.stopcontinue_scan");
        toIntent.setPackage("com.android.scantest");
        sendBroadcast(toIntent);
    }*/

}
