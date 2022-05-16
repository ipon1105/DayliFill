package com.example.myuniversity.WorkPlace.Support.RecView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Load.ItemClickListener;

import java.util.ArrayList;

public class SheetElementAdapter extends RecyclerView.Adapter<SheetElementAdapter.ViewHolder>{

    private ArrayList<String> strList;
    private ItemClickListener mClickListener;

    public SheetElementAdapter(ArrayList<String> strList){
        this.strList = strList;
    }

    public void setClickListener(ItemClickListener mClickListener){
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public SheetElementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_sheet_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SheetElementAdapter.ViewHolder holder, int position) {
        holder.btn.setText(strList.get(position));
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }

    public void clear(){
        strList.clear();
        strList = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }


}
