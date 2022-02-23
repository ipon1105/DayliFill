package com.example.myuniversity.SignOn.Support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuniversity.R;

import java.util.ArrayList;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ViewHolder> {
    public ArrayList<Model> list;
    private LayoutInflater mInflate;
    private Context context;

    private ItemClickListener listener;

    public ListItemsAdapter(Context context, ArrayList<String> list){
        this.mInflate = LayoutInflater.from(context);
        this.context = context;

        this.list = new ArrayList<>();
        for(String s : list)
            this.list.add(new Model(s));
    }

    @NonNull
    @Override
    public ListItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.institute_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemName.setText(list.get(position).name);
        holder.itemName.setTextColor(context.getResources().getColor(list.get(position).set ? R.color.full_green : R.color.white));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Model m : list)
                    m.set = false;

                list.get(position).set = true;
                if (listener != null)
                    listener.onItemClick(holder.itemName, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            itemName = (TextView) view.findViewById(R.id.item);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

}
