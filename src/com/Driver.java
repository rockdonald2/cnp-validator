package com;

import com.pay.PayMetricsProcessor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Driver {

    /**
     * Driver metódus és osztály, amely elkezdi a folyamatot. 
     * ! Később ez lesz a szerver, feltételezhetően.
     * 
     * @param args
     *              CLI argumentumok; ahol:
     *              CSV állomány, amely tartalmazza a tranzakciókat,
     *              JSON állomány elérési útvonala, amely a Marketing osztály által ígényelt adatokat fogja tartalmazni
     * @throws IOException
     *                      ha az állományok megnyitása sikertelen
     */
    public static void main(String[] args) throws IOException {
        var paymentsInputStream = new FileInputStream(args[0]);
        var paymentsOutputStream = new FileOutputStream(args[1]);
        PayMetricsProcessor.getProcessor().process(paymentsInputStream, paymentsOutputStream);
    }

}
