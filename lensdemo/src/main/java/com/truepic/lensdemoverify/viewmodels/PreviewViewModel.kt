package com.truepic.lensdemoverify.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus
import com.truepic.lensdemoverify.gallery.utils.GalleryItem
import com.truepic.lensdemoverify.gallery.utils.GalleryItemType
import com.truepic.lensdemoverify.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewViewModel : ViewModel() {

    private val c2paStatusMetadata: Map<String, C2PAStatus> = HashMap()
    private var items: List<GalleryItem> = listOf()
    private var currentIndex = 0
    var c2paCallback: ((c2paStatus: C2PAStatus, path: String) -> Unit)? = null

    fun load(
        currentPath: String,
        onLoadComplete: (list: List<GalleryItem>, currentIndex: Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            items = Util.getAllMediaFilesByFolder(false, c2paStatusMetadata)

            items.forEachIndexed { index, item ->
                if (item.path == currentPath) {
                    currentIndex = index
                    return@forEachIndexed
                }
            }

            withContext(Dispatchers.Main) {
                onLoadComplete.invoke(items, currentIndex)
            }

            items = Util.updateMediaC2PAFlags(items, c2paStatusMetadata)
            withContext(Dispatchers.Main) {
                onLoadComplete.invoke(items, currentIndex)
                onPageSelected(currentIndex)
            }
        }
    }

    fun onPageSelected(position: Int) {
        currentIndex = position
        c2paCallback?.invoke(
            items[currentIndex].c2PA, if (items[currentIndex].type == GalleryItemType.VIDEO) {
                items[currentIndex].videoPath
            } else {
                items[currentIndex].path
            }
        )
    }

}