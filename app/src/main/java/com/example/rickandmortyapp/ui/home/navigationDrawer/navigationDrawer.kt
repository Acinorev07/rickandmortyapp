package com.example.rickandmortyapp.ui.home.navigationDrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.ui.home.components.DrawerItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navigationDrawer(
    name: String,
    email: String,
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    onItemClicked: (DrawerItem)->Unit
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier= Modifier.padding(20.dp)){
            Column(){
                Image(
                    painter = painterResource(id = R.drawable.rick),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .width(70.dp)
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Text(text = name)
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = email)

            }

        }

        Divider()
        items.forEach { item ->
            val interactionSource = remember { MutableInteractionSource() }
            val colors = NavigationDrawerItemDefaults.colors()
            val selected = remember { mutableStateOf(false) } // Aquí puedes poner tu lógica para determinar si el item está seleccionado

            NavigationDrawerItem(
                label = {  Text(text = stringResource(id = item.resourceId)) },
                selected = selected.value,
                onClick = {

                   // onItemClick(item)
                    //selected.value = !selected.value
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = {  Icon(imageVector = item.icon, contentDescription = stringResource(id = item.resourceId)) },
                colors = colors,
                interactionSource = interactionSource
            )
        }
    }
}