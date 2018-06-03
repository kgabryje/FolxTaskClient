package com.kamilgabryjelski.folxtaskclient.unitTests

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
class ClientDeleteUnitTests: ClientUnitTests() {
    @Test
    fun testDeleteByID() {
        val id = 1L
        server.expect(requestTo(client.uriProvider.deleteByIDUri(id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess())
        client.deleteProductByID(id)
        server.verify()
    }

    @Test
    fun testDeleteByID_IDNotFound() {
        val id = 1L
        server.expect(requestTo(client.uriProvider.deleteByIDUri(id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))
        client.deleteProductByID(id)
        server.verify()
    }

    @Test
    fun testDeleteByName() {
        val name = "prod"
        server.expect(requestTo(client.uriProvider.deleteByNameUri(name)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess())
        client.deleteProductByName(name)
        server.verify()
    }

    @Test
    fun testDeleteByName_NoSuchProduct() {
        val name = "prod"
        server.expect(requestTo(client.uriProvider.deleteByNameUri(name)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))
        client.deleteProductByName(name)
        server.verify()
    }
}
