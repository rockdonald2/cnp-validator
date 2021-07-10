package ro.axonsoft.internship21;

import ro.axonsoft.internship21.pay.PayMetricsProcessorImpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Driver {

    /**
     * Metodul driver, care incepe procesul.
     *
     * @param args
     *              argumentele CLI; unde:
     *              primul este fisierul CSV care contine tranzactile,
     *              a doua este fisierul JSON care va contine datele solicitate de departamentul de marketing
     * @throws IOException
     *                      daca fisierele nu pot fi deschise
     */
    public static void main(String[] args) throws IOException {
        var paymentsInputStream = new FileInputStream(args[0]);
        var paymentsOutputStream = new FileOutputStream(args[1]);
        new PayMetricsProcessorImpl().process(paymentsInputStream, paymentsOutputStream);
    }

}
