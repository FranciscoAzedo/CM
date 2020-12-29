package com.example.christmasapp.ui.subscriptions;

import android.content.Context;
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

public class SubscriptionsFragment extends Fragment {

    private SubscriptionsViewModel subscriptionsViewModel;

    private SubscriptionsFragmentListener subscriptionsFragmentListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionsViewModel =
                new ViewModelProvider(this).get(SubscriptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        final TextView textView = root.findViewById(R.id.text_subscriptions);
        subscriptionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscriptionsFragmentListener.subscriptionsActive(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionsFragmentListener) {
            subscriptionsFragmentListener = (SubscriptionsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subscriptionsFragmentListener = null;
    }

    public interface SubscriptionsFragmentListener {
        void subscriptionsActive(SubscriptionsFragment subscriptionsFragment);
    }
}