package Management;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import DATABASE.Database;
import Enums.Day;
import Enums.Format;
import Enums.LessonType;
import Enums.RegistrationStatus;
import Enums.RequestForm;
import Enums.RequestLanguage;
import Enums.RequestStatus;
import Enums.RequestTypeEmployee;
import Study.*;
import Users.*;


public class Manager extends Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Manager() {
		
	}
	public Manager(String firstName, String lastName) {
		super(firstName, lastName);
	}
	private void printList(List list) {
		for(int i=0; i<list.size(); i++)
			System.out.println(i+1+ ")" +list.get(i));
	}
	public void addCourse() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter name of course:");
		String name = br.readLine();
		System.out.println("Enter description of course:");
		String descrip = br.readLine();
		System.out.println("Enter credits of course:");
		int credits = Integer.parseInt(br.readLine());
		System.out.println("Has this course a prerequisit?\n1.Yes\n2.No");
		int choice = Integer.parseInt(br.readLine());
		if(choice == 1) {
			System.out.println("Choose prerequisit of this course:");
			printList(Database.INSTANCE.courses);
			choice = Integer.parseInt(br.readLine());
			Course prereq = Database.INSTANCE.courses.get(choice - 1);
			Database.INSTANCE.courses.add(new Course(name, descrip, credits, prereq));
			System.out.println("Course is added!");
		}else if(choice == 2){
			Database.INSTANCE.courses.add(new Course(name, descrip, credits));
			System.out.println("Course is added!");
		}
	}
	public void removeCourse() throws NumberFormatException, IOException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choose any course:");
			printList(Database.INSTANCE.courses);
			int choice = Integer.parseInt(br.readLine());
			Course course = Database.INSTANCE.courses.get(choice - 1);
			List<Student> students = Database.INSTANCE.getStudents().stream().filter(s -> s.getCourses().containsKey(course)).collect(Collectors.toList());
			for(Student student : students) {
				student.getCourses().remove(course);
			}
			List<Teacher> teachers = Database.INSTANCE.getTeachers().stream().filter(s -> s.course.equals(course)).collect(Collectors.toList());
			for(Teacher teacher : teachers) {
				teacher.course = new Course();
			}
			Database.INSTANCE.courses.remove(choice - 1);
			System.out.println("Course is removed!");
		}catch(Exception e) {
			System.out.println("This course doesn't exist!");
		}
	}
	public void addLesson() throws NumberFormatException, IOException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nChoose any course:");
			printList(Database.INSTANCE.courses);
			int choice = Integer.parseInt(br.readLine());
			Course course = Database.INSTANCE.courses.get(choice - 1);
			System.out.println("\nChoose any teacher:");
			List<Teacher> teachers = Database.INSTANCE.getTeachers().stream().filter(s -> s.course.equals(course)).collect(Collectors.toList());
			printList(teachers);
			choice = Integer.parseInt(br.readLine());
			Teacher teacher = teachers.get(choice - 1);
			System.out.println("\nChoose a day:");
			int i = 1;
			for(Day day: Day.values()){
				System.out.println(i + ")" + day);
				i ++;
			}
			choice = Integer.parseInt(br.readLine());
			Day day = Day.values()[choice - 1];
			System.out.println("\nEnter start time: ");
			int h = Integer.parseInt(br.readLine());
			int m = Integer.parseInt(br.readLine());
			int s = Integer.parseInt(br.readLine());
			Time start = new Time(h, m, s);
			System.out.println("\nEnter end time: ");
			h = Integer.parseInt(br.readLine());
			m = Integer.parseInt(br.readLine());
			s = Integer.parseInt(br.readLine());
			Time end = new Time(h, m, s);
			System.out.println("\nChoose type of lesson: ");
			i = 1;
			for(LessonType type: LessonType.values()) {
				System.out.println(i + ")" + type);
				i ++;
			}
			choice = Integer.parseInt(br.readLine());
			LessonType type = LessonType.values()[choice - 1];
			System.out.println("\nChoose format of lesson: ");
			i = 1;
			for(Format format: Format.values()) {
				System.out.println(i + ")" + format);
				i ++;
			}
			choice = Integer.parseInt(br.readLine());
			Format format = Format.values()[choice - 1];
			if(format.equals(Format.OFFLINE)) {
				System.out.println("\nEnter room of lesson: ");
				int room = Integer.parseInt(br.readLine());
				Database.INSTANCE.lessons.add(new Lesson(course, day, teacher.getFirstName().charAt(0) + ". " + teacher.getLastName(), start, end, format, type, room));
				teacher.getLessons().add(new Lesson(course, day, teacher.getFirstName().charAt(0) + ". " + teacher.getLastName(), start, end, format, type, room));
				System.out.println("Lesson is added!");
				return;
			}
			Database.INSTANCE.lessons.add(new Lesson(course, day, teacher.getFirstName().charAt(0) + ". " + teacher.getLastName(), start, end, format, type));
			teacher.getLessons().add(new Lesson(course, day, teacher.getFirstName().charAt(0) + ". " + teacher.getLastName(), start, end, format, type));
			System.out.println("Lesson is added!");
		}catch(Exception e) {
			System.out.println("OOPS!");
		}
	}
	public void removeLesson() throws NumberFormatException, IOException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choose a lesson:");
			printList(Database.INSTANCE.lessons);
			int choice = Integer.parseInt(br.readLine());
			Lesson lesson = Database.INSTANCE.lessons.get(choice - 1);
			List<Student> students = Database.INSTANCE.getStudents().stream().filter(s -> s.getLessons().contains(lesson)).collect(Collectors.toList());
			for(Student student : students) {
				student.getLessons().remove(lesson);
			}
			List<Teacher> teachers = Database.INSTANCE.getTeachers().stream().filter(s -> s.getLessons().contains(lesson)).collect(Collectors.toList());
			for(Teacher teacher : teachers) {
				teacher.getLessons().remove(lesson);
			}
			Database.INSTANCE.lessons.remove(choice - 1);
			System.out.println("Lesson is removed!");
		}catch(Exception e) {
			System.out.println("This lesson doesn't exist!");
		}
	}
	public void assignCourse() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose any teacher:");
		printList(Database.INSTANCE.getTeachers());
		int choice = Integer.parseInt(br.readLine());
		Teacher teacher = (Teacher) Database.INSTANCE.getTeachers().get(choice - 1);
		System.out.println("Choose any course:");
		printList(Database.INSTANCE.courses);
		choice = Integer.parseInt(br.readLine());
		Course course = Database.INSTANCE.courses.get(choice - 1);
		teacher.course = course;
		System.out.println("Course " + course.name + " is added for " + teacher.getFirstName() + " " + teacher.getLastName());
	}
	public void sendRequest() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose a type of request:");
		int i = 1;
		for(RequestTypeEmployee type : RequestTypeEmployee.values()) {
			if(type == RequestTypeEmployee.Increase) {
				System.out.println(i +") For increase");
			}
			if(type == RequestTypeEmployee.Dismissal) {
				System.out.println(i + ") For dismissal");
			}
			if(type == RequestTypeEmployee.Stipend) {
				System.out.println(i + ") For stipend");
			}
			i ++;
		}
		int choice = Integer.parseInt(br.readLine());
		RequestTypeEmployee type = RequestTypeEmployee.values()[choice - 1];
		System.out.println("Choose a language of request: ");
		i = 1;
		for(RequestLanguage language: RequestLanguage.values()) {
			System.out.println(i + ") " + language);
			i ++;
		}
		choice = Integer.parseInt(br.readLine());
		RequestLanguage language = RequestLanguage.values()[choice - 1];
		System.out.println("Choose a form of request:");
		i = 1;
		for(RequestForm form : RequestForm.values()) {
			System.out.println(i + ") " + form);
			i ++;
		}
		choice = Integer.parseInt(br.readLine());
		RequestForm form = RequestForm.values()[choice - 1];
		LocalDateTime local = LocalDateTime.now();
		String date = local.getYear() + "-" + local.getMonthValue() + "-" + local.getDayOfMonth() + " " + local.getHour() + ":" + local.getMinute();
		super.getRequests().add(new RequestEmployee(date, form, language, type));
		Database.INSTANCE.requests.add(new RequestEmployee(date, form, language, type));
		System.out.println("\nRequest is sended");
	}
	public void viewSelfRequests() {
		printList(super.getRequests());
	}
	
	public void viewRequests() throws NumberFormatException, IOException {
		List<Request> l = Database.INSTANCE.requests.stream().filter(s -> s.status == RequestStatus.PROCESSING).collect(Collectors.toList());
		printList(l);
		System.out.println("\n1.Process request\n2.Return back\n3.Exit");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int choice = Integer.parseInt(br.readLine());
		if(choice == 1) {
			process:while(true) {
				processRequest();
				System.out.println("\n1.Process another request\n2.Return back\n3.Exit");
				choice = Integer.parseInt(br.readLine());
				if(choice == 1) {
					continue process;
				}
				if(choice == 2) break process;
				if(choice == 3) {
					exit();
					System.exit(0);
				}
			}
		}
		if(choice == 2) return;
		if(choice == 3) {
			exit();
			System.exit(0);
		}
	}
	public void processRequest() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose any request:");
		List<Request> l = Database.INSTANCE.requests.stream().filter(s -> s.status == RequestStatus.PROCESSING).collect(Collectors.toList());
		printList(l);
		int choice = Integer.parseInt(br.readLine());
		Request request = l.get(choice - 1);
		request.status = RequestStatus.DONE;
		System.out.println("Request is done!");
	}
	public void viewPersonalData() {
		System.out.println("First name: " + super.getFirstName());
		System.out.println("Last name: " + super.getLastName());
		System.out.println("ID: " + super.getID());
		System.out.println("Email: " + super.getEmail());
		System.out.println("Birth date: " + super.getPersonalData().birthDate);
		System.out.println("Nation: " + super.getPersonalData().nation);
		System.out.println("Phone number: " + super.getPersonalData().phoneNumber);
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	public void run() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		if(super.getPassword().equals("no")) {
			System.out.println("\nCome up with a password:");
			String password = br.readLine();
			super.setPassword(password);
		}
		if(super.getPersonalData() == null) {
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
			System.out.println("\nWhat do you want to do?");
			System.out.println("1.Add course\n2.Add lesson\n3.Assign course\n4.Chat\n5.Close registration"
					+ "\n6.Open registration\n7.Personal data\n8.Remove course\n9.Remove lesson\n10.Send request"
					+ "\n11.View courses\n12.View lessons\n13.View students\n14.View teachers\n15.View requests\n16.View self requests"
					+ "\n17.Exit");
			int choice = Integer.parseInt(br.readLine());
			if(choice == 1) {
				add:while(true) {
					addCourse();
					System.out.println("\n1.Add another course\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue add;
					else if(choice == 2) break add;
					else if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 2) {
				add:while(true) {
					addLesson();
					System.out.println("\n1.Add another lesson\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue add;
					if(choice == 2) break add;
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 3) {
				assign:while(true) {
					assignCourse();
					System.out.println("\n1.Assign another courrse\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue assign;
					if(choice == 2) break assign;
					if(choice == 3) {
						exit();
						break menu;
					}
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
				Database.INSTANCE.registrationStatus = RegistrationStatus.CLOSE;
				System.out.println("\nRegistration is close!\n");
			}
			else if(choice == 6) {
				Database.INSTANCE.registrationStatus = RegistrationStatus.OPEN;
				System.out.println("\nRegistration is open!\n");
			}
			else if(choice == 7) {
				view:while(true) {
					viewPersonalData();
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
			else if(choice == 8) {
				remove:while(true) {
					removeCourse();
					System.out.println("\n1.Remove another course\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue remove;
					else if(choice == 2) break remove;
					else if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 9) {
				remove:while(true) {
					removeLesson();
					System.out.println("\n1.Remove another lesson\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue remove;
					if(choice == 2) break remove;
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 10) {
				send:while(true) {
					sendRequest();
					System.out.println("\n1.Send another request\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue send;
					if(choice == 2) break send;
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 11) {
				view:while(true) {
					printList(Database.INSTANCE.courses);
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					else if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 12) {
				view:while(true) {
					printList(Database.INSTANCE.lessons);
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					else if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 13) {
				view:while(true) {
					printList(Database.INSTANCE.getStudents());
					System.out.println("\n1.View information\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) {
						System.out.println("Choose any student:");
						choice = Integer.parseInt(br.readLine());
						System.out.println(Database.INSTANCE.getStudents().get(choice - 1).getPersonalData());
					}
					else if(choice == 2) break view;
					else if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 14) {
				view:while(true) {
					printList(Database.INSTANCE.getTeachers());
					System.out.println("\n1.View information\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) {
						System.out.println("Choose any teacher:");
						choice = Integer.parseInt(br.readLine());
						System.out.println(Database.INSTANCE.getTeachers().get(choice - 1).getPersonalData());
					}
					else if(choice == 2) break view;
					else if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 15) {
				view:while(true) {
					viewRequests();
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
			else if(choice == 16) {
				view:while(true) {
					viewSelfRequests();
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
			else if(choice == 17) {
				exit();
				break menu;
			}
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
	public void save() throws IOException {
		Database.write();
	}
}
