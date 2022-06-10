package com.jamal2367.minicarlauncher.presentation

import androidx.appcompat.app.AppCompatActivity

//Used to allow enabling/disabling intent-filter 'HOME' in runtime.
class HomeStubActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        MainActivity.launch(this)
    }
}
