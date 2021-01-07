package com.example.christmasapp.ui.pois;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestImageTask;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;

import java.util.ArrayList;
import java.util.List;

public class PointsOfInterestFragment extends Fragment {

    private PointsOfInterestFragment.PointsOfInterestFragmentListener pointsOfInterestFragmentListener;
    private PoIRecyclerViewAdapter poIRecyclerViewAdapter;
    private RecyclerView rvPOIList;
    private EditText etSearch;
    private TextView tvTotalPOIs;

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    private List<PointOfInterest> searchPointOfInterestList = new ArrayList<>();

    public static PointsOfInterestFragment newInstance() {
        return new PointsOfInterestFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_of_interest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
        fetchPointsOfInterest();
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

    public void updatePointOfInterestInfo(List<PointOfInterest> pointOfInterestList) {
        this.pointOfInterestList.clear();
        this.pointOfInterestList.addAll(pointOfInterestList);
//        this.pointOfInterestList.add(new Event("Evento teste", "", Type.EVENT, null, null));
        updateSearchPointOfInterestList();
        poIRecyclerViewAdapter.notifyDataSetChanged();
        for(PointOfInterest pointOfInterest : pointOfInterestList) {
            new ReadPointOfInterestImageTask(this, pointOfInterest).execute();
        }

    }

    public void updatePointOfInterestImages(PointOfInterest pointOfInterest) {
        poIRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initViewElements(View view) {
        /* References to View elements */
        rvPOIList = view.findViewById(R.id.recycler_view);
        etSearch = view.findViewById(R.id.et_search);
        tvTotalPOIs = view.findViewById(R.id.count_points_of_interest);
    }

    private void populateView() {
        poIRecyclerViewAdapter = new PoIRecyclerViewAdapter(getContext(), searchPointOfInterestList);
        rvPOIList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvPOIList.setAdapter(poIRecyclerViewAdapter);

        /* Listener to handle a click on a single recycler item */
        poIRecyclerViewAdapter.setOnItemClickListener(index -> {
            PointOfInterest poi = searchPointOfInterestList.get(index);

            /* SUBSTITUTIR ISTO URGENTE! */
            if (poi instanceof Event) {
                pointsOfInterestFragmentListener.toEventDetails(this, poi);
            } else  {
                pointsOfInterestFragmentListener.toMonumentDetails(this, poi);
            }
        });

        poIRecyclerViewAdapter.setOnIconClickListener(index -> {
            PointOfInterest poi = searchPointOfInterestList.get(index);

            /* SUBSTITUTIR ISTO URGENTE! */
            if (poi.getName().contains(".")) {
                poi.setName(poi.getName().substring(0, poi.getName().indexOf(".")));
                poIRecyclerViewAdapter.notifyItemChanged(index);
            } else {
                poi.setName(poi.getName() + ".");
                poIRecyclerViewAdapter.notifyItemChanged(index);
            }
        });

        // Listener para pesquisar notas por título
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Limpar a lista de pesquisa
                searchPointOfInterestList.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Caso em que a pesquisa está vazia
                if (s.length() == 0)
                    searchPointOfInterestList.addAll(pointOfInterestList);

                    // Caso em que é inserida alguma pesquisa
                else
                    for (PointOfInterest poi : pointOfInterestList)
                        if (poi.getName().equalsIgnoreCase(s.toString())
                                || poi.getName().toLowerCase().contains(s.toString().toLowerCase()))
                            searchPointOfInterestList.add(poi);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Atualizar o numero de notas na pesquisa
                tvTotalPOIs.setText(searchPointOfInterestList.size() + " resultado(s) encontrado(s)");
                // Atualizar o Adapter
                poIRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateSearchPointOfInterestList(){
        String searchText = etSearch.getText().toString();
        searchPointOfInterestList.clear();
        for (PointOfInterest poi : pointOfInterestList)
            if (poi.getName().equalsIgnoreCase(searchText) || poi.getName().toLowerCase().contains(searchText.toLowerCase()))
                searchPointOfInterestList.add(poi);

        tvTotalPOIs.setText(searchPointOfInterestList.size() + " resultado(s) encontrado(s)");
    }

    private void fetchPointsOfInterest() {
        new ReadPointOfInterestInfoTask(this).execute();
    }

    public interface PointsOfInterestFragmentListener {

        void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment);
        void toMonumentDetails(PointsOfInterestFragment pointsOfInterestFragment0, PointOfInterest poi);
        void toEventDetails(PointsOfInterestFragment pointsOfInterestFragment0, PointOfInterest poi);
    }
}