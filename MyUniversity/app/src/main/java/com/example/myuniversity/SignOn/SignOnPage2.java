package com.example.myuniversity.SignOn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myuniversity.MainActivity;
import com.example.myuniversity.R;
import com.example.myuniversity.SignOn.Support.Downloader;
import com.example.myuniversity.SignOn.Support.ItemClickListener;
import com.example.myuniversity.SignOn.Support.ListItemsAdapter;
import com.example.myuniversity.databinding.FragmentSignOnPage1Binding;
import com.example.myuniversity.databinding.FragmentSignOnPage2Binding;

public class SignOnPage2 extends Fragment {
    private FragmentSignOnPage2Binding binding;
    private ListItemsAdapter itemsAdapter;
    private ItemClickListener listener;

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
        binding.btnNext.setEnabled(false);

        final NavController controller = (NavController) Navigation.findNavController(view);

        listener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemsAdapter.notifyDataSetChanged();
                binding.btnNext.setEnabled(true);
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);

        binding.list.setLayoutManager(layoutManager);
        binding.list.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));
        //binding.list.setAdapter(MainActivity.downloader.);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_signOnPage2_to_signOnPage1);
            }
        });
        //controller.navigate(R.id.action_signOnPage2_to_signOnPage1);
    }
}