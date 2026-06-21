# LeLa Quiz APIs Documentation

## Quiz APIs

### Get All Quizs
- **Endpoint:** `GET http://localhost:8080/api/quiz`

### Get Quiz by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz/1`

### Create Quiz
- **Endpoint:** `POST http://localhost:8080/api/quiz`
- **Body:**
```json
{
  "deckId": 100,
  "quizCode": "string_value",
  "title": "string_value",
  "description": "string_value",
  "quizType": "sample_QuizType",
  "timeLimitSeconds": 1,
  "maxAttempts": 1,
  "shuffleQuestions": true,
  "shuffleOptions": true,
  "isActive": true
}
```

### Update Quiz
- **Endpoint:** `PUT http://localhost:8080/api/quiz/1`
- **Body:**
```json
{
  "deckId": 100,
  "quizCode": "string_value",
  "title": "string_value",
  "description": "string_value",
  "quizType": "sample_QuizType",
  "timeLimitSeconds": 1,
  "maxAttempts": 1,
  "shuffleQuestions": true,
  "shuffleOptions": true,
  "isActive": true
}
```

### Delete Quiz
- **Endpoint:** `DELETE http://localhost:8080/api/quiz/1`

## QuizAnswer APIs

### Get All QuizAnswers
- **Endpoint:** `GET http://localhost:8080/api/quiz-answer`

### Get QuizAnswer by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-answer/1`

### Create QuizAnswer
- **Endpoint:** `POST http://localhost:8080/api/quiz-answer`
- **Body:**
```json
{
  "answerText": "string_value",
  "normalizedAnswerText": "string_value",
  "isCorrect": true,
  "pointsAwarded": 1,
  "timeTakenSeconds": 1,
  "answeredAt": "2026-06-20T10:00:00"
}
```

### Update QuizAnswer
- **Endpoint:** `PUT http://localhost:8080/api/quiz-answer/1`
- **Body:**
```json
{
  "answerText": "string_value",
  "normalizedAnswerText": "string_value",
  "isCorrect": true,
  "pointsAwarded": 1,
  "timeTakenSeconds": 1,
  "answeredAt": "2026-06-20T10:00:00"
}
```

### Delete QuizAnswer
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-answer/1`

## QuizAttempt APIs

### Get All QuizAttempts
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt`

### Get QuizAttempt by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt/1`

### Create QuizAttempt
- **Endpoint:** `POST http://localhost:8080/api/quiz-attempt`
- **Body:**
```json
{
  "quizPublicId": "string_value",
  "startIdempotencyKey": "string_value"
}
```

### Update QuizAttempt
- **Endpoint:** `PUT http://localhost:8080/api/quiz-attempt/1`
- **Body:**
```json
{
  "quizPublicId": "string_value",
  "startIdempotencyKey": "string_value"
}
```

### Delete QuizAttempt
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-attempt/1`

## QuizAttemptOption APIs

### Get All QuizAttemptOptions
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt-option`

### Get QuizAttemptOption by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt-option/1`

### Create QuizAttemptOption
- **Endpoint:** `POST http://localhost:8080/api/quiz-attempt-option`
- **Body:**
```json
{
  "optionKey": "string_value",
  "optionText": "string_value",
  "normalizedText": "string_value",
  "isCorrect": true,
  "displayOrder": 1,
  "createdAt": "2026-06-20T10:00:00"
}
```

### Update QuizAttemptOption
- **Endpoint:** `PUT http://localhost:8080/api/quiz-attempt-option/1`
- **Body:**
```json
{
  "optionKey": "string_value",
  "optionText": "string_value",
  "normalizedText": "string_value",
  "isCorrect": true,
  "displayOrder": 1,
  "createdAt": "2026-06-20T10:00:00"
}
```

### Delete QuizAttemptOption
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-attempt-option/1`

## QuizAttemptQuestion APIs

### Get All QuizAttemptQuestions
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt-question`

### Get QuizAttemptQuestion by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-attempt-question/1`

### Create QuizAttemptQuestion
- **Endpoint:** `POST http://localhost:8080/api/quiz-attempt-question`
- **Body:**
```json
{
  "questionText": "string_value",
  "questionImageUrl": "string_value",
  "questionType": "sample_QuestionType",
  "explanation": "string_value",
  "points": 1,
  "questionTimeLimitSeconds": 1,
  "displayOrder": 1,
  "createdAt": "2026-06-20T10:00:00"
}
```

### Update QuizAttemptQuestion
- **Endpoint:** `PUT http://localhost:8080/api/quiz-attempt-question/1`
- **Body:**
```json
{
  "questionText": "string_value",
  "questionImageUrl": "string_value",
  "questionType": "sample_QuestionType",
  "explanation": "string_value",
  "points": 1,
  "questionTimeLimitSeconds": 1,
  "displayOrder": 1,
  "createdAt": "2026-06-20T10:00:00"
}
```

### Delete QuizAttemptQuestion
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-attempt-question/1`

## QuizQuestion APIs

### Get All QuizQuestions
- **Endpoint:** `GET http://localhost:8080/api/quiz-question`

### Get QuizQuestion by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-question/1`

### Create QuizQuestion
- **Endpoint:** `POST http://localhost:8080/api/quiz-question`
- **Body:**
```json
{
  "questionText": "string_value",
  "questionImageUrl": "string_value",
  "questionType": "sample_QuestionType",
  "explanation": "string_value",
  "points": 1,
  "questionTimeLimitSeconds": 1,
  "displayOrder": 1,
  "isActive": true,
  "version": 100
}
```

### Update QuizQuestion
- **Endpoint:** `PUT http://localhost:8080/api/quiz-question/1`
- **Body:**
```json
{
  "questionText": "string_value",
  "questionImageUrl": "string_value",
  "questionType": "sample_QuestionType",
  "explanation": "string_value",
  "points": 1,
  "questionTimeLimitSeconds": 1,
  "displayOrder": 1,
  "isActive": true,
  "version": 100
}
```

### Delete QuizQuestion
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-question/1`

## QuizQuestionOption APIs

### Get All QuizQuestionOptions
- **Endpoint:** `GET http://localhost:8080/api/quiz-question-option`

### Get QuizQuestionOption by ID
- **Endpoint:** `GET http://localhost:8080/api/quiz-question-option/1`

### Create QuizQuestionOption
- **Endpoint:** `POST http://localhost:8080/api/quiz-question-option`
- **Body:**
```json
{
  "optionKey": "string_value",
  "optionText": "string_value",
  "normalizedText": "string_value",
  "isCorrect": true,
  "displayOrder": 1
}
```

### Update QuizQuestionOption
- **Endpoint:** `PUT http://localhost:8080/api/quiz-question-option/1`
- **Body:**
```json
{
  "optionKey": "string_value",
  "optionText": "string_value",
  "normalizedText": "string_value",
  "isCorrect": true,
  "displayOrder": 1
}
```

### Delete QuizQuestionOption
- **Endpoint:** `DELETE http://localhost:8080/api/quiz-question-option/1`

