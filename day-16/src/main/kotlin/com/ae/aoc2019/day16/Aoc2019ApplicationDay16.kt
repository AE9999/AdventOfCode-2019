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

	fun getIterator(position : Long, pattern: List<Long>) : Iterator<Long> {

		return object : Iterator<Long> {

			var currentIterator = pattern.map { element ->
												(0..position)
														.map { element } }
													.flatten()
													.drop(1)
													.iterator()

			override fun hasNext(): Boolean {
				return true
			}

			override fun next(): Long {
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

	fun applyPattern(signal: List<Long>, pattern: List<Long>, it : Int) : List<Long> {
		var rvalue = ArrayList<Long>()
		for (i in 0 until signal.size) {
			println("Running $i / ${signal.size}  => $it / 100..")
			var entry = 0L
			val patternIterator = getIterator(i.toLong(), pattern)
			for (j in 0 until signal.size) {
				val paternEntry = patternIterator.next()
				entry +=  signal[j] * paternEntry // Check this ..
			}
			val digit = entry.toString().last().toString().toLong()
			rvalue.add(digit)
		}
		return rvalue
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay16::class.java.getResourceAsStream(args[0]!!)))


		bufferedReader.useLines {
			it.forEach { line ->
				val basePattern = listOf(0L, 1L, 0L, -1L)
				var numbers : List<Long>
				numbers = line.toCharArray().map { it.toString().toLong() }
				(0 until 100).forEach {
					numbers = applyPattern(numbers, basePattern, it)
				}
				println("Result: ${numbers.take(8).joinToString(prefix = "", postfix = "", separator = "") }")

				val first7numbers = line.toCharArray()
						                       .take(7)
						                       .map { it.toString() }
						                       .joinToString(prefix = "", postfix = "", separator = "")
						                       .toInt()
				numbers = line.repeat(1000).toCharArray().map { it.toString().toLong() }
				(0 until 100).forEach {
					numbers = applyPattern(numbers, basePattern, it)
				}
				val finalOutput = numbers.drop(first7numbers)
						                        .take(8)
						                        .joinToString(prefix = "", postfix = "", separator = "")
				println("Final output ${first7numbers} => ${finalOutput} ..")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay16>(*args)
}
