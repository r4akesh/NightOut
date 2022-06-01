package com.nightout.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.RegisterActivityBinding
import com.nightout.utils.MyApp
import com.nightout.vendor.viewmodel.RegViewModel

class RegisterActivity : BaseActivity() {
    lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RegisterActivity, R.layout.register_activity)
        initView()

        binding.registerPhNo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text: String = binding.registerPhNo.getText().toString()
                val textLength: Int = binding.registerPhNo.getText().toString().length
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    if (!text.contains("(")) {
                        binding.registerPhNo.setText(
                            StringBuilder(text).insert(text.length - 1, "(").toString()
                        )
                        binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                    }
                } else if (textLength == 5) {
                    if (!text.contains(")")) {
                        binding.registerPhNo.setText(
                            StringBuilder(text).insert(text.length - 1, ")").toString()
                        )
                        binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                    }
                } else if (textLength == 6) {
                    binding.registerPhNo.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                } else if (textLength == 10) {
                    if (!text.contains("-")) {
                        binding.registerPhNo.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                    }


                } else if (textLength == 15) {
                    if (text.contains("-")) {
                        binding.registerPhNo.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                    }
                } else if (textLength == 18) {
                    if (text.contains("-")) {
                        binding.registerPhNo.setText(
                            StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        binding.registerPhNo.setSelection(binding.registerPhNo.getText().length)
                    }
                }

            }
        })
    }

    private fun initView() {
        setTouchNClick(binding.registerSaveBtn)
        setTouchNClick(binding.reigisterActivityBakBtn)
        binding.registerHandler = MyApp.getRegHandler(this)
      //  binding.regviewmodel = ViewModelProviders.of(this).get(RegViewModel::class.java)
        binding.regviewmodel = RegViewModel(this)
    }

  /*  override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.registerSaveBtn) {
            startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
            overridePendingTransition(0, 0)
        } else if (v == binding.reigisterActivityBakBtn) {
            finish()
        }
    }*/
}