package com.kamilgabryjelski.folxtaskclient.integrationTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ClientCreateIntegrationTests: ClientIntegrationTests() {
    @Test
    fun testCreateProduct() {
        val productsBefore: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsBefore)

        val newProductName = "abcdefghijklmnopqrstuvwxyz".split("").shuffled().take(15).joinToString("")

        client.createProduct(newProductName, 123F, ProductStatus.INSTOCK)
        val productsAfter: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsAfter)
        
        assertEquals(productsBefore!!.size + 1, productsAfter!!.size)
    }

    @Test
    fun testCreateProduct_NameExists() {
        val productsBefore: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsBefore)

        client.createProduct(productsBefore!![0].name, productsBefore[0].price - 1, ProductStatus.INSTOCK)
        val productsAfter: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsAfter)
        assertEquals(productsBefore, productsAfter)
    }

    @Test
    fun testCreateProduct_NameExists_Update() {
        val productsBefore: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsBefore)

        val productToUpdate = productsBefore!![0]
        val updatedProduct = Product(
                productToUpdate.id, productToUpdate.name, productToUpdate.price + 1, ProductStatus.INSTOCK
        )
        client.createProduct(updatedProduct.name, updatedProduct.price, updatedProduct.status)

        val expectedProduct: Product = restTemplate.getForObject(
                client.uriProvider.readByNameUri(updatedProduct.name), Product::class.java
        )
        assertEquals(updatedProduct, expectedProduct)
    }
}
