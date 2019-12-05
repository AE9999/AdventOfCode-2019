package com.ae.aoc2019.day05

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.RuntimeException

@SpringBootApplication
class Aoc2019ApplicationDay05 : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)

	private fun getValue(instruction: String, parameter: Int, pc: Int, program: ArrayList<Int>) : Int {
		return if (instruction[2 - parameter].toString().toInt() == 1)
					program[pc + parameter + 1]
		       else
					program[program[pc + parameter  + 1]]
	}

	private fun runProgram(program: ArrayList<Int>,
						   inputs: Iterator<Int>) : Int {

		var pc = 0 // Yeah I passed computer organisation back in the day.
		while (true) {
			val instruction = program[pc].toString().padStart(5, '0')
			val opcode = instruction.substring(3).toInt()
			var inc: Int
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
				System.out.println("Output $v1")
				pc += 2
			} else if (opcode == 5) {
				/* Opcode 5 is jump-if-true:
				if the first parameter is non-zero,
				it sets the instruction pointer to the value from the second parameter.
				Otherwise, it does nothing. */
				val v1 = getValue(instruction, 0, pc, program)
				if (v1 != 0)
					pc = getValue(instruction, 1, pc, program)
			    else
					pc += 3
			} else if (opcode == 6) {
				/*Opcode 6 is jump-if-false:
				if the first parameter is zero,
				it sets the instruction pointer to the value from the second parameter.
				Otherwise, it does nothing*/
				val v1 = getValue(instruction, 0, pc, program)
				if (v1 == 0)
					pc = getValue(instruction, 1, pc, program)
				else
					pc += 3
			} else if (opcode == 7) {
				/*Opcode 7 is less than:
				   if the first parameter is less than the second parameter,
				   it stores 1 in the position given by the third parameter.
				   Otherwise, it stores 0*/
				val v1 = getValue(instruction, 0, pc, program)
				val v2 = getValue(instruction, 1, pc, program)
				val dest = program[pc + 3]
				program[dest] = if (v1 < v2) 1 else 0
				pc += 4
			} else if (opcode == 8) {
				/*Opcode 8 is equals: if the first parameter is equal to the second parameter,
				it stores 1 in the position given by the third parameter.
				Otherwise, it stores 0.*/
				val v1 = getValue(instruction, 0, pc, program)
				val v2 = getValue(instruction, 1, pc, program)
				val dest = program[pc + 3]
				program[dest] = if (v1 == v2) 1 else 0
				pc += 4
			} else {
				throw RuntimeException("Illegal Instruction ${opcode}")
			}
		}

		return program[0]
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay05::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val program = ArrayList(line.split(",").map { it.toInt() })
				System.out.println("Airco Stuff")
				runProgram(program, arrayOf(1).iterator())
				System.out.println("Thermal Stuff")
				runProgram(program, arrayOf(5).iterator())
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay05>(*args)
}
