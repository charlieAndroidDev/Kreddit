package com.cniekirk.kreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.domain.GetImageInformationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class SubmissionViewModel @Inject constructor(
    val getImageInformationUseCase: GetImageInformationUseCase
): BaseViewModel() {

    val imageInformation: MutableLiveData<ImageInformation> = MutableLiveData()

    fun getImageInformation(imageHash: String) {

        getImageInformationUseCase(imageHash) { it.either(::handleFailure, ::handleImageInformation) }

    }

    private fun handleImageInformation(imageInformation: ImageInformation) {
        this.imageInformation.value = imageInformation
    }

}