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
				inc = 4
			} else if (opcode == 2) {
				val v1 = getValue(instruction, 0, pc, program)
				val v2 = getValue(instruction, 1, pc, program)
				val dest = program[pc + 3]
				program[dest] = v1 * v2
				inc = 4
			} else if (opcode == 3) {
				val v1 = program[pc + 1]
				program[v1] = inputs.next()
				inc = 2
			} else if (opcode == 4) {
				val v1 = getValue(instruction, 0, pc, program)
				System.out.println("Output $v1")
				inc = 2
			} else {
				throw RuntimeException("Illegal Instruction ${opcode}")
			}
			pc += inc
		}

		return program[0]
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay05::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				val program = ArrayList(line.split(",").map { it.toInt() })
				runProgram(program, arrayOf(1).iterator())
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay05>(*args)
}
