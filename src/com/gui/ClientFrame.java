package com.gui;

import com.cnp.CnpParts;
import com.network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class ClientFrame extends JFrame {

	private final JButton requestProcess;
	private final JButton showInputFileChooser;
	private final JButton showOutputFileChooser;
	private final JButton showData;
	private final JPanel contentPanel;
	private JList cnpList;
	private JScrollPane scrollableList;

	private String csvInputPath;
	private boolean wrongCsvInput = true;
	private String jsonOutputPath;
	private boolean wrongJsonInput = true;

	private Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers;

	public ClientFrame() {
		this.setTitle("Payments processor");

		this.requestProcess = new JButton("Request process of payments");
		this.requestProcess.setEnabled(false);

		this.showInputFileChooser = new JButton("Input .csv file");
		this.showOutputFileChooser = new JButton("Output .json file");
		this.showData = new JButton("Show information");
		this.showData.setEnabled(false);

		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(5, 1));
		contentPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));

		contentPanel.add(showInputFileChooser);
		contentPanel.add(showOutputFileChooser);
		contentPanel.add(requestProcess);

		cnpList = new JList();
		scrollableList = new JScrollPane(cnpList);
		contentPanel.add(scrollableList);
		contentPanel.add(showData);

		this.showInputFileChooser.addActionListener(e -> {
			wrongCsvInput = !handleInput(".csv", showInputFileChooser);
			requestProcess.setEnabled(!wrongJsonInput && !wrongCsvInput);
		});

		this.showOutputFileChooser.addActionListener(e -> {
			wrongJsonInput = !handleInput(".json", showOutputFileChooser);
			requestProcess.setEnabled(!wrongJsonInput && !wrongCsvInput);
		});

		this.requestProcess.addActionListener(e -> {
			if (!wrongCsvInput && !wrongJsonInput && csvInputPath != null && jsonOutputPath != null) {
				Client client = new Client();
				client.setInputPath(csvInputPath);
				client.setOutputPath(jsonOutputPath);
				client.setFrame(this);
				client.requestProcess();
			} else {
				if (csvInputPath == null || wrongCsvInput) {
					showInputFileChooser.setBackground(Color.RED);
				} else if (jsonOutputPath == null || wrongJsonInput) {
					showOutputFileChooser.setBackground(Color.RED);
				}

				requestProcess.setEnabled(false);
			}
		});

		this.showData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog modalFrame = new JDialog(ClientFrame.this, "Information", true);
				modalFrame.setLayout(new GridLayout(6, 1));
				modalFrame.setPreferredSize(new Dimension(250, 250));
				CnpParts current = (CnpParts) cnpList.getSelectedValue();

				modalFrame.add(new JLabel("CNP: " + current.cnp()));
				modalFrame.add(new JLabel("Sex: " + current.sex()));
				modalFrame.add(new JLabel("Foreigner: " + current.foreigner()));
				modalFrame.add(new JLabel("Birth county: " + current.county().getAbrv()));
				modalFrame.add(new JLabel("Birth date: " + current.birthDate()));

				JList tmpList = new JList(mapOfCustomers.get(current).toArray());
				JScrollPane tmpScrollable = new JScrollPane(tmpList);
				modalFrame.add(tmpScrollable);

				modalFrame.pack();
				modalFrame.setVisible(true);
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
					csvInputPath = fileChooser.getSelectedFile().getPath();
				} else if (extension.equals(".json")) {
					jsonOutputPath = fileChooser.getSelectedFile().getPath();
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

	public void sendMap(Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers) {
		this.mapOfCustomers = mapOfCustomers;
		this.showData.setEnabled(true);
		populateList();
	}

	private void populateList() {
		cnpList.setListData(mapOfCustomers.keySet().toArray());
		this.setVisible(true);
	}

}
