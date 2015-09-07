/*
 *        Copyright (c) 2013 - Cambridge Silicon Radio Ltd
 *
 *  This software is copyrighted by and is the sole property of CSR. All rights,
 *  title, ownership, or other interests in the software remain the property of
 *  CSR.  This software may only be used in accordance with the corresponding
 *  license agreement.  Any unauthorized use, duplication, transmission,
 *  distribution, or disclosure of this software is expressly forbidden.
 *
 *  This Copyright notice may not be removed or modified without prior written
 *  consent of CSR.
 *
 *  CSR, reserves the right to modify this software without notice.
 *
 *  Cambridge Silicon Radio Ltd
 *  The Legacy Building        UK +44 (0) 2890 463140
 *  NISP, Queen's Road
 *  Belfast                                            http://www.csr.com
 *  BT3 9DT
 *
 * ===========================================================================*/

package com.jay.artistview;




import com.jay.artistdetails.R;
import com.jay.imageloader.ImageDownloader;
import com.jay.model.Artist;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




public class ArtistDetailsFragment extends Fragment {
	private View view;
	private Context mContext;
	private TextView artistName;
	private ImageView artistImg;
	private TextView artistBio;
	Artist artistDetails;

	public static ArtistDetailsFragment newInstance( Artist ArtistHolder) {
		ArtistDetailsFragment f = new ArtistDetailsFragment();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putParcelable("Artist", ArtistHolder);
		f.setArguments(args);
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle("Artist Details");
		Bundle bundle = getArguments();
		if (bundle != null) {
			artistDetails = bundle.getParcelable("Artist");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//getting handles to the GUI elements
		View view = inflater.inflate(R.layout.artist_details, container, false);
		artistName = (TextView)view.findViewById(R.id.artistName);
		artistImg = (ImageView)view.findViewById(R.id.artistImage);
		artistBio = (TextView)view.findViewById(R.id.artistBio);
		artistName.setText(artistDetails.getName());
		
		//String textFromHtml = Jsoup.parse(artistDetails.getDescription()).text();
		artistBio.setText(Html.fromHtml(Html.fromHtml(artistDetails.getDescription()).toString()));
		artistImg.setTag(artistDetails.getPicture());
		//new DownloadImageTask(artistImg).execute(artistDetails.getPicture());
		ImageDownloader imageDownloader = new ImageDownloader(); 
		imageDownloader.download(artistDetails.getPicture(), artistImg); 
		return view;
	}
	// /////////////////////////////////////////////////
	// /////////////////// LISTENERS ///////////////////
	// /////////////////////////////////////////////////

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
}
