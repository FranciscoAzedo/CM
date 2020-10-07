package com.example.challenge1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        imageView.setImageResource(getResourceByName(spinner.getSelectedItem().toString(), "drawable"));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private int getResourceByName(String resourceName, String resourceType) {
        return getResources().getIdentifier(resourceName.toLowerCase(), resourceType, getPackageName());
    }
}