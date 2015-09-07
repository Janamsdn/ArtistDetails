package com.jay.model;
import android.os.Parcel;
import android.os.Parcelable;

/* this class will provide artist model*/
public class Artist implements Parcelable {

	private String id;
	private String genres;
	private String picture;
	private String name;
	private String description;

	public Artist(String id, String genres, String picture, String name,
			String description) {
		super();
		this.id = id;
		this.genres = genres;
		this.picture = picture;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getGenres() {
		return genres;
	}

	public String getPicture() {
		return picture;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}


	protected Artist(Parcel in) {
		id = in.readString();
		genres = in.readString();
		picture = in.readString();
		name = in.readString();
		description = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(genres);
		dest.writeString(picture);
		dest.writeString(name);
		dest.writeString(description);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
		@Override
		public Artist createFromParcel(Parcel in) {
			return new Artist(in);
		}

		@Override
		public Artist[] newArray(int size) {
			return new Artist[size];
		}
	};
}