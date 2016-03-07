# Introduction #

This document outlines the layout of the JSON text which will transfer the bus stop live data between the server and the client.


# Layout #

The format of the JSON text.

Source - [JSON Wikipedia Page](http://en.wikipedia.org/wiki/JSON), [The official JSON website](http://www.json.org).

```
{
	"stopCode": "<stop code>",
	"stopName": "<stop name>",
	"services": [
		{ "serviceName": "<service name>", "route": "<service route>", "buses": [
				{ "destination": "<destination>", "arrivalTime": "<arrival time>", "accessible": <true/false> }
			]
		}
	]
}
```

# Example #

A real world example demonstrating the JSON layout.

```
{
	"stopCode": "36232658",
	"stopName": "Haymarket Satio",
	"services": [
		{ "serviceName": "3", "route": "Mayfield -- Clovenstone", "buses": [
				{ "destination": "CLOVENSTONE", "arrivalTime": "5", "accessible": true },
				{ "destination": "CLOVENSTONE", "arrivalTime": "*30", "accessible": false }
			]
		},
		{ "serviceName": "25", "route": "Riccarton -- Restalrig", "buses": [
				{ "destination": "RICCARTON", "arrivalTime": "*12", "accessible": false },
				{ "destination": "RICCARTON", "arrivalTime": "42", "accessible": true }
			]
		},
		{ "serviceName": "33", "route": "Westburn -- Ferniehill", "buses": [
				{ "destination": "BABERTON", "arrivalTime": "22", "accessible": true },
				{ "destination": "LONGSTONE", "arrivalTime": "45", "accessible": true }
			]
		}
	]
}
```