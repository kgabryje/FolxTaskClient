package com.kamilgabryjelski.folxtaskclient.unitTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamilgabryjelski.folxtaskclient.client.ProductClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.test.web.client.MockRestServiceServer

@RestClientTest(ProductClient::class)
abstract class ClientUnitTests {

    @Autowired
    lateinit var client: ProductClient

    @Autowired
    lateinit var server: MockRestServiceServer

    @Autowired
    lateinit var objectMapper: ObjectMapper
}
