package ro.axonsoft.internship21.pay;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import ro.axonsoft.internship21.cnp.CnpException;
import ro.axonsoft.internship21.cnp.CnpParts;
import ro.axonsoft.internship21.cnp.CnpValidatorImpl;

class PayMetricsProcessorImpl implements PayMetricsProcessor {

	private final Set<PayError> m_errors = new HashSet<>();

	@Override
	public void process(InputStream paymentsInputStream, OutputStream metricsOutputStream) throws IOException {
		var dataInput = loadData(paymentsInputStream);

		var listOfCustomers = getCustomers(dataInput);

		PayMetrics metrics;
		if (listOfCustomers.size() != 0) {
			var averagePaymentAmount = getAverage(listOfCustomers);

			var bigPayments = getBigPaymentsNumber(listOfCustomers);

			var paymentsByMinors = getPaymentsByMinors(listOfCustomers);

			var smallPayments = getSmallPaymentsNumber(listOfCustomers);

			var totalAmountCapitalCity = getTotalAmountCapitalCity(listOfCustomers);

			var foreigners = getForeigners(listOfCustomers);

			metrics = PayMetrics.getMetrics(foreigners, paymentsByMinors, bigPayments,
							smallPayments, averagePaymentAmount, totalAmountCapitalCity, m_errors);
		} else {
			metrics = PayMetrics.getMetrics(0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, m_errors);
		}

		getObjectMapper().writeValue(metricsOutputStream, metrics);
	}

	/**
	 * Returneaza ObjectMapper-ul prin care instanta PayMetrics poate fi serializata.
	 *
	 * @return
	 *          ObjectMapper
	 */
	private ObjectMapper getObjectMapper() {
		var objectMapper = new ObjectMapper();

		SimpleModule module =
						new SimpleModule("metricsSerializer", new Version(1, 0, 0, null, null, null));
		module.addSerializer(PayMetrics.class, new PayMetricsImpl.metricsSerializer());
		objectMapper.registerModule(module);

		return objectMapper;
	}

	/**
	 * Visszatéríti azon fizetések számát, amelyet 18 év alattiak intéztek.
	 *
	 * @param listOfCustomers
	 *                          tranzakciók
	 * @return
	 *          fizetések száma
	 */
	int getPaymentsByMinors(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		int counter = 0;
		final var currentYear = Calendar.getInstance().get(Calendar.YEAR);

		for (var customer : listOfCustomers) {
			if (currentYear - customer.getLeft().birthDate().year() <= 18) {
				counter++;
			}
		}

		return counter;
	}

	/**
	 * Visszatéríti a bukaresti születésű román állampolgárok által intézett fizetések összegét.
	 *
	 * @param listOfCustomers
	 *                          tranzakciók
	 * @return
	 *          összeg
	 */
	BigDecimal getTotalAmountCapitalCity(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		var sum = BigDecimal.ZERO;

		for (var customer : listOfCustomers) {
			if (customer.getLeft().judet().getAbrv().equals("BU") && !customer.getLeft().foreigner()) {
				sum = sum.add(customer.getRight());
			}
		}

		return sum;
	}

	/**
	 * Külföldi személyek száma, akik fizetést intéztek.
	 *
	 * @param listOfCustomers
	 *                          tranzakciók
	 * @return
	 *          külföldi személyek száma
	 */
	Integer getForeigners(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		int counter = 0;

		for (var customer : listOfCustomers) {
			if (customer.getLeft().foreigner()) {
				counter++;
			}
		}

		return counter;
	}

	/**
	 * Visszatéríti a LIMIT-ig érvényes fizetések számát.
	 *
	 * @param listOfCustomers
	 *                          tranzakciók
	 * @param limit
	 *              határérték
	 * @return
	 *          fizetések száma
	 */
	Integer getPaymentsNumberByLimit(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers, final String limit) {
		int counter = 0;
		final var upperLimit = new BigDecimal(limit);

		for (var customer : listOfCustomers) {
			if (customer.getRight().compareTo(upperLimit) <= 0) {
				counter++;
			}
		}

		return counter;
	}

	Integer getSmallPaymentsNumber(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		return getPaymentsNumberByLimit(listOfCustomers, "5000");
	}

	Integer getBigPaymentsNumber(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		return listOfCustomers.size() - getPaymentsNumberByLimit(listOfCustomers, "5000");
	}

	/**
	 * Visszatéríti a fizetések átlagát.
	 *
	 * @param listOfCustomers
	 *                          tranzakciók
	 * @return
	 *          fizetések átlaga
	 */
	BigDecimal getAverage(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		var sum = BigDecimal.ZERO;

		for (var customer : listOfCustomers) {
			sum = sum.add(customer.getRight());
		}

		return sum.divide(BigDecimal.valueOf(listOfCustomers.size()), 2, RoundingMode.HALF_EVEN);
	}

	/**
	 * Beolvassa a tranzakciók listáját egy CSV állományból, amely mezőelválasztoként a ';'-t használja.
	 *
	 * @param paymentsInputStream
	 *                              CSV állomány
	 * @return
	 *          állomány sorai
	 * @throws IOException
	 *                      ha az állomány nem található, vagy beolvasási hiba lépett fel
	 */
	List<String[]> loadData(final InputStream paymentsInputStream) throws IOException {
		var csvParser = new CSVParserBuilder()
						.withSeparator(';')
						.build();

		List<String[]> rows;
		try (var reader = new CSVReaderBuilder(new InputStreamReader(paymentsInputStream))
						.withCSVParser(csvParser)
						.build()) {
			rows = reader.readAll();
		} catch (CsvException e) {
			throw new IOException(e.getMessage());
		}

		return rows;
	}

	/**
	 * Ellenőrzi a fizetésekhez tartozó CNP-ket, és visszatéríti a fizetéseket.
	 *
	 * @param dataInput
	 *                  CSV állomány sorai
	 * @return
	 *          tranzakciók
	 */
	ArrayList<Pair<CnpParts, BigDecimal>> getCustomers(final List<String[]> dataInput) {
		var validator = new CnpValidatorImpl();
		var listOfCustomers = new ArrayList<Pair<CnpParts, BigDecimal>>();

		for (int i = 0; i < dataInput.size(); i++) {
			final var currentPayment = dataInput.get(i);

			if (currentPayment.length != 2) {
				continue;
			}

			CnpParts cnp;
			BigDecimal paymentAmount;

			try {
				if (currentPayment[0].equals("")|| currentPayment[1].equals("")) {
					throw new PayException("Missing data from line", PayException.ErrorCode.INVALID_LINE);
				}

				paymentAmount = new BigDecimal(currentPayment[1]);

				if (paymentAmount.compareTo(BigDecimal.ZERO) < 0) {
					throw new PayException("Invalid payment with negative value", PayException.ErrorCode.INVALID_PAYMENT);
				}
			} catch (PayException e) {
				System.out.println(e.getMessage());
				writeError(i + 1, e.getCodeType());
				continue;
			}

			try {
				cnp = validator.validateCnp(currentPayment[0]);
			} catch (CnpException e) {
				System.out.println(e.getMessage());
				writeError(i + 1, e.getCodeType());
				continue;
			}

			listOfCustomers.add(
							new ImmutablePair<>(cnp, paymentAmount)
			);
		}

		return listOfCustomers;
	}

	/**
	 * Kiír egy hibapéldányt a hibatömbbe, együtt annak típusával és sorszámával, ahol a hiba előfordult.
	 *
	 * @param lineNumber
	 *                      sor
	 * @param errorType
	 *                      hibatípus
	 */
	private void writeError(final int lineNumber, final int errorType) {
		m_errors.add(new PayErrorImpl(lineNumber, errorType));
	}

}
