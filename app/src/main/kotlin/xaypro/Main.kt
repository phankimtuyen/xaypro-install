package xaypro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

// --- Bảng màu khớp với index.html ---
private val Bg = Color(0xFFEEF3F9)
private val Bg2 = Color(0xFFDCE8F5)
private val Ink = Color(0xFF1A2430)
private val Muted = Color(0xFF5B6775)
private val Line = Color(0xFFD5DEE8)
private val Brand = Color(0xFF2E75B6)
private val BrandDark = Color(0xFF245E93)
private val BrandSoft = Color(0xFFE8EEF4)

// Đường dẫn file APK (đổi sang link thật khi cần).
private const val APK_URL = "XayPro.apk"

fun main() = application {
    val windowState = rememberWindowState(width = 480.dp, height = 860.dp)
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Tải XâyPro — Ứng dụng Android",
    ) {
        MaterialTheme(colorScheme = lightColorScheme(primary = Brand, background = Bg, surface = Color.White)) {
            DownloadPage()
        }
    }
}

@Composable
private fun DownloadPage() {
    var toast by remember { mutableStateOf<String?>(null) }
    var toastKey by remember { mutableStateOf(0) }

    LaunchedEffect(toastKey) {
        if (toast != null) {
            delay(2200)
            toast = null
        }
    }

    fun showToast(msg: String) {
        toast = msg
        toastKey++
    }

    fun copyLink() {
        runCatching {
            Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(APK_URL), null)
        }
        showToast("Đã sao chép link!")
    }

    fun downloadApk() {
        showToast("Đang tải XayPro.apk…")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Bg, Bg2))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier = Modifier.widthIn(max = 440.dp).fillMaxWidth()) {
                BrandHeader()
                LeadText()
                DownloadCard(onDownload = ::downloadApk, onCopy = ::copyLink)
                StepsCard()
                DemoAccountsCard()
                Spacer(Modifier.height(22.dp))
                Text(
                    "XâyPro © 2026 — Bản Android native Kotlin/Compose",
                    color = Muted,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
            }
        }

        AnimatedVisibility(
            visible = toast != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
        ) {
            Surface(color = Ink, shape = RoundedCornerShape(999.dp), shadowElevation = 8.dp) {
                Text(
                    toast ?: "",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun BrandHeader() {
    Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Brand, BrandDark))),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.size(32.dp)) { drawHouse() }
        }
        Column {
            Text("XâyPro", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(BrandSoft)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text("Android • v1.2.0", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDark)
            }
        }
    }
}

@Composable
private fun LeadText() {
    val text = buildAnnotatedString {
        append("Ứng dụng quản lý xây dựng cho Android (Kotlin/Compose). Tải file cài đặt ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Ink)) { append("APK") }
        append(" bên dưới. Nếu điện thoại chặn tải trong app Grok, hãy mở trang này bằng ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Ink)) { append("Chrome") }
        append(" hoặc tải trên máy tính rồi gửi qua Zalo.")
    }
    Text(text, color = Muted, fontSize = 15.sp, lineHeight = 23.sp, modifier = Modifier.padding(top = 12.dp))
}

@Composable
private fun Card(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Line),
        shadowElevation = 3.dp,
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

@Composable
private fun DownloadCard(onDownload: () -> Unit, onCopy: () -> Unit) {
    Card {
        PrimaryButton(text = "Tải XayPro.apk", onClick = onDownload)
        Spacer(Modifier.height(10.dp))
        SecondaryButton(text = "Sao chép link để gửi Zalo", onClick = onCopy)
        Spacer(Modifier.height(10.dp))
        Text(
            "Sau khi tải xong, mở file → nếu máy hỏi thì bật “Cho phép cài từ nguồn này”.",
            color = Muted,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Brand)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Canvas(Modifier.size(22.dp)) { drawDownload(Color.White) }
            Text(text, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SecondaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BrandSoft)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Canvas(Modifier.size(22.dp)) { drawCopy(Ink, BrandSoft) }
            Text(text, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StepsCard() {
    Card {
        Text("Cách chắc chắn nhất", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(10.dp))
        val steps = listOf(
            buildAnnotatedString {
                append("Mở cuộc chat XâyPro trên "); bold("máy tính"); append(" (Chrome).")
            },
            buildAnnotatedString { append("Bấm "); bold("Tải XayPro.apk"); append(" ở trên.") },
            buildAnnotatedString { append("Gửi file qua "); bold("Zalo"); append(" cho điện thoại này.") },
            buildAnnotatedString { append("Mở file trên điện thoại → "); bold("Cài đặt"); append(".") },
            buildAnnotatedString { append("Đăng nhập bằng tài khoản demo bên dưới.") },
        )
        steps.forEachIndexed { i, s ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text("${i + 1}.", color = Muted, fontSize = 15.sp, modifier = Modifier.width(22.dp))
                Text(s, color = Muted, fontSize = 15.sp, lineHeight = 22.sp)
            }
        }
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.bold(t: String) =
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Ink)) { append(t) }

@Composable
private fun DemoAccountsCard() {
    Card {
        Text("Tài khoản demo", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CredBox("hung", Modifier.weight(1f))
            CredBox("bao", Modifier.weight(1f))
        }
    }
}

@Composable
private fun CredBox(user: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BrandSoft)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Column {
            Text(user, fontWeight = FontWeight.Bold, color = Ink, fontSize = 15.sp)
            Text("Mật khẩu: 1234", color = Muted, fontSize = 14.sp)
        }
    }
}

// --- Vẽ icon bằng Canvas để khớp SVG của trang web ---

private fun DrawScope.strokeOf(w: Float) = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round)

private fun DrawScope.drawHouse() {
    val s = size.minDimension / 24f
    val st = strokeOf(2f * s)
    drawLine(Color.White, Offset(3 * s, 20 * s), Offset(21 * s, 20 * s), strokeWidth = 2 * s, cap = StrokeCap.Round)
    drawPath(Path().apply {
        moveTo(5 * s, 20 * s); lineTo(5 * s, 9 * s); lineTo(12 * s, 4 * s); lineTo(19 * s, 9 * s); lineTo(19 * s, 20 * s)
    }, Color.White, style = st)
    drawPath(Path().apply {
        moveTo(10 * s, 20 * s); lineTo(10 * s, 15 * s); lineTo(14 * s, 15 * s); lineTo(14 * s, 20 * s)
    }, Color.White, style = st)
}

private fun DrawScope.drawDownload(color: Color) {
    val s = size.minDimension / 24f
    val st = strokeOf(2f * s)
    drawLine(color, Offset(12 * s, 3 * s), Offset(12 * s, 15 * s), strokeWidth = 2 * s, cap = StrokeCap.Round)
    drawPath(Path().apply { moveTo(7 * s, 11 * s); lineTo(12 * s, 16 * s); lineTo(17 * s, 11 * s) }, color, style = st)
    drawLine(color, Offset(4 * s, 19 * s), Offset(20 * s, 19 * s), strokeWidth = 2 * s, cap = StrokeCap.Round)
}

private fun DrawScope.drawCopy(color: Color, bg: Color) {
    val s = size.minDimension / 24f
    val st = Stroke(width = 2f * s, join = StrokeJoin.Round)
    drawRoundRect(color, topLeft = Offset(3 * s, 3 * s), size = Size(11 * s, 11 * s), cornerRadius = CornerRadius(2 * s, 2 * s), style = st)
    drawRoundRect(bg, topLeft = Offset(9 * s, 9 * s), size = Size(12 * s, 12 * s), cornerRadius = CornerRadius(2 * s, 2 * s))
    drawRoundRect(color, topLeft = Offset(9 * s, 9 * s), size = Size(11 * s, 11 * s), cornerRadius = CornerRadius(2 * s, 2 * s), style = st)
}
