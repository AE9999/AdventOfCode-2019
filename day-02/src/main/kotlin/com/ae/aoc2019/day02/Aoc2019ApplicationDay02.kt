package com.ae.aoc2019.day02

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader


@SpringBootApplication
class Aoc2019ApplicationDay02 : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)


	private fun calculateTotalWeighted(totalFile: String) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay02::class.java.getResourceAsStream(totalFile)))
		bufferedReader.useLines {}
	}

	override fun run(vararg args: String?) {

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay02>(*args)
}
