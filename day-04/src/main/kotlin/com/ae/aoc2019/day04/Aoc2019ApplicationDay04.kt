package com.ae.aoc2019.day04

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay04 : CommandLineRunner {

	fun matches(target: String) : Boolean {
		var seenSequence = false
		var bound = target[0].toInt()
		for (i in 1 until target.length) {
			if (target[i].toInt() < bound) {
				return false
			}
			bound = max(bound, target[i].toInt())
			seenSequence =  seenSequence || target[i-1] == target[i]
		}
		return seenSequence
	}

	override fun run(vararg args: String?) {
		System.out.println(matches("111111"))
		System.out.println(matches("223450"))
		System.out.println(matches("123789"))
		System.out.println(matches("372037"))
		System.out.println(matches("905157"))
		var matches = 0;
		for (x in  372037..905157) {
			matches += if (matches(x.toString())) 1 else 0
		}
		System.out.println("Found $matches matches ..")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay04>(*args)
}
