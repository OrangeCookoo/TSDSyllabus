package uk.co.btsda.syllabus.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random
import kotlinx.coroutines.launch
import uk.co.btsda.syllabus.R
import uk.co.btsda.syllabus.data.Belt
import uk.co.btsda.syllabus.data.Category
import uk.co.btsda.syllabus.data.SyllabusData
import uk.co.btsda.syllabus.data.Technique
import uk.co.btsda.syllabus.data.beltSubtitle

private enum class AppView(val label: String) {
    SYLLABUS("Syllabus"), BELT("By Belt"), QUIZ("Quiz")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusApp(vm: SyllabusViewModel = viewModel()) {
    val notes by vm.notes.collectAsState()
    val customized by vm.customized.collectAsState()
    var view by rememberSaveable { mutableStateOf(AppView.SYLLABUS) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppHeader() },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = view == AppView.SYLLABUS,
                    onClick = { view = AppView.SYLLABUS },
                    icon = { Icon(Icons.Filled.Category, contentDescription = null) },
                    label = { Text(AppView.SYLLABUS.label) },
                )
                NavigationBarItem(
                    selected = view == AppView.BELT,
                    onClick = { view = AppView.BELT },
                    icon = { Icon(Icons.Filled.MilitaryTech, contentDescription = null) },
                    label = { Text(AppView.BELT.label) },
                )
                NavigationBarItem(
                    selected = view == AppView.QUIZ,
                    onClick = { view = AppView.QUIZ },
                    icon = { Icon(Icons.Filled.Quiz, contentDescription = null) },
                    label = { Text(AppView.QUIZ.label) },
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (view) {
                AppView.SYLLABUS -> SyllabusByCategoryScreen(notes, customized, vm)
                AppView.BELT -> ByBeltScreen(notes, customized, vm)
                AppView.QUIZ -> {
                    val missCounts by vm.quizMissCounts.collectAsState()
                    QuizScreen(
                        notes = notes,
                        missCounts = missCounts,
                        onMissed = vm::quizMissed,
                        onCorrect = vm::quizCorrect,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyllabusByCategoryScreen(
    notes: Map<String, String>,
    customized: Set<String>,
    vm: SyllabusViewModel,
) {
    val categories = Category.entries
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        SearchField(query = query, onQueryChange = { query = it })
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text("${category.emoji}  ${category.display}", fontWeight = FontWeight.Bold)
                    },
                )
            }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            CategoryScreen(
                category = categories[page],
                notes = notes,
                customized = customized,
                query = query.trim(),
                onSave = vm::saveNote,
                onReset = vm::resetNote,
            )
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,
                    )
                )
            )
            .padding(start = 16.dp, end = 20.dp, top = 40.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(3.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.btsda_logo),
                contentDescription = "Bristol Tang Soo Do Academy logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().clip(CircleShape),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                "Tang Soo Do",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                "One-Step Syllabus · BTSDA",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        placeholder = { Text("Search techniques…") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
    )
}

@Composable
private fun CategoryScreen(
    category: Category,
    notes: Map<String, String>,
    customized: Set<String>,
    query: String,
    onSave: (String, String) -> Unit,
    onReset: (String) -> Unit,
) {
    val all = remember(category) { SyllabusData.byCategory(category) }
    val filtered = remember(category, query, notes) {
        if (query.isBlank()) all else all.filter { t ->
            val note = notes[t.id] ?: t.defaultNote
            note.contains(query, ignoreCase = true) ||
                t.number.toString() == query ||
                t.belt.display.contains(query, ignoreCase = true)
        }
    }
    val belts = remember(filtered) { filtered.map { it.belt }.distinct() }

    if (filtered.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "No techniques match “$query”.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 12.dp, end = 12.dp, top = 4.dp, bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        belts.forEach { belt ->
            val beltItems = filtered.filter { it.belt == belt }
            item(key = "header_${category.name}_${belt.name}") {
                BeltHeader(belt = belt, subtitle = beltSubtitle(category, belt), count = beltItems.size)
            }
            items(beltItems, key = { it.id }) { technique ->
                TechniqueCard(
                    technique = technique,
                    note = notes[technique.id] ?: technique.defaultNote,
                    isCustom = technique.id in customized,
                    onSave = onSave,
                    onReset = onReset,
                )
            }
        }
    }
}

@Composable
private fun BeltHeader(belt: Belt, subtitle: String?, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(listOf(belt.primary, belt.accent))
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                "${belt.display} Belt",
                color = belt.onPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    color = belt.onPrimary.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        Box(
            Modifier
                .clip(CircleShape)
                .background(belt.onPrimary.copy(alpha = 0.18f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("$count", color = belt.onPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TechniqueCard(
    technique: Technique,
    note: String,
    isCustom: Boolean,
    onSave: (String, String) -> Unit,
    onReset: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var editing by remember(technique.id) { mutableStateOf(false) }
    var draft by remember(technique.id) { mutableStateOf(note) }

    technique.groupLabel?.let { label ->
        Text(
            label.uppercase(),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 2.dp),
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberBadge(technique.number, technique.belt.primary, technique.belt.onPrimary)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    if (!editing) {
                        Text(
                            note,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (isCustom && !editing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(12.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "your note",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = editing) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = draft,
                        onValueChange = { draft = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Your note") },
                        minLines = 2,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                    )
                    Row(
                        Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isCustom) {
                            TextButton(onClick = {
                                onReset(technique.id)
                                draft = technique.defaultNote
                                editing = false
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Reset")
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = {
                            draft = note
                            editing = false
                        }) { Text("Cancel") }
                        TextButton(onClick = {
                            if (draft.isNotBlank()) onSave(technique.id, draft)
                            editing = false
                        }) { Text("Save") }
                    }
                }
            }

            if (!editing) {
                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PillButton(
                        text = "Watch video",
                        icon = Icons.Default.PlayArrow,
                        container = MaterialTheme.colorScheme.primary,
                        content = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { uriHandler.openUri(technique.videoUrl) },
                    )
                    PillButton(
                        text = "Edit note",
                        icon = Icons.Default.Edit,
                        container = MaterialTheme.colorScheme.surfaceVariant,
                        content = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            draft = note
                            editing = true
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberBadge(number: Int, container: Color, content: Color) {
    val bg by animateColorAsState(container, label = "badge")
    Box(
        Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Text("$number", color = content, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
}

@Composable
private fun PillButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    container: Color,
    content: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(container)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, color = content, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// ------------------------------------------------------------------ By Belt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ByBeltScreen(
    notes: Map<String, String>,
    customized: Set<String>,
    vm: SyllabusViewModel,
) {
    var beltOrdinal by rememberSaveable { mutableStateOf(Belt.WHITE.ordinal) }
    val belt = Belt.entries[beltOrdinal]
    val forBelt = remember(belt) { SyllabusData.byBelt(belt) }
    val cats = remember(belt) { Category.entries.filter { c -> forBelt.any { it.category == c } } }

    Column(Modifier.fillMaxSize()) {
        BeltSelector(
            belts = SyllabusData.beltsRanked,
            selected = belt,
            onSelect = { beltOrdinal = it.ordinal },
        )
        Text(
            "${belt.display} belt · ${forBelt.size} techniques",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            cats.forEach { cat ->
                val items = forBelt.filter { it.category == cat }
                item(key = "belt_${belt.name}_${cat.name}") { CategoryHeader(cat, items.size) }
                items(items, key = { it.id }) { t ->
                    TechniqueCard(
                        technique = t,
                        note = notes[t.id] ?: t.defaultNote,
                        isCustom = t.id in customized,
                        onSave = vm::saveNote,
                        onReset = vm::resetNote,
                    )
                }
            }
        }
    }
}

@Composable
private fun BeltSelector(belts: List<Belt>, selected: Belt, onSelect: (Belt) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        belts.forEach { belt ->
            BeltChip(belt = belt, selected = belt == selected) { onSelect(belt) }
        }
    }
}

/** A pill coloured with the belt's own colour; the selected one gets a ring + tick. */
@Composable
private fun BeltChip(belt: Belt, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Row(
        Modifier
            .clip(shape)
            .background(belt.primary)
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) belt.onPrimary else belt.onPrimary.copy(alpha = 0.30f),
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = belt.onPrimary,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(belt.display, color = belt.onPrimary, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategoryHeader(category: Category, count: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${category.emoji}  ${category.display}",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
        )
        Box(
            Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("$count", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------------- Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizScreen(
    notes: Map<String, String>,
    missCounts: Map<String, Int>,
    onMissed: (String) -> Unit,
    onCorrect: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var beltOrdinal by rememberSaveable { mutableStateOf(Belt.RED.ordinal) }
    val belt = Belt.entries[beltOrdinal]
    var includeBelow by rememberSaveable { mutableStateOf(false) }
    val pool = remember(belt, includeBelow) { SyllabusData.quizPool(belt, includeBelow) }

    var started by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf<Technique?>(null) }
    var revealed by remember { mutableStateOf(false) }
    var missed by remember { mutableStateOf(false) }
    var correct by remember { mutableStateOf(0) }
    var answered by remember { mutableStateOf(0) }

    // Weighted random draw: missed techniques (higher weight) come up more often,
    // and never repeat the same technique twice in a row when there's a choice.
    fun draw() {
        val candidates = pool.filter { it.id != current?.id }.ifEmpty { pool }
        val weighted = candidates.map { it to quizDrawWeight(missCounts[it.id] ?: 0) }
        val total = weighted.sumOf { it.second }.coerceAtLeast(1)
        var r = Random.nextInt(total)
        var pick = candidates.first()
        for ((t, w) in weighted) {
            r -= w
            if (r < 0) { pick = t; break }
        }
        current = pick
        revealed = false
        missed = false
    }

    fun start() {
        correct = 0
        answered = 0
        started = true
        current = null
        draw()
    }

    fun reset() {
        started = false
        current = null
        revealed = false
        missed = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        Text(
            "Revision quiz",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
        )
        Text(
            "Pick a belt and how much to revise. Techniques you miss come back " +
                "more often until you get them right.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 4.dp),
        )

        BeltSelector(
            belts = SyllabusData.beltsRanked,
            selected = belt,
            onSelect = { beltOrdinal = it.ordinal; reset() },
        )

        Row(
            Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = !includeBelow,
                onClick = { includeBelow = false; reset() },
                label = { Text("This belt") },
            )
            FilterChip(
                selected = includeBelow,
                onClick = { includeBelow = true; reset() },
                label = { Text("This belt & below") },
            )
        }

        Button(
            onClick = { start() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text(if (!started) "Start quiz" else "Restart")
        }

        val t = current
        if (started && t != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(18.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "${t.belt.display} belt",
                        color = t.belt.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    )
                    Text(
                        "${t.category.emoji}  ${t.category.display} · #${t.number}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                    )
                    Spacer(Modifier.height(14.dp))
                    if (!revealed) {
                        Button(
                            onClick = { revealed = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Show answer") }
                    } else {
                        Text(
                            notes[t.id] ?: t.defaultNote,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(Modifier.height(12.dp))
                        if (!missed) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                OutlinedButton(
                                    onClick = { onMissed(t.id); answered += 1; missed = true },
                                    modifier = Modifier.weight(1f),
                                ) { Text("Missed") }
                                Button(
                                    onClick = { onCorrect(t.id); correct += 1; answered += 1; draw() },
                                    modifier = Modifier.weight(1f),
                                ) { Text("Got it") }
                            }
                        } else {
                            Text(
                                "No worries — watch it back, then carry on. " +
                                    "This one will come up again soon.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp,
                            )
                            Spacer(Modifier.height(10.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                OutlinedButton(
                                    onClick = { uriHandler.openUri(t.videoUrl) },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Icon(
                                        Icons.Filled.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text("Watch video")
                                }
                                Button(
                                    onClick = { draw() },
                                    modifier = Modifier.weight(1f),
                                ) { Text("Next question") }
                            }
                        }
                    }
                }
            }
            Text(
                "Score: $correct / $answered",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
