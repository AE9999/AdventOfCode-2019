package com.ae.aoc2019.day19

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.min

@SpringBootApplication
class Aoc2019ApplicationDay19 : CommandLineRunner {

	val programMem = HashMap<BigInteger, BigInteger>().withDefault { BigInteger.ZERO }

	class ASCII(val programMem: MutableMap<BigInteger, BigInteger>) {

		var pc: BigInteger = BigInteger.ZERO
		var relativeBase: BigInteger = BigInteger.ZERO

		private fun getDestination(instruction: String, parameter: Int): BigInteger {
			if (instruction[2 - parameter].toString().toInt() == 2) {
				return relativeBase + programMem.getValue(pc + parameter.toBigInteger() + 1.toBigInteger())
			} else if (instruction[2 - parameter].toString().toInt() == 1) {
				return pc + parameter.toBigInteger() + BigInteger.ONE
			} else {
				return programMem.getValue(pc + parameter.toBigInteger() + 1.toBigInteger())
			}
		}

		private fun readMem(instruction: String, parameter: Int): BigInteger {
			if (instruction[2 - parameter].toString().toInt() == 2) {
				val argumentValue = programMem.getValue(pc + parameter.toBigInteger() + BigInteger.ONE)
				return programMem.getValue(relativeBase + argumentValue) // Not plus one?
			} else if (instruction[2 - parameter].toString().toInt() == 1) {
				val index = pc + parameter.toBigInteger() + BigInteger.ONE
				return programMem.getValue(index)
			} else {
				val index = programMem.getValue(pc + parameter.toBigInteger() + BigInteger.ONE)
				return programMem.getValue(index)
			}
		}

		fun run(inputs: Iterator<BigInteger>): List<BigInteger> {
			val rvalue = ArrayList<BigInteger>()
			while (true) {
				val instruction = programMem[pc].toString().padStart(5, '0')
				val opcode = instruction.substring(3).toInt()
				if (opcode == 99) {
					return rvalue
				} else if (opcode == 1) {
					val v1 = readMem(instruction, 0)
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

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay19::class.java.getResourceAsStream(args[0]!!)))

		bufferedReader.useLines {
			it.forEach { line ->
				val rawProgram = line.split(',')
				for (i in 0 until rawProgram.size) {
					programMem[i.toBigInteger()] = BigInteger(rawProgram[i])
				}
				val ascii = ASCII(programMem.toMutableMap())
				val outputs = ascii.run(listOf<BigInteger>().iterator())
				for  (output in outputs) {
					print(output.toInt().toChar().toString())
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay19>(*args)
}
