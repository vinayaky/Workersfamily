package com.example.workersfamily;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileDetail#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class profileDetail extends Fragment {
    TextView name , work, add,number,clint;
    ImageView image;
    FloatingActionButton updateButton;
    String  key = "";
    String imageUrl = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment callHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static profileDetail newInstance(String param1, String param2) {
        profileDetail fragment = new profileDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public profileDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_detail, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateButton = view.findViewById(R.id.updateFab);
        image = view.findViewById(R.id.profileDetailImage);
        name = view.findViewById(R.id.profileDetailName);
        work =view.findViewById(R.id.profileDetailWork);
        add = view.findViewById(R.id.profileDetailAdd);
        clint = view.findViewById(R.id.profileDetailClint);
        number = view.findViewById(R.id.profileDetailNumber);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name.setText(bundle.getString("name"));
            work.setText(bundle.getString("work"));
            add.setText(bundle.getString("add"));
            number.setText(bundle.getString("number"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("image");
            clint.setText(bundle.getString("clint"));
            Glide.with(this).load(bundle.getString("image")).into(image);
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), updateActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("image", imageUrl);
                intent.putExtra("work", work.getText().toString());
                intent.putExtra("add", add.getText().toString());
                intent.putExtra("clint", clint.getText().toString());
                intent.putExtra("number", number.getText().toString());
                intent.putExtra("key", key);
                intent.putExtra("oldImage", imageUrl);

                startActivity(intent);

            }

        });
    }

}