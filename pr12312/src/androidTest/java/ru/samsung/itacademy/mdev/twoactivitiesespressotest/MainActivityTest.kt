package ru.samsung.itacademy.mdev.twoactivitiesespressotest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {


    //-----------------------------------Test Main Activity-------------------------------
    //--check displayed--
    //check main activity displayed
    @Test
    fun checkActivityVisibility1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    //check text "Reply Received" in TextView is visible
    @Test
    fun checkTextVisibility1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.text_header_reply)).check(matches(isDisplayed()))
    }

    //check button is visible
    @Test
    fun checkButtonVisibility1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_main)).check(matches(isDisplayed()))
    }

    //check edit text is visible
    @Test
    fun checkEditTextVisibility1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.editText_main)).check(matches(isDisplayed()))
    }


    //--check text in Button, TextView = text setup in string.xml--
    //check text in View is right one
    @Test
    fun checkTextIsMainActivity1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.text_header_reply)).check(matches(withText(R.string.text_header_reply)))
    }


    //check text(Button) in View is right one
    @Test
    fun checkTextIsMainActivity3() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_main)).check(matches(withText(R.string.button_main)))
    }

    //----------------------------End Test Main Activity-----------------------------

    //------------------------Test click Button-----------------------
    //test button(Main Activity) if after a click we will navigate to second Activity
    //and test if view(second activity will be displayed)
    @Test
    fun navigateTo2Activity1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_main))
            .perform(click())

        //with click everything is alright
        //now we will check if our second activity is visible

        onView(withId(R.id.second)).check(matches(isDisplayed()))

        //test button(Second Activity and after click go to Main Activity
        onView(withId(R.id.button_second))
            .perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }


    //check onBack
    @Test
    fun backPressToMainActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_main))
            .perform(click())
        onView(withId(R.id.second))
            .check(matches(isDisplayed()))

        //check back(button mobile android button hardware)
        //when is SecondActivity
        Espresso.pressBack()

        //after click on button to back
        //main activity must be visible
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
    //------------------------End Test click Button-----------------------

    //-----------------------Pass Data--------------------------
    @Test
    fun passDataFromMainToSecond() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        val dataPass = "Some Text Data"
        //check Second Activity visible
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        //Set data text want pass to edittext
        onView(withId(R.id.editText_main))
            .perform(clearText(), typeText(dataPass));

        //Click button to pass data
        onView(withId(R.id.button_main))
            .perform(click())

        //check Second Activity visible
        onView(withId(R.id.second)).check(matches(isDisplayed()))

        //check data(from main activity send to second) in text view(text messenger)
        onView(withId(R.id.text_message)).check(matches(withText(dataPass)))
    }

    @Test
    fun passDataFromSecondToMain() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        val dataPass = "Some Text Data"

        //Click button to Second
        onView(withId(R.id.button_main))
            .perform(click())

        //check Second Activity visible
        onView(withId(R.id.second)).check(matches(isDisplayed()))

        //Set data text want pass to Main
        onView(withId(R.id.editText_second))
            .perform(clearText(), typeText(dataPass));

        //Click button to PassData from Second To Main
        onView(withId(R.id.button_second))
            .perform(click())

        //check data(from second activity send to main) in text view(text messenger reply)
        onView(withId(R.id.text_message_reply)).check(matches(withText(dataPass)))
    }


    //Check reset edittext(main) = empty when pass data to second and back to main
    @Test
    fun checkEmptyEditTextMainActivity() {

        //Pass data to second (data = "Some Text Data")
        passDataFromMainToSecond()

        //Here second
        //Click button for go to main
        onView(withId(R.id.button_second))
            .perform(click())

        //Here main
        //Check edit text main
        onView(withId(R.id.editText_main)).check(matches(withText("")))

        // => will failed because EditText(MainActivity) should be empty when User back
    }

    //-----------------------End Pass Data--------------------------

}