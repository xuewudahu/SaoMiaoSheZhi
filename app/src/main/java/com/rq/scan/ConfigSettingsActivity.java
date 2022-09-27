package com.rq.scan;



import android.os.Bundle;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import cn.rq.pda.sdk.RqPDAClient;
 
public class ConfigSettingsActivity extends PreferenceActivity {
	//implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{
	private static final String TAG = "BarcodeConfigSettingsActivity";


	public RqPDAClient client;
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		 client = RqPDAClient.newClient(this);     
        
       // setTheme(android.R.style.Theme_Light);
        addPreferencesFromResource(R.xml.configuration_settings);

	   ///

	   CheckBoxPreference aztecCheck = (CheckBoxPreference) findPreference("sym_aztec_enable");

       if(client.getScannerParameter("sym_aztec_enable").equals("1"))
	   	     aztecCheck.setChecked(true);
	   else 
	   	     aztecCheck.setChecked(false);
       aztecCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "Aztec enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_aztec_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_aztec_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_aztec_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "Aztec min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_aztec_min")); 
               	
                    client.setScannerParameter("sym_aztec_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_aztec_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "Aztec max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_aztec_max")); 
               	
                    client.setScannerParameter("sym_aztec_max",newValue.toString()); 
                   return true;
               }

           });

       ////

        CheckBoxPreference codabarCheck = (CheckBoxPreference) findPreference("sym_codabar_enable");

	      if(client.getScannerParameter("sym_codabar_enable").equals("1"))
	   	     codabarCheck.setChecked(true);
	   else 
	   	     codabarCheck.setChecked(false);
	   
        codabarCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "codabar enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_codabar_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_codabar_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_codabar_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "codabar min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_codabar_min")); 
               	
                    client.setScannerParameter("sym_codabar_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_codabar_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "codabar max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_codabar_max")); 
               	
                    client.setScannerParameter("sym_codabar_max",newValue.toString()); 
                   return true;
               }

           });

      findPreference("sym_codabar_check_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_codabar_check_enable setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_codabar_check_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_codabar_check_enable",val);
                   return true;
               }

           });
       
     
       findPreference("sym_codabar_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_codabar_check_transmit_enable setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_codabar_check_transmit_enable")); 

				    String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_codabar_check_transmit_enable",newValue.toString()); 
                   return true;
               }

           });


	    findPreference("sym_codabar_start_stop_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_codabar_start_stop_transmit_enable setting to " + newValue.toString()+" -- old min:= "+client.getScannerParameter("sym_codabar_start_stop_transmit_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_codabar_start_stop_transmit_enable",newValue.toString());
                   return true;
               }

           });
       ////
     
       findPreference("sym_codabar_concatenate_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_codabar_concatenate_enable setting to " + newValue.toString()+" -- old max:= "+client.getScannerParameter("sym_codabar_concatenate_enable")); 

				    String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_codabar_concatenate_enable",newValue.toString()); 
                   return true;
               }

           });

	
	  /* 
       findPreference("sym_code39_check_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code39_check_enable ++"); 
               	
               
               
               	finish();
               		Log.d(TAG, "sym_code39_check_enable ++"); 
                   return true;
               }

           });
       findPreference("sym_code39_start_stop_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code39_start_stop_transmit_enable ++"); 
               	
               
               
               	finish();
               		Log.d(TAG, "sym_code39_start_stop_transmit_enable ++"); 
                   return true;
               }

           });
       
       findPreference("sym_code39_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code39_check_transmit_enable ++"); 
               	
               
               
               	finish();
               		Log.d(TAG, "sym_code39_check_transmit_enable ++"); 
                   return true;
               }

           });
	   */
       
           ////

	      CheckBoxPreference codablockCheck = (CheckBoxPreference) findPreference("sym_codablock_enable");

	      if(client.getScannerParameter("sym_codablock_enable").equals("1"))
	   	     codablockCheck.setChecked(true);
	   else 
	   	     codablockCheck.setChecked(false);
	   

            codablockCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "codeblock enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_codablock_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_codablock_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_codablock_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "codeblock min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_codeblock_min")); 
               	
                    client.setScannerParameter("sym_codeblock_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_codablock_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "codeblock max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_codeblock_max")); 
               	
                    client.setScannerParameter("sym_codeblock_max",newValue.toString()); 
                   return true;
               }

           });

       ////

	      CheckBoxPreference code11Check = (CheckBoxPreference) findPreference("sym_code11_enable");

	      if(client.getScannerParameter("sym_code11_enable").equals("1"))
	   	     code11Check.setChecked(true);
	      else 
	   	     code11Check.setChecked(false);
       
	   code11Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "codeb11 enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_codeb11_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_codeb11_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_code11_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code11_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code11_min")); 
               	
                    client.setScannerParameter("sym_code11_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_code11_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code11_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_code11_max")); 
               	
                    client.setScannerParameter("sym_code11_max",newValue.toString()); 
                   return true;
               }

           });
	    findPreference("sym_code11_check_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code11_check_enable setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code11_check_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code11_check_enable",val);
                   return true;
               }

           });

		////

		   CheckBoxPreference code39Check = (CheckBoxPreference) findPreference("sym_code39_enable");

	      if(client.getScannerParameter("sym_code39_enable").equals("1"))
	   	     code39Check.setChecked(true);
	      else 
	   	     code39Check.setChecked(false);
		 code39Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code39_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_code39_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_code39_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_code39_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code39_min")); 
               	
                    client.setScannerParameter("sym_code39_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_code39_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_code39_max")); 
               	
                    client.setScannerParameter("sym_code39_max",newValue.toString()); 
                   return true;
               }

           });

      findPreference("sym_code39_check_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_check_enable setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code39_check_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code39_check_enable",val);
                   return true;
               }

           });
       
     
       findPreference("sym_code39_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_check_transmit_enable setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_code39_check_transmit_enable")); 

				    String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code39_check_transmit_enable",newValue.toString()); 
                   return true;
               }

           });


	    findPreference("sym_code39_start_stop_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_start_stop_transmit_enable setting to " + newValue.toString()+" -- old min:= "+client.getScannerParameter("sym_code39_start_stop_transmit_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code39_start_stop_transmit_enable",newValue.toString());
                   return true;
               }

           });

		 findPreference("sym_code39_append_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_append_enable setting to " + newValue.toString()+" -- old min:= "+client.getScannerParameter("sym_code39_append_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code39_append_enable",newValue.toString());
                   return true;
               }

           });

		  findPreference("sym_code39_fullascii_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code39_fullascii_enable setting to " + newValue.toString()+" -- old min:= "+client.getScannerParameter("sym_code39_fullascii_enable")); 

					String val  = newValue.toString().equals("true")? "1":"0";
                    client.setScannerParameter("sym_code39_fullascii_enable",newValue.toString());
                   return true;
               }

           });

		  ////
		   CheckBoxPreference gridmatricCheck = (CheckBoxPreference) findPreference("sym_gridmatrix_enable");

	      if(client.getScannerParameter("sym_gridmatrix_enable").equals("1"))
	   	     gridmatricCheck.setChecked(true);
	      else 
	   	     gridmatricCheck.setChecked(false);

		   gridmatricCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "gridmatrix enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_gridmatrix_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_gridmatrix_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_gridmatrix_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "gridmatrix min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_gridmatrix_min")); 
               	
                    client.setScannerParameter("sym_gridmatrix_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_gridmatrix_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "gridmatrix max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_gridmatrix_max")); 
               	
                    client.setScannerParameter("sym_gridmatrix_max",newValue.toString()); 
                   return true;
               }

           });

	    ////

		   CheckBoxPreference code93Check = (CheckBoxPreference) findPreference("sym_code93_enable");

	      if(client.getScannerParameter("sym_code93_enable").equals("1"))
	   	     code93Check.setChecked(true);
	      else 
	   	     code93Check.setChecked(false);

		   code93Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code93_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_code93_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_code93_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_code93_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code93_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code93_min")); 
               	
                    client.setScannerParameter("sym_code93_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_code93_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code93_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_code93_max")); 
               	
                    client.setScannerParameter("sym_code93_max",newValue.toString()); 
                   return true;
               }

           });

        ////
             CheckBoxPreference code128Check = (CheckBoxPreference) findPreference("sym_code128_enable");
         	Log.d(TAG, "sym_code128_enable value:= " +client.getScannerParameter("sym_code128_enable")); 
        
	      if(client.getScannerParameter("sym_code128_enable").equals("1"))
	   	     code128Check.setChecked(true);
	      else 
	   	     code128Check.setChecked(false);

		   code128Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_code128_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_code128_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_code128_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_code128_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code128_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_code128_min")); 
               	
                    client.setScannerParameter("sym_code128_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_code128_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_code128_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_code128_max")); 
               	
                    client.setScannerParameter("sym_code128_max",newValue.toString()); 
                   return true;
               }

           });


       ////

	       CheckBoxPreference compositeCheck = (CheckBoxPreference) findPreference("sym_composite_enable");
         
        
	      if(client.getScannerParameter("sym_composite_enable").equals("1"))
	   	     compositeCheck.setChecked(true);
	      else 
	   	     compositeCheck.setChecked(false);

		   compositeCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "composite enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_composite_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_composite_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_composite_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "composite min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_composite_min")); 
               	
                    client.setScannerParameter("sym_composite_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_composite_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "composite max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_composite_max")); 
               	
                    client.setScannerParameter("sym_composite_max",newValue.toString()); 
                   return true;
               }

           });

	    findPreference("sym_composite_upc_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_composite_upc_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_composite_upc_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_composite_upc_enable",val);
                   return true;
               }

           });

		 ////

		  
	   
      
       findPreference("sym_chinapost_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "chinapost min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_chinapost_min")); 
               	
                    client.setScannerParameter("sym_chinapost_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_chinapost_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "chinapost max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_chinapost_max")); 
               	
                    client.setScannerParameter("sym_chinapost_max",newValue.toString()); 
                   return true;
               }

           });

	    ////
	     CheckBoxPreference datamatrixCheck = (CheckBoxPreference) findPreference("sym_datamatrix_enable");
         
        
	      if(client.getScannerParameter("sym_datamatrix_enable").equals("1"))
	   	     datamatrixCheck.setChecked(true);
	      else 
	   	     datamatrixCheck.setChecked(false);

		   datamatrixCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "datamatrix enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_datamatrix_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_datamatrix_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_datamatrix_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "datamatrix min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_datamatrix_min")); 
               	
                    client.setScannerParameter("sym_datamatrix_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_datamatrix_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "datamatrix max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_datamatrix_max")); 
               	
                    client.setScannerParameter("sym_datamatrix_max",newValue.toString()); 
                   return true;
               }

           });

	     ////
	      CheckBoxPreference ean8Check = (CheckBoxPreference) findPreference("sym_ean8_enable");
         
        
	      if(client.getScannerParameter("sym_ean8_enable").equals("1"))
	   	     ean8Check.setChecked(true);
	      else 
	   	     ean8Check.setChecked(false);

		   ean8Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_enable",val);
                   return true;
               }

           });

		  findPreference("sym_ean8_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_check_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_ean8_addenda_separator_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_addenda_separator_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_addenda_separator_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_addenda_separator_enable",val);
                   return true;
               }

           });

		  findPreference("sym_ean8_2_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_2_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_2_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_2_digit_addenda_enable",val);
                   return true;
               }

           });

		   findPreference("sym_ean8_5_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_5_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_5_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_5_digit_addenda_enable",val);
                   return true;
               }

           });

		    findPreference("sym_ean8_addenda_required_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean8_addenda_required_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean8_addenda_required_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean8_addenda_required_enable",val);
                   return true;
               }

           });

        ////

		  CheckBoxPreference ean13Check = (CheckBoxPreference) findPreference("sym_ean13_enable");
          Log.d(TAG,"sym_ean13_enable value:= "+client.getScannerParameter("sym_ean13_enable"));
        
	      if(client.getScannerParameter("sym_ean13_enable").equals("1"))
	   	     ean13Check.setChecked(true);
	      else 
	   	     ean13Check.setChecked(false);

		   ean13Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_enable",val);
                   return true;
               }

           });

		  findPreference("sym_ean13_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_check_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_ean13_addenda_separator_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_addenda_separator_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_addenda_separator_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_addenda_separator_enable",val);
                   return true;
               }

           });

		  findPreference("sym_ean13_2_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_2_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_2_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_2_digit_addenda_enable",val);
                   return true;
               }

           });

		   findPreference("sym_ean13_5_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_5_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_5_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_5_digit_addenda_enable",val);
                   return true;
               }

           });

		    findPreference("sym_ean13_addenda_required_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_ean13_addenda_required_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_ean13_addenda_required_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_ean13_addenda_required_enable",val);
                   return true;
               }

           });

         ////
          CheckBoxPreference gs1128Check = (CheckBoxPreference) findPreference("sym_gs1_128_enable");
         
        
	      if(client.getScannerParameter("sym_gs1_128_enable").equals("1"))
	   	     gs1128Check.setChecked(true);
	      else 
	   	     gs1128Check.setChecked(false);

		   gs1128Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_gs1_128_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_gs1_128_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_gs1_128_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_gs1_128_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_gs1_128_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_gs1_128_min")); 
               	
                    client.setScannerParameter("sym_gs1_128_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_gs1_128_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_gs1_128_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_gs1_128_max")); 
               	
                    client.setScannerParameter("sym_gs1_128_max",newValue.toString()); 
                   return true;
               }

           });

	   ////
	       CheckBoxPreference hanxinCheck = (CheckBoxPreference) findPreference("sym_hanxin_enable");
         
        
	      if(client.getScannerParameter("sym_hanxin_enable").equals("1"))
	   	     hanxinCheck.setChecked(true);
	      else 
	   	     hanxinCheck.setChecked(false);

		   hanxinCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_hanxin_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_hanxin_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_hanxin_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_hanxin_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_hanxin_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_hanxin_min")); 
               	
                    client.setScannerParameter("sym_hanxin_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_hanxin_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_hanxin_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_hanxin_max")); 
               	
                    client.setScannerParameter("sym_hanxin_max",newValue.toString()); 
                   return true;
               }

           });

	    ////
           CheckBoxPreference iata25Check = (CheckBoxPreference) findPreference("sym_iata25_enable");
         
        
	      if(client.getScannerParameter("sym_iata25_enable").equals("1"))
	   	     iata25Check.setChecked(true);
	      else 
	   	     iata25Check.setChecked(false);
		  
		   iata25Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_iata25_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_iata25_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_iata25_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_iata25_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_iata25_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_iata25_min")); 
               	
                    client.setScannerParameter("sym_iata25_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_iata25_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_iata25_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_iata25_max")); 
               	
                    client.setScannerParameter("sym_iata25_max",newValue.toString()); 
                   return true;
               }

           });


	    ////
	     CheckBoxPreference int25Check = (CheckBoxPreference) findPreference("sym_int25_enable");
         
        
	      if(client.getScannerParameter("sym_int25_enable").equals("1"))
	   	     int25Check.setChecked(true);
	      else 
	   	     int25Check.setChecked(false);

		   int25Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_int25_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_int25_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_int25_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_int25_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_int25_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_int25_min")); 
               	
                    client.setScannerParameter("sym_int25_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_int25_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_int25_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_int25_max")); 
               	
                    client.setScannerParameter("sym_int25_max",newValue.toString()); 
                   return true;
               }

           });

	     findPreference("sym_int25_check_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_int25_check_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_int25_check_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_int25_check_enable",val);
                   return true;
               }

           });

		   findPreference("sym_int25_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_int25_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_int25_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_int25_check_transmit_enable",val);
                   return true;
               }

           });


              ////  

			  
	   
      
       findPreference("sym_koreapost_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_koreapost_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_koreapost_min")); 
               	
                    client.setScannerParameter("sym_koreapost_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_koreapost_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_koreapost_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_koreapost_max")); 
               	
                    client.setScannerParameter("sym_koreapost_min",newValue.toString()); 
                   return true;
               }

           });

	   ////

	      CheckBoxPreference matrix25Check = (CheckBoxPreference) findPreference("sym_matrix25_enable");
         
        
	      if(client.getScannerParameter("sym_matrix25_enable").equals("1"))
	   	     matrix25Check.setChecked(true);
	      else 
	   	     matrix25Check.setChecked(false);

		   matrix25Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_matrix25_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_matrix25_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_matrix25_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_matrix25_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_matrix25_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_matrix25_min")); 
               	
                    client.setScannerParameter("sym_matrix25_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_matrix25_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_matrix25_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_matrix25_max")); 
               	
                    client.setScannerParameter("sym_matrix25_max",newValue.toString()); 
                   return true;
               }

           });

      
	   ////
           CheckBoxPreference maxicodeCheck = (CheckBoxPreference) findPreference("sym_maxicode_enable");
         
        
	      if(client.getScannerParameter("sym_maxicode_enable").equals("1"))
	   	     maxicodeCheck.setChecked(true);
	      else 
	   	     maxicodeCheck.setChecked(false);
		  
		   maxicodeCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_maxicode_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_maxicode_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_maxicode_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_maxicode_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_maxicode_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_maxicode_min")); 
               	
                    client.setScannerParameter("sym_maxicode_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_maxicode_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_maxicode_min max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_maxicode_min")); 
               	
                    client.setScannerParameter("sym_maxicode_max",newValue.toString()); 
                   return true;
               }

           });

        ////
           CheckBoxPreference micropdfCheck = (CheckBoxPreference) findPreference("sym_micropdf_enable");
         
        
	      if(client.getScannerParameter("sym_micropdf_enable").equals("1"))
	   	     micropdfCheck.setChecked(true);
	      else 
	   	     micropdfCheck.setChecked(false);
		

		   micropdfCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_micropdf_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_micropdf_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_micropdf_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_micropdf_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_micropdf_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_micropdf_min")); 
               	
                    client.setScannerParameter("sym_micropdf_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_micropdf_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_micropdf_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_micropdf_max")); 
               	
                    client.setScannerParameter("sym_micropdf_max",newValue.toString()); 
                   return true;
               }

           });


	   ////
	      CheckBoxPreference msiCheck = (CheckBoxPreference) findPreference("sym_msi_enable");
         
        
	      if(client.getScannerParameter("sym_msi_enable").equals("1"))
	   	     msiCheck.setChecked(true);
	      else 
	   	     msiCheck.setChecked(false);

		   msiCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_msi_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_msi_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_msi_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_msi_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_msi_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_msi_min")); 
               	
                    client.setScannerParameter("sym_msi_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_msi_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_msi_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_msi_max")); 
               	
                    client.setScannerParameter("sym_msi_max",newValue.toString()); 
                   return true;
               }

           });

	     findPreference("sym_msi_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_msi_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_msi_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_msi_check_transmit_enable",val);
                   return true;
               }

           });


		   ////
		    CheckBoxPreference pdf417Check = (CheckBoxPreference) findPreference("sym_pdf417_enable");
         
        
	      if(client.getScannerParameter("sym_pdf417_enable").equals("1"))
	   	     pdf417Check.setChecked(true);
	      else 
	   	     pdf417Check.setChecked(false);

		   pdf417Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_pdf417_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_pdf417_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_pdf417_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_pdf417_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_pdf417_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_pdf417_min")); 
               	
                    client.setScannerParameter("sym_pdf417_min",newValue.toString());
                   return true;
               }

           });

	   findPreference("sym_pdf417_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_pdf417_max min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_pdf417_max")); 
               	
                    client.setScannerParameter("sym_pdf417_max",newValue.toString());
                   return true;
               }

           });

	     ////

		  findPreference("sym_postnet_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_postnet_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_postnet_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_postnet_check_transmit_enable",val);
                   return true;
               }

           });

        ////
		   CheckBoxPreference microqrCheck = (CheckBoxPreference) findPreference("sym_microqr_enable");
         
           Log.d(TAG,"sym_microqr_enable value:= "+client.getScannerParameter("sym_microqr_enable"));
	      if(client.getScannerParameter("sym_microqr_enable").equals("1"))
	   	     microqrCheck.setChecked(true);
	      else 
	   	     microqrCheck.setChecked(false);

		   microqrCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_microqr_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_microqr_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_microqr_enable",val);
                   return true;
               }

           });


		  ////
		   CheckBoxPreference qrCheck = (CheckBoxPreference) findPreference("sym_qr_enable");
         
           Log.d(TAG,"sym_qr_enable value:= "+client.getScannerParameter("sym_qr_enable"));
	      if(client.getScannerParameter("sym_qr_enable").equals("1"))
	   	     qrCheck.setChecked(true);
	      else 
	   	     qrCheck.setChecked(false);

		   qrCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_qr_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_qr_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_qr_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_qr_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_qr_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_qr_min")); 
               	
                    client.setScannerParameter("sym_qr_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_qr_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_qr_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_qr_max")); 
               	
                    client.setScannerParameter("sym_qr_max",newValue.toString()); 
                   return true;
               }

           });

	     ////
	      CheckBoxPreference rssCheck = (CheckBoxPreference) findPreference("sym_rss_rss_enable");
         
          Log.d(TAG,"sym_rss_rss_enable value:= "+client.getScannerParameter("sym_rss_rss_enable"));
	      if(client.getScannerParameter("sym_rss_rss_enable").equals("1"))
	   	     rssCheck.setChecked(true);
	      else 
	   	     rssCheck.setChecked(false);


		   rssCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_rss_rss_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_rss_rss_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_rss_rss_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_rss_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_rss_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_rss_min")); 
               	
                    client.setScannerParameter("sym_rss_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_rss_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_rss_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_rss_max")); 
               	
                    client.setScannerParameter("sym_rss_max",newValue.toString()); 
                   return true;
               }

           });

	    findPreference("sym_rss_rsl_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_rss_rsl_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_rss_rsl_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_rss_rsl_enable",val);
                   return true;
               }

           });

		 findPreference("sym_rss_rse_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_rss_rse_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_rss_rse_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_rss_rse_enable",val);
                   return true;
               }

           });


		 ////
           CheckBoxPreference strt25Check = (CheckBoxPreference) findPreference("sym_strt25_enable");
         
          
	      if(client.getScannerParameter("sym_strt25_enable").equals("1"))
	   	     strt25Check.setChecked(true);
	      else 
	   	     strt25Check.setChecked(false);
		 

		   strt25Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_strt25_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_strt25_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_strt25_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_strt25_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_strt25_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_strt25_min")); 
               	
                    client.setScannerParameter("sym_strt25_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_strt25_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_strt25_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_strt25_max")); 
               	
                    client.setScannerParameter("sym_strt25_max",newValue.toString()); 
                   return true;
               }

           });

	    ////
           CheckBoxPreference telepenCheck = (CheckBoxPreference) findPreference("sym_telepen_enable");
         
          
	      if(client.getScannerParameter("sym_telepen_enable").equals("1"))
	   	     telepenCheck.setChecked(true);
	      else 
	   	     telepenCheck.setChecked(false);

 
		   telepenCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_telepen_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_telepen_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_telepen_enable",val);
                   return true;
               }

           });
	   
      
       findPreference("sym_telepen_min").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_telepen_min min setting to " + newValue.toString()+" old min:= "+client.getScannerParameter("sym_telepen_min")); 
               	
                    client.setScannerParameter("sym_telepen_min",newValue.toString());
                   return true;
               }

           });
       
     
       findPreference("sym_telepen_max").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_telepen_max max setting to " + newValue.toString()+" old max:= "+client.getScannerParameter("sym_telepen_max")); 
               	
                    client.setScannerParameter("sym_telepen_max",newValue.toString()); 
                   return true;
               }

           });

	    findPreference("sym_telepen_telepen_old_style").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_telepen_telepen_old_style enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_telepen_telepen_old_style")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_telepen_telepen_old_style",val);
                   return true;
               }

           });

	    ////
	       CheckBoxPreference upcaCheck = (CheckBoxPreference) findPreference("sym_upca_enable");
         
          
	      if(client.getScannerParameter("sym_upca_enable").equals("1"))
	   	     upcaCheck.setChecked(true);
	      else 
	   	     upcaCheck.setChecked(false);

		   upcaCheck.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upca_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_check_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upca_sys_num_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_sys_num_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_sys_num_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_sys_num_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upca_addenda_separator_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_addenda_separator_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_addenda_separator_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_addenda_separator_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upca_2_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_2_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_2_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_2_digit_addenda_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upca_5_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_5_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_5_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_5_digit_addenda_enable",val);
                   return true;
               }

           });

		    findPreference("sym_upca_addenda_required_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_upca_addenda_required_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upca_addenda_required_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upca_addenda_required_enable",val);
                   return true;
               }

           });

			findPreference("sym_translate_upca_to_ean13_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	Log.d(TAG, "sym_translate_upca_to_ean13_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_translate_upca_to_ean13_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_translate_upca_to_ean13_enable",val);
                   return true;
               }

           });

			 ////
			  CheckBoxPreference upce0Check = (CheckBoxPreference) findPreference("sym_upce0_enable");
         
          
	      if(client.getScannerParameter("sym_upce0_enable").equals("1"))
	   	     upce0Check.setChecked(true);
	      else 
	   	     upce0Check.setChecked(false);

		   upce0Check.setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                	Log.d(TAG, "sym_upce0_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upce0_upce0_expanded_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                 Log.d(TAG, "sym_upce0_upce0_expanded_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_upce0_expanded_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_upce0_expanded_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upce0_check_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	  Log.d(TAG, "sym_upce0_check_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_check_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_check_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upce0_sys_num_transmit_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	  Log.d(TAG, "sym_upce0_sys_num_transmit_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_sys_num_transmit_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_sys_num_transmit_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upce0_addenda_separator_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                 Log.d(TAG, "sym_upce0_addenda_separator_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_addenda_separator_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_addenda_separator_enable",val);
                   return true;
               }

           });

		   findPreference("sym_upce0_2_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	  Log.d(TAG, "sym_upce0_2_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_2_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_2_digit_addenda_enable",val);
                   return true;
               }

           });

		    findPreference("sym_upce0_5_digit_addenda_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
                  Log.d(TAG, "sym_upce0_5_digit_addenda_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_5_digit_addenda_enable")); 

				  String val  = newValue.toString().equals("true")? "1":"0";
                  client.setScannerParameter("sym_upce0_5_digit_addenda_enable",val);
                   return true;
               }

           });

			findPreference("sym_upce0_addenda_required_enable").setOnPreferenceChangeListener(
               new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {	
               	   Log.d(TAG, "sym_upce0_addenda_required_enable enable setting to " + newValue.toString()+" old value:="+client.getScannerParameter("sym_upce0_addenda_required_enable")); 

				   String val  = newValue.toString().equals("true")? "1":"0";
                   client.setScannerParameter("sym_upce0_addenda_required_enable",val);
                   return true;
               }

           });
	   

	   
    }  

/*
     @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d(TAG, "onPreferenceClick----->"+preference.getKey());
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "onPreferenceChange----->"+preference.getKey());

		if(preference.getKey().indexOf("enable")!= -1){
	           
				String val  = newValue.toString().equals("true")? "1":"0";
                client.setScannerParameter(preference.getKey(),val); 
		 }else if(preference.getKey().indexOf("min")!= -1 || preference.getKey().indexOf("max")!= -1){
            client.setScannerParameter(preference.getKey(),newValue.toString());
        }else {
            Log.d(TAG, " isChecked = " + String.valueOf(newValue));
        }
        return true;
    }
    */

    /* Flawless work around for submenu issue with 2.3 */
    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
        Log.d(TAG, "onPreferenceTreeClick----->"+preference.getKey());
    	super.onPreferenceTreeClick(preferenceScreen, preference);
    	if (preference!=null)
	    	if (preference instanceof PreferenceScreen)
	        	if (((PreferenceScreen)preference).getDialog()!=null)
	        		((PreferenceScreen)preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());


    	    
    	return false;
    }
    

 
}
