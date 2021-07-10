package ro.axonsoft.internship21.pay;

class PayException extends Exception {

    private final ErrorCode m_code;

    enum ErrorCode {

        INVALID_LINE("INVALID_LINE", 0),
        INVALID_PAYMENT("INVALID_PAYMENT", 2);

        private final String m_errorName;
        private final int m_typeNumber;

        ErrorCode(String errorName, int typeNumber) {
            m_errorName = errorName;
            m_typeNumber = typeNumber;
        }

        String getErrorName() {
            return m_errorName;
        }

        int getErrorTypeNumber() {
            return m_typeNumber;
        }

    }

    /**
     * Constructorul pentru exceptiile generate de erori in cursul procesarea tranzactilor.
     *
     * @param errorMsg
     *              mesajul de eroare scris pe console, intotdeauna incepe cu prefix-ul "Error:"
     * @param code
     *              codul de eroare asociat
     */
    public PayException(String errorMsg, ErrorCode code) {
        super("Error: " + errorMsg);
        m_code = code;
    }

    /**
     * Constructorul de copiere pentru exceptiile aruncate din nou.
     *
     * @param e
     *          exceptia generata
     */
    public PayException(PayException e) {
        super(e.getMessage());
        m_code = e.getCode();
    }

    /**
     * Returneaza codul de eroare asociata.
     *
     * @return ErrorCode
     */
    public ErrorCode getCode() {
        return m_code;
    }

    /**
     * Returneaza tipul codului de eroare asociata.
     *
     * @return numarul tipului
     */
    public int getCodeType() {
        return m_code.getErrorTypeNumber();
    }

}

