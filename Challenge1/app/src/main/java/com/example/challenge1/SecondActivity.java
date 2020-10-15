package com.example.challenge1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    private int optionSelected;

    // View elements
    private EditText etInputName;
    private EditText etInputOwner;
    private EditText etInputAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if(getIntent() != null && getIntent().getExtras() != null){
            optionSelected = getIntent().getExtras().getInt("SpinnerOption");
        }

        // References to View elements
        etInputName = findViewById(R.id.etInputName);
        etInputOwner = findViewById(R.id.etInputOwner);
        etInputAge = findViewById(R.id.etInputAge);
    }

    public void onReturn(View v){
        Intent resultData = new Intent();

        resultData.putExtra("AnimalSelected", optionSelected);

        if(!etInputName.getText().toString().isEmpty())
            resultData.putExtra("AnimalName", etInputName.getText().toString());

        if(!etInputOwner.getText().toString().isEmpty())
            resultData.putExtra("AnimalOwner", etInputOwner.getText().toString());

        if(!etInputAge.getText().toString().isEmpty())
            resultData.putExtra("AnimalAge", Integer.parseInt(etInputAge.getText().toString()));

        this.setResult(1, resultData);
        this.finish();
    }
}