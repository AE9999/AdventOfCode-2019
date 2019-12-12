package com.ae.aoc2019.day12

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.min

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

		fun min() : Int {
			return Math.min(Math.min(x,y), z)
		}

		fun max() : Int {
			return Math.max(Math.max(x,y), z)
		}

		fun aligned(other: MyVec): Boolean {
			(0..2).forEach {
				if (other[it] == 0 && this[it] == 0) {}
				else if (other[it] != 0 && this[it] == 0) return false
				else if (other[it] == 0 && this[it] != 0) return false
				else if (other[it]  > 0 !=  this[it] > 0) return false
			}
			return true
		}

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as MyVec

			if (x != other.x) return false
			if (y != other.y) return false
			if (z != other.z) return false

			return true
		}

		operator fun plus(other: MyVec): MyVec {
			return MyVec(x + other.x, y + other.y, z + other.z)
		}
	}

	class Moon(var position: MyVec,
			   var speed: MyVec) {
		fun update() {
			position = position.plus(speed)
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
			return indexPairs().map { Pair(moons[it.first], moons[it.second]) }
		}

		fun indexPairs() : Sequence<Pair<Int, Int>> {
			return sequence {
				for (i in 0 until moons.size) {
					for (j in i + 1 until moons.size) {
						yield(Pair(i, j))
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

		fun extrapolatedDiff() : Boolean {
			data class DiffEntry(val diff: MyVec, val duration: Int)

			val diffEntries : Array<DiffEntry?> = arrayOfNulls(moons.size)
			indexPairs().forEach {
				val left = moons[it.first]
				val right = moons[it.second]
				val diffLeft = MyVec()
				val durationLeft =  MyVec()
				val diffRight =  MyVec()
				val durationRight =  MyVec()
				for (i in 0..2) {
					diffLeft[i] = Integer.compare(right.position[i], left.position[i])
					durationLeft[i] = (right.position[i] - left.position[i]).absoluteValue
					diffRight[i] = Integer.compare(left.position[i], right.position[i])
					durationRight[i] = (right.position[i] - left.position[i]).absoluteValue
				}
				if (diffEntries[it.first] == null) {
					diffEntries[it.first] = DiffEntry(diffLeft, durationLeft.min())
				}  else {
					val diffEntry = diffEntries[it.first]!!
					if (!diffEntry.diff.aligned(diffLeft)) {
						return false
					}
					diffEntries[it.first] = DiffEntry(diffLeft + diffEntry.diff, min(diffEntry.duration, durationLeft.min()))
				}
				if (diffEntries[it.second] == null) {
					diffEntries[it.second] = DiffEntry(diffRight, durationRight.min())
				} else {
					val diffEntry = diffEntries[it.second]!!
					if (!diffEntry.diff.aligned(diffRight)) {
						return false
					}
					diffEntries[it.second] = DiffEntry(diffRight+ diffEntry.diff, min(diffEntry.duration, durationRight.min()) )
				}
			}
			val vectors = diffEntries.map { it!!.diff }
			val duration  =  diffEntries.map { it!!.duration }.min()!!
			for (i in moons.size) {
				moons[i]
			}

			return true
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

	fun part1(vararg args: String?) {
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

	fun part2(vararg args: String?) {
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
		system.step()
		while() {
			if (!system.extrapolatedDiff()) system.step()
		}

	}

	override fun run(vararg args: String?) {
		part1(*args)
		part2(*args)

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay12>(*args)
}
