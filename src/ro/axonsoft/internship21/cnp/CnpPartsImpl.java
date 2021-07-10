package ro.axonsoft.internship21.cnp;

public class CnpPartsImpl implements CnpParts {

    private final Sex m_sex;
    private final Boolean m_isForeigner;
    private final Judet m_county;
    private final CalDate m_birthDate;
    private final Short m_orderNumber;

    /**
     * Creeaza o instanta de CnpParts, care poate fi utilizata in procesarea tranzactilor.
     *
     * @param sex
     *              tine sex-ul persoanei
     * @param foreigner
     *              tine daca persoana este una rezidenta
     * @param birthDate
     *              tine data nasterii persoanei
     * @param county
     *              tine judetul in care s-a nascut persoana
     * @param orderNumber
     *              tine numarul de ordine a persoanei prin care sunt diferentiata persoanele nascute in acelasi loc, si in acelasi timp
     */
    CnpPartsImpl(Sex sex, Boolean foreigner, CalDate birthDate, Judet county, Short orderNumber) {
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
    public Judet judet() {
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
     * Vizualizeaza datele persoanei dedus din CNP.
     *
     * @return output-ul formatat
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
