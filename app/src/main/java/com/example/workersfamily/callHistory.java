package com.example.workersfamily;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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

public class callHistory extends Fragment {
    RecyclerView recyclerViewHistory;
    List<DataClass> callDataList;
    DatabaseReference databaseReference;
    ValueEventListener callEventListener;
    callAdapter callAdapter;
    String key="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_history, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mainRecyclerView
        recyclerViewHistory = view.findViewById(R.id.callList);
        Bundle bundle=this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
        }
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog callDialog = builder.create();
        callDialog.show();

        callDataList = new ArrayList<>();
        callAdapter = new callAdapter(getContext(), callDataList);
        recyclerViewHistory.setAdapter(callAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("callHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key);
        callDialog.show();
        callEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callDataList.clear();
                for (DataSnapshot UidSnapshot: snapshot.getChildren()) {
                    String uidSnap= UidSnapshot.getKey();
                    String date=snapshot.child(uidSnap).getValue(String.class);
                    FirebaseDatabase.getInstance().getReference("workers").child(uidSnap)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                          String itemSnap= itemSnapshot.getKey();
                                          assert itemSnap != null;
                                          if (itemSnap.equals("mainUser")) {
                                              DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                                              dataClass.setKey(itemSnapshot.getKey());
                                              dataClass.setUserUid(snapshot.getKey());
                                              dataClass.setDate(date);
                                              callDataList.add(dataClass);
                                          }else {

                                          }
                                    }callAdapter.notifyDataSetChanged();
                                    callDialog.dismiss();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                }callAdapter.notifyDataSetChanged();
                callDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {callDialog.dismiss();}
        });

    }
}