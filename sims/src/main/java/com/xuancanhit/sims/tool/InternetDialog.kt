package com.xuancanhit.sims.tool

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import com.xuancanhit.sims.R
import kotlinx.android.synthetic.main.dialog_no_internet.*

class InternetDialog(val context: Context? = null) {


    private fun showNoInternetDialog() {
        val dialog1 = Dialog(context!!, R.style.df_dialog)
        dialog1.setContentView(R.layout.dialog_no_internet)
        dialog1.setCancelable(true)
        dialog1.setCanceledOnTouchOutside(true)
        dialog1.btnSpinAndWinRedeemInternetDialog.setOnClickListener {
            dialog1.dismiss()
        }
        dialog1.show()
    }

    //show no internet dialog
    val internetStatus: Boolean
        get() {
            val cm: ConnectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting()
            if (!isConnected) {
                //show no internet dialog
                showNoInternetDialog()
            }
            return isConnected
        }
}