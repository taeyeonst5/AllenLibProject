package com.allen.allenlib.view.dialog_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allen.allenlib.util.EventWrapper

class DialogDataViewModel : ViewModel() {
    private var _addPhotoItemSelected: MutableLiveData<EventWrapper<BaseBottomSheetDialogFragment.ItemClickState>> =
        MutableLiveData()
    val addPhotoItemSelected: LiveData<EventWrapper<BaseBottomSheetDialogFragment.ItemClickState>> =
        _addPhotoItemSelected

    fun setAddPhotoItemSelected(item: BaseBottomSheetDialogFragment.ItemClickState) {
        val eventWrapper = when (item) {
            is BaseBottomSheetDialogFragment.ItemClickState.OpenCamera -> EventWrapper(
                BaseBottomSheetDialogFragment.ItemClickState.OpenCamera
            )
            is BaseBottomSheetDialogFragment.ItemClickState.OpenGallery -> EventWrapper(
                BaseBottomSheetDialogFragment.ItemClickState.OpenGallery
            )
            is BaseBottomSheetDialogFragment.ItemClickState.DeletePhoto -> EventWrapper(
                BaseBottomSheetDialogFragment.ItemClickState.DeletePhoto
            )
        }
        _addPhotoItemSelected.value = eventWrapper
    }
}