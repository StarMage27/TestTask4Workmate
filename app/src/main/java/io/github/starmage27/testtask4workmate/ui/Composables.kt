package io.github.starmage27.testtask4workmate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.starmage27.testtask4workmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    modifier: Modifier = Modifier,
    label: String = "Please set label",
    elements: List<String> = emptyList(),
    onClick: (selectedElement: String) -> Unit = {}
) {
    val anyString = stringResource(R.string.any)
    var isExpanded by remember { mutableStateOf(false) }
    var selectedElement by remember { mutableStateOf(anyString) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it && elements.isNotEmpty() },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedElement,
            onValueChange = { newValue -> selectedElement = newValue },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable)
            ,
            enabled = (elements.isNotEmpty())
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            if (elements.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(anyString) },
                    onClick = {},
                )
            }
            elements.forEach { element ->
                DropdownMenuItem(
                    text = { Text(
                        text = element,
                    ) },
                    onClick = {
                        selectedElement = element
                        isExpanded = false
                        if (selectedElement == anyString) {
                            onClick("")
                        } else {
                            onClick(selectedElement)
                        }
                    },
                )
            }
        }
    }
}