package com.safetyapp.sms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import myfirstapp.example.com.sms.R;

public class SetEmergencyActivity extends Activity {

    Button buttonEdit;
    TextView textViewENum;
    EditText editENum;
    SharedPreferences mSharedPreferences;
    private static final String PREFS = "prefs";
    private static final String PREF_E_CONTACT = "econtact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_emergency_contact);

        textViewENum = (TextView) findViewById(R.id.textview_message);
        editENum = (EditText) findViewById(R.id.edittext_message);

        // Get stored message
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        final String econtact = mSharedPreferences.getString(PREF_E_CONTACT, "");

        // Set TextView if there's a message in storage
        if (!econtact.isEmpty()) {
            textViewENum.setText(econtact);
        }

        buttonEdit = (Button) findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(new View.OnClickListener()
                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 String input = editENum.getText().toString();
                                                 textViewENum.setText(input);
                                                 SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
                                                 SharedPreferences.Editor e = prefs.edit();
                                                 e.putString(PREF_E_CONTACT, input);
                                                 e.apply();
                                             }
                                         }
        );

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_set_message, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
