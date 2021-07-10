package ro.axonsoft.internship21.pay;

import java.math.BigDecimal;
import java.util.Set;

interface PayMetrics {

    /**
     * Numarul de cetateni straini care au efectuat plati.
     */
    Integer foreigners();

    /**
     * Numarul de plati efectuate de catre minori.
     */
    Integer paymentsByMinors();

    /**
     * Numarul de plati peste pragul de 5000RON.
     */
    Integer bigPayments();

    /**
     * Numarul de plati pana in 5000RON inclusiv.
     */
    Integer smallPayments();

    /**
     * Media sumelor tuturor platilor. Valoarea are maxim doua zecimale.
     */
    BigDecimal averagePaymentAmount();

    /**
     * Totalul platilor efectuate de cetatenii romani nascuti in Bucuresti.
     */
    BigDecimal totalAmountCapitalCity();

    /**
     * Erorile de procesare.
     */
    Set<PayError> errors();
}