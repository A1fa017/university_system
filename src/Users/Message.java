package Users;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String text;
	public String date;
	public String sender;
	public String recipent;
	
	public Message() {
		
	}
	public Message(String sender, String recipent, String text, String date) {
		this.text = text;
		this.date = date;
		this.sender = sender;
		this.recipent = recipent;
	}
	@Override
	public int hashCode() {
		return Objects.hash(date, recipent, sender, text);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return Objects.equals(date, other.date) && Objects.equals(recipent, other.recipent)
				&& Objects.equals(sender, other.sender) && Objects.equals(text, other.text);
	}
	public String toString() {
		return "[Date: " + date + ", Sender: " + sender + ", Recipent: " + recipent + "]";
	}
}
