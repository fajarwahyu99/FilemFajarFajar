package example.fajar.dicoding.cataloguemoviebasisdata.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import example.fajar.dicoding.cataloguemoviebasisdata.db.DatabaseHelper;



public class MovieProvider extends ContentProvider {

    private static final String AUTHORITY = "movie.basisdata";
    private static final String BASE_PATH = "favorites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int FAVORITES = 1;
    private static final int FAVORITES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FAVORITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAVORITES_ID);
    }
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        Log.d("Provider", "onCreate: "+CONTENT_URI);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;

        if (uriMatcher.match(uri) == FAVORITES){
            cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, null,null,null,null,null,DatabaseHelper.FIELD_ID+" DESC");

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

        if (id>0){
            Uri mUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(uri,null);
            return mUri;
        }
        throw new SQLException("Insert failed URI : "+uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)){
            case FAVORITES:
                delCount = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is Unknown URI : "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int upCount = 0;
        switch (uriMatcher.match(uri)){
            case FAVORITES:
                upCount = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is Unknown URI : "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return upCount;
    }
}
