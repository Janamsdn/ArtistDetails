package com.jay.artistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jay.artistdetails.R;
import com.jay.imageloader.ImageDownloader;
import com.jay.model.Artist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity mContext;
	private List<Artist> data;
	private static LayoutInflater inflater=null;


	public LazyAdapter(Activity context, List<Artist> artistList) {
		this.mContext = context;
		this.data=artistList;   
	}
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View objectView = convertView;
		ArtistHolder holder;
		final Artist artistObj = data.get(position);
		if (objectView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			objectView = inflater.inflate(R.layout.list_row,parent, false);
		}
		holder = new ArtistHolder(artistObj);

		holder.title = (TextView)objectView.findViewById(R.id.title); // title
		holder.artistTv = (TextView)objectView.findViewById(R.id.artist); // artist name
		holder.id = (TextView)objectView.findViewById(R.id.id); // duration
		holder.thumb_image=(ImageView)objectView.findViewById(R.id.list_image); // thumb image


		// Setting all values in listview
		holder.title.setText("Name: "+ artistObj.getName());
		holder.artistTv.setText("Genres: "+artistObj.getGenres());
		holder.id.setText(artistObj.getId());
		holder.id.setVisibility(View.GONE);
		holder.thumb_image.setTag(artistObj.getPicture());    // add this      
		ImageDownloader imageDownloader = new ImageDownloader(); 
		imageDownloader.download(artistObj.getPicture(), holder.thumb_image); 
		objectView.setTag(holder);
		return objectView;
	}
	public static class ArtistHolder {
		ImageView thumb_image;
		TextView title;
		Bitmap bitmap;
		TextView id;
		TextView artistTv;
		Artist artist;

		ArtistHolder (Artist artist) {
			this.artist = artist;
		}

		public Artist getArtist() {
			return artist;
		}


	}


}