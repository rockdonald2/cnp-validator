package ro.axonsoft.internship21.cnp;

public class CnpException extends Exception {

    private final ErrorCode m_code;

    public enum ErrorCode {

        INVALID_CNP("INVALID_CNP", 1);

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
     * Constructorul pentru exceptiile generate de erori in cursul validarea CNP-urilor.
     *
     * @param errorMsg
     *              mesajul de eroare scris pe console, intotdeauna incepe cu prefix-ul "Error:"
     * @param code
     *              codul de eroare asociat
     */
    public CnpException(String errorMsg, ErrorCode code) {
        super("Error: " + errorMsg);
        m_code = code;
    }

    /**
     * Constructorul de copiere pentru exceptiile aruncate din nou.
     *
     * @param e
     *          exceptia generata
     */
    public CnpException(CnpException e) {
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
