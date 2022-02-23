package com.example.myuniversity.SignOn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myuniversity.R;
import com.example.myuniversity.databinding.FragmentSignOnPage1Binding;
import com.example.myuniversity.databinding.FragmentSignOnPage2Binding;

public class SignOnPage2 extends Fragment {
    private FragmentSignOnPage2Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignOnPage2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController controller = (NavController) Navigation.findNavController(view);

        //controller.navigate(R.id.action_signOnPage2_to_signOnPage1);
    }
}