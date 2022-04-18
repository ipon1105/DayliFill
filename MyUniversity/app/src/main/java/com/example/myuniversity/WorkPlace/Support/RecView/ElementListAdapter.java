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
import com.example.myuniversity.WorkPlace.WorkPlace;

import java.util.ArrayList;

public class ElementListAdapter extends RecyclerView.Adapter<ElementListAdapter.ViewHolder> {
    private final String[] times = WorkPlace.workPlaceContext.getResources().getStringArray(R.array.times);
    private ArrayList<Element> list;

    public ElementListAdapter(ArrayList<Element> list){
        Log.i("ElementListAdapter","Start Constructor");

        this.list = list;

        Log.i("ElementListAdapter","Stop Constructor");
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
        holder.num.setText(String.valueOf(list.get(position).getNumber()));
        holder.audit.setText(list.get(position).getAuditIndex());
        holder.pair.setText(list.get(position).getPairIndex());

        holder.timeA.setText(String.valueOf(2 * (position + 1)));
        holder.timeB.setText(String.valueOf(2 * (position + 1) + 1));
        if (position < 8){
            holder.timeA.setText(times[2 * position]);
            holder.timeB.setText(times[2 * position + 1]);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView num;
        public TextView timeA;
        public TextView timeB;
        public TextView pair;
        public TextView audit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            num   = (TextView) itemView.findViewById(R.id.num);
            timeA = (TextView) itemView.findViewById(R.id.timeBegin);
            timeB = (TextView) itemView.findViewById(R.id.timeEnd);
            pair  = (TextView) itemView.findViewById(R.id.pair);
            audit = (TextView) itemView.findViewById(R.id.audit);
        }
    }

    public void clear(){
        list.clear();
    }
}
