package com.nightout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightout.R
import com.nightout.base.BaseActivity

import com.nightout.databinding.LoginActivityBinding
import com.nightout.model.FSUsersModel
import com.nightout.model.LoginModel
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.vendor.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.discount_desc.view.*


class LoginActivity : BaseActivity()  {
    lateinit var binding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.login_activity)
        initView()





        binding.loginPhno.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text: String = binding.loginPhno.getText().toString()
                val textLength: Int = binding.loginPhno.getText().toString().length
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    if (!text.contains("(")) {
                        binding.loginPhno.setText(
                            StringBuilder(text).insert(text.length - 1, "(").toString()
                        )
                        binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                    }
                } else if (textLength == 5) {
                    if (!text.contains(")")) {
                        binding.loginPhno.setText(
                            StringBuilder(text).insert(text.length - 1, ")").toString()
                        )
                        binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                    }
                } else if (textLength == 6) {
                    binding.loginPhno.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                } else if (textLength == 10) {
                    if (!text.contains("-")) {
                        binding.loginPhno.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                    }


                } else if (textLength == 15) {
                    if (text.contains("-")) {
                        binding.loginPhno.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                    }
                } else if (textLength == 18) {
                    if (text.contains("-")) {
                        binding.loginPhno.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.loginPhno.setSelection(binding.loginPhno.getText().length)
                    }
                }

            }
        })
    }

    private fun initView() {
        setTouchNClick(binding.loginBtn)
        setTouchNClick(binding.loginActvitySignUp)
        val str1 = resources.getString(R.string.Sign_Up)
        var settext = "<font color='#ffc800'><u>$str1 </u></font>"
//        val str1 = resources.getString(R.string.discount10)
//        var str2 = resources.getString(R.string.firsLine)
//        var settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
        binding.loginActvitySignUp.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)
        binding.loginHandler = MyApp.getLoginHandler(this)
        binding.loginViewModel = LoginViewModel(this)

    }




}