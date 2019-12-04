package com.ae.aoc2019.day03

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
class Aoc2019ApplicationDay03 : CommandLineRunner {

	class Point(val x : Int, val y : Int) {
		fun distanceFromOrigin() : Int {
			return x.absoluteValue + y.absoluteValue
		}

		fun createPointFromHere(wireEntry : String) : Point {
			val direction = wireEntry[0]
			val range = wireEntry.substring(1).toInt()
			return when (direction) {
				'U' -> Point(x, y + range)
				'D' -> Point(x, y - range)
				'L' -> Point(x - 	range, y)
				'R' -> Point(x + range, y)
				else -> throw IllegalStateException("Unexpected input")
			}
		}

		override fun toString(): String {
			return "Point(x=$x, y=$y)"
		}
	}

	class WireEntry(val start: Point, val end: Point) {

		fun xrange(): IntRange {
			return min(start.x, end.x)..max(start.x, end.x)
		}

		fun yrange(): IntRange {
			return min(start.y, end.y)..max(start.y, end.y)
		}

		fun contains(point: Point) : Boolean {
			return xrange().contains(point.x) && yrange().contains(point.y)
		}

		fun lenght() : Int {
			return (max(start.x, end.x) - min(start.x, end.x)) +
				   (max(start.y, end.y) - min(start.y, end.y))
		}

		fun lenghtToPoint(point: Point) : Int {
			return (max(start.x, point.x) - min(start.x, point.x)) +
					(max(start.y, point.y) - min(start.y, point.y))
		}

		fun intersects(other: WireEntry) : Point? {
			val xintersection = xrange().intersect(other.xrange())
			val yintersection = yrange().intersect(other.yrange())
			if (xintersection.isEmpty() || yintersection.isEmpty()) {
				return null
			}
			val point = Point(xintersection.first(), yintersection.first())
			return point
		}

		override fun toString(): String {
			return "WireEntry(start=$start, end=$end)"
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
			 .sortedBy { it.distanceFromOrigin() }
		}

		fun addWireEntry(wireEntry: String) {
			val start = if (wireEntries.isEmpty()) Point(0,0)
			            else wireEntries.last().end
			val end = start.createPointFromHere(wireEntry)
			wireEntries.add(WireEntry(start, end))
		}

		override fun toString(): String {
			return "Wire(wireEntries=$wireEntries)"
		}

		fun distanceToPoint(point: Point): Int {
			var travledDistance = 0
			for (wireEntry in wireEntries) {
				if (wireEntry.contains(point)) {
					travledDistance += wireEntry.lenghtToPoint(point)
					return travledDistance
				}
				travledDistance += wireEntry.lenght()
			}
			throw IllegalStateException("Did not reach specified point ..")
		}
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay03::class.java.getResourceAsStream(args[0]!!)))
		val firstWire = Wire()
		val secondWire = Wire()
		bufferedReader.readLine().split(',').forEach { firstWire.addWireEntry(it) }
		bufferedReader.readLine().split(',').forEach { secondWire.addWireEntry(it)  }
		val intersections = firstWire.intersections(secondWire).filter { it.x != 0 || it.y != 0 }
		val result = intersections.first()
		System.out.println("Solution: ${result.distanceFromOrigin()} ..")
		val closestIntersection = intersections.map { Pair(it, firstWire.distanceToPoint(it) + secondWire.distanceToPoint(it) ) }
				                               .sortedBy { it.second }
				                               .first()
		System.out.println("Solution: ${closestIntersection} ..")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay03>(*args)
}
