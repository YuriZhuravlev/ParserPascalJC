package screen.editor


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import screen.common.AppTheme
import screen.common.Settings
import util.loadableScoped
import kotlin.text.Regex.Companion.fromLiteral

// key - сравнивает с последними значениями
@Composable
fun EditorView(model: Editor, settings: Settings) = key(model) {
    with(LocalDensity.current) {
        SelectionContainer {
            Surface(
                Modifier.fillMaxSize(),
                color = AppTheme.colors.backgroundDark,
            ) {
                val lines by loadableScoped(model.lines)

                if (lines != null) {
                    Box {
                        Lines(lines!!, settings)
                        Box(
                            Modifier
                                .offset(
                                    x = settings.fontSize.toDp() * 0.5f * settings.maxLineSymbols
                                )
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(AppTheme.colors.backgroundLight)
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Lines(lines: Editor.Lines, settings: Settings) = with(LocalDensity.current) {
    val maxNum = remember(lines.lineNumberDigitCount) {
        (1..lines.lineNumberDigitCount).joinToString(separator = "") { "9" }
    }

    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberLazyListState()
        val lineHeight = settings.fontSize.toDp() * 1.6f

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState
        ) {
            items(lines.size) { index ->
                Box(Modifier.height(lineHeight)) {
                    Line(Modifier.align(Alignment.CenterStart), maxNum, lines[index], settings)
                }
            }
        }

//        VerticalScrollbar(
//            adapter = rememberScrollbarAdapter(scrollState, lines.size, lineHeight),
//            modifier = Modifier.align(Alignment.CenterEnd)
//        )
    }
}

@Composable
private fun Line(modifier: Modifier, maxNum: String, line: Editor.Line, settings: Settings) {
    Row(modifier = modifier) {
        DisableSelection {
            Box {
                LineNumber(maxNum, Modifier.alpha(0f), settings)
                LineNumber(line.number.toString(), Modifier.align(Alignment.CenterEnd), settings)
            }
        }
        LineContent(
            line.content,
            modifier = Modifier
                .weight(1f)
//                .withoutWidthConstraints()
                .padding(start = 28.dp, end = 12.dp),
            settings = settings
        )
    }
}

@Composable
private fun LineNumber(number: String, modifier: Modifier, settings: Settings) = Text(
    text = number,
    fontSize = settings.fontSize,
    color = LocalContentColor.current.copy(alpha = 0.30f),
    modifier = modifier.padding(start = 12.dp)
)

@Composable
private fun LineContent(content: Editor.Content, modifier: Modifier, settings: Settings) = Text(
    text = if (content.isCode) {
        codeString(content.value.value)
    } else {
        buildAnnotatedString {
            withStyle(AppTheme.code.simple) {
                append(content.value.value)
            }
        }
    },
    fontSize = settings.fontSize,
    modifier = modifier,
    softWrap = false
)

private fun codeString(str: String) = buildAnnotatedString {
    withStyle(AppTheme.code.simple) {
        val strFormatted = str.replace("\t", "    ")
        append(strFormatted)
        addStyle(AppTheme.code.punctuation, strFormatted, ":")
        addStyle(AppTheme.code.punctuation, strFormatted, "=")
        addStyle(AppTheme.code.punctuation, strFormatted, ">")
        addStyle(AppTheme.code.punctuation, strFormatted, "<")
        addStyle(AppTheme.code.punctuation, strFormatted, "(")
        addStyle(AppTheme.code.punctuation, strFormatted, ")")
        addStyle(AppTheme.code.punctuation, strFormatted, ";")
        addStyle(AppTheme.code.punctuation, strFormatted, ",")
        addStyle(AppTheme.code.punctuation, strFormatted, ".")
        addStyle(AppTheme.code.punctuation, strFormatted, "^")
        addStyle(AppTheme.code.punctuation, strFormatted, "@")
        addStyle(AppTheme.code.punctuation, strFormatted, "+")
        addStyle(AppTheme.code.punctuation, strFormatted, "-")
        addStyle(AppTheme.code.punctuation, strFormatted, "*")
        addStyle(AppTheme.code.punctuation, strFormatted, "<")
        addStyle(AppTheme.code.punctuation, strFormatted, ">")
        addStyle(AppTheme.code.keyword, strFormatted, "program")
        addStyle(AppTheme.code.keyword, strFormatted, "const")
        addStyle(AppTheme.code.keyword, strFormatted, "var")
        addStyle(AppTheme.code.keyword, strFormatted, "begin")
        addStyle(AppTheme.code.keyword, strFormatted, "end")
        addStyle(AppTheme.code.keyword, strFormatted, "or")
        addStyle(AppTheme.code.keyword, strFormatted, "div")
        addStyle(AppTheme.code.keyword, strFormatted, "and")
        addStyle(AppTheme.code.keyword, strFormatted, "not")
        addStyle(AppTheme.code.keyword, strFormatted, "write")
        addStyle(AppTheme.code.keyword, strFormatted, "read")
        addStyle(AppTheme.code.keyword, strFormatted, "for")
        addStyle(AppTheme.code.keyword, strFormatted, "if")
        addStyle(AppTheme.code.keyword, strFormatted, "integer")
        addStyle(AppTheme.code.keyword, strFormatted, "boolean")
        addStyle(AppTheme.code.keyword, strFormatted, "then")
        addStyle(AppTheme.code.keyword, strFormatted, "else")
        addStyle(AppTheme.code.keyword, strFormatted, "do")
        addStyle(AppTheme.code.keyword, strFormatted, "to")
        addStyle(AppTheme.code.keyword, strFormatted, "downto")
        addStyle(AppTheme.code.value, strFormatted, "true")
        addStyle(AppTheme.code.value, strFormatted, "false")
        addStyle(AppTheme.code.value, strFormatted, Regex("[0-9]*"))
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: String) {
    addStyle(style, text, fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: Regex) {
    for (result in regexp.findAll(text)) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}