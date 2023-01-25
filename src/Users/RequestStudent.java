package Users;

import java.io.Serializable;
import java.util.Objects;

import Enums.*;

public class RequestStudent extends Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RequestTypeStudent type;
	
	public RequestStudent() {
		
	}
	public RequestStudent(String date, RequestForm form, RequestLanguage lan, RequestTypeStudent type) {
		super(date, form, lan);
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(type);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestStudent other = (RequestStudent) obj;
		return type == other.type;
	}
	@Override
	public String toString() {
		return "RequestStudent [type=" + type + "]";
	}
	
}
