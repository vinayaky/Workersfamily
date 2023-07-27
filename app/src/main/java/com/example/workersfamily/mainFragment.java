package com.example.workersfamily;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class mainFragment extends Fragment {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListenerMain;
    SearchView mainSearch;
    workerAdapter  adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mainRecyclerView
        recyclerView =view.findViewById(R.id.contactList);
        mainSearch=view.findViewById(R.id.mainSearch);
        mainSearch.clearFocus();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new workerAdapter(getContext(),dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("workers");
        dialog.show();

        eventListenerMain = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot UidSnapshot: snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : UidSnapshot.getChildren()) {
                        DataClass data = itemSnapshot.getValue(DataClass.class);
                        try {
                            if (data.getDataClint().equalsIgnoreCase("want work")) {
                                data.setKey(itemSnapshot.getKey());
                                data.setUserUid(UidSnapshot.getKey());
                                dataList.add(data);
                            }
                        } catch (NullPointerException e) {
                            dataList.add(data);
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
        mainSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchList(newText);
                return true;
            }
        });
    }
    public void SearchList(String text){
        ArrayList<DataClass> mainSearchList = new ArrayList<>();
        for(DataClass dataClass:dataList){
            try {
                if(dataClass.getDataWork().toLowerCase().contains(text.toLowerCase())||dataClass.getDataAdd().toLowerCase().contains(text.toLowerCase())||dataClass.getDataName().toLowerCase().contains(text.toLowerCase())){
                    mainSearchList.add(dataClass);}
                }catch (NullPointerException e){
                Toast.makeText(getContext(), "no worker", Toast.LENGTH_SHORT).show();
            }


        }
        adapter.searchDataList(mainSearchList);
    }
}