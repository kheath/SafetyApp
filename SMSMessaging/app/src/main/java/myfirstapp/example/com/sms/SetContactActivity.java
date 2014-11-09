package myfirstapp.example.com.sms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import myfirstapp.example.com.sms.R;

public class SetContactActivity extends Activity implements View.OnClickListener {

    Button buttonContact;
    TextView textViewContact;
    private static final String PREFS = "prefs";
    private static final String PREF_CONTACT_NUMBER = "number";
    SharedPreferences mSharedPreferences;
    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact);

        // Access the Button defined in layout XML
        // and listen for it here
        buttonContact = (Button) findViewById(R.id.button_contact);
        buttonContact.setOnClickListener(this);

        // Change the TextView to the set number, if there is one
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String number = mSharedPreferences.getString(PREF_CONTACT_NUMBER, "");
        if (number.length() > 0) {
            textViewContact = (TextView) findViewById(R.id.textview_contact);
            textViewContact.setText(number);
        }
    }

    @Override
    public void onClick(View view) {
        // Start an activity for the user to pick a phone number from contacts
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                number = number.replaceAll("[^0-9]", "");
                // Access the device's key-value storage
                mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                // Put the contact's phone number in storage
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(PREF_CONTACT_NUMBER, number);
                e.commit();
                // Update the contact TextView with the number
                textViewContact = (TextView) findViewById(R.id.textview_contact);
                textViewContact.setText(number);
            }
        }
    }
}
