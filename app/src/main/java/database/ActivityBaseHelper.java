package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.ActivityDbSchema.ActivityTable;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class ActivityBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "activityBase.db";

    public ActivityBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ActivityTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ActivityTable.Cols.TIME + ", " +
                ActivityTable.Cols.TYPE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
