

package myfirstapp.example.com.sms;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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
    Button btnOhShit;	// Initialize button to call and emergency number
    Button btnItsOkay;	// Initialize button to send an all-clear text to a friend
    String[] txtPhoneNos = new String[1];	// Initialize phone number
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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnOhShit = (Button) findViewById(R.id.btnOhShit);
        btnItsOkay = (Button) findViewById(R.id.btnItsOkay);
        
        // Put in the "emergency" phone number
        eNumber = "4153200859";
        
        kkMessage = "Yarr matey, the seas be calm.";
        
        // create GPS object
        gps = new GPSTracker(SMS.this);
        
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
        // Code for the big blue button - this sends a text
        btnSendSMS.setOnClickListener(new View.OnClickListener()
        {
            @Override
			public void onClick(View v)
            {
 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                     

                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                
                //if (txtMessage.length()<1)
                {
                	txtMessage = "Yarr matey, I be in a bit of a pickle.";
                }
                txtMessage = txtMessage + "\n Me location is - \nLat: " + latitude + "\nLong: " + longitude;

                for (int i = 0; i < txtPhoneNos.length; i++) {
                    if (isValidPhoneNumber(txtPhoneNos[i], getApplicationContext())) {
                        if (txtPhoneNos[i].length() == 10) {
                            txtPhoneNos[i] = "1" + txtPhoneNos[i];
//                		System.out.println("Assuming there should be a 1 in front. \nPhone number: "+txtPhoneNos);

                        }

                        sendSMS(txtPhoneNos[i], txtMessage, tm);
                    //} else if (!isValidPhoneNumber(txtPhoneNos, getApplicationContext())) {
//                	System.out.println(txtPhoneNos + " is not a valid phone number!");
                    } else {
                        Toast toast = Toast.makeText(getBaseContext(),
                                "One or more phone numbers is not valid.",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                }
            }
        });
        
        // Call da police!
        
        btnOhShit.setOnClickListener(new View.OnClickListener()
        {
            @Override
			public void onClick(View v)
            {
            	if(isPhone(tm))
            	{
            		if (isValidPhoneNumber(eNumber, getApplicationContext()))
            		{
            			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(eNumber)); 
            			startActivity(callIntent);
            		}
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
                for (int i = 0; i < txtPhoneNos.length; i++) {
                    if (isValidPhoneNumber(txtPhoneNos[i], getApplicationContext())) {
                        if (txtPhoneNos[i].length() == 10) {
                            txtPhoneNos[i] = "1" + txtPhoneNos[i];
                            System.out.println("Assuming there should be a 1 in front. \nPhone number: " + txtPhoneNos[i]);

                        }

                        sendSMS(txtPhoneNos[i], kkMessage, tm);
                    }
                    //else if (!isValidPhoneNumber(txtPhoneNos, getApplicationContext()))
                    //{
//                	System.out.println(txtPhoneNos + " is not a valid phone number!");
                    //}
                    else {
                        Toast toast = Toast.makeText(getBaseContext(),
                                "One or more phone numbers is not valid.",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                }
            }
        });
        
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message, TelephonyManager device)
    {
    	if(isPhone(device))
    	{
    		SmsManager smsManager = SmsManager.getDefault();
    		smsManager.sendTextMessage(phoneNumber, null, message, null, null);

    		Toast toast = Toast.makeText(getBaseContext(),
    				"Message Sent.",
    				Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
    	}
    	else
    	{
    		Toast toast = Toast.makeText(getBaseContext(),
    				"You aren't using a phone, brah",
    				Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
    	}

    }
    
    // Checks to see if a phone number is valid (11 or 10 digits).  If not, it displays a toast message and returns false
    public static boolean isValidPhoneNumber(String phoneNum, Context context)
    {
   
        Pattern pattern = Pattern.compile("[0-9]{10,11}");
        Matcher matcher = pattern.matcher(phoneNum);
   
        if (matcher.matches()) {
      	  System.out.println("Phone Number is Valid");
      	  return true;
        }
        else
        {
      	  System.out.println("Not a valid phone number: " + phoneNum);
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
        // TODO
        /*} else if (item.getItemId() == R.id.action_set_message) {
            // Starts the Settings activity on top of the current activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;*/
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        Set<String> newPhoneNos = preferences.getStringSet(PREF_CONTACT_NUMBERS, new HashSet<String>());
        txtPhoneNos = newPhoneNos.toArray(txtPhoneNos);
    }
}