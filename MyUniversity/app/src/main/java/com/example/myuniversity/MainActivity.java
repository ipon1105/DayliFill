package com.example.myuniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner courseSpinner = (Spinner) findViewById(R.id.courceSpinner);
        Spinner universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        Spinner groupSpinner = (Spinner) findViewById(R.id.groupSpinner);

        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(this,R.array.courseList, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(this,R.array.universityList, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this,R.array.groupList, android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setAdapter(courseAdapter);
        universitySpinner.setAdapter(universityAdapter);
        groupSpinner.setAdapter(groupAdapter);

    }
}