package com.ae.aoc2019.day08

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay08(private val wide: Int = 25,
							  private val tall: Int = 6) : CommandLineRunner {



	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay08::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val layers = line.toCharArray()
						         .map { it.toString().toInt() }
						         .chunked(wide * tall)
				val leastZeros = layers.sortedBy { it.filter { it == 0 }.count() }
						               .first()
				println("Calculated code: ${leastZeros.filter { it == 1 }.count() * leastZeros.filter { it == 2 }.count()}  ..")
				for (y in 0 until tall) {
					for (x in 0 until wide) {
						for (z in 0 until layers.size) {
							val myChar = layers[z].get((y * wide) + x)
							if (myChar != 2) {
								print(if (myChar == 0) "X" else "." )
								break
							}
						}
					}
					print("\n")
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay08>(*args)
}
