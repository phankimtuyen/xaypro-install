# XâyPro — ứng dụng desktop (Kotlin + Compose)

Ứng dụng **native** (cửa sổ app thuần, không WebView) quản lý xây dựng, viết bằng
Kotlin + Jetbrains Compose (Compose Desktop).

## Yêu cầu

- JDK 17+ (dự án dùng JDK 21).

## Chạy ứng dụng

```bash
cd app
./gradlew run
```

Nếu chạy trong môi trường không có GPU (VNC/headless) và gặp lỗi
`Cannot create Linux GL context`, hãy bật chế độ vẽ bằng phần mềm:

```bash
SKIKO_RENDER_API=SOFTWARE ./gradlew run -Dskiko.renderApi=SOFTWARE
```

## Giao diện

Ứng dụng tái hiện **y hệt** trang web `index.html` bằng giao diện native: logo,
tiêu đề, huy hiệu phiên bản, nút **Tải XayPro.apk**, nút **Sao chép link để gửi
Zalo** (có toast phản hồi), thẻ "Cách chắc chắn nhất" và thẻ "Tài khoản demo"
(`hung / 1234`, `bao / 1234`).

## Đóng gói cho Windows (.exe)

`jpackage` chỉ tạo được gói cho đúng hệ điều hành đang chạy, nên để build bản
**Windows từ Linux/macOS** dự án dùng [Conveyor](https://conveyor.hydraulic.dev):

```bash
# Cài Conveyor CLI, rồi:
cd app
./gradlew jar
conveyor -f conveyor.conf make windows-zip --output-dir output
```

Kết quả: `output/xaypro-1.2.0-windows-amd64.zip` — gói **portable** đã kèm sẵn
Java runtime + thư viện Skiko cho Windows. Người dùng chỉ cần giải nén và chạy
`bin/XâyPro.exe` (không cần cài Java).

Ghi chú: `build.gradle.kts` khai báo `windowsAmd64(...skiko-awt-runtime-windows-x64...)`
và một luật metadata để chọn biến thể Skiko `awt`, giúp Conveyor gói được cho Windows.

## Đóng gói cho hệ điều hành hiện tại (tuỳ chọn)

```bash
./gradlew packageDistributionForCurrentOS   # .deb / .dmg / .msi
./gradlew createDistributable               # thư mục chạy trực tiếp
```

## Cấu trúc

- `src/main/kotlin/xaypro/Main.kt` — toàn bộ giao diện trang tải (giống hệt
  `index.html`), gồm cả icon vẽ bằng Canvas và toast thông báo.
