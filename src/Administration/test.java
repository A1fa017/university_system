package Administration;

import java.io.IOException;

import DATABASE.Database;

public class test {
	public static void main(String args[]) throws IOException {
		Admin admin = new Admin();
		admin.setEmail("Super@kbtu.kz");
		Database.INSTANCE.users.add(admin);
		Database.write();
	}
}
