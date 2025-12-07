/**
 * @package com.nopaper.work.survey.enums -> survey
 * @author saikatbarman
 * @date 2025 08-Dec-2025 12:58:12 am
 * @git 
 */
package com.nopaper.work.survey.enums;

/**
 *  Purpose:
	 * Defines all supported question types in the survey microservice.
	 * Each question in a survey has a type that determines how it's displayed
	 * and how responses are validated and processed.
	 *
	 * Types:
	 * - SINGLE_CHOICE: One answer from multiple options
	 * - MULTIPLE_CHOICE: Multiple answers from options
	 * - TEXT: Free-form text response
	 * - NUMERIC: Numeric input (integer or decimal)
	 * - RATING: 1-N scale (e.g., 1-5, 1-10)
	 * - LIKERT_SCALE: Strongly Agree to Strongly Disagree scale
	 * - DROPDOWN: Single selection from dropdown list
	 * - SLIDER: Numeric value selection via slider
	 * - RANKING: Rank options in order
	 * - ORDERING: Order items/statements
	 * - DATE_TIME: Date and/or time input
	 * - IMAGE_CHOICE: Select from images
	 * - FILE_UPLOAD: Upload file(s)
	 * - SCORE: Calculated/read-only score display
	 * - MATRIX: Grid of questions (e.g., rows x rating columns)
*/
	 
/**
* Enumeration of all supported question types in the survey system.
* 
* Each type has distinct characteristics for:
* - Input validation (what constitutes a valid response)
* - UI representation (how it's rendered to users)
* - Response storage format (shape of the answer data)
* - Scoring logic (how points are assigned)
*/

public enum QuestionType {
   // Basic question types
   SINGLE_CHOICE("Single Choice - Select one option"),
   MULTIPLE_CHOICE("Multiple Choice - Select multiple options"),
   TEXT("Text - Free-form text response"),
   NUMERIC("Numeric - Numeric input (integer or decimal)"),
   
   // Scale-based question types
   RATING("Rating - Rate on a numeric scale (e.g., 1-5, 1-10)"),
   LIKERT_SCALE("Likert Scale - Agreement scale (Strongly Disagree to Strongly Agree)"),
   
   // Selection types
   DROPDOWN("Dropdown - Select one from dropdown list"),
   IMAGE_CHOICE("Image Choice - Select image(s)"),
   
   // Complex ordering types
   SLIDER("Slider - Numeric value via slider widget"),
   RANKING("Ranking - Rank options in preferred order"),
   ORDERING("Ordering - Order items/statements in sequence"),
   
   // Date/Time types
   DATE_TIME("Date/Time - Date and/or time input"),
   
   // Media upload type
   FILE_UPLOAD("File Upload - Upload file(s)"),
   
   // Calculated type
   SCORE("Score - Calculated/display-only score"),
   
   // Complex matrix type
   MATRIX("Matrix - Grid of questions (rows × columns with rating scale)");

   private final String description;

   /**
    * Constructor for QuestionType enum
    *
    * @param description Human-readable description of the question type
    */
   QuestionType(String description) {
       this.description = description;
   }

   /**
    * Get the description of the question type
    *
    * @return Description string
    */
   public String getDescription() {
       return description;
   }

   /**
    * Check if this question type requires options
    * (i.e., has predefined answer choices)
    *
    * @return true if question type requires options, false otherwise
    */
   public boolean requiresOptions() {
       return switch (this) {
           case SINGLE_CHOICE, MULTIPLE_CHOICE, DROPDOWN,
                RATING, LIKERT_SCALE, RANKING, IMAGE_CHOICE, MATRIX -> true;
           default -> false;
       };
   }

   /**
    * Check if this question type allows multiple selections
    *
    * @return true if multiple selections are allowed, false otherwise
    */
   public boolean allowsMultipleSelections() {
       return switch (this) {
           case MULTIPLE_CHOICE, FILE_UPLOAD -> true;
           default -> false;
       };
   }

   /**
    * Check if this question type is a scale-based type
    *
    * @return true if it's scale-based (rating, likert, slider), false otherwise
    */
   public boolean isScaleBased() {
       return switch (this) {
           case RATING, LIKERT_SCALE, SLIDER -> true;
           default -> false;
       };
   }
}