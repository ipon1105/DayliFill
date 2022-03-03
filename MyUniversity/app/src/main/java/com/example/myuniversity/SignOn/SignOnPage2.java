package com.example.myuniversity.SignOn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myuniversity.MainActivity;
import com.example.myuniversity.R;
import com.example.myuniversity.SignOn.Support.Downloader;
import com.example.myuniversity.SignOn.Support.ItemClickListener;
import com.example.myuniversity.SignOn.Support.ListItemsAdapter;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentSignOnPage1Binding;
import com.example.myuniversity.databinding.FragmentSignOnPage2Binding;

public class SignOnPage2 extends Fragment {
    private FragmentSignOnPage2Binding binding;
    private ListItemsAdapter itemsAdapter;
    private ItemClickListener listener;
    private Context context;

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

        context = this.requireContext();
        Integer blockNum  = ((Integer) getArguments().get("blockNum"));
        Integer institute = ((Integer) getArguments().get("institute"));

        GridLayoutManager layoutManager = new GridLayoutManager(this.requireContext(), 1);
        binding.list.setLayoutManager(layoutManager);
        binding.list.addItemDecoration(new DividerItemDecoration(this.requireContext(),DividerItemDecoration.VERTICAL));

        binding.btnNext.setEnabled(false);
        binding.btnNext.setTextColor(getResources().getColor(R.color.btn_next_off));

        final NavController controller = (NavController) Navigation.findNavController(view);
        listener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemsAdapter.notifyDataSetChanged();
                binding.btnNext.setEnabled(true);
                binding.btnNext.setTextColor(getResources().getColor(R.color.btn_next_on));
            }
        };
        MainActivity.downloader.setFinish(new Downloader.OnFinish() {
            @Override
            public void ProcessIsFinish() {
                //Загрузка завершена
                itemsAdapter = new ListItemsAdapter(context, MainActivity.downloader.getGroupList());
                itemsAdapter.setClickListener(listener);
                binding.list.setAdapter(itemsAdapter);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.downloader.downloadGroup(blockNum, institute);
            }
        }).start();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemsAdapter != null) itemsAdapter.clear();
                controller.navigate(R.id.action_signOnPage2_to_signOnPage1);
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WorkPlace.class);
                startActivity(intent);
            }
        });
    }


}