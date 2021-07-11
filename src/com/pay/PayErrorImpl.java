package com.pay;

import org.json.JSONObject;

class PayErrorImpl implements PayError {

    private final int m_lineNumber;
    private final int m_code;

    /**
     * Létrehoz egy PayError példányt, amely a hiba információt fogja tartalmazni, ami a feldolgozás során felmerült.
     *
     * @param lineNumber
     *                      sorszám, ahol előfordult a hiba
     * @param code
     *              hiba típusa
     */
    public PayErrorImpl(int lineNumber, int code) {
        m_lineNumber = lineNumber;
        m_code = code;
    }

    @Override
    public Integer line() {
        return m_lineNumber;
    }

    @Override
    public Integer type() {
        return m_code;
    }

    @Override
    public JSONObject getJsonObject() {
        var outJsonFormat = new JSONObject();

        outJsonFormat.put("line", this.line());
        outJsonFormat.put("type", this.type());

        return outJsonFormat;
    }

}
