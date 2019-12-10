package com.ae.aoc2019.day10

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay10 : CommandLineRunner {


	data class Point(val x: Int, val y: Int)

	data class Asteroid(val point: Point)

	class AstroidField(val rawField: List<String>) {

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
			var viewable = 0
			var currentPoint = asteroid.point
			var nextPoint = Point(currentPoint.x + dx, currentPoint.x + dy)
			while (!occupied.contains(nextPoint)
					&& (nextPoint.x >= 0 && nextPoint.x <= maxX)
					&& (nextPoint.y >= 0 && nextPoint.y <= maxY)) {
				viewable++
			}
			return viewable
		}

		fun getLargestNumberOfViewable() : Int {

		}
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
