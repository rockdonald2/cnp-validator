package com.cnp;

import java.io.Serializable;

class CalDateImpl implements CalDate, Serializable {

    private final short m_year;
    private final byte m_month;
    private final byte m_day;

    /**
     * Létrehoz egy értelmezhető dátumpéldányt.
     *
     * @param date
     *              dátum "yyyy-MM-dd" formátumban
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
     * Vizualizálja a dátumot "yyyy-MM-dd" formátumban.
     *
     * @return formatált kimenet
     */
    @Override
    public String toString() {
        return m_year + "-" + m_month + "-" + m_day;
    }

}
