package com.nightout.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.nightout.R
import com.nightout.databinding.ImageViewItemBinding
import com.nightout.model.DashboardModel
import com.nightout.model.VenuDetailModel
import com.nightout.model.VenueGallery
import com.nightout.ui.activity.VideoPlayActvity
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.PreferenceKeeper
import com.nightout.utils.Utills


class ImageViewPagerAdapter(private val context: Context, imageArray: MutableList<VenuDetailModel.VenueGallery>) :
    PagerAdapter() {
    private var imageArray: MutableList<VenuDetailModel.VenueGallery> = imageArray

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ImageViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.image_view_item, container, false
        )

        binding.dVenuGalyViewModel = imageArray[position]
        if(imageArray[position].type.equals(context.resources.getString(R.string.typeFile))){
            //0 pic   1 video
            Utills.setImageNormal(context,binding.imageView, imageArray[position].thumbnail)
        }else{
            Utills.setImageNormal(context,binding.imageView, imageArray[position].image)
        }


         binding.postThumIv.setOnClickListener {
             var vPath=PreferenceKeeper.instance.imgPathSave+imageArray[position].image
             if( vPath.isNullOrBlank()) {
                 MyApp.popErrorMsg("","Video Not Found!!",context)
             }
             else{
                 context.startActivity(Intent(context, VideoPlayActvity::class.java)
                     .putExtra(AppConstant.INTENT_EXTRAS.VIDEO_URL, vPath))

             }
          //  context.callVideo(imageArray[position].image)
        }
       /* if (imageArray[position].type != "mp4")
            binding.imageView.setOnClickListener {
                context.callImage(imageArray[position].image)
            } */
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageArray.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

}