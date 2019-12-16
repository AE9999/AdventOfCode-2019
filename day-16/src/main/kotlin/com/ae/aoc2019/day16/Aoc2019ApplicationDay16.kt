package com.ae.aoc2019.day16

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.collections.HashMap
import kotlin.math.absoluteValue

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

	fun applyPattern(signal: List<Int>, pattern: List<Int>, it : Int) : List<Int> {
		var rvalue = ArrayList<Int>()
		for (i in 0 until signal.size) {
//			println("Running $i out of ${signal.size} ..")
			var entry = 0L
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


	fun getMasks(index: Int,
				 stringLenght : Int,
				 pattern: List<Int>): Pair<Sequence<Int>, Sequence<Int>> {
		if (index == 0) {
			val ones = sequence {
				var i = 0
				while (i <= stringLenght) {
					yield(i)
					i += 3
				}
			}

			val minusones = sequence {
				var i = 2
				while (i <= stringLenght) {
					yield(i)
					i += 3
				}
			}
			return Pair(ones, minusones)
		} else {
			val ones = sequence {
				val i = 0
				while (i <= stringLenght)
			}

			val minusones = sequence {

			}

			return Pair(ones, minusones)
		}
	}

	fun applyPatternSmart(signal: List<Int>,
						  pattern: List<Int>,
						  myHashMap : MutableMap<Int, Pair<List<Int>, List<Int>>>,
						  it : Int) : List<Int> {
		var rvalue = ArrayList<Int>()
		for (j in 0 until signal.size) {
			val masks = getMasks(j, signal.size, pattern, myHashMap)
			val entry = masks.first.map { signal[it]  }.sum() - masks.second.map { signal[it]  }.sum()
			val digit = entry.absoluteValue % 10
			rvalue.add(digit)
		}
		return rvalue
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay16::class.java.getResourceAsStream(args[0]!!)))


		bufferedReader.useLines {
			it.forEach { line ->
				val basePattern = listOf(0, 1, 0, -1)
				var numbers : List<Int>
				var numbersQuick : List<Int>
				var myHashMap: MutableMap<Int, Pair<List<Int>, List<Int>>>

				myHashMap = HashMap()
				numbers = line.toCharArray().map { it.toString().toInt() }
				numbersQuick = numbers.toList()
				(0 until 100).forEach {
					numbers = applyPattern(numbers, basePattern, it)
					numbersQuick = applyPatternSmart(numbersQuick, basePattern, myHashMap, it)

					val rNumbers = numbers.take(8).joinToString(prefix = "", postfix = "", separator = "")
					val rNumbersQ = numbersQuick.take(8).joinToString(prefix = "", postfix = "", separator = "")
					if (rNumbers != rNumbersQ) {
						throw RuntimeException("Our Cached method failed ..")
					}
				}
				myHashMap = HashMap()
				val first7numbers = line.toCharArray()
						                       .take(7)
						                       .map { it.toString() }
						                       .joinToString(prefix = "", postfix = "", separator = "")
						                       .toInt()
				numbers = line.repeat(1000).toCharArray().map { it.toString().toInt() }
				(0 until 100).forEach {
					println("Running $it / 100")
					numbers = applyPatternSmart(numbers, basePattern, myHashMap, it)
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
