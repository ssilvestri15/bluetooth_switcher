import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import javax.print.attribute.standard.Destination


object Screen {


    object Main {
        val SPLASH_SCREEN = "splash"
        val ONBOARDING_SCREEN = "onboarding"
        val HOME_SCREEN = "home"
    }

    object OnBoarding {
        val WELCOME_SCREEN = "welcome"
        val CONFIG_SCREEN = "config"
        val LOADING_SCREEN = "loading"
    }

    object Shared {
        val ASK_PERMISSION_SCREEN = "ask_permission"
    }

    val ERROR_SCREEN = "error"
}

class NavHost(private val startDestination: String, private val myNavController: NavController, val screens: NavScope.() -> Unit) {
    init {
        myNavController.setupNavigation(startDestination, this)
    }
}

class NavController {

    private val navigationComposables = mutableMapOf<String, NavigationComposable>()
    private val navigationStack = mutableListOf<NavigationComposable>()
    private var _isNavHostAttached = mutableStateOf(false)

    fun setupNavigation(startDestination: String, nav: NavHost) {
        // Execute the DSL code to populate navigationComposables
        val navScope = object : NavScope {
            override fun composable(name: String, content: @Composable () -> Unit) {
                addComposable(NavigationComposable(name, content))
            }
        }
        nav.screens(navScope)
        _isNavHostAttached.value = true
    }

    fun addComposable(composable: NavigationComposable) {
        navigationComposables[composable.name] = composable
    }

    @Composable
    fun navigateTo(composableName: String, setHead: Boolean = false) {
        val composable = navigationComposables[composableName]
        if (composable != null) {
            // Execute the content associated with the selected screen
            composable.content().let {
                if (setHead)
                    navigationStack.clear()

                navigationStack.add(composable)
            }
        } else {
            println("Screen not found: $composableName")
        }
    }

    @Composable
    fun back() {
        if (navigationStack.size > 1) {
            navigationStack.removeLast()
            val composable = navigationStack.last()
            // Execute the content associated with the selected screen
            composable.content()
        }
    }

}

data class NavigationComposable(val name: String, val content: @Composable () -> Unit)

interface NavScope {
    fun composable(name: String, content: @Composable () -> Unit)
}