package ro.axonsoft.internship21.cnp;

public enum Sex {

    M,
    F,
    U;

    /**
     * Ellenőrzi és visszatéríti a személy nemét
     *
     * @param code
     *            ellenőrizendő CNP alkotóelem
     * @return
     *         nem
     * @throws CnpException
     *                      ha a CNP érvénytelen
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
