package com.allen.allenlib.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allen.allenlib.api.ErrorResponse
import com.allen.allenlib.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * @param isLoading liveData for handle progressbar
 * @param responseLiveData liveData for response Model
 * @param errorLiveData liveData for handle apiError String
 * @param apiCall call api suspend fun
 * @param T should be api return response Model
 */
fun <T> ViewModel.callApi(
    isLoading: MutableLiveData<EventWrapper<Boolean>>? = null,
    responseLiveData: MutableLiveData<EventWrapper<T>>,
    errorLiveData: MutableLiveData<EventWrapper<String>>,
    apiCall: suspend () -> T
) {
    viewModelScope.launch {
        val result = safeApiCall(Dispatchers.IO, isLoading = isLoading) {
            apiCall.invoke()
        }
        when (result) {
            is ResultWrapper.Success -> {
                responseLiveData.value = EventWrapper(result.value)
            }
            is ResultWrapper.GenericError -> {
                logd("errcode=${result.code},errMsg = ${result.error}")
                errorLiveData.postValue(EventWrapper(result.error.toString()))
            }
            is ResultWrapper.NetworkError -> {
                logd("apiFail")
                errorLiveData.postValue(EventWrapper(result.throwable))
            }
        }
    }
}

/**
 * @param isLoading default null: not show progressBar
 * @return Api ResultWrapper type
 * */
suspend fun <T> ViewModel.safeApiCall(
    dispatcher: CoroutineDispatcher,
    isLoading: MutableLiveData<EventWrapper<Boolean>>? = null,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            isLoading?.let {
                withContext(Dispatchers.Main) {
                    logd("open progressbar")
                    it.value = EventWrapper(true)
                }
            }

            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            loge("api throwable: ${throwable.message}")
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError(throwable.toString())
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, ErrorResponse("未知錯誤"))
                }
            }
        } finally {
            isLoading?.let {
                logd("close progressbar")
                withContext(Dispatchers.Main) {
                    isLoading.value = EventWrapper(false)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse {
    loge("convertErrorBody: ${throwable.message()}")
    return ErrorResponse("請確認網路狀態")
}