package com.cnp;

public interface CnpParts {

    /**
     * CNP első számjegye alapján kikövetkeztett nem.
     */
    Sex sex();

    /**
     * Külföldi állampolgár-e.
     *
     * @return {@code true} ha külföldi, {@code false} ellenkező esetben
     */
    Boolean foreigner();

    /**
     * Születési megye.
     */
    County county();

    /**
     * Születési dátum.
     */
    CalDate birthDate();

    /**
     * Sorszám.
     */
    Short orderNumber();

}
