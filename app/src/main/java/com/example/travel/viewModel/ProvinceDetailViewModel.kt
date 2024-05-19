package com.example.travel.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.model.provinceDetail.ProvinceDetail
import com.example.travel.model.provinceResult.provinceDetail
import com.example.travel.repository.ProvinceDetailApi
import kotlinx.coroutines.launch

class ProvinceDetailViewModel(id: String?=null):ViewModel() {
    var provinceDetailResult:provinceDetail? by mutableStateOf(provinceDetail())

    init {
        getProvince(id)
    }

    fun getProvince(id:String?=null){
       viewModelScope.launch {
           provinceDetailResult = ProvinceDetailApi.retrofitService.getDetail(id)
       }
    }
}