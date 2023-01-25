package Users;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

import DATABASE.Database;


public abstract class User implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private int ID;
	private String email;
	private String password = "no";
	private PersonalData personalData;
	private Vector<Request> requests = new Vector<Request>();
	
	{
		ID = Database.nextID();
	}
	public User() {
		
	}
	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = generateEmail();
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getID() {
		return ID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public PersonalData getPersonalData() {
		return personalData;
	}
	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}
	public Vector<Request> getRequests(){
		return requests;
	}
	private String generateEmail() {
		if(this instanceof Employee) {
			char ch = Character.toLowerCase(firstName.charAt(0));
			return ch + "." + lastName.toLowerCase() + "@kbtu.kz";
		}else{
			char ch = Character.toLowerCase(firstName.charAt(0));
			return ch + "_" + lastName.toLowerCase() + "@kbtu.kz";
		}
	}
	public boolean equalsWithName(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		User user = (User) obj;
		return firstName.equals(user.firstName) && lastName.equals(user.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, email, firstName, lastName, password);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return ID == other.ID && Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password);
	}
	@Override
	public String toString() {
		return "Name: " + firstName + " " + lastName;
	}
}
