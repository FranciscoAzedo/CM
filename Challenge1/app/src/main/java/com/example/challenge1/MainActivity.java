package com.example.challenge1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private Spinner spinner;
    private TextView ownerName;
    private TextView animalName;
    private TextView animalAge;

    private ArrayList<Animal> animals = new ArrayList<Animal>(){
        {
            add(new Animal("cat", "Nelsu", "Tobias", 4));
            add(new Animal("dog", "Manel", "Sissi", 3));
            add(new Animal("duck", "Jeremias", "Alfredo", 2));
            add(new Animal("elephant", "Lipito", "Trombas", 14));
            add(new Animal("fish", "Cidito", "Fixe", 10));
            add(new Animal("giraffe", "Celito", "Cumprido", 8));
            add(new Animal("horse", "Litito", "Tarolo", 9));
            add(new Animal("lion", "Acacio", "Jubinha", 24));
            add(new Animal("rabbit", "Armenioc", "Saltitao", 5));
            add(new Animal("tiger", "Junioc", "Dentes", 7));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        ownerName = findViewById(R.id.ownerName);
        animalName = findViewById(R.id.animalName);
        animalAge = findViewById(R.id.animalAge);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Animal currentAnimal = getAnimalByType(spinner.getSelectedItem().toString());

        imageView.setImageResource(
                Utils.getResourceByName(getResources(),
                        currentAnimal.getType(),
                        "drawable",
                        getPackageName())
        );
        ownerName.setText(currentAnimal.getOwner());
        animalName.setText(currentAnimal.getName());
        animalAge.setText(String.valueOf(currentAnimal.getAge()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == 1) {

                int animalSelected = data.getExtras().getInt("AnimalSelected");

                if (data.hasExtra("AnimalName"))
                    animals.get(animalSelected).setName(data.getExtras().getString("AnimalName"));

                if (data.hasExtra("AnimalOwner"))
                    animals.get(animalSelected).setOwner(data.getExtras().getString("AnimalOwner"));

                if (data.hasExtra("AnimalAge"))
                    animals.get(animalSelected).setAge(data.getExtras().getInt("AnimalAge"));

                ownerName.setText(animals.get(animalSelected).getOwner());
                animalName.setText(animals.get(animalSelected).getName());
                animalAge.setText(String.valueOf(animals.get(animalSelected).getAge()));
            }
        }
    }

    private Animal getAnimalByType(String type){
        for (Animal animal : animals) {
            if (animal.getType().equalsIgnoreCase(type))
                return animal;
        }
        return null;
    }

    public void editInfo(View view) {
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.putExtra("SpinnerOption", spinner.getSelectedItemPosition());
        startActivityForResult(intent, 2);
    }
}