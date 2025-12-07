/**
 * @package com.nopaper.work.survey.entity -> survey
 * @author saikatbarman
 * @date 2025 08-Dec-2025 1:07:20 am
 * @git 
 */
package com.nopaper.work.survey.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nopaper.work.survey.enums.ScoringType;
import com.nopaper.work.survey.enums.SurveyStatus;

/**
 * STAGE 1: Domain Model - Survey Entity (JPA)
 *
 *
 * Purpose:
 * Core entity representing a survey/assessment in the system.
 * A survey is a collection of sections, which contain questions.
 * 
 * Key Features:
 * - Survey metadata (title, description, instructions)
 * - Status management (DRAFT, ACTIVE, CLOSED, etc.)
 * - Time constraints (time limit, expiry date)
 * - Scoring configuration
 * - Audit trails (created_at, updated_at, created_by)
 *
 * Database:
 * - Table: survey
 * - Primary Key: survey_id (UUID)
 * - Relationships: One-to-Many with SurveySection, SurveyResponse
 * - Indexes: status, expires_at, created_at (for filtering active/expired surveys)
 */
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA Entity representing a Survey/Assessment.
 * 
 * Hibernate Annotations Used:
 * @Entity - Marks this as a JPA entity (maps to database table)
 * @Table - Specifies table name and indexes
 * @CreationTimestamp - Automatically set on insert
 * @UpdateTimestamp - Automatically updated on modify
 */
@Entity
@Table(
    name = "survey",
    indexes = {
        @Index(name = "idx_survey_status", columnList = "status"),
        @Index(name = "idx_survey_expires_at", columnList = "expires_at"),
        @Index(name = "idx_survey_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"sections", "responses"})
@EqualsAndHashCode(of = {"id"})
public class Survey implements Serializable {
    private static final long serialVersionUID = 1L;

    // ========================================================================
    // Primary Key and Identifiers
    // ========================================================================

    /**
     * Unique identifier for the survey (UUID).
     * 
     * Hibernate Annotation Usage:
     * @Id - Primary key
     * @GeneratedValue(strategy = GenerationType.UUID) - Auto-generate UUID
     * @Column - Map to database column with NOT NULL constraint
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "survey_id", columnDefinition = "UUID")
    private UUID id;

    // ========================================================================
    // Survey Metadata
    // ========================================================================

    /**
     * Title of the survey.
     * 
     * Constraints:
     * - NOT NULL: Every survey must have a title
     * - Length: 1-500 characters
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * Detailed description of the survey's purpose and content.
     * 
     * Constraints:
     * - Nullable: Optional description
     * - Length: Up to 2000 characters
     * - Type: columnDefinition = "TEXT" for larger strings in PostgreSQL
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Instructions for survey respondents.
     * May include HTML formatting for rich text.
     * 
     * Example:
     * "<p>Please answer all questions honestly.</p>"
     */
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    // ========================================================================
    // Survey Status and Lifecycle
    // ========================================================================

    /**
     * Current status of the survey.
     * 
     * Uses Enum type (DRAFT, ACTIVE, PAUSED, CLOSED, ARCHIVED, EXPIRED)
     * Hibernate handles enum-to-string conversion automatically
     */
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    // ========================================================================
    // Scoring Configuration
    // ========================================================================

    /**
     * Type of scoring mechanism used for this survey.
     * 
     * Enum Values:
     * - NO_SCORING: Survey is informational only
     * - FIXED_SCORE: Each question/option has a fixed score
     * - WEIGHTED_SCORE: Questions have different weights
     * - DYNAMIC_SCORE: Score based on response value
     * - FORMULA_BASED: Custom scoring formula
     */
    @Column(name = "scoring_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ScoringType scoringType;

    /**
     * Indicates whether survey shows results/scores to respondents.
     * 
     * Behavior:
     * - true: After completion, respondent sees their score/results
     * - false: Only admin/system can see results (survey appears as informational)
     */
    @Column(name = "show_results", nullable = false)
    private Boolean showResults;

    // ========================================================================
    // Time Constraints
    // ========================================================================

    /**
     * Overall time limit for completing the survey (in minutes).
     * 
     * Usage:
     * - null: No time limit, respondent can take as long as needed
     * - Example: 30 = 30 minutes to complete entire survey
     */
    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    /**
     * Date and time when the survey expires and is no longer accessible.
     * 
     * Usage:
     * - null: No expiry, survey remains open indefinitely
     * - Example: 2025-12-31 23:59:59 = Survey closes at year-end
     * 
     * Once expired, new responses cannot be submitted.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // ========================================================================
    // Audit Trail and Metadata
    // ========================================================================

    /**
     * Timestamp when the survey was created.
     * 
     * Auto-populated by Hibernate @CreationTimestamp.
     * Useful for tracking survey creation date for auditing.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the survey was last updated.
     * 
     * Auto-updated by Hibernate @UpdateTimestamp.
     * Changes when survey metadata, sections, or questions are modified.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Identifier of the user who created the survey.
     * 
     * Usage:
     * - Can be null if using system/automated survey creation
     * - Used for auditing and permission checks
     */
    @Column(name = "created_by", length = 255)
    private String createdBy;

    // ========================================================================
    // Relationships
    // ========================================================================

    /**
     * Sections within this survey.
     * 
     * Relationship:
     * - One Survey has Many SurveySection(s)
     * - cascade = CascadeType.ALL: Delete survey sections when survey is deleted
     * - orphanRemoval = true: Remove sections if they're removed from the list
     * - mappedBy = "survey": SurveySection owns the relationship
     * 
     * Ordering:
     * @OrderBy("displayOrder ASC"): Sections are ordered by display_order column
     */
    @OneToMany(
        mappedBy = "survey",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("displayOrder ASC")
    private List<SurveySection> sections;

    /**
     * Survey responses submitted by respondents.
     * 
     * Relationship:
     * - One Survey has Many SurveyResponse(s)
     * - cascade = CascadeType.REMOVE: When survey is deleted, responses are also deleted
     * - orphanRemoval = false: Responses can be deleted independently
     * - fetch = FetchType.LAZY: Responses loaded on-demand (not with survey)
     * 
     * Usage:
     * - Track all responses for a survey
     * - Compute aggregate statistics (avg score, response count, etc.)
     */
    @OneToMany(
        mappedBy = "survey",
        cascade = CascadeType.REMOVE,
        orphanRemoval = false,
        fetch = FetchType.LAZY
    )
    private List<SurveyResponse> responses;

    // ========================================================================
    // Business Logic Methods
    // ========================================================================

    /**
     * Check if this survey is currently accepting responses.
     * 
     * Conditions for accepting responses:
     * 1. Status must be ACTIVE
     * 2. Current time must be before or at expiry time (if expiry_at is set)
     * 3. Survey must not be marked as expired
     *
     * @return true if survey can accept new responses, false otherwise
     */
    public boolean isAcceptingResponses() {
        // Check if status allows responses
        if (!status.acceptsResponses()) {
            return false;
        }

        // Check if survey has expired
        if (expiresAt != null && LocalDateTime.now().isAfter(expiresAt)) {
            return false;
        }

        return true;
    }

    /**
     * Check if survey has a time limit for completion.
     *
     * @return true if timeLimitMinutes is set and greater than 0, false otherwise
     */
    public boolean hasTimeLimit() {
        return timeLimitMinutes != null && timeLimitMinutes > 0;
    }

    /**
     * Check if survey has expired based on expiry date.
     *
     * @return true if current time is after expiresAt, false otherwise
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Check if this survey uses scoring.
     *
     * @return true if scoringType is not NO_SCORING, false otherwise
     */
    public boolean hasScoring() {
        return scoringType != ScoringType.NO_SCORING;
    }
}