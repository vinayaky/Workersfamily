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

import java.util.ArrayList;
import java.util.List;


public class workerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;

    public workerAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.image);
        holder.name.setText(dataList.get(position).getDataName());
        holder.work.setText(dataList.get(position).getDataWork());
        holder.add.setText(dataList.get(position).getDataAdd());

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
    public  void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
    class  MyViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    TextView name , work, add;
    CardView cardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        image= itemView.findViewById(R.id.workerImage);
        name = itemView.findViewById(R.id.workerName);
        work = itemView.findViewById(R.id.workerWork);
        add = itemView.findViewById(R.id.workerAddress);
        cardView = itemView.findViewById(R.id.cardView);


    }
}
