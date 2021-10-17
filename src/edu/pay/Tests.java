package edu.pay;

import edu.cnp.CnpValidator;
import edu.cnp.exception.CnpException;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import edu.pay.exception.MissingDataException;
import edu.pay.exception.NegativePaymentException;
import edu.pay.exception.PayException;
import org.junit.Assert;
import org.junit.Test;

import edu.cnp.CnpParts;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Tests {

    private static final PayMetricsProcessorImpl test_processor = new PayMetricsProcessorImpl();

    /*
    * Tesztelve az alábbi adatokkal:
    *       1900420112713;318.9
    *       6190614235878;922.317
    *       6140622338914;487.1
    *       1820519078935;853.11
    *       2930621262681;5257.21
    *       1420323428810;199.68
    * */
    private static final Map<CnpParts, ArrayList<BigDecimal>> test_list_correct_data = loadTestList("D:\\CodeSpring\\data\\testCorrectData.csv");

    private static Map<CnpParts, ArrayList<BigDecimal>> loadTestList(final String path) {
        final var list = new HashMap<CnpParts, ArrayList<BigDecimal>>();
        final var validator = CnpValidator.getValidator();

        var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        List<String[]> rows = null;
        try (var reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(path)))
                .withCSVParser(csvParser)
                .build()) {
            rows = reader.readAll();
        } catch (Exception ignored) { }

        for (int i = 0; i < rows.size(); i++) {
            final var currentPayment = rows.get(i);

            if (currentPayment.length != 2) {
                continue;
            }

            CnpParts cnp = null;
            BigDecimal paymentAmount;

            try {
                if (currentPayment[0].equals("") || currentPayment[1].equals("")) {
                    throw new MissingDataException("");
                }

                paymentAmount = new BigDecimal(currentPayment[1]);

                if (paymentAmount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NegativePaymentException("");
                }
            } catch (PayException e) {
                continue;
            }

            for (var j : list.keySet()) {
                if (j.toString().equals(currentPayment[0])) {
                    cnp = j;
                }
            }

            if (cnp == null) {
                try {
                    cnp = validator.validateCnp(currentPayment[0]);
                } catch (CnpException e) {
                    continue;
                }
            }

            if (!list.containsKey(cnp)) {
                list.put(cnp, new ArrayList<>());
            }

            list.get(cnp).add(paymentAmount);
        }

        return list;
    }

    public static class TestPaymentMinors {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(2, test_processor.getPaymentsByMinors(test_list_correct_data));
        }

    }

    public static class TestTotalAmountCapitalCity {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(new BigDecimal("199.68"), test_processor.getTotalAmountCapitalCity(test_list_correct_data));
        }

    }

    public static class TestForeigners {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(0, (int) test_processor.getForeigners(test_list_correct_data));
        }

    }

    public static class TestSmallPayments {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(5, (int) test_processor.getSmallPaymentsNumber(test_list_correct_data));
        }

    }

    public static class TestBigPayments {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(1, (int) test_processor.getBigPaymentsNumber(test_list_correct_data));
        }

    }

    public static class TestAverage {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(new BigDecimal("1339.72"), test_processor.getAverage(test_list_correct_data));
        }

    }

}
