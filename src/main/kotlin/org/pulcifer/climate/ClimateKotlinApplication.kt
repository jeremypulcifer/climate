package org.pulcifer.climate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClimateKotlinApplication

fun main(args: Array<String>) {
	runApplication<ClimateKotlinApplication>(*args)
}
