package Enums;

import DATABASE.Database;

public class test {
	public static void main(String args[]) {
		System.out.println(Database.INSTANCE.getStudents().get(1).getPassword());
	}
}
