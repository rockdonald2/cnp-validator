package com.pay;

public interface PayError {

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

    static PayError generateError(int lineNumber, int code) {
        return new PayErrorImpl(lineNumber, code);
    }
}
