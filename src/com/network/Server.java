package com.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private static boolean IS_RUNNING = true;

	public static void main(String[] args) {
		try {
			var ss = new ServerSocket(11111);

			while (IS_RUNNING) {
				(new ClientHandle(ss.accept())).start();
			}
		} catch (IOException e) {
			System.err.println("Error while creating ServerSocket");
		}
	}

}
