package com.pay;

import org.json.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.Set;

class PayMetricsImpl implements PayMetrics {

    private final int m_foreigners;
    private final int m_paymentsByMinors;
    private final int m_bigPayments;
    private final int m_smallPayments;
    private final BigDecimal m_averagePaymentAmount;
    private final BigDecimal m_totalAmountCapitalCity;
    private final Set<PayError> m_errors;

    /**
     * Létrehoz egy PayMetricsImpl példányt, amely JSON állományként szerializálható
     *
     * @param foreigners
     *                  külföldi személyek száma, akik kifizetést intéztek
     * @param paymentsByMinors
     *                          azon fizetések száma, amelyet 18.-ik életévüket be nem töltött személyek intézték
     * @param bigPayments
     *                      5000 RON-t meghaladó fizetések száma
     * @param smallPayments
     *                      5000 RON-t meg nem haladó fizetések száma, inkluzív
     * @param averagePaymentAmount
     *                              fizetések átlaga
     * @param totalAmountCapitalCity
     *                              bukaresti születésű román állampolgárok által intézett fizetések összege.
     * @param errors
     *              hibatömb
     */
    PayMetricsImpl(int foreigners, int paymentsByMinors, int bigPayments,
                   int smallPayments, BigDecimal averagePaymentAmount,
                   BigDecimal totalAmountCapitalCity, Set<PayError> errors) {
        m_foreigners = foreigners;
        m_paymentsByMinors = paymentsByMinors;
        m_bigPayments = bigPayments;
        m_smallPayments = smallPayments;
        m_averagePaymentAmount = averagePaymentAmount;
        m_totalAmountCapitalCity = totalAmountCapitalCity;
        m_errors = errors;
    }

    @Override
    public Integer foreigners() {
        return m_foreigners;
    }

    @Override
    public Integer paymentsByMinors() {
        return m_paymentsByMinors;
    }

    @Override
    public Integer bigPayments() {
        return m_bigPayments;
    }

    @Override
    public Integer smallPayments() {
        return m_smallPayments;
    }

    @Override
    public BigDecimal averagePaymentAmount() {
        return m_averagePaymentAmount;
    }

    @Override
    public BigDecimal totalAmountCapitalCity() {
        return m_totalAmountCapitalCity;
    }

    @Override
    public Set<PayError> errors() {
        return m_errors;
    }

    public void writeToFile(FileOutputStream file) {
        var metrics = new JSONObject();

        metrics.put("averagePaymentAmount", this.averagePaymentAmount());
        metrics.put("smallPayments", this.smallPayments());
        metrics.put("bigPayments", this.bigPayments());
        metrics.put("paymentsByMinor", this.paymentsByMinors());
        metrics.put("totalAmountCapitalCity", this.totalAmountCapitalCity());
        metrics.put("foreigners", this.foreigners());

        var errs = new JSONArray();
        for (var e : errors()) {
            errs.put(e.getJsonObject());
        }

        metrics.put("errors", errs);

        var o = new OutputStreamWriter(file);
        try {
           o.write(metrics.toString());
           o.flush();
           o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
