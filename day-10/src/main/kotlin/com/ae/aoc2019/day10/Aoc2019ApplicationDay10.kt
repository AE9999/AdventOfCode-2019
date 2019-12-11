package com.ae.aoc2019.day10

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay10 : CommandLineRunner {


	data class Point(val x: Int, val y: Int)

	data class Asteroid(val point: Point)

	class AstroidField(rawField: List<String>) {

		private val astroids : MutableList<Asteroid> = ArrayList()

		private val occupied: MutableSet<Point> = HashSet()

		private val maxX: Int

		private val maxY: Int

		init {
			for (y in 0 until rawField.size) {
				for (x in 0 until  rawField.get(y).length) {
					if (rawField.get(y)[x] == '#') {
						val point = Point(x,y)
						astroids.add(Asteroid(point))
						occupied.add(point)
					}
				}
			}
			maxX = rawField.get(0).length
			maxY = rawField.size
		}

		fun numberViewAbleInLine(asteroid: Asteroid,
								 dx: Int,
								 dy: Int) : Int {
			var nextPoint = Point(asteroid.point.x +dx,  asteroid.point.y +dy)
			while ((nextPoint.x >= 0 && nextPoint.x <= maxX)
					&& (nextPoint.y >= 0 && nextPoint.y <= maxY)) {
				if (occupied.contains(nextPoint)) {
					return 1
				}
				nextPoint = Point(nextPoint.x + dx, nextPoint.y + dy)
			}
			return 0
		}

		private fun subSummedBy(dx : Int, dy : Int, exploredVector: Pair<Int,Int>) : Boolean {
			// Yes I'm that tired and lazy
			val myMax = listOf(dx, dy, exploredVector.first, exploredVector.second).max()!! + 1
			for (i in 1 until myMax) {
				if ((dx * i == exploredVector.first && dy * i == exploredVector.second)
					|| (dx == exploredVector.first * i && dy == exploredVector.second * i)) {
					return true
				}
			}
			return false
		}

		fun getLargestNumberOfViewable() : Pair<Asteroid, Int> {
			val dxs = 0 until maxX
			val dys = 0 until maxY
			val combinations = sequence {
				val exploredVectors = ArrayList<Pair<Int, Int>>()
				for (dx in dxs) {
					for (dy in dys) {
						if ((dx == 0 && dy == 0)
							 || exploredVectors.filter {subSummedBy(dx,dy, it) }.isNotEmpty()) {
							continue
						}
						exploredVectors.add(Pair(dx, dy))
						if (dx == 0) {
							yield(Pair(dx, dy))
							yield(Pair(dx, -dy))
						} else if (dy == 0) {
							yield(Pair(dx, dy))
							yield(Pair(-dx, dy))
						} else {
							yield(Pair(dx, dy))
							yield(Pair(-dx, dy))
							yield(Pair(dx, -dy))
							yield(Pair(-dx, -dy))
						}
					}
				}
			}.filter { it.first != 0 || it.second != 0 } /// Otherwise we will be standing still
			return astroids.map { asteroid ->
				                  val res = Pair(asteroid,
										         combinations.map {
																	numberViewAbleInLine(asteroid,
																						  it.first,
																						  it.second) }.sum()!!)
								  res
			                    }.sortedByDescending { it.second }.first()
		}
	}

	// From the example
	fun doLaserStuff() {

	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay10::class.java.getResourceAsStream(args[0]!!)))
		val rawField = ArrayList<String>()
		bufferedReader.useLines {
			it.forEach { line ->
				rawField.add(line.trim())
			}
		}
		val asteroidField = AstroidField(rawField)
		for (line in rawField) println("|${line}|");
		println("The max meteors visible his ${asteroidField.getLargestNumberOfViewable()}")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay10>(*args)
}
