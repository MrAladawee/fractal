import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.math.sqrt

open class Scales(
    var width: Int = 0,
    var height: Int = 0,
    var xMax: Double = 0.0,
    var xMin: Double = 0.0,
    var yMax: Double = 0.0,
    var yMin: Double = 0.0)
{

}

class Decart(var x: Float, var y: Float): Scales() {

    // 1_D = W / (xMax - xMin)
    fun ScreenToDec(s: Screen): Decart {
        var x_ = s.x*(xMax-xMin) / width + xMin
        var y_ = yMax - s.y*(yMax-yMin) / height
        return Decart(x_.toFloat(), y_.toFloat())
    }
}

class Screen(var x: Int, var y: Int):Scales() {

    fun DecToScreen(d: Decart): Screen {
        // (x_ - xMin) / (xMax - xMin) / = s.x
        var x_ = (((d.x - xMin) * width) / (xMax - xMin))
        var y_ = (((yMax - d.y) * width) / (xMax - xMin))
        return Screen(x_.toInt(), y_.toInt())
    }

}

class Complex(var x: Double, var y: Double) {

    fun pow(k: Int): Complex {
        var c: Complex = Complex(1.0,0.0)

        for (i in 1..k) {
            c *= Complex(this.x, this.y)
        }
        return c
    }

    operator fun times(c: Complex ) : Complex {
        return Complex(this.x*c.x - this.y*c.y, this.x*c.y+this.y*c.x)
    }
    operator fun plus(c: Complex ) : Complex {
        return Complex(this.x+c.x, this.y+c.y)
    }

    fun abs() : Double {
        return sqrt(this.x*this.x + this.y*this.y)
    }
}

fun mandelbrot(d: Decart) : Int {

    var maxIter = 1000
    var r = 4.0
    var c: Complex = Complex(d.x.toDouble(), d.y.toDouble())
    var z = Complex(0.0,0.0)

    var iter = 0

    while( z.abs() < r && iter < maxIter ) {
        z = z.pow(2) + c
        iter++

    }

    return iter

}

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Canvas(modifier = Modifier.fillMaxSize(),
        onDraw = {
            var scales = Scales(this.size.width.toInt(), this.size.height.toInt(),
                10.0,-10.0,5.0,-5.0)

            for (i in 0..scales.width) {
                for (j in 0..scales.height) {
                    var s = Screen(i,j)
                    var d = Decart(0f, 0f)
                    d = d.ScreenToDec(s)
                    var clr = mandelbrot(d)

                    if (clr == 1000) {
                        drawCircle(
                            color = Color.Black,
                            radius = 1f,
                            center = Offset(i.toFloat(),j.toFloat())
                        )
                    }
                    else {
                        drawCircle(
                            color = Color.White,
                            radius = 1f,
                            center = Offset(i.toFloat(),j.toFloat())
                        )
                    }

                }
            }

        }
    )
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
