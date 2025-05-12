package com.truepic.lensdemoverify.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.truepic.lensdemoverify.R
import com.truepic.lensverify.data.c2padata.C2PAData
import com.truepic.lensverify.utils.C2PAPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class C2PAViewModel(application: Application) : AndroidViewModel(application) {

    private val res = application.resources

    fun load(
        path: String?,
        onLoadComplete: (List<Item>) -> Unit,
        onFail: (error: String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (path.isNullOrEmpty()) onFail.invoke("No path provided")

                // TODO: Access libc2pa - Load the C2PA data
                val data = C2PAData()

                val mimeType = getMimeType(File(path!!).path)

                val presenter = C2PAPresenter(
                    mimeType,
                    data, C2PAPresenter.Labels(
                        "",
                        "",
                        "",
                        res.getString(R.string.c2pa_info_thumbnail_type_photo),
                        res.getString(R.string.c2pa_info_thumbnail_type_image),
                        res.getString(R.string.c2pa_info_thumbnail_type_video),
                        res.getString(R.string.c2pa_info_thumbnail_type_audio),
                        res.getString(R.string.c2pa_info_captured),
                        res.getString(R.string.c2pa_info_created),
                        res.getString(R.string.c2pa_info_captured_with),
                        res.getString(R.string.c2pa_info_created_with)
                    )
                )

                val list = presenter.getManifests().reversed().map { manifestStore ->
                    Item(
                        address = presenter.getAddress(getApplication(), manifestStore),
                        thumbnail = presenter.getThumbnail(manifestStore, 200),
                        type = presenter.getType(),
                        typeLabel = presenter.getTypeLabel(),
                        capturedWith = presenter.getCapturedWith(manifestStore),
                        capturedWithLabel = presenter.getCapturedWithLabel(manifestStore),
                        capturedLabel = presenter.getCapturedLabel(manifestStore),
                        isAiGenerated = presenter.isAiGenerated(manifestStore),
                        modifications = presenter.getModifications(manifestStore),
                        capturedDateText = presenter.getCapturedDate(manifestStore),
                        signedByText = presenter.getSignedBy(manifestStore),
                    )
                }

                withContext(Dispatchers.Main) {
                    onLoadComplete.invoke(list)
                }
            } catch (e: RuntimeException) {
                withContext(Dispatchers.Main) {
                    onFail.invoke(e.message)
                }
            }
        }
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    data class Item(
        val address: String?,
        val thumbnail: Bitmap?,
        val type: C2PAPresenter.Type,
        val typeLabel: String,
        val capturedWith: String,
        val capturedWithLabel: String,
        val capturedLabel: String,
        val isAiGenerated: Boolean,
        val modifications: Int,
        val capturedDateText: String,
        val signedByText: String,
    )
}