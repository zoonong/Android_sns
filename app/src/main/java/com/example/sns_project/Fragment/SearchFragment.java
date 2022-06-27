package com.example.sns_project.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sns_project.Model.User;
import com.example.sns_project.Adapter.UserAdapter;
import com.example.sns_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar = view.findViewById(R.id.search_bar);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        recyclerView.setAdapter(userAdapter);

        readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers (String s){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    mUsers.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (search_bar.getText().toString().equals("")){
                    mUsers.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}