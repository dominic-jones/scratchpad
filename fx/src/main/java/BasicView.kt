import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import tornadofx.*

class BasicView : View() {

    val controller: BasicController by inject()
    val person = Person("Tulip", "Mr")
    val model = PersonModel(person)

    override val root: Pane = borderpane {
        top(TopView::class)
        left(LeftBar::class)
        center = Label("Testing")
        bottom = button {
            text = "doThing"
            actionEvents()
                    .map { Unit }
                    .doOnNext { controller.doThing() }
                    .subscribe {}
        }
    }
}

class BasicController : Controller() {
    fun doThing() {
        println("blah")
    }
}

class PersonModel(var person: Person) : ViewModel() {
    val name = bind { person.nameProperty }
    val title = bind { person.titleProperty }
}

class Person(name: String? = null, title: String? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val titleProperty = SimpleStringProperty(this, "title", title)
    var title by titleProperty
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