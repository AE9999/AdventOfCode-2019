package com.ae.aoc2019.day07

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

@SpringBootApplication
class Aoc2019ApplicationDay07 : CommandLineRunner {

	private fun <T> permute(input: List<T>): List<List<T>> {
		// Stolen from https://rosettacode.org/wiki/Permutations#Kotlin
		if (input.size == 1) return listOf(input)
		val perms = mutableListOf<List<T>>()
		val toInsert = input[0]
		for (perm in permute(input.drop(1))) {
			for (i in 0..perm.size) {
				val newPerm = perm.toMutableList()
				newPerm.add(i, toInsert)
				perms.add(newPerm)
			}
		}
		return perms
	}

	class Program(val program: MutableList<Int>) {
		var pc = 0

		private fun getValue(instruction: String, parameter: Int, pc: Int, program: MutableList<Int>) : Int =
				if (instruction[2 - parameter].toString().toInt() == 1)
					program[pc + parameter + 1]
				else
					program[program[pc + parameter  + 1]]

		fun run(inputs: Iterator<Int>) : Int {
			while (true) {
				val instruction = program[pc].toString().padStart(5, '0')
				val opcode = instruction.substring(3).toInt()
				if (opcode == 99) {
					return -1
				} else if (opcode == 1) {
					val v1 =  getValue(instruction, 0, pc, program)
					val v2 = getValue(instruction, 1, pc, program)
					val dest = program[pc + 3]
					program[dest] = v1 + v2
					pc += 4
				} else if (opcode == 2) {
					val v1 = getValue(instruction, 0, pc, program)
					val v2 = getValue(instruction, 1, pc, program)
					val dest = program[pc + 3]
					program[dest] = v1 * v2
					pc += 4
				} else if (opcode == 3) {
					val v1 = program[pc + 1]
					program[v1] = inputs.next()
					pc += 2
				} else if (opcode == 4) {
					val v1 = getValue(instruction, 0, pc, program)
					pc += 2
					return v1
				} else if (opcode == 5) {
					val v1 = getValue(instruction, 0, pc, program)
					if (v1 != 0)
						pc = getValue(instruction, 1, pc, program)
					else
						pc += 3
				} else if (opcode == 6) {
					val v1 = getValue(instruction, 0, pc, program)
					if (v1 == 0)
						pc = getValue(instruction, 1, pc, program)
					else
						pc += 3
				} else if (opcode == 7) {
					val v1 = getValue(instruction, 0, pc, program)
					val v2 = getValue(instruction, 1, pc, program)
					val dest = program[pc + 3]
					program[dest] = if (v1 < v2) 1 else 0
					pc += 4
				} else if (opcode == 8) {
					val v1 = getValue(instruction, 0, pc, program)
					val v2 = getValue(instruction, 1, pc, program)
					val dest = program[pc + 3]
					program[dest] = if (v1 == v2) 1 else 0
					pc += 4
				} else {
					throw RuntimeException("Illegal Instruction ${opcode}")
				}
			}
			throw RuntimeException("Program terminated without output ..")
		}
	}

	private fun runSequence(program: List<Int>,
							phaseSettings: List<Int>) : Int {
		var lastOutput = 0
		for (phaseSetting in phaseSettings) {
			val program = Program(program.toMutableList())
			lastOutput = program.run(arrayOf(phaseSetting, lastOutput).iterator())
		}
		println("Output: ${phaseSettings}: ${lastOutput} ..")
		return lastOutput
	}

	private fun runWithFeedBack(program: List<Int>,
								phaseSettings: List<Int>) : Int {
		val programs = phaseSettings.map { Program(program.toMutableList()) }.toList()
		var lastOutput = 0
		for (i in 0 until phaseSettings.size) {
			lastOutput = programs[i].run( arrayOf(phaseSettings[i], lastOutput).iterator() )
		}

		while (true) {
			for (i in 0 until phaseSettings.size) {
				val extraOutput = programs[i].run(arrayOf(lastOutput).iterator())
				if (extraOutput == -1) {
					return lastOutput
				}
				lastOutput = extraOutput
			}
			println("Finished ${phaseSettings} final output:  ${lastOutput} ..")
		}
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay07::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val program = line.split(",").map { it.toInt() }.toList()
				println("Max output ${permute((0..4).toList()).map { runSequence(program, it) }.max()} ..")

				println("Max output with feedback ${permute((5..9).toList()).map { runWithFeedBack(program, it) }.max()} ..")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay07>(*args)
}
