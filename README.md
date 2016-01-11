
Building 

```
mvn install
```


Usage example:

```
java -jar /Users/bryanhunt/.m2/repository/packetops/syslog-and-java/2.0-SNAPSHOT/syslog-and-java-2.0-SNAPS
HOT.jar --port 11513 --ip 127.0.0.1
Startup options
[ option: port port  :: listening port (standard 514, default 11514) ]
[ option: help  :: Print help information ]
[ option: ip ip  :: listening ip addrees (defaults to 127.0.0.1 ]
[ option: outdir outdir  :: file to store logfiles ]
 Listening on 127.0.0.1:11513 - logging requests to /tmp//loggger-*
Logging to : file:///tmp/logger-2016-01-11-11-02-43-482+0000

```
 
---

Sending messages using netcat

```

[~%]nc -u 127.0.0.1 11513
dsadfsaadfs

```

---


reading the logfile:

```
[~%]tail -f /tmp/logger-2016-01-11-10-57-42-673+0000
Mon Jan 11 10:57:50 GMT 2016:asdffads


Mon Jan 11 10:58:09 GMT 2016:sadffdsafda
^C
[~%]tail -f /tmp/logger-2016-01-11-10-58-50-437+0000
/127.0.0.1:57958:Mon Jan 11 10:59:02 GMT 2016:dsadfsaadfs
/127.0.0.1:50602:Mon Jan 11 10:59:18 GMT 2016:dsaffdsfads
```