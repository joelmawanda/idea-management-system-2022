package com.flyhub.ideaMS.dao;

public enum SuggestionType {
    COMPLIMENT("COMPLIMENT"),
    COMPLAINT("COMPLAINT"),
    SUGGESTION("SUGGESTION"),
    ISSUE("ISSUE"),
    HAZZARD("HAZZARD"),
    OTHER("OTHER");
    private final String    suggestionType;

    private SuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getSuggestionType() {
        return suggestionType;
    }
}
