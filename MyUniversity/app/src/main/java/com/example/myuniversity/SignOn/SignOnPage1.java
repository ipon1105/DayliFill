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

import com.example.myuniversity.R;
import com.example.myuniversity.SignOn.Support.Downloader;
import com.example.myuniversity.SignOn.Support.ItemClickListener;
import com.example.myuniversity.SignOn.Support.ListItemsAdapter;
import com.example.myuniversity.databinding.FragmentSignOnPage1Binding;

public class SignOnPage1 extends Fragment {

    private FragmentSignOnPage1Binding binding;
    private ListItemsAdapter itemsAdapter;
    private ItemClickListener listener;
    private Downloader onlineData;
    private int index = R.id.btnFullTime;

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
        onlineData = new Downloader();
        onlineData.execute("https://www.sevsu.ru/univers/shedule");
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

        binding.btnNext.setEnabled(false);

        onlineData.setFinish(new Downloader.OnFinish() {
            @Override
            public void ProcessIsFinish() {

                if(index == R.id.btnFullTime)
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), onlineData.getInstituteFullTime());
                else if (index == R.id.btnPartTime)
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), onlineData.getInstitutePartTime());
                else
                    itemsAdapter = new ListItemsAdapter(binding.getRoot().getContext(), onlineData.getInstituteSession());

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
        //controller.navigate(R.id.action_signOnPage1_to_signOnPage2);
    }

    public void myClick(View view){
        binding.btnNext.setEnabled(false);
        if(view != null)
            index = view.getId();

        binding.btnFullTime.setTextColor(getResources().getColor((index == R.id.btnFullTime ? R.color.whiteOn : R.color.whiteOff)));
        binding.btnPartTime.setTextColor(getResources().getColor((index == R.id.btnPartTime ? R.color.whiteOn : R.color.whiteOff)));
        binding.btnSession.setTextColor(getResources().getColor((index == R.id.btnSession ? R.color.whiteOn : R.color.whiteOff)));

        if(index == R.id.btnFullTime)
            itemsAdapter = new ListItemsAdapter(this.getContext(), onlineData.getInstituteFullTime());
        else if (index == R.id.btnPartTime)
            itemsAdapter = new ListItemsAdapter(this.getContext(), onlineData.getInstitutePartTime());
        else
            itemsAdapter = new ListItemsAdapter(this.getContext(), onlineData.getInstituteSession());

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