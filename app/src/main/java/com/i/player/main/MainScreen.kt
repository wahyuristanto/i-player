package com.i.player.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.i.player.databinding.ActivityMainScreenBinding
import com.i.player.main.song_detail.SongDetailActivity
import com.i.player.main.song_detail.SongDetailViewModel
import com.i.player.models.SongModel

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var mainScreenViewModel: MainScreenViewModel
    private lateinit var songDetailViewModel: SongDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainScreenViewModel = ViewModelProvider(this)
            .get(MainScreenViewModel::class.java)
        songDetailViewModel= ViewModelProvider(this)
            .get(SongDetailViewModel::class.java)



        with(binding){
            search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.e("_onQueryTextChange", newText.toString())
                    mainScreenViewModel.getFeed(newText)
                    return false
                }
            })
        }
        mainScreenViewModel.listFeed.observe(this) {
            with(binding) {

                it?.let { data ->
                    when (data.size > 0) {
                        true -> {
                            listSongs?.visibility = View.VISIBLE
                            animationView?.visibility = View.GONE
                        }
                        false -> {
                            listSongs?.visibility = View.GONE
                            animationView.visibility = View.VISIBLE
                        }
                    }
                }

                val listAdapter = MainScreenAdapter()
                listSongs.layoutManager = LinearLayoutManager(this@MainScreen)
                listSongs.adapter = listAdapter
                listAdapter.setData(it)
                listAdapter.setOnClickItemCallback(object : MainScreenAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: SongModel) {
                        val intent = Intent(this@MainScreen, SongDetailActivity::class.java)
                        intent.putExtra(SongDetailActivity.EXTRA_SONG_LIST, it)
                        intent.putExtra(SongDetailActivity.EXTRA_SONG, data)
                        startActivity(intent)
                    }
                })
            }
        }


    }


}