package com.rq.scan.service;
 

import android.util.Log;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class AssistService extends Service {
 
	private static final String TAG = "AssistService";

   public class LocalBinder extends Binder {
        public AssistService getService() {
            return AssistService.this;
        }
    }
 
    @Override
    public IBinder onBind(Intent intent) {
       Log.d(TAG, "AssistService: onBind()");
        return new LocalBinder();
    }
 
    @Override
     public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	Log.d(TAG, "AssistService: onDestroy()");
     }   
    
 
}
