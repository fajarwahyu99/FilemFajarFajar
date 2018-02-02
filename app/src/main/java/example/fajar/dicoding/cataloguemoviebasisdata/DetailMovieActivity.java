package example.fajar.dicoding.cataloguemoviebasisdata;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.fajar.dicoding.cataloguemoviebasisdata.db.MovieHelper;
import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;
import example.fajar.dicoding.cataloguemoviebasisdata.sync.DetailMovieLoader;

public class DetailMovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    private static final String TAG = "TAG_Detail";
    public static String EXTRA_ID = "id";
    public static String EXTRA_TITLE = "title";
    public static String EXTRA_MOVIE = "movie";

    private MovieItems movieItems;
    private ArrayList<MovieItems> arrayList;
    private MovieHelper movieHelper;
    private AppPreference appPreference;

    private int movie_id;
    private boolean isFavorite ;
    private String movie_title;

    @BindView(R.id.progress) ProgressBar loading;
    @BindView(R.id.img_poster) ImageView imgPoster;
    @BindView(R.id.img_backdrop) ImageView imgCollaps;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.layout_detail)RelativeLayout detailView;
    @BindView(R.id.tv_overview) TextView tvOverView;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_not_found) TextView tvNotFound;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.collapsingToolbar)CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        movieHelper = new MovieHelper(this);
        appPreference = new AppPreference(this);
        movieHelper.open();

        movieItems = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movie_id = getIntent().getIntExtra(EXTRA_ID,0);
        movie_title = getIntent().getStringExtra(EXTRA_TITLE);

        if (movieItems != null) {
            Log.d(TAG, "onCreate: movieItems "+movieItems.getMovie_id());
            setCollapsToolbar();
            setFavoriteStatus();
            if (savedInstanceState != null) {
                loading.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(0, null, this);
            } else {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }
        if (movie_id!=0){
            Log.d(TAG, "onCreate: movie_id -> "+movie_id);
            setCollapsToolbar();
            loading.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    void setFavoriteStatus(){
        int movie_db = movieHelper.getData(movieItems.getMovie_id());
        if (movie_db == movieItems.getMovie_id()) {
            fab.setImageResource(R.drawable.ic_favorite_white_36dp);
        }else {
            fab.setImageResource(R.drawable.ic_favorite_border_white_36dp);
        }
    }

    public boolean isFavoriteSuccess(){
        int movie_db = movieHelper.getData(movieItems.getMovie_id());
        if (movie_db == movieItems.getMovie_id()) {
            movieHelper.delete(movieItems.getMovie_id());
            isFavorite=true;
            return true;
        }else {
            MovieItems movie = new MovieItems();
            movie.setMovie_id(movieItems.getMovie_id());
            movie.setTitle(movieItems.getTitle());
            movie.setPoster_path(movieItems.getPoster_path());
            movie.setOverview(movieItems.getOverview());
            movie.setRelease_date(movieItems.getRelease_date());
            movieHelper.insert(movieItems);
            isFavorite=false;
            return false;
        }
    }

    @OnClick(R.id.fab)
    void setFab(){
        isFavorite = isFavoriteSuccess();
        if (isFavorite) {
            Snackbar.make(fab, getString(R.string.message_delete_snackbar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            fab.setImageResource(R.drawable.ic_favorite_border_white_36dp);
        }else {
            Snackbar.make(fab, getString(R.string.message_success_snackbar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            fab.setImageResource(R.drawable.ic_favorite_white_36dp);
        }
    }

    private void setCollapsToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(" ");

        if (movie_id==0) {
            Glide.with(this)
                    .load(BuildConfig.BASE_URL_POSTER + movieItems.getPoster_path())
                    .override(350, 550)
                    .into(imgCollaps);
        }
        // https://stackoverflow.com/a/32724422/1277751
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (movie_id==0) {
                        collapsingToolbarLayout.setTitle(movieItems.getTitle());
                        isShow = true;
                    }else {
                        collapsingToolbarLayout.setTitle(movie_title);
                        isShow = true;
                    }
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        if (movieItems!=null){
            detailView.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            return new DetailMovieLoader(this,movieItems.getMovie_id());
        }else {
            detailView.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            return new DetailMovieLoader(this,movie_id);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        loading.setVisibility(View.GONE);
        detailView.setVisibility(View.VISIBLE);
        String poster = data.get(0).getPoster_path();
        if (movie_id!=0){
            Glide.with(this)
                    .load(BuildConfig.BASE_URL_POSTER+poster)
                    .override(350,550)
                    .into(imgCollaps);
        }
        String title = data.get(0).getTitle();
        String overview = data.get(0).getOverview();
        String release_date = data.get(0).getRelease_date();
        Glide.with(DetailMovieActivity.this)
                .load(BuildConfig.BASE_URL_POSTER+poster)
                .into(imgPoster);
        tvTitle.setText(title);
        tvOverView.setText(overview);
        tvReleaseDate.setText(release_date);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null){
            movieHelper.close();
        }
    }
}
