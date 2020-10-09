package com.chestnutcone.phoneblocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private PhoneRegex phoneRegex = new PhoneRegex();
    private static final int REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");
//        checkPermission();
        requestRole();

        // get add button
        ImageButton addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // jump to form
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        // advance add button
        ImageButton advanceBtn = (ImageButton) findViewById(R.id.advance_button);
        advanceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // goes into advance setting which allows users to just enter regex
                startActivity(new Intent(MainActivity.this, AdvanceActivity.class));
            }
        });
    }

    public void requestRole() {
        // trying new API
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
        startActivityForResult(intent, REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                // app is now the call screening app
                Log.d(TAG, "onActivityResult: CALL SCREEN APP");

            } else {
                Log.d(TAG, "onActivityResult: NOT CALL SCREEN APP");
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        String[] myDataset = phoneRegex.getRegexEntries(this, PhoneRegex.typeReject);
        initializeLayout(myDataset);
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    public void showRejectionRegex(View v) {
        String[] myDataset = phoneRegex.getRegexEntries(this, PhoneRegex.typeReject);
        initializeLayout(myDataset);
    }

    public void showExceptionRegex(View v) {
        String[] myDataset = phoneRegex.getRegexEntries(this, PhoneRegex.typeExcept);
        initializeLayout(myDataset);
    }

    private void initializeLayout(String[] myDataset) {
        // start list view
//        phoneRegex.deleteAllEntries(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        // get swipe gestures
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                RadioButton rejectRadio = (RadioButton) findViewById(R.id.mainActivity_radio_reject);
                String type;
                if (rejectRadio.isChecked()) {
                    type = PhoneRegex.typeReject;
                } else {
                    type = PhoneRegex.typeExcept;
                }

                // on swipe
                Integer position = viewHolder.getAdapterPosition();
                phoneRegex.deleteEntry(MainActivity.this, position, type);
                String[] mDataset = phoneRegex.getRegexEntries(MainActivity.this, type);
                // create new adapter
                mAdapter = new RecyclerViewAdapter(mDataset);
                recyclerView.setAdapter(mAdapter);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }



}
