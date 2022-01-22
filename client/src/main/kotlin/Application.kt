import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.css.*
import model.*
import react.*
import react.dom.html.ReactHTML.h1
import react.dom.render
import services.SquareService
import styled.css
import styled.styledDiv
import styled.styledH2
import kotlin.time.Duration.Companion.seconds

fun main() {
    render(document.getElementById("react-app")!!) {
        h1 {
            +"Квадрат"
        }
        child(Main::class) { }
    }
}

external interface MainState : State {
    var square: Square?
}

class Main : RComponent<Props, MainState>() {
    init {
        state.square = null
    }

    override fun componentDidMount() {
        val service = SquareService(Job())
        MainScope().launch {
            while (true) {
                val square = service.getSquare()
                setState { this.square = square.toSquare() }
                delay(0.1.seconds)
            }
        }
    }

    override fun RBuilder.render() {
        state.square?.toColors()?.let { colors ->
            styledDiv {
                colors.forEach { line ->
                    styledDiv {
                        css {
                            display = Display.flex
                        }
                        line.forEach {
                            styledDiv {
                                css {
                                    width = (500 / colors.size).px
                                    height = (500 / colors.size).px
                                    backgroundColor = rgb(it.r, it.g, it.b)
                                }
                            }
                        }
                    }
                }
            }
        }

        state.square?.calculateGoodness(Quadratic)?.let {
            styledH2 {
                +"Хорошесть = -$it"
            }
        }
    }
}