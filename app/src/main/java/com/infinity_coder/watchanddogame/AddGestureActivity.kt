package com.infinity_coder.watchanddogame

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_gesture.*
import kotlinx.android.synthetic.main.activity_settings.*


class AddGestureActivity : AppCompatActivity() {

    private lateinit var gestureLibrary: GestureLibrary
    private var isGestureDrawn = false
    private var currentGesture : Gesture? = null
    private var gestureName : String = ""

    private val gestureListener: GestureOverlayView.OnGestureListener = object : GestureOverlayView.OnGestureListener {
        override fun onGestureStarted(overlay: GestureOverlayView, event: MotionEvent) {
            isGestureDrawn = true
        }

        override fun onGesture(overlay: GestureOverlayView, event: MotionEvent) {
            currentGesture = overlay.gesture
        }

        override fun onGestureEnded(gestureView: GestureOverlayView, motion: MotionEvent) {
        }

        override fun onGestureCancelled(overlay: GestureOverlayView, event: MotionEvent) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gesture)

        gestureLibrary = GestureLibraryStore.getInstance(this)
        gestureLibrary?.load()

        resetEverything()

        initUi()
    }

    private fun initUi() {
        addGestureToolbar.setTitle(R.string.add_gesture_title)
        setActionBar(addGestureToolbar, true)
        addGestureOverlayView.addOnGestureListener(gestureListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_gesture, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteItem -> clearGestureView()

            R.id.saveItem -> inputName()

            android.R.id.home -> onBackPressed()

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun clearGestureView() {
        resetEverything()
        addGestureOverlayView.clear(false)
    }

    private fun inputName() {

        if (currentGesture?.strokes == null) {
            showToast("First, draw a sketch")
            return
        }

        val namePopup: AlertDialog.Builder = AlertDialog.Builder(this)
        namePopup.setTitle("Gesture name")
        val nameField = EditText(this)
        namePopup.setView(nameField)
        namePopup.setPositiveButton("Save") { _, i ->
            if (!nameField.text.toString().matches(Regex(""))) {
                gestureName = nameField.text.toString()
                saveGesture()
            } else {
                inputName()
                showToast("Invalid name")
            }
        }
        namePopup.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { _, i ->
                gestureName = ""
                return@OnClickListener
            })
        namePopup.show()
    }

    private fun saveGesture() {
        gestureLibrary.addGesture(gestureName, currentGesture)
        if (gestureLibrary.save()) {
            showToast("Gesture saved")
            clearGestureView()
        } else {
            showToast("Gesture not saved!")
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun resetEverything() {
        isGestureDrawn = false
        currentGesture = null
        gestureName = ""
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AddGestureActivity::class.java)
        }
    }
}