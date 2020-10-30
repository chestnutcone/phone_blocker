package com.chestnutcone.phoneblocker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class PhoneRegex {
    private static final String fileName = "phone.json";
    static final String typeReject = "reject";
    static final String typeExcept = "except";
    static final String TAG = PhoneRegex.class.getSimpleName();


    void addRegexEntry(Context ctx, String entry, Boolean append, String type) {
        String filePath = ctx.getFilesDir().toString();
        File myFile = new File(filePath, fileName);

        // get entry
        JSONObject obj = getJSONFile(ctx);
        String[] saved = getRegexEntries(ctx, type, obj);
        boolean exist = false;
        // check if entry exist
        if (append) {
            for (int i=0; i<saved.length; i++) {
                if (entry.equals(saved[i])) {
                    exist = true;
                    break;
                }
            }
        }

        if (!exist) {
            String savedString;
            try {
                savedString = obj.get(type).toString();
            } catch (NullPointerException npe) {
                savedString = "";
            }

            String newString;
            if (append) {
                newString = savedString+entry+",";
            } else {
                newString = entry+",";
            }
            obj.put(type, newString);
            writeJSON(obj, myFile);

        } else {
            Toast toast = Toast.makeText(
                    ctx, "Phone pattern already exists", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void writeJSON(JSONObject obj, File file) {
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            String jsonText = out.toString();

            // below is a simple csv type txt file
            FileWriter writer = new FileWriter(file);
            // write json string
            writer.append(jsonText);

            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }

    void setSettings(Context ctx, String key, boolean value) {
        // to save global settings
        // first use case is handling unknown caller id
        String filePath = ctx.getFilesDir().toString();
        File myFile = new File(filePath, fileName);

        JSONObject obj = getJSONFile(ctx);
        obj.put(key, value);
        writeJSON(obj, myFile);
    }

    boolean getUnknownCallerSettings(Context ctx) {
        JSONObject obj = getJSONFile(ctx);
        if (!obj.containsKey(MainActivity.unknownCallerSettings)) {
            // default of False
            setSettings(ctx, MainActivity.unknownCallerSettings, false);
        }
        return (boolean) obj.get(MainActivity.unknownCallerSettings);
    }

    void addRegexEntry(Context ctx, String entry, String type) {
        addRegexEntry(ctx, entry, true, type);
    }

    void deleteEntry(Context ctx, Integer position, String type) {
        String[] entries = getRegexEntries(ctx, type);

        ArrayList<String> newArray = new ArrayList<>();
        for (int i=0; i<entries.length; i++) {
            if (i != position) {
                newArray.add(entries[i]);
            }
        }
        String newValues = String.join(",", newArray);
        addRegexEntry(ctx, newValues, false, type);
    }

    private JSONObject getJSONFile(Context ctx) {
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, fileName);

        StringBuilder phoneText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            String line;
            while ((line = br.readLine()) != null ) {
                phoneText.append(line);
            }
            br.close();

            // process json
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(phoneText.toString());
            return obj;
        } catch (IOException e) {

        } catch (ParseException pe) {
            Toast toast = Toast.makeText(
                    ctx, "Parse Exception", Toast.LENGTH_LONG);
            toast.show();
        }
        return new JSONObject();
    }

    String[] getRegexEntries(Context ctx, String type){
        // type refers to 'reject' or 'except' phone list
        JSONObject obj = getJSONFile(ctx);
        try {
            if (obj.containsKey(type)) {
                return obj.get(type).toString().split(",");
            }
        } catch (NullPointerException npe) {
            Toast toast = Toast.makeText(
                    ctx, "Null Pointer Exception", Toast.LENGTH_LONG);
            toast.show();
        }
        return new String[0];
    }

    String[] getRegexEntries(Context ctx, String type, JSONObject obj){
        // type refers to 'reject' or 'except' phone list
        try {
            if (obj.containsKey(type)) {
                return obj.get(type).toString().split(",");
            }
        } catch (NullPointerException npe) {
            Toast toast = Toast.makeText(
                    ctx, "Null Pointer Exception", Toast.LENGTH_LONG);
            toast.show();
        }
        return new String[0];
    }

    String constructRegex(String type, String number) {
        String pattern;
        switch (type) {
            case "Starts with":
                pattern = "\\+?"+number+"\\d*";
                break;
            case "Contains":
                pattern = "\\+?\\d*"+number+"\\d*";
                break;
            case "Ends with":
                pattern = "\\+?\\d*"+number;
                break;
            default:
                pattern = "";
        }
        return pattern;
    }

    Boolean checkExist(Context ctx, String number, String type) {
        // check if number exists within type of phone number
        String[] blockedNumber = getRegexEntries(ctx, type);
        for(String blockedNum : blockedNumber)
        {
            if (Pattern.matches(blockedNum, number)) {
                return true;
            }
        }
        return false;
    }

    boolean rejectCall(Context ctx, String phoneNumber) {
        if (checkExist(ctx, phoneNumber, PhoneRegex.typeReject) && !checkExist(ctx, phoneNumber, PhoneRegex.typeExcept)) {
            return true;
        }
        return false;
    }

}
