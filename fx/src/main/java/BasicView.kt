import com.fasterxml.jackson.annotation.JsonIgnoreType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.toBinding
import javafx.beans.property.Property
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
import java.nio.file.Paths

class BasicView : View() {

    companion object : KLogging()

    override val root = BorderPane()

    val controller: BasicController by inject()
    val model: PersonModel by inject()

    init {
        with(root) {
            top(TopView::class)
            left(LeftBar::class)
            center = vbox {
                hbox {
                    label("name")
                    label(model.name)
                }
                hbox {
                    label {
                        text = "str"
                    }
                    label(model.str)
                    button("+") {
                        actionEvents()
                                .map { model.str }
                                .subscribe { it.setValue(it.value.toLong() + 1) }
                    }
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
                                .map { "modified" }
                                .toBinding()
                )

                model.itemProperty.bind(
                        button.actionEvents()
                                // TODO 2017-07-13 Dom - Get from somewhere better
                                .map { controller.get("Kevral") }
                                .startWith(controller.get("Riesz"))
                                .toBinding()
                )

                button {
                    text = "Save"
                    actionEvents()
                            .subscribe { controller.save(model) }
                }
            }
        }
    }

}

@JsonIgnoreType
abstract class PropertyMixin : Property<Any>

class BasicController : Controller() {

    companion object : KLogging()

    val jsonWriter: ObjectWriter = ObjectMapper()
            .setMixIns(mapOf(Property::class.java to PropertyMixin::class.java))
            .writerWithDefaultPrettyPrinter()

    val person = Person("Riesz", 14)
    val person2 = Person("Kevral", 10)

    val data = listOf(person, person2).associateBy { it.name }

    fun get(name: String): Person {
        return data[name] ?: throw RuntimeException()
    }

    fun save(model: PersonModel) {
        model.commit()
        Paths.get("out-data")
                .resolve("out.json")
                .toFile()
                .printWriter()
                .use {
                    val thing = jsonWriter.writeValueAsString(model.item)
                    logger.info { thing }
                    it.println(thing)
                }
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