package otus.homework.flowcats

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class CatsRepository(
    private val catsService: CatsService,
    private val refreshIntervalMs: Long = 5000
) {

    fun listenForCatFacts() = flow {
        while (true) {
            runCatching { catsService.getCatFact() }
                .onSuccess { emit(Result.Success(it)) }
                .onFailure { emit(Result.Error(it.message.toString())) }
            delay(refreshIntervalMs)
        }
    }
}
