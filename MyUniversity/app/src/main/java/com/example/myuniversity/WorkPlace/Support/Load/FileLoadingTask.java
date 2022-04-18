package com.example.myuniversity.WorkPlace.Support.Load;


import android.os.AsyncTask;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileLoadingTask extends AsyncTask<Void, Void, Void> {
    private String url;
    private File destination;
    private FileLoadingListener fileLoadingListener;
    private Throwable throwable;

    public FileLoadingTask(String url, File destination, FileLoadingListener fileLoadingListener) {
        this.url = url;
        this.destination = destination;
        this.fileLoadingListener = fileLoadingListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fileLoadingListener.onBegin();
    }

    @Override
    protected Void doInBackground(Void... params) {
        fileLoadingListener.onBegin();
        try {
            FileUtils.copyURLToFile(new URL(url), destination);
        } catch (IOException e) {
            throwable = e;
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return null;
        }

        fileLoadingListener.onSuccess();
        fileLoadingListener.onEnd();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fileLoadingListener.onEnd();
        if (throwable != null) {
            fileLoadingListener.onFailure(throwable);
        } else {
            fileLoadingListener.onSuccess();
        }
    }
}
