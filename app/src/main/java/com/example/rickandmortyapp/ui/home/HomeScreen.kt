package com.example.rickandmortyapp.ui.home


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.ui.home.components.CharacterItem
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.ui.home.components.DrawerItem
import com.example.rickandmortyapp.ui.home.components.SearchCharactersItem
import com.example.rickandmortyapp.ui.home.navigationDrawer.navigationDrawer
import com.example.rickandmortyapp.ui.home.searchBar.SearchEvent
import com.example.rickandmortyapp.ui.home.searchBar.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClicked: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow
    val scaffoldState = rememberScaffoldState()

    // Implementacion del modalNavigationDrawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.showSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val state2 = searchViewModel.state
    val eventFlow2 = searchViewModel.eventFlow

    LaunchedEffect(scaffoldState.snackbarHostState) {
        eventFlow2.collect { event ->
            when (event) {
                is SearchViewModel.RUIEvent.ShowSnackBar -> event.message.let {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.asString(context)
                    )
                }
            }
        }
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f) // Ajusta la altura maxima aquí (0.5f indica la mitad de la pantalla)
                    .background(Color.Transparent)
            ) {
                navigationDrawer(
                    name = "Fredy Acuña",
                    email = "Facunap@unal.edu.co",
                    items = DrawerItem.values().toList()
                ) {
                    when (it) {
                        DrawerItem.ABOUT -> {
                            // navController.navigate(AppScreen.FormScreen.route)
                        }
                        DrawerItem.SETTINGS -> {
                            Toast.makeText(context, "Oprimiste el Boton settings", Toast.LENGTH_SHORT).show()
                        }
                        DrawerItem.RECENT -> {
                            Toast.makeText(context, "Oprimiste el Boton recent", Toast.LENGTH_SHORT).show()
                        }
                        DrawerItem.UPLOAD -> {
                            Toast.makeText(context, "Oprimiste el Boton upload", Toast.LENGTH_SHORT).show()
                        }
                    }
                    scope.launch { drawerState.close() }
                }
            }
        },
        content = {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {

                    HomeTopBar(onMenuClick = { scope.launch { drawerState.open() } })
                },
                bottomBar = {
                    HomeBottomBar(
                        showPrevious = state.showPrevious,
                        showNext = state.showNext,
                        onPreviousPressed = { viewModel.getCharacters(false) },
                        onNextPressed = { viewModel.getCharacters(true) }
                    )
                }
            ) { innerPadding ->

                    searchBar(
                        modifier = Modifier.padding(innerPadding),
                        searchViewModel = searchViewModel,
                        onItemClicked = { onItemClicked(it) },
                        state = viewModel
                    )


                }

        }
    )
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    onMenuClick:()->Unit
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.home_title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = {onMenuClick()}){
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }
        ,
        backgroundColor = MaterialTheme.colors.surface
    )
}

@Composable
private fun HomeBottomBar(
    showPrevious: Boolean,
    showNext: Boolean,
    onPreviousPressed: ()->Unit,
    onNextPressed: ()->Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = showPrevious,
                onClick = onPreviousPressed
            ){
                Text(
                    text = stringResource(id = R.string.previous_button)
                )
            }
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = showNext,
                onClick = onNextPressed
            ){
                Text(
                    text = stringResource(id= R.string.next_button)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBar(
    modifier: Modifier,
    searchViewModel: SearchViewModel,
    onItemClicked: (Int) -> Unit,
    state: HomeViewModel
){
    val state2 = state.state
    val searchBarState = searchViewModel.state

    DockedSearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp), // Modificado: fillMaxWidth() y padding
        query = searchBarState.input,
        onQueryChange = { searchViewModel.onEvent(SearchEvent.EnteredCharacter(it)) }, // Emite evento al cambiar la búsqueda
        onSearch = { searchViewModel.findGetCharacters(searchBarState.input) },
        active = searchBarState.isLoading,
        onActiveChange = { searchViewModel.onActiveChange(it) }, // Agregado
        placeholder = { Text(stringResource(id = R.string.buscar), fontSize = 20.sp) },
        leadingIcon = {
            IconButton(onClick = {
                searchViewModel.findGetCharacters(
                    searchBarState.input,
                    true
                )
            }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }

                      },
        trailingIcon = {
            IconButton(onClick = {
                searchViewModel.onActiveChange(false)
            }) {
               Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
    ) {


        // Despliega los resultados de búsqueda
        val characterMatches = when (val result = searchBarState.characters) {
            is Result.Success -> result.data?.filter {
                it.name.contains(searchBarState.input, ignoreCase = true)
            } ?: emptyList()
            is Result.Error -> emptyList()
            is Result.Error2 -> emptyList()
            is Result.Loading -> emptyList()
        }

        LazyColumn {
            items(characterMatches.size) { index ->
                val character = characterMatches[index]
                SearchCharactersItem(
                    item = character,
                    modifier = Modifier
                        .clickable {
                            onItemClicked(character.id)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

    }

    HomeContent(
        modifier = Modifier.padding(top = 60.dp),
        onItemClicked = { onItemClicked(it) },
        isLoading = state2.isLoading,
        characters = state2.characters,

        )
}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onItemClicked: (Int) -> Unit,
    isLoading: Boolean = false,
    characters: List<Characters> = emptyList(),

) {

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.surface
    ) {

        // LazyColumn para mostrar la lista de personajes inicial
        LazyColumn(
            contentPadding = PaddingValues(vertical = 6.dp),
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(characters.size) { index ->
                    CharacterItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = characters[index],
                        onItemClicked = { onItemClicked(it) }
                    )
                }
            }
        )

        if (isLoading) FullScreenLoading()
    }
}

@Composable
private fun FullScreenLoading(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ){
        CircularProgressIndicator()
    }
}