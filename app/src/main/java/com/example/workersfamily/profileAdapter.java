package com.example.workersfamily;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class profileAdapter extends RecyclerView.Adapter<profileHolder> {
    private Context profileContext;
    private List<DataClass> userprofileList;

    public profileAdapter(Context profileContext, List<DataClass> userprofileList) {
        this.profileContext = profileContext;
        this.userprofileList = userprofileList;
    }


    @NonNull
    @Override
    public profileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileview,parent,false);
        return new profileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profileHolder holder, int position) {
        Glide.with(profileContext).load(userprofileList.get(position).getDataImage()).into(holder.image);
        holder.name.setText(userprofileList.get(position).getDataName());
        holder.work.setText(userprofileList.get(position).getDataWork());
        holder.number.setText(userprofileList.get(position).getDataNumber());    
        holder.clint.setText(userprofileList.get(position).getDataClint());
        holder.add.setText(userprofileList.get(position).getDataAdd());

        holder.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileContext,profileDetailActivity.class);
                intent.putExtra("image",userprofileList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("name",userprofileList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("work",userprofileList.get(holder.getAdapterPosition()).getDataWork());
                intent.putExtra("number",userprofileList.get(holder.getAdapterPosition()).getDataNumber());
                intent.putExtra("clint",userprofileList.get(holder.getAdapterPosition()).getDataClint());
                intent.putExtra("add",userprofileList.get(holder.getAdapterPosition()).getDataAdd());
                intent.putExtra("key",userprofileList.get(holder.getAdapterPosition()).getKey());

                profileContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userprofileList.size();
    }
}
class profileHolder  extends RecyclerView.ViewHolder{
    ImageView image;
    TextView name , work, add,clint,number;
    CardView profileCardView;

    public profileHolder(@NonNull View itemView) {
        super(itemView);
        image= itemView.findViewById(R.id.profileImg);
        name = itemView.findViewById(R.id.profileName);
        work = itemView.findViewById(R.id.profileWork);
        add = itemView.findViewById(R.id.profileAddress);
        clint = itemView.findViewById(R.id.profileClint);
        number = itemView.findViewById(R.id.profilePhone);
        profileCardView = itemView.findViewById(R.id.profileView);


    }
}
