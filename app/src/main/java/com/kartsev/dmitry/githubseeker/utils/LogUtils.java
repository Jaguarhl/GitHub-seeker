package com.kartsev.dmitry.githubseeker.utils;

import android.support.v4.util.ArraySet;
import android.util.Log;

import com.kartsev.dmitry.githubseeker.BuildConfig;

import java.util.Set;

public class LogUtils {
    private static Set<String> allowedTags = new ArraySet<>();

    public static void setAsAllowedTag(String tag) {
        if (BuildConfig.DEBUG) {
            allowedTags.add(tag);
        }
    }

    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG && allowedTags.contains(tag)) {
            Log.d(tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (BuildConfig.DEBUG && allowedTags.contains(tag)) {
            Log.v(tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (BuildConfig.DEBUG && allowedTags.contains(tag)) {
            Log.i(tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (BuildConfig.DEBUG && allowedTags.contains(tag)) {
            Log.w(tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (BuildConfig.DEBUG && allowedTags.contains(tag)) {
            Log.e(tag, message);
        }
    }
}

