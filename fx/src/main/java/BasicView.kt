import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.toBinding
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import mu.KLogging
import tornadofx.*

class BasicView : View() {

    companion object : KLogging()

    override val root = BorderPane()

    val controller: BasicController by inject()
    val person = Person("Riesz", 14)
    val person2 = Person("Kevral", 10)
    val model: PersonModel by inject()

    init {
        logger.info { "Testing logging" }

        with(root) {
            top(TopView::class)
            left(LeftBar::class)
            center = vbox {
                hbox {
                    label {
                        text = "str"
                    }
                    label(model.str)
                }
                hbox {
                    label {
                        text = "dex"
                    }
                    label(model.dex)
                }
            }
            bottom = hbox {
                val button = button {
                    text = "doThing"
                }

                val label = label {
                    text = "initial"
                }

                label.textProperty().bind(
                        button.actionEvents()
                                .map { Unit }
                                .doOnNext { controller.doThing() }
                                .map { "modified" }
                                .toBinding()
                )

                model.itemProperty.bind(
                        button.actionEvents()
                                .map { person2 }
                                .startWith(person)
                                .toBinding()
                )
            }
        }
    }
}

class BasicController : Controller() {
    fun doThing() {
        println("blah")
    }
}

class PersonModel : ItemViewModel<Person>() {
    val name = bind(Person::nameProperty)
    val str = bind(Person::strProperty)
    val dex = bind(Person::dexProperty)
}

class Person(name: String, str: Long) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val strProperty = SimpleLongProperty(this, "str", str)
    var str by strProperty

    val dexProperty = SimpleLongProperty(this, "str", 13)
    var dex by dexProperty
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