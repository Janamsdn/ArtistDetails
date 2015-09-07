package com.jay.imageloader;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/*Background download using an AsyncTask
  
 */
public class ImageDownloader {
	private static LinkedHashMap<String, Bitmap> cash = new LinkedHashMap<String, Bitmap>();

	public void download(String url, ImageView imageView) {
		if (cancelPotentialDownload(url, imageView)) {
			if(cash.containsKey(url)) {
				imageView.setImageBitmap(cash.get(url)); 
			} else {
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
				imageView.setImageDrawable(downloadedDrawable);
				task.execute(url);
			}
		}
	}

	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		String url;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			return downlodBitmap(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if(isCancelled()) {
				result = null;
			}
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with it
				if (this == bitmapDownloaderTask) {
					imageView.setImageBitmap(result);
					cash.put(url, result);
				}
			}
		}
	}

	public static Bitmap downlodBitmap(String url) {		
		final DefaultHttpClient client = new DefaultHttpClient() ;
		final HttpGet getRequest = new HttpGet(url);	
		try {
			HttpResponse responce = client.execute(getRequest);
			final int statusCode = responce.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
				return null;
			}	    	

			final HttpEntity entity = responce.getEntity();
			if(entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
					return bitmap;
				} finally {
					if(inputStream != null) {
						inputStream.close();
						entity.consumeContent();
					}
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
		} 
		return null;
	} 


}
