package com.network;

import com.pay.PayMetricsProcessor;

import java.io.*;
import java.net.Socket;

public class ClientHandle extends Thread {

	Socket client;

	public ClientHandle(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Server error: error while creating in/out streams");

			// ! Nem akarjuk, hogy folytatódjon a folyamat, ha nincs honnan beolvasni az adatokat
			return;
		}

		FileInputStream paymentsInputStream = null;
		try {
			paymentsInputStream = new FileInputStream(in.readLine());
		} catch (IOException e) {
			System.err.println("Server error: error while finding input tranzactions");
		}

		FileOutputStream paymentsOutputStream = null;
		try {
			paymentsOutputStream = new FileOutputStream(in.readLine());
		} catch (IOException e) {
			System.err.println("Server error: error while creating output JSON");
		}

		try {
			PayMetricsProcessor.getProcessor().process(paymentsInputStream, paymentsOutputStream);
		} catch (IOException e) {
			System.err.println("Server error: error while processing payments");
		}

		out.println("Successfully processed the payments");
		out.flush();

		try {
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			System.err.println("Server error: error while closing socket");
		}
	}

}
