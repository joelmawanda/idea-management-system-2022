package com.flyhub.ideaMS.dao.entityauthority;

public enum EntityAuthorisationType {
    /**
     * identifies the entity authorisation type as for a Group
     */
    GROUP('G'),
    /**
     * identifies the entity authorisation type as for an Individual
     */
    INDIVIDUAL('I');

    /**
     * string representation of this Authorisation Entity Type
     */
    private final char name;

    EntityAuthorisationType(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }
}
