import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {//伴生对象,类似于Java中的静态成员
        private const val DATABASE_NAME = "schedule.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_SCHEDULE_TABLE = ("CREATE TABLE " +
                ScheduleEntry.TABLE_NAME + "(" +
                ScheduleEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                ScheduleEntry.COLUMN_TITLE + " TEXT," +
                ScheduleEntry.COLUMN_TIME + " TEXT," +
                ScheduleEntry.COLUMN_LOCATION + " TEXT," +
                ScheduleEntry.COLUMN_DESCRIPTION + " TEXT," +
                ScheduleEntry.COLUMN_IS_COMPLETED + " INTEGER" + ")")
        db.execSQL(CREATE_SCHEDULE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleEntry.TABLE_NAME)
        onCreate(db)
    }
}