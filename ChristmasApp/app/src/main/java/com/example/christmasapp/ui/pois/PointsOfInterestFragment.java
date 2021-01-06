package com.example.christmasapp.ui.pois;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.tasks.ReadPointOfInterestTask;
import com.example.christmasapp.tasks.SaveTopicTask;
import com.example.christmasapp.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PointsOfInterestFragment extends Fragment {

    private PointsOfInterestViewModel pointsOfInterestViewModel;
    private PointsOfInterestFragment.PointsOfInterestFragmentListener pointsOfInterestFragmentListener;
    private PoIRecyclerViewAdapter poIRecyclerViewAdapter;

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pointsOfInterestViewModel =
                new ViewModelProvider(this).get(PointsOfInterestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points_of_interest, container, false);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        poIRecyclerViewAdapter = new PoIRecyclerViewAdapter(getContext(), pointOfInterestList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(poIRecyclerViewAdapter);

        /* Listener to handle a click on a single recycler item */
        poIRecyclerViewAdapter.setOnItemClickListener(index -> {
            PointOfInterest poi = pointOfInterestList.get(index);

            /* SUBSTITUTIR ISTO URGENTE! */
            if (poi.getName().equals("Taberna do Tozé Vitor")) {
                Navigation.findNavController(getView()).navigate(R.id.action_pois_to_monument_detailed);
            } else if (poi.getName().equals("Taberna do Tozé Leno")) {
                Navigation.findNavController(getView()).navigate(R.id.action_pois_to_event_detailed);
            }
        });

        poIRecyclerViewAdapter.setOnIconClickListener(index -> {
            PointOfInterest poi = pointOfInterestList.get(index);

            /* SUBSTITUTIR ISTO URGENTE! */
            if (poi.getName().contains(".")) {
                poi.setName(poi.getName().substring(0, poi.getName().indexOf(".")));
                poIRecyclerViewAdapter.notifyItemChanged(index);
            } else {
                poi.setName(poi.getName() + ".");
                poIRecyclerViewAdapter.notifyItemChanged(index);
            }
        });

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
        poIRecyclerViewAdapter.notifyDataSetChanged();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) getActivity());
        Topic topic = new Topic("CM_TP_2020", pointOfInterestList.get(0).getImageUrl());
        bundle.putSerializable(Constants.TOPIC_KEY, topic);
        new SaveTopicTask(bundle).execute();

//        Topic topic2 = new Topic("CM_TP_2020_TEST", pointOfInterestList.get(1).getImageUrl());
//        bundle.putSerializable(Constants.TOPIC_KEY, topic2);
//        new SaveTopicTask(bundle).execute();
    }

    public interface PointsOfInterestFragmentListener {
        void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment);
    }
}