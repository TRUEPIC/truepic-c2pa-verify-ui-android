package com.truepic.lensdemoverify.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.truepic.lensdemoverify.R
import com.truepic.lensdemoverify.gallery.utils.C2PAStatus
import com.truepic.lensdemoverify.utils.Util
import com.truepic.lensverify.utils.C2PAPresenter
import com.truepic.lenssdkverify.LensSecurityUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class C2PAViewModel(application: Application) : AndroidViewModel(application) {

    private val res = application.resources

    fun load(
        path: String?,
        onLoadComplete: (C2PAStatus, List<Item>) -> Unit,
        onFail: (error: String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (path.isNullOrEmpty()) onFail.invoke("No path provided")

                // TODO: accessing libc2pa
                val libc2pa = LensSecurityUtil.verifyJsonWithThumbnails(path)

                val data = Util.jsonToC2PADataWithThumbnails(libc2pa.first, libc2pa.second)
                val status = Util.getC2PAStatus(data)

                val presenter = C2PAPresenter(
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
                        signedWithText = presenter.getSignedWith(manifestStore)
                    )
                }

                withContext(Dispatchers.Main) {
                    onLoadComplete.invoke(status, list)
                }
            } catch (e: RuntimeException) {
                withContext(Dispatchers.Main) {
                    onFail.invoke(e.message)
                }
            }
        }
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
        val signedWithText: String?
    )
}