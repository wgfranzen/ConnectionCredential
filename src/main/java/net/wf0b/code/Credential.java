package net.wf0b.code;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

/**
 * The credential supplies the essential items for obtaining a connection to a database.
 */
public class Credential {

    /**
     * The instance name, representing the application or operational name of the database.
     */
    private final String instanceName;

    /**
     * The connection string.
     */
    private final String connectionString;

    /**
     * The driver name.
     */
    private final String driverName;

    /**
     * The user name.
     */
    private final String user;

    /**
     * The user's password.
     */
    private String password;

    /**
     * An indicator denoting the credential is no longer used.
     */
    private boolean active = true;

    /**
     * Instantiates a credential for the current user.
     *
     * @param instanceName     the instance name, representing the application or operational name of the database
     * @param driverName       the driver name
     * @param connectionString the connection string
     * @param password         the user's password
     */
    protected Credential(String instanceName, String driverName, String connectionString, char[] password) {
        this(instanceName, driverName, connectionString, password, OperatingSystem.getUser());
    }

    /**
     * Instantiates a credential for a role the current user may play.
     *
     * @param instanceName     the instance name, representing the application or operational name of the database
     * @param driverName       the driver name
     * @param connectionString the connection string
     * @param password         the user's password
     * @param role             the role of the user, for example "dbadmin"
     */
    protected Credential(String instanceName, String driverName, String connectionString, char[] password, String role) {
        this.instanceName = instanceName;
        this.driverName = driverName;
        this.connectionString = connectionString;
        if (password != null)
            if (password.length > 0)
                setPassword(password);
        this.user = role;
    }

    /**
     * sets the password
     *
     * @param password the password
     */
    protected void setPassword(char[] password) {
        try {
            SecretKeySpec key = OperatingSystem.getKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            this.password = Base64.getEncoder().encodeToString(cipher.doFinal(new String(password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(System.err);
        } catch (InvalidKeyException e) {
            e.printStackTrace(System.err);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace(System.err);
        } catch (BadPaddingException e) {
            e.printStackTrace(System.err);
        }
        Arrays.fill(password, ' ');
    }

    /**
     * Gets the identifier of the credential
     *
     * @return the identifier of the credential
     */
    protected Integer getIdentifier() {
        return (instanceName + ":" + user).hashCode();
    }

    /**
     * Gets instanceName.
     *
     * @return value of instanceName
     */
    protected String getInstanceName() {
        return instanceName;
    }

    /**
     * Gets connectionString.
     *
     * @return value of connectionString
     */
    protected String getConnectionString() {
        return connectionString;
    }

    /**
     * Gets driverName.
     *
     * @return value of driverName
     */
    protected String getDriverName() {
        return driverName;
    }

    /**
     * Gets user.
     *
     * @return value of user
     */
    protected String getUser() {
        return OperatingSystem.getUser();
    }

    /**
     * Gets role of the user.
     *
     * @return the role of the user
     */
    protected String getRole() {
        if (user.equals(OperatingSystem.getUser())) return "self";
        else return user;
    }

    /**
     * Gets the connection from the Connection Credentials
     *
     * @param properties the non-user, non-password attributes or properties of the connection
     * @return connection
     */
    public Connection getConnection(Properties properties) {
        Connection result = null;
        Properties p = new Properties(properties);
        p.setProperty("user", getUser());
        p.setProperty("password", getCred());
        try {
            result = DriverManager.getConnection(getConnectionString(), properties);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;
    }

    /**
     * Gets the Credential, in the clear
     *
     * @return in the clear
     */
    protected String getCred() {
        String result = null;
        try {
            SecretKeySpec key = OperatingSystem.getKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] x = cipher.doFinal(Base64.getDecoder().decode(this.password));
            result = new String(x, StandardCharsets.UTF_8);
            Arrays.fill(x, (byte) 0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(System.err);
        } catch (InvalidKeyException e) {
            e.printStackTrace(System.err);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace(System.err);
        } catch (BadPaddingException e) {
            e.printStackTrace(System.err);
        }
        return result;
    }

    /**
     * Gets active.
     *
     * @return value of active
     */
    protected Boolean isActive() {
        return active;
    }

    /**
     * Sets the active indicator to show inactive
     */
    protected void setInactive() {
        this.active = false;
    }
}
