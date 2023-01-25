package Study;

import java.sql.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;

import Administration.Admin;
import DATABASE.Database;
import Management.Manager;
import Users.Student;

public class test {
	public static void main(String args[]) throws Exception {
		HashMap<String, Vector<String>> h = new HashMap<String, Vector<String>>();
		h.put("a", new Vector<String>());
		h.get("a").add("b");
		h.get("a").add("c");
		System.out.println(!h.containsKey("b"));
	}
}
