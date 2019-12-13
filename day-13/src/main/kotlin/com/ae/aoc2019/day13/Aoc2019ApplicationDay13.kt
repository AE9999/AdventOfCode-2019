package com.ae.aoc2019.day13

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay13 : CommandLineRunner {

	data class Point(val x : Int,  val y : Int)

	class Arcade(val programMem: MutableMap<BigInteger, BigInteger>) {

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
					try {
						programMem[v1] = inputs.next()
					} catch (e : NoSuchElementException) {
						System.out.println("Ran out of input.")
						return rvalue
					}
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

	fun intToChar(value: BigInteger) : String {
		return when(value) {
			BigInteger.ZERO -> "."
			BigInteger.ONE -> "#"
			2.toBigInteger() ->  "X"
			3.toBigInteger() ->  "="
			4.toBigInteger() ->  "o"
			else -> throw RuntimeException("Illegal Char")
		}
	}

	fun printOutput(output: List<BigInteger>) {
		print("\n\n")
		println("Still ${output.chunked(3).filter { it[2].toInt() == 2 }.size} block titles remaining.")

		val panel = HashMap<Point, BigInteger>().withDefault { BigInteger.ZERO }
		var score = BigInteger.ZERO
		output.chunked(3).forEach {
			if (it[0] == (-1).toBigInteger()) {
				score = it[2]
			} else {
				panel[Point(it[0].toInt(), it[1].toInt())] = it[2]
			}
		}

		val maxX: Int = panel.keys.map { it.x }.max()!!
		val minX: Int = panel.keys.map { it.x }.min()!!
		val maxY: Int = panel.keys.map { it.y }.max()!!
		val minY: Int = panel.keys.map { it.y }.min()!!

		for(y in ((minY-1)..(maxY+1)).reversed() ) { // Sure why not reversed.
			for (x in (minX-1)..(maxX+1)) {
				print(intToChar(panel.getValue(Point(x,y))))
			}
			print("\n")
		}
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay13::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val rawProgram = line.split(',')
				val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }
				for (i in 0 until rawProgram.size) {
					programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
				}


				var arcade: Arcade
				var output: List<BigInteger>

				arcade = Arcade(programMem.toMutableMap().withDefault { BigInteger.ZERO })
				output = arcade.run(listOf(BigInteger.ZERO).iterator())
				printOutput(output)

				programMem[BigInteger.ZERO] = 2.toBigInteger()


				arcade = Arcade(programMem.toMutableMap().withDefault { BigInteger.ZERO })

				val minusOne = (-1).toBigInteger()
				val one = BigInteger.ONE
				val commands = listOf(one, one, minusOne, minusOne)
				val input = object : Iterator<BigInteger> {
					var index = 0

					override fun hasNext(): Boolean {
						return true
					}

					override fun next(): BigInteger {
						if (index < commands.size) {
							return commands[index++]
						}
						return BigInteger.ZERO
					}
				}

				output = arcade.run(input)
				printOutput(output)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay13>(*args)
}
