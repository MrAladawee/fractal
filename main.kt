import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.MouseEvent
import javax.xml.crypto.Data
import kotlin.Exception
import kotlin.math.abs
import kotlin.math.pow

fun mandelbrot(ScalePoint : Pair<Int,Int>) : Int {
    var z1: Pair<Double, Double> = Pair(0.0, 0.0)
    var c: Pair<Double, Double> = Pair(ScalePoint.first.toDouble(), ScalePoint.second.toDouble())
    var iteration = 0
    var max_iteration = 1000

    while (((z1.first.pow(2.0) + z1.second.pow(2.0)).pow(0.5)) < 2 && iteration < max_iteration) {
        z1 = Pair(
            (z1.first * z1.first - z1.second * z1.second) + c.first,
            (z1.first * z1.second + z1.second * z1.first) + c.second
        )
        iteration++
    }
    return iteration
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    Canvas(modifier = Modifier.fillMaxSize().clickable{}. onPointerEvent(PointerEventType.Press){

    },
        onDraw = {

            var matrix = Array(this.size.width.toInt()){IntArray(this.size.height.toInt())}

            // Пробегаем по ширине
            var number : MutableSet<Int> = mutableSetOf()
            for (i in 0..this.size.width.toInt()-1) {
                // Пробегаем по высоте
                for (j in 0..this.size.height.toInt()-1)
                {
                    var chooseColor = mandelbrot(Pair(i,j))
                    var colorr : Color = Color.Red
                    if (chooseColor == 1000) {colorr = Color.White}
                    else if (chooseColor == 1) {colorr = Color.Black}
                    else if (chooseColor == 2) colorr = Color.Red

                    drawCircle(
                        color = colorr,
                        radius = 1f,
                        center = Offset(i.toFloat(),j.toFloat())
                    )

                    matrix[i][j] = chooseColor


                }
            }

            for (line in 0..(this.size.width).toInt() - 2) {
                for (column in 0..(this.size.height).toInt() - 2){
                    if (matrix[line][column] > 1) print("${line} : ${column} ")
                }
            }
        })
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
