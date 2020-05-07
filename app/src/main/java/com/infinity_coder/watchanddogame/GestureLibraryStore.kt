package com.infinity_coder.watchanddogame

import android.content.Context
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import java.io.File
import java.io.FileOutputStream

object GestureLibraryStore {

    private var gestureLibrary: GestureLibrary? = null

    fun getInstance(context: Context): GestureLibrary {
        if (gestureLibrary != null) return gestureLibrary!!

        val gestureFile = File("${context.filesDir.path}/gesture.txt")
        if (!gestureFile.exists()) {
            val defaultGestureFile = context.assets.open("gesture.txt")
            defaultGestureFile.copyTo(FileOutputStream(gestureFile))
        }
        gestureLibrary = GestureLibraries.fromFile(gestureFile)
        return gestureLibrary!!
    }
}