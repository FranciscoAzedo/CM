package com.example.challenge1.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.challenge1.Animal;
import com.example.challenge1.R;
import com.example.challenge1.Utils;

public class AnimalInfoFragment extends Fragment {

    private EditText animalName;
    private EditText ownerName;
    private EditText animalAge;
    private Button saveAnimalChangesButton;

    private static final String ANIMAL_KEY = "ANIMAL";
    private static final String INDEX_KEY = "INDEX";

    private int currentAnimalIndex;
    private Animal currentAnimal;

    private OnAnimalInfoFragmentInteractionListener mListener;

    public AnimalInfoFragment() {
        // Required empty public constructor
    }

    public static AnimalInfoFragment newInstance() {
        return new AnimalInfoFragment();
    }

    public void initArguments(){
        if (getArguments() != null) {
            currentAnimal = Utils.deserializeAnimal(getArguments().getString(ANIMAL_KEY));
            currentAnimalIndex = Integer.parseInt(getArguments().getString(INDEX_KEY));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initArguments();

        View view = inflater.inflate(R.layout.fragment_animal_info, container, false);
        animalAge = view.findViewById(R.id.animalAge);
        animalName = view.findViewById(R.id.animalName);
        ownerName = view.findViewById(R.id.ownerName);

        saveAnimalChangesButton = view.findViewById(R.id.button);
        saveAnimalChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChangesToAnimal(currentAnimal);
                mListener.OnAnimalInfoFragmentInteraction(currentAnimal, currentAnimalIndex);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnimalInfoFragmentInteractionListener)
            mListener = (OnAnimalInfoFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement OnAnimalInfoFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setChangesToAnimal(Animal animal){
        if(!animalName.getText().toString().isEmpty())
            animal.setName(animalName.getText().toString());
        else
            animal.setName(null);

        if(!animalAge.getText().toString().isEmpty())
            animal.setAge(Integer.parseInt(animalAge.getText().toString()));
        else
            animal.setAge(0);

        if(!ownerName.getText().toString().isEmpty())
            animal.setOwner(ownerName.getText().toString());
        else
            animal.setOwner(null);
    }


    public interface OnAnimalInfoFragmentInteractionListener {
        void OnAnimalInfoFragmentInteraction(Animal animal, int currentAnimalIndex);
    }
}