import javafx.application.Application
import tornadofx.App

class BasicApplication : App(BasicView::class)

fun main(args: Array<String>) {
    Application.launch(BasicApplication::class.java, *args)
}