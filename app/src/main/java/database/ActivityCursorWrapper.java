package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import database.ActivityDbSchema.ActivityTable;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class ActivityCursorWrapper extends CursorWrapper {
    public ActivityCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String getActivity(){
        String actTime = getString(getColumnIndex(ActivityTable.Cols.TIME));
        String actType = getString((getColumnIndex(ActivityTable.Cols.TYPE)));
        return "Time: " + actTime + " Type: " + actType;
    }
}
