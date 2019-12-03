package com.ae.aoc2019.day03

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.absoluteValue

@SpringBootApplication
class Aoc2019ApplicationDay03 : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)

	class Point(val x : Int, val y : Int) {
		fun disstanceFromOrigin() : Int {
			return x.absoluteValue + y.absoluteValue
		}

		fun createPointFromHere(wireEntry : String) : Point {
			val direction = wireEntry[0]
			val range = wireEntry.substring(1).toInt()
			return when (direction) {
				'U' -> Point(x, y + range)
				'D' -> Point(x, y - range)
				'L' -> Point(x - range, y)
				'R' -> Point(x + range, y)
				else -> throw IllegalStateException("Unexpected input")
			}
		}
	}

	class WireEntry(val start: Point, val end: Point) {
		fun intersects(other: WireEntry) : Point? {
			return null
		}
	}

	class Wire(val wireEntries: ArrayList<WireEntry> = ArrayList()) {

		fun intersections(other: Wire) : List<Point> {
			return sequence {
				for (l in wireEntries) {
					for (r in other.wireEntries) {
						yield(l.intersects(r))
					}
				}
			}.filter { it != null }
			 .map { it!! }
			 .toList()
			 .sortedBy { it.disstanceFromOrigin() }
		}

		fun addWireEntry(wireEntry: String) {
			val start = if (wireEntries.isEmpty()) Point(0,0)
			        else Point(0,0)
			val end = start.createPointFromHere(wireEntry)
			wireEntries.add(WireEntry(start, end))
		}
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay03::class.java.getResourceAsStream(args[0]!!)))
		val first = Wire()
		val second = Wire()
		val l = bufferedReader.readLine().split(',').forEach { first.addWireEntry(it) }
		val r = bufferedReader.readLine().split(',').forEach { second.addWireEntry(it)  }
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay03>(*args)
}
