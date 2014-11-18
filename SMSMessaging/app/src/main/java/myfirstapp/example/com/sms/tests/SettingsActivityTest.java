package myfirstapp.example.com.sms.tests;


import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import myfirstapp.example.com.sms.R;
import myfirstapp.example.com.sms.SMS;
import myfirstapp.example.com.sms.SettingsActivity;


public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity>{
    Context c;
    public SettingsActivityTest() {
        super (SettingsActivity.class);
        }


    public void testPhoneNumber() throws Exception {
        SettingsActivity activity = getActivity();
        /* Trying to figure out how to load preferences from resources, apparently this method
        which is used in our code is deprecated and we should "instantiate PreferenceFragment
        objects to load your preferences from a resource file. See the sample code here: PreferenceActivity"

        http://stackoverflow.com/questions/6822319/what-to-use-instead-of-addpreferencesfromresource-in-a-preferenceactivity

        addPreferencesFromResource(R.xml.preferences);
        */







    }

}