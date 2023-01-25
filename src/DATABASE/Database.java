package DATABASE;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import Enums.RegistrationStatus;
import Study.*;
import Users.*;


public class Database implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Database INSTANCE;
	public Vector<User> users = new Vector<User>();;
	public Vector<Course> courses = new Vector<Course>();;
	public Vector<Lesson> lessons = new Vector<Lesson>();;
	public RegistrationStatus registrationStatus = RegistrationStatus.CLOSE;;
	public Vector<Message> messages = new Vector<Message>();;
	public Vector<Request> requests = new Vector<Request>();;
	
	static {
		if(new File("data").exists()) {
			try {
				INSTANCE = read();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			INSTANCE = new Database();
		}
	}
	private Database() {}
	public static Database read() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream("data");
		ObjectInputStream ois = new ObjectInputStream(fis);
		return (Database) ois.readObject();
	}
	public static void write() throws IOException {
		FileOutputStream fos = new FileOutputStream("data");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(INSTANCE);
		oos.close();
		fos.close();
	}
	public List<Student> getStudents(){
		//return Database.INSTANCE.users.stream().filter(s -> s instanceof Student).collect(Collectors.toList());
		List<Student> students = new ArrayList<Student>();
		for(User user : users) {
			if(user instanceof Student) {
				students.add((Student) user);
			}
		}
		return students;
	}
	public List<Teacher> getTeachers(){
//		return Database.INSTANCE.users.stream().filter(s -> s instanceof Teacher).collect(Collectors.toList());
		List<Teacher> teachers = new ArrayList<Teacher>();
		for(User user : users) {
			if(user instanceof Teacher) {
				teachers.add((Teacher) user);
			}
		}
		return teachers;
	}
	public List<User> getEmployee(){
		return Database.INSTANCE.users.stream().filter(s -> s instanceof Employee).collect(Collectors.toList());
	}
	public static int nextID() {
		return INSTANCE.users.size() + 1;
	}
}
