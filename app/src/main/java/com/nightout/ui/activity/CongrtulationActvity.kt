package com.nightout.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.databinding.CongrtulationActivityBinding
import com.nightout.utils.AppConstant
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class CongrtulationActvity : BaseActivity() {
    lateinit var binding: CongrtulationActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@CongrtulationActvity, R.layout.congrtulation_activity)
        initView()
        setAnimation()
        var congtresMsg=intent.getStringExtra(AppConstant.INTENT_EXTRAS.CONGRETS_MSG)
        binding.congrtulationActvityOrder.text=congtresMsg
    }

    private fun setAnimation() {
        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(500)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
            //.streamFor(300, 5000L)
            .streamFor(particlesPerSecond = 500,1000)
    }

    private fun initView() {
         setTouchNClick(binding.congrtulationActvityFeedback)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if(v==binding.congrtulationActvityFeedback){
//            val i = Intent(this, HomeActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(i)
            finish()
            overridePendingTransition(0,0)
        }
    }
}