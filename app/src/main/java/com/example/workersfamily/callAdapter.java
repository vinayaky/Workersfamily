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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class callAdapter extends RecyclerView.Adapter<CallViewholder> {
    private Context context;
    private List<DataClass> dataList;


    public callAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public CallViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.callview,parent,false);
        return new CallViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewholder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.image);
        holder.name.setText(dataList.get(position).getDataName());
        holder.work.setText(dataList.get(position).getDataWork());
        holder.add.setText(dataList.get(position).getDataAdd());
        holder.day.setText(dataList.get(position).getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detailActivity.class);
                intent.putExtra("image",dataList.get(holder.getAdapterPosition()).getDataImage() );
                intent.putExtra("name",dataList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("work",dataList.get(holder.getAdapterPosition()).getDataWork());
                intent.putExtra("add",dataList.get(holder.getAdapterPosition()).getDataAdd());
                intent.putExtra("number",dataList.get(holder.getAdapterPosition()).getDataNumber());
                intent.putExtra("key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("userUid",dataList.get(holder.getAdapterPosition()).getUserUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
    class CallViewholder extends RecyclerView.ViewHolder{
    ImageView image;
    TextView name , work, add,day,time;
    CardView cardView;

        public CallViewholder(@NonNull View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.callerImage);
            name = itemView.findViewById(R.id.callerName);
            work = itemView.findViewById(R.id.callerWork);
            add = itemView.findViewById(R.id.callerAddress);
            day = itemView.findViewById(R.id.callerDate);

            cardView = itemView.findViewById(R.id.callView);
        }
    }
