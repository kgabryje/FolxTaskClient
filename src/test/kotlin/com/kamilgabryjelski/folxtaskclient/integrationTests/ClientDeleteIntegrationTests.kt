package com.kamilgabryjelski.folxtaskclient.integrationTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ClientDeleteIntegrationTests: ClientIntegrationTests() {
    @Test
    fun testDeleteByID() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        client.deleteProductByID(productsOnServer!![0].id)

        val afterDelete: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(afterDelete)

        assertEquals(productsOnServer.drop(1), afterDelete)
    }

    @Test
    fun testDeleteByID_IDNotFound() {
        val id = -1L
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        client.deleteProductByID(id)

        val afterFailedDelete: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(afterFailedDelete)

        assertEquals(productsOnServer, afterFailedDelete)
    }

    @Test
    fun testDeleteByName() {
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        client.deleteProductByName(productsOnServer!![0].name)

        val afterDelete: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(afterDelete)

        assertEquals(productsOnServer.drop(1), afterDelete)
    }

    @Test
    fun testDeleteByName_NoSuchProduct() {
        val name = "no_such_product"
        val productsOnServer: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(productsOnServer)

        client.deleteProductByName(name)

        val afterDelete: List<Product>? = restTemplate.exchange(
                client.uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        ).body
        assertNotNull(afterDelete)

        assertEquals(productsOnServer, afterDelete)
    }
}
