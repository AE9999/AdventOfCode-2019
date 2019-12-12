package com.ae.aoc2019.day12

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

@SpringBootApplication
class Aoc2019ApplicationDay12 : CommandLineRunner {

	class MyVec(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
		operator fun plusAssign(speed: MyVec) {
			x += speed.x
			y += speed.y
			z += speed.z
		}

		operator fun get(i: Int) : Int {
			return when(i) {
				0 -> x
				1 -> y
				2 -> z
				else -> throw RuntimeException("Illegal accessor $i")
			}
		}

		operator fun set(i: Int, value: Int) {
			when(i) {
				0 -> x = value
				1 -> y = value
				2 -> z = value
				else -> throw RuntimeException("Illegal accessor $i")
			}
		}

		override fun toString(): String {
			return "<x=$x, y=$y, z=$z)>"
		}

		fun absoluteValue(): Int {
			return x.absoluteValue + y.absoluteValue + z.absoluteValue
		}


	}

	class Moon(var position: MyVec,
			   var speed: MyVec) {
		fun update() {
			position += speed
		}

		fun potentialEnergy() : Int {
			return position.absoluteValue()
		}

		fun kineticEnergy() : Int {
			return speed.absoluteValue()
		}

		fun totalEnergy() : Int {
			return potentialEnergy() * kineticEnergy()
		}

		override fun toString(): String {
			return "Moon(position=$position, speed=$speed)"
		}
	}

	class System(val moons: List<Moon>) {

		fun pairs() : Sequence<Pair<Moon, Moon>> {
			return sequence {
				for (i in 0 until moons.size) {
					for (j in i + 1 until moons.size) {
						yield(Pair(moons[i], moons[j]))
					}
				}
			}
		}

		fun step() {
			pairs().forEach {
				for (i in 0..2) {
					it.first.speed[i] += Integer.compare(it.second.position[i], it.first.position[i])
					it.second.speed[i] += Integer.compare(it.first.position[i], it.second.position[i])
				}
			}
			moons.forEach { it.update() }
		}

		fun totalEnergy() : Int {
			return moons.map { it.totalEnergy() }.sum()
		}

		fun print() {
			for (moon in moons) {
				println(moon)
			}
		}


	}

	override fun run(vararg args: String?) {
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay12::class.java.getResourceAsStream(args[0]!!)))
		val moons = ArrayList<Moon>()

		bufferedReader.useLines {
			val regex = Regex(pattern = """-?\d+""")
			it.forEach { line ->
				val matchResult = regex.findAll(line).iterator()
				moons.add(Moon(MyVec(matchResult.next().value.toInt(),
						             matchResult.next().value.toInt(),
						             matchResult.next().value.toInt()),
						       MyVec()))

			}
		}
		val system = System(moons)
		println("After 0 steps:")
		system.print()
		(0 until 1000).forEach {
			system.step()
			println("After ${it+1} steps:")
			system.print()
		}
		println("Sum of total energy: ${system.totalEnergy()} ..")
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay12>(*args)
}
