package com.n8.hellorainbowhat

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import com.google.android.things.contrib.driver.apa102.Apa102

sealed class Output(val color: Int, val frequency:Double)
object RED : Output(Color.RED, 261.626)
object GREEN : Output(Color.GREEN, 293.665)
object BLUE : Output(Color.BLUE, 329.628)

class MainActivity : Activity() {

    val buttonA:Button by lazy { RainbowHat.openButtonA() }
    val buttonB:Button by lazy { RainbowHat.openButtonB() }
    val buttonC:Button by lazy { RainbowHat.openButtonC() }

    val ledRed:Gpio by lazy { RainbowHat.openLedRed() }
    val ledGreen:Gpio by lazy { RainbowHat.openLedGreen() }
    val ledBlue:Gpio by lazy { RainbowHat.openLedBlue() }

    val speaker:Speaker by lazy { RainbowHat.openPiezo() }

    val ledstrip:Apa102 by lazy {
        RainbowHat.openLedStrip().apply {
            brightness = 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // turn off all RGB leds
        clearRGBLEDs()

        // turn off the LED strip
        clearLedStrip()

        speaker.stop()

        fun handleButtonEvent(pressed:Boolean, output: Output) {
            when(pressed){
                true -> handleButtonPress(output)
                false -> handleButtonUp(output)
            }
        }

        buttonA.setOnButtonEventListener { button, pressed ->
            handleButtonEvent(pressed, RED)
        }
        buttonB.setOnButtonEventListener { button, pressed ->
            handleButtonEvent(pressed, GREEN)
        }
        buttonC.setOnButtonEventListener { button, pressed ->
            handleButtonEvent(pressed, BLUE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            buttonA.close()
            buttonB.close()
            buttonC.close()

            clearRGBLEDs()
            ledRed.close()
            ledGreen.close()
            ledBlue.close()

            clearLedStrip()
            ledstrip.close()

            speaker.close()
        } catch (e: Throwable) {
            Log.e("onDestroy", e.message)
        }
    }

    private fun handleButtonPress(output:Output) {
        speaker.play(output.frequency)
        setLedStripColor(output.color)

        when (output) {
            RED -> ledRed
            GREEN -> ledGreen
            BLUE -> ledBlue
        }.value = true
    }

    private fun handleButtonUp(output:Output) {
        speaker.stop()
        clearLedStrip()

        when (output) {
            RED -> ledRed
            GREEN -> ledGreen
            BLUE -> ledBlue
        }.value = false
    }

    private fun setLedStripColor(color: Int) {
        val rainbow = IntArray(RainbowHat.LEDSTRIP_LENGTH)
        for (i in rainbow.indices) {
            rainbow[i] = color
        }
        ledstrip.write(rainbow)
    }

    private fun clearLedStrip() {
        setLedStripColor(Color.TRANSPARENT)
    }

    private fun clearRGBLEDs() {
        ledRed.value = false
        ledGreen.value = false
        ledBlue.value = false
    }
}
