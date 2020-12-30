package com.example.christmasapp.ui.points_of_interest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestTask;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;

import java.io.IOException;
import java.util.List;

public class PointsOfInterestFragment extends Fragment {

    private PointsOfInterestViewModel pointsOfInterestViewModel;

    private PointsOfInterestFragment.PointsOfInterestFragmentListener pointsOfInterestFragmentListener;

    private List<PointOfInterest> pointOfInterestList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pointsOfInterestViewModel =
                new ViewModelProvider(this).get(PointsOfInterestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points_of_interest, container, false);
        final TextView textView = root.findViewById(R.id.text_points_of_interest);
        pointsOfInterestViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        fetchPointsOfInterest();
        return root;
    }

    private void fetchPointsOfInterest() {
        new ReadPointOfInterestTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        pointsOfInterestFragmentListener.pointsOfInterestActive(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PointsOfInterestFragmentListener) {
            pointsOfInterestFragmentListener = (PointsOfInterestFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pointsOfInterestFragmentListener = null;
    }

    public interface PointsOfInterestFragmentListener {
        void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment);
    }
}