package com.ae.aoc2019.day13

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.collections.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay13 : CommandLineRunner {

	data class Point(val x : Int,  val y : Int)

	val zero = BigInteger.ZERO
	val one = BigInteger.ONE
	val minusOne = (-1).toBigInteger()
	val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }

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

		fun run(inputs: Iterator<BigInteger>) : List<BigInteger>? {
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
						return null
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
		val panel = HashMap<Point, BigInteger>().withDefault { BigInteger.ZERO }
		var rocks = 0
		output.chunked(3).forEach {
			if (it[0] != (-1).toBigInteger()) {
				panel[Point(it[0].toInt(), it[1].toInt())] = it[2]
				if (it[2].toInt() == 2) rocks++
			}
		}

		val maxX: Int = panel.keys.map { it.x }.max()!!
		val minX: Int = panel.keys.map { it.x }.min()!!
		val maxY: Int = panel.keys.map { it.y }.max()!!
		val minY: Int = panel.keys.map { it.y }.min()!!
		var paintedRocks = 0
		for(y in ((minY-1)..(maxY+1)).reversed() ) { // Sure why not reversed.
			for (x in (minX-1)..(maxX+1)) {
				val char = intToChar(panel.getValue(Point(x,y)))
				if (char == "X") {
					paintedRocks++
				}
				print(char)
			}
			print("\n")
		}
		println("Rocks: $rocks, Painted Rocker $paintedRocks ..")
	}

//	data class GameState(remainingRocks: Int,
//
//						 )
	data class OutputAnalysis(val score: BigInteger?,
							  val paintedRocks: Int,
							  val ballLocation: Point?,
							  val barLocation: Point?,
							  val maxX: Int,
							  val minX: Int,
							  val maxY: Int,
							  val minY: Int)

	fun analyseOutput(output: List<BigInteger>) : OutputAnalysis {
		val panel = HashMap<Point, BigInteger>().withDefault { BigInteger.ZERO }
		var score: BigInteger? = null
		var ballLocation: Point? = null
		var barLocation: Point? = null
		output.chunked(3).forEach {
			if (it[0] != (-1).toBigInteger()) {
				panel[Point(it[0].toInt(), it[1].toInt())] = it[2]
			} else {
				score = it[2]
			}
		}

		val maxX: Int = panel.keys.map { it.x }.max()!!
		val minX: Int = panel.keys.map { it.x }.min()!!
		val maxY: Int = panel.keys.map { it.y }.max()!!
		val minY: Int = panel.keys.map { it.y }.min()!!
		var paintedRocks = 0
		for(y in ((minY-1)..(maxY+1)).reversed() ) { // Sure why not reversed.
			for (x in (minX-1)..(maxX+1)) {
				val point = Point(x,y)
				val char = intToChar(panel.getValue(point))
				if (char == "X") {
					paintedRocks++
				}
				if (char == "=") {
					barLocation = point
				}
				if (char == "o") {
					ballLocation = point
				}
			}
		}

		return OutputAnalysis(score,
				              paintedRocks,
				              ballLocation,
				              barLocation,
							  maxX,
							  minX,
							  maxY,
							  minY)

	}

	fun runIntput(input: Iterator<BigInteger>) : List<BigInteger>? {
		val arcade = Arcade(programMem.toMutableMap().withDefault { BigInteger.ZERO })
		return arcade.run(input)
	}

	fun getNextViable(windowSize: Int, outputAnalysis: OutputAnalysis) : List<BigInteger>? {
		// Try going more to the left
		for (amountLeft in 1 .. outputAnalysis.barLocation!!.x) {
			if (amountLeft >= windowSize) { continue }
			val baseInput = (0 until (windowSize - amountLeft)).map { zero }.toMutableList()
			val lefts = (0 until amountLeft).map { minusOne }
			baseInput.addAll(lefts)
			val output = runIntput(baseInput.iterator()) ?: return baseInput
			printOutput(output!!)

		}

		// Try going more to the right
		for (amountRight in 1..outputAnalysis.maxX) {
			if (amountRight >= windowSize) { continue }
			val baseInput = (0 until (windowSize - amountRight)).map { zero }.toMutableList()
			val rights = (0 until amountRight).map { one }
			baseInput.addAll(rights)
			val output = runIntput(baseInput.iterator())
			if (output == null)  {
				return baseInput
			}
			printOutput(output!!)
		}

		return null
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay13::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val rawProgram = line.split(',')
				for (i in 0 until rawProgram.size) {
					programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
				}
				var arcade: Arcade
				var output: List<BigInteger>?

				arcade = Arcade(programMem.toMutableMap().withDefault { BigInteger.ZERO })
				output = arcade.run(listOf(BigInteger.ZERO).iterator())
				printOutput(output!!)

				programMem[BigInteger.ZERO] = 2.toBigInteger()
				val knownCorrectInputs = ArrayList<List<BigInteger>>()

				output = null

				while (true) {
					var input = ArrayList<BigInteger>()
					knownCorrectInputs.forEach { input.addAll(it) }

					var newInput = ArrayList<BigInteger>()
					while (output == null) {
						newInput.add(zero)
						val currentInput = input.toMutableList()
						currentInput.addAll(newInput)
						output = runIntput(currentInput.iterator())
					}

					// We died. Figuring out how to prevent it
					val outputAnalysis = analyseOutput(output)
					val windowSize = newInput.size
					println("Score: ${outputAnalysis.score
							?: "X"} remainingRocks: $outputAnalysis.remainingRocks , inputs ${windowSize}..")
					printOutput(output!!)

					val knownCorrectInput = getNextViable(windowSize, outputAnalysis)
					if (knownCorrectInput == null) {
						throw IllegalStateException("We are fucked now ..")
					}

					// Start using better ideas ..

					knownCorrectInputs.add(knownCorrectInput)
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay13>(*args)
}
