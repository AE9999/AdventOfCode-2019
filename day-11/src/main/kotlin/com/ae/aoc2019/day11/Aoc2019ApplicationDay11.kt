package com.ae.aoc2019.day11

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay11 : CommandLineRunner {


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay11::class.java.getResourceAsStream(args[0]!!)))

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay11>(*args)
}
