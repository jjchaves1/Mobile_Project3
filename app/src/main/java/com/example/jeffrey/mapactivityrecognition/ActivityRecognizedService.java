package com.example.jeffrey.mapactivityrecognition;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import database.ActivityBaseHelper;
import database.ActivityCursorWrapper;
import database.ActivityDbSchema;
import database.ActivityDbSchema.ActivityTable;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class ActivityRecognizedService extends IntentService {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DetectedActivity lastAct = null;
    private Date lastDate = null;
    private String toastActivity = null;
    public static final String IMAGE = "image";
    public static final String TEXT = "text";
    public static final String NOTIFICATION = "name.heqian.cs528.googlefit";

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private static ContentValues getContentValues(String activityString){
        ContentValues values = new ContentValues();
        Date d = new Date();
        values.put(ActivityTable.Cols.TIME, d.getTime());
        values.put(ActivityTable.Cols.TYPE, activityString);
        return values;
    }

    private ActivityCursorWrapper queryActivities(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ActivityTable.NAME,
                null, // SELECT all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ActivityCursorWrapper(cursor);
    }

    public List<String> getActivities(){
        List<String> activities = new ArrayList<String>();
        ActivityCursorWrapper cursor = queryActivities(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                activities.add(cursor.getActivity());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return activities;
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        DetectedActivity highestProbActivity = probableActivities.get(0);
        mContext = getApplicationContext();
        mDatabase = new ActivityBaseHelper(mContext).getWritableDatabase();
        String activityString;
        ContentValues vals;

        for( DetectedActivity activity : probableActivities ) {
            if (activity.getConfidence() > highestProbActivity.getConfidence())
                highestProbActivity = activity;
        }
        if(lastAct != null && highestProbActivity.getType() != lastAct.getType() && lastDate != null && toastActivity != null){
            long rightNow = new Date().getTime();
            long minsPassed = (lastDate.getTime() - rightNow) / 60000;
            long secsPassed = ((lastDate.getTime() - rightNow) % 60000) / 1000;
            int duration = Toast.LENGTH_SHORT;
            String a = "You have just " + toastActivity + " for ";
            String b = " min, ";
            String c = " seconds.";
            String toastString = a + minsPassed + b + secsPassed + c;
            Toast activityChangeToast = Toast.makeText(mContext, toastString, duration);
            activityChangeToast.show();
        }
        if(lastAct == null && lastDate == null){
            lastAct = highestProbActivity;
            lastDate = new Date();
        }

        switch( highestProbActivity.getType() ) {
            case DetectedActivity.IN_VEHICLE: {
                activityString = "In Vehicle";
                toastActivity = "driven";
                vals = getContentValues(activityString);
                mDatabase.insert(ActivityTable.NAME, null, vals);
                Log.e("ActivityRecogition", "In Vehicle: " + highestProbActivity.getConfidence());
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(IMAGE, R.drawable.in_vehicle);
                intent.putExtra(TEXT, R.string.driving);
                sendBroadcast(intent);
                break;
            }
            case DetectedActivity.ON_FOOT: {
                activityString = "On Foot";
                toastActivity = "walked";
                vals = getContentValues(activityString);
                mDatabase.insert(ActivityTable.NAME, null, vals);
                Log.e( "ActivityRecogition", "On Foot: " + highestProbActivity.getConfidence() );
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(IMAGE, R.drawable.walking);
                intent.putExtra(TEXT, R.string.walking);
                sendBroadcast(intent);
                break;
            }
            case DetectedActivity.RUNNING: {
                activityString = "Running";
                toastActivity = "ran";
                vals = getContentValues(activityString);
                mDatabase.insert(ActivityTable.NAME, null, vals);
                Log.e( "ActivityRecogition", "Running: " + highestProbActivity.getConfidence() );
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(IMAGE, R.drawable.running);
                intent.putExtra(TEXT, R.string.running);

                sendBroadcast(intent);
                break;
            }
            case DetectedActivity.STILL: {
                activityString = "Still";
                toastActivity = "been still";
                vals = getContentValues(activityString);
                mDatabase.insert(ActivityTable.NAME, null, vals);
                Log.e( "ActivityRecogition", "Still: " + highestProbActivity.getConfidence() );
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(IMAGE, R.drawable.still);
                intent.putExtra(TEXT, R.string.still);
                sendBroadcast(intent);
                break;
            }
            case DetectedActivity.WALKING: {
                activityString = "Walking";
                toastActivity = "walked";
                vals = getContentValues(activityString);
                mDatabase.insert(ActivityTable.NAME, null, vals);
                Log.e( "ActivityRecogition", "Walking: " + highestProbActivity.getConfidence() );

                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(IMAGE, R.drawable.walking);
                intent.putExtra(TEXT, R.string.walking);
                sendBroadcast(intent);

                break;
            }
            case DetectedActivity.UNKNOWN: {
                Log.e( "ActivityRecogition", "Unknown: " + highestProbActivity.getConfidence() );
                break;
            }
        }
        // Debug to print entire SQLite table as it forms
        List<String> acts = getActivities();
        for(String act: acts){
            System.out.println(act);
        }
        System.out.println();
        lastAct = highestProbActivity;
    }
}
