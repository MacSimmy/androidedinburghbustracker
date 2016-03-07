# Introduction #

This guide describes the config file format for the Edinburgh Bus Tracker Server.


# Details #

## Default config location ##

The default file name for the config file is <path to server JAR file directory>/bustracker.conf however the user may specify an alternative location for the config file by executing the server JAR file like so;

```
java -jar EdinburghBusTrackerServer.jar -c <path to config file>
```

## Config file format ##

Comments are allowed. Commented lines must be prefixed by the # symbol. If the comment continues on to a new line, the new line must also be prefixed by the # symbol.

The config file uses the following format;

```
key=value
```

Example:

```
port=1234
```

This format is used for simplicity and the config for the server is not hugely complex. Whitespace is ignored unless it exists after the start and before the end of a string. When an unknown key is encountered the server will report an error to the error console (most likely your terminal or system logger) and then take no further action regarding this key, simply discarding it.

## Possible configuration options ##

This is a listing of all possible configuration options for the server;

| **Key Name** | **Data type** | **Valid values** | **Default value** | **Function** |
|:-------------|:--------------|:-----------------|:------------------|:-------------|
| port         | Integer       | 1 - 65535        | 4876              | The local port to listen for client connections on. |
| bindaddress  | IP Address    | 0.0.0.0 - 255.255.255.255 | 0.0.0.0           | The local interface address to bind to.  0.0.0.0 is the default system interface. |
| maxconnections | Integer       | 0 - MAXINT       | 100               | The maximum number of simultaneous client connections this server can accept. If 0 or a very high number is specified, the server will keep accepting connections until the host system does not allow it to accept any more, ie resource starvation. Setting this value to a sane number is highly recommended if you want a stable server and system. |
| dbpath       | String        | Any valid absolute or abstract path on the host system | ./                | Specify a web-accessible location where the database should be put after it is generated so that the mobile clients can download this database. "dburl" should be set too. |
| dburl        | String        | Any valid URL    | http://localhost/busstops.db | The full URL, including "http://", to where the mobile clients can download the latest bus stop database. |

## Default config file ##

This is the default config file for the server, located in bustracker.conf;

```
# The default settings for the Edinburgh Bus Tracker for Android server
port=4876
bindaddress=0.0.0.0
maxconnections=100
dbpath=./
dburl=http://localhost/busstops.db
```