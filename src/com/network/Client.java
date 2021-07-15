package com.network;

import com.cnp.CnpParts;
import com.gui.ClientController;
import com.gui.ClientView;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class Client {

	private String inputPath;
	private String outputPath;
	private ClientController controller;

	public Client(ClientController controller) {
		this.controller = controller;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getInputPath() {
		return inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void requestProcess() {
		if (this.inputPath == null || this.outputPath == null) {
			ClientView.showErrorMessage("Client error: input or output paths are not set");

			return;
		}

		Socket s = null;
		try {
			s = new Socket("localhost", 11111);
		} catch (IOException e) {
			ClientView.showErrorMessage("Client error: error while creating socket");

			return;
		}

		PrintWriter out = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			ClientView.showErrorMessage("Client error: error while creating in/out streams");

			return;
		}

		out.println(inputPath);
		out.println(outputPath);
		out.flush();

		ObjectInputStream inClient = null;
		try {
			inClient = new ObjectInputStream(s.getInputStream());
			Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers = (Map<CnpParts, ArrayList<BigDecimal>>) inClient.readObject();

			ClientView.showInformationMessage("Payments successfully processed");

			controller.receiveMapOfCustomers(mapOfCustomers);
		} catch (IOException | ClassNotFoundException e) {
			ClientView.showErrorMessage("Client error: error while recreating map of customers");

			controller.setRequestProcessActive(false);
			controller.setShowMetricesActive(false);
			controller.setShowDataActive(false);
		}

		try {
			out.close();
			inClient.close();
			s.close();
		} catch (IOException e) {
			ClientView.showErrorMessage("Client error: error while reading server answer");
		}
	}

}
