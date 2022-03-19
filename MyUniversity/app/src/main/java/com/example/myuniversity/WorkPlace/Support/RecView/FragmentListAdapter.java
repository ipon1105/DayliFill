package com.example.myuniversity.WorkPlace.Support.RecView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuniversity.R;

import java.util.ArrayList;

public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.ViewHolder> {
    private ArrayList<Day> list;

    private String numbers[];
    private String times[];

    public FragmentListAdapter(ArrayList<Day> list, Context context){
        Log.i(String.valueOf(Log.INFO),"Create new Days with " + String.valueOf(list.size()) +" size;");
        for(int i = 0; i < list.size(); i++){
            Log.i(String.valueOf(Log.INFO),"Day " + list.get(i).getDay() +" have " + list.get(i).getElementList().size() + "elements;");

        }
        this.list = list;

        this.numbers = context.getResources().getStringArray(R.array.numbers);
        this.times = context.getResources().getStringArray(R.array.times);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleName.setText(list.get(position).getDay());
        holder.list.setAdapter(new ElementListAdapter(list.get(position).getElementList(), times));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public FrameLayout titleBox;
        public TextView titleName;
        public RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            titleBox = (FrameLayout) itemView.findViewById(R.id.titleBox);
            titleName = (TextView) itemView.findViewById(R.id.txtTitle);
            list = (RecyclerView) itemView.findViewById(R.id.dayList);

            list.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    public void clear(){
        list.clear();
    }

}
