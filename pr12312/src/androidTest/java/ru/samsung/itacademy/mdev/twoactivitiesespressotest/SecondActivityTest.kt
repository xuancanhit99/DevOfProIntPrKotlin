package ru.samsung.itacademy.mdev.twoactivitiesespressotest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Test


class SecondActivityTest {
    //----------------------------Test Second Activity-----------------------------

    //--check displayed--
    //check second activity displayed
    @Test
    fun checkActivityVisibility2() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.second))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check text "Reply Received" in TextView is visible
    @Test
    fun checkTextVisibility2() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.text_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check button is visible
    @Test
    fun checkButtonVisibility2() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.button_second))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check edit text is visible
    @Test
    fun checkEditTextVisibility2() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.editText_second))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    //--check text in Button, TextView = text setup in string.xml--
    //check text in View is right one
    @Test
    fun checkTextIsSecondActivity2() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.text_header))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.text_header)))
    }

    //check text(Button) in View is right one
    @Test
    fun checkTextIsMainActivity4() {
        val activityScenario = ActivityScenario.launch(SecondActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.button_second))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.button_second)))
    }
    //----------------------------End Test Second Activity-----------------------------





}