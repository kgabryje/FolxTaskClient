package com.kamilgabryjelski.folxtaskclient.integrationTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ClientReadIntegrationTests: ClientIntegrationTests() {
    @Test
    fun testReadAllProducts() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        val expectedProducts = client.readAllProducts()
        assertNotNull(expectedProducts)
        assertEquals(productsOnServer, expectedProducts)
    }

    @Test
    fun testReadByID() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        val id = productsOnServer!![0].id
        val returnedProduct = client.readProductByID(id)
        assertNotNull(returnedProduct)
        assertEquals(productsOnServer[0], returnedProduct)
    }

    @Test
    fun testReadByID_IDNotFound() {
        val id = -1L
        assertNull(client.readProductByID(id))
    }

    @Test
    fun testReadByName() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        val name = productsOnServer!![0].name
        val returnedProduct = client.readProductByName(name)
        assertNotNull(returnedProduct)
        assertEquals(productsOnServer[0], returnedProduct)
    }

    @Test
    fun testReadByName_NoSuchProduct() {
        val name = "no_such_product"
        assertNull(client.readProductByName(name))
    }
}
