

package myfirstapp.example.com.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class SMS extends Activity
{
    Button btnSendSMS;
    String txtPhoneNo;
    String txtMessage;
    GPSTracker gps;
    double latitude;
    double longitude;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
//        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
//        txtMessage = (EditText) findViewById(R.id.txtMessage);
     // create GPS object
        gps = new GPSTracker(SMS.this);
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        btnSendSMS.setOnClickListener(new View.OnClickListener()
        {
            @Override
			public void onClick(View v)
            {
 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                     
                    // \n is for new line
                        
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            	
//                String phoneNo = txtPhoneNo.getText().toString();
//                String message = txtMessage.getText().toString();
                
                if (txtMessage.length()<1)
                {
                	txtMessage = "Yarr matey, I be in a bit of a pickle.";
                }
//                txtMessage.setText(message);
                txtMessage = txtMessage + "\n Me location is - \nLat: " + latitude + "\nLong: " + longitude;
                
                if (isValidPhoneNumber(txtPhoneNo))
                {
                	if (txtPhoneNo.length() == 10)
                	{
                		txtPhoneNo = "1"+txtPhoneNo;
                		System.out.println("Assuming there should be a 1 in front. \nPhone number: "+txtPhoneNo);
                    	sendSMS(txtPhoneNo, txtMessage);
                	}
                	else
                	{
                		System.out.println("Phone number: "+txtPhoneNo);
                		sendSMS(txtPhoneNo, txtMessage);
                	}
                }
                else if (!isValidPhoneNumber(txtPhoneNo))
                {
                	System.out.println(txtPhoneNo + " is not a valid phone number!");
                }
                else
                {
                	Toast toast = Toast.makeText(getBaseContext(),
                            "Please enter both a valid phone number and message.",
                            Toast.LENGTH_SHORT);
                	toast.setGravity(Gravity.CENTER, 0, 0);
                	toast.show();
                }
//                	Toast.makeText(getBaseContext(),
//                            "Please enter both a valid phone number and message.",
//                            Toast.LENGTH_SHORT).setGravity(Gravity.CENTER, 0, 0).show();
            }
        });
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {
    	SmsManager smsManager = SmsManager.getDefault();
    	smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    	
    	Toast toast = Toast.makeText(getBaseContext(),
                "Message Sent.",
                Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.show();
    	
//        PendingIntent pi = PendingIntent.getActivity(this, 0,
//                new Intent(this, SMS.class), 0);
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
    
    public static boolean isValidPhoneNumber(String phoneNum)
    {
    	boolean isValid = false;
   
        Pattern pattern = Pattern.compile("[0-9]{10,11}");
        Matcher matcher = pattern.matcher(phoneNum);
   
        if (matcher.matches()) {
      	  System.out.println("Phone Number is Valid");
      	  isValid = true;
        }
        else
        {
      	  System.out.println("Not a valid phone number!");
        }
        return isValid;
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
        if (item.getItemId() == R.id.action_settings) {
            // Starts the Settings activity on top of the current activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String newPhoneNo = preferences.getString("pref_recipientNumber", "");
        String newMessage = preferences.getString("pref_message", "");
        txtPhoneNo = newPhoneNo;
        txtMessage = newMessage;
    }
    
}