package com.xuancanhit.sims.tool.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.xuancanhit.sims.R
import kotlinx.android.synthetic.main.dialog_email.*

class EmailDialog(val context: Context? = null){
    @SuppressLint("SetTextI18n")
    fun showVerifyEmailDialog(title:String, message:String) {
        val dialog1 = Dialog(context!!, R.style.df_dialog)
        dialog1.setContentView(R.layout.dialog_email)
        dialog1.tv_dialog_email_title.text = title
        dialog1.tv_dialog_email_message.text = message
        dialog1.setCancelable(true)
        dialog1.setCanceledOnTouchOutside(true)
        dialog1.btnSpinAndWinRedeemEmailDialog.setOnClickListener {
            dialog1.dismiss()
        }
        dialog1.show()
    }
}