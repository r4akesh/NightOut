package com.nightout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nightout.R
import com.nightout.adapter.StoryAdapter
import com.nightout.adapter.VenuTitleBotmSheetAdapter
import com.nightout.databinding.FragmentHomeBinding
import com.nightout.model.StoryModel
import com.nightout.model.VenuBotmSheetModel
import com.nightout.model.VenuBotmSheetTitleModel
import com.nightout.model.VenuListModel
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.*

class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        bottomSheetBehavior =BottomSheetBehavior.from(binding.btmShhetInclue.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
       // bottomSheetBehavior.isHideable = false
      //  bottomSheetBehavior.peekHeight = resources.getDimension(R.dimen._100sdp).toInt()
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })

        setListStoryDummy()
        setListVenuListBootmShhetDuumy()

        return binding.root
    }

    lateinit var venuTitleBotmSheetAdapter: VenuTitleBotmSheetAdapter
    private fun setListVenuListBootmShhetDuumy() {
        var listTile = ArrayList<VenuBotmSheetTitleModel>()
        var listSub = ArrayList<VenuBotmSheetModel>()
        listSub.add(VenuBotmSheetModel("Vanity Night Clubs","1 Fairclough St, Liverpool",R.drawable.venusub_img1))
        listTile.add(VenuBotmSheetTitleModel("Club",listSub))

        listSub.add(VenuBotmSheetModel("Raise a Glass","Liverpool 1 Fairclough St",R.drawable.venusub_img2))
        listTile.add(VenuBotmSheetTitleModel("Bars",listSub))

        listSub.add(VenuBotmSheetModel("Neon Nights","25 Fairclough St, Lverol",R.drawable.venusub_img3))
        listTile.add(VenuBotmSheetTitleModel("Pubs",listSub))
        venuTitleBotmSheetAdapter = VenuTitleBotmSheetAdapter(requireContext(),listTile,object :VenuTitleBotmSheetAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        } )

        binding.btmShhetInclue.bottomSheetRecycleVenuList.also {
            it.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
            it.adapter=venuTitleBotmSheetAdapter
        }
    }

    lateinit var storyAdapter: StoryAdapter
    private fun setListStoryDummy() {
        var listStory = ArrayList<StoryModel>()
        listStory.add(StoryModel("Raise Glass",R.drawable.stry_img1))
        listStory.add(StoryModel("Vanity Night",R.drawable.stry_img2))
        listStory.add(StoryModel("Selbys Store",R.drawable.stry_img3))
        listStory.add(StoryModel("Feel the",R.drawable.stry_img4))
        listStory.add(StoryModel("Raise Glass",R.drawable.stry_img1))
        listStory.add(StoryModel("Vanity Night",R.drawable.stry_img2))
        listStory.add(StoryModel("Selbys Store",R.drawable.stry_img3))
        listStory.add(StoryModel("Feel the",R.drawable.stry_img4))
        storyAdapter= StoryAdapter(requireActivity(),listStory , object: StoryAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.btmShhetInclue.bottomSheetRecyclerstory.also{
            it.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            it.adapter = storyAdapter
        }

    }


}