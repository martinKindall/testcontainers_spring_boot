package com.codigomorsa.integracion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IntegracionApplication

fun main(args: Array<String>) {
	runApplication<IntegracionApplication>(*args)
}
