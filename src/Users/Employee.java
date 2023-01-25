package Users;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import DATABASE.Database;
import Enums.RequestForm;
import Enums.RequestLanguage;
import Enums.RequestTypeEmployee;
import Enums.RequestTypeStudent;
import Study.Lesson;

public abstract class Employee extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Vector<Message>> messages = new HashMap<String, Vector<Message>>();
	
	public Employee() {
	
	}
	public Employee(String firstName, String lastName) {
		super(firstName, lastName);
	}
	private void printList(List list) {
		for(int i=0; i<list.size(); i++)
			System.out.println(i+1+ ")" +list.get(i));
	}
	public HashMap<String, Vector<Message>> getMessages() { 
		return messages;
	}
	public void viewChat() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List<String> workers = new ArrayList<String>(messages.keySet());
		for(String worker : workers) {
			System.out.println(worker);
		}
		System.out.println("\n1.View message\n2.Send message\n3.Return back\n4.Exit");
		int choice = Integer.parseInt(br.readLine());
		if(choice == 1) {
			System.out.println("Choose any user:");
			printList(workers);
			choice = Integer.parseInt(br.readLine());
			String employee = workers.get(choice - 1);
			for(Message message : messages.get(employee)) {
				System.out.println(message.sender + ": " + message.text);
			}
		}
		if(choice == 2) {
			sendMessages();
		}
		if(choice == 3) {
			return;
		}
		if(choice == 4) {
			exit();
			System.exit(0);
		}
	}
	public void sendMessages() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose any user:");
		List<User> workers = Database.INSTANCE.getEmployee();
		int i = 1;
		for(User user : workers) {
			System.out.println(i + ")" + user);
			i ++;
		}
		int choice = Integer.parseInt(br.readLine());
		Employee employee = (Employee) workers.get(choice - 1);
		System.out.println("Enter your message:");
		String txt = br.readLine();
		LocalDateTime local = LocalDateTime.now();
		String date = local.getYear() + "-";
		Vector<String> v = new Vector<String>();
		v.add(local.getMonthValue() + "");
		v.add(local.getDayOfMonth() + "");
		v.add(local.getHour() + "");
		v.add(local.getMinute() + "");
		i = 1;
		for(String s : v) {
			if(s.length() < 2) {
				s = "0" + s;
			}
			if(i < 2) {
				date += s + "-";
			}
			else if(i == 2) {
				date += s + " ";
			}
			else if(i == 3) {
				date += s + ":";
			}
			else date += s;
			i ++;
		}
		if(!messages.containsKey(employee.getFirstName() + " " + employee.getLastName())) {
			messages.put(employee.getFirstName() + " " + employee.getLastName(), new Vector<Message>());
			employee.messages.put(this.getFirstName() + " " + this.getLastName(), new Vector<Message>());
		}
		Message message = new Message(this.getFirstName() + " " + this.getLastName(), employee.getFirstName() + " " + employee.getLastName(), txt, date);
		messages.get(employee.getFirstName() + " " + employee.getLastName()).add(message);
		employee.messages.get(this.getFirstName() + " " + this.getLastName()).add(message);
		Database.INSTANCE.messages.add(message);
	}
	public void exit() {
		System.out.println("Bye bye!");
		try {
			save();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void save() throws IOException, Exception {
		Database.write();
	}
}
