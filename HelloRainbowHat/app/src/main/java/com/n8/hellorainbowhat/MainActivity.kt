package com.n8.hellorainbowhat

import android.app.Activity
import android.os.Bundle
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

class MainActivity : Activity() {

    val buttonA:Button by lazy { RainbowHat.openButtonA() }
    val buttonB:Button by lazy { RainbowHat.openButtonB() }
    val buttonC:Button by lazy { RainbowHat.openButtonC() }

    val ledRed:Gpio by lazy { RainbowHat.openLedRed() }
    val ledGreen:Gpio by lazy { RainbowHat.openLedGreen() }
    val ledBlue:Gpio by lazy { RainbowHat.openLedBlue() }

    val speaker:Speaker by lazy { RainbowHat.openPiezo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // turn off all leds
        ledRed.value = false
        ledGreen.value = false
        ledBlue.value = false

        speaker.stop()

        buttonA.setOnButtonEventListener { button, pressed ->
            when(pressed){
                true -> speaker.play(261.626)
                false -> speaker.stop()
            }
            ledRed.value = pressed
        }
        buttonB.setOnButtonEventListener { button, pressed ->
            when(pressed){
                true -> speaker.play(293.665)
                false -> speaker.stop()
            }
            ledGreen.value = pressed
        }
        buttonC.setOnButtonEventListener { button, pressed ->
            when(pressed){
                true -> speaker.play(329.628)
                false -> speaker.stop()
            }
            ledBlue.value = pressed
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonA.close()
        buttonB.close()
        buttonC.close()

        ledRed.close()
        ledGreen.close()
        ledBlue.close()

        speaker.close()
    }

}
