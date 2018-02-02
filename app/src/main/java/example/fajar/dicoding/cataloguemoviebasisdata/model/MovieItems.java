package example.fajar.dicoding.cataloguemoviebasisdata.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by adul on 16/09/17.
 */

public class MovieItems implements Parcelable {

    private int id;
    private int movie_id;
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;

    public MovieItems() {
    }

    public MovieItems(JSONObject object) {
        try {
            int id = object.getInt("id");
            int movie_id = object.getInt("id");
            String title = object.getString("title");
            String poster = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            this.id = id;
            this.movie_id = movie_id;
            this.title = title;
            this.poster_path = poster;
            this.overview = overview;
            this.release_date = release_date;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected MovieItems(Parcel in) {
        id = in.readInt();
        movie_id = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel in) {
            return new MovieItems(in);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(movie_id);
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }
}
