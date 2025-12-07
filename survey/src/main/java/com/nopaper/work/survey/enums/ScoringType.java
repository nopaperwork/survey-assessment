/**
 * @package com.nopaper.work.survey.enums -> survey
 * @author saikatbarman
 * @date 2025 08-Dec-2025 1:02:22 am
 * @git 
 */
package com.nopaper.work.survey.enums;

/**
 * STAGE 1: Domain Model - Scoring Type Enumeration
 *
 * Purpose:
 * Defines the scoring mechanism types that can be applied to questions or sections.
 * Different scoring types allow for flexible evaluation based on survey requirements.
 *
 * Types:
 * - NO_SCORING: Survey has no scoring
 * - FIXED_SCORE: Each question/option has a fixed score
 * - WEIGHTED_SCORE: Questions have different weights within a section
 * - DYNAMIC_SCORE: Score based on response value (e.g., slider position = score)
 * - FORMULA_BASED: Custom formula to calculate final score
 */

/**
 * Enumeration of supported scoring mechanism types.
 * 
 * Used to configure how responses are evaluated and scored in a survey.
 */
public enum ScoringType {
    // No scoring
    NO_SCORING("No Scoring - Survey is informational only"),
    
    // Fixed scoring mechanisms
    FIXED_SCORE("Fixed Score - Each question/ option has a predefined score"),
    WEIGHTED_SCORE("Weighted Score - Questions have different weights within a section"),
    
    // Dynamic/Automatic scoring
    DYNAMIC_SCORE("Dynamic Score - Score based on response value (e.g., slider position)"),
    
    // Complex scoring
    FORMULA_BASED("Formula Based - Custom formula to calculate final score");

    private final String description;

    /**
     * Constructor for ScoringType enum
     *
     * @param description Human-readable description of the scoring type
     */
    ScoringType(String description) {
        this.description = description;
    }

    /**
     * Get the description of the scoring type
     *
     * @return Description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if this scoring type requires score configuration
     *
     * @return true if scoring configuration is needed, false for NO_SCORING
     */
    public boolean requiresConfiguration() {
        return this != NO_SCORING;
    }
}