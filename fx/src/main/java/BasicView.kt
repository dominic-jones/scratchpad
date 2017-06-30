import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import tornadofx.Controller
import tornadofx.View
import tornadofx.action
import tornadofx.borderpane
import tornadofx.button
import tornadofx.plusAssign

class BasicView : View() {

    val controller: BasicController by inject()

    override val root: Pane = borderpane {
        top(TopView::class)
        left(LeftBar::class)
        center = Label("Testing")
        bottom = button {
            text = "doThing"
            action {
                controller.doThing()
            }
        }
    }
}

class BasicController : Controller() {
    fun doThing() {
        println("blah")
    }
}

class TopView(override val root: Pane = StackPane()) : View() {
    init {
        root += Label("TopBar")
    }
}

class LeftBar(override val root: Pane = VBox()) : View() {
    init {
        root += Button("Command1")
        root += Button("Command2")
        root += Button("Command3")
    }
}