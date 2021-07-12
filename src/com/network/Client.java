package com.network;

import java.io.*;
import java.net.Socket;

public class Client {

	private final String inputPath;
	private final String outputPath;

	public Client(String inputPath, String outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;

		requestProcess();
	}

	private void requestProcess() {
		Socket s = null;
		try {
			s = new Socket("localhost", 11111);
		} catch (IOException e) {
			System.err.println("Client error: error while creating socket");

			System.exit(1);
		}

		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Client error: error while in/out streams");

			System.exit(1);
		}

		out.println(inputPath);
		out.println(outputPath);
		out.flush();

		try {
			System.out.println("Server answer: " + in.readLine());
		} catch (IOException e) {
			System.err.println("Client error: error while reading server answer");

			System.exit(1);
		}

		try {
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			System.err.println("Client error: error while reading server answer");
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: Client <<input>.csv> <<output>.json>");

			System.exit(1);
		}

		new Client(args[0], args[1]);
	}

}
