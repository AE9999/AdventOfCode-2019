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
	companion object {
//		val logger = LoggerFactory.getLogger(javaClass)
	}

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

		fun intersects(other: WireEntry) : Point? {
//			System.out.println("Checking ${this} vs ${other} ")
			val xintersection = xrange().intersect(other.xrange())
			val yintersection = yrange().intersect(other.yrange())
			if (xintersection.isEmpty() || yintersection.isEmpty()) {
//				System.out.println("No intersection ..")
				return null
			}
			val point = Point(xintersection.first(), yintersection.first())
//			System.out.println("Found ${point} as intersection ..\n\n")
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


	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay03::class.java.getResourceAsStream(args[0]!!)))
		val firstWire = Wire()
		val secondWire = Wire()
		bufferedReader.readLine().split(',').forEach { firstWire.addWireEntry(it) }
//		System.out.println("First wire:\n$firstWire")
		bufferedReader.readLine().split(',').forEach { secondWire.addWireEntry(it)  }
//		System.out.println("Second wire:\n$secondWire\n\n")
		val intersections = firstWire.intersections(secondWire)
		val result = intersections.filter { it.x != 0 || it.y != 0 }
				                  .first()
		System.out.println("Solution: ${result.distanceFromOrigin()} ..")


	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay03>(*args)
}
