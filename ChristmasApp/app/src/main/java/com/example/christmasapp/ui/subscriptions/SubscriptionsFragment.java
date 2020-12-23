package com.example.christmasapp.ui.subscriptions;

import android.os.Bundle;
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
import com.example.christmasapp.ui.points_of_interest.PointsOfInterestViewModel;

public class SubscriptionsFragment extends Fragment {

    private SubscriptionsViewModel subscriptionsFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionsFragment =
                new ViewModelProvider(this).get(SubscriptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        final TextView textView = root.findViewById(R.id.text_subscriptions);
        subscriptionsFragment.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}