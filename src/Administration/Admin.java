package Administration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import DATABASE.Database;
import Management.Manager;
import Study.Course;
import Study.Lesson;
import Users.Employee;
import Users.PersonalData;
import Users.Student;
import Users.Teacher;
import Users.User;

public class Admin extends Employee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Admin() {
		
	}
	public Admin(String firstName, String lastName) {
		super(firstName, lastName);
	}
	private void printList(List list) {
		for(int i=0; i<list.size(); i++)
			System.out.println(i+1+ ")" +list.get(i));
	}
	public boolean add() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter first name of user:");
		String firstName = br.readLine();
		System.out.println("Enter last name of user:");
		String lastName = br.readLine();
		System.out.println("Who is user?\n1.Admin\n2.Manager\n3.Teacher\n4.Student");
		int choice = Integer.parseInt(br.readLine());
		User u;
		if(choice == 1) {
			u = new Admin(firstName, lastName);
		}
		else if(choice == 2){
			u = new Manager(firstName, lastName);
		}
		else if(choice == 3) {
			System.out.println("Does this teacher have a course?\n1.Yes\n2.No");
			choice = Integer.parseInt(br.readLine());
			if(choice == 1) {
				System.out.println("Which course?");
				printList(Database.INSTANCE.courses);
				choice = Integer.parseInt(br.readLine());
				Course course = Database.INSTANCE.courses.get(choice - 1);
				u = new Teacher(firstName, lastName, course);
			}
			else u = new Teacher(firstName, lastName);
		}
		else {
			System.out.println("Enter year of study:");
			int yearOfStudy = Integer.parseInt(br.readLine());
			u = new Student(firstName, lastName, yearOfStudy);
		}
		for(User user : Database.INSTANCE.users) {
			if(user.equalsWithName(u)) {
				System.out.println("This user is already exist!");
				return false;
			}
		}
		Database.INSTANCE.users.add(u);
		System.out.println("User is added!");
		return true;
	}
	public boolean remove() throws NumberFormatException, IOException {
		System.out.println("Choose any user: ");
		printList(Database.INSTANCE.users);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int choice = Integer.parseInt(br.readLine());
		if(choice > Database.INSTANCE.users.size() || Database.INSTANCE.users.size() == 0) {
			System.out.println("This user don't exist!");
			return false;
		}
		User user = Database.INSTANCE.users.get(choice - 1);
		if(user instanceof Teacher) {
			List<Lesson> lessons = Database.INSTANCE.lessons.stream().filter(s -> s.teacher.equals(user.getFirstName().charAt(0) + ". " + user.getLastName())).collect(Collectors.toList());
			for(Lesson lesson : lessons) {
				lesson.teacher = "";
			}
		}
		Database.INSTANCE.users.remove(choice - 1);
		System.out.println("User is removed!");
		return true;
	}
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if(super.getPassword().equals("no") && !super.getEmail().equals("Super@kbtu.kz")) {
				System.out.println("\nCome up with a password:");
				String password = br.readLine();
				super.setPassword(password);
			}
			if(super.getPersonalData() == null && !super.getEmail().equals("Super@kbtu.kz")) {
				System.out.println("Enter your birth year:");
				int year = Integer.parseInt(br.readLine());
				System.out.println("Enter your birth month:");
				int month = Integer.parseInt(br.readLine());
				System.out.println("Enter your birth day:");
				int day = Integer.parseInt(br.readLine());
				System.out.println("Enter your nationality:");
				String nation = br.readLine();
				System.out.println("Enter your phone number:");
				int phoneNumber = Integer.parseInt(br.readLine());
				super.setPersonalData(new PersonalData(new Date(year, month, day), nation, phoneNumber));
			}
			menu:while(true) {
				System.out.println("\nWhat do you want to do?\n1.Add user\n2.Remove user\n3.View users\n4.Chat\n5.Exit");
				int choice = Integer.parseInt(br.readLine());
				if(choice == 1) {
					add:while(true) {
						add(); 
						System.out.println("\n1.Add another user\n2.Return back\n3.Exit");
						choice = Integer.parseInt(br.readLine());
						if(choice == 1) {
							continue add;
						}else if(choice == 2) {
							break add;
						}else if(choice == 3) {
							exit();
							break menu;
						}
					}
				}
				else if(choice == 2) {
					remove:while(true) {
						remove();
						System.out.println("\n1.Remove another user\n2.Return back\n3.Exit");
						choice = Integer.parseInt(br.readLine());
						if(choice == 1) continue remove;
						else if(choice == 2) break remove;
						else if(choice == 3) {
							exit();
							break menu;
						}
					}
				}
				else if(choice == 3) {
					printList(Database.INSTANCE.users);
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue menu;
					else if(choice == 2) {
						exit();
						break menu;
					}
				}
				else if(choice == 4) {
					view:while(true) {
						viewChat();
						System.out.println("\n1.Return back\n2.Exit");
						choice = Integer.parseInt(br.readLine());
						if(choice == 1) {
							break view;
						}
						if(choice == 2) {
							exit();
							break menu;
						}
					}
				}
				else if(choice == 5) {
					exit();
					break menu;
				}
			}
		}catch(Exception e) {
			System.out.println("Something bad happened... \n Saving resources...");
			
		}
	}
	public void exit() {
		System.out.println("Bye bye!");
		try {
			save();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void save() throws Exception {
		Database.write();
	}
}
