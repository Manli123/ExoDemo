package com.axat.starbarn.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class SimpleMediaPlayer extends Application {

    public static SimpleCache simpleCache;
    public static final long exoPlayerCacheSize = 100 * 1024 * 1024; // Setting cache size to be ~ 100 MB
    public LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor;
    public StandaloneDatabaseProvider standaloneDatabaseProvider;

    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
//    public static final long  maxMemory = Runtime.getRuntime().maxMemory();
//
//    // Use 1/8th of the available memory for this memory cache.
//    public static final long  cacheSize = maxMemory / 8;


    public static SimpleCache getInstance(Context context) {
        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        if (simpleCache != null) return simpleCache;
        else return simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), leastRecentlyUsedCacheEvictor, new StandaloneDatabaseProvider(context));
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.e("cacheSize","cacheSize"+cacheSize);
        leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        standaloneDatabaseProvider = new StandaloneDatabaseProvider(this);
        simpleCache = new SimpleCache(
                new File(this.getCacheDir(), "media"),
                leastRecentlyUsedCacheEvictor,
                standaloneDatabaseProvider);
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


}
