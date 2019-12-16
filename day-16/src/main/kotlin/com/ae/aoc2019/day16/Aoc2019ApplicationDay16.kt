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
			println("Running $i out of ${signal.size} ..")
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

	val myHashMap = HashMap<Int, Pair<List<Int>, List<Int>>>()

	fun getMasks(index: Int, stringLenght : Int, pattern: List<Int>) : Pair<List<Int>, List<Int>> {
		if(myHashMap.contains(index)) {
			return myHashMap[index]!!
		}
		val iterator = getIterator(index, pattern)

		val ones = ArrayList<Int>()
		val minusones = ArrayList<Int>()
		for (i in 0 until stringLenght) {
			val next = iterator.next()
			if (next == 0) continue
			if (next == 1) ones.add(i)
			else minusones.add(i)
		}
		myHashMap[index] = Pair(ones, minusones)
		return myHashMap[index]!!
	}

	fun applyPatternSmart(signal: List<Int>, pattern: List<Int>, it : Int) : List<Int> {
		var rvalue = ArrayList<Int>()
		var entry = 0L
		val masks = getMasks(0, signal.size.toInt(), pattern)
		for (j in 0 until signal.size) {
//			println("Running $j out of ${signal.size} ..")
			entry += masks.first.map { signal[it]  }.sum()
			entry -= masks.second.map { signal[it]  }.sum()
			val digit = entry.absoluteValue.toInt() % 10
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
				numbers = line.toCharArray().map { it.toString().toInt() }
				(0 until 100).forEach {
					numbers = applyPatternSmart(numbers, basePattern, it)
				}
				println("Result: ${numbers.take(8).joinToString(prefix = "", postfix = "", separator = "") }")

				val first7numbers = line.toCharArray()
						                       .take(7)
						                       .map { it.toString() }
						                       .joinToString(prefix = "", postfix = "", separator = "")
						                       .toInt()
				numbers = line.repeat(1000).toCharArray().map { it.toString().toInt() }
				(0 until 100).forEach {
					numbers = applyPatternSmart(numbers, basePattern, it)
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
