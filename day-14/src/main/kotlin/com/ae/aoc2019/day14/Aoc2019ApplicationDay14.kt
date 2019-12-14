package com.ae.aoc2019.day14

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import kotlin.collections.HashMap

@SpringBootApplication
class Aoc2019ApplicationDay14 : CommandLineRunner {

	data class Ingredient(val label: String)

	class FormulaUnit(rawUnit: String) {
		val amount: Int
		val ingredient: Ingredient

		init {
			val parsed = rawUnit.split(" ")
			amount = parsed[0].trim().toInt()
			ingredient = Ingredient(parsed[1].trim())
		}
	}

	class Formula(val rawInputs: String, val rawOutPut: String) {
		val inputs: Set<FormulaUnit>
		val output: FormulaUnit

		init {
		    inputs = rawInputs.split(",").map { FormulaUnit(it.trim()) }.toSet()
			output = FormulaUnit(rawOutPut)
		}

		override fun toString(): String {
			return "$rawInputs => $rawOutPut"
		}
	}

	data class MyState(val reserves: MutableMap<Ingredient, Int>,
					   val startingOreCost: Long,
					   val endingOreCost: Long,
					   val step: Long)

	val ore = Ingredient("ORE")
	val fuel = Ingredient("FUEL")

	private fun solve(formula: Formula,
			          ingredient2Formula: HashMap<Ingredient, Formula>,
					  reserves : MutableMap<Ingredient, Int>) : Long {

		var amount : Long = 0

		var formulasForMissingIngredients = formula.inputs.filter {
																	reserves.getValue(it.ingredient) < it.amount
																	&& it.ingredient != ore }

		// Make everything that is not ore
		while (formulasForMissingIngredients.isNotEmpty()) {
			val formulaToMake = ingredient2Formula[formulasForMissingIngredients.first().ingredient]!!
			amount += solve(formulaToMake, ingredient2Formula, reserves)

			formulasForMissingIngredients = formula.inputs.filter {
				                                                    reserves.getValue(it.ingredient) < it.amount
					                                                && it.ingredient != ore }
		}

		formula.inputs.forEach {
			if (it.ingredient == ore) {
				amount += it.amount
			} else {
				reserves[it.ingredient] = reserves.getValue(it.ingredient) - it.amount
			}
		}
		reserves[formula.output.ingredient] = reserves.getValue(formula.output.ingredient) + formula.output.amount

		return amount
	}

	fun printReserves(reserves: MutableMap<Ingredient, Int>) {
		reserves.entries.sortedBy { it.key.label }.forEach {
			println("${it.key.label} => ${it.value} ..")
		}
		println("")
		println("")
	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay14::class.java.getResourceAsStream(args[0]!!)))

		val pastStates = ArrayList<MyState>()
		val ingredient2Formula = HashMap<Ingredient, Formula>()
		bufferedReader.useLines {
			it.forEach { line ->
				val rawInputs = line.split("=>")[0]
				val rawOutput = line.split("=>")[1].trim()
				val formula = Formula(rawInputs, rawOutput)
				if (ingredient2Formula.contains(formula.output.ingredient)) {
					throw RuntimeException("Failed assumption ..")
				}
				ingredient2Formula[formula.output.ingredient] = formula
			}
		}

		val fuel = Ingredient("FUEL")
		val fuelFormula = ingredient2Formula[fuel]!!
		val reserves : MutableMap<Ingredient, Int> = HashMap<Ingredient, Int>().withDefault { 0 }

		var amountOreSpend : Long = 0
		var createdFuel : Long = 0
		var windingDown = false
		var step: Long = 0
		while(true) {
			val oreCost = solve(fuelFormula, ingredient2Formula, reserves)
			amountOreSpend += oreCost
			if (amountOreSpend >= 1000000000000L) {
				break
			}
			reserves[fuel] = 0
			createdFuel++

			println("A total of $amountOreSpend / 1000000000000L was required to create ${createdFuel}:fuel ..")
			val currentState = MyState(reserves.toMutableMap(),
					                   amountOreSpend - oreCost,
					                   amountOreSpend,
					                   step)

			step++

			if (!windingDown) {
				val phaseStart = pastStates.filter { it.reserves.equals(currentState.reserves) }.firstOrNull()
				if (phaseStart != null) {
					println("We returned to a state seen before ..")
					println("Current ..")
					printReserves(reserves)
					println("Past one ..")
					printReserves(phaseStart!!.reserves)
					println("Starting extropalation ..")
					// When did it occur
					val phaseLenght = (currentState.step - phaseStart.step) + 1
					val phaseOreCost = currentState.endingOreCost - phaseStart.startingOreCost
					while (amountOreSpend + phaseOreCost <= 1000000000000L) {
						amountOreSpend += phaseOreCost
						createdFuel += phaseLenght
					}
					windingDown = true
				}
				pastStates.add(currentState)
			}
		}
		println("Total amount of fuel created $createdFuel ..")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay14>(*args)
}
