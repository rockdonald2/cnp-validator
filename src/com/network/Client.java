package com.network;

import com.gui.ClientFrame;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {

	private String inputPath;
	private String outputPath;

	public Client() { }

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
			ClientFrame.showErrorMessage("Client error: input or out paths are not set");

			return;
		}

		Socket s = null;
		try {
			s = new Socket("localhost", 11111);
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Client error: error while creating socket");

			return;
		}

		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Client error: error while in/out streams");

			return;
		}

		out.println(inputPath);
		out.println(outputPath);
		out.flush();

		try {
			System.out.println("Server answer: " + in.readLine());

			ClientFrame.showInformationMessage("Payments successfully processed");
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Client error: error while reading server answer");

			return;
		}

		try {
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			ClientFrame.showErrorMessage("Client error: error while reading server answer");
		}
	}

	public static void main(String[] args) {
		ClientFrame cf = new ClientFrame(new Client());
		cf.setBounds(100, 100, 250, 200);
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cf.setVisible(true);
	}

}
