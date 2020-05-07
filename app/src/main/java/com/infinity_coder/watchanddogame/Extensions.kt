package com.infinity_coder.watchanddogame

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

fun AppCompatActivity.setActionBar(toolbar: Toolbar, hasBackNavigation: Boolean = false) {
    setSupportActionBar(toolbar)
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.actionBarItemBackground))
    if (hasBackNavigation) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    }
}