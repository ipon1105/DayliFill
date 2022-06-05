package com.example.myuniversity.WorkPlace.TabContent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingList;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingTask;
import com.example.myuniversity.WorkPlace.Support.Load.ItemClickListener;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentSettingBinding;

import java.io.File;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Setting extends Fragment {
    MyRecyclerViewAdapter myRecyclerViewAdapter_1;

    FragmentSettingBinding binding;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = this.getContext();

        if (WorkPlace.manager != null)
            WorkPlace.manager.startParser();

        initContentList();
    }

    //Функция инициализации первого списка
    private void initContentList(){
        myRecyclerViewAdapter_1 = new MyRecyclerViewAdapter( context, WorkPlace.info.getCourceList(), 0);

        myRecyclerViewAdapter_1.setClickListener(new ItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                WorkPlace.info.setCourceIndex(position);
                myRecyclerViewAdapter_1.notifyDataSetChanged();

                if (!WorkPlace.hasConnection(context))
                    Toast.makeText(context, "Нет интернета для загрузки расписания", Toast.LENGTH_SHORT).show();
                else {
                    String filename = new File(WorkPlace.info.getUrlList().get(position)).getName();
                    new FileLoadingTask(
                            WorkPlace.info.getWebSiteStr(),
                            WorkPlace.info.getFilePath(filename),
                            new FileLoadingList() {
                                @Override
                                public void Success() {
                                    Log.i("Setting", "Success load");

                                    WorkPlace.info.setFileName(filename);
                                    WorkPlace.initExcelManager();

                                    ((Activity) WorkPlace.workPlaceContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WorkPlace.workPlaceContext, "Файл успешно загружен.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void Faild(Exception exception) {
                                    Log.e("Setting:FileLoadingTask", "Failed load: ", exception);
                                    ((Activity) WorkPlace.workPlaceContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WorkPlace.workPlaceContext, "Ошибка. Не получилось загрузить файл.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                    ).execute();
                }

            }
        });

        binding.contentList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.contentList.setAdapter(myRecyclerViewAdapter_1);
    }
/*
    //Функция инициализации третьего списка
    private void initSheetList() {
        if (WorkPlace.info.getContentIndex() != -1) {

            myRecyclerViewAdapter_3 = new MyRecyclerViewAdapter(
                    context,
                    WorkPlace.manager.getSheetNameList(),
                    2
            );

            myRecyclerViewAdapter_3.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    WorkPlace.info.setSheet(position);
                    myRecyclerViewAdapter_3.notifyDataSetChanged();
                }
            });

            binding.sheetList.setLayoutManager(new LinearLayoutManager(context));
            binding.sheetList.setAdapter(myRecyclerViewAdapter_3);
        }
        if (myRecyclerViewAdapter_3 != null)
            myRecyclerViewAdapter_3.notifyDataSetChanged();
    }

    //Функция инициализации второго списка
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initGroupList(){
        if (WorkPlace.info.getContentIndex() != -1) {

            if (WorkPlace.manager != null)
                WorkPlace.manager.startParser();

            myRecyclerViewAdapter_2 = new MyRecyclerViewAdapter(
                    context,
                    WorkPlace.manager.getGroupListName(0),
                    1
            );

            myRecyclerViewAdapter_2.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    WorkPlace.info.setGroup(position);
                    myRecyclerViewAdapter_2.notifyDataSetChanged();
                }
            });

            binding.groupList.setLayoutManager(new LinearLayoutManager(context));
            binding.groupList.setAdapter(myRecyclerViewAdapter_2);
        }
        if (myRecyclerViewAdapter_2 != null)
            myRecyclerViewAdapter_2.notifyDataSetChanged();
    }
*/



}

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int a;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<String> data, int a) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.a = a;
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

        switch (a){
            case 0:
                if (WorkPlace.info.getCourceIndex() == position)
                    holder.myTextView.setTypeface(null, Typeface.BOLD);
                break;
            case 1:
                if (WorkPlace.info.getGroup() == position)
                    holder.myTextView.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                if (WorkPlace.info.getSheet() == position)
                    holder.myTextView.setTypeface(null, Typeface.BOLD);
                break;
        }
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
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
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


}