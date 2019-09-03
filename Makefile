# Makefile for JAVA network programs

default: Server.class Client.class

# Server
Server.class: Server.java
	javac Server.java

# Client
Client.class: Client.java
	javac Client.java

clean:
	rm -rf *.class \
