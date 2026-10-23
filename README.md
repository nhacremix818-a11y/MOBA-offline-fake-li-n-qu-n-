# Liên Quân Offline 3D - Android MOBA Game

Game MOBA Offline đấu trường 3D góc nhìn ngang phong cách Liên Quân Mobile (Arena of Valor) với đầy đủ 20 vị tướng đại diện 6 vai trò, kỹ năng, trang bị và cơ chế chiến đấu 5v5, 1v1, Phòng tập luyện.

---

## 📦 Các tệp cần thiết để Build APK trên GitHub

Dự án đã được tích hợp sẵn toàn bộ công cụ và cấu hình CI/CD để GitHub tự động biên dịch file `.apk`:

1. **`.github/workflows/build-apk.yml`**: Kịch bản GitHub Actions tự động build APK khi bạn đẩy code lên repository hoặc nhấn "Run workflow" thủ công.
2. **`gradlew` & `gradlew.bat`**: Gradle Wrapper script trên Linux/macOS và Windows để GitHub runner chạy lệnh build mà không cần cài đặt trước Gradle.
3. **`gradle/wrapper/gradle-wrapper.jar` & `gradle-wrapper.properties`**: Thư viện Wrapper phiên bản chuẩn tương thích dự án.
4. **`debug.keystore.base64`**: Khóa ký bản dựng Debug được mã hóa sẵn, kịch bản CI sẽ tự động giải mã để ký file APK hợp lệ cho Android cài đặt ngay.
5. **`.env.example`**: Tệp mẫu biến môi trường phục vụ Secrets Gradle Plugin.

---

## 🚀 Hướng dẫn đẩy lên GitHub và lấy file APK tự động

### Bước 1: Khởi tạo và Đẩy mã nguồn lên GitHub
Nếu bạn xuất mã nguồn thành file ZIP hoặc clone về máy tính:

```bash
git init
git add .
git commit -m "Initial commit - Lien Quan Offline 3D MOBA"
git branch -M main
git remote add origin https://github.com/TÊN_TÀI_KHOẢN/TÊN_REPO.git
git push -u origin main
```

*(Hoặc sử dụng tính năng **Export / Push to GitHub** trực tiếp từ thanh cài đặt của Google AI Studio)*.

---

### Bước 2: Tải file APK từ GitHub Actions
1. Trên trang GitHub repository của bạn, nhấn vào tab **Actions**.
2. Bạn sẽ thấy quy trình **Build Android APK** đang tự động chạy.
3. Sau khi tiến trình hoàn thành (dấu tích xanh ✅ ~2-3 phút), nhấn vào lần chạy đó.
4. Kéo xuống mục **Artifacts** ở dưới cùng:
   - Nhấn tải tệp **`LienQuanOffline-APK`** (tệp `.zip`).
5. Giải nén tệp tải về bạn sẽ có file `app-debug.apk` để cài đặt trực tiếp lên điện thoại Android hoặc gửi cho bạn bè!

---

### 💻 Hướng dẫn Build APK thủ công trên máy tính (Tùy chọn)

Nếu bạn muốn tự build trên máy tính cá nhân:

#### Yêu cầu:
- Đã cài đặt **JDK 17** hoặc **JDK 21**.
- Android SDK (hoặc đã cài sẵn Android Studio).

#### Lệnh build qua Terminal / Command Prompt:

**Trên Linux / macOS:**
```bash
cp .env.example .env
base64 -d debug.keystore.base64 > debug.keystore
chmod +x gradlew
./gradlew assembleDebug
```

**Trên Windows (PowerShell):**
```powershell
Copy-Item .env.example .env
[System.IO.File]::WriteAllBytes("debug.keystore", [System.Convert]::FromBase64String((Get-Content debug.keystore.base64 -Raw)))
.\gradlew.bat assembleDebug
```

File APK sau khi build xong sẽ nằm tại:
`app/build/outputs/apk/debug/app-debug.apk`
