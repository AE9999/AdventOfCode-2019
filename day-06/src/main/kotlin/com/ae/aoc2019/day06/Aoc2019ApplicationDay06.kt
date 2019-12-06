package com.ae.aoc2019.day06

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.RuntimeException
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
        var optimalPath = Int.MAX_VALUE
        val seen = HashSet<String>()
        val toDo = Stack<Pair<String, Int>>()
        while (!toDo.isEmpty()) {
            val currentId = toDo.peek().first
            val currentDistance = toDo.peek().second
            toDo.pop()
            val orbits = fp.getOrDefault(currentId, ArrayList())
            orbits.filter { !seen.contains(it) }.forEach { toDo.push(it) }
        }
        return optimalPath
    }

	override fun run(vararg args: String?) {
        val nodes = HashSet<String>()
        val fp = HashMap<String, ArrayList<String>>()
		val bufferedReader = BufferedReader(InputStreamReader(Aoc2019ApplicationDay06::class.java.getResourceAsStream(args[0]!!)))
		bufferedReader.useLines {
			it.forEach { line ->
				line.split(')').forEach { nodes.add(it) }
				val pair = line.split(')').zipWithNext().map { Pair(it.second, it.first) }.first()
				if (!fp.containsKey(pair.first)) {
					fp[pair.first] = ArrayList()
				}
				fp[pair.first]!!.add(pair.second)
			}
		}
		System.out.println("Found ${nodes.map { countParents(it, fp) }.sum()} direct and indirect orbits")

        val targets =  fp["SAN"]!!

	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2019ApplicationDay06>(*args)
}
