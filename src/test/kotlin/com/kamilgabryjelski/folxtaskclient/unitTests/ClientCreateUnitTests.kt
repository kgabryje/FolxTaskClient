package com.kamilgabryjelski.folxtaskclient.unitTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
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
class ClientCreateUnitTests: ClientUnitTests() {
    @Test
    fun testCreateProduct() {
        server.expect(requestTo(client.uriProvider.createUri()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.CREATED))
        client.createProduct("prod", 123F, ProductStatus.INSTOCK)
        server.verify()
    }

    @Test
    fun testCreateProduct_ConflictNames_NoUpdate() {
        val name = "prod"

        val productOnServer = Product(1, name, 124F, ProductStatus.INSTOCK)
        val productOnServerJSON = objectMapper.writeValueAsString(productOnServer)
        server.expect(requestTo(client.uriProvider.createUri()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.CONFLICT))
        server.expect(requestTo(client.uriProvider.readByNameUri(name)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(productOnServerJSON, MediaType.APPLICATION_JSON))
        client.createProduct("prod", 123F, ProductStatus.INSTOCK)
        server.verify()
    }

    @Test
    fun testCreateProduct_ConflictNames_UpdateSuccess() {
        val name = "prod"

        val productOnServer = Product(1, name, 122F, ProductStatus.INSTOCK)
        val productOnServerJSON = objectMapper.writeValueAsString(productOnServer)
        server.expect(requestTo(client.uriProvider.createUri()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.CONFLICT))
        server.expect(requestTo(client.uriProvider.readByNameUri(name)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(productOnServerJSON, MediaType.APPLICATION_JSON))
        server.expect(requestTo(client.uriProvider.updateUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess())
        client.createProduct("prod", 123F, ProductStatus.INSTOCK)
        server.verify()
    }

    @Test
    fun testCreateProduct_Withdrawn() {
        server.expect(requestTo(client.uriProvider.createUri()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST))
        client.createProduct("prod", 123F, ProductStatus.WITHDRAWN)
        server.verify()
    }

    @Test
    fun testCreateProduct_IDExists() {
        server.expect(requestTo(client.uriProvider.createUri()))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST))
        client.createProduct("prod", 123F, ProductStatus.WITHDRAWN)
        server.verify()
    }
}
