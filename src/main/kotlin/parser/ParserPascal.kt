package parser

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ParserPascal(val text: String) {
    private var mResult: Boolean? = null
    private var mIndex = 0

    companion object {
        //^, @, ;, a, b, …, z, A, B,... Z, 0, 1, …,9, :=, ., ”,”, (, )

        const val PROGRAM = "program"
        const val VAR = "var"
        const val CONST = "const"
        const val BEGIN = "begin"
        const val END = "end"
        const val WRITE = "write"
        const val READ = "read"
        const val IF = "if"
        const val FOR = "for"
        const val INTEGER = "integer"
        const val BOOLEAN = "boolean"
        const val DIV = "div"
        const val THEN = "then"
        const val ELSE = "else"
        const val DO = "do"
        const val TO = "to"
        const val DOWN_TO = "downto"
        const val OR = "or"
        const val AND = "and"
        const val NOT = "not"
        val setSeparator = setOf(' ', '\n', '\r')
        val setArithmeticOperations = setOf("+", "-", "*", DIV)
        val setRelationshipOperations = setOf("=", "<", ">", "<=", ">=", "<>")
    }

    private fun nextToken(): Boolean {
        if (mIndex < text.lastIndex) {
            mIndex++
            return true
        }
        return false
    }

    private fun nextTokenAndSkipSeparator(): Boolean {
        val next = if (mIndex < text.lastIndex) {
            mIndex++
            true
        } else {
            false
        }
        while (mIndex < text.lastIndex && setSeparator.contains(text[mIndex])) {
            mIndex++
        }
        return next
    }

    private fun getToken(): Char {
        return text[mIndex]
    }

    private fun terminal(word: String): Boolean {
        word.forEach {
            if (it.equals(getToken(), true)) {
                nextToken()
            } else {
                return false
            }
        }
        return true
    }

    suspend fun parse(): Boolean {
        return suspendCoroutine { continuation ->
            if (mResult != null) {
                continuation.resume(mResult!!)
            } else {
                mResult = program()
                continuation.resume(mResult!!)
            }
        }
    }

    private fun program(): Boolean {
        if (head()) {

        }
        return false
    }

    private fun head(): Boolean {
        return false
    }
}