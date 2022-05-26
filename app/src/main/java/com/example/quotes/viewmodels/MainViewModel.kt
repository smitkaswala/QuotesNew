package com.example.quotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.quotes.apimodels.DataItem
import com.example.quotes.apimodels.Quotes
import com.example.quotes.retrofit.RetrofitInstance
import com.example.quotes.retrofit.RetrofitServices
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {

    var retroService: RetrofitServices = RetrofitInstance.getRetrofitInstance().create(RetrofitServices::class.java)

    fun getListData(id: Int): Flow<PagingData<DataItem>> {
        return Pager (config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = {CharacterPagingSource(retroService,id)}).flow.cachedIn(viewModelScope)

    }

}