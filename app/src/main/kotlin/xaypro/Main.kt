package xaypro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

private val BrandBlue = Color(0xFF2E75B6)
private val BrandDark = Color(0xFF245E93)
private val BrandSoft = Color(0xFFE8EEF4)

private data class Account(val username: String, val password: String, val fullName: String)

private val ACCOUNTS = listOf(
    Account("hung", "1234", "Nguyễn Văn Hùng"),
    Account("bao", "1234", "Trần Quốc Bảo"),
)

private fun authenticate(username: String, password: String): Account? =
    ACCOUNTS.firstOrNull { it.username == username.trim() && it.password == password }

private enum class ProjectStatus(val label: String, val color: Color) {
    ON_TRACK("Đúng tiến độ", Color(0xFF1F8A54)),
    DELAYED("Chậm tiến độ", Color(0xFFCC8A00)),
    DONE("Hoàn thành", Color(0xFF2E75B6)),
}

private data class Project(
    val name: String,
    val location: String,
    val progress: Float,
    val status: ProjectStatus,
)

private val SAMPLE_PROJECTS = listOf(
    Project("Nhà phố 3 tầng — Anh Minh", "Quận 7, TP.HCM", 0.85f, ProjectStatus.ON_TRACK),
    Project("Biệt thự sân vườn", "Thủ Đức, TP.HCM", 0.45f, ProjectStatus.DELAYED),
    Project("Sửa chữa văn phòng ACB", "Quận 1, TP.HCM", 1.0f, ProjectStatus.DONE),
    Project("Kho xưởng KCN Tân Bình", "Bình Tân, TP.HCM", 0.30f, ProjectStatus.ON_TRACK),
)

fun main() = application {
    val windowState = rememberWindowState(width = 1000.dp, height = 720.dp)
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "XâyPro — Quản lý xây dựng",
    ) {
        XayProTheme {
            var currentUser by remember { mutableStateOf<Account?>(null) }
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                val user = currentUser
                if (user == null) {
                    LoginScreen(onLoggedIn = { currentUser = it })
                } else {
                    MainScreen(user = user, onLogout = { currentUser = null })
                }
            }
        }
    }
}

@Composable
private fun XayProTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = BrandBlue,
        onPrimary = Color.White,
        primaryContainer = BrandSoft,
        onPrimaryContainer = BrandDark,
        background = Color(0xFFEEF3F9),
        surface = Color.White,
        onSurface = Color(0xFF1A2430),
    )
    MaterialTheme(colorScheme = colors, content = content)
}

@Composable
private fun BrandLogo(size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size / 3.2).dp))
            .background(Brush.linearGradient(listOf(BrandBlue, BrandDark))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "XâyPro",
            tint = Color.White,
            modifier = Modifier.size((size * 0.58).dp),
        )
    }
}

@Composable
private fun LoginScreen(onLoggedIn: (Account) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun submit() {
        val acc = authenticate(username, password)
        if (acc != null) {
            error = null
            onLoggedIn(acc)
        } else {
            error = "Sai tài khoản hoặc mật khẩu."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEEF3F9), Color(0xFFDCE8F5)))),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.width(400.dp).padding(24.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BrandLogo(64)
                Spacer(Modifier.height(14.dp))
                Text("XâyPro", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A2430))
                Text(
                    "Quản lý xây dựng • v1.2.0",
                    fontSize = 13.sp,
                    color = BrandDark,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(22.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; error = null },
                    label = { Text("Tài khoản") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = null },
                    label = { Text("Mật khẩu") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showPassword) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    modifier = Modifier.fillMaxWidth(),
                )

                AnimatedVisibility(visible = error != null) {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    )
                }

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { submit() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Filled.Login, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(16.dp))
                Divider(color = BrandSoft)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Tài khoản demo: hung / 1234  •  bao / 1234",
                    fontSize = 12.sp,
                    color = Color(0xFF5B6775),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(user: Account, onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BrandLogo(34)
                        Spacer(Modifier.width(10.dp))
                        Text("XâyPro", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    Text(
                        "Xin chào, ${user.fullName}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                    OutlinedButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Đăng xuất")
                    }
                    Spacer(Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1A2430),
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Text("Tổng quan công trình", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "Theo dõi tiến độ các công trình đang thi công.",
                color = Color(0xFF5B6775),
            )
            Spacer(Modifier.height(18.dp))

            SummaryRow(SAMPLE_PROJECTS)
            Spacer(Modifier.height(22.dp))

            Text("Danh sách công trình", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            SAMPLE_PROJECTS.forEach { project ->
                ProjectCard(project)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SummaryRow(projects: List<Project>) {
    val total = projects.size
    val done = projects.count { it.status == ProjectStatus.DONE }
    val delayed = projects.count { it.status == ProjectStatus.DELAYED }
    val avg = if (projects.isEmpty()) 0 else (projects.map { it.progress }.average() * 100).toInt()

    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        StatCard(Icons.Filled.Apartment, "Công trình", total.toString(), Modifier.weight(1f))
        StatCard(Icons.Filled.CheckCircle, "Hoàn thành", done.toString(), Modifier.weight(1f))
        StatCard(Icons.Filled.Warning, "Chậm tiến độ", delayed.toString(), Modifier.weight(1f))
        StatCard(Icons.Filled.Timeline, "Tiến độ TB", "$avg%", Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(BrandSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = BrandDark, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A2430))
            Text(label, fontSize = 13.sp, color = Color(0xFF5B6775))
        }
    }
}

@Composable
private fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(project.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A2430))
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF5B6775),
                            modifier = Modifier.size(15.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(project.location, fontSize = 13.sp, color = Color(0xFF5B6775))
                    }
                }
                StatusChip(project.status)
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = project.progress,
                    modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = project.status.color,
                    trackColor = BrandSoft,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "${(project.progress * 100).toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A2430),
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: ProjectStatus) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(status.color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(status.label, color = status.color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
