# About ConnectionCredential

ConnectionCredential retains jdbc database connection information, primarily for the use of applications with
an embedded java database (Derby). Since any connection could be specified, including sensitive passwords,
ConnectionCredential retains encrypted passwords.

ConnectionCredential is <b>NOT</b> a password vault; it is more like a locked file cabinet with the key to the cabinet
in plain site. ConnectionCredential is intended for convenience and <b>NOT</b> security. Please be aware of this and
plan accordingly.  <b><i>If your data must be secure, do not use ConnectionCredential.</i></b>

## Preparation

The Pass Phrase used to encrypt passwords is maintained as a user's environmental variable,
<code>ConnectionCredential</code>. This must be added prior to any execution. Please note this is intended as a
user specific environmental variable and not a system-wide variable.

## A Credential

The use cases for ConnectionCredential include retain and maintain credentials to establish a connection to the database
the credentials represent. The core element is a Credential. The Credential consists of two parts, the database
access part, and the user part.

The database access part consists of an <code>instanceName</code>, <code>connection string</code> and a
<code>driverName</code>. The <code>instanceName</code> is typically the name of the application, or host name for the
application. The <code>connection string</code> is the formal means to access the database.

The user part of a Credential consists of a user identifier and a password. The user identifier may be the operating
system's user's name, or it may be a role played by that user. It is common to allow a user to have two access points
to a database: one for performing administrative activities (a role based higher level of privilege), and then the
normal access using their existing name. When the user of the credential is equal to the user from a operating system
perspective, the role is considered to be <i>self</i>. When the user of the credential is not equal to the user from
an operating system perspective, the role is considered to be the user of the credential. Thus, a single user may have
many user parts to a single database access part.

The Credential is retrieved by supplying the <code>instanceName</code> (assuming the user to be the current operating
system's user) or supplying the <code>instanceName</code> and <code>role</code>. The connection is constructed from the
connection string assigned, with the credentials supplied.

## Usage

ConnectionCredential is established to add, change or remove specific Credentials from
the store. The usage is shown below. Associated with the jar are a run.bat (for windows)
and run.sh (for linux/unix) that will pass the parameters directly to ConnectionCredential.

    Usage: ConnectionCredential [-chVx] [-d[=<driverName>]] [-p[=<password>]] [-r
    [=<role>]] [-s[=<connectionString>]]
    -i=<instanceName>
    Maintains jdbc database connections for the user.
    -c, --change          change existing entry
    -d, --driver[=<driverName>]
    the jdbc driver name
    -h, --help            Show this help message and exit.
    -i, --instance=<instanceName>
    the instance (or application) name
    -p, --password[=<password>]
    Passphrase
    -r, --role[=<role>]   the role a user has
    -s, --string[=<connectionString>]
    the connection string
    -V, --version         Print version information and exit.
    -x, --delete          delete existing entry

### Add a Connection Credential

Let's assume we have an application to retain Contact Information called <code>Contact</code>. Let's also assume the
data is
retained in a SQL Server database called <code>MySQLServer</code>. The driver class name is
<code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code>. Let's also assume that you connect with as yourself, but also
as a dbadmin. When you sign on as a dbadmin, you can make structural table changes; but as yourself you can't make
structural table changes.

    ConnectionCredential -i Contact -d com.microsoft.sqlserver.jdbc.SQLServerDriver \
    -s jdbc:sqlserver://MySqlServer -p 

    ConnectionCredential -i Contact -d com.microsoft.sqlserver.jdbc.SQLServerDriver \
    -s jdbc:sqlserver://MySqlServer -r dbadmin -p 

Each execution will prompt you for a password from the console.

These credentials will be retained in a ConnectionCredential datastore.

### Change a Connection Credential

Let's assume the existing application <code>Contact</code>. Let's say that you've
changed the password for your <code>dbadmin</code> role. To make changes:

    ConnectionCredential -c -r dbadmin -i Contact -d com.microsoft.sqlserver.jdbc.SQLServerDriver \
    -s jdbc:sqlserver://MySqlServer -p 

When prompted, enter the new password.

### Remove a Connection Credential

Let's assume the existing application <code>Contact</code>. Let's say that you've been promoted and will no longer
perform the <code>dbadmin</code> role. To remove the role:

    ConnectionCredential -x -r dbadmin -i Contact 

Let's also say that you'll also not use the the <code>Contact</code> application, requiring you to delete your
personal credentials:

    ConnectionCredential -x -i Contact 

### Using ConnectionCredential to Get a Connection

The following calls will lookup and provide an access based on a retained ConnectionCredential:

    Connection OperatingSystem.getConnection(String instanceName, Properties properties)

    Connection OperatingSystem.getConnection(String instanceName, String role, Properties properties) 

Both of these will fetch the appropriate the ConnectionCredential, then attempt to establish a connection. In these
cases, the user and password will be added to the properties passed to the connection request. The properties provide
the means to pass other parameters to the connection as needed.

#### Embedded Java Database (Derby)

In some cases, an embedded java database, without any user/password credentials is used by the application. To create
an embedded Java Database, use:

    Connection OperatingSystem.getApplicationConnection(String application)

This appends a <code>create=true</code> property to the connection string. Additionally, this records the database as
personal application folders (in windows, <code>\AppData\Local\application\database</code>). 








