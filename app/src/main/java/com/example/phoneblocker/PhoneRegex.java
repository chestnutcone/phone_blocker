package com.example.phoneblocker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class PhoneRegex {
    public void addRegexEntry(Context ctx, String entry, Boolean append) {
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, "phone.txt");
        try {
            FileWriter writer = new FileWriter(myFile, append);

            Log.d(TAG, "addRegexEntry: "+entry);
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

    public void addRegexEntry(Context ctx, String entry) {
        addRegexEntry(ctx, entry, true);
    }

    public void deleteAllEntries(Context ctx) {
        addRegexEntry(ctx, "", false);
    }

    public void deleteEntry(Context ctx, Integer position) {
        String[] entries = getRegexEntries(ctx);

        ArrayList<String> newArray = new ArrayList<>();
        for (int i=0; i<entries.length; i++) {
            if (i != position) {
                newArray.add(entries[i]);
            }
        }
        String newValues = String.join(",", newArray);
        addRegexEntry(ctx, newValues, false);
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

    public String constructRegex(String type, String number) {
        String pattern;
        switch (type) {
            case "Starts with":
                pattern = "\\+"+number+"\\d*";
                break;
            case "Contains":
                pattern = "\\+\\d*"+number+"\\d*";
                break;
            case "Ends with":
                pattern = "\\+\\d*"+number;
                break;
            default:
                pattern = "";
        }
        Log.d(TAG, "constructRegex: with pattern"+pattern);
        return pattern;
    }
}
