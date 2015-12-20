package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper1 extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="FirstDataBase";
    private final static int DATABASE_VERSION=1;
    private final static String TABLE_NAME="Table1";
    public final static String WEIGHT="weight";
    public final static String ID="id";
    public final static String TEMPERATURE="temperature";

    public DBHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO �Զ����ɵķ������
        String sql = "CREATE TABLE " + TABLE_NAME + " ("+ID+"INTEGER primary key autoincrement"  + WEIGHT + " text, "+ TEMPERATURE +" text);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO �Զ����ɵķ������
        String sql="DROP TABLE IF EXIST"+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);

    }
    public  long insert(String wei,String tem)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(WEIGHT, wei);
        cv.put(TEMPERATURE,tem);
        long row=db.insert(TABLE_NAME, null, cv);
        return row;
    }
    /*	public Cursor select() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db
            .query(TABLE_NAME, null, null, null, null, null, null);
            return cursor;
            }*/
    public void delete(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=ID+"=?";
        String[] whereValue={Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);

    }

    public int select(int id, int flag )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=ID+"=?";
        int weight = 0;
        int temperature = 0;
        String[] whereValue={Integer.toString(id)};
        Cursor cursor=db.rawQuery(TABLE_NAME, whereValue);
        while(cursor.moveToNext()){
            weight=cursor.getInt(0);
            temperature=cursor.getInt(1);
        }
        cursor.close();
        db.close();
        if(flag==0)
        {
            return weight;
        }
        else
            return temperature;
    }
	/* public People[] queryOneData(long id) {
		  Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_AGE, KEY_HEIGHT},
				  KEY_ID + "=" + id, null, null, null, null);
		  return ConvertToPeople(results);
	  }*/

    public void update(int id,String wei,String tem)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=ID+"=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues();
        cv.put(WEIGHT, wei);
        cv.put(TEMPERATURE, tem);
        db.update(TABLE_NAME, cv, where, whereValue);
    }


}
