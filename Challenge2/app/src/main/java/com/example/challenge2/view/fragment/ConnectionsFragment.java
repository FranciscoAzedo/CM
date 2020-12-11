package com.example.challenge2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.SaveTopicTask;
import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.model.Topic;
import com.example.challenge2.view.ConnectionListAdapter;
import com.example.challenge2.view.NoteActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionsFragment extends Fragment {

    private NoteActivity noteActivity;

    private ImageView ivAdd;
    private ImageView ivBack;

    // Recycler View elements
    private RecyclerView rvConnectionsList;
    private ConnectionListAdapter rvConnectionsListAdapter;
    private LayoutManager rvConnectionsListLayoutManager;

    private OnConnectionsFragmentInteractionListener mListener;

    private NoteKeeperDBHelper noteKeeperDBHelper;
    private ArrayList<Note> noteTitlesList = new ArrayList<>();
    private ArrayList<Note> noteTitlesSearchList = new ArrayList<>();

    private ArrayList<Topic> topicList = new ArrayList<>();

    public static ConnectionsFragment newInstance() {
        return new ConnectionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connections_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initArguments();
        initViewElements(view);
        populateView();

        // Listener para quando existe um click para adicionar topico
        ivAdd.setOnClickListener(v -> {
            final EditText edittext = new EditText(getContext());
            edittext.setHint(R.string.change_dialog_add_topic_hint);

            // Apresentar a dialog para o utilizador introduzir novo titulo
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.change_dialog_title)
                    .setView(edittext)
                    .setPositiveButton(R.string.change_dialog_confirm, (dialog, whichButton) -> {

                        String title = edittext.getText().toString();

                        if (!title.isEmpty()) {
                            Topic topic = new Topic(title);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
                            bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
                            bundle.putSerializable(Utils.TOPIC_KEY, topic);
                            new SaveTopicTask(getActivity(), bundle).execute();
                        }
                    })
                    .setNegativeButton(R.string.change_dialog_cancel, (dialog, whichButton) -> {
                    }).show();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
//            bundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
//            mListener.OnConnectionsFragmentInteraction(bundle);
        });

        ivBack.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
            bundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
            mListener.OnConnectionsBackFragmentInteraction(bundle);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        rvConnectionsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_long_press, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionsFragmentInteractionListener) {
            mListener = (OnConnectionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyLoadedTopics(Bundle bundle) {
        if (bundle != null) {
            topicList = (ArrayList<Topic>) bundle.getSerializable(Utils.LIST_TOPICS_KEY);
        } else {
            topicList = new ArrayList<>();
        }
    }

    public void notifyNewTopic(Bundle bundle) {
        if (bundle != null) {
            Topic topic = (Topic) bundle.getSerializable(Utils.TOPIC_KEY);
            topicList.add(topic);
            rvConnectionsListAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDeletedTopic(Bundle bundle) {
        Topic topic = (Topic) bundle.getSerializable(Utils.TOPIC_KEY);
        topicList.remove(topic);
        rvConnectionsListAdapter.notifyDataSetChanged();
    }

    private void initArguments() {
        if (getArguments() != null) {
            noteKeeperDBHelper = (NoteKeeperDBHelper) getArguments().getSerializable(Utils.DATABASE_HELPER_KEY);
            noteActivity = (NoteActivity) getArguments().getSerializable(Utils.ACTIVITY_KEY);
            noteTitlesList = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_NOTES_KEY);
            noteTitlesSearchList = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_SEARCH_NOTES_KEY);

            ArrayList<Topic> topics = (ArrayList<Topic>) getArguments().getSerializable(Utils.LIST_TOPICS_KEY);

            if (topics != null)
                addTopicsList(topics);
        } else {
            noteKeeperDBHelper = new NoteKeeperDBHelper(getActivity());
            noteActivity = new NoteActivity();
            noteTitlesList = new ArrayList<>();
            noteTitlesSearchList = new ArrayList<>();
            topicList = new ArrayList<>();
        }
    }

    private void addTopicsList(ArrayList<Topic> topics) {
        for (Topic topic : topics)
            if (!topicList.contains(topic))
                topicList.add(topic);
    }

    private void initViewElements(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        ivAdd = view.findViewById(R.id.iv_add);
        rvConnectionsList = view.findViewById(R.id.recycler_connections);
    }

    private void populateView() {
        // Setup da Recycler View
        rvConnectionsListLayoutManager = new LinearLayoutManager(getContext());
        rvConnectionsList.setLayoutManager(rvConnectionsListLayoutManager);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        bundle.putSerializable(Utils.ACTIVITY_KEY, noteActivity);
        rvConnectionsListAdapter = new ConnectionListAdapter(topicList, getActivity(), bundle);
        rvConnectionsList.setAdapter(rvConnectionsListAdapter);
    }

    public interface OnConnectionsFragmentInteractionListener {
        void OnConnectionsBackFragmentInteraction(Bundle bundle);
    }
}
