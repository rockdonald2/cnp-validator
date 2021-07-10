package ro.axonsoft.internship21.cnp;

import java.lang.StringBuilder;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.lang3.StringUtils;

public class CnpValidatorImpl implements CnpValidator {

    private static final int CNP_LENGTH = 13;
    private static final byte[] CONTROL_DIGIT_ARRAY = new byte[]{2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    private enum CnpPart {

        SEX_AND_CENTURY_CODE(0, 1),
        BIRTH_DATE(1, 7),
        BIRTH_COUNTY_CODE(7, 9),
        ORDER_NUMBER(9, 12),
        CONTROL_CODE(12, 13);

        private final int m_startIndex;
        private final int m_endIndex;

        CnpPart(final int startIndex, final int endIndex) {
            m_startIndex = startIndex;
            m_endIndex = endIndex;
        }

        /**
         * Extrage componente din CNP.
         *
         * @param cnp
         *              CNP-ul de validat
         * @return
         *          componenta corespondenta
         */
        public String extractFrom(final String cnp) {
            return cnp.substring(m_startIndex, m_endIndex);
        }

    }

    @Override
    public CnpParts validateCnp(String cnp) throws CnpException {
        validateLength(cnp);

        validateFormat(cnp);

        validateControlNumber(cnp);

        var sex = getSex(CnpPart.SEX_AND_CENTURY_CODE.extractFrom(cnp));

        var foreigner = getForeignStatus(CnpPart.SEX_AND_CENTURY_CODE.extractFrom(cnp));

        var birthDate = getBirthDate(CnpPart.BIRTH_DATE.extractFrom(cnp), CnpPart.SEX_AND_CENTURY_CODE.extractFrom(cnp));

        var birthCounty = getBirthCounty(CnpPart.BIRTH_COUNTY_CODE.extractFrom(cnp));

        var orderNumber = getOrderNumber(CnpPart.ORDER_NUMBER.extractFrom(cnp));

        return new CnpPartsImpl(sex, foreigner, birthDate, birthCounty, orderNumber);
    }

    /**
     * Valideaza daca CNP-ul contine doar numere.
     *
     * @param cnp
     *              CNP-ul de validat
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    void validateFormat(final String cnp) throws CnpException {
        if (!StringUtils.isNumeric(cnp)) {
            throw new CnpException("Non-digit character appears in CNP", CnpException.ErrorCode.INVALID_CNP);
        }
    }

    /**
     * Valideaza daca CNP-ul contine 13 numere.
     *
     * @param cnp
     *              CNP-ul de validat
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    void validateLength(final String cnp) throws CnpException {
        if (cnp.length() != CNP_LENGTH) {
            throw new CnpException("Missing digits from CNP", CnpException.ErrorCode.INVALID_CNP);
        }
    }

    /**
     * Transformeaza CNP-ul dintr-un String intr-un Array.
     *
     * @param cnpString
     *                  CNP-ul de transformat
     * @return
     *          CNP-ul intr-un Array
     */
    private byte[] createByteCnpFromString(final String cnpString) throws CnpException {
        final var cnpDigits = new byte[cnpString.length()];

        for (int i = 0; i < cnpString.length(); i++) {
            try {
                cnpDigits[i] = Byte.parseByte(cnpString.substring(i, i + 1));
            } catch (Exception e) {
                throw new CnpException("Invalid digit", CnpException.ErrorCode.INVALID_CNP);
            }
        }

        return cnpDigits;
    }

    /**
     * Valideaza si returneaza sex-ul persoanei.
     *
     * @param sexCodeString
     *                      componentul S care reprezinta sexul persoanei intr-un String
     * @return
     *          Sex-ul persoanei
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    Sex getSex(final String sexCodeString) throws CnpException {
        try {
            return Sex.getByCode(sexCodeString);
        } catch (CnpException e) {
            throw new CnpException(e);
        }
    }

    /**
     * Valideaza si returneaza daca persoana este una rezidenta.
     *
     * @param sexCodeString
     *                      componentul S care reprezinta sexul persoanei intr-un String prin care parametrul poate fi dedusa
     * @return
     *          boolean
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    boolean getForeignStatus(final String sexCodeString) throws CnpException {
        try {
            return switch (Byte.parseByte(sexCodeString)) {
                case 7, 8, 9 -> true;
                case 1, 2, 3, 4, 5, 6 -> false;
                default -> throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
            };
        } catch (Exception e) {
            throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
        }
    }

    /**
     * Valideaza, returneaza si compune data nasterii persoanei.
     *
     * @param dateString
     *                  componentul AA, LL, ZZ formate din anul, luna, si ziua nasterii intr-un String
     * @param centuryCode
     *                  componentul S care reprezinta secolul in care s-a nascut persoana
     * @return
     *          data nasterii persoanei intr-un CalDate instanta
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    CalDate getBirthDate(final String dateString, String centuryCode) throws CnpException {
        final var dateCharArr = dateString.toCharArray();
        final var composedDate = new StringBuilder();

        var centuryString = getCentury(centuryCode);

        if (centuryString.isEmpty()) {
            centuryString = guessForeignerCentury(dateString.substring(0, 2));
        }

        composedDate.append(centuryString);

        for (int i = 0; i < dateCharArr.length; ++i) {
            composedDate.append(dateCharArr[i]);

            if (i == 1 || i == 3) {
                composedDate.append('-');
            }
        }

        final var returnDate = composedDate.toString();

        if (!GenericValidator.isDate(returnDate, "yyyy-MM-dd", true)) {
            throw new CnpException("Invalid birth date", CnpException.ErrorCode.INVALID_CNP);
        }

        return new CalDateImpl(returnDate);
    }

    /**
     * Ghiceste secolul in care s-a nascut persoana rezidenta.
     *
     * @param dateYearString
     *                      componentul AA care reprezinta anul nasterii intr-un String
     * @return
     *          secolul in care s-a nascut persoana intr-un String
     */
    private String guessForeignerCentury(final String dateYearString) {
        return Byte.parseByte(dateYearString) <= 21 ? "20" : "19";
    }

    /**
     * Returneaza si valideaza secolul in care s-a nascut persoana.
     *
     * @param sexCodeString
     *                      componentul S care reprezinta secolul in care s-a nascut persoana
     * @return
     *          secolul in care s-a nascut persoana intr-un String
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    String getCentury(final String sexCodeString) throws CnpException {
        try {
            return switch (Byte.parseByte(sexCodeString)) {
                case 1, 2 -> "19";
                case 3, 4 -> "18";
                case 5, 6 -> "20";
                case 7, 8, 9 -> "";
                default -> throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
            };
        } catch (Exception e) {
            throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
        }
    }

    /**
     * Returneaza si valideaza judetul nasterii persoanei.
     *
     * @param countyString
     *                      componentul JJ care reprezinta judetul sau sectorul in care s-a nascut persoana intr-un String
     * @return
     *          Judet
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    Judet getBirthCounty(final String countyString) throws CnpException {
        try {
            return Judet.getByCode(countyString);
        } catch (CnpException e) {
            throw new CnpException(e);
        }
    }

    /**
     * Returneaza numarul de ordine a persoanei.
     *
     * @param orderNumberString
     *                          componentul NNN care reprezinta un numar secvential intr-un String
     * @return
     *          numarul de ordine
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    Short getOrderNumber(final String orderNumberString) throws CnpException {
        try {
            return Short.parseShort(orderNumberString);
        } catch (Exception e) {
            throw new CnpException("Invalid order number", CnpException.ErrorCode.INVALID_CNP);
        }
    }

    /**
     * Valideaza numarul de control CNP-ului.
     *
     * @param cnpString
     *                  CNP-ul de validat intr-un String
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    void validateControlNumber(final String cnpString) throws CnpException {
        final var cnpDigitArr = createByteCnpFromString(cnpString);

        if (CONTROL_DIGIT_ARRAY.length != (cnpDigitArr.length - 1)) {
            throw new CnpException("Missing digits from CNP", CnpException.ErrorCode.INVALID_CNP);
        }

        int digitSum = 0;
        for (int i = 0; i < cnpDigitArr.length - 1; i++) {
            digitSum += CONTROL_DIGIT_ARRAY[i] * cnpDigitArr[i];
        }

        byte controlNumber = cnpDigitArr[cnpDigitArr.length - 1];

        if (((digitSum % 11 == 10) && controlNumber != 1)) {
            throw new CnpException("Invalid control number", CnpException.ErrorCode.INVALID_CNP);
        }

        if (controlNumber != (digitSum % 11) && (digitSum % 11 != 10)) {
            throw new CnpException("Invalid control number", CnpException.ErrorCode.INVALID_CNP);
        }
    }

}
