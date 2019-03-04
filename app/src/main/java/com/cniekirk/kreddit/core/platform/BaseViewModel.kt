package com.cniekirk.kreddit.core.platform

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniekirk.kreddit.utils.Failure

abstract class BaseViewModel: ViewModel() {

    val failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        Log.d("FAILURE", "Failure: $failure")
        this.failure.value = failure
    }

}