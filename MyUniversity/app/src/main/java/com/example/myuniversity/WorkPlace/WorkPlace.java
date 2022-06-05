package com.example.myuniversity.WorkPlace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Downloader;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Fresh;
import com.example.myuniversity.WorkPlace.Support.Info;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
import com.example.myuniversity.databinding.WelcomBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class WorkPlace extends AppCompatActivity implements Fresh {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static FileLoadingListener excelManager_FileLoadingListener;

    private TabLayout.OnTabSelectedListener tabList_OnTabSelectedListener;
    private View.OnClickListener btnNext_OnClickListener;

    public static Context workPlaceContext;
    public static ExcelManager manager;
    public static Info info;

    private NavController nav;
    private Downloader downloader;

    private ActivityWorkPlaceBinding binding;
    private WelcomBinding welcomBinding;

    private ArrayAdapter<String> spinnerAadapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        general_init();

        if (info.isFirstStart()){
            setContentView(welcomBinding.getRoot());
            welcomBinding.btnNext.setOnClickListener(btnNext_OnClickListener);
            loadData();

            return;
        }

        setContentView(binding.getRoot());
        init();
    }

    private void initListeners(){
        excelManager_FileLoadingListener = new FileLoadingListener() {
            @Override
            public void onBegin() {
                Log.i("ExcelManager", "Begin Excel Manager parser.");
            }
            @Override
            public void onSuccess() {
                Log.i("ExcelManager", "Success Excel Manager parser.");


                spinnerAadapter = new ArrayAdapter<>(
                    workPlaceContext,
                        R.layout.spinner_list,
                    WorkPlace.manager.getGroupListName(WorkPlace.info.getSheet())
                );
                binding.groupSpinner.setAdapter(spinnerAadapter);
                binding.groupSpinner.setSelection(info.getGroup());
                binding.groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        WorkPlace.info.setGroup(position);

                        //nav.navigate(R.id.action_homeWork_to_schedule);
                        //nav.navigate(R.id.action_setting_to_schedule);
                        //nav.navigate(R.id.action_setting_to_homeWork);
                        //nav.navigate(R.id.schedule_0_1);
                        //nav.navigate(R.id.schedule_0_2);
                        //nav.navigate(R.id.action_homeWork_to_setting);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                runOnUiThread(()-> {
                   spinnerAadapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onFailure(Throwable cause) {
                Log.e("ExcelManager", "Failed Excel Manager parser: " + cause);
            }
            @Override
            public void onEnd() {
                Log.i("ExcelManager", "End Excel Manager parser.");
            }
        };
        tabList_OnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            int lastPos = -1;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (lastPos == -1)
                    lastPos = 0;

                try {
                    switch (tab.getPosition()){
                        case 0:
                            if (lastPos == 1)
                                nav.navigate(R.id.action_homeWork_to_schedule);
                            else
                                nav.navigate(R.id.action_setting_to_schedule);
                            break;
                        case 1:
                            if (lastPos == 2)
                                nav.navigate(R.id.action_setting_to_homeWork);
                            else
                                nav.navigate(R.id.schedule_0_1);
                            break;
                        case 2:
                            if (lastPos == 0)
                                nav.navigate(R.id.schedule_0_2);
                            else
                                nav.navigate(R.id.action_homeWork_to_setting);
                            break;

                    }
                    lastPos = tab.getPosition();
                } catch (Exception e){
                    Log.e("WorkPlace", "Error: " + e);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        btnNext_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions((WorkPlace) workPlaceContext);

                if (info.isFirstStart())
                    loadData();
                else {
                    setContentView(binding.getRoot());
                    init();
                }
            }
        };
    }

    //Общая инициализация
    private void general_init(){
        fullscreen();
        workPlaceContext = this;

        welcomBinding = WelcomBinding.inflate(getLayoutInflater());
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());

        info = new Info(workPlaceContext);

        initExcelManager();
        initListeners();
    }

    //Данный участок отвечает за инициализацию переменной для загрузки данных с сайта
    public void loadData(){
        Log.i("WorkPlace", "Begin setManager");

        if (!hasConnection(workPlaceContext)) {
            Toast.makeText(workPlaceContext, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
            Log.i("WorkPlace", "Internet connection not exist");

            return;
        }


        downloader = (Downloader) new Downloader(new FileLoadingListener() {
            @Override
            public void onBegin() {
                Log.i("WorkPlace", "Begin FileLoadingListener");

                verifyStoragePermissions((WorkPlace) workPlaceContext);

                runOnUiThread(() -> {
                    welcomBinding.btnNext.setEnabled(false);
                    welcomBinding.btnNext.setVisibility(View.INVISIBLE);

                    welcomBinding.progressBar.setEnabled(true);
                    welcomBinding.progressBar.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onSuccess() {
                Log.i("WorkPlace", "Success FileLoadingListener");

                if (!verifyStoragePermissions((WorkPlace) workPlaceContext)) {
                    Log.i("WorkPlace", "No permissions");

                    runOnUiThread(() -> {
                        welcomBinding.btnNext.setEnabled(true);
                        welcomBinding.btnNext.setVisibility(View.VISIBLE);

                        welcomBinding.progressBar.setEnabled(false);
                        welcomBinding.progressBar.setVisibility(View.INVISIBLE);

                        welcomBinding.btnNext.setText(getResources().getString(R.string.btnAgainPage2));
                    });

                    info.setFirstStart(true);
                    return;
                }

                runOnUiThread(() -> {
                    welcomBinding.btnNext.setEnabled(true);
                    welcomBinding.btnNext.setVisibility(View.VISIBLE);

                    welcomBinding.progressBar.setEnabled(false);
                    welcomBinding.progressBar.setVisibility(View.INVISIBLE);

                    welcomBinding.btnNext.setText(getResources().getString(R.string.btnNextPage2));
                });

                info.setFirstStart(false);
                info.setUrlList(downloader.getUrlList());
                info.setCourceList(downloader.getContentList());

                initExcelManager();
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.e("WorkPlace", "Exception FileLoadingListener: ",  cause);

                runOnUiThread(() -> {
                    welcomBinding.btnNext.setEnabled(true);
                    welcomBinding.btnNext.setVisibility(View.VISIBLE);

                    welcomBinding.progressBar.setEnabled(false);
                    welcomBinding.progressBar.setVisibility(View.INVISIBLE);

                    welcomBinding.btnNext.setText(getResources().getString(R.string.btnAgainPage2));
                });

                info.setFirstStart(true);
            }

            @Override
            public void onEnd() {
                Log.i("WorkPlace", "End FileLoadingListener");
            }
        });

        Log.i("WorkPlace", "Downloader execute");
        downloader.execute();

        Log.i("WorkPlace", "End setManager");
    }

    //Функция инициализации данных
    public static void initExcelManager() {
        //Если не выбран курс в настройках, то Путь к файлу будет null
        if (info.getFilePath() == null)
            return;

        manager = new ExcelManager(
            info.getFilePath(),
            excelManager_FileLoadingListener
        );
    }

    //Инициализация
    private void init(){
        initListeners();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.box);

        if (navHostFragment != null)
            nav = navHostFragment.getNavController();

        binding.tabList.setOnTabSelectedListener(tabList_OnTabSelectedListener);
        WorkPlace.verifyStoragePermissions((WorkPlace) workPlaceContext);

        generalUpdate();

        if (WorkPlace.manager == null)
            return;
        ArrayList<String> strList = WorkPlace.manager.getGroupListName(WorkPlace.info.getSheet());

        spinnerAadapter = new ArrayAdapter<>
                (this,
                        R.layout.spinner_list,
                        strList);

        if (strList == null)
            return;

        binding.groupSpinner.setAdapter(spinnerAadapter);

        binding.groupSpinner.setSelection(WorkPlace.info.getGroup());
        binding.groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WorkPlace.info.setGroup(position);

                    //nav.navigate(R.id.action_homeWork_to_schedule);
                    //nav.navigate(R.id.action_setting_to_schedule);
                    //nav.navigate(R.id.action_setting_to_homeWork);
                    //nav.navigate(R.id.schedule_0_1);
                    //nav.navigate(R.id.schedule_0_2);
                    //nav.navigate(R.id.action_homeWork_to_setting);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.groupSpinner.setSelection(info.getGroup());
    }

    //Попытаться обновить данные
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generalUpdate(){
        if (!hasConnection(workPlaceContext)) {
            Toast.makeText(workPlaceContext, "Невозможно подключиться к интернету.", Toast.LENGTH_SHORT).show();
            return;
        }

        initExcelManager();
        loadData();
    }

    //Проверить доступ к файловому мессенджеру
    public static boolean verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
    }

    //Настройка окна
    private void fullscreen(){
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        //Запрещаю вертерь экран
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Проверка соендинения
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;

        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;

        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }

    @Override
    public void update() {
        if (WorkPlace.manager == null)
            initExcelManager();

        if (WorkPlace.manager == null)
            return;

        if (spinnerAadapter != null)
            spinnerAadapter.clear();

        spinnerAadapter = new ArrayAdapter<>
                (this,
                        R.layout.spinner_list,
                        WorkPlace.manager.getGroupListName(WorkPlace.info.getSheet())
                );

        binding.groupSpinner.setAdapter(spinnerAadapter);
        spinnerAadapter.notifyDataSetChanged();
    }
}

