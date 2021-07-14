package com.network;

import com.cnp.CnpParts;
import com.gui.ClientFrame;
import com.pay.PayMetricsProcessor;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

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
			ClientFrame.showErrorMessage("Server error: error while creating in/out streams");
		}

		FileInputStream paymentsInputStream = null;
		try {
			paymentsInputStream = new FileInputStream(in.readLine());
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Server error: error while finding input tranzactions");

			return;
		}

		FileOutputStream paymentsOutputStream = null;
		try {
			paymentsOutputStream = new FileOutputStream(in.readLine());
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Server error: error while finding output file");

			return;
		}

		Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers = null;
		try {
			mapOfCustomers = PayMetricsProcessor.getProcessor().process(paymentsInputStream, paymentsOutputStream);
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Server error: error while processing payments");
		}

		out.println("Successfully processed the payments");
		out.flush();

		ObjectOutputStream outClient = null;
		try {
			outClient = new ObjectOutputStream(client.getOutputStream());
			outClient.writeObject(mapOfCustomers);
			outClient.flush();
		} catch (IOException e) {
			System.out.println("Server: " + e);
			ClientFrame.showErrorMessage("Server error: error while serializing map of customers");
		}

		try {
			in.close();
			out.close();
			client.close();
			outClient.close();
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Server error: error while closing socket");
		}
	}

}
