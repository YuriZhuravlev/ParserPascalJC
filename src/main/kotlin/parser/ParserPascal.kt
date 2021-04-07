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
        const val TRUE = "true"
        const val FALSE = "false"
        val setSeparator = setOf(' ', '\n', '\r', '\t')
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

    private fun getToken(skipSeparators: Boolean = false): Char {
        if (skipSeparators && setSeparator.contains(getToken())) {
            nextTokenAndSkipSeparator()
        }
        return text[mIndex]
    }

    private fun setIndex(new: Int): Boolean {
        mIndex = new
        return true
    }

    private fun terminal(word: String, skipSeparators: Boolean = false): Boolean {
        if (skipSeparators) {
            getToken(true)
        }
        word.forEach {
            if (it.equals(getToken(), true) && nextToken()) {
                // next token
            } else {
                return false
            }
        }
        println("found terminate $word | $mIndex")
        return true
    }

    suspend fun parse(): Boolean {
        println("______________\n_____________\n")
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
        // <программа> ::= <заголовок программы> ; <блок> .
        if (head()
            && getToken(true) == ';'
            && nextToken()
            && block()
            && getToken(true) == '.'
        ) {
            println("found program | $mIndex")
            return empty()
        }
        return false
    }

    private fun head(): Boolean {
        // <заголовок программы> ::= program <идентификатор>
        if (terminal(PROGRAM, true)
            && nextTokenAndSkipSeparator()
            && identifier()
        ) {
            println("found head | $mIndex")
            return true
        }
        return false
    }

    private fun block(): Boolean {
        // <блок> ::= <раздел описаний> <раздел операторов> | <раздел операторов>
        val index = mIndex
        if ((sectionDescription() && sectionOperator())
            || (setIndex(index) && sectionOperator())
        ) {
            println("found block | $mIndex")
            return true
        }
        return false
    }

    private fun sectionDescription(): Boolean {
        // <раздел описаний> ::= <раздел констант> <раздел переменных> | <раздел переменных> <раздел констант> | <раздел констант> | <раздел переменных>
        val index = mIndex
        if ((sectionConsts() && sectionVars())
            || (setIndex(index) && sectionVars() && sectionConsts())
            || (setIndex(index) && sectionConsts())
            || (setIndex(index) && sectionVars())
        ) {
            println("found sectionDescription | $mIndex")
            return true
        }
        return false
    }

    private fun sectionConsts(): Boolean {
        // <раздел констант> ::= const <описание константы> ; {<описание константы> ; }
        if (terminal(CONST, true)
            && nextTokenAndSkipSeparator()
            && descriptionConst()
            && getToken(true) == ';'
            && nextToken()
        ) {
            var index = mIndex
            while (descriptionConst() && getToken(true) == ';' && nextToken()) {
                index = mIndex
            }
            mIndex = index
            println("found sectionConsts | $mIndex")
            return true
        }
        return false
    }

    private fun descriptionConst(): Boolean {
        // <описание константы> ::= <идентификатор> = <выражение>
        if (identifier()
            && getToken(true) == '='
            && nextToken()
            && expression()
        ) {
            println("found descriptionConst | $mIndex")
            return true
        }
        return false
    }

    private fun sectionVars(): Boolean {
        // <раздел переменных> ::= var <описание переменных> ; {<описание переменных>;}
        if (terminal(VAR, true)
            && nextTokenAndSkipSeparator()
            && descriptionVars()
            && getToken(true) == ';'
            && nextToken()
        ) {
            var index = mIndex
            while (descriptionVars() && getToken(true) == ';' && nextToken()) {
                index = mIndex
            }
            mIndex = index
            println("found sectionVars | $mIndex")
            return true
        }
        return false
    }

    private fun descriptionVars(): Boolean {
        // <описание переменных> ::= <список имен переменных> : <тип>
        if (listVarNames() && getToken(true) == ':' && nextToken() && type()) {
            println("found descriptionVars | $mIndex")
            return true
        }
        return false
    }

    private fun listVarNames(): Boolean {
        // <список имен переменных> ::= <идентификатор> | <список имен переменных> , <идентификатор>
        if (identifier()) {
            var index = mIndex
            while (getToken(true) == ',' && nextToken() && identifier()) {
                index = mIndex
            }
            mIndex = index
            println("found listVarNames | $mIndex")
            return true
        }
        return false
    }

    private fun sectionOperator(): Boolean {
        //<раздел операторов> ::= begin <список операторов> end | begin end
        if (terminal(BEGIN, true)
            && nextTokenAndSkipSeparator()
        ) {
            val index = mIndex
            if ((listOperator() && terminal(END, true))
                || (setIndex(index) && terminal(END))
            ) {
                println("found sectionOperator | $mIndex")
                return true
            }
        }
        return false
    }

    private fun operator(): Boolean {
        // <оператор> ::= <ввод/вывод> | <оператор выбора> | <оператор цикла> | <составной оператор> | <оператор присваивания>
        val index = mIndex
        if (inputOutput()
            || (setIndex(index) && operatorSelect())
            || (setIndex(index) && operatorLoop())
            || (setIndex(index) && operatorCompound())
            || (setIndex(index) && operatorAssignment())
        ) {
            println("found operator | $mIndex")
            return true
        }
        return false
    }

    private fun inputOutput(): Boolean {
        // <ввод/вывод> ::= <оператор ввода/вывода> ”(“ <выражение> “)” | <оператор ввода/вывода> “()” | <оператор ввода/вывода>
        if (operatorInputOutput()) {
            if (getToken(true) == '(' && nextToken()) {
                val index = mIndex
                if ((getToken(true) == ')' && nextToken())
                    || (setIndex(index) && expression() && getToken(true) == ')' && nextToken())) {
                    println("found inputOutput | $mIndex")
                    return true
                }
                return false
            }
            println("found inputOutput | $mIndex")
            return true
        }
        return false
    }

    private fun operatorInputOutput(): Boolean {
        // <оператор ввода/вывода> ::= write | read
        getToken(true)
        val index = mIndex
        if (terminal(WRITE)
            || (setIndex(index) && terminal(READ))
        ) {
            println("found operatorInputOutput | $mIndex")
            return true
        }
        return false
    }

    private fun operatorSelect(): Boolean {
        // <оператор выбора> ::= if <логическое выражение> then <оператор> | if <логическое выражение> then <оператор> else <оператор>
        if (terminal(IF, true)
            && booleanExpression()
            && terminal(THEN, true)
            && operator()
        ) {
            var index = mIndex
            if (terminal(ELSE, true) && nextTokenAndSkipSeparator() && operator()) {
                index = mIndex
            }
            mIndex = index
            println("found operatorSelect | $mIndex")
            return true
        }
        return false
    }

    private fun operatorLoop(): Boolean {
        // <оператор цикла> ::= for <идентификатор> := <выражение> to <выражение> do <оператор> | for <идентификатор> := <выражение> downto <выражение> do <оператор>
        if (terminal(FOR, true)
            && identifier()
            && terminal(":=", true)
            && expression()
        ) {
            getToken(true)
            val index = mIndex
            if ((terminal(TO) || (setIndex(index) && terminal(DOWN_TO)))
                && expression()
                && terminal(DO, true)
                && operator()
            ) {
                println("found operatorLoop | $mIndex")
                return true
            }
        }
        return false
    }

    private fun operatorCompound(): Boolean {
        // <составной оператор> ::= begin <список операторов> end | begin end
        if (terminal(BEGIN, true)
            && nextTokenAndSkipSeparator()
        ) {
            val index = mIndex
            if ((listOperator() && terminal(END, true))
                || (setIndex(index) && terminal(END))
            ) {
                println("found operatorCompound | $mIndex")
                return true
            }
        }
        return false
    }

    private fun listOperator(): Boolean {
        // <список операторов> ::= <оператор> |  <оператор> ; | <оператор> ; <список операторов>
        if (operator()) {
            var index = mIndex
            var fl = true
            while (fl && getToken(true) == ';' && nextToken()) {
                index = mIndex
                if (operator()) {
                    index = mIndex
                } else {
                    fl = false
                }
            }
            mIndex = index
            println("found listOperator | $mIndex")
            return true
        }
        return false
    }

    private fun operatorAssignment(): Boolean {
        // <оператор присваивания> ::= <переменная> := <выражение> | <переменная> := @ <идентификатор>
        if (variable()
            && terminal(":=", true)
            && ((getToken(true) == '@' && nextToken() && identifier()) || expression())
        ) {
            println("found operatorAssignment | $mIndex")
            return true
        }
        return false
    }

    private fun expression(): Boolean {
        //<выражение> ::= <арифметическое выражение> | <логическое выражение>
        val index = mIndex
        if (arithmeticExpression()
            || (setIndex(index) && booleanExpression())) {
            println("found expression | $mIndex")
            return true
        }
        return false
    }

    private fun booleanExpression(): Boolean {
        //<логическое выражение> ::= <простое логическое выражение> | <отношение>
        val index = mIndex
        if (simpleBooleanExpression()
            || (setIndex(index) && relationship())) {
            println("found booleanExpression | $mIndex")
            return true
        }
        return false
    }

    private fun simpleBooleanExpression(): Boolean {
        //<простое логическое выражение> ::= < логическое слагаемое > | <простое логическое выражение> or <логическое слагаемое>
        if (booleanTerm()) {
            var index = mIndex
            if (terminal(OR, true) && simpleBooleanExpression()) {
                index = mIndex
            }
            mIndex = index
            println("found simpleBooleanExpression | $mIndex")
            return true
        }
        return false
    }

    private fun booleanTerm(): Boolean {
        //<логическое слагаемое> ::= <логический множитель> | <логическое слагаемое> and <логический множитель>
        if (booleanFactor()) {
            var index = mIndex
            if (terminal(AND, true) && booleanTerm()) {
                index = mIndex
            }
            mIndex = index
            println("found booleanTerm | $mIndex")
            return true
        }
        return false
    }

    private fun booleanFactor(): Boolean {
        //<логический множитель> ::= <логическая константа> | <переменная> | not <логический множитель> | “(“ <логическое выражение> “)”
        getToken(true)
        val index = mIndex
        if (booleanConstant()
            || (setIndex(index) && terminal(NOT) && booleanFactor())
            || (setIndex(index) && variable())
            || (setIndex(index) && getToken(true) == '(' && nextToken() && booleanExpression() && getToken(true) == ')' && nextToken())
        ) {
            println("found booleanFactor | $mIndex")
            return true
        }
        return false
    }

    private fun relationship(): Boolean {
        //<отношение> ::= <арифметическое выражение> <операция сравнения><арифметическое выражение> | <простое логическое выражение> <операция сравнения> <простое логическое выражение>
        val index = mIndex
        if ((arithmeticExpression() && relationshipOperations() && arithmeticExpression())
            || (setIndex(index) && booleanExpression() && relationshipOperations() && booleanExpression())
        ) {
            println("found relationship | $mIndex")
            return true
        }
        return false
    }

    private fun arithmeticExpression(): Boolean {
        //<арифметическое выражение> ::= <слагаемое> { <операции сложения> <слагаемое> } | <операция сложения> <слагаемое> { <операция сложения> <слагаемое>}
        var index = mIndex
        if (additionOperations()) {
            index = mIndex
        } else {
            mIndex = index
        }
        if (term()) {
            index = mIndex
            while (additionOperations() && term()) {
                index = mIndex
            }
            mIndex = index
            println("found arithmeticExpression | $mIndex")
            return true
        }
        return false
    }

    private fun term(): Boolean {
        //<слагаемое> ::= <множитель> { <операция умножения> <множитель> }
        if (factor()) {
            var index = mIndex
            while (multiplicationOperations() && factor()) {
                index = mIndex
            }
            mIndex = index
            println("found term | $mIndex")
            return true
        }
        return false
    }

    private fun factor(): Boolean {
        //<множитель> ::= <целое число> | <переменная> | “(“ <арифметическое выражение> “)”
        getToken(true)
        val index = mIndex
        if (integer()
            || (setIndex(index) && variable())
            || (setIndex(index) && getToken(true) == '(' && nextToken() && arithmeticExpression() && getToken(true) == ')' && nextToken())
        ) {
            println("found factor | $mIndex")
            return true
        }
        return false
    }

    private fun variable(): Boolean {
        // <переменная> ::= <идентификатор>(^)
        if (identifier()) {
            if (getToken(true) == '^') {
                nextToken()
            }
            println("found variable | $mIndex")
            return true
        }
        return false
    }

    private fun integer(): Boolean {
        // <целое число> ::= <цифра> | - <цифра> | + <цифра> | <целое число><цифра>
        val firstToken = getToken(true)
        var isNumber = false
        if (firstToken == '-' || firstToken == '+') {
            nextToken()
        }
        while (getToken().isDigit() && nextToken()) {
            isNumber = true
        }
        if (isNumber) { println("found integer | $mIndex") }
        return isNumber
    }

    private fun additionOperations(): Boolean {
        // <операция сложения> ::= + | –
        getToken(true)
        val index = mIndex
        if ((getToken() == '+' && nextToken())
            || (setIndex(index) && (getToken() == '-' && nextToken()))
        ) {
            return true
        }
        return false
    }

    private fun multiplicationOperations(): Boolean {
        // <операция умножения> ::= * | div
        getToken(true)
        val index = mIndex
        if ((getToken() == '*' && nextToken())
            || (setIndex(index) && terminal(DIV))
        ) {
            return true
        }
        return false
    }

    private fun booleanConstant(): Boolean {
        // <логическая константа> ::= true | false
        getToken(true)
        val index = mIndex
        if (terminal(TRUE)
            || (setIndex(index) && terminal(FALSE))
        ) {
            return true
        }
        return false
    }

    private fun relationshipOperations(): Boolean {
        //<операция сравнения> ::= <> | <= | < | = | >= | >
        getToken(true)
        val index = mIndex
        setRelationshipOperations.forEach {
            if (setIndex(index) && terminal(it)) {
                return true
            }
        }
        return false
    }

    private fun identifier(): Boolean {
        if (getToken(true).isLetter() && nextToken()) {
            while (getToken().isLetterOrDigit() && nextToken()) {
                // next token body
            }
            println("found identifier | $mIndex")
            return true
        }
        return false
    }

    private fun type(): Boolean {
        // <тип> ::= (^) integer | (^) boolean
        if (getToken(true) == '^') {
            nextToken()
        }
        val index = mIndex
        if (terminal(INTEGER)
            || (setIndex(index) && terminal(BOOLEAN))
        ) {
            println("found type | $mIndex")
            return true
        }
        return false
    }

    private fun empty(): Boolean {
        if (mIndex == text.lastIndex) {
            println("found empty | $mIndex")
            return true
        }
        nextTokenAndSkipSeparator()
        return setSeparator.contains(getToken(true))
    }
}