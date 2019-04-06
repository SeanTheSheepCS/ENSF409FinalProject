package server.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
 * BUGS/FEATURES to fix/finish:
 * IP Address Server initialization - automatically configures to IP. 
 * Split into 2 branches in git. 
 */

/**
 * Server is simple socket server that starts a CommunicationsManager/Client
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 */
public class Server {
	/**
	 * serverSocket is socket server uses, and pool that contains threads for
	 * clients.
	 */
	private ServerSocket serverSocket;
	private ExecutorService pool;

	/**
	 * c-tor, initializes pool and server socket, connects to port 9898.
	 */
	public Server() {
		try {
			pool = Executors.newFixedThreadPool(5);
			serverSocket = new ServerSocket(9898);
		} catch (IOException e) {
			System.out.println("\nCreate new socket error");
			System.out.println(e.getMessage());
		}
		System.out.println("Server is running");
	}

	/**
	 * c-tor, initializes pool and server socket, connects to IP address provided.
	 * 
	 * @param port        on computer to connect to.
	 * @param backlog     is amount of clients to keep in queue.
	 * @param bindAddress is IP address.
	 */
	public Server(int port, int backlog, String bindAddress) {
		try {
			pool = Executors.newFixedThreadPool(2);
			serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(bindAddress));
		} catch (IOException e) {
			System.err.println("Create new socket error");
			System.out.println(e.getMessage());
		}
		System.out.println("Internet Server is running");
	}

	/**
	 * runs CommunicationManager whenever a successfull connection to a client is
	 * made.
	 * 
	 * closes all streams if exception is found.
	 */
	public void startCommunications() {
		try {
			while (true) {
				CommunicationsManager control = new CommunicationsManager(serverSocket.accept());
				System.out.println("Connected to a client");
				pool.execute(control);
			}
		} catch (IOException e) {
			System.out.println("Failed to connect to client to start communicating");
		} finally {
			closeAllStreams();
		}
	}

	/**
	 * closes the serverSocket and the pool.
	 */
	public void closeAllStreams() {
		try {
			serverSocket.close();
			pool.shutdown();
		} catch (IOException e) {
			System.out.println("Failed to close all streams in Server");
		}
	}

	public static void main(String[] args) {
		//Server server = new Server();
		Server server = new Server(9898, 5, "10.13.75.100");
		server.startCommunications();
	}

}
