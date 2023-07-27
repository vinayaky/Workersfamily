package com.example.workersfamily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class feedbackAdapter extends RecyclerView.Adapter<myViewHolder> {
    private Context context;
    private List<FeedbackClass> dataList;

    public feedbackAdapter(Context context, List<FeedbackClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbackcard,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.feedbackView.setText(dataList.get(position).getFeedbackData());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
    class myViewHolder extends RecyclerView.ViewHolder{
    TextView feedbackView;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        feedbackView = itemView.findViewById(R.id.feedbackView);
    }
}
