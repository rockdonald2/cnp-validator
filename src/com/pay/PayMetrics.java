package com.pay;

import java.math.BigDecimal;
import java.util.Set;

public interface PayMetrics {

    static PayMetrics getMetrics(int foreigners, int paymentsByMinors, int bigPayments,
                                 int smallPayments, BigDecimal averagePaymentAmount,
                                 BigDecimal totalAmountCapitalCity, Set<PayError> errors) {
        return new PayMetricsImpl(foreigners, paymentsByMinors, bigPayments, smallPayments, averagePaymentAmount, totalAmountCapitalCity, errors);
    }

    /**
     * Külföldi személyek száma, akik intéztek fizetést.
     */
    Integer foreigners();

    /**
     * Kiskorúak által intézett fizetések száma.
     */
    Integer paymentsByMinors();

    /**
     * 5000 RON fölötti fizetések száma.
     */
    Integer bigPayments();

    /**
     * 5000 RON alatti fizetések, inkluzív.
     */
    Integer smallPayments();

    /**
     * Kifizetések átlaga, két tizedes pontossággal.
     */
    BigDecimal averagePaymentAmount();

    /**
     * Bukaresti születésű román állampolgárok által intézett kifizetések összege.
     */
    BigDecimal totalAmountCapitalCity();

    /**
     * Feldolgozási hibák.
     */
    Set<PayError> errors();
}