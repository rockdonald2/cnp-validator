package ro.axonsoft.internship21.pay;

interface PayError {

    /**
     * Sorszám, ahol a hiba előfordult.
     */
    Integer line();

    /**
     * Hibák típusa:
     * <ul>
     * <li>0 érvénytelen sor</li>
     * <li>1 CNP érvénytelen</li>
     * <li>2 fizetett összeg érvénytelen</li>
     * </ul>
     */
    Integer type();
}
