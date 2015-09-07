package com.jay.controler;

import java.util.List;

import com.jay.model.Artist;
   
//interface to inform to UI from asyntask
public interface ArtistResponseListener {
	// you can define any parameter as per your requirement
	public void callbackArtistResponse(List<Artist>artistList);
}
