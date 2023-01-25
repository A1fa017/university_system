package Users;

import java.io.Serializable;
import java.sql.*;
import java.util.Objects;

public class PersonalData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Date birthDate = new Date(0,0,0);
	public String nation;
	public int phoneNumber;
	
	public PersonalData() {
		
	}
	public PersonalData(Date birthDate, String nation, int phoneNumber) {
		this.birthDate = birthDate;
		this.nation = nation;
		this.phoneNumber = phoneNumber;
	}
	@Override
	public int hashCode() {
		return Objects.hash(birthDate, nation, phoneNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonalData other = (PersonalData) obj;
		return Objects.equals(birthDate, other.birthDate)
			&& Objects.equals(nation, other.nation) && phoneNumber == other.phoneNumber;
	}
	public String toString() {
		return "[Birth date: " + birthDate + ", Nation: " + nation  + ", Phone number: " + phoneNumber + "]";
	}
}
