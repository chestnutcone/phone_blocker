package com.chestnutcone.phoneblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AdvanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);

        ImageButton saveButton = (ImageButton) findViewById(R.id.save_regex_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText regexInput = (EditText) findViewById(R.id.regex_input);
                String regexString = regexInput.getText().toString();

                if (regexString != "") {
                    try {
                        Pattern.compile(regexString);
                        PhoneRegex phoneRegex = new PhoneRegex();

                        String type;
                        RadioButton rejectRadio = (RadioButton) findViewById(R.id.advanceActivity_radio_reject);
                        if (rejectRadio.isChecked()) {
                            type = PhoneRegex.typeReject;
                        } else {
                            type = PhoneRegex.typeExcept;
                        }

                        phoneRegex.addRegexEntry(AdvanceActivity.this, regexString, type);
                        // back to main activity
                        //startActivity(new Intent(AdvanceActivity.this, MainActivity.class));
                        finish();
                    } catch (PatternSyntaxException exception){
                        Toast toast = Toast.makeText(
                                getApplicationContext(), "Invalid regex", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(
                            getApplicationContext(), "Not saving empty", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

    }
}
