# Makefile for JAVA network programs

default: Server.class Client.class

# Server
chat_server.class: chat_server.java
	javac chat_server.java

# Client
chat_client.class: chat_client.java
	javac chat_client.java

ClientConnection.class: ClientConnection.java
	javac ClientConnection.java

InputThread.class: InputThread.java
	javac InputThreadn.java

InputUTFThread.class: InputUTFThread.java
	javac InputUTFThread.java

Server.class: Server.java
	javac Server.java

State.class: State.java
	javac State.java

StateUpdateThread.class: StateUpdateThread.java
	javac StateUpdateThread.java

clean:
	rm -rf *.class \
