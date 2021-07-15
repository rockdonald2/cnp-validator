package com.gui;

import com.cnp.CnpParts;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;

public class ClientView extends JFrame {

	private final JButton requestProcess;
	private final JButton showInputFileChooser;
	private final JButton showOutputFileChooser;
	private final JButton showData;
	private final JButton showMetrices;
	private final JPanel contentPanel;
	private final JList cnpList;
	private final JScrollPane scrollableList;
	private JMenuBar menuBar;

	private ClientController controller;

	public ClientView() {
		this.setTitle("Payments processor");

		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Options");
		menu.getAccessibleContext().setAccessibleDescription("Server instructions");
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("Stop server");
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.stopServer();
				System.out.println("stopping server");
			}
		});

		this.requestProcess = new JButton("Request process of payments");
		this.requestProcess.setEnabled(false);

		this.showInputFileChooser = new JButton("Input .csv file");
		this.showOutputFileChooser = new JButton("Output .json file");
		this.showData = new JButton("Show information");
		this.showData.setEnabled(false);
		this.showMetrices = new JButton("Show metrices");
		this.showMetrices.setEnabled(false);

		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(6, 1));
		contentPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));

		this.setJMenuBar(menuBar);

		contentPanel.add(showInputFileChooser);
		contentPanel.add(showOutputFileChooser);
		contentPanel.add(requestProcess);

		cnpList = new JList();
		scrollableList = new JScrollPane(cnpList);
		contentPanel.add(scrollableList);
		contentPanel.add(showData);
		contentPanel.add(showMetrices);

		this.showInputFileChooser.addActionListener(e -> {
			controller.setInputPath(showChooser());
		});

		this.showOutputFileChooser.addActionListener(e -> {
			controller.setOutputPath(showChooser());
		});

		this.requestProcess.addActionListener(e -> {
			controller.requestProcess();
		});

		this.showData.addActionListener(e -> {
			controller.requestShowData((CnpParts) cnpList.getSelectedValue());
		});

		this.showMetrices.addActionListener(e -> {
			controller.requestShowMetrices();
		});

		this.setContentPane(contentPanel);
	}

	private File showChooser() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}

		return null;
	}

	public static void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showInformationMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	public JButton getShowInputFileChooser() {
		return showInputFileChooser;
	}

	public JButton getShowOutputFileChooser() {
		return showOutputFileChooser;
	}

	public JButton getShowData() {
		return showData;
	}

	public JButton getShowMetrices() {
		return showMetrices;
	}

	public JButton getRequestProcess() {
		return requestProcess;
	}

	public JList getCnpList() {
		return cnpList;
	}

	public void showClientData(CnpParts client, BigDecimal[] values) {
		JDialog modalFrame = new JDialog(ClientView.this, "Information", true);
		modalFrame.setLayout(new GridLayout(6, 1));
		modalFrame.setPreferredSize(new Dimension(250, 250));

		modalFrame.add(new JLabel("CNP: " + client.cnp()));
		modalFrame.add(new JLabel("Sex: " + client.sex()));
		modalFrame.add(new JLabel("Foreigner: " + client.foreigner()));
		modalFrame.add(new JLabel("Birth county: " + client.county().getAbrv()));
		modalFrame.add(new JLabel("Birth date: " + client.birthDate()));

		JScrollPane tmpScrollable = new JScrollPane(new JList(values));
		modalFrame.add(tmpScrollable);

		modalFrame.pack();
		modalFrame.setVisible(true);
	}

	public void showMetricesData(JSONObject object) {
		JDialog modalFrame = new JDialog(ClientView.this, "Information", true);
		modalFrame.setLayout(new GridLayout(6, 1));
		modalFrame.setPreferredSize(new Dimension(350, 250));

		modalFrame.add(new JLabel("No. of payments by minor citizens: " + object.getInt("paymentsByMinor")));
		modalFrame.add(new JLabel("No. of small payments below 5000 RON: " + object.getInt("smallPayments")));
		modalFrame.add(new JLabel("No. of big payments above 5000 RON: " + object.getInt("bigPayments")));
		modalFrame.add(new JLabel("No. of foreign buyers: " + object.getInt("foreigners")));
		modalFrame.add(new JLabel("Average payment amount: " + object.getBigDecimal("averagePaymentAmount") + " RON"));
		modalFrame.add(new JLabel("Total amount capital city: " + object.getBigDecimal("totalAmountCapitalCity") + " RON"));

		modalFrame.pack();
		modalFrame.setVisible(true);
	}

	public void setController(ClientController controller) {
		this.controller = controller;
	}

}