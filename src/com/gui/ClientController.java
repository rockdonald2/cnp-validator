package com.gui;

import com.cnp.CnpParts;
import com.network.Client;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class ClientController {

	private ClientModel model;
	private ClientView view;

	public ClientController() {
		this.model = new ClientModel();
		this.view = new ClientView();

		this.view.setController(this);
		this.view.setBounds(100, 100, 400, 400);
		this.view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.view.setVisible(true);
	}

	public void setInputPath(File file) {
		if (file == null) {
			ClientView.showErrorMessage("Usage error: no file selected");
			setInputButtonColor(Color.RED);

			return;
		}

		if (!file.getName().contains(".csv")) {
			ClientView.showErrorMessage("Usage error: Wrong file extension");
			model.setWrongCsvInput(true);
			setInputButtonColor(Color.RED);
		} else {
			model.setCsvInputPath(file.getAbsolutePath());
			model.setWrongCsvInput(false);
			setInputButtonColor(Color.GREEN);

			setRequestProcessActive(!model.isWrongJsonInput());
		}
	}

	public void setOutputPath(File file) {
		if (file == null) {
			ClientView.showErrorMessage("Usage error: no file selected");
			setOutputButtonColor(Color.RED);

			return;
		}

		if (!file.getName().contains(".json")) {
			ClientView.showErrorMessage("Usage error: Wrong file extension");
			model.setWrongJsonInput(true);
			setOutputButtonColor(Color.RED);
		} else {
			model.setWrongJsonInput(false);
			model.setJsonOutputPath(file.getAbsolutePath());
			setOutputButtonColor(Color.GREEN);

			setRequestProcessActive(!model.isWrongCsvInput());
		}
	}

	public void setInputButtonColor(Color color) {
		view.getShowInputFileChooser().setBackground(color);
	}

	public void setOutputButtonColor(Color color) {
		view.getShowOutputFileChooser().setBackground(color);
	}

	public void requestProcess() {
		if (model.validInputs()) {
			Client client = new Client();
			client.setInputPath(model.getCsvInputPath());
			client.setOutputPath(model.getJsonOutputPath());
			client.setController(this);
			client.requestProcess();
		} else {
			if (model.isWrongCsvInput()) {
				setInputButtonColor(Color.RED);
			} else if (model.isWrongJsonInput()) {
				setOutputButtonColor(Color.RED);
			}

			view.getRequestProcess().setEnabled(false);
		}
	}

	public void requestShowData(CnpParts client) {
		if (client == null) {
			ClientView.showInformationMessage("Please select a CNP first");

			return;
		}

		view.showClientData(client, model.getClientPayments(client));
	}

	public void requestShowMetrices() {
		InputStream is = null;
		try {
			is = new FileInputStream(model.getJsonOutputPath());
		} catch (FileNotFoundException fileNotFoundException) {
			ClientView.showErrorMessage("Usage error: .json output file not found");
		}

		view.showMetricesData(new JSONObject(new JSONTokener(is)));
	}

	public void receiveMapOfCustomers(Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers) {
		model.setMapOfCustomers(mapOfCustomers);
		populateList();
	}

	private void populateList() {
		view.getCnpList().setListData(model.getMapOfCustomers().keySet().toArray());
		setShowDataActive(true);
		setShowMetricesActive(true);
		updateView();
	}

	public void setRequestProcessActive(boolean flag) {
		view.getRequestProcess().setEnabled(flag);
	}

	public void setShowDataActive(boolean flag) {
		view.getShowData().setEnabled(flag);
	}

	public void setShowMetricesActive(boolean flag) {
		view.getShowMetrices().setEnabled(flag);
	}

	public void updateView() {
		view.setVisible(true);
	}

	public static void main(String[] args) {
		ClientController controller = new ClientController();
	}

}
