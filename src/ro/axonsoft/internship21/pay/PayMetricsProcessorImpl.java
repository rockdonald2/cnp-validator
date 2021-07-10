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
import ro.axonsoft.internship21.cnp.Judet;

public class PayMetricsProcessorImpl implements PayMetricsProcessor {

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

			metrics = new PayMetricsImpl(foreigners, paymentsByMinors, bigPayments,
							smallPayments, averagePaymentAmount, totalAmountCapitalCity, m_errors);
		} else {
			metrics = new PayMetricsImpl(0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, m_errors);
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
	 * Returneaza numarul de plati efectuate de persoane ce nu au implinit varsta majoratului.
	 *
	 * @param listOfCustomers
	 *                          lista de plati
	 * @return
	 *          numarul de plati
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
	 * Returneaza suma totala a platilor efectuate de cetateni romani nascuti in Bucuresti.
	 *
	 * @param listOfCustomers
	 *                          lista de plati
	 * @return
	 *          suma totala
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
	 * Returneaza numarul de cetateni straini ce au efectuat plati.
	 *
	 * @param listOfCustomers
	 *                          lista de plati
	 * @return
	 *          numarul de cetateni straini
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
	 * Returneaza numarul de plati cu valoare pana in LIMIT.
	 *
	 * @param listOfCustomers
	 *                          lista de plati
	 * @param limit
	 *              limita
	 * @return
	 *          numarul de plati
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
	 * Returneaza media valorilor plati.
	 *
	 * @param listOfCustomers
	 *                          lista de plati
	 * @return
	 *          media valorilor plati
	 */
	BigDecimal getAverage(final ArrayList<Pair<CnpParts, BigDecimal>> listOfCustomers) {
		var sum = BigDecimal.ZERO;

		for (var customer : listOfCustomers) {
			sum = sum.add(customer.getRight());
		}

		return sum.divide(BigDecimal.valueOf(listOfCustomers.size()), 2, RoundingMode.HALF_EVEN);
	}

	/**
	 * Incarca lista platilor dintr-un fisier CSV care foloseasca ca separator de campuri caracterul ';'.
	 *
	 * @param paymentsInputStream
	 *                              fisierul CSV ca o instanta de InputStream
	 * @return
	 *          randurile fisierului iterabila ca String Array
	 * @throws IOException
	 *                      daca fisierul nu este gasit sau daca apare o eroare in incarcare
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
	 * Valideaza CNP-urile asociat cu plati si returneaza lista platilor.
	 *
	 * @param dataInput
	 *                  randurile fisierului CSV iterabila ca String Array
	 * @return
	 *          lista platilor
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
	 * Scrie o eroare in set-ul de erori, impreuna cu tipul de eroare si randul in care a aparut acestea.
	 *
	 * @param lineNumber
	 *                      randul
	 * @param errorType
	 *                      tipul de eroare
	 */
	private void writeError(final int lineNumber, final int errorType) {
		m_errors.add(new PayErrorImpl(lineNumber, errorType));
	}

}
