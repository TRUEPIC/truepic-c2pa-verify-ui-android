package com.truepic.lensdemoverify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.truepic.lensdemoverify.databinding.DialogC2paBinding
import com.truepic.lenssdkverify.LensSecurityUtil
import org.json.JSONObject

class C2PARawDialogFragment(private val path: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogC2paBinding.inflate(inflater, container, false)

        val formattedJson: String = try {
            // TODO: accessing libc2pa
            val json = JSONObject(LensSecurityUtil.verifyJson(path))
            json.toString(2)
        } catch (e: Exception) {
            e.message.orEmpty()
        }

        binding.codeTextView.text = formattedJson
        binding.codeTextView.setHorizontallyScrolling(true)
        binding.closeButton.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // full screen dialog
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

    }

}