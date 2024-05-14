import android.provider.BaseColumns

object ScheduleEntry : BaseColumns {
    const val TABLE_NAME = "schedule"
    const val COLUMN_ID = BaseColumns._ID
    const val COLUMN_TITLE = "title"
    const val COLUMN_TIME = "time"
    const val COLUMN_LOCATION = "location"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_IS_COMPLETED = "is_completed"
}