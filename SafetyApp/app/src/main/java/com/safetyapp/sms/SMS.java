

package com.safetyapp.sms;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myfirstapp.example.com.sms.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SMS extends Activity
{
	
	
    Button btnSendSMS;	// Initialize button to send an alert to a friend
    Button btnICE;	// Initialize button to call and emergency number
    Button btnItsOkay;	// Initialize button to send an all-clear text to a friend
    String txtPhoneNo;	// Initialize phone number
    ArrayList<String> txtPhoneNos = new ArrayList<String>();	// Initialize phone number
    String txtMessage;	// Initialize alert message
    String kkMessage;	// Initialize all-clear message
    String eNumber;	// Initialize emergency number
    GPSTracker gps;	// Initialize gps
    double latitude;
    double longitude;
    
    
    
	// Initialize this to find out if a phone is in use.
    TelephonyManager tm;

    private static final String PREFS = "prefs";
    private static final String PREF_CONTACT_NUMBERS = "numbers";
    private static final String PREF_MESSAGE = "message";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnICE = (Button) findViewById(R.id.btnICE);
        btnItsOkay = (Button) findViewById(R.id.btnItsOkay);
        btnItsOkay.setVisibility(View.GONE);
        
        // Put in the "emergency" phone number

        eNumber = "";
        
        kkMessage = "Everything is fine now. No need to worry.";
        
        
        
        // create GPS object
        gps = new GPSTracker(SMS.this);
        
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
        // Code for the big button - this sends a text
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                        	  
                                        	  System.out.println("Arraylist is: " + txtPhoneNos);
                                              System.out.println(txtPhoneNos.isEmpty());
                                        	  
                                              if (isAirplaneModeOn(getApplicationContext())) {
                                                  Toast toast = Toast.makeText(getBaseContext(),
                                                          "Turn off Airplane Mode!",
                                                          Toast.LENGTH_SHORT);
                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                  toast.show();
                                              } else if (txtPhoneNos.isEmpty()) {
                                            	  Toast toast = Toast.makeText(getBaseContext(),
                                                          "Please add a contact!",
                                                          Toast.LENGTH_SHORT);
                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                  toast.show();
                                                  
                                              } else {

                                                  // check if GPS enabled
                                                  if (gps.canGetLocation()) {

                                                      latitude = gps.getLatitude();
                                                      longitude = gps.getLongitude();




                                                  txtMessage = txtMessage + "\n If you don't hear from me, \n my location is - \n http://maps.google.com/maps?daddr=" + latitude + "," + longitude;

                                                  for (int i = 0; i < txtPhoneNos.size(); i++) {
                                                      if (isValidPhoneNumber(txtPhoneNos.get(i), getApplicationContext())) {
                                                          if (txtPhoneNos.get(i).length() == 10) {
                                                              txtPhoneNos.set(i,"1" + txtPhoneNos.get(i));
                                                          }

                                                          sendSMS(txtPhoneNos.get(i), txtMessage);
                                                          btnItsOkay.setVisibility(View.VISIBLE);
                                                      } else if (!isValidPhoneNumber(txtPhoneNo, getApplicationContext())) {
                                                          Toast toast = Toast.makeText(getBaseContext(),
                                                                  "One or more phone numbers is not valid.",
                                                                  Toast.LENGTH_SHORT);
                                                          toast.setGravity(Gravity.CENTER, 0, 0);
                                                          toast.show();
                                                          break;
                                                      }
                                                  }
                                                  } else {
                                                      // can't get location
                                                      // GPS or Network is not enabled
                                                      // Ask user to enable GPS/network in settings
                                                      gps.showSettingsAlert();
                                                  }
                                              }
                                          }
                                      });
        
        
        // Call da police!  --> Works now!
        
        btnICE.setOnClickListener(new View.OnClickListener()
        {
            @Override
			public void onClick(View v)
            {
            	if(isPhone(tm))
            	{
//            		if (isValidPhoneNumber(eNumber, getApplicationContext()))
//            		{

            			Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+eNumber));
                        try {
                            startActivity(callIntent);
                            finish();
                            Log.i("Finished making a call...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(SMS.this,
                                    "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                        }
//            		}
            	}
            	else
            	{
            		 
            		Toast toast = Toast.makeText(getBaseContext(),
            				"This feature is only available on phones",
            				Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER, 0, 0);
            		toast.show();  
            	}
            }
        });
        
        // It's all cool, brah
        
        btnItsOkay.setOnClickListener(new View.OnClickListener()
        {
        	
            @Override
			public void onClick(View v)
            {
            	if (txtPhoneNos.isEmpty()) {
              	  Toast toast = Toast.makeText(getBaseContext(),
                            "Please add a contact!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    
                } else{
	                for (int i = 0; i < txtPhoneNos.size(); i++) {
	                    if (isValidPhoneNumber(txtPhoneNos.get(i), getApplicationContext())) {
	                        if (txtPhoneNos.get(i).length() == 10) {
	                            txtPhoneNos.set(i,"1" + txtPhoneNos.get(i));
	                            System.out.println("Assuming there should be a 1 in front. \nPhone number: " + txtPhoneNo);
	
	                        }
	                        sendSMS(txtPhoneNos.get(i), kkMessage);
	                        btnItsOkay.setVisibility(View.GONE);
	                    } else if (!isValidPhoneNumber(txtPhoneNos.get(i), getApplicationContext())) {
	//                	System.out.println(txtPhoneNo + " is not a valid phone number!");
	                    } else {
	                        Toast toast = Toast.makeText(getBaseContext(),
	                                "Please enter a valid phone number.",
	                                Toast.LENGTH_SHORT);
	                        toast.setGravity(Gravity.CENTER, 0, 0);
	                        toast.show();
	                    }
	                }
                }
            }
        });
        
    }

    // Complex method to send and post status of an SMS message.
    // Shows toast messages for the status of the message.
    // Code taken from here: http://mobiforge.com/design-development/sms-messaging-android
    private void sendSMS(String phoneNumber, String message)
    {        
    	
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	Toast toast = Toast.makeText(getBaseContext(),
                                "SMS sent",
                                Toast.LENGTH_SHORT);
                    	toast.setGravity(Gravity.CENTER, 0, 0);
                    	toast.show();
                        
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    	Toast toast1 = Toast.makeText(getBaseContext(),
                                "Generic failure",
                                Toast.LENGTH_SHORT);
                    	toast1.setGravity(Gravity.CENTER, 0, 0);
                    	toast1.show();
                    	break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                    	Toast toast2 = Toast.makeText(getBaseContext(),
                                "No service",
                                Toast.LENGTH_SHORT);
                    	toast2.setGravity(Gravity.CENTER, 0, 0);
                    	toast2.show();                    
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    	Toast toast3 = Toast.makeText(getBaseContext(),
                                "Null PDU",
                                Toast.LENGTH_SHORT);
                    	toast3.setGravity(Gravity.CENTER, 0, 0);
                    	toast3.show();
                    	break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    	Toast toast4 = Toast.makeText(getBaseContext(),
                                "Radio off - turn off airplane mode",
                                Toast.LENGTH_SHORT);
                    	toast4.setGravity(Gravity.CENTER, 0, 0);
                    	toast4.show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	Toast toast = Toast.makeText(getBaseContext(),
                                "SMS delivered",
                                Toast.LENGTH_SHORT);
                    	toast.setGravity(Gravity.CENTER, 0, 0);
                    	toast.show();
                        break;
                    case Activity.RESULT_CANCELED:
                    	Toast toast1 = Toast.makeText(getBaseContext(),
                                "SMS not delivered",
                                Toast.LENGTH_SHORT);
                    	toast1.setGravity(Gravity.CENTER, 0, 0);
                    	toast1.show();
                        break;                        
                }
                // Remove this receiver so we don't constantly get updates
                SMS.this.unregisterReceiver(this);
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
    
    // Checks to see if a phone number is valid (11 or 10 digits).  If not, it displays a toast message and returns false
    public static boolean isValidPhoneNumber(String phoneNum, Context context)
    {
    	if (phoneNum.length() < 1) {
        	return false;
        }
    	
    	Pattern pattern = Pattern.compile("[0-9]{10,11}");
        Matcher matcher = pattern.matcher(phoneNum);
   
        if (matcher.matches()) {
      	  return true;
        }
        else
        {
      	  Toast toast = Toast.makeText(context,
      			  "Please provide a valid phone number",
      			  Toast.LENGTH_SHORT);
      	  toast.setGravity(Gravity.CENTER, 0, 0);
      	  toast.show();
      	  return false;
        }
    }
    
    // Checks to see if this app is running on a phone
    public static boolean isPhone(TelephonyManager device)
    {
    	
        if(device.getPhoneType() == 0)
        {
        	return false;
        }
        else
        {
        	return true;
        }
    }
    
    /**
    * Gets the state of Airplane Mode.
    * 
    * @param context
    * @return true if enabled.
    */
    
    public static boolean isAirplaneModeOn(Context context) {        
        return Settings.Global.getInt(context.getContentResolver(), 
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
       
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.action_set_contact) {
            // Starts the Set Contact activity on top of the current activity
            Intent intent = new Intent(this, SetContactActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_set_message) {
            // Starts the Settings activity on top of the current activity
            Intent intent = new Intent(this, SetMessageActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        Set<String> newPhoneNos = preferences.getStringSet(PREF_CONTACT_NUMBERS, new HashSet<String>());
        txtPhoneNos.clear();
        if(!newPhoneNos.isEmpty()) {
        	txtPhoneNos.addAll(newPhoneNos); // newPhoneNos.toArray(txtPhoneNos);
        }
        

        String newMessage = preferences.getString(PREF_MESSAGE, "Hi! I'm in a sketchy situation, and I'm somewhat concerned.");
        txtMessage = newMessage;
    }
}