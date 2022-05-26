package com.example.quotes.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quotes.apimodels.ItemsResponse
import com.example.quotes.retrofit.RetrofitInstance
import com.example.quotes.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel : ViewModel() {

    private val createNewQuotesLiveData : MutableLiveData<ItemsResponse?> = MutableLiveData()

    @JvmName("getCreateNewQuotesLiveData1")
    fun getCreateNewQuotesLiveData(): MutableLiveData<ItemsResponse?>{
        return createNewQuotesLiveData
    }
    val error:MutableLiveData<String?> = MutableLiveData()

    fun quotesView(id : Int){

        val retrofitService = RetrofitInstance.getRetrofitInstance().create(RetrofitServices::class.java)

        val call = retrofitService.quotesItems("list/5/$id",1)
        call.enqueue(object : Callback<ItemsResponse> {
            override fun onResponse(call: Call<ItemsResponse>, response: Response<ItemsResponse>) {

                Log.e("TAG", "onResponse: " + response.body())
                if (response.isSuccessful){
                    getCreateNewQuotesLiveData().postValue(response.body())

                }else{
                    error.value = "something wrong"
                    getCreateNewQuotesLiveData().postValue(null)
                }
            }

            override fun onFailure(call: Call<ItemsResponse>, t: Throwable) {
                getCreateNewQuotesLiveData().postValue(null)
            }
        })
    }


}