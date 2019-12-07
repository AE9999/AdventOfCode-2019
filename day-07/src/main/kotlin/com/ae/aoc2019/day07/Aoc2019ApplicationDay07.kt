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

	private fun getValue(instruction: String, parameter: Int, pc: Int, program: MutableList<Int>) : Int =
			if (instruction[2 - parameter].toString().toInt() == 1)
				program[pc + parameter + 1]
			else
				program[program[pc + parameter  + 1]]

	private fun runProgram(program: MutableList<Int>,
						   inputs: Iterator<Int>) : Int {

		var pc = 0 // Yeah I passed computer organisation back in the day.
		while (true) {
			val instruction = program[pc].toString().padStart(5, '0')
			val opcode = instruction.substring(3).toInt()
			if (opcode == 99) {
				break
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

	private fun runSequence(program: List<Int>,
							phaseSettings: List<Int>) : Int {
		var lastOutput = 0
		for (phaseSetting in phaseSettings) {
			lastOutput = runProgram(program.toMutableList(), arrayOf(phaseSetting, lastOutput).iterator())
		}
		println("Output: ${phaseSettings}: ${lastOutput} ..")
		return lastOutput
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay07::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val program = line.split(",").map { it.toInt() }.toList()
				println("Max output ${permute((0..4).toList()).map { runSequence(program, it) }.max()} ..")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay07>(*args)
}
