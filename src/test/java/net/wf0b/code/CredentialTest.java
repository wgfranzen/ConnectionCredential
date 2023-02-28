package net.wf0b.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Credential Unit Test")
class CredentialTest {

    private static final char[] PSWD = {'P', 'a', 'S', 's', 'W', '0', 'R', 'd'};

    private static final Credential CREDENTIAL = new Credential("ConnectionCredential",
            "org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:target/DB/ConnectionCredential", PSWD);

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("detail")
    @Tag("Credential")
    @Test
    public @interface DetailTest {
    }


    @DetailTest
    @DisplayName("Credential.getIdentifier()")
    void getIdentifier() {
        assertEquals(228281984, CREDENTIAL.getIdentifier());
    }

    @DetailTest
    @DisplayName("Credential.getInstanceName()")
    void getInstanceName() {
        assertEquals("ConnectionCredential", CREDENTIAL.getInstanceName());
    }

    @DetailTest
    @DisplayName("Credential.getConnectionString()")
    void getConnectionString() {
        assertEquals("jdbc:derby:target/DB/ConnectionCredential", CREDENTIAL.getConnectionString());
    }

    @DetailTest
    @DisplayName("Credential.getDriverName()")
    void getDriverName() {
        assertEquals("org.apache.derby.jdbc.EmbeddedDriver", CREDENTIAL.getDriverName());
    }

    @DetailTest
    @DisplayName("Credential.getUser()")
    void getUser() {
        assertEquals(OperatingSystem.getUser(), CREDENTIAL.getUser());
    }

    @DetailTest
    @DisplayName("Credential.getRole()")
    void getRole() {
        assertEquals("self", CREDENTIAL.getRole());
    }

    @DetailTest
    @DisplayName("Credential.getConnection()")
    void getConnection() {
        Properties createDB = new Properties();
        createDB.setProperty("create", "true");
        Connection conn = CREDENTIAL.getConnection(createDB);
        System.err.println(conn.toString());
    }

    @DetailTest
    @DisplayName("Credential.getCred()")
    void getCred() {
        assertEquals("PaSsW0Rd", CREDENTIAL.getCred());
    }
}