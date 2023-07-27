package com.example.workersfamily;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HiringFragment extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SearchView hiringSearch;
    workerAdapter  adapter;
    List<DataClass> dataListHiring;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView profile = view.findViewById(R.id.profileL);
        hiringSearch = view.findViewById(R.id.hiringSearch);
        hiringSearch.clearFocus();
        profile.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataListHiring = new ArrayList<>();
        adapter = new workerAdapter(getContext(),dataListHiring);
        profile.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("workers");
        dialog.show();

         eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListHiring.clear();
                for (DataSnapshot UidItemS : snapshot.getChildren()) {
                    for (DataSnapshot itemS : UidItemS.getChildren()) {
                        DataClass dataHiring = itemS.getValue(DataClass.class);
                        try {
                            if (dataHiring.getDataClint().equalsIgnoreCase("Hiring")) {
                                dataHiring.setKey(itemS.getKey());
                                dataHiring.setUserUid(UidItemS.getKey());
                                dataListHiring.add(dataHiring);
                            }
                        } catch (NullPointerException e) {
                            dataListHiring.add(dataHiring);
                        }

                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
         hiringSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 hiringSearchList(newText);
                 return true;
             }
         });
    }
    public void hiringSearchList(String text){
        ArrayList<DataClass> hiringSearchList = new ArrayList<>();
        for(DataClass dataClass:dataListHiring){
            if(dataClass.getDataWork().toLowerCase().contains(text.toLowerCase())||dataClass.getDataAdd().toLowerCase().contains(text.toLowerCase())||dataClass.getDataName().toLowerCase().contains(text.toLowerCase())){
                hiringSearchList.add(dataClass);
            }
        }
        adapter.searchDataList(hiringSearchList);
    }
}