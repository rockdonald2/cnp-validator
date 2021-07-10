package ro.axonsoft.internship21.pay;

class PayErrorImpl implements PayError {

    private final int m_lineNumber;
    private final int m_code;

    /**
     * Creeaza o instante de PayError, care a aparut in pregatirea tranzactilor pentru procesare.
     *
     * @param lineNumber
     *                      numarul liniei in care a aparut eroarea
     * @param code
     *              tipul eroarei
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

}
