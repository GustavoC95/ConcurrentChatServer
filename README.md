#Concurrent Chat Server
Simple multiple client chat server.

##Technologies
Project is created with:
* Java 1.7
* Java Socket
* Java IO

##Setup
To build this project using ant:
```
$ git clone https://github.com/GustavoC95/ConcurrentChatServer.git
$ You will need a build tool (Ant, Maven, etc..).
$ Go to the folder that has the build.xml file and make the build tool run build.xml.
$ build.xml has all the information to package all project into a single executable file.
```

To test this project:
```
$ Run the executable file (java -jar ChatServer.jar || run normally through OS GUI)
$ nc localhost 8080 //on a terminal to connect a client to the running server
$ Chat away!
```