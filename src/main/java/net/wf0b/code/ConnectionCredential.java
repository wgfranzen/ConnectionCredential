/*
 * Copyright (c) 2020, 2021, 2022, 2023 Bill Franzen (WF0B).
 *
 *  ParameterException is part of common.
 *
 *  common is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  common is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License along with ParseAdif. If not, see <https://www.gnu.org/licenses/>.
 */

package net.wf0b.code;

import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * ConnectionCredential is an object to manage four points of connecting to a database:
 * <ol>
 * <li>The Connection String, typically a URL used to provide the means to locate the database</li>
 * <li>The Name of the JDBC Driver for that particular database</li>
 * <li>The Name of the User/Account that will be accessing the server (which defaults to the current system user.name property value)</li>
 * <li>The password associated with the Name of the User/Account (which is subsequently encrypted)<li>
 * </ol>
 * <p>ConnectionCredential is intended to be run as a command, providing the capability of serializing the ConnectionCredential for repeated use.</p>
 * <p>Once a ConnectionCredential is created and retained, its major function is to provide a factory method to create aSQL Connection object for use in
 * interacting with a particular database.</p>
 *
 * <code>
 * <br> Usage: ConnectionCredential [-chVx] [-d[=&lt;driverName&gt;]] [-p[=&lt;password&gt;]] [-r [=&lt;role&gt;]] [-s[=&lt;connectionString&gt;]] -i=&lt;instanceName&gt;
 * <br> Maintains jdbc database connections for the user.
 * <br> -c, --change                      change existing entry
 * <br> -d, --driver[=&lt;driverName&gt;]       the jdbc driver name
 * <br> -h, --help                        Show this help message and exit.
 * <br> -i, --instance=&lt;instanceName&gt;     the instance (or application) name
 * <br> -p, --password[=&lt;password&gt;]       Passphrase
 * <br> -r, --role[=&lt;role&gt;]               the role a user has
 * <br> -s, --string[=&lt;connectionString&gt;] the connection string
 * <br> -V, --version                     Print version information and exit.
 * <br> -x, --delete                      delete existing entry
 * </code>
 */
// TODO add a listing function
@CommandLine.Command(name = "ConnectionCredential", mixinStandardHelpOptions = true, version = "ConnectionCredential-0.1-Snapshot",
        description = "Maintains jdbc database connections for the user.")
public class ConnectionCredential implements Callable<Integer> {

    /**
     * The instance (or application) name
     */
    @CommandLine.Option(names = {"-i", "--instance"}, description = "the instance (or application) name", required = true)
    private String instanceName;

    /**
     * the jdbc driver name
     */
    @CommandLine.Option(names = {"-d", "--driver"}, arity = "0..1", description = "the jdbc driver name")
    private String driverName;

    /**
     * the connection string
     */
    @CommandLine.Option(names = {"-s", "--string"}, arity = "0..1", description = "the connection string")
    private String connectionString;

    /**
     * The role a user hase
     */
    @CommandLine.Option(names = {"-r", "--role"}, arity = "0..1", description = "the role a user has")
    private String role;

    /**
     * the password to be used
     */
    @CommandLine.Option(names = {"-p", "--password"}, arity = "0..1", interactive = true, description = "Passphrase")
    private char[] password;

    /**
     * Denotes whether this is a change
     */
    @CommandLine.Option(names = {"-c", "--change"}, description = "change existing entry")
    private final boolean isChange = false;

    /**
     * Denotes whether this is deleted
     */
    @CommandLine.Option(names = {"-x", "--delete"}, description = "delete existing entry")
    private final boolean isDelete = false;

    /**
     * Processes the parameters
     *
     * @param args The arguments for ConnectionCredential
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ConnectionCredential()).execute(args);
    }

    /**
     * Enables the Picocli interface
     *
     * @return Executes the functions of ConnectionCredential
     * @throws Exception for an error thrown
     */
    public Integer call() throws Exception {
        Credentials credentials = OperatingSystem.getCredentials();
        Credential credential;
        if (isDelete) {
            if (instanceName != null) {
                if (role == null) {
                    if (credentials.containsKey(instanceName)) {
                        credentials.get(instanceName).setInactive();
                    } else {
                        System.err.println("credentials for " + instanceName + " not found, not deleted");
                    }
                } else {
                    if (credentials.containsKey(instanceName, role)) {
                        credentials.get(instanceName, role).setInactive();
                    } else {
                        System.err.println("credentials for " + instanceName + " and " + role + " not found, not deleted");
                    }
                }
            }
        } else {
            if (driverName == null || connectionString == null || instanceName == null) {
                System.err.println("missing information, can not continue");
            } else {
                if (role == null)
                    credential = new Credential(instanceName, driverName, connectionString, password);
                else
                    credential = new Credential(instanceName, driverName, connectionString, password, role);

                if (isChange) {
                    if (credentials.containsKey(credential.getIdentifier())) {
                        credentials.put(credential.getIdentifier(), credential);
                        System.out.println("credential updated");
                    } else {
                        System.err.println("credential not found, can not be changed, will be added");
                    }
                } else {
                    if (credentials.containsKey(credential.getIdentifier())) {
                        System.err.println("credential already exists, not added");
                    } else {
                        credentials.put(credential.getIdentifier(), credential);
                        System.out.println("credential added");
                    }
                }
            }
        }

        OperatingSystem.putCredentials(credentials);

        return 0;
    }


}