package com.cnp;

class CnpPartsImpl implements CnpParts {

    private final Sex m_sex;
    private final Boolean m_isForeigner;
    private final County m_county;
    private final CalDate m_birthDate;
    private final Short m_orderNumber;

    /**
     * Értelmezhető CNP példány, amelynek adatai lekérdezhetőek.
     *
     * @param sex
     *              nem
     * @param foreigner
     *              külföldi-e
     * @param birthDate
     *              születési dátum
     * @param county
     *              születési megye
     * @param orderNumber
     *              sorszám
     */
    CnpPartsImpl(Sex sex, Boolean foreigner, CalDate birthDate, County county, Short orderNumber) {
        m_sex = sex;
        m_isForeigner = foreigner;
        m_county = county;
        m_birthDate = birthDate;
        m_orderNumber = orderNumber;
    }

    @Override
    public Sex sex() {
        return m_sex;
    }

    @Override
    public Boolean foreigner() {
        return m_isForeigner;
    }

    @Override
    public County county() {
        return m_county;
    }

    @Override
    public CalDate birthDate() {
        return m_birthDate;
    }

    @Override
    public Short orderNumber() {
        return m_orderNumber;
    }

    /**
     * Vizualizálja a személy CNP-ből kikövetkeztett adatait.
     *
     * @return formatált kimenet
     */
    @Override
    public String toString() {
        return m_sex + " -- "
                + m_isForeigner + " -- "
                + m_birthDate + " -- "
                + m_county.getAbrv() + " -- "
                + m_orderNumber;
    }

}
