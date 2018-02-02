package com.expert.andro.favoritemovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expert.andro.favoritemovie.BuildConfig;
import com.expert.andro.favoritemovie.R;

/**
 * Created by adul on 02/10/17.
 */

public class FavoriteMovieAdapter extends CursorAdapter {

    public static String FIELD_MOVIE_ID = "movie_id";
    public static String FIELD_TITLE = "title";
    public static String FIELD_POSTER = "poster";
    public static String FIELD_OVERVIEW = "overview";
    public static String FIELD_RELEASE = "release";

    public FavoriteMovieAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_movie,viewGroup,false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            ImageView imgPoster = view.findViewById(R.id.img_item_photo);
            TextView tvMovie_id = view.findViewById(R.id.tv_movie_id);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            TextView tvOverView = view.findViewById(R.id.tv_overview);
            TextView tvRelease = view.findViewById(R.id.tv_release);

            Glide.with(context)
                    .load(BuildConfig.BASE_URL_POSTER+cursor.getString(cursor.getColumnIndexOrThrow(FIELD_POSTER)))
                    .override(350,550)
                    .into(imgPoster);

            tvMovie_id.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_MOVIE_ID)));
            tvTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
            tvOverView.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_OVERVIEW)));
            tvRelease.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_RELEASE)));
        }
    }
}
