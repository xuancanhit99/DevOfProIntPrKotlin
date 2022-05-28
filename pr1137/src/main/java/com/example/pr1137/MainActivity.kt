package com.example.pr1137

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat


class MainActivity : AppCompatActivity(), Communicator {

    private lateinit var v1: View
    private val fragmentManager = supportFragmentManager
    private var h = 0
    private var m = 0
    private var sun = false
    private var moon = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }
    private fun initView() {
        // View (Sub-Class) where onTouchEvent is implemented
        v1 = findViewById(R.id.main_view)
        onTouch()
    }
    private fun passDataToFragmentAndReset() {
        passDataComm(h, m, sun, moon)
        h = 0
        m = 0
        sun = false
        moon = false
    }
    private fun testTimePickerDialog() {
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mCurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute -> //Toast.makeText(this, "$selectedHour:$selectedMinute", Toast.LENGTH_SHORT).show()
                h = selectedHour
                m = selectedMinute
                passDataToFragmentAndReset()
                //Toast.makeText(this, "$h $m Солнце днем:$sun Лунаизвезды ночью:$moon", Toast.LENGTH_SHORT).show()
            },
            hour,
            minute,
            true
        ) //Yes 24 hour time
        mTimePicker.show()
    }

    private fun testCheckboxDialog() {
        // Set up the alert builder
        val builder = AlertDialog.Builder(this)
        //builder.setTitle("Choose some animals")
        // Add a checkbox list
        val status = arrayOf("Лунаизвезды ночью", "Солнце днем")
        val checkedItems = booleanArrayOf(false, false)
        builder.setMultiChoiceItems(status, checkedItems) { _, which, isChecked ->
            // The user checked or unchecked a box
            when (which) {
                0 -> moon = isChecked
                //Toast.makeText(this, "Moon And Start ON", Toast.LENGTH_SHORT).show()
                1 -> sun = isChecked
                //Toast.makeText(this, "Солнце днем ON", Toast.LENGTH_SHORT).show()
                //else -> Toast.makeText(this, "ALL OFF", Toast.LENGTH_SHORT).show()
            }
        }

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK") { _, _ ->
            // The user clicked OK
            testTimePickerDialog()
        }
        //builder.setNegativeButton("Cancel", null)

        // Create and show the alert dialog
        val dialog = builder.create()
        //dialog.setCanceledOnTouchOutside(false);
        dialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onTouch() {
        // OnTouchListener on the selected view
        v1.setOnTouchListener { _, event ->
            return@setOnTouchListener when (MotionEventCompat.getActionMasked(event)) {
                MotionEvent.ACTION_DOWN -> {
                    // Make a Toast when movements captured on the sub-class
                    //Toast.makeText(applicationContext, "Move", Toast.LENGTH_SHORT).show()
                    testCheckboxDialog()
                    true
                }
                else -> false
            }
        }
    }
    override fun passDataComm(hour: Int, minute: Int, sun: Boolean, moonAndStars: Boolean) {
        val bundle = Bundle()
        bundle.putInt("HOUR", hour)
        bundle.putInt("MINUTE", minute)
        bundle.putBoolean("SUN", sun)
        bundle.putBoolean("MOON_AND_STARS", moonAndStars)

        val testFragment = TestFragment()
        testFragment.arguments = bundle

        Log.d("LOG", testFragment.arguments.toString())

        val fragmentTransactionOnClick = fragmentManager.beginTransaction()
        fragmentTransactionOnClick.replace(R.id.fragment, testFragment)
        fragmentTransactionOnClick.addToBackStack(null)
        fragmentTransactionOnClick.commit()
    }
}