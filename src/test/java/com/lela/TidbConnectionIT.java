package com.lela;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=none")
@ActiveProfiles("prod")
class TidbConnectionIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void connectsToTidbAndReadsFlywaySeedData() {
        String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = 1",
                Integer.class
        );
        Integer languageCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM languages", Integer.class);
        Integer roleCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Integer.class);

        assertThat(version).containsIgnoringCase("tidb");
        assertThat(databaseName).isEqualTo("lela");
        assertThat(migrationCount).isNotNull().isGreaterThanOrEqualTo(1);
        assertThat(languageCount).isNotNull().isGreaterThanOrEqualTo(6);
        assertThat(roleCount).isNotNull().isGreaterThanOrEqualTo(4);
    }
}
