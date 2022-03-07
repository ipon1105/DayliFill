package com.example.myuniversity.WorkPlace.Support.RecView;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuniversity.R;

import java.util.ArrayList;

public class ElementListAdapter extends RecyclerView.Adapter<ElementListAdapter.ViewHolder> {
    private ArrayList<Element> list;
    private String numbers[];
    private String times[];

    public ElementListAdapter(ArrayList<Element> list, String numbers[], String times[]){
        Log.i(String.valueOf(Log.INFO),"Create new Elements with " + String.valueOf(list.size()) +" size;");
        this.list = list;
        this.numbers = numbers;
        this.times   = times;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_pair, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.num.setText(numbers[position]);
        holder.timeA.setText(times[2 * position]);
        holder.timeB.setText(times[2 * position + 1]);
        holder.pair.setText(list.get(position).getPairIndex());
        holder.audit.setText(list.get(position).getAuditIndex());

        int color = ((position % 2 == 0) ? R.color.backOtt : R.color.backEven);

        holder.numBox.setBackgroundColor(holder.view.getContext().getColor(color));
        holder.timeBox.setBackgroundColor(holder.view.getContext().getColor(color));
        holder.auditBox.setBackgroundColor(holder.view.getContext().getColor(color));
        holder.pairBox.setBackgroundColor(holder.view.getContext().getColor(color));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public View view;

        public TextView num;
        public TextView timeA;
        public TextView timeB;
        public TextView pair;
        public TextView audit;

        public ConstraintLayout numBox;
        public ConstraintLayout timeBox;
        public ConstraintLayout pairBox;
        public ConstraintLayout auditBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            num   = (TextView) itemView.findViewById(R.id.num);
            timeA = (TextView) itemView.findViewById(R.id.timeBegin);
            timeB = (TextView) itemView.findViewById(R.id.timeEnd);
            pair  = (TextView) itemView.findViewById(R.id.pair);
            audit = (TextView) itemView.findViewById(R.id.audit);

            numBox  = (ConstraintLayout) itemView.findViewById(R.id.numBox);
            timeBox = (ConstraintLayout) itemView.findViewById(R.id.timeBox);
            pairBox = (ConstraintLayout) itemView.findViewById(R.id.pairBox);
            auditBox= (ConstraintLayout) itemView.findViewById(R.id.auditBox);

        }
    }

    public void clear(){
        list.clear();
    }
}
