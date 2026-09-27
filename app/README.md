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

## Đóng gói (tuỳ chọn)

Tạo bản cài đặt native cho hệ điều hành hiện tại:

```bash
./gradlew packageDistributionForCurrentOS   # .deb / .dmg / .msi
./gradlew packageAppImage                    # thư mục chạy trực tiếp
```

## Cấu trúc

- `src/main/kotlin/xaypro/Main.kt` — toàn bộ giao diện trang tải (giống hệt
  `index.html`), gồm cả icon vẽ bằng Canvas và toast thông báo.
