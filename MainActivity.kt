package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation=false
    private var canAddDecimal=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    workingsTV.append(view.text)
                    canAddDecimal = false
                }
            } else {
                workingsTV.append(view.text)
                canAddOperation = true
            }
        }
    }
    fun operationAction(view: View) {
        if(view is Button && canAddOperation)
            workingsTV.append(view.text)
            canAddOperation=false
            canAddDecimal=true
    }
    fun allclearaction(view: View) {
        workingsTV.text=""
        resultsTV.text=""
    }
    fun backspaceaction(view: View) {
        val length=workingsTV.length()
        if(length>0){
            workingsTV.text=workingsTV.text.subSequence(0,length-1)
        }
    }
    fun equalaction(view: View) {
        resultsTV.text=calculateResults()
    }

    private fun calculateResults(): String {
        val digitOperators=digitOperators()
        if(digitOperators.isEmpty()){
            return ""
        }
        val timesdivision=timesdivisionCalculate(digitOperators)
        if(timesdivision.isEmpty())return ""
        val result=addsub(timesdivision)
        return result.toString()
    }

    private fun addsub(passedlist: MutableList<Any>): Float {
        var result=passedlist[0] as Float
        for(i in passedlist.indices){
            if(passedlist[i] is Char && i!=passedlist.lastIndex){
                val operator=passedlist[i]
                var nextdig=passedlist[i+1]as Float
                if(operator=='+'){
                    result+=nextdig
                }
                if(operator=='-'){
                    result-=nextdig
                }
            }
        }
        return result
    }

    private fun timesdivisionCalculate(passedlist: MutableList<Any>): MutableList<Any> {
        var list=passedlist
        if(list.contains('x') || list.contains('/')){
            list=calcTimesdiv(list)
        }
        return list
    }

    private fun calcTimesdiv(passedlist: MutableList<Any>): MutableList<Any> {
        val newlist= mutableListOf<Any>()
        var restartindex=passedlist.size
        for(i in passedlist.indices){
            if(passedlist[i] is Char && i!=passedlist.lastIndex && i<restartindex){
                val operator=passedlist[i]
                val prevDigit=passedlist[i-1] as Float
                val nextDigit=passedlist[i+1] as Float
                when(operator){
                    'x'-> {
                        newlist.add(prevDigit * nextDigit)
                        restartindex = i + 1
                    }
                    '/'-> {
                        newlist.add(prevDigit / nextDigit)
                        restartindex = i + 1
                    }
                    else->{
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }
            }
            if(i>restartindex){
                newlist.add(passedlist[i])

            }
        }
        return newlist;
    }

    private fun digitOperators():MutableList<Any>{
        val list= mutableListOf<Any>()
        var currDigit=""
        for(character in workingsTV.text){
            if(character.isDigit() || character=='.'){
                currDigit+=character
            }
            else{
                list.add(currDigit.toFloat())
                currDigit=""
                list.add(character)
            }
        }
        if(currDigit!=""){
            list.add(currDigit.toFloat())
        }
        return list
    }
}