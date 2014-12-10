package com.safetyapp.sms.tests;


import com.safetyapp.sms.SMS;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;
import android.telephony.TelephonyManager;


public class SMSTest2 extends ActivityInstrumentationTestCase2<SMS>{
    TelephonyManager tm;
    Context c;

    public SMSTest2() {
        super (SMS.class);
        }


    public void testPhoneNumber() throws Exception {
        SMS activity = getActivity();
        tm = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        Assert.assertEquals(true, SMS.isPhone(tm));


    }

}