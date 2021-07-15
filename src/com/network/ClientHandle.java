package com.network;

import com.cnp.CnpParts;
import com.gui.ClientView;
import com.pay.PayMetricsProcessor;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ClientHandle extends Thread {

	Socket client;

	private boolean IS_RUNNING = true;

	public ClientHandle(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while creating in/out streams");

			return;
		}

		FileInputStream paymentsInputStream = null;
		try {
			paymentsInputStream = new FileInputStream(in.readLine());
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while finding input tranzactions");

			return;
		}

		FileOutputStream paymentsOutputStream = null;
		try {
			paymentsOutputStream = new FileOutputStream(in.readLine());
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while finding output file");

			return;
		}

		Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers = null;
		try {
			mapOfCustomers = PayMetricsProcessor.getProcessor().process(paymentsInputStream, paymentsOutputStream);
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while processing payments");

			return;
		}

		ObjectOutputStream outClient = null;
		try {
			outClient = new ObjectOutputStream(client.getOutputStream());
			outClient.writeObject(mapOfCustomers);
			outClient.flush();
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while serializing map of customers");
		}

		try {
			in.close();
			outClient.close();
			client.close();
		} catch (IOException e) {
			ClientView.showErrorMessage("Server error: error while closing socket");
		}
	}

}
