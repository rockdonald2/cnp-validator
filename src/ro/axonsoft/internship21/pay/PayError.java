package ro.axonsoft.internship21.pay;

interface PayError {

    /**
     * Numarul liniei la care s-a obtinut eroarea.
     */
    Integer line();

    /**
     * Tipul erorii:
     * <ul>
     * <li>0 linie invalida</li>
     * <li>1 pentru CNP invalid</li>
     * <li>2 pentru suma plata invalida</li>
     * </ul>
     */
    Integer type();
}
