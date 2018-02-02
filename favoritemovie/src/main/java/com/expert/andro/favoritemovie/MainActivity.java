package com.expert.andro.favoritemovie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.expert.andro.favoritemovie.adapter.FavoriteMovieAdapter;
import com.expert.andro.favoritemovie.model.MovieItem;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private FavoriteMovieAdapter favoriteMovieAdapter;
    private ListView lvFavorite;
    private final int LOAD_FAV_ID = 110;

    private static final String AUTHORITY = "movie.basisdata";
    private static final String BASE_PATH = "favorites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.title_activity_main);

        lvFavorite = (ListView) findViewById(R.id.lv_favorites);
        lvFavorite.setOnItemClickListener(this);
        favoriteMovieAdapter = new FavoriteMovieAdapter(this, null, true);

        lvFavorite.setAdapter(favoriteMovieAdapter);
        getSupportLoaderManager().initLoader(LOAD_FAV_ID, null, this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cursor cursor = (Cursor) favoriteMovieAdapter.getItem(position);
        MovieItem movieItem = new MovieItem();
        movieItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        movieItem.setMovie_id(cursor.getInt(cursor.getColumnIndexOrThrow("movie_id")));
        movieItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        movieItem.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow("poster")));
        movieItem.setOverview(cursor.getString(cursor.getColumnIndexOrThrow("overview")));
        movieItem.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow("release")));

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_FAV_ITEM, movieItem);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_FAV_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteMovieAdapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAV_ID);
    }
}
