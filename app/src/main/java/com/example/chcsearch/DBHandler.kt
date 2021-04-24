package com.example.chcsearch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context?, cursorFactory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION){

    /**
     * Creates database table
     * @param db reference to the chcsearchapp database
     */
    override fun onCreate(db: SQLiteDatabase) {
        // define create statement for studnet table
        val query = "CREATE TABLE " + TABLE_STUDENT + "(" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_STUDENT_YEAR + " TEXT, " +
                COLUMN_STUDENT_MAJOR + " TEXT);"

        // execute create statement
        db.execSQL(query)
    }

    /**
     * Creates a new version of the database.
     * @param db reference to chcsearchapp database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // define drop statement for the student table
        val query = "DROP TABLE IF EXISTS " + TABLE_STUDENT

        // execute the drop statement
        db.execSQL(query)

        // call method that creates the table
        onCreate(db)
    }

    /**
     * This method gets called by the MainActivity when the app launches.
     * It inserts a new row in the student table.
     * @param name student name
     * @param major student major
     * @param year student year
     */
    fun addStudent(name: String?, major: String?, year: String?){

        // get reference to chcsearchapp database
        val db = writableDatabase

        // initialize a ContentValues object
        val values = ContentValues()

        // put data into the ContentValues object
        values.put(COLUMN_STUDENT_NAME, name)
        values.put(COLUMN_STUDENT_MAJOR, major)
        values.put(COLUMN_STUDENT_YEAR, year)

        // insert data in ContentValues object into student table
        db.insert(TABLE_STUDENT, null, values)

        // close database connection
        db.close()
    }

    /**
     * This method gets called by the search method in the StudentAdapter.
     * It will select the students from the database that satisfy
     * the specified search criteria.
     */
    fun search(key: String, value: String?) : MutableList<Student> {
        // get a reference to the chcsearchapp database
        val db = writableDatabase

        // initialize query String
        var query = ""

        if (key.equals("major")) {
            // define select statement and store it in query String
            query = "SELECT * FROM " + TABLE_STUDENT +
                    " WHERE " + COLUMN_STUDENT_MAJOR + " = " + "'" + value + "'"
        } else {
            // define select statement and store it in query String
            query = "SELECT * FROM " + TABLE_STUDENT +
                    " WHERE " + COLUMN_STUDENT_YEAR + " = " + "'" + value + "'"
        }

        // execute the select statement and store its return in an
        // immutable Cursor
        val c = db.rawQuery(query, null)

        // create MutableList of Students that will be
        // returned by the method
        val list: MutableList<Student> = ArrayList()

        // loop through the rows in the Cursor
        while (c.moveToNext()) {
            // create an immutable Student using the data in the current
            // row in the Cursor
            val student: Student = Student(c.getInt(c.getColumnIndex("_id")),
                c.getString(c.getColumnIndex("name")),
                c.getString(c.getColumnIndex("major")),
                c.getString(c.getColumnIndex("year")));
            // add the Student that was just created into the MutableList
            // of Students
            list.add(student)
        }

        // close database reference
        db.close()

        // return the MutableList of Students
        return list
    }

    companion object {
        // initialize constants for the DB name and version
        private const val DATABASE_NAME = "chcsearchapp.db"
        private const val DATABASE_VERSION = 1

        // initialize constants for the student table
        private const val TABLE_STUDENT = "student"
        private const val COLUMN_STUDENT_ID = "_id"
        private const val COLUMN_STUDENT_NAME = "name"
        private const val COLUMN_STUDENT_MAJOR = "major"
        private const val COLUMN_STUDENT_YEAR = "year"
    }
}