package com.google.firebase.codelab.mlkit.interfaces;

import android.net.Uri;
import android.view.View;

public interface ImageUtilsCallback<T extends View> {
    void onPicked(T t, Uri uri);
}
