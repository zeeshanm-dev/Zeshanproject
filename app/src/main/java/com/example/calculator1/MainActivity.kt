package com.example.calculator1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var text: EditText
    private lateinit var name: String
    private lateinit var resultdisply: EditText
    private val RECORD_AUDIO_PERMISSION_CODE = 100
    private var currentnumber = ""
    private var previousnumber = ""
    private var operator = ""
    private var isnewinput = true
    private val calculationHistory = mutableListOf<String>()
    private val speechRecognizer = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val spokenText = data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.get(0)
            if (spokenText != null) {
                processVoiceCommand(spokenText)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)
        resultdisply = findViewById(R.id.text1)

        setupNumberBotton()
        setupOperatorBotton()
        setupActionBotton()
    }

    private fun showhistry() {
       if (calculationHistory.isEmpty()) {
           androidx.appcompat.app.AlertDialog.Builder(this)
               .setTitle("History")
               .setMessage("No calculations yet")
               .setPositiveButton("OK", null)
               .show()
           return
       }
        val historyArray = calculationHistory.toTypedArray()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("History")
            .setItems(historyArray, null)
            .setPositiveButton("Clear History") { _, _ ->
                calculationHistory.clear()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun setupNumberBotton() {
        val NumberButtons = arrayOf(
            R.id.button16 to "0",
            R.id.button1 to "1",
            R.id.button2 to "2",
            R.id.button3 to "3",
            R.id.button4 to "4",
            R.id.button5 to "5",
            R.id.button6 to "6",
            R.id.button7 to "7",
            R.id.button8 to "8",
            R.id.button9 to "9"
        )
        for ((id, value) in NumberButtons) {
            findViewById<ImageButton>(id).setOnClickListener {
                onNumberClick(value)
            }
        }

        findViewById<ImageButton>(R.id.button18).setOnClickListener {
            if (!currentnumber.contains(".")) {
                currentnumber = if (currentnumber.isEmpty()) "0." else "$currentnumber."
                resultdisply.setText(currentnumber)
            }
        }

    }
    fun onNumberClick(value: String) {
        if (isnewinput) {
            currentnumber = value
            isnewinput = false
        } else {
            currentnumber += value
        }
        if (previousnumber.isNotEmpty()&& currentnumber.isNotEmpty()){
            text.setText("$previousnumber $operator $currentnumber")
        }else{
            text.setText(currentnumber)
        }
        resultdisply.setText("")
    }
        fun setupOperatorBotton() {
            val operatorButtons = arrayOf(
                R.id.button15 to "+",
                R.id.button11 to "-",
                R.id.button14 to "/",
                R.id.buttonx to "*",
                R.id.button13 to "%",
            )
            for ((id, op) in operatorButtons) {
                findViewById<ImageButton>(id).setOnClickListener {
                    onOperatorClick(op)
                }
            }

        }
        fun onOperatorClick(op: String) {
            if (currentnumber.isEmpty() && previousnumber.isEmpty()) return

            if (previousnumber.isNotEmpty() && currentnumber.isNotEmpty()) {
                val num1 = previousnumber.toDouble()
                val num2 = currentnumber.toDouble()
                val result = when (operator) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    "/" -> if (num2 != 0.0) num1 / num2 else 0.0
                    else -> num2
                }
                previousnumber = formatResult(result)
            } else {
                previousnumber = currentnumber
            }
            operator = op
            text.setText("$previousnumber $operator ")
            currentnumber=""
            isnewinput = true
        }

         fun setupActionBotton() {
            findViewById<ImageButton>(R.id.button19).setOnClickListener {
                calculateResult()
                text.setText("")
            }

             findViewById<ImageButton>(R.id.buttonvoice).setOnClickListener {
                 checkPermissionAndStartVoice()
             }
            findViewById<ImageButton>(R.id.buttonc).setOnClickListener {
                clearAll()
            }
             findViewById<ImageButton>(R.id.buttonhistory).setOnClickListener {
                 showhistry()
             }

            findViewById<ImageButton>(R.id.button13).setOnClickListener {
                if (currentnumber.isNotEmpty()) {
                    val value = currentnumber.toDouble() / 100
                    currentnumber = formatResult(value)
                    resultdisply.setText(currentnumber)
                }
            }
            findViewById<ImageButton>(R.id.button12)?.setOnClickListener {
                if (currentnumber.isNotEmpty()) {
                    currentnumber = if (currentnumber.startsWith("-")) {
                        currentnumber.substring(1)
                    } else {
                        "-$currentnumber"
                    }
                    resultdisply.setText(currentnumber)
                }
            }
        }
    private fun checkPermissionAndStartVoice() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        } else {
            startVoiceRecognition()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition()
            } else {
                Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a calculation, e.g. 'five plus three'")
        }
        try {
            speechRecognizer.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_SHORT).show()
        }
    }
    private fun processVoiceCommand(spokenText: String) {
        val cleaned = spokenText.lowercase().trim()

        // Replace spoken words with symbols
        var expression = cleaned
            .replace("plus", "+")
            .replace("add", "+")
            .replace("minus", "-")
            .replace("subtract", "-")
            .replace("times", "*")
            .replace("multiply", "*")
            .replace("multiplied by", "*")
            .replace("divide", "/")
            .replace("divided by", "/")
            .replace("percent", "%")
            .replace("equals", "")
            .replace("equal to", "")
            .replace("what is", "")
            .trim()

        // Extract number, operator, number using regex
        val regex = Regex("(-?\\d+\\.?\\d*)\\s*([+\\-*/%])\\s*(-?\\d+\\.?\\d*)")
        val match = regex.find(expression)

        if (match != null) {
            val (num1, op, num2) = match.destructured

            clearAll()
            currentnumber = num1
            resultdisply.setText(currentnumber)
            isnewinput = false

            onOperatorClick(op)

            currentnumber = num2
            resultdisply.setText(currentnumber)

            calculateResult()
            text.setText("")
        } else {
            Toast.makeText(this, "Couldn't understand: \"$spokenText\"", Toast.LENGTH_SHORT).show()
        }
    }
         fun calculateResult() {
            if (previousnumber.isEmpty() || currentnumber.isEmpty() || operator.isEmpty()) return

            val num1 = previousnumber.toDouble()
            val num2 = currentnumber.toDouble()

            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else {
                    resultdisply.setText("Error")
                    return
                }
                else -> num2
            }

            val formatted = formatResult(result)
             calculationHistory.add(0, "$previousnumber $operator $currentnumber = $formatted")
             text.setText("$previousnumber $operator $currentnumber")
             resultdisply.setText(formatted)
             currentnumber = formatted
            previousnumber = ""
            operator = ""
            isnewinput = true
        }
         fun formatResult(value: Double): String {
            return if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                value.toString()
            }
        }

         fun clearAll() {
            currentnumber = ""
            previousnumber = ""
            operator = ""
            isnewinput = true
            text.setText("")
            resultdisply.setText("0")
        }


}