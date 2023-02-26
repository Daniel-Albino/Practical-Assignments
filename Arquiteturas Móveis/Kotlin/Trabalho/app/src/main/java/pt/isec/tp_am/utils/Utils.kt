package pt.isec.tp_am.utils

import java.lang.Math.*
import kotlin.math.pow

fun calculateExpression(str:String): Double {
    return object : Any() {
        var pos:Int = -1
        var ch :Int = 0
        fun nextChar() {
            ch = if (++pos < str.length) str[pos].code else -1
        }

        fun eat(charToEat: Int): Boolean {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
            return x
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)` | number
        //        | functionName `(` expression `)` | functionName factor
        //        | factor `^` factor
        fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                if (eat('+'.code)) x += parseTerm() // addition
                else if (eat('-'.code)) x -= parseTerm() // subtraction
                else return x
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                if (eat('*'.code)) x *= parseFactor() // multiplication
                else if (eat('/'.code)) x /= parseFactor() // division
                else return x
            }
        }
        fun parseFactor(): Double {
            if (eat('+'.code)) return +parseFactor() // unary plus
            if (eat('-'.code)) return -parseFactor() // unary minus
            var x: Double
            val startPos = pos
            if (eat('('.code)) { // parentheses
                x = parseExpression()
                if (!eat(')'.code)) throw RuntimeException("Missing ')'")
            } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                x = str.substring(startPos, pos).toDouble()
            } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                val func = str.substring(startPos, pos)
                if (eat('('.code)) {
                    x = parseExpression()
                    if (!eat(')'.code)) throw RuntimeException("Missing ')' after argument to $func")
                } else {
                    x = parseFactor()
                }
                x =
                    when (func) {
                        "sqrt" -> sqrt(x)
                        "sin" -> sin(Math.toRadians(x))
                        "cos" -> cos(Math.toRadians(x))
                        "tan" -> tan(Math.toRadians(x))
                        else -> throw RuntimeException("Unknown function: $func")
                    }
            } else {
                throw RuntimeException("Unexpected: " + ch.toChar())
            }
            if (eat('^'.code)) x = x.pow(parseFactor()) // exponentiation
            return x
        }
    }.parse()
}

fun prepareStringOperation(str : String) : String{
    if (str == null || str.contains(" ") || str.isBlank() || str.length <= 2)
        return "NA"
    var aux = listOf<String>()
    if(str.contains("÷") || str.contains("x")){
        for(i in str.indices){
            if (str[i] == '÷') {
                aux += "/"

            }else if(str[i] == 'x') {
                aux += "*"
            }else
                aux += str[i].toString()
        }
    } else
        return str
    var auxS : String = ""
    for(i in aux.indices){
        auxS += aux.get(i).toString()
    }
    if (auxS == "")
        return "NA"
    return auxS
}