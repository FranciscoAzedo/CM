package com.example.christmasapp.ui.pois;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.tasks.DeleteTopicTask;
import com.example.christmasapp.tasks.ReadPointOfInterestImageTask;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;
import com.example.christmasapp.tasks.SaveTopicTask;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PointsOfInterestFragment extends Fragment {

    private PointsOfInterestFragment.PointsOfInterestFragmentListener pointsOfInterestFragmentListener;
    private PoIRecyclerViewAdapter poIRecyclerViewAdapter;
    private RecyclerView rvPOIList;
    private EditText etSearch;
    private TextView tvTotalPOIs;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    private List<PointOfInterest> searchPointOfInterestList = new ArrayList<>();

    private boolean created = false;
    private boolean isNetworkAvailable = false;

    // Broadcast Receiver to handles connectivity changes (enabled/disabled)
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();

                if(!TextUtils.isEmpty(action) && action.matches("android.net.conn.CONNECTIVITY_CHANGE"))
                    isNetworkAvailable = Utils.isOnline(getContext());
            }
        }
    };

    public static PointsOfInterestFragment newInstance() {
        return new PointsOfInterestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        created = true;
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
        if (created) {
            fetchPointsOfInterest();
            created = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register broadcast receiver to be aware of changes on connectivity status
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getContext().registerReceiver(networkReceiver, intentFilter);

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getActivity());
        List<Topic> topicList = sharedPreferencesHelper.getSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY);
        for (PointOfInterest pointOfInterest : pointOfInterestList)
            if (!Utils.sharedPreferencesContainsPointOfInterest(pointOfInterest.getName(), topicList))
                pointOfInterest.setSubscribed(false);
        poIRecyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onPause() {
        // Unregister the broadcast receiver for changes on connectivity status
        getContext().unregisterReceiver(networkReceiver);

        super.onPause();
    }

    public void updatePointOfInterestInfo(List<PointOfInterest> pointOfInterestList) {
        this.pointOfInterestList.clear();
        this.pointOfInterestList.addAll(pointOfInterestList);
        updateSearchPointOfInterestList();
        poIRecyclerViewAdapter.notifyDataSetChanged();
        for(PointOfInterest pointOfInterest : pointOfInterestList) {
            if(isNetworkAvailable)
                new ReadPointOfInterestImageTask(this, pointOfInterest).execute();
        }

    }

    public void updatePointOfInterestImages() {
        poIRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initViewElements(View view) {
        /* References to View elements */
        rvPOIList = view.findViewById(R.id.recycler_view);
        etSearch = view.findViewById(R.id.et_search);
        tvTotalPOIs = view.findViewById(R.id.count_points_of_interest);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);

        // Get the current network status
        isNetworkAvailable = Utils.isOnline(requireContext());
    }

    private void populateView() {
        poIRecyclerViewAdapter = new PoIRecyclerViewAdapter(getContext(), searchPointOfInterestList);
        rvPOIList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvPOIList.setAdapter(poIRecyclerViewAdapter);

        /* Listener to handle a click on a single recycler item */
        poIRecyclerViewAdapter.setOnItemClickListener(index -> {
            PointOfInterest poi = searchPointOfInterestList.get(index);

            if (poi instanceof Event)
                pointsOfInterestFragmentListener.toEventDetails(this, poi);
            else
                pointsOfInterestFragmentListener.toMonumentDetails(this, poi);
        });

        poIRecyclerViewAdapter.setOnIconClickListener(index -> {
            PointOfInterest poi = searchPointOfInterestList.get(index);
            Topic topic = new Topic(
                 poi.getName(),
                 poi.getImageUrl()
            );

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TOPIC_KEY, topic);
            bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) getActivity());

            if(poi.isSubscribed()) {
                new DeleteTopicTask(bundle).execute();
            } else {
                new SaveTopicTask(bundle).execute();
            }

            poi.setSubscribed(!poi.isSubscribed());
            poIRecyclerViewAdapter.notifyDataSetChanged();
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

        // Listener update Swipe Refresh Layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchPointsOfInterest();
            swipeRefreshLayout.setRefreshing(false);
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
        if(isNetworkAvailable)
            new ReadPointOfInterestInfoTask(this).execute();
    }

    public interface PointsOfInterestFragmentListener {
        void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment);
        void toMonumentDetails(PointsOfInterestFragment pointsOfInterestFragment0, PointOfInterest poi);
        void toEventDetails(PointsOfInterestFragment pointsOfInterestFragment0, PointOfInterest poi);
    }
}