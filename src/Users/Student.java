package Users;

import java.io.*;


import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import DATABASE.Database;
import Enums.*;
import Enums.LessonType;
import Enums.RegistrationStatus;
import Study.*;

public class Student extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double gpa;
	private int credits;
	private int yearOfStudy;
	private HashMap<Course, Mark> courses = new HashMap<Course, Mark>();
	private Vector<Lesson> lessons = new Vector<Lesson>();
	
	public Student() {
		
	}
	public Student(String firstName, String lastName) {
		super(firstName, lastName);
	}
	public Student(String firstName, String lastName, int yearOfStudy) {
		this(firstName, lastName);
		this.yearOfStudy = yearOfStudy;
	}
	public HashMap<Course, Mark> getCourses(){
		return courses;
	}
	public Vector<Lesson> getLessons(){
		return lessons;
	}
	private void printList(List list) {
		for(int i=0; i<list.size(); i++)
			System.out.println(i+1+ ")" +list.get(i));
	}
	public boolean registrationForCourse() throws NumberFormatException, IOException {
		if(Database.INSTANCE.registrationStatus == RegistrationStatus.OPEN) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choose any course:");
			printList(Database.INSTANCE.courses);
			int choice = Integer.parseInt(br.readLine());
			Course course = Database.INSTANCE.courses.get(choice - 1);
			if(credits + course.credits > 30) {
				System.out.println("You can't get this course!");
				return false;
			}
			if(courses.containsKey(course)) {
				System.out.println("You have this course!");
				return false;
			}
			if(courses.containsKey(course.prereq)) {
				if(courses.get(course.prereq).total() < 50) {
					System.out.println("You can't get this course!");
					return false;
				}
			}
			courses.put(course, new Mark());
			System.out.println("Course is added!");
			return true;
		}
		System.out.println("Registration is close!");
		return false;
	}
	public void registrationForLesson() throws NumberFormatException, IOException {
		if(Database.INSTANCE.registrationStatus == RegistrationStatus.CLOSE) {
			System.out.println("Registration is close");
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose a course: ");
		List<Course> c = new ArrayList<Course>(courses.keySet());
		printList(c);
		int choice = Integer.parseInt(br.readLine());
		Course course = c.get(choice - 1);
		List<Lesson> l = lessons.stream().filter(s -> s.course.equals(course)).collect(Collectors.toList());
		if(l.size() == 2) {
			System.out.println("You are already registered for the lessons of this course!");
			return;
		}
		System.out.println("Choose a lesson: ");
		List<Lesson> l1 = Database.INSTANCE.lessons.stream().filter(s -> s.course.equals(course)).collect(Collectors.toList());
		int i = 1;
		for(Lesson ls : l1) {
			boolean ok = true;
			for(Lesson ls1 : lessons) {
				if(((ls.start.equals(ls1.start) || ls.end.equals(ls1.end)) || (ls.start.after(ls1.start) && ls.start.before(ls1.end))
						|| (ls.end.after(ls1.start) && ls.end.before(ls1.start))) && ls.day == ls1.day) {
						ok = false;
						break;
				}
			}
			if(ok) System.out.println(i + ")" + ls);
			else System.err.println(i + ")" + ls);
			i ++;
		}
		choice = Integer.parseInt(br.readLine());
		Lesson lesson = l1.get(choice - 1);
		for(Lesson ls1 : lessons) {
			if(((lesson.start.equals(ls1.start) || lesson.end.equals(ls1.end)) || (lesson.start.after(ls1.start) && lesson.start.before(ls1.end))
					|| (lesson.end.after(ls1.start) && lesson.end.before(ls1.start))) && ls1.day == lesson.day) {
					System.out.println("You have antoher lesson on this time!");
					return;
			}
		}
		lessons.add(lesson);
		Teacher t = new Teacher();
		for(User user : Database.INSTANCE.getTeachers()) {
			Teacher teacher = (Teacher) user;
			if((teacher.getFirstName().charAt(0) + ". " + teacher.getLastName()).equals(lesson.teacher)) {
				t = teacher;
				break;
			}
		}
		System.out.println("Lesson is added!");
	}
	@SuppressWarnings("unlikely-arg-type")
	public void removeLesson() throws NumberFormatException, IOException {
		if(Database.INSTANCE.registrationStatus == RegistrationStatus.CLOSE) {
			System.out.println("Registration is close");
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose a course: ");
		List<Course> c = new ArrayList<Course>(courses.keySet());
		printList(c);
		int choice = Integer.parseInt(br.readLine());
		Course course = c.get(choice - 1);
		System.out.println("Choose a lesson: ");
		List<Lesson> l = lessons.stream().filter(s -> s.course.equals(course)).collect(Collectors.toList());
		printList(l);
		choice = Integer.parseInt(br.readLine());
		Lesson lesson = lessons.get(choice - 1);
		Teacher t = new Teacher();
		for(User user : Database.INSTANCE.getTeachers()) {
			Teacher teacher = (Teacher) user;
			if((teacher.getFirstName() + " " + teacher.getLastName()).equals(lesson.teacher)) {
				t = teacher;
				break;
			}
		} 
		t.getLessons().remove(this);
		lessons.remove(choice - 1);
		System.out.println("Lesson is removed!");
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
			str11 = getLesson2(i, Day.MON);
			str2 = getLesson1(i, Day.TUE);
			str12 = getLesson2(i, Day.TUE);
			str3 = getLesson1(i, Day.WED);
			str13 = getLesson2(i, Day.WED);
			str4 = getLesson1(i, Day.THU);
			str14 = getLesson2(i, Day.THU);
			str5 = getLesson1(i, Day.FRI);
			str15 = getLesson2(i, Day.FRI);
			str6 = getLesson1(i, Day.SAT);
			str16 = getLesson2(i, Day.SAT);
			str7 = getLesson1(i, Day.SUN);
			str17 = getLesson2(i, Day.SUN);
			System.out.println(String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s %s", str, '|', str1, '|', str2, '|', str3, '|', str4, '|', str5, '|', str6, '|', str7));
			System.out.println(String.format("%7s %s %s %s %s %s %s %s %s %s %s %s %s", '|', str11, '|', str12, '|', str13, '|', str14, '|', str15, '|', str16, '|', str17));
			System.out.println(String.format("%s", "-----------------------------------------------------------------------------------------------------------------------------------------------------------"));
		}
	}
	private String getLesson1(int h, Day day) {
		String str;
		try {
			Optional<Lesson> l = lessons.stream().filter(s -> h >= s.start.getHours() && h < s.end.getHours() && s.day == day).findAny();
			if(l.get().type == LessonType.LECTURE) {
				str = l.get().course.name + " " + 'L';
			}else {
				str = l.get().course.name + " " + 'P';
			}
		}catch(Exception e) {
			str = "";
		}
		while(str.length() < 19) {
			str += ' ';
		}
		return str;
	}
	private String getLesson2(int h, Day day) {
		String str;
		try {
			Optional<Lesson> l = lessons.stream().filter(s -> h >= s.start.getHours() && h < s.end.getHours() && s.day == day).findAny();
			str = l.get().teacher + " " + l.get().room; 
		}catch(Exception e) {
			str = "";
		}
		while(str.length() < 19) {
			str += ' ';
		}
		return str;
	}
	public void viewTranscript() {
		System.out.println(String.format("%20s %s %10s %s %5s %s %3s %s %2s %s %7s", '|', "     Name", '|', "  Credits", '|', "Digit grade", '|', "Letter grade", '|', "    GPA", '|'));
		System.out.println(String.format("%20s %s %s",'|', "----------------------------------------------------------------------------------", '|'));
		List<Course> c = new ArrayList<Course>(courses.keySet());
		for(Course course : c) {
			String name = course.name;
			while(name.length() < 18) {
				name += ' ';
			}
			String credit = "   " + course.credits + " ";
			while(credit.length() < 13) {
				credit += ' ';
			}
			String digit = "   " + courses.get(course).total() + " ";
			while(digit.length() < 13) {
				digit += ' ';
			}
			String letter = "   " + courses.get(course).totalLetter();
			while(letter.length() < 13) {
				letter += ' ';
			}
			String gpa = "    " + courses.get(course).calculateGPA() + " ";
			while(gpa.length() < 13) {
				gpa += ' ';
			}
			System.out.println(String.format("%20s %s %s %s %s %s %s %s %s %s %s",'|', name, '|', credit, '|', digit, '|', letter, '|', gpa, '|'));
			System.out.println(String.format("%20s %s %s",'|', "----------------------------------------------------------------------------------", '|'));
		}
	}
	public void viewAttestation() {
		List<Course> c = new ArrayList<Course>(courses.keySet());
		for(Course course : c) {
			String name = course.name;
			if(name.length() < 18) {
				name += ' ';
			}
			String first = "   " + courses.get(course).att1 + " ";
			while(first.length() < 10) {
				first += " ";
			}
			String second = "   " + courses.get(course).att2 + " ";
			while(second.length() < 10) {
				second += " ";
			}
			String f = "   " + courses.get(course).finalExam + " ";
			while(f.length() < 10) {
				f += ' ';
			}
			String letter = "   " + courses.get(course).totalLetter();
			while(letter.length() < 10) {
				letter += ' ';
			}
			System.out.println(String.format("%20s %s %s %s %s %s %s %s %s %s %s", '|', name, '|', first, '|', second, '|', f, '|', letter, '|'));
			System.out.println(String.format("%20s %s %s",'|', "--------------------------------------------------------", '|'));
			
		}
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
		for(RequestTypeStudent type : RequestTypeStudent.values()) {
			if(type == RequestTypeStudent.AcademicMob) {
				System.out.println(i + ") Academic mobility");
			}
			if(type == RequestTypeStudent.Stipend) {
				System.out.println(i + ") For stipend");
			}
			if(type == RequestTypeStudent.ThePlaceOfDemand) {
				System.out.println(i + ") For the place of demand");
			}
			if(type == RequestTypeStudent.WorkAround) {
				System.out.println(i + ") For work around");
			}
			if(type == RequestTypeStudent.Transcript) {
				System.out.println(i + ") For transcript");
			}
			i ++;
		}
		int choice = Integer.parseInt(br.readLine());
		RequestTypeStudent type = RequestTypeStudent.values()[choice - 1];
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
		super.getRequests().add(new RequestStudent(date, form, language, type));
		Database.INSTANCE.requests.add(new RequestStudent(date, form, language, type));
		System.out.println("\nRequest is sended!");
	}
	public void viewRequests() {
		printList(super.getRequests());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(courses, credits, gpa, yearOfStudy);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(courses, other.courses) && credits == other.credits
				&& Double.doubleToLongBits(gpa) == Double.doubleToLongBits(other.gpa)
				&& yearOfStudy == other.yearOfStudy;
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
			System.out.println("\nWhat do you want to do?");
			System.out.println("1.Personal data\n2.Registration for course\n3.Registration for lesson\n4.Remove lesson"
					+ "\n5.Schedule\n6.Send request\n7.View requests\n8.View attestation\n9.View transcript\n10.View courses\n11.Exit");
			int choice = Integer.parseInt(br.readLine());
			if(choice == 1) {
				view: while(true) {
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
			else if(choice == 2) {
				registration:while(true) {
					registrationForCourse();
					System.out.println("\n1.Choose another course\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue registration;
					else if(choice == 2) break registration;
					else if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 3) {
				registration:while(true) {
					registrationForLesson();
					System.out.println("\n1.Add another lesson\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) continue registration;
					if(choice == 2) break registration;
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 4) {
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
			else if(choice == 7) {
				view:while(true) {
					viewRequests();
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
				send: while(true) {
					sendRequest();
					System.out.println("\n1.Send another request\n2.Return back\n3.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) {
						continue send;
					}
					if(choice == 2) {
						break send;
					}
					if(choice == 3) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 8) {
				view: while(true) {
					viewAttestation();
					System.out.println("\n1.Return\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 9) {
				view: while(true) {
					viewTranscript();
					System.out.println("\n1.Return\n2.Exit");
					choice = Integer.parseInt(br.readLine());
					if(choice == 1) break view;
					if(choice == 2) {
						exit();
						break menu;
					}
				}
			}
			else if(choice == 10) {
				for(Course course: courses.keySet()) {
					System.out.println(course);
				}
				System.out.println("\n1.Return back\n2.Exit");
				choice = Integer.parseInt(br.readLine());
				if(choice == 1) continue menu;
				else if(choice == 2) {
					exit();
					break menu;
				}
			}
			
			else if(choice == 11) {
				exit();
				break menu;
			}
		}
	}
	public void exit() {
		System.out.println("\nBye bye!");
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
