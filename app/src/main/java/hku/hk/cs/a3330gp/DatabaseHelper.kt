package hku.hk.cs.a3330gp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import hku.hk.cs.a3330gp.util.Constants
import java.io.ByteArrayOutputStream

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val HISTORY_TABLE = Constants.HISTORY_TABLE
        private const val HISTORY_ID = Constants.HISTORY_ID
        private const val HISTORY_DATE = Constants.HISTORY_DATE
        private const val HISTORY_IMAGE = Constants.HISTORY_IMAGE
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create your database table here
        val createTableQuery = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableQuery)
        db?.execSQL("CREATE TABLE $HISTORY_TABLE " +
                "($HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$HISTORY_DATE TEXT," +
                "$HISTORY_IMAGE BLOB)")
        Log.d("db-create", "huh")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        val dropHistoryTableQuery = "DROP TABLE IF EXISTS $HISTORY_TABLE"
        db?.execSQL(dropHistoryTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long{
        val values = ContentValues().apply{
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean{
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun savePhoto(bmp: Bitmap, date:String):Long {
        val outputData = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputData)
        val values = ContentValues().apply {
            put(HISTORY_DATE, date)
            put(HISTORY_IMAGE, outputData.toByteArray())
        }
        val db = writableDatabase
        val id = db.insert(HISTORY_TABLE, null, values)
        return id
    }

    fun readImage(id:Long? = null):Cursor? {
        val db = readableDatabase
        var selection:String? = null
        var selectionArgs:Array<String>? = null
        var orderBy:String? = "$HISTORY_DATE DESC"
        if (id != null) {
            selection = "$HISTORY_ID = ?"
            selectionArgs = arrayOf(id.toString())
            orderBy = null
        }
        val cursor: Cursor? = db.query(
            HISTORY_TABLE,
            arrayOf(HISTORY_ID, HISTORY_DATE, HISTORY_IMAGE),
            selection,
            selectionArgs,
            null,
            null,
            orderBy
        )
        return cursor

    }
}