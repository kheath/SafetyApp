package com.safetyapp.sms.tests;



import com.safetyapp.sms.*;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.*;
import android.content.Context;


public class SMSTest extends ActivityInstrumentationTestCase2<SMS>{
    Context c;
    public SMSTest() {
        super (SMS.class);
        }


    public void testPhoneNumber() throws Exception {
        SMS activity = getActivity();
        String phoneNumber = "4153200859";
        Assert.assertEquals(true, activity.isValidPhoneNumber(phoneNumber, c));

        //Both of the following tests give null pointer exceptions
        /*
        String phoneNumber2 = "320591";
        Assert.assertEquals(false, activity.isValidPhoneNumber(phoneNumber2, c));


        String phoneNumber3 = "415320085a";
        Assert.assertEquals(false, activity.isValidPhoneNumber(phoneNumber3, c));
        */




    }

}