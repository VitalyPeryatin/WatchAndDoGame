package com.infinity_coder.watchanddogame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initUi()
    }

    private fun initUi() {
        settingsToolbar.setTitle(R.string.settings_title)
        setActionBar(settingsToolbar, true)

        addGestureLayout.setOnClickListener {
            openAddGestureActivity()
        }

        gestureListLayout.setOnClickListener {
            openGestureListActivity()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openAddGestureActivity() {
        val intent = AddGestureActivity.getIntent(this)
        startActivity(intent)
    }

    private fun openGestureListActivity() {
        val intent = GestureListActivity.getIntent(this)
        startActivity(intent)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}