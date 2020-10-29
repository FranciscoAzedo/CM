package com.example.challenge1.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.challenge1.Animal;
import com.example.challenge1.R;
import com.example.challenge1.Utils;

import java.util.ArrayList;

public class ListAnimalsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private Spinner spinner;
    private TextView ownerName;
    private TextView animalName;
    private TextView animalAge;
    private Button animalInfoButton;

    private static final String LIST_ANIMALS_KEY = "LIST_ANIMALS";
    private static final String INDEX_KEY = "INDEX";

    private ArrayList<Animal> animals;
    private Animal currentAnimal;
    private int currentAnimalIndex;

    private OnListAnimalsFragmentInteractionListener mListener;

    public ListAnimalsFragment() {
        // Required empty public constructor
    }

    public static ListAnimalsFragment newInstance() {
        return new ListAnimalsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initArguments(){

        if (getArguments() != null) {
            animals = Utils.deserializeListOfAnimals(getArguments().getString(LIST_ANIMALS_KEY));
            currentAnimalIndex = Integer.parseInt(getArguments().getString(INDEX_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initArguments();

        View view = inflater.inflate(R.layout.fragment_list_animals, container, false);

        imageView = view.findViewById(R.id.imageView);
        ownerName = view.findViewById(R.id.ownerName);
        animalName = view.findViewById(R.id.animalName);
        animalAge = view.findViewById(R.id.animalAge);

        spinner = view.findViewById(R.id.spinner);
        spinner.setSelection(currentAnimalIndex);
        spinner.setOnItemSelectedListener(this);

        animalInfoButton = view.findViewById(R.id.button);
        animalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnListAnimalsFragmentInteraction(currentAnimal, currentAnimalIndex);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentAnimal = getAnimalByType(spinner.getSelectedItem().toString());

        imageView.setImageResource(
                Utils.getResourceByName(getResources(),
                        currentAnimal.getType(),
                        "drawable",
                        getActivity().getPackageName())
        );

        updateLabels(currentAnimal);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void updateLabels(Animal animal){
        if(animal.getName() != null){
            animalName.setText("Name: " + animal.getName());
            animalName.setEnabled(true);
            animalName.setVisibility(View.VISIBLE);
        }
        else {
            animalName.setVisibility(View.GONE);
            animalName.setEnabled(false);
        }

        if(animal.getOwner() != null){
            ownerName.setText("Owner: " + animal.getOwner());
            ownerName.setEnabled(true);
            ownerName.setVisibility(View.VISIBLE);
        }
        else {
            ownerName.setVisibility(View.GONE);
            ownerName.setEnabled(false);
        }

        if(animal.getAge() != 0){
            animalAge.setText("Age: " + animal.getAge());
            animalAge.setEnabled(true);
            animalAge.setVisibility(View.VISIBLE);
        }
        else {
            animalAge.setVisibility(View.GONE);
            animalAge.setEnabled(false);
        }
    }

    private Animal getAnimalByType(String type){
        for (Animal animal : animals) {
            if (animal.getType().equalsIgnoreCase(type))
                return animal;
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListAnimalsFragmentInteractionListener) {

            // This will initialize the variable. It will return an exception if it is not
            //  implemented in the java code of the variable context (in our case the
            //  context is the MainActivity.
            mListener = (OnListAnimalsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListAnimalsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListAnimalsFragmentInteractionListener {
        void OnListAnimalsFragmentInteraction(Animal animal, int currentAnimalIndex);
    }
}
