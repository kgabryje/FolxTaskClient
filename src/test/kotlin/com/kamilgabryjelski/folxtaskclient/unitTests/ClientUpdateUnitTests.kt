package com.kamilgabryjelski.folxtaskclient.unitTests

import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@RunWith(SpringRunner::class)
class ClientUpdateUnitTests: ClientUnitTests() {
    val product1 = Product(1, "prod1", 123F, ProductStatus.WITHDRAWN)
    val product2 = Product(2, "prod2", 234F, ProductStatus.INSTOCK)

    @Test
    fun testUpdateProduct() {
        server.expect(requestTo(client.uriProvider.updateUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess())
        client.updateProduct(product1.id, product1.name)
        server.verify()
    }

    @Test
    fun testUpdateProduct_IDNotFound() {
        server.expect(requestTo(client.uriProvider.updateUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))
        client.updateProduct(3L)
        server.verify()
    }

    @Test
    fun testUpdateProduct_NameExists() {
        server.expect(requestTo(client.uriProvider.updateUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CONFLICT))
        client.updateProduct(product1.id, product2.name)
        server.verify()
    }
}
