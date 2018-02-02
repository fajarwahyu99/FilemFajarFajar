package example.fajar.dicoding.cataloguemoviebasisdata.stackwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import example.fajar.dicoding.cataloguemoviebasisdata.BuildConfig;
import example.fajar.dicoding.cataloguemoviebasisdata.R;
import example.fajar.dicoding.cataloguemoviebasisdata.db.MovieHelper;
import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;

/**
 * Created by adul on 04/10/17.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<MovieItems> mWidgetItems = new ArrayList<>();
    private Context context;
    private int mAppWidgetId;
    private  MovieHelper movieHelper;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        movieHelper = new MovieHelper(context);
        movieHelper.open();
        mWidgetItems.addAll(movieHelper.getAllData());
    }

    @Override
    public void onDestroy() {
        if (movieHelper != null){
            movieHelper.close();
        }
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .load(BuildConfig.BASE_URL_POSTER+mWidgetItems.get(position).getPoster_path())
                    .asBitmap()
                    .error(new ColorDrawable(context.getResources().getColor(R.color.colorSubTitle)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error", "error ");
        }

        rv.setImageViewBitmap(R.id.imgView, bmp);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imgView, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
