package com.example.phoneblocker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.ContentValues.TAG;


public class PhoneRegex {
    public void addRegexEntry(Context ctx, String entry) {
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, "phone.txt");
        try {
            FileWriter writer = new FileWriter(myFile, true);
            writer.append(entry);
            writer.append(",");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast toast = Toast.makeText(
                    ctx, "Fail to save phone pattern", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void deleteAllEntries(Context ctx) {
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, "phone.txt");
        try {
            FileWriter writer = new FileWriter(myFile);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast toast = Toast.makeText(
                    ctx, "Fail to delete all phone pattern", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public String[] getRegexEntries(Context ctx){
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, "phone.txt");

        StringBuilder phoneText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            String line;
            while ((line = br.readLine()) != null ) {
                phoneText.append(line);
            }
            br.close();
            Log.d(TAG, "getRegexEntries: "+phoneText);
            return phoneText.toString().split(",");
        } catch (IOException e) {
            Toast toast = Toast.makeText(
                    ctx, "Fail to get phone pattern", Toast.LENGTH_LONG);
            toast.show();
        }
        return new String[0];
    }
}
