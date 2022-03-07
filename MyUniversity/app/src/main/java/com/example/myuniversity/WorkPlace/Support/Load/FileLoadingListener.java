package com.example.myuniversity.WorkPlace.Support.Load;

public interface FileLoadingListener {

    void onBegin();

    void onSuccess();

    void onFailure(Throwable cause);

    void onEnd();

}
