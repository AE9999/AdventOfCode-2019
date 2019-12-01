package com.ae.aoc2019.day01

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader


@SpringBootApplication
class Aoc2019ApplicationDay01 : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)

	private fun calculateTotal(totalFile: String) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay01::class.java.getResourceAsStream(totalFile)))
		bufferedReader.useLines {
			val total = it.map {mass ->
				val fuel = ((mass.toLong()) / 3) - 2
				logger.info("$mass -> $fuel")
				fuel
			}.sum()
			logger.info("Calculated total: $total ...")
		}
	}

	fun weightedRecursive(mass: Long) : Long {
		val fuel = ((mass) / 3) - 2
		return if (fuel <= 0L) 0 else return fuel + weightedRecursive(fuel)
	}

	private fun calculateTotalWeighted(totalFile: String) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay01::class.java.getResourceAsStream(totalFile)))
		bufferedReader.useLines {
			val total = it.map {mass ->
				val fuel = weightedRecursive(mass.toLong())
				logger.info("$mass -> $fuel")
				fuel
			}.sum()
			logger.info("Calculated total: $total ...")
		}
	}

	override fun run(vararg args: String?) {
		calculateTotalWeighted(args[0]!!)
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay01>(*args)
}
