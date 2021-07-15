package com.nightout.ui.activity

import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.StoredetailActivityBinding
import kotlinx.android.synthetic.main.discount_desc.view.*


class StoreDetail : BaseActivity() {
    lateinit var binding: StoredetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.storedetail_activity)
        binding = DataBindingUtil.setContentView(this@StoreDetail, R.layout.storedetail_activity)

        initView()

    }

    private fun initView() {
         setTouchNClick(binding.storeDeatilMenu)
         setTouchNClick(binding.storeDeatilDiscount)
         setTouchNClick(binding.storeDeatilMore)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.storeDeatilMenu){
            binding.storeDeatilMenu.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))

            binding.storeDeatilMenuDesc.visibility=VISIBLE
            binding.storeDeatilDiscount.visibility=GONE
        }
       else if(v==binding.storeDeatilDiscount){
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.black))
            binding.storeDeatilMore.setBackgroundResource(0)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.white_second))

            val str1 = resources.getString(R.string.discount10)
            var str2 = resources.getString(R.string.firsLine)
            var settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.firstLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

              str2 = resources.getString(R.string.secondLine)
              settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.secondLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)


            str2 = resources.getString(R.string.thridLine)
            settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.thrdLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

            str2 = resources.getString(R.string.firsLine)
            settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fourthLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

            str2 = resources.getString(R.string.secondLine)
            settext = "<font color='#087d19'>$str1 </font> <font color='#D4D4D4'> <b>$str2</b></font>"
            binding.storeDeatilDisDesc.fifthLine.setText(Html.fromHtml(settext), TextView.BufferType.SPANNABLE)

            binding.storeDeatilMenuDesc.visibility=GONE
            binding.storeDeatilDiscount.visibility=VISIBLE
        }
       else if(v==binding.storeDeatilMore){
            binding.storeDeatilMenu.setBackgroundResource(0)
            binding.storeDeatilMenu.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilDiscount.setBackgroundResource(0)
            binding.storeDeatilDiscount.setTextColor(resources.getColor(R.color.white_second))
            binding.storeDeatilMore.setBackgroundResource(R.drawable.box_yelo)
            binding.storeDeatilMore.setTextColor(resources.getColor(R.color.black))
        }
    }
}