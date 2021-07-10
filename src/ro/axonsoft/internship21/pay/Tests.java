package ro.axonsoft.internship21.pay;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import ro.axonsoft.internship21.cnp.CnpParts;
import ro.axonsoft.internship21.cnp.CnpValidatorImpl;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class Tests {

    private static final PayMetricsProcessorImpl m_test_processor = new PayMetricsProcessorImpl();

    /*
    * Tesztelve az alábbi adatokkal:
    *       1900420112713;318.9
    *       6190614235878;922.317
    *       6140622338914;487.1
    *       1820519078935;853.11
    *       2930621262681;5257.21
    *       1420323428810;199.68
    * */
    private static final ArrayList<Pair<CnpParts, BigDecimal>> m_test_list_correct_data = loadTestList("");

    private static ArrayList<Pair<CnpParts,BigDecimal>> loadTestList(final String path) {
        final var list = new ArrayList<Pair<CnpParts, BigDecimal>>();
        final var validator = new CnpValidatorImpl();

        var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        List<String[]> rows = null;
        try (var reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(path)))
                .withCSVParser(csvParser)
                .build()) {
            rows = reader.readAll();
        } catch (Exception ignored) { }

        assert rows != null;
        for (var row : rows) {
            try {
                list.add(
                        new ImmutablePair<>(validator.validateCnp(row[0]), new BigDecimal(row[1]))
                );
            } catch (Exception ignored) {}
        }

        return list;
    }

    public static class TestPaymentMinors {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(2, m_test_processor.getPaymentsByMinors(m_test_list_correct_data));
        }

    }

    public static class TestTotalAmountCapitalCity {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(new BigDecimal("199.68"), m_test_processor.getTotalAmountCapitalCity(m_test_list_correct_data));
        }

    }

    public static class TestForeigners {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(0, (int) m_test_processor.getForeigners(m_test_list_correct_data));
        }

    }

    public static class TestSmallPayments {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(5, (int) m_test_processor.getSmallPaymentsNumber(m_test_list_correct_data));
        }

    }

    public static class TestBigPayments {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(1, (int) m_test_processor.getBigPaymentsNumber(m_test_list_correct_data));
        }

    }

    public static class TestAverage {

        @Test
        public void correctAnswer() {
            Assert.assertEquals(new BigDecimal("1339.72"), m_test_processor.getAverage(m_test_list_correct_data));
        }

    }

}
