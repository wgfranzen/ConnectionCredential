package net.wf0b.code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import static net.wf0b.code.OperatingSystem.OS.*;

/**
 * OperatingSystem provides a determination of the operating system.
 */
public class OperatingSystem {

    /**
     * An Enumeration of Operating System Architectures
     */
    public enum OS {
        /**
         * Windows
         */
        WINDOWS,

        /**
         * Mac
         */
        MAC,

        /**
         * Solaris
         */
        SOLARIS,

        /**
         * AIX
         */
        AIX,

        /**
         * Linux
         */
        LINUX,

        /**
         * Unix
         */
        UNIX,

        /**
         * Unknown
         */
        UNKNOWN
    }

    /**
     * The operating system
     */
    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Gets an indicator denoting a Windows Operating System
     *
     * @return an indicator denoting a Windows Operating System
     */
    public static boolean isWindows() {
        return OS.contains("win");
    }

    /**
     * Gets an indicator denoting a Mac Operating System
     *
     * @return an indicator denoting a Mac Operating System
     */
    public static boolean isMac() {
        return OS.contains("mac");
    }

    /**
     * Gets an indicator denoting a Solaris Operating System
     *
     * @return an indicator denoting a Solaris Operating System
     */
    public static boolean isSolaris() {
        return OS.contains("sunos");
    }

    /**
     * Gets an indicator denoting a Unix/Linux Operating System
     *
     * @return an indicator denoting a Unix/Linux Operating System
     */
    public static boolean is_ix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    /**
     * Gets an indicator denoting a Linux Operating System
     *
     * @return an indicator denoting a Linux Operating System
     */
    public static boolean isLinux() {
        return OS.contains("nux");
    }

    /**
     * Gets an indicator denoting a Unix Operating System
     *
     * @return an indicator denoting a Unix Operating System
     */
    public static boolean isUnix() {
        return OS.contains("nix");
    }

    /**
     * Gets an indicator denoting an Aix Operating System
     *
     * @return an indicator denoting an Aix Operating System
     */
    public static boolean isAix() {
        return OS.contains("aix");
    }

    /**
     * Gets the enumerated OS
     *
     * @return this OS enumeration
     */
    public static OS getOS() {
        if (isWindows()) return WINDOWS;
        if (isLinux()) return LINUX;
        if (isMac()) return MAC;
        if (isUnix()) return UNIX;
        if (isAix()) return AIX;
        if (isSolaris()) return SOLARIS;
        return UNKNOWN;
    }

    /**
     * Returns the Path to the Application Data.
     * <p>First execution will create directory when it doesn't exist.</p>
     *
     * @param application the Application Name
     * @return the Path
     * @throws IOException when directory create fails
     */
    protected static Path getApplicationHome(String application) throws IOException {
        Path result;
        if (isWindows()) {
            result = Paths.get(System.getProperty("user.home"), "AppData", "Local", "." + application);
            if (!Files.exists(result)) Files.createDirectories(result);
            result = Files.setAttribute(result, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } else {
            result = Paths.get(System.getProperty("user.home"), "." + application);
            if (!Files.exists(result)) Files.createDirectories(result);
        }
        Properties p = new Properties(System.getProperties());
        p.put("application.home", result.toString());
        System.setProperties(p);
        return result;
    }

    /**
     * Gets the working directory
     *
     * @return the working directory
     */
    public static Path getWork() {
        return Paths.get(System.getProperty("user.dir"));
    }

    /**
     * Gets the file separator
     *
     * @return the file separator
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * gets the line separator
     *
     * @return the line separator
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * gets the path separator
     *
     * @return the path separator
     */
    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    /**
     * gets the class path
     *
     * @return the path separator
     */
    public static Path[] getClassPath() {
        String[] cp = System.getProperty("java.class.path").split(getPathSeparator());
        Path[] results = new Path[cp.length];
        for (int i = 0; i < cp.length; i++) results[i] = Paths.get(cp[i]);
        return results;
    }

    /**
     * gets the architecture of the OS
     *
     * @return the architecture of the OS
     */
    public static String getArchitecture() {
        return System.getProperty("os.arch");
    }

    /**
     * gets the name of the OS
     *
     * @return the name of the OS
     */
    public static String getName() {
        return System.getProperty("os.name");
    }

    /**
     * gets the version of the OS
     *
     * @return the version of the OS
     */
    public static String getVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Gets the phrase
     *
     * @return the phrase
     */
    protected static String getPhrase() {
        return System.getenv("ConnectionCredential");
    }

    /**
     * Provides a secret key based on the user, the phrase, and a token
     *
     * @return the secret key
     */
    protected static SecretKeySpec getKey() {
        SecretKeySpec secretKey = null;
        byte[] key;
        MessageDigest sha;
        try {
            key = (getUser() + getPhrase() + "HiddenMessage").getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        return secretKey;
    }

    /**
     * Hex Character array
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Translate a byte array into a String of hexvalues
     *
     * @param bytes the byte array
     * @return resulting hex string
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


    /**
     * Gets the user
     *
     * @return the user
     */
    public static String getUser() {
        return System.getProperty("user.name");
    }

    /**
     * gets the credentials
     *
     * @return the credentials
     */
    protected static Credentials getCredentials() throws IOException {
        Path path = Paths.get(getApplicationHome("ConnectionCredential").toString(), ".credentials");
        if (!Files.exists(path)) return new Credentials();
        return new Gson().fromJson(Files.newBufferedReader(path), Credentials.class);
    }

    /**
     * puts the credentials
     *
     * @param credentials
     * @throws IOException
     */
    protected static void putCredentials(Credentials credentials) throws IOException {
        Credentials c = new Credentials();
        for (Integer key : credentials.keySet()) {
            if (credentials.get(key).isActive()) c.put(key, credentials.get(key));
        }
        Path path = Paths.get(getApplicationHome("ConnectionCredential").toString(), ".credentials");
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(c);
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write(json);
        writer.flush();
        writer.close();
    }

    /**
     * Gets the connection for the user in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @param properties   connection properties (other than user, password)
     * @return the connection
     * @throws IOException for any IO error
     */
    public static Connection getConnection(String instanceName, Properties properties) throws IOException {
        return getConnection(instanceName, getUser(), properties);
    }

    /**
     * Gets the connection for the user's role in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @param role         the user's role
     * @param properties   connection properties (other than user, password)
     * @return the connection
     * @throws IOException for any IO error
     */
    public static Connection getConnection(String instanceName, String role, Properties properties) throws IOException {
        return getCredentials().getConnection(instanceName, role, properties);
    }

    /**
     * Establishes a Connection to the Application Standalone Database
     *
     * @param application the Application Name
     * @return The Connection
     * @throws IOException  when the application name can not be assigned to a disk location
     * @throws SQLException when the connection fails
     */
    public static Connection getApplicationConnection(String application) throws IOException, SQLException {
        Path applicationHome = getApplicationHome(application);
        String connectionString = "jdbc:derby:" + applicationHome + "/database;create=true";
        return DriverManager.getConnection(connectionString);
    }

    // TODO Add a generic Derby Connection using DB Location
    // TODO Consider adding certain properties
}
