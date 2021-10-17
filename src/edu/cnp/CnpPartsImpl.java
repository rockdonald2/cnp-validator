package edu.cnp;

import java.io.Serializable;

class CnpPartsImpl implements CnpParts, Serializable {

	private final Sex sex;
	private final Boolean isForeigner;
	private final County county;
	private final CalDate birthDate;
	private final Short orderNumber;
	private final String cnp;

	/**
	 * Értelmezhető CNP példány, amelynek adatai lekérdezhetőek.
	 *
	 * @param sex
	 *              nem
	 * @param foreigner
	 *              külföldi-e
	 * @param birthDate
	 *              születési dátum
	 * @param county
	 *              születési megye
	 * @param orderNumber
	 *              sorszám
	 */
	CnpPartsImpl(Sex sex, Boolean foreigner, CalDate birthDate, County county, Short orderNumber, String cnp) {
		this.sex = sex;
		isForeigner = foreigner;
		this.county = county;
		this.birthDate = birthDate;
		this.orderNumber = orderNumber;
		this.cnp = cnp;
	}

	@Override
	public Sex sex() {
		return sex;
	}

	@Override
	public Boolean foreigner() {
		return isForeigner;
	}

	@Override
	public County county() {
		return county;
	}

	@Override
	public CalDate birthDate() {
		return birthDate;
	}

	@Override
	public Short orderNumber() {
		return orderNumber;
	}

	@Override
	public String cnp() { return cnp; }

	/**
	 * Vizualizálja a személy CNP-jét.
	 *
	 * @return formatált kimenet
	 */
	@Override
	public String toString() {
		return cnp;
	}

}
