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
class ClientUpdateIntegrationTests: ClientIntegrationTests() {
    @Test
    fun testUpdateProduct() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        val productToUpdate = productsOnServer!![0]
        val updatedProduct = Product(productToUpdate.id, productToUpdate.name, 789F, ProductStatus.WITHDRAWN)
        client.updateProduct(updatedProduct.id, updatedProduct.name, updatedProduct.price, updatedProduct.status)

        val actualProduct = restTemplate.getForObject(client.uriProvider.readByIDUri(updatedProduct.id), Product::class.java)

        assertEquals(updatedProduct, actualProduct)
    }

    @Test
    fun testUpdateProduct_IDNotFound() {
        val id = -1L
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        client.updateProduct(id)

        val productsAfterFailedUpdate: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsAfterFailedUpdate)
        assertEquals(productsOnServer, productsAfterFailedUpdate)
    }

    @Test
    fun testUpdateProduct_NameExists() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        val product1 = productsOnServer!![0]
        val product2 = productsOnServer[1]

        client.updateProduct(product1.id, product2.name)

        val productsAfterFailedUpdate: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsAfterFailedUpdate)
        assertEquals(productsOnServer, productsAfterFailedUpdate)
    }
}
