package com.example.calculatorapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Scriptable

class CalculatorViewModel  : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equationText.value?.let {
            if (btn == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }

            if (btn == "DEL") {
                if (it.isNotEmpty()) {
                    _equationText.value = it.substring(0, it.length - 1)

                    // Recalculate result if equation is not empty
                    _resultText.value = if (_equationText.value!!.isNotEmpty()) {
                        try {
                            calculateResult(_equationText.value!!)
                        } catch (e: Exception) {
                            "Error"
                        }
                    } else {
                        "0" // Reset result if equation is empty
                    }
                }
                return
            }

            if (btn == "=") {
                _equationText.value = _resultText.value
                return
            }

            _equationText.value = it + btn

            // Calculate Result
            try {
                _resultText.value = calculateResult(_equationText.value.toString())
            } catch (_: Exception) {
            }


        }
    }


    private fun calculateResult(equation: String): String {
        val context: org.mozilla.javascript.Context = org.mozilla.javascript.Context.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()
        var finalResult =
            context.evaluateString(scriptable, equation, "Javascript", 1, null).toString()
        if (finalResult.endsWith("0")) {
            finalResult = finalResult.replace(".0", "")
        }
        return finalResult
    }
}


