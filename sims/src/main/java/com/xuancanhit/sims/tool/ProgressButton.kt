package com.xuancanhit.sims.tool

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.xuancanhit.sims.R

class ProgressButton
    (
    private var cardView: CardView? = null,
    private var constraintLayout: ConstraintLayout? = null,
    private var progressBar: ProgressBar? = null,
    private var textView: TextView? = null,

    ) {

    @SuppressLint("SetTextI18n")
    fun buttonActivated(text: String = "Running", ani: Animation? = null) {
        if (ani != null) {
            progressBar!!.startAnimation(ani)
        }
        progressBar!!.visibility = View.VISIBLE
        textView!!.text = text
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun buttonFinished(text: String = "Successful",  ani: Animation? = null) {
        if (ani != null) {
            progressBar!!.startAnimation(ani)
            constraintLayout!!.startAnimation(ani)
        }
        progressBar!!.visibility = View.GONE
        constraintLayout!!.setBackgroundColor(cardView!!.resources.getColor(R.color.green))
        textView!!.text = text
    }

    @SuppressLint("SetTextI18n")
    fun buttonReset(text: String = "Error",  ani: Animation? = null) {
        if (ani != null) {
            progressBar!!.startAnimation(ani)
            constraintLayout!!.startAnimation(ani)
        }
        constraintLayout!!.setBackgroundColor(cardView!!.resources.getColor(R.color.button_blue_color))
        progressBar!!.visibility = View.GONE
        textView!!.text = text
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun buttonError(text: String = "Error",  ani: Animation? = null) {
        if (ani != null) {
            progressBar!!.startAnimation(ani)
            constraintLayout!!.startAnimation(ani)
        }
        progressBar!!.visibility = View.GONE
        constraintLayout!!.setBackgroundColor(cardView!!.resources.getColor(R.color.red))
        textView!!.text = text
    }


}
