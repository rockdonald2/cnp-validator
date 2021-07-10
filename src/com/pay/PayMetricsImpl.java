package com.pay;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
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
     *                  ! javítsd ki, hogy ne ismétlődjön
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

    public static class metricsSerializer extends StdSerializer<PayMetrics> {

        public metricsSerializer() {
            this(null);
        }

        public metricsSerializer(Class<PayMetrics> m) {
            super(m);
        }

        @Override
        public void serialize(
                PayMetrics payMetrics, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
            try {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeNumberField("averagePaymentAmount", payMetrics.averagePaymentAmount());
                jsonGenerator.writeNumberField("smallPayments", payMetrics.smallPayments());
                jsonGenerator.writeNumberField("bigPayments", payMetrics.bigPayments());
                jsonGenerator.writeNumberField("paymentsByMinor", payMetrics.paymentsByMinors());
                jsonGenerator.writeNumberField("totalAmountCapitalCity", payMetrics.totalAmountCapitalCity());
                jsonGenerator.writeNumberField("foreigners", payMetrics.foreigners());

                jsonGenerator.writeFieldName("errors");
                jsonGenerator.writeStartArray();
                for (var error : payMetrics.errors()) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeNumberField("line", error.line());
                    jsonGenerator.writeNumberField("type", error.type());
                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray();

                jsonGenerator.writeEndObject();
            } catch (IOException ignored) { }
        }
    }

}
