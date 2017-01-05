package model;



/**
 * Created by Matan on 20/12/2016.
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DatabaseHelper extends SQLiteOpenHelper {

    // DB properties
    private SQLiteDatabase db = null;
    private static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "PlaceDB.db";

    // places table properties
    public static final String TABLE_NAME = "places";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_FILE_NAME = "image_data";
    public static final String IS_FAVORITE = "is_favorite";

    //ceate query
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE "
            + TABLE_NAME + "(" + ID + " integer primary key," + NAME + " TEXT ," + DESCRIPTION
            + " TEXT," + IMAGE_FILE_NAME + " BLOB," + IS_FAVORITE
            + " INTEGER" + ")";

    private static final String DROP_TABLE_PLACES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_PLACE = "select * from " + TABLE_NAME +" where "+ ID + " = ?";
    private static final String SELECT_ALL_PLACES = "select * from " + TABLE_NAME ;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME , null, DATABASE_VERSION);
    }

    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void close() {
        try {
            db.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(CREATE_TABLE_PLACES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(DROP_TABLE_PLACES);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Long insertRow (long id , String name, String description, boolean is_favorite, Bitmap bitmap) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(NAME, name);
            contentValues.put(DESCRIPTION, description);
            contentValues.put(IS_FAVORITE, is_favorite);

            if (bitmap != null) {
                byte[] data = getBitmapAsByteArray(bitmap);
                if (data != null && data.length > 0) {
                    contentValues.put(IMAGE_FILE_NAME, data);
                }
            }

            return db.insert(TABLE_NAME, null, contentValues);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1L;
        }
    }


    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public int numberOfRows(){

        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public int updateRow (long id, String name, String description, boolean is_favorite, Bitmap image) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(IS_FAVORITE, is_favorite);


        if (image != null) {
            byte[] data = getBitmapAsByteArray(image);
            if (data != null && data.length > 0) {
                contentValues.put(IMAGE_FILE_NAME, data);
            }
        }
        else{
            contentValues.putNull(IMAGE_FILE_NAME);
        }

        try {
            return db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Long.toString(id) } );
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }



    public int deleteRow (long id) {

        try {
            return db.delete(TABLE_NAME, "id = ? ",new String[] { Long.toString(id) });
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Cursor getCursor(long id) {

        Cursor res = null;

        if(id > -1){
            res =  db.rawQuery( SELECT_PLACE, new String[] { Long.toString(id) });
        }
        else{
            res =  db.rawQuery(SELECT_ALL_PLACES, null );
        }


        return res;
    }

    private Place getPlaceFromCursor(Cursor res){
        Place p ;
        Bitmap image;
        p = new Place(res.getLong(res.getColumnIndex(ID))
                , res.getString(res.getColumnIndex(NAME))
                , res.getString(res.getColumnIndex(DESCRIPTION))
                , Boolean.getBoolean(res.getString(res.getColumnIndex(IS_FAVORITE)))
                , null);

        byte[] img1Byte = res.getBlob(res.getColumnIndex(IMAGE_FILE_NAME));
        if (img1Byte != null && img1Byte.length > 0) {
            image = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
            if (image != null) {
                p.setImgData(image);
            }
        }


        return p;
    }

    public Place getRow(long id) {
        Place p = null;
        Cursor c =  null;
        try {
            c =  getCursor(id);
            if (c != null) {
                c.moveToFirst();
                p = getPlaceFromCursor(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null)
                c.close();
        }

        return  p;
    }

    public ArrayList<Place> getAllRows() {
        ArrayList<Place> array_list = new ArrayList<>();

        Cursor res = null;
        try {
            res =  getCursor(-1);
            if (res != null) {
                res.moveToFirst();

                while(res.isAfterLast() == false){

                    array_list.add(getPlaceFromCursor(res));
                    res.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(res!=null){
                res.close();
            }
        }

        return array_list;
    }
}