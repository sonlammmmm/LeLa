   # Tài liệu Phân tích API & Database - Lela Flashcard Platform
   *(Tài liệu chi tiết phân tích 31 cấu trúc Entity thực tế đang có trong mã nguồn)*

   ## 1. Cấu trúc Database & Các API Endpoints

   ### Nhóm 1 — Entity độc lập & Cơ sở (Không có FK)

   #### Entity: `Language`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String languageCode` | Mã ngôn ngữ, ví dụ en, ja, vi. |
   | `String name` | Tên ngôn ngữ hiển thị. |
   | `String nativeName` | Tên bản địa của ngôn ngữ. |
   | `String flagUrl` | Đường dẫn biểu tượng cờ. |
   | `Boolean isActive = true` | Trạng thái còn được sử dụng. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/languages` : Lấy danh sách (Phân trang)
   - `GET /api/v1/languages/{id}` : Lấy chi tiết
   - `POST /api/v1/languages` : Tạo mới
   - `PUT /api/v1/languages/{id}` : Cập nhật
   - `DELETE /api/v1/languages/{id}` : Xóa

   #### Entity: `SubscriptionPlan`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String planCode` | Mã gói đăng ký. |
   | `String name` | Tên gói hiển thị. |
   | `String description` | Mô tả quyền lợi của gói. |
   | `BigDecimal price = BigDecimal.ZERO` | Giá tiền của gói. |
   | `String currencyCode = "VND"` | Mã tiền tệ. |
   | `BillingCycle billingCycle = BillingCycle.MONTHLY` | Chu kỳ thanh toán. |
   | `Integer billingIntervalCount = 1` | Số chu kỳ cho mỗi lần tính phí. |
   | `Integer maxOwnedDecks = 3` | Số deck tối đa được sở hữu. |
   | `Integer maxCardsPerDeck = 50` | Số thẻ tối đa trong mỗi deck. |
   | `Integer maxDailyReviews = 100` | Số lượt review tối đa mỗi ngày. |
   | `Boolean quizEnabled = false` | Cho phép dùng tính năng quiz. |
   | `Boolean leaderboardEnabled = false` | Cho phép tham gia leaderboard. |
   | `Boolean offlineEnabled = false` | Cho phép học offline. |
   | `String featuresJson` | Cấu hình tính năng mở rộng dạng JSON. |
   | `Boolean isActive = true` | Trạng thái gói còn bán. |
   | `Integer displayOrder = 0` | Thứ tự hiển thị gói. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/subscriptionplans` : Lấy danh sách (Phân trang)
   - `GET /api/v1/subscriptionplans/{id}` : Lấy chi tiết
   - `POST /api/v1/subscriptionplans` : Tạo mới
   - `PUT /api/v1/subscriptionplans/{id}` : Cập nhật
   - `DELETE /api/v1/subscriptionplans/{id}` : Xóa

   #### Entity: `Role`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `RoleCode roleCode` | Mã vai trò, ví dụ ADMIN hoặc LEARNER. |
   | `String name` | Tên vai trò hiển thị. |
   | `String description` | Mô tả quyền hạn của vai trò. |
   | `Boolean isActive = true` | Trạng thái còn được sử dụng. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/roles` : Lấy danh sách (Phân trang)
   - `GET /api/v1/roles/{id}` : Lấy chi tiết
   - `POST /api/v1/roles` : Tạo mới
   - `PUT /api/v1/roles/{id}` : Cập nhật
   - `DELETE /api/v1/roles/{id}` : Xóa

   #### Entity: `Tag`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String name` | Tên tag hiển thị. |
   | `String slug` | Chuỗi định danh tag dùng trong URL hoặc lọc. |
   | `LocalDateTime createdAt` | Thời điểm tạo tag. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/tags` : Lấy danh sách (Phân trang)
   - `GET /api/v1/tags/{id}` : Lấy chi tiết
   - `POST /api/v1/tags` : Tạo mới
   - `PUT /api/v1/tags/{id}` : Cập nhật
   - `DELETE /api/v1/tags/{id}` : Xóa

   ---

   ### Nhóm 2 — User & Phân quyền

   #### Entity: `Users`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String username` | Tên đăng nhập duy nhất. |
   | `String email` | Email đăng nhập và nhận thông báo. |
   | `String passwordHash` | Mật khẩu đã được mã hóa. |
   | `String fullName` | Họ tên hiển thị của người dùng. |
   | `String avatarUrl` | Đường dẫn ảnh đại diện. |
   | `Language nativeLanguage` | Ngôn ngữ mẹ đẻ của người dùng. |
   | `Language targetLanguage` | Ngôn ngữ người dùng đang học. |
   | `UserStatus status = UserStatus.ACTIVE` | Trạng thái tài khoản. |
   | `String timezone = "Asia/Ho_Chi_Minh"` | Múi giờ dùng để tính streak. |
   | `Integer dailyGoalCards = 10` | Mục tiêu số thẻ học mỗi ngày. |
   | `Long xpTotal = 0L` | Tổng điểm kinh nghiệm đã tích lũy. |
   | `Integer streakCurrent = 0` | Chuỗi ngày học hiện tại. |
   | `Integer streakLongest = 0` | Chuỗi ngày học dài nhất. |
   | `LocalDate lastActivityDate` | Ngày phát sinh hoạt động học gần nhất. |
   | `LocalDateTime lastActiveAt` | Thời điểm hoạt động gần nhất. |
   | `LocalDateTime emailVerifiedAt` | Thời điểm xác thực email. |
   | `LocalDateTime deletedAt` | Thời điểm xóa mềm tài khoản. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/users` : Lấy danh sách (Phân trang)
   - `GET /api/v1/users/{id}` : Lấy chi tiết
   - `POST /api/v1/users` : Tạo mới
   - `PUT /api/v1/users/{id}` : Cập nhật
   - `DELETE /api/v1/users/{id}` : Xóa

   #### Entity: `UserRoleAssignment`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng được gán vai trò. |
   | `Role role` | Vai trò được gán. |
   | `Users assignedBy` | Người thực hiện gán vai trò. |
   | `LocalDateTime createdAt` | Thời điểm gán vai trò. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/userroleassignments` : Lấy danh sách (Phân trang)
   - `GET /api/v1/userroleassignments/{id}` : Lấy chi tiết
   - `POST /api/v1/userroleassignments` : Tạo mới
   - `PUT /api/v1/userroleassignments/{id}` : Cập nhật
   - `DELETE /api/v1/userroleassignments/{id}` : Xóa

   #### Entity: `UserRoleAssignmentId`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Long userId` | ID người dùng. |
   | `Long roleId` | ID vai trò. |

   #### Entity: `RefreshTokenSession`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Chủ sở hữu phiên refresh token. |
   | `String tokenHash` | Hash của refresh token, không lưu token gốc. |
   | `String tokenFamilyId` | Nhóm token dùng để phát hiện tái sử dụng. |
   | `RefreshTokenSession replacedByToken` | Token mới thay thế token hiện tại. |
   | `String deviceName` | Tên thiết bị đăng nhập. |
   | `String ipAddress` | Địa chỉ IP tạo hoặc dùng token. |
   | `String userAgent` | Thông tin trình duyệt hoặc ứng dụng. |
   | `LocalDateTime expiresAt` | Thời điểm hết hạn token. |
   | `LocalDateTime lastUsedAt` | Thời điểm sử dụng token gần nhất. |
   | `LocalDateTime revokedAt` | Thời điểm thu hồi token. |
   | `String revokeReason` | Lý do thu hồi token. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/refreshtokensessions` : Lấy danh sách (Phân trang)
   - `GET /api/v1/refreshtokensessions/{id}` : Lấy chi tiết
   - `POST /api/v1/refreshtokensessions` : Tạo mới
   - `PUT /api/v1/refreshtokensessions/{id}` : Cập nhật
   - `DELETE /api/v1/refreshtokensessions/{id}` : Xóa

   ---

   ### Nhóm 3 — Thanh toán & Gói cước

   #### Entity: `Payment`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng thực hiện thanh toán. |
   | `UserSubscription subscription` | Đăng ký liên quan đến thanh toán. |
   | `String provider` | Nhà cung cấp thanh toán. |
   | `String providerTransactionId` | Mã giao dịch từ nhà cung cấp. |
   | `BigDecimal amount` | Số tiền giao dịch. |
   | `String currencyCode = "VND"` | Mã tiền tệ. |
   | `PaymentStatus status = PaymentStatus.PENDING` | Trạng thái thanh toán. |
   | `LocalDateTime paidAt` | Thời điểm thanh toán thành công. |
   | `LocalDateTime failedAt` | Thời điểm thanh toán thất bại. |
   | `LocalDateTime refundedAt` | Thời điểm hoàn tiền. |
   | `String failureReason` | Lý do thất bại. |
   | `String providerPayload` | Dữ liệu phản hồi gốc từ nhà cung cấp. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/payments` : Lấy danh sách (Phân trang)
   - `GET /api/v1/payments/{id}` : Lấy chi tiết
   - `POST /api/v1/payments` : Tạo mới
   - `PUT /api/v1/payments/{id}` : Cập nhật
   - `DELETE /api/v1/payments/{id}` : Xóa

   #### Entity: `UserSubscription`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng đăng ký gói. |
   | `SubscriptionPlan plan` | Gói đăng ký được sử dụng. |
   | `UserSubscriptionStatus status = UserSubscriptionStatus.ACTIVE` | Trạng thái đăng ký. |
   | `LocalDateTime startedAt` | Thời điểm bắt đầu đăng ký. |
   | `LocalDateTime expiresAt` | Thời điểm hết hạn. |
   | `LocalDateTime trialEndsAt` | Thời điểm kết thúc dùng thử. |
   | `LocalDateTime cancelledAt` | Thời điểm hủy đăng ký. |
   | `Boolean autoRenew = false` | Có tự động gia hạn hay không. |
   | `String provider` | Nhà cung cấp thanh toán. |
   | `String providerSubscriptionId` | Mã đăng ký từ nhà cung cấp. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/usersubscriptions` : Lấy danh sách (Phân trang)
   - `GET /api/v1/usersubscriptions/{id}` : Lấy chi tiết
   - `POST /api/v1/usersubscriptions` : Tạo mới
   - `PUT /api/v1/usersubscriptions/{id}` : Cập nhật
   - `DELETE /api/v1/usersubscriptions/{id}` : Xóa

   ---

   ### Nhóm 4 — Bộ thẻ & Flashcard

   #### Entity: `Deck`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String deckCode` | Mã định danh deck. |
   | `String slug` | Chuỗi URL thân thiện của deck. |
   | `String title` | Tiêu đề deck. |
   | `String description` | Mô tả nội dung deck. |
   | `String coverImageUrl` | Đường dẫn ảnh bìa deck. |
   | `Users owner` | Người sở hữu hoặc tạo deck. |
   | `Language language` | Ngôn ngữ chính của deck. |
   | `String category` | Nhóm chủ đề của deck. |
   | `DeckDifficulty difficulty = DeckDifficulty.BEGINNER` | Độ khó của deck. |
   | `DeckVisibility visibility = DeckVisibility.PUBLIC` | Phạm vi hiển thị deck. |
   | `DeckStatus status = DeckStatus.DRAFT` | Trạng thái kiểm duyệt deck. |
   | `Boolean isFeatured = false` | Deck có được gắn nổi bật hay không. |
   | `Integer totalCards = 0` | Tổng số flashcard trong deck. |
   | `Long viewCount = 0L` | Tổng số lượt xem deck. |
   | `Long enrollmentCount = 0L` | Tổng số lượt đăng ký học deck. |
   | `LocalDateTime submittedAt` | Thời điểm gửi kiểm duyệt. |
   | `Users reviewedBy` | Người kiểm duyệt deck. |
   | `LocalDateTime reviewedAt` | Thời điểm kiểm duyệt. |
   | `String rejectionReason` | Lý do từ chối deck. |
   | `LocalDateTime publishedAt` | Thời điểm xuất bản deck. |
   | `LocalDateTime deletedAt` | Thời điểm xóa mềm deck. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/decks` : Lấy danh sách (Phân trang)
   - `GET /api/v1/decks/{id}` : Lấy chi tiết
   - `POST /api/v1/decks` : Tạo mới
   - `PUT /api/v1/decks/{id}` : Cập nhật
   - `DELETE /api/v1/decks/{id}` : Xóa

   #### Entity: `Flashcard`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Deck deck` | Deck chứa flashcard. |
   | `String frontText` | Nội dung mặt trước của thẻ. |
   | `String backText` | Nội dung mặt sau của thẻ. |
   | `String phonetic` | Phiên âm hoặc cách đọc. |
   | `String exampleText` | Câu ví dụ minh họa. |
   | `String hint` | Gợi ý khi học thẻ. |
   | `String note` | Ghi chú bổ sung cho thẻ. |
   | `String frontImageUrl` | Đường dẫn ảnh mặt trước. |
   | `String backImageUrl` | Đường dẫn ảnh mặt sau. |
   | `String frontAudioUrl` | Đường dẫn âm thanh mặt trước. |
   | `String backAudioUrl` | Đường dẫn âm thanh mặt sau. |
   | `Integer cardOrder = 0` | Thứ tự thẻ trong deck. |
   | `Boolean isActive = true` | Thẻ còn được sử dụng hay không. |
   | `Users createdBy` | Người tạo thẻ. |
   | `Users updatedBy` | Người cập nhật thẻ gần nhất. |
   | `LocalDateTime deletedAt` | Thời điểm xóa mềm thẻ. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/flashcards` : Lấy danh sách (Phân trang)
   - `GET /api/v1/flashcards/{id}` : Lấy chi tiết
   - `POST /api/v1/flashcards` : Tạo mới
   - `PUT /api/v1/flashcards/{id}` : Cập nhật
   - `DELETE /api/v1/flashcards/{id}` : Xóa

   #### Entity: `FlashcardTag`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Flashcard flashcard` | Flashcard được gắn tag. |
   | `Tag tag` | Tag được gắn vào flashcard. |
   | `LocalDateTime createdAt` | Thời điểm gắn tag. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/flashcard-tags` : Lấy danh sách (Phân trang)
   - `GET /api/v1/flashcard-tags/{id}` : Lấy chi tiết
   - `POST /api/v1/flashcard-tags` : Tạo mới
   - `PUT /api/v1/flashcard-tags/{id}` : Cập nhật
   - `DELETE /api/v1/flashcard-tags/{id}` : Xóa

   #### Entity: `FlashcardTagId`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Long flashcardId` | ID flashcard. |
   | `Long tagId` | ID tag. |

   ---

   ### Nhóm 5 — Nội dung bài tập (Quiz)

   #### Entity: `Quiz`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Deck deck` | Deck chứa quiz. |
   | `String quizCode` | Mã định danh quiz. |
   | `String title` | Tiêu đề quiz. |
   | `String description` | Mô tả quiz. |
   | `QuizType quizType = QuizType.MULTIPLE_CHOICE` | Loại quiz mặc định. |
   | `Integer timeLimitSeconds` | Thời gian làm bài tối đa tính bằng giây. |
   | `Integer maxAttempts = 3` | Số lần làm bài tối đa. |
   | `Boolean shuffleQuestions = true` | Có trộn thứ tự câu hỏi hay không. |
   | `Boolean shuffleOptions = true` | Có trộn thứ tự đáp án hay không. |
   | `Integer totalQuestions = 0` | Tổng số câu hỏi trong quiz. |
   | `Boolean isActive = true` | Quiz còn được sử dụng hay không. |
   | `Users createdBy` | Người tạo quiz. |
   | `Users updatedBy` | Người cập nhật quiz gần nhất. |
   | `LocalDateTime deletedAt` | Thời điểm xóa mềm quiz. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quizs` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quizs/{id}` : Lấy chi tiết
   - `POST /api/v1/quizs` : Tạo mới
   - `PUT /api/v1/quizs/{id}` : Cập nhật
   - `DELETE /api/v1/quizs/{id}` : Xóa

   #### Entity: `QuizQuestion`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Quiz quiz` | Quiz chứa câu hỏi. |
   | `Flashcard sourceCard` | Flashcard nguồn để tạo câu hỏi. |
   | `String questionText` | Nội dung câu hỏi. |
   | `String questionImageUrl` | Đường dẫn ảnh minh họa câu hỏi. |
   | `QuestionType questionType` | Loại câu hỏi. |
   | `String explanation` | Giải thích đáp án. |
   | `Integer points = 10` | Số điểm của câu hỏi. |
   | `Integer questionTimeLimitSeconds` | Thời gian tối đa cho câu hỏi. |
   | `Integer displayOrder = 0` | Thứ tự hiển thị câu hỏi. |
   | `Boolean isActive = true` | Câu hỏi còn được sử dụng hay không. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quiz-questions` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quiz-questions/{id}` : Lấy chi tiết
   - `POST /api/v1/quiz-questions` : Tạo mới
   - `PUT /api/v1/quiz-questions/{id}` : Cập nhật
   - `DELETE /api/v1/quiz-questions/{id}` : Xóa

   #### Entity: `QuizQuestionOption`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `QuizQuestion question` | Câu hỏi chứa đáp án này. |
   | `String optionKey` | Ký hiệu đáp án, ví dụ A, B, C. |
   | `String optionText` | Nội dung đáp án. |
   | `String normalizedText` | Nội dung đáp án đã chuẩn hóa để so khớp. |
   | `Boolean isCorrect = false` | Đáp án này có đúng hay không. |
   | `Integer displayOrder = 0` | Thứ tự hiển thị đáp án. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quiz-question-options` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quiz-question-options/{id}` : Lấy chi tiết
   - `POST /api/v1/quiz-question-options` : Tạo mới
   - `PUT /api/v1/quiz-question-options/{id}` : Cập nhật
   - `DELETE /api/v1/quiz-question-options/{id}` : Xóa

   ---

   ### Nhóm 6 — Học tập & Tiến độ (SRS)

   #### Entity: `DeckEnrollment`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng đăng ký học deck. |
   | `Deck deck` | Deck được đăng ký học. |
   | `DeckEnrollmentStatus status = DeckEnrollmentStatus.ACTIVE` | Trạng thái đăng ký học. |
   | `LocalDateTime enrolledAt` | Thời điểm đăng ký học. |
   | `LocalDateTime pausedAt` | Thời điểm tạm dừng học. |
   | `LocalDateTime completedAt` | Thời điểm hoàn thành deck. |
   | `LocalDateTime droppedAt` | Thời điểm bỏ học deck. |
   | `LocalDateTime lastStudiedAt` | Thời điểm học gần nhất. |
   | `LocalDateTime nextReviewAt` | Thời điểm review tiếp theo. |
   | `Integer masteredCards = 0` | Số thẻ đã nắm vững. |
   | `String note` | Ghi chú cá nhân cho enrollment. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/deckenrollments` : Lấy danh sách (Phân trang)
   - `GET /api/v1/deckenrollments/{id}` : Lấy chi tiết
   - `POST /api/v1/deckenrollments` : Tạo mới
   - `PUT /api/v1/deckenrollments/{id}` : Cập nhật
   - `DELETE /api/v1/deckenrollments/{id}` : Xóa

   #### Entity: `CardProgress`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng sở hữu tiến độ học. |
   | `Flashcard card` | Flashcard đang theo dõi tiến độ. |
   | `CardProgressState state = CardProgressState.NEW` | Trạng thái SRS hiện tại của thẻ. |
   | `ReviewableCardState suspendedFromState` | Trạng thái trước khi tạm ngưng thẻ. |
   | `Integer intervalDays = 0` | Khoảng cách review tính theo ngày. |
   | `Integer repetitions = 0` | Số lần lặp lại thành công. |
   | `Integer lapseCount = 0` | Số lần quên hoặc trả lời sai. |
   | `Integer learningStep = 0` | Bước học hiện tại trong pha learning. |
   | `Integer scheduledDays = 0` | Số ngày được lên lịch. |
   | `Integer elapsedDays = 0` | Số ngày đã trôi qua từ lần review trước. |
   | `LocalDateTime dueAt` | Thời điểm thẻ đến hạn review. |
   | `LocalDateTime lastReviewedAt` | Thời điểm review gần nhất. |
   | `Integer lastRating` | Mức đánh giá gần nhất của người học. |
   | `String algorithmVersion = "SM2_V1"` | Phiên bản thuật toán SRS. |
   | `Integer totalReviews = 0` | Tổng số lượt review thẻ. |
   | `Integer correctCount = 0` | Tổng số lượt trả lời đúng. |
   | `Integer againCount = 0` | Số lượt đánh giá Again. |
   | `Integer hardCount = 0` | Số lượt đánh giá Hard. |
   | `Integer goodCount = 0` | Số lượt đánh giá Good. |
   | `Integer easyCount = 0` | Số lượt đánh giá Easy. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/cardprogress` : Lấy danh sách (Phân trang)
   - `GET /api/v1/cardprogress/{id}` : Lấy chi tiết
   - `POST /api/v1/cardprogress` : Tạo mới
   - `PUT /api/v1/cardprogress/{id}` : Cập nhật
   - `DELETE /api/v1/cardprogress/{id}` : Xóa

   #### Entity: `ReviewSession`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String publicId` | ID công khai dùng cho API. |
   | `Users user` | Người dùng thực hiện phiên review. |
   | `Deck deck` | Deck đang được review. |
   | `ReviewSessionType sessionType = ReviewSessionType.REGULAR` | Loại phiên review. |
   | `ReviewSessionStatus status = ReviewSessionStatus.IN_PROGRESS` | Trạng thái phiên review. |
   | `String deviceId` | Thiết bị thực hiện review. |
   | `Boolean offlineMode = false` | Phiên có phát sinh từ chế độ offline hay không. |
   | `LocalDateTime startedAt` | Thời điểm bắt đầu phiên. |
   | `LocalDateTime completedAt` | Thời điểm hoàn thành phiên. |
   | `LocalDateTime abandonedAt` | Thời điểm bỏ dở phiên. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/reviewsessions` : Lấy danh sách (Phân trang)
   - `GET /api/v1/reviewsessions/{id}` : Lấy chi tiết
   - `POST /api/v1/reviewsessions` : Tạo mới
   - `PUT /api/v1/reviewsessions/{id}` : Cập nhật
   - `DELETE /api/v1/reviewsessions/{id}` : Xóa

   #### Entity: `SrsReview`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `ReviewSession reviewSession` | Phiên review chứa sự kiện này. |
   | `Users user` | Người dùng thực hiện review. |
   | `Flashcard card` | Flashcard được review. |
   | `String clientEventId` | ID sự kiện từ client để chống gửi trùng. |
   | `Integer rating` | Mức đánh giá Again, Hard, Good hoặc Easy. |
   | `Integer responseMs` | Thời gian trả lời tính bằng mili giây. |
   | `ReviewableCardState previousState` | Trạng thái SRS trước review. |
   | `ReviewableCardState newState` | Trạng thái SRS sau review. |
   | `BigDecimal easeBefore` | Hệ số dễ nhớ trước review. |
   | `BigDecimal easeAfter` | Hệ số dễ nhớ sau review. |
   | `Integer intervalBefore` | Khoảng cách review trước đó. |
   | `Integer intervalAfter` | Khoảng cách review mới. |
   | `LocalDateTime dueBefore` | Thời điểm đến hạn trước review. |
   | `LocalDateTime dueAfter` | Thời điểm đến hạn mới. |
   | `String algorithmVersion` | Phiên bản thuật toán SRS đã dùng. |
   | `Integer xpAwarded = 0` | XP được cộng cho review này. |
   | `LocalDateTime clientReviewedAt` | Thời điểm review theo client. |
   | `LocalDateTime serverReceivedAt` | Thời điểm server nhận sự kiện. |
   | `LocalDateTime reviewedAt` | Thời điểm review được ghi nhận. |
   | `LocalDateTime createdAt` | Thời điểm tạo bản ghi review. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/srsreviews` : Lấy danh sách (Phân trang)
   - `GET /api/v1/srsreviews/{id}` : Lấy chi tiết
   - `POST /api/v1/srsreviews` : Tạo mới
   - `PUT /api/v1/srsreviews/{id}` : Cập nhật
   - `DELETE /api/v1/srsreviews/{id}` : Xóa

   #### Entity: `DailyLearningActivity`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng có hoạt động học. |
   | `LocalDate activityDate` | Ngày hoạt động học. |
   | `String timezone` | Múi giờ dùng để tính ngày học. |
   | `Integer reviewCount = 0` | Số lượt review trong ngày. |
   | `Integer cardsLearned = 0` | Số thẻ đã học trong ngày. |
   | `Integer quizCount = 0` | Số quiz đã làm trong ngày. |
   | `Integer minutesSpent = 0` | Số phút học trong ngày. |
   | `Integer xpEarned = 0` | XP kiếm được trong ngày. |
   | `Boolean goalMet = false` | Người dùng đã đạt mục tiêu ngày hay chưa. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/dailylearningactivitys` : Lấy danh sách (Phân trang)
   - `GET /api/v1/dailylearningactivitys/{id}` : Lấy chi tiết
   - `POST /api/v1/dailylearningactivitys` : Tạo mới
   - `PUT /api/v1/dailylearningactivitys/{id}` : Cập nhật
   - `DELETE /api/v1/dailylearningactivitys/{id}` : Xóa

   ---

   ### Nhóm 7 — Lượt làm bài kiểm tra (Quiz Attempt)

   #### Entity: `QuizAttempt`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String publicId` | ID công khai dùng cho API. |
   | `Quiz quiz` | Quiz được làm. |
   | `Users user` | Người dùng làm quiz. |
   | `Integer attemptNumber` | Số thứ tự lần làm bài. |
   | `String startIdempotencyKey` | Khóa idempotency khi bắt đầu attempt. |
   | `QuizAttemptStatus status = QuizAttemptStatus.IN_PROGRESS` | Trạng thái lần làm bài. |
   | `LocalDateTime startedAt` | Thời điểm bắt đầu làm bài. |
   | `LocalDateTime expiresAt` | Thời điểm hết hạn làm bài. |
   | `LocalDateTime submittedAt` | Thời điểm nộp bài. |
   | `LocalDateTime abandonedAt` | Thời điểm bỏ dở bài làm. |
   | `Integer timeSpentSeconds` | Tổng thời gian làm bài tính bằng giây. |
   | `Integer totalQuestions = 0` | Tổng số câu hỏi trong attempt. |
   | `Integer correctAnswers = 0` | Số câu trả lời đúng. |
   | `Integer scorePoints = 0` | Tổng điểm đạt được. |
   | `BigDecimal scorePercent` | Điểm phần trăm. |
   | `Boolean passed` | Kết quả đạt hay không đạt. |
   | `Integer xpAwarded = 0` | XP được cộng sau khi nộp bài. |
   | `Long version = 0L` | Phiên bản dùng cho optimistic locking. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quizattempts` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quizattempts/{id}` : Lấy chi tiết
   - `POST /api/v1/quizattempts` : Tạo mới
   - `PUT /api/v1/quizattempts/{id}` : Cập nhật
   - `DELETE /api/v1/quizattempts/{id}` : Xóa

   #### Entity: `QuizAttemptQuestion`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `QuizAttempt attempt` | Lần làm bài chứa câu hỏi snapshot. |
   | `QuizQuestion sourceQuestion` | Câu hỏi nguồn tại thời điểm tạo snapshot. |
   | `String questionText` | Nội dung câu hỏi đã snapshot. |
   | `String questionImageUrl` | Ảnh câu hỏi đã snapshot. |
   | `QuestionType questionType` | Loại câu hỏi đã snapshot. |
   | `String explanation` | Giải thích đáp án đã snapshot. |
   | `Integer points` | Số điểm của câu hỏi trong attempt. |
   | `Integer questionTimeLimitSeconds` | Giới hạn thời gian cho câu hỏi. |
   | `Integer displayOrder` | Thứ tự câu hỏi trong attempt. |
   | `LocalDateTime createdAt` | Thời điểm tạo snapshot câu hỏi. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quiz-attempt-questions` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quiz-attempt-questions/{id}` : Lấy chi tiết
   - `POST /api/v1/quiz-attempt-questions` : Tạo mới
   - `PUT /api/v1/quiz-attempt-questions/{id}` : Cập nhật
   - `DELETE /api/v1/quiz-attempt-questions/{id}` : Xóa

   #### Entity: `QuizAttemptOption`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `QuizAttemptQuestion attemptQuestion` | Câu hỏi snapshot chứa đáp án này. |
   | `QuizQuestionOption sourceOption` | Đáp án nguồn tại thời điểm tạo snapshot. |
   | `String optionKey` | Ký hiệu đáp án đã snapshot. |
   | `String optionText` | Nội dung đáp án đã snapshot. |
   | `String normalizedText` | Nội dung đáp án đã chuẩn hóa. |
   | `Boolean isCorrect = false` | Đáp án snapshot có đúng hay không. |
   | `Integer displayOrder` | Thứ tự đáp án trong câu hỏi. |
   | `LocalDateTime createdAt` | Thời điểm tạo snapshot đáp án. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quiz-attempt-options` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quiz-attempt-options/{id}` : Lấy chi tiết
   - `POST /api/v1/quiz-attempt-options` : Tạo mới
   - `PUT /api/v1/quiz-attempt-options/{id}` : Cập nhật
   - `DELETE /api/v1/quiz-attempt-options/{id}` : Xóa

   #### Entity: `QuizAnswer`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `QuizAttempt attempt` | Lần làm bài chứa câu trả lời. |
   | `QuizAttemptQuestion attemptQuestion` | Câu hỏi được trả lời. |
   | `QuizAttemptOption selectedAttemptOption` | Đáp án được chọn. |
   | `String answerText` | Nội dung trả lời dạng văn bản. |
   | `String normalizedAnswerText` | Nội dung trả lời đã chuẩn hóa. |
   | `Boolean isCorrect` | Câu trả lời đúng hay sai. |
   | `Integer pointsAwarded = 0` | Số điểm được cộng cho câu trả lời. |
   | `Integer timeTakenSeconds` | Thời gian trả lời tính bằng giây. |
   | `LocalDateTime answeredAt` | Thời điểm trả lời. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/quiz-answers` : Lấy danh sách (Phân trang)
   - `GET /api/v1/quiz-answers/{id}` : Lấy chi tiết
   - `POST /api/v1/quiz-answers` : Tạo mới
   - `PUT /api/v1/quiz-answers/{id}` : Cập nhật
   - `DELETE /api/v1/quiz-answers/{id}` : Xóa

   ---

   ### Nhóm 8 — Thống kê, Xếp hạng & Thông báo

   #### Entity: `LeaderboardSnapshot`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng được ghi nhận điểm xếp hạng. |
   | `LeaderboardPeriodType periodType` | Loại kỳ xếp hạng. |
   | `LocalDate periodStart` | Ngày bắt đầu kỳ xếp hạng. |
   | `LocalDate periodEnd` | Ngày kết thúc kỳ xếp hạng. |
   | `Long xpScore = 0L` | Điểm XP dùng để xếp hạng. |
   | `Long quizScore = 0L` | Điểm quiz dùng để xếp hạng. |
   | `Integer streakDays = 0` | Số ngày streak trong kỳ. |
   | `Integer cardsMastered = 0` | Số thẻ đã nắm vững trong kỳ. |
   | `Long totalScore = 0L` | Tổng điểm xếp hạng. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/leaderboardsnapshots` : Lấy danh sách (Phân trang)
   - `GET /api/v1/leaderboardsnapshots/{id}` : Lấy chi tiết
   - `POST /api/v1/leaderboardsnapshots` : Tạo mới
   - `PUT /api/v1/leaderboardsnapshots/{id}` : Cập nhật
   - `DELETE /api/v1/leaderboardsnapshots/{id}` : Xóa

   #### Entity: `Notification`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng nhận thông báo. |
   | `NotificationType type` | Loại thông báo. |
   | `NotificationChannel channel = NotificationChannel.IN_APP` | Kênh gửi thông báo. |
   | `NotificationStatus status = NotificationStatus.PENDING` | Trạng thái gửi thông báo. |
   | `String title` | Tiêu đề thông báo. |
   | `String message` | Nội dung thông báo. |
   | `String actionUrl` | Đường dẫn hành động khi mở thông báo. |
   | `String relatedEntityType` | Loại đối tượng liên quan. |
   | `Long relatedEntityId` | ID đối tượng liên quan. |
   | `String deduplicationKey` | Khóa chống gửi trùng thông báo. |
   | `Boolean isRead = false` | Người dùng đã đọc hay chưa. |
   | `LocalDateTime readAt` | Thời điểm đọc thông báo. |
   | `LocalDateTime scheduledAt` | Thời điểm dự kiến gửi. |
   | `LocalDateTime deliveredAt` | Thời điểm gửi thành công. |
   | `LocalDateTime failedAt` | Thời điểm gửi thất bại. |
   | `String failureReason` | Lý do gửi thất bại. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/notifications` : Lấy danh sách (Phân trang)
   - `GET /api/v1/notifications/{id}` : Lấy chi tiết
   - `POST /api/v1/notifications` : Tạo mới
   - `PUT /api/v1/notifications/{id}` : Cập nhật
   - `DELETE /api/v1/notifications/{id}` : Xóa

   ---

   ### Nhóm 9 — Hệ thống / Cơ sở hạ tầng

   #### Entity: `AuditLog`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users actorUser` | Người thực hiện hành động. |
   | `String action` | Tên hành động được audit. |
   | `String entityType` | Loại entity bị tác động. |
   | `String entityId` | ID entity bị tác động. |
   | `String beforeData` | Dữ liệu trước khi thay đổi. |
   | `String afterData` | Dữ liệu sau khi thay đổi. |
   | `String ipAddress` | Địa chỉ IP thực hiện hành động. |
   | `String userAgent` | Trình duyệt hoặc ứng dụng thực hiện hành động. |
   | `String requestId` | ID request để truy vết log. |
   | `LocalDateTime createdAt` | Thời điểm ghi audit log. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/auditlogs` : Lấy danh sách (Phân trang)
   - `GET /api/v1/auditlogs/{id}` : Lấy chi tiết
   - `POST /api/v1/auditlogs` : Tạo mới
   - `PUT /api/v1/auditlogs/{id}` : Cập nhật
   - `DELETE /api/v1/auditlogs/{id}` : Xóa

   #### Entity: `OutboxEvent`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `String aggregateType` | Loại aggregate phát sinh sự kiện. |
   | `String aggregateId` | ID aggregate phát sinh sự kiện. |
   | `String eventType` | Loại sự kiện cần phát ra. |
   | `String payload` | Nội dung sự kiện dạng JSON. |
   | `OutboxEventStatus status = OutboxEventStatus.PENDING` | Trạng thái xử lý sự kiện. |
   | `LocalDateTime availableAt` | Thời điểm sự kiện sẵn sàng xử lý. |
   | `LocalDateTime processedAt` | Thời điểm xử lý xong sự kiện. |
   | `Integer retryCount = 0` | Số lần thử xử lý lại. |
   | `String lastError` | Lỗi gần nhất khi xử lý sự kiện. |
   | `LocalDateTime createdAt` | Thời điểm tạo sự kiện. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/outboxevents` : Lấy danh sách (Phân trang)
   - `GET /api/v1/outboxevents/{id}` : Lấy chi tiết
   - `POST /api/v1/outboxevents` : Tạo mới
   - `PUT /api/v1/outboxevents/{id}` : Cập nhật
   - `DELETE /api/v1/outboxevents/{id}` : Xóa

   #### Entity: `IdempotencyRecord`
   | Tên trường | Kiểu dữ liệu / Chú thích |
   |---|---|
   | `Users user` | Người dùng gửi request. |
   | `String scope` | Phạm vi áp dụng idempotency. |
   | `String idempotencyKey` | Khóa idempotency từ client. |
   | `String requestHash` | Hash nội dung request. |
   | `IdempotencyProcessingStatus processingStatus = IdempotencyProcessingStatus.PROCESSING` | Trạng thái xử lý request. |
   | `Integer responseStatusCode` | HTTP status code đã trả về. |
   | `String responseBody` | Body phản hồi đã lưu lại. |
   | `LocalDateTime lockedUntil` | Thời điểm hết khóa xử lý. |
   | `LocalDateTime expiresAt` | Thời điểm bản ghi idempotency hết hạn. |

   **API Endpoints dự kiến:**
   - `GET /api/v1/idempotencyrecords` : Lấy danh sách (Phân trang)
   - `GET /api/v1/idempotencyrecords/{id}` : Lấy chi tiết
   - `POST /api/v1/idempotencyrecords` : Tạo mới
   - `PUT /api/v1/idempotencyrecords/{id}` : Cập nhật
   - `DELETE /api/v1/idempotencyrecords/{id}` : Xóa

   ---

   ## 2. Bảng phân quyền (RBAC)

   | Chức năng | ADMIN | CONTENT_CREATOR | LEARNER | MODERATOR |
   | :--- | :---: | :---: | :---: | :---: |
   | **Quản lý ngôn ngữ, Gói đăng ký** | ✅ | ❌ | ❌ | ❌ |
   | **Thanh toán & Subscriptions** | ✅ | ❌ | ✅ (Của mình) | ❌ |
   | **Tạo/Sửa Bộ thẻ (Deck & Flashcard)**| ✅ | ✅ (Của mình) | ❌ | ✅ (Chỉ duyệt/ẩn) |
   | **Tạo bài kiểm tra (Quiz)** | ✅ | ✅ (Của mình) | ❌ | ❌ |
   | **Đăng ký (Enroll) & Học (SRS Review)**| ✅ | ✅ | ✅ | ❌ |
   | **Làm bài kiểm tra (Quiz Attempt)** | ✅ | ✅ | ✅ | ❌ |
   | **Xem Leaderboard & Notification** | ✅ | ✅ | ✅ | ✅ |
   | **Quản lý User & Xem Log, Hạ tầng** | ✅ | ❌ | ❌ | ❌ |

   ## 3. Ghi chú Kỹ thuật (Technical Notes)
   1. **Thuật toán SRS (Spaced Repetition System)**: Dựa vào bảng `SrsReview` và `CardProgress`. Mức đánh giá sẽ cập nhật `next_review_at` theo các thuộc tính `intervalDays` và `repetitions`.
   2. **DailyLearningActivity**: Hoạt động học tập thay thế cho bảng LearningStreak, lưu chi tiết số phút học, điểm XP kiếm được theo từng ngày.
   3. **Cơ sở hạ tầng & Outbox Pattern**: Đã áp dụng `OutboxEvent` để gửi thông báo/sự kiện bất đồng bộ và `IdempotencyRecord` để đảm bảo không bị trừ tiền hoặc lỗi giao dịch lặp.
   4. **Quiz & Attempts**: Chức năng kiểm tra bao gồm cả việc snapshot câu hỏi (`QuizAttemptQuestion`, `QuizAttemptOption`) giúp lịch sử làm bài không bị thay đổi dù câu hỏi gốc bị sửa đổi.
