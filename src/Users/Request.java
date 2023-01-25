package Users;

import java.io.Serializable;
import java.util.Objects;

import Enums.*;

public abstract class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String date;
	public RequestForm form;
	public RequestStatus status;
	public RequestLanguage lan;
	
	public Request() {
		
	}
	public Request(String date, RequestForm form, RequestLanguage lan) {
		this.date = date;
		this.form = form;
		this.status = RequestStatus.PROCESSING;
		this.lan = lan;
	}
	@Override
	public int hashCode() {
		return Objects.hash(date, form, lan, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		return Objects.equals(date, other.date) && form == other.form && lan == other.lan && status == other.status;
	}
	@Override
	public String toString() {
		return "Request [date=" + date + ", form=" + form + ", status=" + status + ", lan=" + lan + "]";
	}
	
}
