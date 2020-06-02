package com.allen.allenlib.view.dialog_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.allen.allenlib.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_photo_bottom_sheet.*

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected val viewModel: DialogDataViewModel by activityViewModels()

    open fun getLayoutId(): Int = R.layout.dialog_photo_bottom_sheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvCamera.setOnClickListener {
            viewModel.setAddPhotoItemSelected(ItemClickState.OpenCamera)
        }
        tvGallery.setOnClickListener {
            viewModel.setAddPhotoItemSelected(ItemClickState.OpenGallery)
        }
        tvDeletePhoto.setOnClickListener {
            viewModel.setAddPhotoItemSelected(ItemClickState.DeletePhoto)
        }

    }

    protected fun setDeleteItemVisible(visible: Int) {
        deleteGroup.visibility = visible
    }

    sealed class ItemClickState {
        object OpenCamera : ItemClickState()
        object OpenGallery : ItemClickState()
        object DeletePhoto : ItemClickState()
    }
}