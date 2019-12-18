package com.ae.aoc2019.day18

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay18 : CommandLineRunner {

	val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }

	data class Point(val x: Int, val y: Int) {
		fun up(): Point {  return Point(x, y+1) }
		fun down(): Point {  return Point(x, y  -1 ) }
		fun left(): Point {  return Point(x - 1, y  ) }
		fun right(): Point {  return Point(x + 1, y  ) }
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay18::class.java.getResourceAsStream(args[0]!!)))
		val panel = HashMap<Point, String>().withDefault { "#" }
		val doors = HashMap<String, Point>()
		val keys = HashMap<String, Point>()
		var currentLocation: Point?
		var currentY = 0
		var maxX = 0

		bufferedReader.useLines {
			it.forEach { line ->
				val charArray = line.trim().toCharArray()
				for (x in 0 until charArray.size) {
					panel[Point(x, currentY)] = charArray[x].toString()
					maxX = Math.max(x, maxX)
					when (charArray[x]) {
						in 'a'..'z' -> keys[charArray[x].toString()] = Point(x,currentY)
						in 'A'..'Z' -> doors[charArray[x].toString()] = Point(x,currentY)
						'#' -> {}
						'@' -> currentLocation = Point(x,currentY)
						else -> throw RuntimeException("Unexpected Input")
					}
				}
				currentY++
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay18>(*args)
}
