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


	fun getIterator(position : Int, pattern: List<Int>) : Iterator<Int> {

		return object : Iterator<Int> {

			var currentIterator = pattern.map { element ->
												(0..position)
														.map { element } }
													.flatten()
													.drop(1)
													.iterator()

			override fun hasNext(): Boolean {
				return true
			}

			override fun next(): Int {
				if (!currentIterator.hasNext()) {
					currentIterator = pattern.map { element ->
													(0..position).map { element } }
											 .flatten()
											 .iterator()
				}
				return currentIterator.next()
			}
		}
	}

	fun applyPattern(signal: List<Int>, pattern: List<Int>) : List<Int> {
		var rvalue = ArrayList<Int>()
		for (i in 0 until signal.size) {
			var entry = 0
			val patternIterator = getIterator(i, pattern)
			for (j in 0 until signal.size) {
				val paternEntry = patternIterator.next()
				entry +=  signal[j] * paternEntry // Check this ..
			}
			val digit = entry.toString().last().toString().toInt()
			rvalue.add(digit)
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
					numbers = applyPattern(numbers, basePattern)
				}
				println("Result: ${numbers.take(8).joinToString(prefix = "", postfix = "", separator = "") }")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay16>(*args)
}
