package com.cniekirk.kreddit.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniekirk.kreddit.utils.Failure

abstract class BaseViewModel: ViewModel() {

    val failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

}