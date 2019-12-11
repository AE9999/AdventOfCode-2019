package com.ae.aoc2019.day11

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.collections.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay11 : CommandLineRunner {

	enum class Direction {
		UP,DOWN,LEFT,RIGHT
	}

	data class Point(val x : Int,  val y : Int) {
		fun nextPoint(direction : Direction) : Point {
			return when(direction) {
						Direction.UP -> Point(x, y + 1)
						Direction.DOWN -> Point(x, y - 1)
						Direction.LEFT -> Point(x -1 , y)
						Direction.RIGHT -> Point(x + 1, y)
			}
		}
	}

	class Robot(val programMem: MutableMap<BigInteger, BigInteger>,
				val hull : MutableMap<Point, BigInteger>) {

		var direction = Direction.UP
		var location = Point(0, 0)
		var pc : BigInteger = BigInteger.ZERO
		var relativeBase: BigInteger = BigInteger.ZERO
		val paintedPanels = HashSet<Point>()

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

		private fun run(inputs: Iterator<BigInteger>) : List<BigInteger> {
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
					programMem[v1] = inputs.next()
					pc += 2.toBigInteger()
				} else if (opcode == 4) {
					val v1 = readMem(instruction, 0)
					pc += 2.toBigInteger()
					rvalue.add(v1)
					if(rvalue.size == 2) {
						return rvalue
					}
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

		private fun calculateInput() : Iterator<BigInteger> {
			return listOf(hull.getValue(location)).iterator()
		}

		fun paintStuff() : Int {
			var output = run(calculateInput())
			while (output.isNotEmpty()) {
				paintedPanels.add(location)
				hull[location] = output[0]

				when(direction) {
					Direction.UP -> {
						direction = if (output[0] == BigInteger.ZERO) // Left
										Direction.LEFT
							        else // Right
										Direction.RIGHT
					}
					Direction.DOWN -> {
						direction = if (output[0] == BigInteger.ZERO) // Left
										Direction.RIGHT
									else // Right
										Direction.LEFT
					}
					Direction.LEFT -> {
						direction = if (output[0] == BigInteger.ZERO) // Left
										Direction.DOWN
									else // Right
										Direction.UP
					}
					Direction.RIGHT -> {
						direction = if (output[0] == BigInteger.ZERO) // Left
										Direction.UP
									else // Right
										Direction.DOWN
					}
				}
				location = location.nextPoint(direction)
				output = run(calculateInput())
			}
			return paintedPanels.size
		}
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay11::class.java.getResourceAsStream(args[0]!!)))

		bufferedReader.useLines {
			it.forEach { line ->
				val rawProgram = line.split(',')
				val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }
				val panel : MutableMap<Point, BigInteger> = HashMap<Point, BigInteger>().withDefault { BigInteger.ZERO }
				for (i in 0 until rawProgram.size) {
					programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
				}
				var robot : Robot
				robot = Robot(programMem.toMutableMap(), panel)
				robot.paintStuff()
			}
		}

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay11>(*args)
}
