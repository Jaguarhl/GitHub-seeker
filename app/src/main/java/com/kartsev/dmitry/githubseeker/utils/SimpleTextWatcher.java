package com.kartsev.dmitry.githubseeker.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class SimpleTextWatcher implements TextWatcher {

    private CallBackAfterTextChanged callBackAfterTextChanged;

    public interface CallBackAfterTextChanged {
        void afterTextChanged(Editable s);
    }

    public SimpleTextWatcher(CallBackAfterTextChanged callBackAfterTextChanged) {
        this.callBackAfterTextChanged = callBackAfterTextChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        callBackAfterTextChanged.afterTextChanged(s);
    }
}
