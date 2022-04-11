package com.example.myuniversity.WorkPlace.TabContent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.WorkPlace;
import com.example.myuniversity.databinding.FragmentScheduleBinding;

import java.io.FileNotFoundException;

public class Schedule extends Fragment {
    private FragmentScheduleBinding binding;
    private ExcelManager manager;

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

    private void init() {
        binding.groupId.setChecked(WorkPlace.info.getGroup() == 0 ? true : false);
        if (WorkPlace.info.getGroup() == 0 ? true : false)
            binding.groupId.setText(getResources().getText(R.string.group_2));
        else
            binding.groupId.setText(getResources().getText(R.string.group_1));

        binding.groupId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                WorkPlace.info.setGroup(b ? 0 : 1);
                if (WorkPlace.info.getGroup() == 0 ? true : false)
                    binding.groupId.setText(getResources().getText(R.string.group_2));
                else
                    binding.groupId.setText(getResources().getText(R.string.group_1));
            }
        });

        if (WorkPlace.info.getFilePath() != null){
            ExcelManager manager = new ExcelManager(
                WorkPlace.info.getFilePath(),
                new FileLoadingListener() {
                    @Override
                    public void onBegin() {
                        Log.i("ExcelManager", "Begin Excel Manager parser.");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i("ExcelManager", "Success Excel Manager parser.");
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        Log.e("ExcelManager", "Failed Excel Manager parser: " + cause);
                    }

                    @Override
                    public void onEnd() {
                        Log.i("ExcelManager", "End Excel Manager parser.");
                    }
                }
            );

            manager.startParser();
        }

        if (WorkPlace.info.getContentIndex() == -1 || WorkPlace.info.getGroupIndex() == -1){
            binding.txtNonContent.setVisibility(View.VISIBLE);
            return;
        }
        binding.txtNonContent.setVisibility(View.INVISIBLE);

        //
        //try {
        //    manager = (ExcelManager) getArguments().getSerializable("manager");
//
        //    binding.fragmentList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        //    binding.fragmentList.setAdapter(new FragmentListAdapter(manager.getDays(), binding.getRoot().getContext()));
        //}catch (Exception e){
        //    Log.d("Schedule", "Error: ", e);
        //}

        /*
        ArrayList<Element> monday = new ArrayList<>();
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("ПУЛ", ""));
        monday.add(new Element("ПУЛ", ""));
        monday.add(new Element("ПУЛ", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));
        monday.add(new Element("", ""));

        ArrayList<Element> tuesday = new ArrayList<>();
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));
        tuesday.add(new Element("", ""));

        ArrayList<Element> wednesday = new ArrayList<>();
        wednesday.add(new Element("Английский", "А101"));
        wednesday.add(new Element("Английский", "А001"));
        wednesday.add(new Element("", ""));
        wednesday.add(new Element("", ""));
        wednesday.add(new Element("Теория алгоритмов", "Л"));
        wednesday.add(new Element("Технология создания программных продуктов",  "Л"));
        wednesday.add(new Element("", ""));
        wednesday.add(new Element("", ""));

        ArrayList<Element> thursday = new ArrayList<>();
        thursday.add(new Element("Теория алгоритмов", "Г512"));
        thursday.add(new Element("Численные методы", "Г512"));
        thursday.add(new Element("", ""));
        thursday.add(new Element("", ""));
        thursday.add(new Element("", ""));
        thursday.add(new Element("",  ""));
        thursday.add(new Element("", ""));
        thursday.add(new Element("", ""));

        ArrayList<Element> friday = new ArrayList<>();
        friday.add(new Element("", ""));
        friday.add(new Element("Графический дизайн", "Б507"));
        friday.add(new Element("Русский язык делового общения", "В302"));
        friday.add(new Element("Управление данными", "Г511"));
        friday.add(new Element("", ""));
        friday.add(new Element("", ""));
        friday.add(new Element("", ""));
        friday.add(new Element("", ""));

        ArrayList<Element> saturday = new ArrayList<>();
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));
        saturday.add(new Element("", ""));

        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day("Понедельник", monday));
        days.add(new Day("Вторник", tuesday));
        days.add(new Day("Среда", wednesday));
        days.add(new Day("Четверг", thursday));
        days.add(new Day("Пятница", friday));
        days.add(new Day("Суббота", saturday));
        */
    }
}