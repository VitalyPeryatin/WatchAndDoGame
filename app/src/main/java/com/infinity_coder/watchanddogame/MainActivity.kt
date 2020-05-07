package com.infinity_coder.watchanddogame

import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView.OnGesturePerformedListener
import android.gesture.Prediction
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var gestureLibrary: GestureLibrary? = null
    var currentGestureEntity: GestureEntity? = null

    private val handleGestureListener = OnGesturePerformedListener { _, gesture ->
        val predictions: List<Prediction> = gestureLibrary?.recognize(gesture) ?: emptyList()

        if (predictions.isNotEmpty()) {
            val prediction = predictions[0]
            if (prediction.score > 1.0 && prediction.name == currentGestureEntity?.gestureName) {
                Toast.makeText(this@MainActivity, prediction.name, Toast.LENGTH_SHORT).show()
                pickGesture()
            } else {
                Toast.makeText(this@MainActivity, "Неверно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    override fun onStart() {
        super.onStart()

        gestureLibrary = GestureLibraryStore.getInstance(this)
        gestureLibrary?.load()
        pickGesture()
    }

    private fun initUi() {
        setActionBar(mainToolbar, hasBackNavigation = false)

        recognizeGestureView.addOnGesturePerformedListener(handleGestureListener)
        recognizeGestureView.gestureStrokeAngleThreshold = 90.0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> openSettings()

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openSettings() {
        val intent = SettingsActivity.getIntent(this)
        startActivity(intent)
    }

    private fun pickGesture() {
        val gestureList: MutableList<GestureEntity> = mutableListOf()
        val gestureSet: Set<String> = gestureLibrary?.gestureEntries ?: emptySet()
        for (gestureName in gestureSet) {
            val gestures: List<Gesture> = gestureLibrary?.getGestures(gestureName) ?: emptyList()
            val gestureEntities = gestures.map { gesture ->
                GestureEntity(gestureName, gesture)
            }
            gestureList.addAll(gestureEntities)
        }

        currentGestureEntity = try {
            val randomPosition = Random.nextInt(0, gestureList.size)
            gestureList[randomPosition]
        }
        catch (e: Exception) {
            null
        }

        if (currentGestureEntity != null) {
            val currentGesture = currentGestureEntity!!.gesture
            val gestureBitmap = currentGesture.toBitmap(100, 100, 3, Color.YELLOW)
            previewImageView.setImageBitmap(gestureBitmap)

            gestureNameTextView.text = currentGestureEntity?.gestureName
        } else {
            previewImageView.setBackgroundColor(Color.WHITE)
            Toast.makeText(this, "В списке жестов не найдено ни одного жеста", Toast.LENGTH_SHORT).show()
        }
    }
}
