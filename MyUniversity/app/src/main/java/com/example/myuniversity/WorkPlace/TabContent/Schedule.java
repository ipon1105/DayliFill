package com.example.myuniversity.WorkPlace.TabContent;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.Support.RecView.Day;
import com.example.myuniversity.WorkPlace.Support.RecView.FragmentListAdapter;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentScheduleBinding;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Schedule extends Fragment {
    private FragmentScheduleBinding binding;
    private FragmentListAdapter adapter;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        context = this.getContext();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        ArrayList<Day> days = null;

        binding.groupId.setChecked(WorkPlace.info.getGroupId());
        if (WorkPlace.info.getGroupId())
            binding.groupId.setText(getResources().getText(R.string.group_1));
        else
            binding.groupId.setText(getResources().getText(R.string.group_2));

        binding.groupId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                WorkPlace.info.setGroupId(b);

                if (WorkPlace.info.getGroupId())
                    binding.groupId.setText(getResources().getText(R.string.group_1));
                else
                    binding.groupId.setText(getResources().getText(R.string.group_2));

                if (WorkPlace.manager != null) {
                    adapter.clear();
                    adapter = new FragmentListAdapter(WorkPlace.manager.getDays(WorkPlace.info.getSheet()), context);
                    binding.fragmentList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        if (WorkPlace.info.getContentIndex() == -1){
            binding.txtNonContent.setVisibility(View.VISIBLE);
            return;
        }
        binding.txtNonContent.setVisibility(View.INVISIBLE);

        WorkPlace.initExcel();

        WorkPlace.manager.startParser();
        days =  WorkPlace.manager.getDays(WorkPlace.info.getSheet());

        if (days == null)
            return;

        adapter = new FragmentListAdapter(days, this.getContext());

        binding.fragmentList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.fragmentList.setAdapter(adapter);
    }
}