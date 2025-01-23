package otus.homework.flowcats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                catsViewModel.catsStateFlow.collect { state ->
                    when (state) {
                        is Result.Success -> {
                            view.hideProgressBar()
                            view.populate(state.data)
                        }

                        is Result.Error -> {
                            view.hideProgressBar()
                            Toast.makeText(
                                this@MainActivity,
                                "Error: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        Result.Loading -> {
                            view.showProgressBar()
                        }
                    }
                }
            }
        }
    }
}