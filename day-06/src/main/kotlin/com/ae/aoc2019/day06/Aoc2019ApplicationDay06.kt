package com.ae.aoc2019.day06

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.util.*

@SpringBootApplication
class Aoc2019ApplicationDay06 : CommandLineRunner {

	private fun countParents(identiefier: String, fp: HashMap<String, ArrayList<String>>) : Int {
		val seen = HashSet<String>()
		val toDo = Stack<String>()
		var nOrbits = 0
		toDo.push(identiefier)
		while (!toDo.empty()) {
			val currentId = toDo.pop()
			val orbits = fp.getOrDefault(currentId, ArrayList())
			seen.add(currentId)
			nOrbits += orbits.size
			orbits.filter { !seen.contains(it) }.forEach { toDo.push(it) }

		}
		return nOrbits
	}

    private fun findNrOfOrbits(source: String,
                               targets: Set<String>,
                               fp: HashMap<String, ArrayList<String>>) : Int {

        val seen = HashSet<String>()

        val toDo = Stack<Pair<String, Int>>()
        toDo.push(Pair(source, 0))
        while (!toDo.isEmpty()) {
            val currentId = toDo.peek().first
            val currentDistance = toDo.peek().second
            toDo.pop()
            val orbits = fp.getOrDefault(currentId, ArrayList())
            seen.add(currentId)
            orbits.filter { !seen.contains(it) }
                  .forEach {
                      if (targets.contains(it))
                          return currentDistance
                      else
                          toDo.push(Pair(it, currentDistance + 1))
                  }
        }
        throw IllegalStateException("Graph was assumed to be connected ..")
    }

	override fun run(vararg args: String?) {
        val nodes = HashSet<String>()
        val orbitsDirected = HashMap<String, ArrayList<String>>()
        val orbitsUnDirected = HashMap<String, ArrayList<String>>()
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay06::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				line.split(')').forEach { nodes.add(it) }
				val pair = line.split(')').zipWithNext().map { Pair(it.second, it.first) }.first()
				if (!orbitsDirected.containsKey(pair.first)) {  orbitsDirected[pair.first] = ArrayList() }
                if (!orbitsUnDirected.containsKey(pair.first)) {  orbitsUnDirected[pair.first] = ArrayList()  }
                if (!orbitsUnDirected.containsKey(pair.second)) { orbitsUnDirected[pair.second] = ArrayList() }
				orbitsDirected[pair.first]!!.add(pair.second)
                orbitsUnDirected[pair.first]!!.add(pair.second)
                orbitsUnDirected[pair.second]!!.add(pair.first)
			}
		}
		System.out.println("Found ${nodes.map { countParents(it, orbitsDirected) }.sum()} direct and indirect orbits")

        val targets =  orbitsDirected["SAN"]!!.toSet()
        System.out.println("Minimum number of orbital transfers required: ${findNrOfOrbits("YOU", targets, orbitsUnDirected)}")

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay06>(*args)
}
