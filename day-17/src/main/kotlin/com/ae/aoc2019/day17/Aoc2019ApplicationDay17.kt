package com.ae.aoc2019.day17

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import kotlin.math.absoluteValue
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay17 : CommandLineRunner {

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay17::class.java.getResourceAsStream(args[0]!!)))

		bufferedReader.useLines {
			it.forEach { line ->

		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay17>(*args)
}
