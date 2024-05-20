package com.example.travel.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.model.provinceDetail.ProvinceDetail
import com.example.travel.repository.ProvinceApi
import kotlinx.coroutines.launch

class SearchViewModel(query: String? = null): ViewModel() {
    var provinceResult: ProvinceDetail? by mutableStateOf(ProvinceDetail())

    init {
        getProvince(query)
    }

    private fun getProvince(query: String? = null){
        viewModelScope.launch {
            provinceResult = ProvinceApi.retrofitService.getDetail(query)
        }
    }
}