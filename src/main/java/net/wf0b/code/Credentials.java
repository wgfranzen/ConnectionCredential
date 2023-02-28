package net.wf0b.code;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Properties;

/**
 * The container of credentials.
 */
public class Credentials extends Hashtable<Integer, Credential> {

    /**
     * Gets the Credential on the basis of the instance name
     *
     * @param instanceName this instance name
     * @return this Credential
     */
    public Credential get(String instanceName) {
        return get(instanceName, OperatingSystem.getUser());
    }

    /**
     * Gets the Credential on the basis of the instance name and role
     *
     * @param instanceName this instance name
     * @param role         this role
     * @return the Credential
     */
    public Credential get(String instanceName, String role) {
        return get((instanceName + ":" + role).hashCode());
    }

    /**
     * Removes the Credential on the basis of the instance name
     *
     * @param instanceName this instance name
     * @return this Credential just removed
     */
    public Credential remove(String instanceName) {
        return remove(instanceName, OperatingSystem.getUser());
    }

    /**
     * Removes the Credential on the basis of the instance name and role
     *
     * @param instanceName this instance name
     * @param role         this role
     * @return the Credential just removed
     */
    public Credential remove(String instanceName, String role) {
        return remove((instanceName + ":" + role).hashCode());
    }

    /**
     * Determines if credentials are available for the user in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @return an indicator denoting the instance name for the user exists
     */
    public boolean containsKey(String instanceName) {
        return containsKey(instanceName, OperatingSystem.getUser());
    }

    /**
     * Determines if credentials are available for the user's role in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @param role         the role of the user
     * @return an indicator denoting the instance name for the user's role exists
     */
    public boolean containsKey(String instanceName, String role) {
        return contains((instanceName + ":" + role).hashCode());
    }

    /**
     * Gets the connection for the user in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @param properties   connection properties (other than user, password)
     * @return the connection
     */
    protected Connection getConnection(String instanceName, Properties properties) {
        return getConnection(instanceName, OperatingSystem.getUser(), properties);
    }

    /**
     * Gets the connection for the user's role in an instance name
     *
     * @param instanceName the instance name, representing the application or operational name of the database
     * @param role         the user's role
     * @param properties   connection properties (other than user, password)
     * @return the connection
     */
    protected Connection getConnection(String instanceName, String role, Properties properties) {
        return get(instanceName + ":" + role).getConnection(properties);
    }

}
