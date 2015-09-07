package com.jay.artistview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jay.artistdetails.R;
import com.jay.artistview.LazyAdapter.ArtistHolder;
import com.jay.controler.ArtistResponseListener;
import com.jay.model.Artist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;

public class ArtistMainActivity extends Activity implements ArtistResponseListener {

	private TextView tvIsConnected;
	private ListView artistListView;
	private LazyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setTitle("Artist List");
		// get reference to the views
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
		artistListView=(ListView)findViewById(R.id.list);
		// check if you are connected or not
		if(isConnected()){
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("Wifi conncted");
		}
		else{
			tvIsConnected.setText("Wifi are NOT conncted");
		}

		artistListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				Object holder = view.getTag();
				if (holder instanceof ArtistHolder) {
					Artist radioStationHolder = ((ArtistHolder) holder).artist;
					showArtistDetails(radioStationHolder);
				}

			}

		});
		// call AsynTask to perform network operation on separate thread
		new HttpAsynkTask(this,ArtistMainActivity.this,ArtistConstants.URL).execute();
	}

	protected void showArtistDetails(Artist radioStationHolder) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("fragment");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		ArtistDetailsFragment newFragment = ArtistDetailsFragment.newInstance(radioStationHolder);
		ft.replace(R.id.artist_container, newFragment,"fragment");
		ft.commit();
		getFragmentManager().executePendingTransactions();

	}

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	// convert InputStream to String
	private static String convertInputStreamToString(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}



	public boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;	
	}

	@Override
	public void callbackArtistResponse(List<Artist> artistList) {
		if(!artistList.isEmpty()){
			adapter=new LazyAdapter(this, artistList);        
			artistListView.setAdapter(adapter);

		}

	}
}
