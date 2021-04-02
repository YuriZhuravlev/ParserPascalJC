package util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

class SingleSelection {
    var selected: Any? by mutableStateOf(null)
}