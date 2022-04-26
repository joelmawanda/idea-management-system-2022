package com.flyhub.ideaMS.dao;

/**
 *
 * @author Benjamin E Ndugga
 */
public abstract class DaoConstants {

    /**
     * the sys_user mappings table name with the functional group
     */
    public static final String SYS_USER_FUNCTIONAL_MAPPINGS_TABLE_NAME = "sys_user_functional_group_mappings";

    /**
     * the merchant mappings table name with the functional group
     */
    public static final String MERCHANTS_FUNCTIONAL_MAPPINGS_TABLE_NAME = "merchant_functional_group_mappings";

    /**
     * the name of the sequence to be created in the database
     */
    public static final String MERCHANT_ID_SEQUENCE_NAME = "merchant_id_seq";

    /**
     * the name of the sequence to be created in the database for the sys users
     */
    public static final String SYS_ID_SEQUENCE_NAME = "sys_id_seq";

    /**
     * the name of the sequence to be created in the database for the suggestions
     */
    public static final String SUGG_ID_SEQUENCE_NAME = "sugg_id_seq";

    /**
     * the name of the sequence to be created in the database for the suggestions
     */
    public static final String IDEAS_ID_SEQUENCE_NAME = "ideas_id_seq";

    /**
     * this is the prefix of the merchant Id
     */
    public static final String MERCHANT_ID_SEQUENCE_PREFIX = "NORMAL_USER_ID_";

    /**
     * this is the prefix of the merchant Id
     */
    public static final String SYS_ID_SEQUENCE_PREFIX = "SYS_";


    /**
     * this is the prefix of the suggestion Id
     */
    public static final String SUGG_ID_SEQUENCE_PREFIX = "SUGG_";

    /**
     * this is the prefix of the ideas Id
     */
    public static final String IDEAS_ID_SEQUENCE_PREFIX = "IDEA_";

    /**
     * this is the initial value for the sequence
     */
    public static final int INIT_SEQUENCE_VAL = 1000;

    /**
     * this is the value the sequence increments by
     */
    public static final int INIT_SEQUENCE_INC_VAL = 1;

    /**
     * this is the table name that has the mappings between the functional
     * groups and modules
     */
    public static final String FUNCTIONAL_GROUP_MAPPINGS_TABLE = "functional_group_module_mappings";

}
