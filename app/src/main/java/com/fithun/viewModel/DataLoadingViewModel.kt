package com.fithun.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithun.api.responses.ViewCurrentPriceAndExpectedPool
import com.fithun.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

class DataLoadingViewModel : ViewModel() {

    private val _dataLoadingState = MutableStateFlow<Resource<List<ViewCurrentPriceAndExpectedPool>>>(Resource.Empty())
    val dataLoadingState: StateFlow<Resource<List<ViewCurrentPriceAndExpectedPool>>> = _dataLoadingState

    private var totalItemsLoaded = 0
    private val pageSize = 100

    fun loadArrayData(array: List<ViewCurrentPriceAndExpectedPool>, page: Int) {
        viewModelScope.launch {
            _dataLoadingState.value = Resource.Loading()
            try {
                val startIndex = (page - 1) * pageSize
                val endIndex = min(startIndex + pageSize, array.size)
                val result = array.subList(startIndex, endIndex)
                _dataLoadingState.value = Resource.Success(result)
                totalItemsLoaded += result.size
            } catch (e: Exception) {
                _dataLoadingState.value = Resource.Error(e.message)
            }
        }
    }

    fun getTotalItemsLoaded(): Int {
        return totalItemsLoaded
    }
}
