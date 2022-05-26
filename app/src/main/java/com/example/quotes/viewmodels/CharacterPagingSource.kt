package com.example.quotes.viewmodels

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.quotes.apimodels.DataItem
import com.example.quotes.apimodels.Quotes
import com.example.quotes.retrofit.RetrofitServices

class CharacterPagingSource(val apiService: RetrofitServices, val id: Int) : PagingSource<Int, DataItem>()  {

    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {

        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {

        try {
            val currentLoadingPageKey = params.key ?: 1

            val response = apiService.pagerItems("list/5/$id",currentLoadingPageKey)

            Log.e("TAG", "load: ${response.isSuccessful}")

            val list: List<DataItem> = response.body()?.quotes?.data as List<DataItem>

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = list,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1))

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

}