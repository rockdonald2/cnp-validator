package com.pay;

import com.cnp.CnpParts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class PayUtils {

	static int getTotalTranzactionNumber(Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers) {
		int counter = 0;

		for (var customer : mapOfCustomers.keySet()) {
			for (var v : mapOfCustomers.get(customer)) {
				counter++;
			}
		}

		return counter;
	}

	static BigDecimal sumTranzactions(Map<CnpParts, ArrayList<BigDecimal>> mapOfCustomers) {
		var sum = BigDecimal.ZERO;

		for (var customer : mapOfCustomers.keySet()) {
			for (var v : mapOfCustomers.get(customer)) {
				sum = sum.add(v);
			}
		}

		return sum;
	}

}
