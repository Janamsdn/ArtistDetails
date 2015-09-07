package com.jay.artistview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jay.controler.ArtistResponseListener;
import com.jay.model.Artist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/*Background download using an AsyncTask
 */

public class HttpAsynkTask extends AsyncTask<String, Void, String> {

	private List<Artist> artistsList;
	private ArtistResponseListener listener;
	private Context mcontxt;
	private String Url;
	
	
	private ProgressDialog pd = null;
	public HttpAsynkTask( Context context,ArtistResponseListener listener, String Url  ) {
		this.mcontxt=context;
		this.listener=listener;
		this.Url=Url;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(mcontxt);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Retrieving data ....");
		pd.setCancelable(false);
		pd.show();

	}

	@Override
	protected void onCancelled(String result) {
		super.onCancelled(result);
	}

	@Override
	protected String doInBackground(String... urls) {
		return GetJsonData(Url);
	}
	public static String GetJsonData(String url){
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


	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if (pd != null) {
			pd.dismiss();
		}
		artistsList = new ArrayList<Artist>();
		try {
			JSONObject json = new JSONObject(result);
			JSONArray artists = json.getJSONArray("artists");
			for (int i = 0; i < artists.length(); i++) {
				JSONObject jsonObj = null;
				jsonObj = artists.getJSONObject(i);
				Artist artist=new Artist(jsonObj.getString(ArtistConstants.KEY_ID),jsonObj.getString(ArtistConstants.KEY_GENRES),jsonObj.getString(ArtistConstants.KEY_THUMB_URL)
						,jsonObj.getString(ArtistConstants.KEY_NAME),jsonObj.getString(ArtistConstants.KEY_DEC));
				// adding List to ArrayList
				artistsList.add(artist);
			}

			this.listener.callbackArtistResponse(artistsList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}