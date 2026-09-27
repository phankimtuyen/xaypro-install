# XâyPro — trang cài đặt

Trang tải & hướng dẫn cài đặt ứng dụng **XâyPro** (Android native Kotlin/Compose, v1.2.0).

App Grok trên điện thoại không lưu được APK, nên dùng trang này: mở bằng Chrome để tải trực tiếp, hoặc tải trên máy tính rồi gửi qua Zalo sang điện thoại.

## Nội dung

- `index.html` — trang tải một file, không cần build. Gồm:
  - Nút **Tải XayPro.apk** (tải trực tiếp file APK).
  - Nút **Sao chép link để gửi Zalo**.
  - Hướng dẫn cài đặt từng bước.
  - Tài khoản demo: `hung / 1234` hoặc `bao / 1234`.

## Thêm file APK

Nút tải trỏ tới đường dẫn tương đối `XayPro.apk`. Đặt file APK cùng thư mục với `index.html`:

```
/index.html
/XayPro.apk   ← đặt bản build APK ở đây
```

Muốn dùng link khác (ví dụ GitHub Releases), sửa thuộc tính `href` của nút `#download` trong `index.html`.

## Chạy thử

Không cần build, chỉ cần một static server:

```bash
python3 -m http.server 8000
# mở http://localhost:8000
```

Môi trường Cloud Agent đã cấu hình sẵn server này (xem `.cursor/environment.json`).
