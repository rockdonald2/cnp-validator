package ro.axonsoft.internship21.cnp;

public interface CnpValidator {

    /**
     * Validálja a paraméterként kapott CNP-t, és visszatéríti értelmezhető formátumban.
     *
     * @param cnp
     *            ellenőrízendő CNP
     * @return CNP alkotóelemei értelmezhető objektumként
     * @throws CnpException
     *             ha a CNP nem érvényes
     */
    CnpParts validateCnp(String cnp) throws CnpException;

}