package example.fajar.dicoding.cataloguemoviebasisdata.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import example.fajar.dicoding.cataloguemoviebasisdata.model.MovieItems;

/**
 * Created by adul on 26/09/17.
 */

public class MovieHelper {

    private static String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public Cursor searchQuery(int title){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                DatabaseHelper.FIELD_MOVIE_ID + " LIKE '%" + title + "%'", null);
    }

    public ArrayList<MovieItems> getSearchResult(int keyword){
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        Cursor cursor = searchQuery(keyword);
        cursor.moveToFirst();
        MovieItems movieItems;

        if (cursor.getCount()>0){
            do {
                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                movieItems.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_POSTER)));
                movieItems.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_OVERVIEW)));
                movieItems.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_RELEASE)));

                arrayList.add(movieItems);

                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public int getData(int id){
        int result = 0;
        Cursor cursor = searchQuery(id);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            result = cursor.getInt(1);
            Log.d("TAG", "getData: if "+result);
            for (; !cursor.isAfterLast(); cursor.moveToNext()){
                result = cursor.getInt(1);
                Log.d("TAG", "getData: for "+result);
            }
        }
        cursor.close();
        return result;
    }

    public Cursor queryAllData(){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY "+
        DatabaseHelper.FIELD_MOVIE_ID + " DESC", null);
    }

    public ArrayList<MovieItems> getAllData(){
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        MovieItems movieItems;

        if (cursor.getCount()>0){
            do {
                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                movieItems.setMovie_id(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_MOVIE_ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                movieItems.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_POSTER)));
                movieItems.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_OVERVIEW)));
                movieItems.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_RELEASE)));

                arrayList.add(movieItems);

                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MovieItems items){
        ContentValues initialValue = new ContentValues();
        initialValue.put(DatabaseHelper.FIELD_MOVIE_ID, items.getMovie_id());
        initialValue.put(DatabaseHelper.FIELD_TITLE, items.getTitle());
        initialValue.put(DatabaseHelper.FIELD_POSTER, items.getPoster_path());
        initialValue.put(DatabaseHelper.FIELD_OVERVIEW, items.getOverview());
        initialValue.put(DatabaseHelper.FIELD_RELEASE, items.getRelease_date());
        Log.d("TAG", "insert: success");
        return sqLiteDatabase.insert(DATABASE_TABLE, null, initialValue);
    }

    public void update(MovieItems items){
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.FIELD_TITLE, items.getTitle());
        args.put(DatabaseHelper.FIELD_POSTER, items.getPoster_path());
        args.put(DatabaseHelper.FIELD_OVERVIEW, items.getOverview());
        args.put(DatabaseHelper.FIELD_RELEASE, items.getRelease_date());
        sqLiteDatabase.update(DATABASE_TABLE,args,DatabaseHelper.FIELD_ID + " = '" + items.getId() + "'",null);
    }

    public void delete(int id){
        sqLiteDatabase.delete(DATABASE_TABLE, DatabaseHelper.FIELD_MOVIE_ID + " = '" + id + "'",null);
        Log.d("TAG", "delete: success");
    }
}
