# Complex Event Processing Service

The __Complex Event Processing Service__ (CEPS) is a Java based web service which is capable of receiving arbitrary data from outside sources and processing this data via client defined rules. 

It is currently actively being used for sending alarms about water levels in Germany to the Android application [Pegel Alarm](http://pegelalarm.de) (available also on [GitHub](https://github.com/jvalue/hochwasser-app)).

## Modules

The CEPS is divided into a number of gradle modules:

- `server`: the main server application, built using [Dropwizard](http://www.dropwizard.io/)
- `models`: domain models
- `client-retrofit`: a Java client implementation for the REST API of the CEPS


## Setup

In its current form the CEPS is specialized for receiving data from the [Open Data Service](https://github.com/jvalue/open-data-service) (ODS), an open source web service capable of gathering data from many different sources.

For setting up the CEPS please refer to the [setup section](https://github.com/jvalue/open-data-service#setup) of the ODS. Configuration wise the main differences are:

- `url`: URL of the running CEPS instance. Required by the CEPS to receive data callbacks from other services
- `ods`: credentials required to register the CEPS as a client on the ODS to receive data callbacks


## Usage

Much like the ODS there are three primary ways of getting a feeling for the REST api of the CEPS:

- the REST api [implementation](https://github.com/jvalue/cep-service/tree/master/server/src/main/java/org/jvalue/ceps/rest)
- the [Postman collection](https://www.getpostman.com/collections/ca5a4471fe02fe198a06)
- the Java client implementation (see below)

In order for the Postman collection to work, Postman needs to know a couple of things (e.g. passwords, urls, etc.), which can be set by importing a so called environment file that looks like this:

```json
{
	"id": "b9d8ff70-9540-e7c6-e423-d8cd74d9c6e3",
	"name": "ods-localhost",
	"values": [
		{
			"key": "ceps_base_url",
			"value": "http://localhost:8082/ceps/api/v1",
			"type": "text",
			"name": "ceps_base_url",
			"enabled": true
		},
		{
			"key": "ceps_admin_username",
			"value": "admin@adminland.com",
			"type": "text",
			"name": "ceps_admin_username",
			"enabled": true
		},
		{
			"key": "ceps_admin_password",
			"value": "admin123",
			"type": "text",
			"name": "ceps_admin_password",
			"enabled": true
		}
	],
	"timestamp": 1436519975279,
	"synced": false,
	"syncedFilename": ""
}
```

## Client implementation

Just like the ODS, the CEPS comes equipped with a fully function Java client implementation which as generated using [JaxRs2Retrofit](https://github.com/Maddoc42/JaxRs2Retrofit). For gradle users:

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'org.jvalue.ceps:client-retrofit:0.1.1'
}
```

## License

Copyright 2014, 2015 Friedrich-Alexander Universität Erlangen-Nürnberg

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program. If not, see http://www.gnu.org/licenses/.
