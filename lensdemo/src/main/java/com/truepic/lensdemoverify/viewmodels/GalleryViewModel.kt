package com.truepic.lensdemoverify.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.truepic.lensdemoverify.gallery.utils.GalleryItem
import com.truepic.lensdemoverify.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    var itemsListener: ((items: ArrayList<GalleryItem>) -> Unit)? = null

    fun resume() {
        load()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = Util.getAllMediaFilesByFolder(true)

            withContext(Dispatchers.Main) {
                itemsListener?.invoke(list)
            }
        }
    }

}