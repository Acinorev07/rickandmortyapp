package com.example.rickandmortyapp

import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.use_cases.GetCharactersUseCase
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.ui.home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: CoroutineDispatcher = kotlinx.coroutines.test.TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        (testDispatcher as? kotlinx.coroutines.test.TestCoroutineDispatcher)?.cleanupTestCoroutines()
    }
}


@ObsoleteCoroutinesApi
internal class HomeViewModelTest {

    @RelaxedMockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        homeViewModel = HomeViewModel(getCharactersUseCase)

        // Utiliza Dispatchers.Unconfined en lugar de Dispatchers.Main
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the HomeViewModel is started, the use case returns the characters and saves them in the mutableStateOf state, to be displayed on the main screen`(): Unit =
        runTest {
            // tu lógica de prueba aquí


            // Given
            val page = 1
            val listCharacters = listOf(
                Characters(1, "nameCharacter1", "Human", "image1"),
                Characters(2, "nameCharacter2", "Alien", "image2"),
                Characters(3, "nameCharacters3", "Animal", "image3")
            )
            val expected = Result.Success(listCharacters)
            coEvery { getCharactersUseCase(page) } returns flowOf(expected)

            // When
            homeViewModel.getCharacters(true)

            println("ViewModel Characters: ${homeViewModel.state.characters}")
            println("Expected Characters: $listCharacters")
            assert(homeViewModel.state.characters == listCharacters)
        }

}