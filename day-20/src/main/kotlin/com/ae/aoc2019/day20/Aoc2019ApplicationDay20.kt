package com.ae.aoc2019.day20

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay20 : CommandLineRunner {

	val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }

	data class Point(val x: Int, val y: Int) {
		fun up(): Point {  return Point(x, y+1) }
		fun down(): Point {  return Point(x, y  -1 ) }
		fun left(): Point {  return Point(x - 1, y  ) }
		fun right(): Point {  return Point(x + 1, y  ) }

		fun asInput(): Iterator<BigInteger> = listOf(x.toBigInteger(), y.toBigInteger()).iterator()
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay20::class.java.getResourceAsStream(args[0]!!)))

		bufferedReader.useLines {
			it.forEach { line ->
				val rawProgram = line.split(',')
				for (i in 0 until rawProgram.size) {
					programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay20>(*args)
}
