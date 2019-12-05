package com.ae.aoc2019.day05

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

@SpringBootApplication
class Aoc2019ApplicationDay05 : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)

//	private fun getVal(program: ArrayList<Int>, value: Int, mode: Int) : Int {
//		if (mode == 1) return value
//		return program[value]
//	}

	private fun runProgram(noun: Int, verb: Int, program: ArrayList<Int>) : Int {
		program[1] = noun
		program[2] = verb

		var pc = 0 // Yeah I passed computer organisation back in the day.
		while (true) {
			val instruction = program[pc]
			var jump = 0
			if (program[pc] == 99) {
				break
			} else if (instruction == 1) {
				val v1 = program[program[pc + 1]]
				val v2 = program[program[pc + 2]]
				val dest = program[pc + 3]
				program[dest] = v1 + v2
			} else if (instruction == 2) {
				val v1 = program[program[pc + 1]]
				val v2 = program[program[pc + 2]]
				val dest = program[pc + 3]
				program[dest] = v1 * v2
			} else if (instruction== 3) {
				program[program[pc + 1]] = program[pc + 1]
			} else if (instruction == 3) {
				System.out.println(program[program[pc + 1]])
			}
			pc += 4
		}

		return program[0]
	}


	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay05::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				for (noun in 0..99) {
					for (verb in 0..99) {
						val program = ArrayList(line.split(",").map { it.toInt() })
						val result = runProgram(noun, verb, program)
						if (result == 19690720) {
							logger.info("Answer => ${100 * noun + verb} ..")
							return
						}
					}
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay05>(*args)
}
