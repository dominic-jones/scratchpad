import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class Basic : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Hello"

        val button = Button()
        button.text = "World"
        button.setOnAction { it -> println(it.source) }

        val stackPane = StackPane()
        stackPane.children.add(button)

        primaryStage.scene = Scene(stackPane, 300.0, 250.0)
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(Basic::class.java)
}