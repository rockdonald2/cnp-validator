package ro.axonsoft.internship21.cnp;

public class CalDateImpl implements CalDate{

    private final short m_year;
    private final byte m_month;
    private final byte m_day;

    /**
     * Creaza o instanta de data parsabil.
     *
     * @param date
     *              data intr-un format "yyyy-MM-dd"
     */
    CalDateImpl(String date) {
        var dateElements = date.split("-");
        m_year = Short.parseShort(dateElements[0]);
        m_month = Byte.parseByte(dateElements[1]);
        m_day = Byte.parseByte(dateElements[2]);
    }

    @Override
    public Short year() {
        return m_year;
    }

    @Override
    public Byte month() {
        return m_month;
    }

    @Override
    public Byte day() {
        return m_day;
    }

    /**
     * Vizualizeaza data intr-un format "yyyy-MM-dd".
     *
     * @return output-ul formatat
     */
    @Override
    public String toString() {
        return m_year + "-" + m_month + "-" + m_day;
    }

}
