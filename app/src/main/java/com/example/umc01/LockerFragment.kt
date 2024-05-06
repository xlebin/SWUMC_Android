package com.example.umc01

import LockerAlbumRVAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc01.databinding.FragmentAlbumBinding
import com.example.umc01.databinding.FragmentHomeBinding
import com.example.umc01.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private var lockeralbumDatas = ArrayList<LockerAlbum>()

    private val information = arrayListOf("저장한 곡", "음악파일")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {

        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)

        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
                tab,position ->
            tab.text = information[position]
        }.attach()

        //데이터 리스트 생성 더미 데이터

        lockeralbumDatas.apply {
            add(LockerAlbum("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(LockerAlbum("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(LockerAlbum("Sugarcoat", "NATTY(KISS OF LIFE)", R.drawable.img_album_exp3))
            add(LockerAlbum("Boy with Luv", "방탄소년단(BTS)", R.drawable.img_album_exp4))
            add(LockerAlbum("BBoom BBoom", "모모랜드(MOMOLAND)", R.drawable.img_album_exp5))
            add(LockerAlbum("Weekend", "태연(TAE YEON)", R.drawable.img_album_exp6))
            add(LockerAlbum("Hype Boy", "뉴진스(New Jeans)", R.drawable.nwjns))
        }


        val lockerRVAdapter = LockerAlbumRVAdapter(lockeralbumDatas)
        binding.lockerContentVpRecycler.adapter = lockerRVAdapter
        binding.lockerContentVpRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        lockerRVAdapter.setLockerItemClickListener(object :LockerAlbumRVAdapter.LockerItemClickListener{

            override fun onLockerItemClick(album: LockerAlbum) {
                TODO("Not yet implemented")
            }

            override fun onLockerRemoveSong(position: Int) {
                lockerRVAdapter.removeSong(position)
            }
        })

//        val lockerAdapter = LockerVPAdapter(this)
//        binding.lockerContentVp.adapter = lockerAdapter
//        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
//                tab,position ->
//            tab.text = information[position]
//        }.attach()
//        return binding.root
//    }

        return binding.root
    }
}
