package com.n8.hellorainbowhat

import android.app.Activity
import android.os.Bundle
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

class MainActivity : Activity() {

    val buttonA:Button by lazy { RainbowHat.openButtonA() }
    val buttonB:Button by lazy { RainbowHat.openButtonB() }
    val buttonC:Button by lazy { RainbowHat.openButtonC() }

    val ledRed:Gpio by lazy { RainbowHat.openLedRed() }
    val ledGreen:Gpio by lazy { RainbowHat.openLedGreen() }
    val ledBlue:Gpio by lazy { RainbowHat.openLedBlue() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // turn off all leds
        ledRed.value = false
        ledGreen.value = false
        ledBlue.value = false

        // turn on an led when each button is pressed
        buttonA.setOnButtonEventListener { button, pressed -> ledRed.value = pressed }
        buttonB.setOnButtonEventListener { button, pressed -> ledGreen.value = pressed }
        buttonC.setOnButtonEventListener { button, pressed -> ledBlue.value = pressed }
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonA.close()
        buttonB.close()
        buttonC.close()

        ledRed.close()
        ledGreen.close()
        ledBlue.close()
    }

}
