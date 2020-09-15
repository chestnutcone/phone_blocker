package com.example.phoneblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Spinner spinner = (Spinner) findViewById(R.id.add_type_spinner);
        String[] types = new String[]{"Starts with", "Contains", "Ends with"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, types);
        // Drop down layout sytle - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // bind save button to onclick listener
        ImageButton saveBtn = (ImageButton) findViewById(R.id.save_entry_button);
        saveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // get number value
                EditText phoneNumberEditText = (EditText) findViewById(R.id.blockNumber);
                String phoneNumber = phoneNumberEditText.getText().toString();

                if (phoneNumber.equals("")) {
                    Toast toast = Toast.makeText(
                            getApplicationContext(), "Empty number. Not saving", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                // get dropdown type
                Spinner spinner = (Spinner) findViewById(R.id.add_type_spinner);
                String selectedType = spinner.getSelectedItem().toString();
                PhoneRegex phoneRegex = new PhoneRegex();
                String regex_pattern = phoneRegex.constructRegex(selectedType, phoneNumber);
                phoneRegex.addRegexEntry(AddActivity.this, regex_pattern);

                Toast toast = Toast.makeText(
                        getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
                toast.show();

                // redirect back to first activity
                startActivity(new Intent(AddActivity.this, MainActivity.class));

            }
        });

        ImageButton advanceBtn = (ImageButton) findViewById(R.id.advance_button);
        advanceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // goes into advance setting which allows users to just enter regex
                startActivity(new Intent(AddActivity.this, AdvanceActivity.class));
            }
        });
    }
}