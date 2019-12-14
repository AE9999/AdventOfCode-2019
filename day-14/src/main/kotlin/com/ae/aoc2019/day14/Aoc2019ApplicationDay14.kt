package com.ae.aoc2019.day14

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@SpringBootApplication
class Aoc2019ApplicationDay14 : CommandLineRunner {

	class Ingredient(val label: String)

	class FormulaUnit(rawUnit: String) {
		val amount: Int
		val ingredient: Ingredient

		init {
			val parsed = rawUnit.split(" ")
			amount = parsed[0].trim().toInt()
			ingredient = Ingredient(parsed[1].trim())
		}
	}

	class Formula(rawInputs: String, rawOutPut: String) {
		val inputs: Set<FormulaUnit>
		val output: FormulaUnit

		init {
		    inputs = rawInputs.split(",").map { FormulaUnit(it.trim()) }.toSet()
			output = FormulaUnit(rawOutPut)
		}
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay14::class.java.getResourceAsStream(args[0]!!)))
		val ingredient2Formula = HashMap<Ingredient, MutableSet<Formula>>().withDefault { HashSet() }
		bufferedReader.useLines {
			it.forEach { line ->
				val rawInputs = line.split("=>")[0]
				val rawOutput = line.split("=>")[1].trim()
				val formula = Formula(rawInputs, rawOutput)
				ingredient2Formula.getValue(formula.output.ingredient).add(formula)
			}
		}

		val ore = Ingredient("ORE")
		val fuel = Ingredient("FUEL")
		var amountOreSpend = 0

		if (ingredient2Formula.getValue(fuel).size > 1) {  throw RuntimeException("Failed assumption ..")  }
		val formula = ingredient2Formula.getValue(fuel).first()
		val toDoStack = Stack<FormulaUnit>()
		val reserves : MutableMap<Ingredient, Int> = HashMap<Ingredient, Int>().withDefault { 0 }
		toDoStack.push(FormulaUnit("1 FUEL"))
		while(!toDoStack.empty()) {
			val requirment = toDoStack.peek()
			val availableAmount = reserves.getValue(requirment.ingredient)
			if (requirment.ingredient == ore) {
				amountOreSpend += requirment.amount
				toDoStack.pop()
			} else {
				if (availableAmount >= requirment.amount) {
					reserves[requirment.ingredient] = reserves.getValue(requirment.ingredient) - requirment.amount
					toDoStack.pop()
				} else {
					if (ingredient2Formula.getValue(requirment.ingredient).size > 1) {
						throw RuntimeException("Failed assumption ..")
					}
					val formula = ingredient2Formula.getValue(requirment.ingredient).first()
					formula.inputs.forEach {
						if (availableAmount >= requirment.amount) {

						}
					}
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay14>(*args)
}
