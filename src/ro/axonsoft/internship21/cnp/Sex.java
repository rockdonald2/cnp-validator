package ro.axonsoft.internship21.cnp;

public enum Sex {

    M,
    F,
    U;

    /**
     * Valideaza si returneaza sex-ul persoanei dedus din CNP.
     *
     * @param code
     *            codul de validat
     * @return
     *         sex-ul persoanei
     * @throws CnpException
     *                      daca CNP-ul nu este valid
     */
    public static Sex getByCode(final String code) throws CnpException {
        try {
            return switch (Byte.parseByte(code)) {
                case 1, 3, 5, 7 -> Sex.M;
                case 2, 4, 6, 8 -> Sex.F;
                case 9 -> Sex.U;
                default -> throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
            };
        } catch (Exception e) {
            throw new CnpException("Invalid sex code", CnpException.ErrorCode.INVALID_CNP);
        }
    }

}
