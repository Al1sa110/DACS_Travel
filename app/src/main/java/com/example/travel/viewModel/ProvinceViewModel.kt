package com.example.travel.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.model.provinceDetail.ProvinceDetail
import com.example.travel.repository.ProvinceApi
import com.example.travel.repository.ProvinceDetailApi
import kotlinx.coroutines.launch

class ProvinceViewModel:ViewModel() {
    var provinceResult:ProvinceDetail? by mutableStateOf(ProvinceDetail())

    init {
        getProvince()
    }

    private fun getProvince(){
        viewModelScope.launch {
            provinceResult = ProvinceApi.retrofitService.getDetail()
        }
    }
}