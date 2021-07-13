package com.gui;

import com.network.Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClientFrame extends JFrame {

	private final Client client;
	private final JButton requestProcess;
	private final JButton showInputFileChooser;
	private final JButton showOutputFileChooser;

	private boolean wrongCsvInput = false;
	private boolean wrongJsonInput = false;

	public ClientFrame(Client client) {
		this.client = client;

		this.setTitle("Payments processor");

		this.requestProcess = new JButton("Request process of payments");
		this.requestProcess.setEnabled(false);

		this.showInputFileChooser = new JButton("Input .csv file");
		this.showOutputFileChooser = new JButton("Output .json file");

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

		this.requestProcess.setPreferredSize(new Dimension(this.getWidth(), 50));
		this.showInputFileChooser.setPreferredSize(new Dimension(this.getWidth(), 50));
		this.showOutputFileChooser.setPreferredSize(new Dimension(this.getWidth(), 50));

		contentPanel.add(showInputFileChooser, BorderLayout.NORTH);
		contentPanel.add(showOutputFileChooser, BorderLayout.CENTER);
		contentPanel.add(requestProcess, BorderLayout.SOUTH);

		this.showInputFileChooser.addActionListener(e -> {
			wrongCsvInput = !handleInput(".csv", showInputFileChooser);
			requestProcess.setEnabled(!wrongJsonInput && !wrongCsvInput);
		});

		this.showOutputFileChooser.addActionListener(e -> {
			wrongJsonInput = !handleInput(".json", showOutputFileChooser);
			requestProcess.setEnabled(!wrongJsonInput && !wrongCsvInput);
		});

		this.requestProcess.addActionListener(e -> {
			if (!wrongCsvInput && !wrongJsonInput) {
				System.out.println(client.getInputPath());
				System.out.println(client.getOutputPath());

				client.requestProcess();
			}
		});

		this.setContentPane(contentPanel);
	}

	private boolean handleInput(String extension, JButton button) {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			if (!selectedFile.getName().contains(extension)) {
				showErrorMessage("Usage error: Wrong file extension");
				button.setBackground(Color.RED);

				return false;
			} else {
				if (extension.equals(".csv")) {
					client.setInputPath(fileChooser.getSelectedFile().getPath());
				} else if (extension.equals(".json")) {
					client.setOutputPath(fileChooser.getSelectedFile().getPath());
				}

				button.setBackground(Color.GREEN);

				return true;
			}
		}

		return false;
	}

	public static void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showInformationMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

}
