sensor-web-harvester
====================

###New Custom Network/Offerings read more at the bottom

sensor-web-harvester is a Scala project that harvests sensor data from web sources. The data is then pushed to an SOS using the [sos-injection module](https://github.com/axiomalaska/sos-injection) project. SosInjector is a project that wraps an [Sensor Observation Service (SOS)](http://52north.org/communities/sensorweb/sos/). The sos-injection module provides Java classes to enter stations, sensors, and observations into an SOS.

sensor-web-harvester is used to fill an SOS with observations from many well-known sensor sources
(such as NOAA and NERRS). This project pulls sensor observation values from the source’s stations.
It then formats the data to be placed into the user’s SOS by using the sos-injector. The source stations
used are filtered by a chosen bounding box area. 

The current sources that observations are pulled from are:

* [HADS](http://dipper.nws.noaa.gov/hdsc/pfds/)
* [NDBC](http://www.ndbc.noaa.gov/)
* [NOAA NOS CO-OPS](http://tidesonline.nos.noaa.gov/)
* [NOAA Weather](http://www.nws.noaa.gov/)
* [RAWS](http://www.raws.dri.edu/)
* [SnoTel](http://www.wcc.nrcs.usda.gov/)
* [USGS Water](http://waterdata.usgs.gov/ak/nwis/uv)
* [NERRS](http://www.nerrs.noaa.gov/)
* [GLOS](http://glos.us)
* [STORET](http://waterqualitydata.us)


This project uses an H2 metadata database to store station information from the sources. The metadata database information is used to
retrieve observations from the stations' sources. The database is file based and autogenerated if it doesn't already exist.

This project can be executed by running the pre-built jar with the command line (see "Running the SOS Injector") or
by writing custom Java code (see "Writing Custom Java Code").


Installation
------------
This project can be used on either a Windows or Linux computer. An Apple computer is expected to work however
it has not been tested. 

The following are the requirements to run this project:
* Java 1.6 or newer 
* An already running instance of an [IOOS Customized 52 North SOS](http://ioossostest.axiomalaska.com)


Configuring the SOS Injector
-----------
The pre-built sensor-web-harvester.jar and example_sos.properties can be downloaded from the 
[Downloads folder](https://github.com/axiomalaska/sensor-web-harvester/tree/master/download) on Github. 

The command line takes in a properties file which contains all of the needed variables to perform an SOS update.  The properties file requires the following variables:
```
# The URL where the H2 metadata database should be stored
database_url = jdbc:h2:/usr/local/sensor_web_harvester

# The URL to the SOS being used.
sos_url = http://localhost:8080/52n-sos-ioos/sos

# The publisher's country
publisher_country = USA

# The publisher's email address
publisher_email = publisher@example.com

# The web address of the publisher
publisher_web_address = http://example.org

# The name of the publishing organization
publisher_name = RA

# The northernmost latitude of the bounding box
north_lat = 50.0

# The southernmost latitude of the bounding box
south_lat = 40.0

# The westernmost longitude of the bounding box
west_lon = -93.0

# The easternmost longitude of the bounding box
east_lon = -75.0

# The network root for the default network in the SOS that contains all the stations. This network is different for each SOS. For example for AOOS the defaut network is urn:ioos:network:aoos:all.  The "network_root_id" is "all" and the "network_root_source_id" is "aoos".
network_root_id = all
network_root_source_id = aoos

# semi-colon seperated value list of sources that are to be updated (optional: this will default to 'all' if it is not the properties file)
# sources = all - this will operate on all known sources
# sources = nerrs;storet;glos - this will operate on the nerrs, storet and glos sources
# Accepted values: all, glos, hads, ndbc, nerrs, noaa_nos_coops, noaaweather, raws, snotel, storet, usgswater
sources = all
```

An example of a properties file named `example_sos.properties` is also provided on Github at the [Downloads Folder](https://github.com/axiomalaska/sensor-web-harvester/tree/master/download).


Running the SOS Injector
-----------
#### Note: Running these processes can take a long time (hours) as information is downloaded and extracted from many sources.

The sensor-web-harvester has three modes:

### metadata
This mode harvests from the source(s) defined in the properties file and updates the metadata database.
This command should be run conservatively (approx. 3 times a week) since the sources’ stations do not change often and this command is taxing on the sources’ servers.

```bash
java -jar sensor-web-harvester.jar -metadata [path to properties file]
```

### writeiso
This mode writes ISO 19115-2 metadata files based on data in the metadata database

```bash
java -jar sensor-web-harvester.jar -writeiso [path to properties file]
```

### updatesos
This mode downloads data from the sources and injects the data into the 52N instance specified in the properties file.
Do not call this command more than once hourly (for reasons previously stated).

Example: 
```bash
java -jar sensor-web-harvester.jar -updatesos [path to properties file]
```


Writing Custom Java Code
-----------
This is example code demonstrating how to update the metadata database and the SOS from within custom Java code.

```java
// Southern California Bounding Box
Location southWestCorner = new Location(32.0, -123.0);
Location northEastCorner = new Location(35.0, -113.0);
BoundingBox boundingBox = new BoundingBox(southWestCorner, northEastCorner);

String databaseUrl = "jdbc:postgresql://localhost:5432/sensor";
String databaseUsername = "sensoruser";
String databasePassword = "sensor";
String sosUrl = "http://localhost:8080/sos/sos";

MetadataDatabaseManager metadataManager = new MetadataDatabaseManager(databaseUrl, 
  databaseUsername, databasePassword, boundingBox)

// Updates the local metadata database with station information
// This call should be made conservatively (approx. 3 times a week) since the 
// sources’ stations do not change often and this call is taxing on the sources’ servers.
metadataManager.update();

// Information about the group publishing this data on the SOS. 
PublisherInfoImp publisherInfo = new PublisherInfoImp();
publisherInfo.setCountry("USA");
publisherInfo.setEmail("publisher@domain.com");
publisherInfo.setName("IOOS");
publisherInfo.setWebAddress("http://www.ioos.gov/");

SosNetworkImp rootNetwork = new SosNetworkImp()
rootNetwork.setId("all")
rootNetwork.setSourceId("aoos")

SosSourcesManager sosManager = new SosSourceManager(databaseUrl, 
  databaseUsername, databasePassword, sosUrl, publisherInfo, rootNetwork);
  
// Updates the SOS with data pulled from the source sites. 
// This uses the metadata database
// Most of the data is hourly. The data should be pulled conservatively (approx. hourly) 
// since the observations do not change often and this action is taxing on the sources’ servers.
sosManager.updateSos();
```


Create Custom Networks for Sources or Stations
-----------
To create custom networks/offerings one must adjust three tables (network, network_source, network_station) in the metadata database. All new networks need to be created and associated to source or stations before they are submitted to the SOS. Meaning that if a station is already created on the SOS it cannot later be associated to a network. 

First step, each custom network needs be added to the network table. The tag and source_tag columns are the main columns that need filled in for a new network. The tag and source_tag are combined (urn:ioos:network:[source_tag]:[tag]) to create the id of the network in the SOS. 

These custom networks can be assoicated to all stations of a source with the use of the network_source table. To associate a network with a source, place the source's database id and the network's database id in a row. 

These custom networks can be associated to specific stations from the network_station table. A row needs to be created for each station that a network is assoicated to. In each of these rows add the network id and the station id to be associated. 


List of Sources URLs 
-----------

### [HADS](http://dipper.nws.noaa.gov/hdsc/pfds/)
#### Pull stations Information
* http://amazon.nws.noaa.gov/hads/goog_earth/ - used to get the list of state URLs
* http://amazon.nws.noaa.gov/cgi-bin/hads/interactiveDisplays/displayMetaData.pl?table=dcp&nesdis_id= - used to pull station information. Needs the stations id or foreign id at the end of the URL

#### Observation Retrieval
* http://amazon.nws.noaa.gov/nexhads2/servlet/DecodedData - pull observations for a station. A POST request with the needed values pairs of:

1. state = nil
1. hsa = nil
1. of = 1
1. nesdis_ids = [station id]
1. sinceday = [number of days of observations requested]

### [NDBC](http://www.ndbc.noaa.gov/)
This source has an SOS service that is used to pull station information and observation data.

http://opendap.co-ops.nos.noaa.gov/ioos-dif-sos/SOS

### [NOAA NOS CO-OPS](http://tidesonline.nos.noaa.gov/)
This source has an SOS service that is used to pull station information and observation data.

http://opendap.co-ops.nos.noaa.gov/ioos-dif-sos/SOS

### [NOAA Weather](http://www.nws.noaa.gov/)
#### Pull stations Information
* http://weather.noaa.gov/data/nsd_cccc.txt

#### Observation Retrieval
* http://www.nws.noaa.gov/data/obhistory/[station id].html

### [RAWS](http://www.raws.dri.edu/)
#### Pull stations Information
* http://www.raws.dri.edu/cgi-bin/wea_info.pl?[station id] - get station information 
* List of URLs for each state used to find stations 

1. http://www.raws.dri.edu/aklst.html  
1. http://www.raws.dri.edu/azlst.html 
1. http://www.raws.dri.edu/ncalst.html 
1. http://www.raws.dri.edu/ccalst.html  
1. http://www.raws.dri.edu/scalst.html  
1. http://www.raws.dri.edu/colst.html  
1. http://www.raws.dri.edu/hilst.html  
1. http://www.raws.dri.edu/nidwmtlst.html  
1. http://www.raws.dri.edu/sidlst.html  
1. http://www.raws.dri.edu/emtlst.html 
1. http://www.raws.dri.edu/nidwmtlst.html  
1. http://www.raws.dri.edu/nvlst.html  
1. http://www.raws.dri.edu/nmlst.html  
1. http://www.raws.dri.edu/orlst.html  
1. http://www.raws.dri.edu/utlst.html  
1. http://www.raws.dri.edu/walst.html  
1. http://www.raws.dri.edu/wylst.html  
1. http://www.raws.dri.edu/illst.html  
1. http://www.raws.dri.edu/inlst.html 
1. http://www.raws.dri.edu/ialst.html  
1. http://www.raws.dri.edu/kslst.html  
1. http://www.raws.dri.edu/ky_tnlst.html  
1. http://www.raws.dri.edu/mi_wilst.html  
1. http://www.raws.dri.edu/mnlst.html  
1. http://www.raws.dri.edu/molst.html  
1. http://www.raws.dri.edu/nelst.html  
1. http://www.raws.dri.edu/ndlst.html  
1. http://www.raws.dri.edu/ohlst.html  
1. http://www.raws.dri.edu/sdlst.html  
1. http://www.raws.dri.edu/mi_wilst.html  
1. http://www.raws.dri.edu/al_mslst.html  
1. http://www.raws.dri.edu/arlst.html  
1. http://www.raws.dri.edu/fllst.html  
1. http://www.raws.dri.edu/ga_sclst.html  
1. http://www.raws.dri.edu/lalst.html  
1. http://www.raws.dri.edu/nclst.html  
1. http://www.raws.dri.edu/oklst.html  
1. http://www.raws.dri.edu/txlst.html  
1. http://www.raws.dri.edu/prlst.html  
1. http://www.raws.dri.edu/ct_ma_rilst.html  
1. http://www.raws.dri.edu/de_mdlst.html  
1. http://www.raws.dri.edu/me_nh_vtlst.html  
1. http://www.raws.dri.edu/nj_palst.html  
1. http://www.raws.dri.edu/nylst.html  
1. http://www.raws.dri.edu/va_wvlst.html

#### Observation Retrieval
* http://www.raws.dri.edu/cgi-bin/wea_list2.pl - pull observations for a station. A POST request with the needed values pairs of:

1. stn = [station id]
1. smon = [start month - two digit Integer]
1. sday = [start day of month - two digit Integer]
1. syea = [start year - two digit Integer]
1. emon = [end month - two digit Integer]
1. eday = [end day of month - two digit Integer]
1. eyea = [end year - two digit Integer]
1. dfor = 02
1. srce = W
1. miss = 03
1. flag = N
1. Dfmt = 02
1. Tfmt = 01
1. Head = 02
1. Deli = 01
1. unit = M
1. WsMon = 01
1. WsDay = 01
1. WeMon = 12
1. WeDay = 31
1. WsHou = 00
1. WeHou = 24

### [SnoTel](http://www.wcc.nrcs.usda.gov/)
#### Pull stations Information
* http://www.wcc.nrcs.usda.gov/ftpref/data/water/wcs/earth/snotelwithoutlabels.kmz - contains a list of all the stations 
* http://www.wcc.nrcs.usda.gov/nwcc/sensors - list the sensor of a station. A POST request with the needed values pairs of:

1. sitenum = [station id]

#### Observation Retrieval
* http://www.wcc.nrcs.usda.gov/nwcc/view - pull observations for a station. A POST request with the needed values pairs of:

1. time_zone = PST
1. sitenum = [station id]
1. timeseries = Hourly
1. interval = WEEK
1. format = copy
1. report = ALL

### [USGS Water](http://waterdata.usgs.gov/ak/nwis/uv)
#### Pull stations Information
* http://waterservices.usgs.gov/nwis/iv?stateCd=[state tag]&period=PT4H
* state tags = "al", "ak", "aq", "az", "ar", "ca", "co", "ct", "de", "dc", "fl", "ga", "gu", "hi", "id", "il", "in", "ia", "ks", "ky", "la", "me", "md", "ma", "mi", "mn", "ms", "mo", "mt", "ne", "nv", "nh", "nj", "nm", "ny", "nc", "nd", "mp", "oh", "ok", "or", "pa", "pr", "ri", "sc", "sd", "tn", "tx", "ut", "vt", "vi", "va", "wa", "wv", "wi", "wy"

#### Observation Retrieval
* http://waterservices.usgs.gov/nwis/iv?sites=[station id]&parameterCd=[sensor ids]&startDT=[start date formated as yyyy-MM-dd'T'HH:mm:ss'Z']&endDT=[end date formated as yyyy-MM-dd'T'HH:mm:ss'Z']

### [NERRS](http://www.nerrs.noaa.gov/)
This source has a webservice end point at http://cdmo.baruch.sc.edu/webservices2/requests.cfc?wsdl that is used to pull the station information and the observations for each station. 

A java jar was create to work with this webservice in java. Below shows references needed to use this jar in Maven or it can be downloaded at http://nexus.axiomalaska.com/nexus/content/repositories/public/com/axiomalaska/nerrs_webservice/1.0.0/nerrs_webservice-1.0.0.jar

#### Maven Dependency

```xml
<repository>
  <id>axiom_public_releases</id>
  <name>Axiom Releases</name>
  <url>http://nexus.axiomalaska.com/nexus/content/repositories/public/</url>
</repository>
<dependency>
  <groupId>com.axiomalaska</groupId>
  <artifactId>nerrs_webservice</artifactId>
  <version>1.0.0</version>
</dependency>
```

