package com.cniekirk.kreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.domain.GetImageInformationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ViewModel to provide [ImageInformation] to the Submission View
 */
@ExperimentalCoroutinesApi
@Singleton
class SubmissionViewModel @Inject constructor(
    val getImageInformationUseCase: GetImageInformationUseCase
): BaseViewModel() {

    // Emits change to the fragment
    val imageInformation: MutableLiveData<ImageInformation> = MutableLiveData()

    /**
     * Invoke [getImageInformationUseCase] and call [handleImageInformation] once operation is successfully completed
     */
    fun getImageInformation(imageHash: String) {

        getImageInformationUseCase(imageHash) { it.either(::handleFailure, ::handleImageInformation) }

    }

    /**
     * Update the [imageInformation] LiveData object
     */
    private fun handleImageInformation(imageInformation: ImageInformation) {
        this.imageInformation.value = imageInformation
    }

}