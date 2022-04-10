package com.example.myuniversity.WorkPlace.TabContent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myuniversity.R;
import com.example.myuniversity.databinding.FragmentHomeWorkBinding;

public class HomeWork extends Fragment {
    FragmentHomeWorkBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeWorkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}