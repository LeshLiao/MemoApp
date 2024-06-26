package com.plcoding.roomguideandroid

import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.plcoding.roomguideandroid.ui.theme.RoomGuideAndroidTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contacts.db"
        ).build()
    }
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoomGuideAndroidTheme {

                val state by viewModel.state.collectAsState()
                ContactScreen(state = state, onEvent = viewModel::onEvent)

                var messageText by remember { mutableStateOf("Hello World!") }
                MessageScreen(messageText)

//                myTestFunction()

                // Initialize the handler
                handler = Handler(Looper.getMainLooper()) {
                    if (it.what == 1) {
                        messageText = it.obj as String
                        true
                    } else {
                        false
                    }
                }


                // Start the background thread
                Thread {
                    try {
                        Thread.sleep(2000) // Sleep for 2 seconds
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    // Create and send a message
                    val message = handler.obtainMessage(1, "Hello from background thread!")
                    handler.sendMessage(message)
                }.start()
            }
        }
    }
}

@Composable
fun MessageScreen(messageText: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = messageText)
    }
}

//val exampleVal: String by lazy {
//    println("TEST_TEST, initial1!")
//    println("TEST_TEST, initial2!")
//    println("TEST_TEST, initial3!")
//    "Welcome1"
//    "Welcome2"
//}

fun myTestFunction() {
    var myInt:Int = 1
    var myVal:Boolean? = null



//    println("TEST_TEST", myInt)
    Log.d("TAG_TEST", myInt.toString())
    myInt = 2

    if (myVal == true) {
        Log.d("TAG_TEST", myInt.toString())
    }


    for (i in 100..103) {
        Log.d("TAG_TEST", i.toString())
    }

    val items = arrayOf("Volvo", "BMW", "Ford", "Mazda")
    Log.d("TAG_TEST", items[0])

    for (item in items) {
        Log.d("TAG_TEST", item)
    }

    val number = 3
    var day = when(number) {
        1 -> "one"
        2 -> "two"
        3 -> "three"
        else -> "unknown"
    }

    Log.d("TAG_TEST", "day=" + day)

    val car1 = Car("volvo",2025)
//    car1.brand = "volvo"5
//    car1.year = 2024
//    Log.d("TAG_TEST", "car=" + car1.brand + car1.year.toString())

    val bus1 = Bus("Mazda",2003)
    Log.d("TAG_TEST", "bus=" + bus1.brand + bus1.year.toString())

    fun Int.timesThree() = this * 3
    Log.d("TAG_TEST", "5 timesThree()=" + 5.timesThree())

    Log.d("TAG_TEST", bus1.toString())
    Log.d("TAG_TEST", car1.toString())
}

//class Car() {
//    var brand = ""
//    var year = 0
//}

class Bus(var brand:String, val year:Int)

data class Car(var brand:String, var year:Int)