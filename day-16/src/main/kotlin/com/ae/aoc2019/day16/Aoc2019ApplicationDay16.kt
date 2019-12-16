package com.ae.aoc2019.day16

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.collections.HashMap

@SpringBootApplication
class Aoc2019ApplicationDay16 : CommandLineRunner {

	fun calculatedatter(pattern: List<Int>, position: Int) : List<Int> {
		return pattern.flatMap { element ->
			                 (0 until position)
				          .map { element } }
				      .drop(1)
	}

	fun applyPatternToDigit(subSignal : List<Int>, pattern: List<Int>) : Int {
		var rvalue = 0
		for (i in 0 until subSignal.size) {
			rvalue +=  subSignal[i] * pattern[i % pattern.size]
		}
		return rvalue.toString().toCharArray().last().toString().toInt()
	}

	fun applyPattern(signal: List<Int>, pattern: List<Int>) : List<Int> {
		var rvalue = ArrayList<Int>()
		for (i in 0 until signal.size) {
			val calculatedPatter = calculatedatter(pattern, i)
			rvalue.add( applyPatternToDigit(signal.drop(i+1), calculatedPatter) )
		}
		return rvalue
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay16::class.java.getResourceAsStream(args[0]!!)))


		bufferedReader.useLines {
			it.forEach { line ->
				val basePattern = listOf(0, 1, 0, -1)
				var numbers = line.toCharArray().map { it.toString().toInt() }
				(0 until 100).forEach {
					val pattern = ArrayList<Int>()
					numbers = applyPattern(numbers, pattern)
					println("Result: $numbers")
				}

			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay16>(*args)
}
