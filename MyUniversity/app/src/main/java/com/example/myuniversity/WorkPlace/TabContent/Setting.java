package com.example.myuniversity.WorkPlace.TabContent;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentSettingBinding;

import java.util.ArrayList;

public class Setting extends Fragment {
    FragmentSettingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    //Инициализация
    private void init(){
        Context context = this.getContext();
        MyRecyclerViewAdapter myRecyclerViewAdapter_1 = new MyRecyclerViewAdapter(this.getContext(), WorkPlace.info.getContentList());

        myRecyclerViewAdapter_1.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                WorkPlace.info.setContentIndex(position);
                myRecyclerViewAdapter_1.notifyDataSetChanged();

                if (!WorkPlace.hasConnection(context))
                    Toast.makeText(context, "Нет интернета для загрузки расписания", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Начинаю загрузку расписания с сайта", Toast.LENGTH_SHORT).show();

                MyRecyclerViewAdapter myRecyclerViewAdapter_2 = new MyRecyclerViewAdapter(
                        context,
                        WorkPlace.info.getDirectoryList(myRecyclerViewAdapter_1.getItem(position))
                );

                binding.groupList.setLayoutManager(new LinearLayoutManager(context));
                binding.groupList.setAdapter(myRecyclerViewAdapter_2);
            }
        });

        binding.contentList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.contentList.setAdapter(myRecyclerViewAdapter_1);
    }
}

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
        holder.myTextView.setTypeface(null, Typeface.NORMAL);

        if (WorkPlace.info.getContentIndex() == position)
            holder.myTextView.setTypeface(null, Typeface.BOLD);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}