package com.nightout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nightout.R
import com.nightout.adapter.VenuAdapterAdapter
import com.nightout.databinding.FragmentBarcrawlnewBinding
import com.nightout.interfaces.OnMenuOpenListener
import com.nightout.ui.activity.AddBarCrawlActvity
import com.nightout.ui.activity.BarCrawlSaveActivity
import com.nightout.ui.activity.BarCrawlSavedListActivity
import com.nightout.ui.activity.BarCrawlShredListActivity
import com.nightout.ui.activity.barcrawl.SearchCityActivity

class BarCrwalFragment() : Fragment() , View.OnClickListener {

    lateinit var binding : FragmentBarcrawlnewBinding

    private var onMenuOpenListener: OnMenuOpenListener? = null

    constructor(onMenuOpenListener: OnMenuOpenListener) : this() {
        this.onMenuOpenListener = onMenuOpenListener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcrawl, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcrawlnew, container, false)
        initView()
       // setlistTop()

        return binding.root
    }

    override fun onClick(v: View?) {
        /* if(v==binding.menuOpenBtnBar){
             onMenuOpenListener?.onOpenMenu()
         }

         else if(v==binding.barcrawlSharBarCrawal){
             startActivity(Intent(requireActivity(), ListParticipteActvity::class.java))
         }*/

        if(v==binding.createBtn){
            //startActivity( Intent (requireActivity(),AddBarCrawlActvity::class.java))
            startActivity( Intent (requireActivity(),SearchCityActivity::class.java))
        }
        else if(v==binding.savedBtn){
            startActivity( Intent (requireActivity(), BarCrawlSavedListActivity::class.java))
        }

        else if(v==binding.sharedBtn){
            startActivity( Intent (requireActivity(), BarCrawlShredListActivity::class.java))
        }
    }


    /*private fun setlistTop() {
        var listStoreType = ArrayList<VenuModel>()
        listStoreType.add(VenuModel(3,"Club", false))
        listStoreType.add(VenuModel(1,"Bar", false))
        listStoreType.add(VenuModel(2,"Pub", false))
        listStoreType.add(VenuModel(4,"Food", false))
        listStoreType.add(VenuModel(5,"Event", false))


         venuAdapterAdapter = VenuAdapterAdapter(requireContext(),listStoreType,  object : VenuAdapterAdapter.ClickListener {
                override fun onClick(pos: Int) {
                    for (i in 0 until listStoreType.size) {
                        listStoreType[i].isSelected = pos == i
                    }
                    venuAdapterAdapter.notifyDataSetChanged()
                }

            })

        binding.fragmentBatRecyclerTop.also {
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = venuAdapterAdapter
        }
    }*/

    private fun initView() {
        binding.menuOpenBtnBar.setOnClickListener { onMenuOpenListener?.onOpenMenu() }
        binding.savedBtn.setOnClickListener(this)
        binding.createBtn.setOnClickListener(this)
        binding.sharedBtn.setOnClickListener(this)
    }






}