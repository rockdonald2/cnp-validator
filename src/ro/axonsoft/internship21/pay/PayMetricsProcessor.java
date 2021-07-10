package ro.axonsoft.internship21.pay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

interface PayMetricsProcessor {

    /**
     * Proceseaza platile din {@code paymentsInputStream} si scrie metricele in
     * {@code metricsOutputStream}
     *
     * @param paymentsInputStream
     *             input stream al fisierului csv conținând plățile
     * @param metricsOutputStream
     *             output stream al fișierului in care se serializeaza
     *             obiectul conținând metricile și erorile
     * @throws IOException
     *             daca apare o eroare de I/O.
     */
    void process(InputStream paymentsInputStream, OutputStream metricsOutputStream) throws IOException;

}
