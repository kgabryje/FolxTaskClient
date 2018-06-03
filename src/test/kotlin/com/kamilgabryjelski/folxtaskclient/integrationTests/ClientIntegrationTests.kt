package com.kamilgabryjelski.folxtaskclient.integrationTests

import com.kamilgabryjelski.folxtaskclient.client.ProductClient
import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
abstract class ClientIntegrationTests {

    @Autowired
    lateinit var client: ProductClient

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Before
    fun fillDatabase() {
        val products = listOf(
                Product(name = "prod1", price = 123F, status = ProductStatus.INSTOCK),
                Product(name = "prod2", price = 234F, status = ProductStatus.INSTOCK),
                Product(name = "prod3", price = 345F, status = ProductStatus.INSTOCK),
                Product(name = "prod4", price = 456F, status = ProductStatus.INSTOCK)
        )
        products.forEach {
            restTemplate.put(client.uriProvider.createUri(), it)
        }
    }
}
