/**
 * @package com.nopaper.work.survey.enums -> survey
 * @author saikatbarman
 * @date 2025 08-Dec-2025 1:04:49 am
 * @git 
 */
package com.nopaper.work.survey.enums;

/**
 * STAGE 1: Domain Model - Survey Status Enumeration
 *
 *
 * Purpose:
 * Represents the lifecycle status of a survey.
 *
 * States:
 * - DRAFT: Survey is being created/configured, not yet published
 * - ACTIVE: Survey is published and accepting responses
 * - PAUSED: Survey is temporarily unavailable
 * - CLOSED: Survey is no longer accepting responses (but can be viewed)
 * - ARCHIVED: Survey is archived and hidden from public access
 * - EXPIRED: Survey expiry date has passed
 */

/**
 * Enumeration representing the status/lifecycle of a survey.
 */

public enum SurveyStatus {
    DRAFT("Draft - Survey is being created/configured"),
    ACTIVE("Active - Survey is published and accepting responses"),
    PAUSED("Paused - Survey is temporarily unavailable"),
    CLOSED("Closed - Survey is no longer accepting responses"),
    ARCHIVED("Archived - Survey is archived and hidden"),
    EXPIRED("Expired - Survey expiry date has passed");

    private final String description;

    /**
     * Constructor for SurveyStatus enum
     *
     * @param description Human-readable description of the status
     */
    SurveyStatus(String description) {
        this.description = description;
    }

    /**
     * Get the description of the status
     *
     * @return Description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if this status allows accepting new responses
     *
     * @return true if survey is accepting responses, false otherwise
     */
    public boolean acceptsResponses() {
        return this == ACTIVE;
    }

    /**
     * Check if this status indicates the survey is no longer usable
     *
     * @return true if survey is expired, archived, or closed
     */
    public boolean isInactive() {
        return this == ARCHIVED || this == CLOSED || this == EXPIRED;
    }
}