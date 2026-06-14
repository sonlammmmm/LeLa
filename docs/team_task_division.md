# Kế hoạch chia Task cho 4 thành viên (Làm việc song song, giảm thiểu block)

Dựa trên cấu trúc 9 nhóm chức năng trong `api_architecture.md`, vấn đề lớn nhất khiến các thành viên phải "đợi nhau" là **Sự phụ thuộc về Dữ liệu (Foreign Keys)**. Ví dụ: Để làm `Quiz` cần có `Deck`, để làm `DeckEnrollment` cần có `Users` và `Deck`.

Để 4 người làm việc song song hiệu quả nhất, chúng ta cần áp dụng chiến lược **API First** & **Chia Phase** kết hợp với **Phân chia theo Domain (Vertical Slicing)**.

---

## 🚀 Chiến lược để không phải đợi nhau

1. **Phase 0 (1-2 ngày đầu - Cả team cùng làm):**
   - **Chốt API Contract:** Viết file Swagger/OpenAPI hoặc Postman collection cho TẤT CẢ API.
   - **Tạo Entity & Mock Data:** 1 người chịu trách nhiệm gen toàn bộ 31 Entity class (chỉ khai báo field và relationship, không viết logic) và push lên nhánh `main`. 
   - *Kết quả:* Mọi người đều có đủ class `Users`, `Deck`, `Quiz`... để reference mà không bị lỗi biên dịch.

2. **Giao tiếp qua Event / Interface thay vì gọi trực tiếp (Loosely Coupled):**
   - Khi `Quiz` hoàn thành, thay vì gọi trực tiếp hàm cộng điểm, bắn ra `QuizCompletedEvent`. Thành viên làm Notification/Leaderboard chỉ cần lắng nghe event này.

---

## 👥 Phân chia Task cho 4 Thành viên

### 🧑‍💻 Thành viên 1: Core Foundation & Identity (Nền tảng & User)
Đảm nhận các tính năng cốt lõi về người dùng, phân quyền và dòng tiền.

* **Nhóm 1:** Language, Role, Tag (Cơ sở)
* **Nhóm 2:** Users, UserRoleAssignment, RefreshTokenSession (Auth, JWT, Đăng nhập/Đăng ký)
* **Nhóm 3:** SubscriptionPlan, Payment, UserSubscription (Thanh toán, nâng cấp tài khoản)
* **Không bị block vì:** Đây là các module gốc, không phụ thuộc vào các module khác. Có thể bắt đầu code ngay lập tức.

### 🧑‍💻 Thành viên 2: Content Creator Domain (Quản lý Nội dung)
Đảm nhận hệ thống tạo và quản lý nội dung học tập của Giảng viên / Người dùng.

* **Nhóm 4:** Deck, Flashcard, FlashcardTag (Bộ thẻ và thẻ)
* **Nhóm 5:** Quiz, QuizQuestion, QuizQuestionOption (Bộ câu hỏi)
* **Không bị block vì:** Mặc dù phụ thuộc vào `Users` (owner), nhưng Thành viên 2 chỉ cần dùng `userId` (Long) để lưu trữ. Khi test, tự insert 1 record User ảo vào DB (Mock data). Không cần đợi Thành viên 1 code xong UI/Logic đăng nhập.

### 🧑‍💻 Thành viên 3: Learner Experience & SRS (Trải nghiệm học & Thuật toán)
Đảm nhận logic phức tạp nhất: Thuật toán lặp lại ngắt quãng (SRS) và làm bài kiểm tra.

* **Nhóm 6:** DeckEnrollment, CardProgress, ReviewSession, SrsReview, DailyLearningActivity (Học và tính toán ngày review tiếp theo)
* **Nhóm 7:** QuizAttempt, QuizAttemptQuestion, QuizAttemptOption, QuizAnswer (Logic Snapshot câu hỏi, chấm điểm làm bài)
* **Không bị block vì:** Chỉ cần Entity `Deck`, `Flashcard`, `Quiz` đã có ở Phase 0. Thành viên 3 tự viết script tạo sẵn vài Deck và Quiz mẫu trong DB để test logic SRS và Chấm điểm. Mọi tính toán diễn ra độc lập với cách tạo Deck ở Thành viên 2.

### 🧑‍💻 Thành viên 4: System, Engagement & Background Jobs (Hệ thống & Tương tác)
Đảm nhận các service chạy ngầm, thống kê, hệ thống thông báo và logs.

* **Nhóm 8:** LeaderboardSnapshot, Notification (Xếp hạng, Gửi email/in-app notification)
* **Nhóm 9:** AuditLog, OutboxEvent, IdempotencyRecord (Ghi log, xử lý sự kiện bất đồng bộ)
* **Không bị block vì:** 
  - Thành viên 4 sẽ thiết kế kiến trúc Event-Driven (ví dụ Kafka/RabbitMQ hoặc Spring ApplicationEvent). 
  - Thay vì đợi các module kia hoàn thiện, Thành viên 4 tự viết các *Publisher giả (Mock Publisher)* để bắn event ảo (`UserRegisteredEvent`, `DeckReviewedEvent`) để test hệ thống Ghi log, Xếp hạng và Thông báo.

---

## 🔄 Quy trình làm việc hàng ngày (Daily Workflow)

1. **Pull nhánh `main`:** Để lấy các Entity / Interface mới nhất do team cập nhật.
2. **Dùng Mock Dữ liệu:** Nếu cần test api `/api/v1/decks` mà chưa có User, hãy dùng 1 user cứng `ID = 1`.
3. **Push Code theo từng PR nhỏ (Từng Entity):** Viết xong CRUD của Entity nào, review và merge ngay Entity đó để người khác có thể sử dụng service (ví dụ `DeckService`).

Bằng cách phân chia theo **Lĩnh vực (Domain)** như trên, mỗi người sẽ ôm trọn từ Database -> Repository -> Service -> Controller cho phần của mình, tỷ lệ merge conflict file sẽ giảm xuống gần 0%.
