package com.rq.scan;

import android.app.Application;
//sevice中获取activity对象
public class MyApplication extends Application {
        private ScanSettingActivity mActivity;
        public ScanSettingActivity getmActivity() {
            return mActivity;
        }
        public void setmActivity(ScanSettingActivity mActivity) {
            this.mActivity = mActivity;
        }

}
