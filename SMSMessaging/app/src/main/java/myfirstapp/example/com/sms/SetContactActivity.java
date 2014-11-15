package myfirstapp.example.com.sms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SetContactActivity extends Activity {

    Button buttonContact;
    Button buttonClear;
    String newestNumber = "";
    private static final String PREFS = "prefs";
    private static final String PREF_CONTACT_NUMBERS = "numbers";
    SharedPreferences mSharedPreferences;
    ListView listViewContact;
    ArrayAdapter<String> arrayAdapterContact;
    ArrayList<String> contactList = new ArrayList<String>();
    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact);

        // Access the contact ListView
        listViewContact = (ListView) findViewById(R.id.listview_contact);
        // Create an ArrayAdapter for the ListView
        arrayAdapterContact = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                contactList);
        // Set the ListView to use the ArrayAdapter
        listViewContact.setAdapter(arrayAdapterContact);

        // Access the contact button defined in layout XML
        // and listen for it here
        buttonContact = (Button) findViewById(R.id.button_contact);
        buttonContact.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View view) {
                     // Start an activity for the user to pick a phone number from contacts
                     Intent intent = new Intent(Intent.ACTION_PICK);
                     intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                     if (intent.resolveActivity(getPackageManager()) != null) {
                         startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                     }
                 }
             }
        );

        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View view) {
                     // Button clears contact list on press
                     mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                     SharedPreferences.Editor e = mSharedPreferences.edit();
                     e.clear();
                     e.apply();
                     contactList.clear();
                     arrayAdapterContact.notifyDataSetChanged();
                     Toast toast = Toast.makeText(getBaseContext(),
                             "Recipient phone numbers cleared.",
                             Toast.LENGTH_SHORT);
                     toast.setGravity(Gravity.CENTER, 0, 0);
                     toast.show();
                 }
             }
        );

/*        // Change the TextView to the set number, if there is one
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String number = mSharedPreferences.getString(PREF_CONTACT_NUMBERS, "");
        if (number.length() > 0) {
            textViewContact = (TextView) findViewById(R.id.textview_contact);
            textViewContact.setText(number);
        }*/
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
                Set<String> storedNum = mSharedPreferences.getStringSet(PREF_CONTACT_NUMBERS, new HashSet<String>());
                // Put the contact's phone number in the set and save to storage
                storedNum.add(number);
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putStringSet(PREF_CONTACT_NUMBERS, storedNum);
                e.apply();
                newestNumber = number;
                if (newestNumber.length() != 0 ) {
                    System.out.println("hi");
                    contactList.add(newestNumber);
                    arrayAdapterContact.notifyDataSetChanged();
                }
            }
        }
    }
}
