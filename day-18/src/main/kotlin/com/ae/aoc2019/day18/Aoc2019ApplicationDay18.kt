package com.ae.aoc2019.day18

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.*
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

	data class SearchEntry(val point: Point,
						   val distance: Int)

	fun distance(a: Point, b: Point, panel: HashMap<Point, String>) : Int {
		val visitedPoints = HashSet<Point>()
		val searchQueue = ArrayDeque<SearchEntry>()
		searchQueue.push(SearchEntry(a, 0))
		while (searchQueue.isNotEmpty()) {

			val searchEntry = searchQueue.pop()
			if (visitedPoints.contains(searchEntry.point)) {
				continue
			}
			visitedPoints.add(searchEntry.point)

			if (searchEntry.point == b) {
				return searchEntry.distance
			}

			if (panel.getValue(searchEntry.point) == "#") {
				continue
			}

			val upPoint = searchEntry.point.up()
			if (!visitedPoints.contains(upPoint)) {
				searchQueue.add(SearchEntry(upPoint, searchEntry.distance + 1))
			}

			val downPoint = searchEntry.point.down()
			if (!visitedPoints.contains(downPoint)) {
				searchQueue.add(SearchEntry(downPoint, searchEntry.distance + 1))
			}

			val leftPoint = searchEntry.point.left()
			if (!visitedPoints.contains(leftPoint)) {
				searchQueue.add(SearchEntry(leftPoint, searchEntry.distance + 1))
			}

			val rightpoint = searchEntry.point.right()
			if (!visitedPoints.contains(rightpoint)) {
				searchQueue.add(SearchEntry(rightpoint, searchEntry.distance + 1))
			}
		}
		throw RuntimeException("Unable to calculate distance ..")
	}

	private fun solve(currentLocation: Point,
					  keys: HashMap<String, Point>,
					  doors: HashMap<String, Point>,
					  panel: MutableMap<Point, String>): Int {
		return 0
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay18::class.java.getResourceAsStream(args[0]!!)))
		val panel = HashMap<Point, String>().withDefault { "#" }
		val doors = HashMap<String, Point>()
		val keys = HashMap<String, Point>()
		var currentLocation: Point? = null
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
		val result = solve(currentLocation!!, keys, doors, panel)
		println("Need at least $result step to reclaim all keys")

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay18>(*args)
}
