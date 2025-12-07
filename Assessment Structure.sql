CREATE TABLE "assessment" (
  "assessment_id" uuid PRIMARY KEY,
  "title" "VARCHAR(500)" NOT NULL,
  "description" TEXT,
  "assessment_type" TEXT,
  "instructions" TEXT,
  "status" "VARCHAR(20)" NOT NULL,
  "scoring_type" "VARCHAR(30)" NOT NULL,
  "show_results" BOOLEAN NOT NULL,
  "time_limit_minutes" uuid,
  "expires_at" TIMESTAMP,
  "created_at" TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMP NOT NULL,
  "created_by" "VARCHAR(255)"
);

CREATE TABLE "assessment_section" (
  "section_id" uuid PRIMARY KEY,
  "assessment_id" uuid NOT NULL,
  "title" "VARCHAR(500)" NOT NULL,
  "description" TEXT,
  "display_order" uuid NOT NULL,
  "scoring_type" "VARCHAR(30)" NOT NULL,
  "max_score" "DOUBLE",
  "pass_score" "DOUBLE",
  "created_at" TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMP NOT NULL
);

CREATE TABLE "question" (
  "question_id" uuid PRIMARY KEY,
  "section_id" uuid NOT NULL,
  "question_text" TEXT NOT NULL,
  "description" TEXT,
  "question_type" "VARCHAR(30)" NOT NULL,
  "display_order" uuid NOT NULL,
  "is_required" BOOLEAN NOT NULL,
  "is_disabled" BOOLEAN NOT NULL,
  "time_limit_seconds" uuid,
  "scoring_type" "VARCHAR(30)" NOT NULL,
  "max_points" "DOUBLE",
  "image_url" "VARCHAR(2048)",
  "video_url" "VARCHAR(2048)",
  "created_at" TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMP NOT NULL
);

CREATE TABLE "question_option" (
  "option_id" uuid PRIMARY KEY,
  "question_id" uuid NOT NULL,
  "option_text" TEXT NOT NULL,
  "description" TEXT,
  "display_order" uuid NOT NULL,
  "points" "DOUBLE",
  "numeric_value" "DOUBLE",
  "image_url" "VARCHAR(2048)",
  "image_alt_text" "VARCHAR(500)",
  "video_url" "VARCHAR(2048)",
  "created_at" TIMESTAMP NOT NULL
);

CREATE TABLE "assessment_response" (
  "response_id" uuid PRIMARY KEY,
  "assessment_id" uuid NOT NULL,
  "respondent_id" "VARCHAR(500)",
  "status" "VARCHAR(20)" NOT NULL,
  "ip_address" "VARCHAR(50)",
  "user_agent" TEXT,
  "total_score" "DOUBLE",
  "max_score" "DOUBLE",
  "score_percentage" "DOUBLE",
  "is_passed" BOOLEAN,
  "time_spent_seconds" uuid,
  "started_at" TIMESTAMP,
  "submitted_at" TIMESTAMP,
  "created_at" TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMP NOT NULL
);

CREATE TABLE "assessment_response_answer" (
  "answer_id" uuid PRIMARY KEY,
  "response_id" uuid NOT NULL,
  "question_id" uuid NOT NULL,
  "option_id" uuid,
  "answer_payload" JSONB NOT NULL,
  "answer_value" TEXT,
  "answer_score" "DOUBLE",
  "is_correct" BOOLEAN,
  "time_spent_seconds" uuid,
  "answered_at" TIMESTAMP,
  "created_at" TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMP NOT NULL
);

CREATE INDEX "idx_assessment_status" ON "assessment" ("status");

CREATE INDEX "idx_assessment_expires_at" ON "assessment" ("expires_at");

CREATE INDEX "idx_assessment_created_at" ON "assessment" ("created_at");

CREATE INDEX "idx_section_assessment_id" ON "assessment_section" ("assessment_id");

CREATE INDEX "idx_section_display_order" ON "assessment_section" ("assessment_id", "display_order");

CREATE INDEX "idx_question_section_id" ON "question" ("section_id");

CREATE INDEX "idx_question_type" ON "question" ("question_type");

CREATE INDEX "idx_question_display_order" ON "question" ("section_id", "display_order");

CREATE INDEX "idx_option_question_id" ON "question_option" ("question_id");

CREATE INDEX "idx_option_display_order" ON "question_option" ("question_id", "display_order");

CREATE INDEX "idx_response_assessment_id" ON "assessment_response" ("assessment_id");

CREATE INDEX "idx_response_respondent_id" ON "assessment_response" ("respondent_id");

CREATE INDEX "idx_response_status" ON "assessment_response" ("status");

CREATE INDEX "idx_response_created_at" ON "assessment_response" ("created_at");

CREATE INDEX "idx_answer_response_id" ON "assessment_response_answer" ("response_id");

CREATE INDEX "idx_answer_question_id" ON "assessment_response_answer" ("question_id");

ALTER TABLE "assessment_section" ADD FOREIGN KEY ("assessment_id") REFERENCES "assessment" ("assessment_id") ON DELETE CASCADE;

ALTER TABLE "question" ADD FOREIGN KEY ("section_id") REFERENCES "assessment_section" ("section_id") ON DELETE CASCADE;

ALTER TABLE "question_option" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("question_id") ON DELETE CASCADE;

ALTER TABLE "assessment_response" ADD FOREIGN KEY ("assessment_id") REFERENCES "assessment" ("assessment_id") ON DELETE CASCADE;

ALTER TABLE "assessment_response_answer" ADD FOREIGN KEY ("response_id") REFERENCES "assessment_response" ("response_id") ON DELETE CASCADE;

ALTER TABLE "assessment_response_answer" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("question_id");

ALTER TABLE "assessment_response_answer" ADD FOREIGN KEY ("option_id") REFERENCES "question_option" ("option_id");
