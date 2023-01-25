package Study;

import java.io.*;
import java.util.*;

public class Mark implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double att1;
	public double att2;
	public double finalExam;
	
	public Mark() {
		
	}
	public Mark(double att1, double att2, double finalExam) {
		this.att1 = att1;
		this.att2 = att2;
		this.finalExam = finalExam;
	}
	public double total() {
		return att1 + att2 + finalExam;
	}
	public String totalLetter() {
		double digit = total();
		if(digit < 50) {
			return "F";
		}
		if(digit >= 50 && digit < 55) {
			return "D";
		}
		if(digit < 60) {
			return "D+";
		}	
		if(digit < 65) {
			return "C-";
		}
		if(digit < 70) {
			return "C";
		}
		if(digit < 75) {
			return "C+";
		}
		if(digit < 80) {
			return "B-";
		}
		if(digit < 85) {
			return "B";
		}
		if(digit < 90) {
			return "B+";
		}
		if(digit < 95) {
			return "A-";
		}
		return "A";
	}
	
	public double calculateGPA() {
		double digit = total();
		if(digit < 50) return 0;
		if(digit >= 50 && digit < 55) {
			return 1;
		}
		if(digit < 60) {
			return 1.33;
		}	
		if(digit < 65) {
			return 1.67;
		}
		if(digit < 70) {
			return 2;
		}
		if(digit < 75) {
			return 2.33;
		}
		if(digit < 80) {
			return 2.67;
		}
		if(digit < 85) {
			return 3;
		}
		if(digit < 90) {
			return 3.33;
		}
		if(digit < 95) {
			return 3.67;
		}
		return 4;
	}
	@Override
	public int hashCode() {
		return Objects.hash(att1, att2, finalExam);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mark other = (Mark) obj;
		return Double.doubleToLongBits(att1) == Double.doubleToLongBits(other.att1)
				&& Double.doubleToLongBits(att2) == Double.doubleToLongBits(other.att2)
				&& Double.doubleToLongBits(finalExam) == Double.doubleToLongBits(other.finalExam);
	}
	
}
