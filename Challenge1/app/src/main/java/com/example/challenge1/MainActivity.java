package com.example.challenge1;

//import androidx.annotation.Nullable;
//import android.content.Intent;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.challenge1.fragments.AnimalInfoFragment;
import com.example.challenge1.fragments.ListAnimalsFragment;

import java.util.ArrayList;

//public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//
//    private ImageView imageView;
//    private Spinner spinner;
//    private TextView ownerName;
//    private TextView animalName;
//    private TextView animalAge;
//
//    private ArrayList<Animal> animals = new ArrayList<Animal>(){
//        {
//            add(new Animal("cat", "Nelsu", "Tobias", 4));
//            add(new Animal("dog", "Manel", "Sissi", 3));
//            add(new Animal("duck", "Jeremias", "Alfredo", 2));
//            add(new Animal("elephant", "Lipito", "Trombas", 14));
//            add(new Animal("fish", "Cidito", "Fixe", 10));
//            add(new Animal("giraffe", "Celito", "Cumprido", 8));
//            add(new Animal("horse", "Litito", "Crinas", 9));
//            add(new Animal("lion", "Acacio", "Jubinha", 24));
//            add(new Animal("rabbit", "Armenioc", "Saltitao", 5));
//            add(new Animal("tiger", "Junioc", "Dentes", 7));
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        imageView = findViewById(R.id.imageView);
//        ownerName = findViewById(R.id.ownerName);
//        animalName = findViewById(R.id.animalName);
//        animalAge = findViewById(R.id.animalAge);
//
//        spinner = findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(this);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Animal currentAnimal = getAnimalByType(spinner.getSelectedItem().toString());
//
//        imageView.setImageResource(
//                Utils.getResourceByName(getResources(),
//                        currentAnimal.getType(),
//                        "drawable",
//                        getPackageName())
//        );
//
//        updateLabels(currentAnimal);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) { }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 2) {
//            if (resultCode == 1) {
//
//                int animalSelected = data.getExtras().getInt("AnimalSelected");
//
//                if (data.hasExtra("AnimalName"))
//                    animals.get(animalSelected).setName(data.getExtras().getString("AnimalName"));
//                else
//                    animals.get(animalSelected).setName(null);
//
//                if (data.hasExtra("AnimalOwner"))
//                    animals.get(animalSelected).setOwner(data.getExtras().getString("AnimalOwner"));
//                else
//                    animals.get(animalSelected).setOwner(null);
//
//
//                if (data.hasExtra("AnimalAge"))
//                    animals.get(animalSelected).setAge(data.getExtras().getInt("AnimalAge"));
//                else
//                    animals.get(animalSelected).setAge(0);
//
//
//                updateLabels(animals.get(animalSelected));
//            }
//        }
//    }
//
//    private void updateLabels(Animal animal){
//        if(animal.getName() != null){
//            animalName.setText("Name: " + animal.getName());
//            animalName.setEnabled(true);
//            animalName.setVisibility(View.VISIBLE);
//        }
//        else {
//            animalName.setVisibility(View.GONE);
//            animalName.setEnabled(false);
//        }
//
//        if(animal.getOwner() != null){
//            ownerName.setText("Owner: " + animal.getOwner());
//            ownerName.setEnabled(true);
//            ownerName.setVisibility(View.VISIBLE);
//        }
//        else {
//            ownerName.setVisibility(View.GONE);
//            ownerName.setEnabled(false);
//        }
//
//        if(animal.getAge() != 0){
//            animalAge.setText("Age: " + animal.getAge());
//            animalAge.setEnabled(true);
//            animalAge.setVisibility(View.VISIBLE);
//        }
//        else {
//            animalAge.setVisibility(View.GONE);
//            animalAge.setEnabled(false);
//        }
//    }
//
//    private Animal getAnimalByType(String type){
//        for (Animal animal : animals) {
//            if (animal.getType().equalsIgnoreCase(type))
//                return animal;
//        }
//        return null;
//    }
//
//    public void editInfo(View view) {
//        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
//        intent.putExtra("SpinnerOption", spinner.getSelectedItemPosition());
//        startActivityForResult(intent, 2);
//    }
//}

public class MainActivity extends AppCompatActivity implements ListAnimalsFragment.OnListAnimalsFragmentInteractionListener, AnimalInfoFragment.OnAnimalInfoFragmentInteractionListener {

    private ArrayList<Animal> animals = new ArrayList<Animal>(){
        {
            add(new Animal("cat", "Nelsu", "Tobias", 4));
            add(new Animal("dog", "Manel", "Sissi", 3));
            add(new Animal("duck", "Jeremias", "Alfredo", 2));
            add(new Animal("elephant", "Lipito", "Trombas", 14));
            add(new Animal("fish", "Cidito", "Fixe", 10));
            add(new Animal("giraffe", "Celito", "Cumprido", 8));
            add(new Animal("horse", "Litito", "Crinas", 9));
            add(new Animal("lion", "Acacio", "Jubinha", 24));
            add(new Animal("rabbit", "Armenioc", "Saltitao", 5));
            add(new Animal("tiger", "Junioc", "Dentes", 7));
        }
    };

    private static final String LIST_ANIMALS_KEY = "LIST_ANIMALS";
    private static final String ANIMAL_KEY = "ANIMAL";
    private static final String INDEX_KEY = "INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListAnimalsFragment listAnimalsFragment = ListAnimalsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(LIST_ANIMALS_KEY, Utils.serializeListOfAnimals(animals));
        bundle.putString(INDEX_KEY, String.valueOf(0));
        listAnimalsFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout, listAnimalsFragment, "listAnimalsFragment");
        fragmentTransaction.commit();
    }

    private void updateAnimal(Animal updatedAnimal){
        for (Animal animal: animals) {
            if (animal.getType().equalsIgnoreCase(updatedAnimal.getType())) {
                animal.setName(updatedAnimal.getName());
                animal.setAge(updatedAnimal.getAge());
                animal.setOwner(updatedAnimal.getOwner());
            }
        }
    }

    @Override
    public void OnListAnimalsFragmentInteraction(Animal animal, int currentAnimalIndex) {

        AnimalInfoFragment animalInfoFragment;

        if((animalInfoFragment = (AnimalInfoFragment) getSupportFragmentManager().findFragmentByTag("animalInfoFragment")) == null) {
            animalInfoFragment = AnimalInfoFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(ANIMAL_KEY, Utils.serializeAnimal(animal));
            bundle.putString(INDEX_KEY, String.valueOf(currentAnimalIndex));
            animalInfoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout, animalInfoFragment, "animalInfoFragment");
            fragmentTransaction.addToBackStack("Top");
            fragmentTransaction.commit();
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putString(ANIMAL_KEY, Utils.serializeAnimal(animal));
            bundle.putString(INDEX_KEY, String.valueOf(currentAnimalIndex));
            animalInfoFragment.setArguments(bundle);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void OnAnimalInfoFragmentInteraction(Animal animal, int currentAnimalIndex) {

        ListAnimalsFragment listAnimalsFragment;

        updateAnimal(animal);
        if((listAnimalsFragment = (ListAnimalsFragment) getSupportFragmentManager().findFragmentByTag("listAnimalsFragment")) == null) {
            listAnimalsFragment = ListAnimalsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(LIST_ANIMALS_KEY, Utils.serializeListOfAnimals(animals));
            bundle.putString(INDEX_KEY, String.valueOf(currentAnimalIndex));
            listAnimalsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.layout, listAnimalsFragment, "listAnimalsFragment");
            fragmentTransaction.commit();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString(LIST_ANIMALS_KEY, Utils.serializeListOfAnimals(animals));
            bundle.putString(INDEX_KEY, String.valueOf(currentAnimalIndex));
            listAnimalsFragment.setArguments(bundle);
            getSupportFragmentManager().popBackStack();
        }
    }
}