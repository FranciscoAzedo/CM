package com.example.christmasapp.ui.points_of_interest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestTask;

import java.util.ArrayList;
import java.util.List;

public class PointsOfInterestFragment extends Fragment {

    private PointsOfInterestViewModel pointsOfInterestViewModel;
    private PointsOfInterestFragment.PointsOfInterestFragmentListener pointsOfInterestFragmentListener;
    private PoIRecycleViewAdapter poIRecycleViewAdapter;

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pointsOfInterestViewModel =
                new ViewModelProvider(this).get(PointsOfInterestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points_of_interest, container, false);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        poIRecycleViewAdapter = new PoIRecycleViewAdapter(getContext(), pointOfInterestList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(poIRecycleViewAdapter);

        fetchPointsOfInterest();
        return root;
    }

    private void fetchPointsOfInterest() {
        new ReadPointOfInterestTask(this).execute();
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

    public void updatePointOfInterest(List<PointOfInterest> pointOfInterestList) {
        this.pointOfInterestList.clear();
        this.pointOfInterestList.addAll(pointOfInterestList);
        poIRecycleViewAdapter.notifyDataSetChanged();
    }

    public interface PointsOfInterestFragmentListener {
        void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment);
    }
}