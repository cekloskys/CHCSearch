package com.example.chcsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // declare DBHandler as mutable field using null safety
    var dbHandler: DBHandler? = null

    // declare EditTexts as mutable fields using null safety
    var majorEditText: EditText? = null
    var yearEditText: EditText? = null

    // declare RecyclerView as mutable field using null safety
    var studentRecyclerView: RecyclerView? = null

    // declare a StudentAdapter as a mutable field
    // specify that it will be initialized later
    lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fully initialize dbHandler
        dbHandler = DBHandler(this, null)

        // make EditTexts refer to actual EditTexts in activity_main layout resource
        majorEditText = findViewById<View>(R.id.majorEditText) as EditText
        yearEditText = findViewById<View>(R.id.yearEditText) as EditText

        // make RecyclerView refer to actual RecyclerView in activity_main layout resource
        studentRecyclerView = findViewById<View>(R.id.studentRecyclerView) as RecyclerView

        // initialize a MutableList of Students
        var students: MutableList<Student> = ArrayList()

        // initialize the StudentAdapter
        studentAdapter = StudentAdapter(students)

        // tell Kotlin that RecyclerView isn't null and set the StudentAdapter on it
        studentRecyclerView!!.adapter = studentAdapter

        // tell Kotlin that the RecylerView isn't null and apply a LinearLayout to it
        studentRecyclerView!!.layoutManager = LinearLayoutManager(this)

        // populate the student table in the database
        // these lines of code should be commented out after the
        // app is run the first time
        addStudent("Sean Plunkett", "CIS", "Junior")
        addStudent("Shaquan Patterson", "CIT", "Senior")
        addStudent("Michael Gipson", "CIT", "Senior")
        addStudent("Mustaf Nuredini", "CIS", "Junior")
        addStudent("Yassin Alabdulaziz", "CIT", "Sophomore")
        addStudent("Dwan Conyers", "CIT", "Sophomore")
    }

    /**
     * This method populates a student in the database.  It gets called when
     * the app launches.
     * @param name student name
     * @param major student major
     * @param year student year
     */
    fun addStudent(name: String, major: String, year: String) {
        dbHandler?.addStudent(name, major, year)
    }

    /**
     * This method gets called when the Search button is clicked.  It
     * searches for students based on the specified search criteria and
     * refreshes the StudentAdapter with the retrieved data so that it
     * may be displayed in the RecyclerView.
     * @param view MainActivity
     */
    fun search(view: View?){

        // get values input in EditTexts
        // store them in variables
        val major = majorEditText!!.text.toString()
        val year = yearEditText!!.text.toString()

        // trim variables and check if they are both equal to empty Strings
        if (major.trim() == "" && year.trim() == ""){
            // display "Please enter a search criteria! Toast
            Toast.makeText(this, "Please enter a search criteria!", Toast.LENGTH_LONG).show()
        } else if (major.trim() != "" && year.trim() != ""){
            // trim variables and check if neither are equal to an empty String
            // display "Please enter only one search criteria! Toast
            Toast.makeText(this, "Please enter only one search criteria!", Toast.LENGTH_LONG).show()
            // clear major EditText
            majorEditText!!.text.clear()
            // clear year EditText
            yearEditText!!.text.clear()
        } else {
            if (major.trim() != "") {
                // trim major and check if it's not equal to an empty String
                // call search by major in StudentAdapter
                studentAdapter.search(dbHandler!!, "major", major)
                // refresh StudentAdapter Mutable List
                studentAdapter.students = dbHandler!!.search("major", major)
                // clear major EditText
                majorEditText!!.text.clear()
            } else {
                // call search by year in StudentAdapter
                studentAdapter.search(dbHandler!!, "year", year)
                // refresh StudentAdapter Mutable List
                studentAdapter.students = dbHandler!!.search("year", year)
                // clear year EditText
                yearEditText!!.text.clear()
            }
        }
    }
}