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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.ui.theme.CRCalculatorTheme
import kotlin.math.exp

private val playerLowXPBudget = mapOf(
    1 to 50,
    2 to 100,
    3 to 150,
    4 to 250,
    5 to 500,
    6 to 600,
    7 to 750,
    8 to 1000,
    9 to 1300,
    10 to 1600,
    11 to 1900,
    12 to 2200,
    13 to 2600,
    14 to 2900,
    15 to 3300,
    16 to 3800,
    17 to 4500,
    18 to 5000,
    19 to 5500,
    20 to 6400,
)

private val playerModerateXPBudget = mapOf(
    1 to 75,
    2 to 150,
    3 to 225,
    4 to 375,
    5 to 750,
    6 to 1000,
    7 to 1300,
    8 to 1700,
    9 to 2000,
    10 to 2300,
    11 to 2900,
    12 to 3700,
    13 to 4200,
    14 to 4900,
    15 to 5400,
    16 to 6100,
    17 to 7200,
    18 to 8700,
    19 to 10700,
    20 to 13200,
)

private val playerHighXPBudget = mapOf(
    1 to 100,
    2 to 200,
    3 to 400,
    4 to 500,
    5 to 1100,
    6 to 1400,
    7 to 1700,
    8 to 2100,
    9 to 2600,
    10 to 3100,
    11 to 4100,
    12 to 4700,
    13 to 5400,
    14 to 6200,
    15 to 7800,
    16 to 9800,
    17 to 11700,
    18 to 14200,
    19 to 17200,
    20 to 22000,
)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    options: List<Int>,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value.toString(),
            onValueChange = {},
            modifier = Modifier
                .width(100.dp)
                .menuAnchor(),
            readOnly = true,
            label = { Text(text = label) },
            maxLines = 1,
            minLines = 1,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option.toString())
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
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
        // possibly remove per row xp budget lines entirely?
        Text(text = "XP: ${playerModerateXPBudget.getOrDefault(level, 0) * quantity}")
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