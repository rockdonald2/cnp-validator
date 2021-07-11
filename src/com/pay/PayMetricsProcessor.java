package com.pay;

import java.io.*;

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
    void process(FileInputStream paymentsInputStream, FileOutputStream metricsOutputStream) throws IOException;

    static PayMetricsProcessor getProcessor() {
        return new PayMetricsProcessorImpl();
    }

}
