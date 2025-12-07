survey (UUID PK)
├── Metadata: title, description, instructions
├── Status: status (DRAFT/ACTIVE/etc)
├── Scoring: scoring_type, show_results
├── Timing: time_limit_minutes, expires_at
├── Audit: created_at, updated_at, created_by
└── Indexes: status, expires_at, created_at

survey_section (UUID PK, FK: survey_id)
├── Organization: title, description, display_order
├── Scoring: scoring_type, max_score, pass_score
└── Indexes: survey_id, (survey_id, display_order)

question (UUID PK, FK: section_id)
├── Content: question_text, description, question_type
├── Configuration: display_order, is_required, is_disabled
├── Scoring: scoring_type, max_points
├── Timing: time_limit_seconds
├── Media: image_url, video_url
└── Indexes: section_id, question_type, (section_id, display_order)

question_option (UUID PK, FK: question_id)
├── Content: option_text, description, display_order
├── Scoring: points, numeric_value
├── Media: image_url, image_alt_text, video_url
└── Indexes: question_id, (question_id, display_order)

survey_response (UUID PK, FK: survey_id)
├── Respondent: respondent_id, ip_address, user_agent
├── Status: status (STARTED/SUBMITTED/etc)
├── Scoring: total_score, max_score, score_percentage, is_passed
├── Timing: time_spent_seconds, started_at, submitted_at
└── Indexes: survey_id, respondent_id, status, created_at

survey_response_answer (UUID PK, FK: response_id, question_id, option_id)
├── Answer: answer_payload (JSONB!), answer_value
├── Scoring: answer_score, is_correct
├── Timing: time_spent_seconds, answered_at
└── Indexes: response_id, question_id, JSONB GIN index


Table "assessment" {
  "assessment_id" uuid [pk]
  "title" VARCHAR(500) [not null]
  "description" TEXT
  "assessment_type" TEXT
  "instructions" TEXT
  "status" VARCHAR(20) [not null]
  "scoring_type" VARCHAR(30) [not null]
  "show_results" BOOLEAN [not null]
  "time_limit_minutes" uuid
  "expires_at" TIMESTAMP
  "created_at" TIMESTAMP [not null]
  "updated_at" TIMESTAMP [not null]
  "created_by" VARCHAR(255)

  Indexes {
    status [name: "idx_assessment_status"]
    expires_at [name: "idx_assessment_expires_at"]
    created_at [name: "idx_assessment_created_at"]
  }
}

Table "assessment_section" {
  "section_id" uuid [pk]
  "assessment_id" uuid [not null]
  "title" VARCHAR(500) [not null]
  "description" TEXT
  "display_order" uuid [not null]
  "scoring_type" VARCHAR(30) [not null]
  "max_score" DOUBLE
  "pass_score" DOUBLE
  "created_at" TIMESTAMP [not null]
  "updated_at" TIMESTAMP [not null]

  Indexes {
    assessment_id [name: "idx_section_assessment_id"]
    (assessment_id, display_order) [name: "idx_section_display_order"]
  }
}

Table "question" {
  "question_id" uuid [pk]
  "section_id" uuid [not null]
  "question_text" TEXT [not null]
  "description" TEXT
  "question_type" VARCHAR(30) [not null]
  "display_order" uuid [not null]
  "is_required" BOOLEAN [not null]
  "is_disabled" BOOLEAN [not null]
  "time_limit_seconds" uuid
  "scoring_type" VARCHAR(30) [not null]
  "max_points" DOUBLE
  "image_url" VARCHAR(2048)
  "video_url" VARCHAR(2048)
  "created_at" TIMESTAMP [not null]
  "updated_at" TIMESTAMP [not null]

  Indexes {
    section_id [name: "idx_question_section_id"]
    question_type [name: "idx_question_type"]
    (section_id, display_order) [name: "idx_question_display_order"]
  }
}

Table "question_option" {
  "option_id" uuid [pk]
  "question_id" uuid [not null]
  "option_text" TEXT [not null]
  "description" TEXT
  "display_order" uuid [not null]
  "points" DOUBLE
  "numeric_value" DOUBLE
  "image_url" VARCHAR(2048)
  "image_alt_text" VARCHAR(500)
  "video_url" VARCHAR(2048)
  "created_at" TIMESTAMP [not null]

  Indexes {
    question_id [name: "idx_option_question_id"]
    (question_id, display_order) [name: "idx_option_display_order"]
  }
}

Table "assessment_response" {
  "response_id" uuid [pk]
  "assessment_id" uuid [not null]
  "respondent_id" VARCHAR(500)
  "status" VARCHAR(20) [not null]
  "ip_address" VARCHAR(50)
  "user_agent" TEXT
  "total_score" DOUBLE
  "max_score" DOUBLE
  "score_percentage" DOUBLE
  "is_passed" BOOLEAN
  "time_spent_seconds" uuid
  "started_at" TIMESTAMP
  "submitted_at" TIMESTAMP
  "created_at" TIMESTAMP [not null]
  "updated_at" TIMESTAMP [not null]

  Indexes {
    assessment_id [name: "idx_response_assessment_id"]
    respondent_id [name: "idx_response_respondent_id"]
    status [name: "idx_response_status"]
    created_at [name: "idx_response_created_at"]
  }
}

Table "assessment_response_answer" {
  "answer_id" uuid [pk]
  "response_id" uuid [not null]
  "question_id" uuid [not null]
  "option_id" uuid
  "answer_payload" JSONB [not null]
  "answer_value" TEXT
  "answer_score" DOUBLE
  "is_correct" BOOLEAN
  "time_spent_seconds" uuid
  "answered_at" TIMESTAMP
  "created_at" TIMESTAMP [not null]
  "updated_at" TIMESTAMP [not null]

  Indexes {
    response_id [name: "idx_answer_response_id"]
    question_id [name: "idx_answer_question_id"]
  }
}

Ref:"assessment"."assessment_id" < "assessment_section"."assessment_id" [delete: cascade]

Ref:"assessment_section"."section_id" < "question"."section_id" [delete: cascade]

Ref:"question"."question_id" < "question_option"."question_id" [delete: cascade]

Ref:"assessment"."assessment_id" < "assessment_response"."assessment_id" [delete: cascade]

Ref:"assessment_response"."response_id" < "assessment_response_answer"."response_id" [delete: cascade]

Ref:"question"."question_id" < "assessment_response_answer"."question_id"

Ref:"question_option"."option_id" < "assessment_response_answer"."option_id"