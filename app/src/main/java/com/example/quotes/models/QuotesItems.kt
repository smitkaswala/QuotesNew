package com.example.quotes.models

import java.io.Serializable

data class QuotesItems(var imageId : Int? = null, var imageURL : String? = null, var categoryId : Int? = null) : Serializable