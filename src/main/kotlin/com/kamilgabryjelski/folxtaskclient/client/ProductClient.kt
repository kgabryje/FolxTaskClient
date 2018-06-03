package com.kamilgabryjelski.folxtaskclient.client

import com.kamilgabryjelski.folxtaskclient.uri.UriConstants
import com.kamilgabryjelski.folxtaskclient.model.Product
import com.kamilgabryjelski.folxtaskclient.model.ProductStatus
import com.kamilgabryjelski.folxtaskclient.uri.UriProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Service
class ProductClient {
    @Autowired
    lateinit var restTemplate: RestTemplate
    val logger: Logger = LoggerFactory.getLogger(ProductClient::class.java)
    val uriProvider = UriProvider()

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    fun createProduct(name: String, price: Float, status: ProductStatus) {
        val product = Product(name = name, price = price, status = status)
        logger.info("Creating product: $product")

        try {
            val response = restTemplate.exchange(
                    uriProvider.createUri(),
                    HttpMethod.PUT,
                    HttpEntity(product),
                    Product::class.java
            )
            logger.info(response.toString())
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
            if (exception.statusCode == HttpStatus.CONFLICT) {
                logger.info("Updating product")
                readProductByName(name)?.takeIf { it.price < price }?.let { updateProduct(it.id, name, price, status) }
            }
        }
    }

    fun readAllProducts(): List<Product>? {
        logger.info("Reading all products")

        val response = restTemplate.exchange(
                uriProvider.readAllUri(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                object: ParameterizedTypeReference<List<Product>>(){}
        )
        logger.info(response.toString())

        return response.body
    }

    fun readProductByID(id: Long): Product? {
        logger.info("Reading product with ID: $id")

        return try {
            restTemplate.getForEntity(uriProvider.readByIDUri(id), Product::class.java).run {
                logger.info(toString())
                body
            }
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
            null
        }
    }

    fun readProductByName(name: String): Product? {
        logger.info("Reading product with name: $name")

        return try {
            restTemplate.getForEntity(uriProvider.readByNameUri(name), Product::class.java).run {
                logger.info(toString())
                body
            }
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
            null
        }
    }

    fun updateProduct(id: Long, name: String = "", price: Float = 0F, status: ProductStatus = ProductStatus.WITHDRAWN) {
        val product = Product(id, name, price, status)
        logger.info("Updating product with data: $product")

        try {
            val response = restTemplate.exchange(
                    uriProvider.updateUri(),
                    HttpMethod.POST,
                    HttpEntity(product),
                    Product::class.java
            )
            logger.info(response.toString())
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
        }
    }

    fun deleteProductByID(id: Long) {
        logger.info("Deleting product with ID: $id")

        try {
            val response = restTemplate.exchange(
                    uriProvider.deleteByIDUri(id),
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Unit::class.java
            )
            logger.info(response.toString())
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
        }
    }

    fun deleteProductByName(name: String) {
        logger.info("Deleting product with name: $name")

        try {
            val response = restTemplate.exchange(
                    uriProvider.deleteByNameUri(name),
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Unit::class.java)
            logger.info(response.toString())
        }
        catch (exception: HttpStatusCodeException) {
            logger.info(exception.responseBodyAsString)
        }
    }
}
