package com.jamal2367.minicarlauncher.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.DataBindingUtil
import com.jamal2367.minicarlauncher.R
import com.jamal2367.minicarlauncher.databinding.ActivityMainBinding
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.LauncherFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    companion object {
        private const val INTENT_SELF_TERMINATE = "minicarlauncher.activity.terminateService"

        fun launch(ctx: Context) {
            val intent = Intent(ctx, MainActivity::class.java)
            ctx.startActivity(intent)
        }

    }

    private lateinit var launchActivityComponent: ComponentName

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchActivityComponent = ComponentName(this, HomeStubActivity::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.tbToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        handleIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_shortcuts_place, LauncherFragment())
            commitAllowingStateLoss()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean = false

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == INTENT_SELF_TERMINATE) {
            disableDefaultLauncher()
            finish()

        } else {
            enableDefaultLauncher()
        }
    }

    private fun disableDefaultLauncher() {
        toggleLaunchActivityState(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
    }

    private fun enableDefaultLauncher() {
        toggleLaunchActivityState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
    }

    private fun toggleLaunchActivityState(newState: Int) {
        val currentState = packageManager.getComponentEnabledSetting(launchActivityComponent)

        if (currentState != newState) {
            packageManager.setComponentEnabledSetting(
                launchActivityComponent,
                newState,
                PackageManager.DONT_KILL_APP
            )

            val callMainIntent = Intent(Intent.ACTION_MAIN)
            callMainIntent.addCategory(Intent.CATEGORY_HOME)
            startActivity(callMainIntent)
        }
    }
}
