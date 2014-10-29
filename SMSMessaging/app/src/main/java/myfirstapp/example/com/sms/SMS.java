

package myfirstapp.example.com.sms;

import myfirstapp.example.com.sms.GPSTracker;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Gravity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMS extends Activity
{
    Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;
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
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
     // create GPS object
        gps = new GPSTracker(SMS.this);

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
            	
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();
                
                if (message.length()<1)
                {
                	message = "Yarr matey, I be in a bit of a pickle.";
                }
                txtMessage.setText(message);
                message = message + "\n Me location is - \nLat: " + latitude + "\nLong: " + longitude;
                
                if (isValidPhoneNumber(phoneNo))
                {
                	if (phoneNo.length() == 10)
                	{
                		phoneNo = "1"+phoneNo;
                		System.out.println("Assuming there should be a 1 in front. \nPhone number: "+phoneNo);
                    	sendSMS(phoneNo, message);
                	}
                	System.out.println("Phone number: "+phoneNo);
                	sendSMS(phoneNo, message);
                }
                else if (!isValidPhoneNumber(phoneNo))
                {
               		txtPhoneNo.setError("Please enter a valid phone number in international format.");
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
    
}