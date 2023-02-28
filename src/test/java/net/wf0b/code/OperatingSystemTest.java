package net.wf0b.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Operating System Unit Test")
class OperatingSystemTest {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("detail")
    @Tag("OperatingSystem")
    @Test
    public @interface DetailTest {
    }

    @DetailTest
    @DisplayName("OperatingSystem.isWindows()")
    void isWindows() {
        assertTrue(OperatingSystem.isWindows());
    }

    @DetailTest
    @DisplayName("OperatingSystem.isMac()")
    void isMac() {
        assertFalse(OperatingSystem.isMac());
    }

    @DetailTest
    @DisplayName("OperatingSystem.isSolaris()")
    void isSolaris() {
        assertFalse(OperatingSystem.isSolaris());
    }

    @DetailTest
    @DisplayName("OperatingSystem.is_ix()")
    void is_ix() {
        assertFalse(OperatingSystem.is_ix());
    }

    @DetailTest
    @DisplayName("OperatingSystem.isLinux()")
    void isLinux() {
        assertFalse(OperatingSystem.isLinux());
    }

    @DetailTest
    @DisplayName("OperatingSystem.isUnix()")
    void isUnix() {
        assertFalse(OperatingSystem.isUnix());
    }

    @DetailTest
    @DisplayName("OperatingSystem.isAix()")
    void isAix() {
        assertFalse(OperatingSystem.isAix());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getOS()")
    void getOS() {
        assertEquals(OperatingSystem.OS.WINDOWS, OperatingSystem.getOS());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getApplicationHome()")
    void getApplicationHome() throws IOException {
        assertEquals("C:\\Users\\wgfra\\AppData\\Local\\.ConnectionCredential",
                OperatingSystem.getApplicationHome("ConnectionCredential").toString());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getWork()")
    void getWork() {
        assertEquals(Paths.get("C:\\Users\\wgfra\\IdeaProjects\\ConnectionCredential"),
                OperatingSystem.getWork());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getFileSeparator()")
    void getFileSeparator() {
        assertEquals("\\", OperatingSystem.getFileSeparator());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getLineSeparator()")
    void getLineSeparator() {
        assertEquals("\r\n", OperatingSystem.getLineSeparator());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getPathSeparator()")
    void getPathSeparator() {
        assertEquals(";", OperatingSystem.getPathSeparator());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getClassPath()")
    void getClassPath() {
        System.err.println(Arrays.toString(OperatingSystem.getClassPath()));
    }

    @DetailTest
    @DisplayName("OperatingSystem.getArchitecture()")
    void getArchitecture() {
        assertEquals("amd64", OperatingSystem.getArchitecture());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getName()")
    void getName() {
        assertEquals("Windows 10", OperatingSystem.getName());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getVersion()")
    void getVersion() {
        assertEquals("10.0", OperatingSystem.getVersion());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getPhrase()")
    void getPhrase() {
        assertEquals("Here comes Peter Cottontail, hopping down the bunny trail", OperatingSystem.getPhrase());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getKey()")
    void getKey() {

    }

    @DetailTest
    @DisplayName("OperatingSystem.getUser()")
    void getUser() {
        assertEquals("wgfra", OperatingSystem.getUser());
    }

    @DetailTest
    @DisplayName("OperatingSystem.getConnection(instanceName, properties)")
    void getConnection() {

    }

    @DetailTest
    @DisplayName("OperatingSystem.getConnection(instanceName, role, properties)")
    void testGetConnection() {

    }

    @DetailTest
    @DisplayName("OperatingSystem.getApplicationConnection(application)")
    void testGetApplicationConnection() throws SQLException, IOException {
        Connection connection = OperatingSystem.getApplicationConnection("ConnectionCredential");
        System.err.println(connection.getSchema());

    }
}
