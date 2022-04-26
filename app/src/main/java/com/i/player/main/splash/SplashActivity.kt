package com.i.player.main.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.i.player.R
import com.i.player.databinding.ActivitySplashBinding
import com.i.player.main.BaseActivity
import com.i.player.main.MainScreen
import com.i.player.main.song_detail.SongDetailViewModel


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    private lateinit var songViewModel: SplashViewModel

    override fun getLayout() = R.layout.activity_splash

    override fun getViewModel(): SplashViewModel {
        return ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songViewModel= ViewModelProvider(this)
            .get(SplashViewModel::class.java)

        // Apply Binding
        mViewDataBinding.viewModel = mViewModel

        initObserver()

        // Perform Initialization
        mViewModel.preProcessing()
    }

    /**
     * Listen to ViewModel changes
     */
    private fun initObserver() {
        // Observe Sync Complete
        mViewModel.syncLiveData.observe(
            this,
            { _ ->
                startSongActivity()
            }
        )

        // Observe Error
        mViewModel.errorLiveData.observe(
            this,
            { error ->
                showErrorMessage(error)
            }
        )
    }

    private fun showErrorMessage(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    /**
     * Navigate to Song Listing Screen
     */
    private fun startSongActivity() {
        // Start Song Activity
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)

        // Finish Splash Activity. It should not open on back press
        finish()
    }
}