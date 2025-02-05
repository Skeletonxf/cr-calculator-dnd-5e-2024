package io.github.skeletonxf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.engine.Budget
import io.github.skeletonxf.ui.NumberPicker
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRCalculatorTheme {
                Content()
            }
        }
    }
}

@Composable
@Preview
fun Content() {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize()
    ) {
        PlayerBudgetRow()
    }
}

@Composable
fun PlayerBudgetRow() {
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    var level by rememberSaveable { mutableIntStateOf(1) }
    Row(
        modifier = Modifier
            .sizeIn(maxWidth = 350.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            value = quantity,
            onValueChange = { quantity = it },
            label = "Quantity",
            options = (1..12).toList(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "x")
        Spacer(modifier = Modifier.width(8.dp))
        NumberPicker(
            value = level,
            onValueChange = { level = it },
            label = "Level",
            options = (1..20).toList()
        )
        Spacer(modifier = Modifier.width(8.dp))
        // TODO: Need to factor in height of half a label text to properly align the row
        // or possibly remove per row xp budget lines entirely?
        Text(text = "XP: ${Budget.get(level, Budget.Type.Moderate) * quantity}")
        Spacer(modifier = Modifier.width(8.dp))
        Spacer(modifier = Modifier.weight(1F))
        IconButton(
            onClick = {
                // todo clear row
            },
        ) {
            Text(text = "x") // todo replace with icon
        }
    }
}