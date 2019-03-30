package server.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket serverSocket;
	private ExecutorService pool;
	private Socket socket;

	public Server() {
		try {
			pool = Executors.newFixedThreadPool(2);
			serverSocket = new ServerSocket(9898);
		} catch (IOException e) {
			System.out.println("Create new socket error");
			System.out.println(e.getMessage());
		}
		System.out.println("Server is running");
	}

	public Server(int port, int backlog, InetAddress bindAddress) {
		try {
			pool = Executors.newFixedThreadPool(2);
			serverSocket = new ServerSocket(port, backlog, bindAddress);
		} catch (IOException e) {
			System.out.println("Create new socket error");
			System.out.println(e.getMessage());
		}
		System.out.println("Interner Server is running");
	}

	public void startCommunications() {
		try {
			while (true) {
				socket = serverSocket.accept();
				ControllerRun control = new ControllerRun(socket);
				pool.execute(control);
			}
		} catch (IOException e) {
			System.out.println("Failed to connect to client to start communicating");
		} finally {
			closeAllStreams();
		}
	}

	public void closeAllStreams() {
		try {
			socket.close();
			serverSocket.close();
			pool.shutdown();
		} catch (IOException e) {
			System.out.println("Failed to close all streams in Server");
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.startCommunications();
	}

}
