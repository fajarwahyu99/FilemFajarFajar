package com.expert.andro.favoritemovie;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expert.andro.favoritemovie.model.MovieItem;

public class DetailActivity extends AppCompatActivity {

    public static String EXTRA_FAV_ITEM = "extra_fav_item";
    private ImageView imgPoster;
    private TextView tvTitle, tvOverview, tvRelease, tvId, tvMovieId;

    private MovieItem movieItem = null;
    private boolean isNotEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle(R.string.title_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRelease = (TextView) findViewById(R.id.tv_release);
        tvOverview = (TextView) findViewById(R.id.tv_overview);
        imgPoster = (ImageView) findViewById(R.id.img_poster);

        movieItem = getIntent().getParcelableExtra(EXTRA_FAV_ITEM);

        if (movieItem != null){
            isNotEmpty = true;
            tvTitle.setText(movieItem.getTitle());
            tvOverview.setText(movieItem.getOverview());
            tvRelease.setText(movieItem.getRelease_date());

            Glide.with(this)
                    .load(BuildConfig.BASE_URL_POSTER + movieItem.getPoster_path())
                    .into(imgPoster);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isNotEmpty){
            getMenuInflater().inflate(R.menu.menu_form,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_delete){
            showDeleteAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlertDialog() {
        String dialogMessage = getString(R.string.dialog_message_delete);
        String dialogTitle = getString(R.string.dialog_title_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.dialog_positive_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectionClause = "_id=?";
                String selectionArgs[] = new String[] {String.valueOf(movieItem.getId())};

                getContentResolver().delete(MainActivity.CONTENT_URI, selectionClause, selectionArgs);

                Toast.makeText(DetailActivity.this, getString(R.string.toast_deleted_msg), Toast.LENGTH_SHORT).show();
                finish();
            }
        }).setNegativeButton(getString(R.string.dialog_negative_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
