package example.fajar.dicoding.cataloguemoviebasisdata.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.fajar.dicoding.cataloguemoviebasisdata.MainActivity;
import example.fajar.dicoding.cataloguemoviebasisdata.R;
import example.fajar.dicoding.cataloguemoviebasisdata.adapter.MovieAdapter;
import example.fajar.dicoding.cataloguemoviebasisdata.db.MovieHelper;
import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private static final String TAG = "TAG_Favorite";
    private MovieHelper movieHelper;
    private ArrayList<MovieItems> mDataMovie;
    private MovieAdapter adapter;

    @BindView(R.id.tv_not_found)TextView tvNotFound;
    @BindView(R.id.rcv_movie)RecyclerView rcvlistMovie;
    @BindView(R.id.progress)ProgressBar loading;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);

        rcvlistMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvlistMovie.setHasFixedSize(true);

        adapter = new MovieAdapter(getActivity());
        movieHelper = new MovieHelper(getActivity());
        movieHelper.open();

        mDataMovie = new ArrayList<>();
        adapter.setListMovie(mDataMovie);
        rcvlistMovie.setAdapter(adapter);

        if (savedInstanceState != null) {
            setToolbarTitle();
        }
        return view;
    }
    private void setToolbarTitle() {
        String[] activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(activityTitles[2]);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Run");
        new LoadFavoriteAsync().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (movieHelper!=null){
            movieHelper.close();
        }
    }

    private class LoadFavoriteAsync extends AsyncTask<Void,Void,ArrayList<MovieItems>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

            if (mDataMovie.size()>0){
                mDataMovie.clear();
            }
        }

        @Override
        protected ArrayList<MovieItems> doInBackground(Void... voids) {
            return movieHelper.getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItems> movieItemses) {
            super.onPostExecute(movieItemses);
            loading.setVisibility(View.GONE);

            mDataMovie.addAll(movieItemses);
            adapter.setListMovie(mDataMovie);
            Toast.makeText(getActivity(), ""+movieItemses.size(), Toast.LENGTH_SHORT).show();

            if (mDataMovie.size() == 0){
                showSnackBarMessage("data empty");
            }
        }
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(rcvlistMovie,message,Snackbar.LENGTH_SHORT).show();
    }
}
