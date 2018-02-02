package example.fajar.dicoding.cataloguemoviebasisdata.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.fajar.dicoding.cataloguemoviebasisdata.MainActivity;
import example.fajar.dicoding.cataloguemoviebasisdata.R;
import example.fajar.dicoding.cataloguemoviebasisdata.adapter.MovieAdapter;
import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;
import example.fajar.dicoding.cataloguemoviebasisdata.sync.SearchMovieLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMovieFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    private static final String TAG = "TAG_Search";
    private static final String STATE_SEARCH = "state_search";
    private MovieAdapter adapter;
    @BindView(R.id.tv_not_found)
    TextView tvNotFound;
    @BindView(R.id.edt_search_movie)
    EditText edtSearchMovie;
    @BindView(R.id.btn_search_movie)
    Button btnSearchMovie;
    @BindView(R.id.rcv_movie)
    RecyclerView rcvlistMovie;
    @BindView(R.id.progress)
    ProgressBar loading;

    public SearchMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_movie, container, false);
        ButterKnife.bind(this,view);

        adapter = new MovieAdapter(getActivity());

        loading.setVisibility(View.GONE);
        rcvlistMovie.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            Toast.makeText(getContext(), "!null search", Toast.LENGTH_SHORT).show();
            setToolbarTitle();
            String stateSearch = savedInstanceState.getString(STATE_SEARCH);
            edtSearchMovie.setHint("");
            edtSearchMovie.setText(stateSearch);
            getActivity().getSupportLoaderManager().initLoader(0,null,this);
        }

        return view;
    }
    
    private void setToolbarTitle() {
        String[] activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(activityTitles[0]);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_SEARCH, edtSearchMovie.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.btn_search_movie)
    public void search(View view){
        if (view.getId() == R.id.btn_search_movie){
            rcvlistMovie.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.INVISIBLE);
            String searchMovie = edtSearchMovie.getText().toString();

            boolean isEmptyFields = false;

            if (TextUtils.isEmpty(searchMovie)){
                isEmptyFields = true;
                edtSearchMovie.setError(getString(R.string.message_error_empty_field));
            }
            if (!isEmptyFields){
                getActivity().getSupportLoaderManager().initLoader(0,null,this);
            }
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader  Search: 1");
        String searchMovie = edtSearchMovie.getText().toString();

        if (!TextUtils.isEmpty(searchMovie)){
            loading.setVisibility(View.VISIBLE);
            rcvlistMovie.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onCreateLoader  Search: "+searchMovie);
            return new SearchMovieLoader(getActivity(),searchMovie);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader,final ArrayList<MovieItems> data) {
        Log.d(TAG, "onLoadFinished Search: 1");
        if (data.size()!=0) {
            loading.setVisibility(View.GONE);
            rcvlistMovie.setVisibility(View.VISIBLE);
            adapter.setListMovie(data);
            rcvlistMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcvlistMovie.setAdapter(adapter);
        }else {
            loading.setVisibility(View.GONE);
            rcvlistMovie.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.VISIBLE);
            tvNotFound.setText(getString(R.string.message_error_load));
            tvNotFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportLoaderManager().restartLoader(0,null,SearchMovieFragment.this);
                    tvNotFound.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setListMovie(null);

    }
}
