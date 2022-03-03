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

import com.example.myuniversity.MainActivity;
import com.example.myuniversity.R;
import com.example.myuniversity.SignOn.Support.Downloader;
import com.example.myuniversity.SignOn.Support.ItemClickListener;
import com.example.myuniversity.SignOn.Support.ListItemsAdapter;
import com.example.myuniversity.databinding.FragmentSignOnPage1Binding;

public class SignOnPage1 extends Fragment {

    private FragmentSignOnPage1Binding binding;
    private ListItemsAdapter itemsAdapter;
    private ItemClickListener listener;
    private int index = R.id.btnFullTime;
    private int instituteIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignOnPage1Binding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController controller = (NavController) Navigation.findNavController(view);
        listener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                instituteIndex = position;
                itemsAdapter.notifyDataSetChanged();
                binding.btnNext.setEnabled(true);
                binding.btnNext.setTextColor(getResources().getColor(R.color.btn_next_on));
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);

        binding.list.setLayoutManager(layoutManager);
        binding.list.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));

        binding.btnNext.setEnabled(false);
        binding.btnNext.setTextColor(getResources().getColor(R.color.btn_next_off));

        MainActivity.downloader.setFinish(new Downloader.OnFinish() {
            @Override
            public void ProcessIsFinish() {

                if(index == R.id.btnFullTime)
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), MainActivity.downloader.getInstituteFullTime());
                else if (index == R.id.btnPartTime)
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), MainActivity.downloader.getInstitutePartTime());
                else
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), MainActivity.downloader.getInstituteSession());

                itemsAdapter.setClickListener(listener);

                if (itemsAdapter.list.size() != 0){
                    binding.list.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.list.setAdapter(itemsAdapter);
                            binding.bar.setVisibility(View.INVISIBLE);
                            binding.bar.setEnabled(false);
                        }
                    });

                } else {
                    binding.bar.setVisibility(View.VISIBLE);
                    binding.bar.setEnabled(true);
                }
            }
        });

        View.OnClickListener myClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(view);
            }
        };
        binding.btnFullTime.setOnClickListener(myClick);
        binding.btnPartTime.setOnClickListener(myClick);
        binding.btnSession.setOnClickListener(myClick);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int block = (index == R.id.btnFullTime ? 0 : (index == R.id.btnPartTime ? 1 : 2));
                Bundle bundle = new Bundle();

                bundle.putInt("blockNum", block);
                bundle.putInt("institute", instituteIndex);

                controller.navigate(R.id.action_signOnPage1_to_signOnPage2, bundle);
            }
        });
        myClick(null);

    }

    public void myClick(View view){
        binding.btnNext.setEnabled(false);
        binding.btnNext.setTextColor(getResources().getColor(R.color.btn_next_off));

        if(view != null)
            index = view.getId();

        binding.btnFullTime.setTextColor(getResources().getColor((index == R.id.btnFullTime ? R.color.toggle_on : R.color.toggle_off)));
        binding.btnPartTime.setTextColor(getResources().getColor((index == R.id.btnPartTime ? R.color.toggle_on : R.color.toggle_off)));
        binding.btnSession.setTextColor(getResources().getColor((index == R.id.btnSession ? R.color.toggle_on : R.color.toggle_off)));

        if(index == R.id.btnFullTime)
            itemsAdapter = new ListItemsAdapter(this.getContext(), MainActivity.downloader.getInstituteFullTime());
        else if (index == R.id.btnPartTime)
            itemsAdapter = new ListItemsAdapter(this.getContext(), MainActivity.downloader.getInstitutePartTime());
        else
            itemsAdapter = new ListItemsAdapter(this.getContext(), MainActivity.downloader.getInstituteSession());

        itemsAdapter.setClickListener(listener);

        if (itemsAdapter.list.size() != 0){
            binding.list.setAdapter(itemsAdapter);
            binding.bar.setVisibility(View.INVISIBLE);
            binding.bar.setEnabled(false);
        } else {
            binding.bar.setVisibility(View.VISIBLE);
            binding.bar.setEnabled(true);
        }
    }
}