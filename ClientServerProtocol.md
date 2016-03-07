# Introduction #

SINCE MY BUS EDINBURGH 2.0, THIS IS NOW DEPRECATED.

The client-server protocol is described here.


# Format #

The client-server protocol is designed to be as lightweight as possible. It is also designed to be natural to those who are familiar with functional programming languages. The protocol follows the following format:

```
function:arg1,arg2,arg3,...
```

If a command does not accept arguments, then it is called by simply its name, omitting the colon ':' and any arguments. See the examples section for a real working example. Currently, no commands require more than one argument.

```
function
```

The server deals with each client in a separate thread which does not spawn any additional threads. As such, when a command is executed that client's thread is blocked until a result is returned. Many commands can be issued to the server at a time, however, the server will queue these up and execute them in order. Thus, results to commands will be returned in the same order that the respective commands were issued.

# Available commands #

| **Command** | **Arguments** | **Returns** | Description |
|:------------|:--------------|:------------|:------------|
| getBusTimesByStopCode | The stop code | + 

&lt;crlf&gt;

 JSON text 

&lt;crlf&gt;

 - | Get the live data for a bus stop. |
| getDBURL    | none          | Bus stop database URL | Get the URL to the bus stop database for client updating. |
| getDBLastModTime | none          | Database last modification time. | Get the timestamp (milliseconds since the EPOCH) of when the database was last generated. |
| getLatestAndroidClientVersion | none          | Latest Android client version. | Get the latest Android client version available. Deprecated. |
| exit        | none          | nothing     | Closes the client's connection to the server. |

## Example ##

An example session showing the commands issued by a client.

```
getDBLastModTime
getDBURL
getLatestAndroidClientVersion /* Deprecated */
getBusTimesByStopCode:36232658
exit
```