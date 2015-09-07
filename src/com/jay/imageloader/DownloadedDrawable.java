package com.jay.imageloader;

import java.lang.ref.WeakReference;

import com.jay.imageloader.ImageDownloader.BitmapDownloaderTask;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


/* A local cache of downloaded images is maintained internally to improve
* performance.
*/
public class DownloadedDrawable extends ColorDrawable {
    private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
        super(Color.BLACK);
        bitmapDownloaderTaskReference =
            new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
    }

    public BitmapDownloaderTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}
