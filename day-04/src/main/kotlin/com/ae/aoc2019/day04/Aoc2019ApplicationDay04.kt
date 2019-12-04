package com.ae.aoc2019.day04

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.math.max

@SpringBootApplication
class Aoc2019ApplicationDay04 : CommandLineRunner {

	fun matches(target: String) : Boolean {
		var seenSequence = false
		var bound = target[0].toInt()
		for (i in 1 until target.length) {
			if (target[i].toInt() < bound) return false
			bound = max(bound, target[i].toInt())
			seenSequence =  seenSequence || target[i-1] == target[i]
		}
		return seenSequence
	}

	fun matchesMoreStrict(target: String) : Boolean {
		var bound = target[0].toInt()
		var seenStrictSequence = false
		var cc = target[0]
		var ca = 1
		for (i in 1 until target.length) {
			if (target[i].toInt() < bound) return false
			bound = max(bound, target[i].toInt())
			if (cc == target[i]) {
				ca += 1
			} else {
				seenStrictSequence = seenStrictSequence || ca == 2
				cc = target[i]
				ca = 1
			}
		}
		return seenStrictSequence || ca == 2
	}

	override fun run(vararg args: String?) {
		var matches = 0
		for (x in  372037..905157) {
			matches += if (matches(x.toString())) 1 else 0
		}
		System.out.println("Found $matches matches ..")

		matches = 0
		for (x in  372037..905157) {
			matches += if (matchesMoreStrict(x.toString())) 1 else 0
		}
		System.out.println("Found $matches more strict matches ..")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay04>(*args)
}
