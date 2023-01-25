package Users;

import java.io.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import DATABASE.Database;
import Enums.Day;
import Enums.LessonType;
import Enums.RequestForm;
import Enums.RequestLanguage;
import Enums.RequestTypeEmployee;
import Study.*;

public class Teacher extends Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Course course = new Course();
	private Vector<Lesson> lessons = new Vector<Lesson>();
	
	public Teacher() {
		
	}
	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}
	public Teacher(String firstName, String lastName, Course course) {
		this(firstName, lastName);
		this.course = course;
	}
	public Vector<Lesson> getLessons() {
		return lessons;
	}
	private void printList(List list) {
		for(int i = 0; i < list.size(); i ++) {
			System.out.println(i + 1 + ")" + list.get(i));
		}
	}
	public void viewChat() throws NumberFormatException, IOException {
		super.viewChat();
	}
	public void sendMessages() throws NumberFormatException, IOException {
		super.sendMessages();
	}
	public void putMark() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose a lesson:");
		printList(lessons);
		int choice = Integer.parseInt(br.readLine());
		Lesson lesson = lessons.get(choice - 1);
		System.out.println("Choose a student: ");
		List<Student> students = Database.INSTANCE.getStudents().stream().filter(s -> s.getLessons().contains(lesson)).collect(Collectors.toList());
		printList(students);
		choice = Integer.parseInt(br.readLine());
		Student s = students.get(choice - 1);
		System.out.println("\nChoose an attestation:\n1.Attestation 1\n2.Attestation 2\n3.Final exam");
		choice = Integer.parseInt(br.readLine());
		if(choice == 1) {
			System.out.println("\nEnter grade:");
			int grade = Integer.parseInt(br.readLine());
			while(s.getCourses().get(course).total() + grade > 60) {
				System.out.println("Total sum of attestation should not exceed 60!");
				grade = Integer.parseInt(br.readLine());
			}
			Mark m = s.getCourses().get(course);
			m.att1 = grade;
		}else if(choice == 2) {
			System.out.println("\nEnter grade:");
			int grade = Integer.parseInt(br.readLine());
			while(s.getCourses().get(course).total() + grade > 60) {
				System.out.println("Total sum of attestation should not exceed 60!");
				grade = Integer.parseInt(br.readLine());
			}
			Mark m = s.getCourses().get(course);
			m.att2 = grade;
		}else if(choice == 3) {
			System.out.println("\nEnter grade:");
			int grade = Integer.parseInt(br.readLine());
			while(grade > 40) {
				System.out.println("Final exam point should not exceed 40!");
				grade = Integer.parseInt(br.readLine());
			}
			Mark m = s.getCourses().get(course);
			m.finalExam = grade;
		}
	}
	public void viewStudents() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose a lesson:");
		printList(lessons);
		int choice = Integer.parseInt(br.readLine());
		Lesson lesson = lessons.get(choice - 1);
		List<Student> students = Database.INSTANCE.getStudents().stream().filter(s -> s.getLessons().contains(lesson)).collect(Collectors.toList());
		printList(students); 
	}
	public void viewSchedule() {
		System.out.println();
		System.out.println(String.format("%7s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s", '|', Day.MON, '|', Day.TUE, '|', Day.WED, '|', Day.THU, '|', Day.FRI, '|', Day.SAT, '|', Day.SUN, '|'));
		System.out.println(String.format("%s", "-----------------------------------------------------------------------------------------------------------------------------------------------------------"));
		for(int i = 8; i <= 22; i ++) {
			String str = i + ":00";
			if(str.length() < 5) str += ' ';
			String str1, str2, str3, str4, str5, str6, str7, str11, str12, str13, str14, str15, str16, str17;
			str1 = getLesson1(i, Day.MON);
			str2 = getLesson1(i, Day.TUE);
			str3 = getLesson1(i, Day.WED);
			str4 = getLesson1(i, Day.THU);
			str5 = getLesson1(i, Day.FRI);
			str6 = getLesson1(i, Day.SAT);
			str7 = getLesson1(i, Day.SUN);
			System.out.println(String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s %s", str, '|', str1, '|', str2, '|', str3, '|', str4, '|', str5, '|', str6, '|', str7));
			System.out.println(String.format("%s", "-----------------------------------------------------------------------------------------------------------------------------------------------------------"));
		}
	}
	private String getLesson1(int h, Day day) {
		String str;
		try {
			Optional<Lesson> lesson = lessons.stream().filter(s -> h >= s.start.getHours() && h < s.end.getHours() && s.day == day).findAny();
			if(lesson.get().type == LessonType.LECTURE) {
				str = lesson.get().course.name + " " + 'L' + " " + lesson.get().room;
			}else {
				str = lesson.get().course.name + " " + 'P' + " " + lesson.get().room;
			}
		}catch(Exception e) {
			str = "";
		}
		while(str.length() < 19) {
			str += ' ';
		}
		return str;
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
	public void viewRequests() {
		printList(super.getRequests());
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
	public void run() throws NumberFormatException, IOException {
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
			System.out.println("What do you want to do?");
			System.out.println("1.Chat\n2.Personal data\n3.View students\n4.Put mark\n5.Schedule\n6.Send request\n7.View requests\n8.Exit");
			int choice = Integer.parseInt(br.readLine());
			if(choice == 1) {
				view: while(true) {
					viewChat();
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 2) {
				view:while(true) {
					viewPersonalData();
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 3) {
				view:while(true) {
					viewStudents();
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 4) {
				put:while(true) {
					putMark();
					System.out.println("\n1.Rate another student\n2.Return back\n3.Exit");
					choice =Integer.parseInt(br.readLine());
					if(choice == 1) continue put;
					if(choice == 2) break put;
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 5) {
				view:while(true) {
					viewSchedule();
					System.out.println("\n1.Return back\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 6) {
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
			else if(choice == 7) {
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
			else if(choice == 8) {
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
