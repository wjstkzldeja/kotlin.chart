package com.osl.base.project.main.views.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import timber.log.Timber

class CameraViewModel(
) : OslViewModel() {
    private val _onCamera = MutableLiveData<Event<Unit>>()
    val onCamera: LiveData<Event<Unit>> get() = _onCamera

    private val _onPickAlbum = MutableLiveData<Event<Unit>>()
    val onPickAlbum: LiveData<Event<Unit>> get() = _onPickAlbum

    private val _imageTest = MutableLiveData<Uri>()
    val imageTest: LiveData<Uri> get() = _imageTest


    fun onCameraClick(){
        _onCamera.value = Event(Unit)
    }
    fun onPickAlbumClick(){
        _onPickAlbum.value = Event(Unit)
    }

    fun setImage(uri: Uri?) {
        uri?.let {
            Timber.d("foodLens setImage: ${uri}")
            _imageTest.value = it
            Timber.d("foodLens _imageTest: ${_imageTest.value}")
        }

    }
}