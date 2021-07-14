package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.utils.ExceptionHandler

class SplashActivity : BaseActivity() {
    private var crash: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(  R.layout.activity_splash)
        crash = intent.getStringExtra(ExceptionHandler.CRASH_REPORT)

        if (crash == null) {
            startSplash()
        } else {
            showCrashDialog("" + crash)
        }

    }

    private fun startSplash() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }, 100)
    }

    fun showCrashDialog(report: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle("App Crashed " + resources.getString(R.string.app_name))
        builder1.setMessage("Oops! The app crashed due to below reason:\n\n$report")
        builder1.setCancelable(true)
        @Suppress("UNUSED_PARAMETER")
        builder1.setPositiveButton(
            "Send Report"
        ) { dialog, id ->
            dialog.cancel()
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/html"
            i.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("rakeshkushwaha@emails.emizentech.com")
            )
            i.putExtra(Intent.EXTRA_TEXT, report)
            i.putExtra(Intent.EXTRA_SUBJECT, "App Crashed")
            startActivity(Intent.createChooser(i, "Send Mail via:"))
            finish()
        }

        builder1.setNegativeButton("Restart") { dialog, id ->
            dialog.cancel()
            startSplash()
        }

        val alert11 = builder1.create()
        alert11.show()
    }
}