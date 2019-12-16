package com.ae.aoc2019.day15

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap

@SpringBootApplication
class Aoc2019ApplicationDay15 : CommandLineRunner {

	val north = BigInteger.ONE
	val south = 2.toBigInteger()
	val west = 3.toBigInteger()
	val east = 4.toBigInteger()
	val wallOutput = BigInteger.ZERO
	val moveOutput = BigInteger.ONE
	val doneOutput = 2.toBigInteger()
	val noOuput = 99.toBigInteger()

	val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }

	class RepairDroid(val programMem: MutableMap<BigInteger, BigInteger>) {

		var pc : BigInteger = BigInteger.ZERO
		var relativeBase: BigInteger = BigInteger.ZERO

		private fun getDestination(instruction: String, parameter: Int) : BigInteger {
			if (instruction[2 - parameter].toString().toInt() == 2) {
				return relativeBase + programMem.getValue(pc + parameter.toBigInteger() + 1.toBigInteger())
			} else if (instruction[2 - parameter].toString().toInt() == 1) {
				return pc + parameter.toBigInteger() + BigInteger.ONE
			} else {
				return programMem.getValue(pc + parameter.toBigInteger() +  1.toBigInteger())
			}
		}

		private fun readMem(instruction: String, parameter: Int) : BigInteger {
			if (instruction[2 - parameter].toString().toInt() == 2) {
				val argumentValue = programMem.getValue(pc + parameter.toBigInteger() + BigInteger.ONE)
				return programMem.getValue(relativeBase + argumentValue) // Not plus one?
			} else if (instruction[2 - parameter].toString().toInt() == 1) {
				val index = pc + parameter.toBigInteger() + BigInteger.ONE
				return programMem.getValue(index)
			} else {
				val index = programMem.getValue(pc + parameter.toBigInteger() + BigInteger.ONE)
				return  programMem.getValue(index)
			}
		}

		fun run(inputs: Iterator<BigInteger>) : List<BigInteger> {
			val rvalue = ArrayList<BigInteger>()
			while (true) {
				val instruction = programMem[pc].toString().padStart(5, '0')
				val opcode = instruction.substring(3).toInt()
				if (opcode == 99) {
					return rvalue
				} else if (opcode == 1) {
					val v1 =  readMem(instruction, 0)
					val v2 = readMem(instruction, 1)
					val dest = getDestination(instruction, 2)
					programMem[dest] = v1 + v2
					pc += 4.toBigInteger()
				} else if (opcode == 2) {
					val v1 = readMem(instruction, 0)
					val v2 = readMem(instruction, 1)
					val dest = getDestination(instruction, 2)
					programMem[dest] = v1 * v2
					pc += 4.toBigInteger()
				} else if (opcode == 3) {
					val v1 = getDestination(instruction, 0)
					if (!inputs.hasNext()) {
						return rvalue
					}
					programMem[v1] = inputs.next()
					pc += 2.toBigInteger()
				} else if (opcode == 4) {
					val v1 = readMem(instruction, 0)
					pc += 2.toBigInteger()
					rvalue.add(v1)
				} else if (opcode == 5) {
					val v1 = readMem(instruction, 0).toInt()
					if (v1 != 0)
						pc = readMem(instruction, 1)
					else
						pc += 3.toBigInteger()
				} else if (opcode == 6) {
					val v1 = readMem(instruction, 0).toInt()
					if (v1 == 0)
						pc = readMem(instruction, 1)
					else
						pc += 3.toBigInteger()
				} else if (opcode == 7) {
					val v1 = readMem(instruction, 0)
					val v2 = readMem(instruction, 1)
					val dest = getDestination(instruction, 2)
					programMem[dest] = if (v1 < v2) BigInteger.ONE else BigInteger.ZERO
					pc += 4.toBigInteger()
				} else if (opcode == 8) {
					val v1 = readMem(instruction, 0)
					val v2 = readMem(instruction, 1)
					val dest = getDestination(instruction, 2)
					programMem[dest] = if (v1 == v2) BigInteger.ONE else BigInteger.ZERO
					pc += 4.toBigInteger()
				} else if (opcode == 9) {
					val v1 = readMem(instruction, 0)
					relativeBase += v1
					pc += 2.toBigInteger()
				} else {
					throw RuntimeException("Illegal Instruction ${opcode}")
				}
			}
		}
	}

	data class Point(val x : Int, val y: Int) {
		fun up() : Point = Point(x, y+1)
		fun down() : Point = Point(x, y - 1)
		fun left() : Point = Point(x - 1, y)
		fun right() : Point = Point(x  + 1, y)
	}

	data class SearchEntry(val point: Point,
				           val inputSequence: List<BigInteger>)

	fun calculateShortesPath(line: String) : List<BigInteger> {
		val rawProgram = line.split(',')
		for (i in 0 until rawProgram.size) {
			programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
		}

		val visitedPoints = HashSet<Point>()
		val searchQueue = ArrayDeque<SearchEntry>()
		searchQueue.push(SearchEntry(Point(0,0), listOf()))
		while (searchQueue.isNotEmpty()) {

			val searchEntry = searchQueue.pop()
			if (visitedPoints.contains(searchEntry.point)) {
				continue
			}

			visitedPoints.add(searchEntry.point)
			val repairDroid = RepairDroid(programMem.toMutableMap())
			val completeOutput = repairDroid.run(searchEntry.inputSequence.iterator())
			val lastOutput : BigInteger = completeOutput.lastOrNull() ?: noOuput

			if (lastOutput == wallOutput) {
				continue
			}

			if (lastOutput == doneOutput) {
				return searchEntry.inputSequence
			}

			val upPoint = searchEntry.point.up()
			if (!visitedPoints.contains(upPoint)) {
				val upList = searchEntry.inputSequence.toMutableList()
				upList.add(north)
				searchQueue.add(SearchEntry(upPoint, upList))
			}

			val downPoint = searchEntry.point.down()
			if (!visitedPoints.contains(downPoint)) {
				val downList = searchEntry.inputSequence.toMutableList()
				downList.add(south)
				searchQueue.add(SearchEntry(downPoint, downList))
			}

			val leftPoint = searchEntry.point.left()
			if (!visitedPoints.contains(leftPoint)) {
				val leftList = searchEntry.inputSequence.toMutableList()
				leftList.add(west)
				searchQueue.add(SearchEntry(leftPoint, leftList))
			}

			val rightpoint = searchEntry.point.right()
			if (!visitedPoints.contains(rightpoint)) {
				val rightList = searchEntry.inputSequence.toMutableList()
				rightList.add(east)
				searchQueue.add(SearchEntry(rightpoint, rightList))
			}
		}
		throw IllegalStateException("Should have found the exit ..")
	}

	fun exploreMap(line: String) : Map<Point, BigInteger> {
		val rawProgram = line.split(',')
		for (i in 0 until rawProgram.size) {
			programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
		}

		val visitedPoints = HashMap<Point, BigInteger>()
		val searchQueue = ArrayDeque<SearchEntry>()
		searchQueue.push(SearchEntry(Point(0,0), listOf()))
		while (searchQueue.isNotEmpty()) {

			val searchEntry = searchQueue.pop()
			if (visitedPoints.contains(searchEntry.point)) { continue }

			val repairDroid = RepairDroid(programMem.toMutableMap())
			val completeOutput = repairDroid.run(searchEntry.inputSequence.iterator())
			val lastOutput : BigInteger = completeOutput.lastOrNull() ?: moveOutput
			visitedPoints[searchEntry.point] = lastOutput

			if (lastOutput == wallOutput) { continue }

			val upPoint = searchEntry.point.up()
			if (!visitedPoints.contains(upPoint)) {
				val upList = searchEntry.inputSequence.toMutableList()
				upList.add(north)
				searchQueue.add(SearchEntry(upPoint, upList))
			}

			val downPoint = searchEntry.point.down()
			if (!visitedPoints.contains(downPoint)) {
				val downList = searchEntry.inputSequence.toMutableList()
				downList.add(south)
				searchQueue.add(SearchEntry(downPoint, downList))
			}

			val leftPoint = searchEntry.point.left()
			if (!visitedPoints.contains(leftPoint)) {
				val leftList = searchEntry.inputSequence.toMutableList()
				leftList.add(west)
				searchQueue.add(SearchEntry(leftPoint, leftList))
			}

			val rightpoint = searchEntry.point.right()
			if (!visitedPoints.contains(rightpoint)) {
				val rightList = searchEntry.inputSequence.toMutableList()
				rightList.add(east)
				searchQueue.add(SearchEntry(rightpoint, rightList))
			}
		}

		return visitedPoints
	}

	fun paintPanel(panel : Map<Point, BigInteger>) {
		val maxX: Int = panel.keys.map { it.x }.max()!!
		val minX: Int = panel.keys.map { it.x }.min()!!
		val maxY: Int = panel.keys.map { it.y }.max()!!
		val minY: Int = panel.keys.map { it.y }.min()!!

		for(y in ((minY-1)..(maxY+1)) ) { // Sure why not reversed.
			for (x in (minX-1)..(maxX+1)) {
				val char = when(panel.getValue(Point(x,y))) {
					wallOutput -> "#"
					moveOutput -> "."
					doneOutput -> "O"
					else -> "?"
				}
				print(char)
			}
			print("\n")
		}
	}

	data class FillEntry(val point: Point,
					     val minute: Int)

	fun calculateOxygenTime(panel : MutableMap<Point, BigInteger>) : Int {
		val fillQueue = ArrayDeque<FillEntry>()
		var maxtime = 0
		fillQueue.push(FillEntry(panel.entries.filter { it.value == doneOutput }.first().key, maxtime))
		while (fillQueue.isNotEmpty()) {
			val fillentry = fillQueue.pop()
			maxtime = Math.max(maxtime, fillentry.minute)
			val up = fillentry.point.up()
			if (panel[up] == moveOutput) {
				panel[up] = doneOutput
				fillQueue.add(FillEntry(up, maxtime + 1))
			}
			val down = fillentry.point.down()
			if (panel[down] == moveOutput) {
				panel[down] = doneOutput
				fillQueue.add(FillEntry(down, maxtime + 1))
			}
			val right = fillentry.point.right()
			if (panel[right] == moveOutput) {
				panel[right] = doneOutput
				fillQueue.add(FillEntry(right, maxtime + 1))
			}
			val left = fillentry.point.left()
			if (panel[left] == moveOutput) {
				panel[left] = doneOutput
				fillQueue.add(FillEntry(left, maxtime + 1))

			}
		}
		return maxtime
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay15::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val shortestPath = calculateShortesPath(line)
				println("Found ${shortestPath} (${shortestPath.size}) as the shortest path ")

				val panel = exploreMap(line).toMutableMap().withDefault { (-1).toBigInteger() }
				paintPanel(panel)

				println("This room is filled in ${calculateOxygenTime(panel)} minutes")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay15>(*args)
}
