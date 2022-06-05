package com.example.myuniversity.WorkPlace.TabContent;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentHomeWorkBinding;

@RequiresApi(api = Build.VERSION_CODES.N)
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    public void init(){
        ((EditText) binding.txtBox).setText(WorkPlace.info.getHomeWork(), TextView.BufferType.EDITABLE);
        ((EditText) binding.txtBox).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                WorkPlace.info.setHomeWork(binding.txtBox.getText().toString());
                return false;
            }
        });
        ((EditText) binding.txtBox).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                WorkPlace.info.setHomeWork(binding.txtBox.getText().toString());
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        WorkPlace.info.setHomeWork(binding.txtBox.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WorkPlace.info.setHomeWork(binding.txtBox.getText().toString());
    }
}