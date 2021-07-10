package com.cnp;

import java.lang.StringBuilder;

import com.cnp.exception.*;
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
     * Ellenőrzi, hogy a CNP csak számokból áll-e.
     *
     * @param cnp
     *              ellenőrízendő CNP
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    void validateFormat(final String cnp) throws CnpException {
        if (!StringUtils.isNumeric(cnp)) {
            throw new CnpFormatException("Non-digit character appears in CNP");
        }
    }

    /**
     * Ellenőrzi, hogy a CNP 13 számjegyből áll-e.
     *
     * @param cnp
     *              ellenőrízendő CNP
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    void validateLength(final String cnp) throws CnpException {
        if (cnp.length() != CNP_LENGTH) {
            throw new CnpFormatException("Missing digits from CNP");
        }
    }

    /**
     * A CNP-t String-ből tömbbé alakítja.
     *
     * @param cnpString
     *                  átalakítandó CNP
     * @return
     *          CNP tömbként
     */
    private byte[] createByteCnpFromString(final String cnpString) throws CnpException {
        final var cnpDigits = new byte[cnpString.length()];

        for (int i = 0; i < cnpString.length(); i++) {
            try {
                cnpDigits[i] = Byte.parseByte(cnpString.substring(i, i + 1));
            } catch (Exception e) {
                throw new CnpFormatException("Invalid digit");
            }
        }

        return cnpDigits;
    }

    /**
     * Ellenőrzi és visszatéríti a személy nemét.
     *
     * @param sexCodeString
     *                      a CNP alkotóeleme, amely a nem kikövetkeztetésére szolgál
     * @return
     *          nem
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    Sex getSex(final String sexCodeString) throws CnpException {
        try {
            return Sex.getByCode(sexCodeString);
        } catch (CnpException e) {
            throw e;
        }
    }

    /**
     * Ellenőrzi és visszatéríti, hogy rezidens a személy.
     *
     * @param sexCodeString
     *                      CNP alkotóeleme, amely a státuszának megállapítására szolgál
     * @return
     *          logikai érték, külföldi-e
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    boolean getForeignStatus(final String sexCodeString) throws CnpException {
        try {
            return switch (Byte.parseByte(sexCodeString)) {
                case 7, 8, 9 -> true;
                case 1, 2, 3, 4, 5, 6 -> false;
                default -> throw new InvalidSexException("Invalid sex code");
            };
        } catch (Exception e) {
            throw new InvalidSexException("Invalid sex code");
        }
    }

    /**
     * Ellenőrzi és visszatéríti a személy születési dátumát.
     *
     * @param dateString
     *                  CNP alkotóeleme, amely a dátum kikövetkeztetésére szolgál
     * @param centuryCode
     *                  a CNP alkotóeleme, amely az évszázad kikövetkeztetésére szolgál
     * @return
     *          születési dátum CalDate példányként
     * @throws CnpException
     *                      ha a CNP érvénytelen
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
            throw new InvalidBirthDateException("Invalid birth date");
        }

        return new CalDateImpl(returnDate);
    }

    /**
     * Kikövetkezteti az évszázadot, amelyben született a nem rezidens személy.
     *
     * @param dateYearString
     *                      CNP alkotóeleme, amely a születési év kikövetkeztetésére szolgál
     * @return
     *          évszázad
     */
    private String guessForeignerCentury(final String dateYearString) {
        return Byte.parseByte(dateYearString) <= 21 ? "20" : "19";
    }

    /**
     * Ellenőrzi és visszatéríti az évszázadot, amelyben a személy született.
     *
     * @param sexCodeString
     *                      a CNP alkotóeleme, amely a születési év kikövetkeztetésére szolgál
     * @return
     *          évszázad
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    String getCentury(final String sexCodeString) throws CnpException {
        try {
            return switch (Byte.parseByte(sexCodeString)) {
                case 1, 2 -> "19";
                case 3, 4 -> "18";
                case 5, 6 -> "20";
                case 7, 8, 9 -> "";
                default -> throw new InvalidCenturyException("Invalid sex/century code");
            };
        } catch (Exception e) {
            throw new InvalidCenturyException("Invalid sex/century code");
        }
    }

    /**
     * Ellenőrzi és visszatéríti a születési megyét.
     *
     * @param countyString
     *                      CNP alkotóeleme, amely a megye kikövetkeztetésére szolgál
     * @return
     *          County
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    County getBirthCounty(final String countyString) throws CnpException {
        try {
            return County.getByCode(countyString);
        } catch (CnpException e) {
            throw e;
        }
    }

    /**
     * Visszatéríti a személy sorszámát.
     *
     * @param orderNumberString
     *                          CNP alkotóeleme, amely a sorszám kikövetkeztetésére szolgál
     * @return
     *          sorszám
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    Short getOrderNumber(final String orderNumberString) throws CnpException {
        try {
            return Short.parseShort(orderNumberString);
        } catch (Exception e) {
            throw new InvalidOrderNumberException("Invalid order number");
        }
    }

    /**
     * Ellenőrzi a CNP kontrollszámát.
     *
     * @param cnpString
     *                  CNP
     * @throws CnpException
     *                      ha a CNP érvénytelen
     */
    void validateControlNumber(final String cnpString) throws CnpException {
        final var cnpDigitArr = createByteCnpFromString(cnpString);

        int digitSum = 0;
        for (int i = 0; i < cnpDigitArr.length - 1; i++) {
            digitSum += CONTROL_DIGIT_ARRAY[i] * cnpDigitArr[i];
        }

        byte controlNumber = cnpDigitArr[cnpDigitArr.length - 1];

        if (((digitSum % 11 == 10) && controlNumber != 1)) {
            throw new InvalidControlNumberException("Invalid control number");
        }

        if (controlNumber != (digitSum % 11) && (digitSum % 11 != 10)) {
            throw new InvalidControlNumberException("Invalid control number");
        }
    }

}
