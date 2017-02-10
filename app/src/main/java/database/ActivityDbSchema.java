package database;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class ActivityDbSchema {
    public static final class ActivityTable {
        public static final String NAME = "activities";

        public static final class Cols {
            public static final String TIME = "act_time";
            public static final String TYPE = "act_type";
        }
    }

    public static final class Cols {
        public static final String TIME = "act_time";
        public static final String TYPE = "act_type";
    }
}
