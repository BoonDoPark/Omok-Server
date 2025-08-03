package com.example.omok;

import com.example.omok.socket.AsyncSocketServer;
import com.example.omok.socket.SocketServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OmokServerApplication implements CommandLineRunner {
	private final SocketServer socketServer;
	private final AsyncSocketServer asyncServerSocket;

    public OmokServerApplication(SocketServer socketServer, AsyncSocketServer asyncServerSocket) {
        this.socketServer = socketServer;
        this.asyncServerSocket = asyncServerSocket;
    }

    public static void main(String[] args) {
		SpringApplication.run(OmokServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		socketServer.start();
		asyncServerSocket.asyncServerSocket();
	}
}
