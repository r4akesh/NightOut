package com.nightout.ui

import android.os.Bundle
import com.nightout.R
import com.nightout.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.login_activity)
    }
}