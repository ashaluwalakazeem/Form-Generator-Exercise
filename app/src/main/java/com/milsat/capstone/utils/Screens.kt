package com.milsat.capstone.utils

import androidx.compose.runtime.Immutable

typealias ArgumentKeyName = String
typealias ArgumentKeyValue = Any

@Immutable
enum class Screens(
    private val obligatoryArgs: List<String>? = null,
    private val optionalArgs: List<String>? = null
) {
    Home;

    operator fun invoke(): String {
        val argList = StringBuilder()
        obligatoryArgs?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        optionalArgs?.let { opArgs ->
            opArgs.forEachIndexed { index: Int, s: String ->
                if(index == 0) argList.append("?$s={$s}")
                if(index != 0) argList.append("&$s={$s}")
            }
        }
        return name + argList
    }

    fun withArgs(obligatoryParams: List<Any>? = null, optionalParams: List<Pair<ArgumentKeyName, ArgumentKeyValue>>? = null): String {
        val destination = StringBuilder()
        obligatoryParams?.forEach { arg -> destination.append("/$arg") }
        optionalParams?.let { opArgs ->
            opArgs.forEachIndexed { index: Int, s: Pair<ArgumentKeyName, Any> ->
                if(index == 0) destination.append("?${s.first}=${s.second}")
                if(index != 0) destination.append("&${s.first}=${s.second}")
            }
        }
        return name + destination
    }
}