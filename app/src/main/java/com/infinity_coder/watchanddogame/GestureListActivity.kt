package com.infinity_coder.watchanddogame

import android.content.Context
import android.content.Intent
import android.gesture.Gesture
import android.gesture.GestureLibrary
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_gesture_list.*


class GestureListActivity : AppCompatActivity() {

    private var gestureAdapter: GestureAdapter? = null
    private lateinit var gestureLibrary: GestureLibrary

    private val itemClickListener = object : GestureAdapter.OnItemClickListener {
        override fun onDelete(gestureEntity: GestureEntity) {
            gestureLibrary.removeGesture(gestureEntity.gestureName, gestureEntity.gesture)
            gestureLibrary.save()
            fillGestureList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_list)
        gestureLibrary = GestureLibraryStore.getInstance(this)
        gestureLibrary.load()

        initUi()

        fillGestureList()
    }

    private fun initUi() {
        gestureListToolbar.setTitle(R.string.gesture_list_title)
        setActionBar(gestureListToolbar, true)

        gestureAdapter = GestureAdapter(itemClickListener)
        gestureRecyclerView.adapter = gestureAdapter
        gestureRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun fillGestureList() {
        val gestureList = ArrayList<GestureEntity>()
        val gestureSet: Set<String> = gestureLibrary.gestureEntries ?: emptySet()
        for (gestureName in gestureSet) {
            val gestures: List<Gesture> = gestureLibrary.getGestures(gestureName) ?: emptyList()
            val gestureEntities = gestures.map { gesture ->
                GestureEntity(gestureName, gesture)
            }
            gestureList.addAll(gestureEntities)
        }
        gestureAdapter?.setGestures(gestureList)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, GestureListActivity::class.java)
        }
    }
}