package com.example.myuniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //id кнопки, на которое приходилось нажатие
    int index = R.id.btnFullTime;

    private Downloader onlineData;
    private ListItemsAdapter itemsAdapter;

    private Button btnNext;
    private Button btnFullTime;
    private Button btnPartTime;
    private Button btnSession;
    private RecyclerView list;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineData = new Downloader();
        onlineData.execute("https://www.sevsu.ru/univers/shedule");

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

        btnFullTime = (Button) findViewById(R.id.btnFullTime);
        btnPartTime = (Button) findViewById(R.id.btnPartTime);
        btnSession = (Button) findViewById(R.id.btnSession);
        btnNext = (Button) findViewById(R.id.btnNext);

        list = (RecyclerView) findViewById(R.id.instituteList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        onClick(findViewById(index));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //"https://www.sevsu.ru/univers/shedule", "1"
        //AsyncTask<String, Void, ArrayList<String>> a = new Downloader().execute();
    }

    public void onClick(View view){
        index = view.getId();

        btnFullTime.setTextColor(getResources().getColor((index == R.id.btnFullTime ? R.color.whiteOn : R.color.whiteOff)));
        btnPartTime.setTextColor(getResources().getColor((index == R.id.btnPartTime ? R.color.whiteOn : R.color.whiteOff)));
        btnSession.setTextColor(getResources().getColor((index == R.id.btnSession ? R.color.whiteOn : R.color.whiteOff)));

        if(index == R.id.btnFullTime)
            itemsAdapter = new ListItemsAdapter(this, onlineData.getInstituteFullTime());
        else if (index == R.id.btnPartTime)
            itemsAdapter = new ListItemsAdapter(this, onlineData.getInstitutePartTime());
        else
            itemsAdapter = new ListItemsAdapter(this, onlineData.getInstituteSession());

        if (itemsAdapter != null)
            list.setAdapter(itemsAdapter);
    }

    class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ViewHolder> {
        private ArrayList<String> list;
        private LayoutInflater mInflate;
        private Context context;

        public ListItemsAdapter(Context context, ArrayList<String> list){
            this.mInflate = LayoutInflater.from(context);
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ListItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflate.inflate(R.layout.institute_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemsAdapter.ViewHolder holder, int position) {
            String name = list.get(position);
            if(name != null)
                holder.itemName.setText(name);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView itemName;
            public View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                view = itemView;
                itemName = (TextView) view.findViewById(R.id.item);
            }
        }
    }

}

class Downloader extends AsyncTask<String, Void, ArrayList<String>> {
    private final String instituteBlocks = ".su-column-content";
    private final String instituteNames = ".su-spoiler-title";

    private ArrayList<String> instituteFullTime;
    private ArrayList<String> institutePartTime;
    private ArrayList<String> instituteSession;

    public Downloader(){
        instituteFullTime = new ArrayList<>();
        institutePartTime = new ArrayList<>();
        instituteSession  = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        Document doc;
        Elements contents;

        try {
            doc = Jsoup.connect(strings[0]).get();
            contents = doc.select(instituteBlocks);

            downloadFullTime(contents.get(0));
            downloadPartTime(contents.get(1));
            downloadSession (contents.get(2));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("debug", "exception", e);
        }

        return null;
    }

    //Скачать из блока ДФО
    void downloadFullTime(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteFullTime.add(el.text());
    }

    //Скачать из блока ЗФО
    void downloadPartTime(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            institutePartTime.add(el.text());
    }

    //Скачать из блока Сессия
    void downloadSession(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteSession.add(el.text());
    }

    public ArrayList<String> getInstituteFullTime() {
        return instituteFullTime;
    }

    public ArrayList<String> getInstitutePartTime() {
        return institutePartTime;
    }

    public ArrayList<String> getInstituteSession() {
        return instituteSession;
    }
}