package myfirstapp.example.com.smsmessaging;

import android.app.Activity;
import android.widget.Button;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import myfirstapp.example.com.sms.*;

/**
 * Created by Ephemerality on 11/10/2014.

public class PhoneNumberTest extends ActivityInstrumentationTestCase2(){

    public void testPhoneNumber () {


        String phoneNumber = "4153200859";
        assertEquals(isValidPhoneNumber(phoneNumber, getApplicationContext()));


    }
}
 */