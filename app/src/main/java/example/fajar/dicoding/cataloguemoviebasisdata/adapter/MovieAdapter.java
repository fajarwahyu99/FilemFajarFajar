package example.fajar.dicoding.cataloguemoviebasisdata.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.fajar.dicoding.cataloguemoviebasisdata.BuildConfig;
import example.fajar.dicoding.cataloguemoviebasisdata.DetailMovieActivity;
import example.fajar.dicoding.cataloguemoviebasisdata.R;
import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;

/**
 * Created by adul on 16/09/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<MovieItems> listMovie;

    public ArrayList<MovieItems> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<MovieItems> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_items,parent,false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieItems posisi = getListMovie().get(position);

        Glide.with(context)
                .load(BuildConfig.BASE_URL_POSTER+posisi.getPoster_path())
                .override(350,550)
                .into(holder.imgPoster);

        holder.tvName.setText(posisi.getTitle());
        holder.tvOverview.setText(posisi.getOverview());
        holder.tvRelease.setText(posisi.getRelease_date());

        holder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                int ID_MOVIE = posisi.getId();
                MovieItems movieItems = new MovieItems();
                movieItems.setMovie_id(ID_MOVIE);
                movieItems.setMovie_id(posisi.getMovie_id());
                movieItems.setTitle(posisi.getTitle());
                movieItems.setPoster_path(posisi.getPoster_path());
                movieItems.setOverview(posisi.getOverview());
                movieItems.setRelease_date(posisi.getRelease_date());

                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieItems);
                context.startActivity(intent);
            }
        }));

        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, posisi.getTitle());
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
            return getListMovie().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_item_photo) ImageView imgPoster;
        @BindView(R.id.tv_item_name) TextView tvName;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.tv_release)TextView tvRelease;
        @BindView(R.id.btn_detail) Button btnDetail;
        @BindView(R.id.btn_share) Button btnShare;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
