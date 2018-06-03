package com.kamilgabryjelski.folxtaskclient.model

data class Product(
        val id: Long = -1,
        val name: String = "",
        var price: Float = 0F,
        var status: ProductStatus = ProductStatus.WITHDRAWN
)
