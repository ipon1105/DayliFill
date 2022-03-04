package com.example.myuniversity.WorkPlace;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Day;
import com.example.myuniversity.WorkPlace.Support.Element;
import com.example.myuniversity.WorkPlace.Support.FragmentListAdapter;
import com.example.myuniversity.databinding.FragmentScheduleBinding;

import java.util.ArrayList;

public class Schedule extends Fragment {
    FragmentScheduleBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init(){
        Log.d("debug", "Start init");
        ArrayList<Element> monday = new ArrayList<>();
        monday.add(new Element("Пара 1", "Г512"));
        monday.add(new Element("Пара 2", "Г512"));
        monday.add(new Element("Пара 3", "Г512"));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));

        ArrayList<Element> tuesday = new ArrayList<>();
        tuesday.add(new Element("Пара 1", "Г412"));
        tuesday.add(new Element("Пара 2", "Г412"));
        tuesday.add(new Element("Пара 3", "Г412"));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));


        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day("Понедельник", monday));
        days.add(new Day("Вторник", tuesday));

        binding.fragmentList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.fragmentList.setAdapter(new FragmentListAdapter(days, binding.getRoot().getContext()));
    }
}