package com.flyhub.ideaMS.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * this class handles some of the ddl queries that can not be effected with JPA
 * <em>the profiles on this class have been set , to allow for compilation since
 * we are using spring-boot-maven-plugin that creates a context at compilation
 * time, this auto wire the
 * <code>org.springframework.core.env.Environment</code> object which is used in
 * the post construct method</em>
 *
 * @author Benjamin E Ndugga
 * @since 07/09/2021
 */
@Component
public class DaoAccessUtils {

    private static final Logger log = Logger.getLogger(DaoAccessUtils.class.getName());

    /**
     * the postgres dialect class name
     */
    private static final String POSTGRES_DIALECT_CLASS_NAME = "org.hibernate.dialect.PostgreSQLDialect";

    /**
     * the mysql dialect class name
     */
    private static final String MYSQL_DIALECT_CLASS_NAME = "org.hibernate.dialect.MySQL57Dialect";

    /**
     * the mariabdb dialect
     */
    private static final String MARIADB_DIALECT_CLASS_NAME = "org.hibernate.dialect.MariaDB53Dialect";

    /**
     * creating a sequence for mariaDB
     */
    private static final String H2_DIALECT_CLASS_NAME = "org.hibernate.dialect.H2Dialect";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    private static String MERCHANT_SEQUENCE_QUERY = "";

    private static String SYS_USER_ID_QUERY = "";

    private static String SUGG_ID_QUERY = "";

    private static String IDEAS_ID_QUERY = "";

    @PostConstruct
    @SuppressWarnings("null")
    private void init() {

        log.info("Setting up query for DB");

        switch (env.getProperty("spring.jpa.properties.hibernate.dialect")) {
            case POSTGRES_DIALECT_CLASS_NAME: {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s  INCREMENT %d START %d", DaoConstants.MERCHANT_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL, DaoConstants.INIT_SEQUENCE_VAL));
                MERCHANT_SEQUENCE_QUERY = String.format("select nextval('%s')", DaoConstants.MERCHANT_ID_SEQUENCE_NAME);

                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s  INCREMENT %d START %d", DaoConstants.SYS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL, DaoConstants.INIT_SEQUENCE_VAL));
                SYS_USER_ID_QUERY = String.format("select nextval('%s')", DaoConstants.SYS_ID_SEQUENCE_NAME);

                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s  INCREMENT %d START %d", DaoConstants.SUGG_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL, DaoConstants.INIT_SEQUENCE_VAL));
                SUGG_ID_QUERY = String.format("select nextval('%s')", DaoConstants.SUGG_ID_SEQUENCE_NAME);

                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s  INCREMENT %d START %d", DaoConstants.IDEAS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL, DaoConstants.INIT_SEQUENCE_VAL));
                IDEAS_ID_QUERY = String.format("select nextval('%s')", DaoConstants.IDEAS_ID_SEQUENCE_NAME);

                break;
            }
            case MYSQL_DIALECT_CLASS_NAME:
            case MARIADB_DIALECT_CLASS_NAME: {

                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

                jdbcTemplate.execute(String.format("create table IF NOT EXISTS %s (next_val bigint(20))", DaoConstants.MERCHANT_ID_SEQUENCE_NAME));
                try {
                    jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.MERCHANT_ID_SEQUENCE_NAME), Long.class);

                } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
                    log.debug("initialising mysql " + DaoConstants.MERCHANT_ID_SEQUENCE_NAME + " sequence table.");
                    //this exception is thrown when the queryForObject method fails to find the expected row count of 1
                    //therefore we init the value
                    jdbcTemplate.execute(String.format("insert into %s values (%d)", DaoConstants.MERCHANT_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_VAL));
                }

                jdbcTemplate.execute(String.format("create table IF NOT EXISTS %s (next_val bigint(20))", DaoConstants.SYS_ID_SEQUENCE_NAME));
                try {
                    jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.SYS_ID_SEQUENCE_NAME), Long.class);

                } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
                    log.debug("initialising mysql " + DaoConstants.SYS_ID_SEQUENCE_NAME + " sequence table.");
                    //this exception is thrown when the queryForObject method fails to find the expected row count of 1
                    //therefore we init the value
                    jdbcTemplate.execute(String.format("insert into %s values (%d)", DaoConstants.SYS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_VAL));
                }

                break;
            }

            case H2_DIALECT_CLASS_NAME: {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s START WITH %d INCREMENT BY %d", DaoConstants.SYS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_VAL, DaoConstants.INIT_SEQUENCE_INC_VAL));

                jdbcTemplate.execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s START WITH %d INCREMENT BY %d", DaoConstants.MERCHANT_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_VAL, DaoConstants.INIT_SEQUENCE_INC_VAL));
                break;
            }
            default: {
                throw new RuntimeException(String.format("Un-supported/Unknown hibernate dialect: %s", env.getProperty("spring.jpa.properties.hibernate.dialect")));
            }
        }
    }

    /**
     *
     * @return the current merchant Id which is a sequence that is managed by
     * the database
     */
    @SuppressWarnings("null")
    public String generateMerchantId() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        long id = 0;

        switch (env.getProperty("spring.jpa.properties.hibernate.dialect")) {
            case POSTGRES_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(MERCHANT_SEQUENCE_QUERY, Long.class);
                break;
            }

            case MYSQL_DIALECT_CLASS_NAME:
            case MARIADB_DIALECT_CLASS_NAME: {
                jdbcTemplate.execute(String.format("update %s set next_val=next_val+%d", DaoConstants.MERCHANT_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL));

                id = jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.MERCHANT_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            case H2_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(String.format("VALUES NEXT VALUE FOR %s", DaoConstants.SYS_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            default: {
                throw new RuntimeException("Unknown hibernate dialect");
            }
        }

        log.info("GENERATED-MERCH-ID: " + id);

        return String.format(DaoConstants.MERCHANT_ID_SEQUENCE_PREFIX + "%05d", id);
    }

    @SuppressWarnings("null")
    public String generateSystemUserId() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        long id = 0;

        switch (env.getProperty("spring.jpa.properties.hibernate.dialect")) {
            case POSTGRES_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(SYS_USER_ID_QUERY, Long.class);
                break;
            }

            case MYSQL_DIALECT_CLASS_NAME:
            case MARIADB_DIALECT_CLASS_NAME: {
                jdbcTemplate.execute(String.format("update %s set next_val=next_val+%d", DaoConstants.SYS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL));

                id = jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.SYS_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            case H2_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(String.format("VALUES NEXT VALUE FOR %s", DaoConstants.MERCHANT_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            default: {
                throw new RuntimeException("Unknown hibernate dialect");
            }
        }

        log.info("GENERATED-SYS-ID: " + id);

        return String.format(DaoConstants.SYS_ID_SEQUENCE_PREFIX + "%05d", id);
    }

    @SuppressWarnings("null")
    public String generateSuggestionId() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        long id = 0;

        switch (env.getProperty("spring.jpa.properties.hibernate.dialect")) {
            case POSTGRES_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(SUGG_ID_QUERY, Long.class);
                break;
            }

            case MYSQL_DIALECT_CLASS_NAME:
            case MARIADB_DIALECT_CLASS_NAME: {
                jdbcTemplate.execute(String.format("update %s set next_val=next_val+%d", DaoConstants.SUGG_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL));

                id = jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.SUGG_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            case H2_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(String.format("VALUES NEXT VALUE FOR %s", DaoConstants.MERCHANT_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            default: {
                throw new RuntimeException("Unknown hibernate dialect");
            }
        }

        log.info("GENERATED-SUGGESTION-ID: " + id);

        return String.format(DaoConstants.SUGG_ID_SEQUENCE_PREFIX + "%05d", id);
    }


    @SuppressWarnings("null")
    public String generateIdeasId() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        long id = 0;

        switch (env.getProperty("spring.jpa.properties.hibernate.dialect")) {
            case POSTGRES_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(IDEAS_ID_QUERY, Long.class);
                break;
            }

            case MYSQL_DIALECT_CLASS_NAME:
            case MARIADB_DIALECT_CLASS_NAME: {
                jdbcTemplate.execute(String.format("update %s set next_val=next_val+%d", DaoConstants.IDEAS_ID_SEQUENCE_NAME, DaoConstants.INIT_SEQUENCE_INC_VAL));

                id = jdbcTemplate.queryForObject(String.format("select next_val from %s", DaoConstants.IDEAS_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            case H2_DIALECT_CLASS_NAME: {
                id = jdbcTemplate.queryForObject(String.format("VALUES NEXT VALUE FOR %s", DaoConstants.MERCHANT_ID_SEQUENCE_NAME), Long.class);

                break;
            }

            default: {
                throw new RuntimeException("Unknown hibernate dialect");
            }
        }

        log.info("GENERATED-IDEAS-ID: " + id);

        return String.format(DaoConstants.IDEAS_ID_SEQUENCE_PREFIX + "%05d", id);
    }


    /**
     * @deprecated this is not in use
     * @param user_id
     * @param func_Id
     * @throws SQLException
     */
    public final void deleteSysUserFGMapping(long user_id, String func_Id) throws SQLException {
        PreparedStatement prepareStatement = dataSource.getConnection().prepareStatement("DELETE FROM " + DaoConstants.SYS_USER_FUNCTIONAL_MAPPINGS_TABLE_NAME + " WHERE func_id =? AND user_id = ?");
        prepareStatement.setString(1, func_Id);
        prepareStatement.setLong(2, user_id);
        int i = prepareStatement.executeUpdate();
    }
}
