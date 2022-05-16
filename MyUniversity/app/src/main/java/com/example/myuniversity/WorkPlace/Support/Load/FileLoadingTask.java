package com.example.myuniversity.WorkPlace.Support.Load;


import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileLoadingTask extends AsyncTask<Void, Void, Void> {
    private String path;
    private File destination;
    private FileLoadingList fileLoadingListener;

    public FileLoadingTask(String path, File destination, FileLoadingList fileLoadingListener) {
        this.path = path;
        this.destination = destination;
        this.fileLoadingListener = fileLoadingListener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            FileUtils.copyURLToFile(new URL(path), destination);
        } catch (IOException e) {
            Log.e("Setting:FileLoadingTask", "Failed load: ", e);
            return null;
        }

        fileLoadingListener.Success();
        return null;
    }
}
