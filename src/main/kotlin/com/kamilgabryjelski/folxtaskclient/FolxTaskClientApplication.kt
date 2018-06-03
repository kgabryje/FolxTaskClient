package com.kamilgabryjelski.folxtaskclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FolxTaskClientApplication

fun main(args: Array<String>) {
    runApplication<FolxTaskClientApplication>(*args)
}
