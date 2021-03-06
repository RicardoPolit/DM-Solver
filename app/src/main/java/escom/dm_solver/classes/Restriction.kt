package escom.dm_solver.classes

import android.util.Log
import kotlin.collections.ArrayList

class Restriction {

    var coeficientes = ArrayList<Fraction>()
    var variables = ArrayList<String>()
    var operator = MAYOR_IGUAL
    var result = Fraction(0,1)

    fun addTermino(s:String){

        val coef = s.substring(0..s.length-2)
        val variable = s.substring(s.length-1)
        var j=0

        for(varx in variables){
            if( varx.compareTo(variable) > 0 ) { break }
            j++
        }

        if(coef.isBlank() || coef.equals("+"))
            coeficientes.add(j,Fraction(1, 1))
        else if (coef.equals("-"))
            coeficientes.add(j,Fraction(-1,1))
        else
            coeficientes.add(j,Fraction(coef))

        variables.add(j,variable)
    }

    companion object { // STATIC METHODS / VARIABLES

        val MAYOR_IGUAL = 0
        val MENOR_IGUAL = 1
        val IGUAL = 2

        fun createRestriction(s:String):Restriction?{

            val regex = """([+|-]?[0-9]*|([0-9]+/[0-9]+))[a-y]((\+|-)([0-9]*|([0-9]+/[0-9]+))[a-y])*((<=)|(>=)|=)[+|-]?[0-9]+(/[0-9]+)?""".toRegex()

            if( regex.matches(s) ){

                val r = Restriction()
                var termino = """([+|-]?[0-9]*|([0-9]+/[0-9]+))[a-y]""".toRegex().find(s)?.value
                r.addTermino(termino.toString())

                var inputx = s.substring(termino.toString().length)
                val found = """(\+|-)([0-9]*|([0-9]+/[0-9]+))[a-y]""".toRegex().findAll(inputx)
                found.forEach { f -> r.addTermino( f.value ) }

                if(found.count()>0)
                    inputx = inputx.substring( found.last().range.last+1 )

                var op = """(<=)|(>=)|=""".toRegex().find(inputx)?.value
                r.operator = if(op.equals("<=")) MENOR_IGUAL
                        else if(op.equals(">=")) MAYOR_IGUAL
                        else IGUAL

                inputx = inputx.substring(op.toString().length)
                r.result = Fraction( inputx )

                return r
            }
            else
                return null
        }
    }

    fun varExists(s: String):Boolean{
        var existe = false

        for( v in variables )
            if( v.equals(s) ){
                existe = true
            }
        return existe
    }

    /* Calcula el valor de la variable = variables[posicion]
     * Considera las demas variables a 0, y despeja
    */
    fun findValue(posicion:Int):Double{
        val ret = result.toDouble() / coeficientes[posicion].toDouble()
        return ret
    }

    /* Evalua si la funcion es cumplida o no */
    fun eval(values :ArrayList<Double>):Boolean{
        var ac = 0.0
        for(i in 0 until coeficientes.size){
            ac += coeficientes[i].toDouble()*values[i]
        }

        if(operator == MAYOR_IGUAL)
            return ac >= result.toDouble()
        else if(operator == MENOR_IGUAL)
            return ac <= result.toDouble()
        else
            return ac == result.toDouble()
    }

    fun eval(values :ArrayList<Fraction>):Fraction{
        // TODO: Implementar metodo para fracciones
        return Fraction(1,1)
    }

    override fun toString():String{
        var aux = ""

        if(coeficientes.size == variables.size) {
            for (i in 0..coeficientes.size - 1)
                if (coeficientes[i].num != 0)
                    aux += "${coeficientes[i]}<b>${variables[i]}</b> "
        }
        else return ""

        if(aux.startsWith("+ "))
            aux = aux.substring(2)
        else
            aux = "-"+aux.substring(2)

        when(operator){
            MAYOR_IGUAL -> aux += "&geq "
            MENOR_IGUAL -> aux += "&leq "
            IGUAL -> aux += "= "
        }

        aux += if(result.num>0)
                result.toString().substring(2)
            else
                "-"+result.toString().substring(2)

        return aux
    }
}