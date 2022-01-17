package com.nightout.ui.activity.Prebooking

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.PrebookdetailActivityBinding
import com.nightout.model.PrebookedlistResponse
import com.nightout.utils.AppConstant

class PrebookedDetail : BaseActivity() {
    lateinit var binding: PrebookdetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(THIS!!, R.layout.prebookdetail_activity)

        setToolBar()
        var prebookedDetail= intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.PreBookDetail) as PrebookedlistResponse.PreBookingDetail
        setData()
    }

    private fun setData() {
         dsfdsfds
    }

    private fun setToolBar() {
        binding.prebookeddetailToolBar.toolbarBell.visibility= View.GONE
        binding.prebookeddetailToolBar.toolbar3dot.visibility= View.GONE
        binding.prebookeddetailToolBar.toolbarTitle.text = resources.getString(R.string.BookedVenue)
        binding.prebookeddetailToolBar.toolbarBack.setOnClickListener {
            finish()
        }
    }
}