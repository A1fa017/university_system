package Study;

import java.io.*;
import java.util.*;

public class Course implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String descrip;
	public Course prereq;
	public int credits;
	
	public Course() {
		
	}
	public Course(String name, String descrip, int credits) {
		this.name = name;
		this.descrip = descrip;
		this.credits = credits;
	}
	public Course(String name, String descrip, int credits, Course prereq) {
		this.name = name;
		this.descrip = descrip;
		this.credits = credits;
		this.prereq = prereq;
	}
	@Override
	public int hashCode() {
		return Objects.hash(credits, descrip, name, prereq);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return credits == other.credits && Objects.equals(descrip, other.descrip) && Objects.equals(name, other.name)
				&& Objects.equals(prereq, other.prereq);
	}
	@Override
	public String toString() {
		String pre;
		if(prereq == null) pre = "NO";
		else pre = prereq.name;
		return "[Name: " + name + ", Description: " + descrip + ", Credits: " + credits + ", Prerequisit: " + pre + "]";
	}
}
