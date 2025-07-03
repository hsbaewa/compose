package kr.co.hs.compose

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

@Preview
@Composable
private fun PreviewCheckbox() {
    Checkbox(
        checked = true,
        label = "checkbox",
        spaceWithLabel = 10.dp
    )
}

@Composable
fun Checkbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    size: Dp? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    label: String? = null,
    spaceWithLabel: Dp? = null
) {
    label
        ?.let {
            Row(modifier = size
                ?.let {
                    modifier
                        .width(it)
                        .aspectRatio(1f)
                }
                ?: modifier
            ) {
                RawCheckbox(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = onCheckedChange
                )
                spaceWithLabel?.let { Spacer(modifier = Modifier.size(it)) }
                Text(
                    text = it,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        ?: RawCheckbox(
            modifier = size
                ?.let {
                    modifier
                        .width(it)
                        .aspectRatio(1f)
                }
                ?: modifier,
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )

}

@Composable
private fun RawCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        androidx.compose.material3.Checkbox(
            checked = checked,
            modifier = modifier,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}


@Preview
@Composable
private fun PreviewRadioGroup() {
    RadioGroup(
        list = listOf("a", "b"),
        spaceWithLabel = 8.dp,
        selected = 1,
        onSelectedIndex = { }
    )
}


@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    list: List<String>,
    selected: Int? = null,
    spaceWithLabel: Dp? = null,
    onSelectedIndex: ((Int) -> Unit)? = null
) {
    var select by remember { mutableIntStateOf(-1) }
    LaunchedEffect(selected) {
        selected?.let {
            select = it
            onSelectedIndex?.invoke(it)
        }
    }

    Column(modifier = modifier) {
        list.fastForEachIndexed { i, s ->
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                val view = LocalView.current

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = CircleShape,
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        select = i
                        onSelectedIndex?.invoke(i)
                    }
                ) {
                    RadioButton(
                        selected = select == i,
                        label = s,
                        spaceWithLabel = spaceWithLabel,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
                        onClick = null
                    )
                }
            }

        }

    }
}

@Preview
@Composable
private fun PreviewRadioButton() {
    RadioButton(
        selected = true,
        label = "radio",
        spaceWithLabel = 10.dp
    )
}

@Composable
fun RadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    label: String? = null,
    spaceWithLabel: Dp? = null
) {
    label
        ?.let {
            Row(modifier = modifier) {
                RawRadioButton(selected = selected, enabled = enabled, onClick = onClick)
                spaceWithLabel?.let { Spacer(modifier = Modifier.size(it)) }
                Text(
                    text = it,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        ?: RawRadioButton(
            selected = selected,
            modifier = modifier,
            enabled = enabled,
            onClick = onClick
        )
}

@Composable
private fun RawRadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}