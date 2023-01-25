package Demo;

import java.io.*;

import Administration.Admin;
import DATABASE.Database;
import Management.Manager;
import Users.*;

public class Main {
	public static void display() {
		System.out.println("Welcome to the university system KBTU!");
	}
	public static void enter() throws Exception {
		User user = null;
		menu:while(true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter your email:");
			String email = br.readLine();
			System.out.println("\nEnter your password:");
			String password = br.readLine();
			for(User u : Database.INSTANCE.users) {
				if(u.getEmail().equals(email) && u.getPassword().equals(password)) {
					user = u;
				}
			}
			if(user == null) {
				System.out.println("Wrong email or password");
				continue menu;
			}
			break;
		}
		if(user instanceof Student) {
			Student student = (Student) user;
			student.run();
		}
		if(user instanceof Teacher) {
			Teacher teacher = (Teacher) user;
			teacher.run();
		}
		if(user instanceof Admin) {
			Admin admin = (Admin) user;
			admin.run();
		}
		if(user instanceof Manager) {
			Manager manager = (Manager) user;
			manager.run();
		}
	}
	public static void main(String args[]) throws Exception {
		display();
		enter();
	}
}
