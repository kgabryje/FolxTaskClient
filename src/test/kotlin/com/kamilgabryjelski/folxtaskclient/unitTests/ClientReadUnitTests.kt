package com.kamilgabryjelski.folxtaskclient.unitTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@RunWith(SpringRunner::class)
class ClientReadUnitTests: ClientUnitTests() {
    val product1 = Product(1, "prod1", 123F, ProductStatus.WITHDRAWN)
    val product2 = Product(2, "prod2", 234F, ProductStatus.INSTOCK)
    val products = listOf(product1, product2)

    @Test
    fun testReadAllProducts() {
        val productsJSON = objectMapper.writeValueAsString(products)
        server.expect(requestTo(client.uriProvider.readAllUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(productsJSON, MediaType.APPLICATION_JSON))
        val returnedProducts = client.readAllProducts()
        assertEquals(products, returnedProducts)
        server.verify()
    }

    @Test
    fun testReadByID() {
        val id = product1.id
        val product1JSON = objectMapper.writeValueAsString(product1)
        server.expect(requestTo(client.uriProvider.readByIDUri(id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(product1JSON, MediaType.APPLICATION_JSON))
        val returnedProduct = client.readProductByID(id)
        assertEquals(product1, returnedProduct)
        server.verify()
    }

    @Test
    fun testReadByID_IDNotFound() {
        val id = 4L
        server.expect(requestTo(client.uriProvider.readByIDUri(id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))
        client.readProductByID(id)
        server.verify()
    }

    @Test
    fun testReadByName() {
        val name = product1.name
        val product1JSON = objectMapper.writeValueAsString(product1)
        server.expect(requestTo(client.uriProvider.readByNameUri(name)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(product1JSON, MediaType.APPLICATION_JSON))
        val returnedProduct = client.readProductByName(name)
        assertEquals(product1, returnedProduct)
        server.verify()
    }

    @Test
    fun testReadByID_NoSuchProduct() {
        val name = "no_such_product"
        server.expect(requestTo(client.uriProvider.readByNameUri(name)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))
        client.readProductByName(name)
        server.verify()
    }
}
