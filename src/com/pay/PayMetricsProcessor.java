package com.pay;

import com.cnp.CnpParts;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public interface PayMetricsProcessor {

    /**
     * Feldolgozza a {@code paymentsInputStream}-ban található tranzakciókat és kiírja a mutatókat a
     * {@code metricsOutputStream}-ba
     *
     * @param paymentsInputStream
     *             csv állomány a tranzakciókkal
     * @param metricsOutputStream
     *             állomány elérési útvonal, ahová szerializálja a mutatókat és a hozzátartozó hibákat
     * @throws IOException
     *             ha valamilyen I/O hiba jelenne meg
     */
    Map<CnpParts, ArrayList<BigDecimal>> process(FileInputStream paymentsInputStream, FileOutputStream metricsOutputStream) throws IOException;

    static PayMetricsProcessor getProcessor() {
        return new PayMetricsProcessorImpl();
    }

}
