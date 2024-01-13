package com.example.rickandmortyapp.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.rickandmortyapp.domain.model.Characters

@Composable
fun SearchCharactersItem(
    modifier: Modifier = Modifier,
    item: Characters
    ){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(corner = CornerSize(12.dp))
    ){
        Row{
            CharacterImageContainer2(modifier = Modifier.size(75.dp)){
                CharacterImage2(item.image)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier.fillMaxWidth()){
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${item.specie}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun CharacterImage2(image:String){
    Box{
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .size(Size.ORIGINAL)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CharacterImageContainer2(
    modifier: Modifier,
    content: @Composable ()-> Unit
){
    Surface(modifier.aspectRatio(1f), RoundedCornerShape(4.dp)){
        content()
    }
}

