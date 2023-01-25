package Users;

import java.awt.AWTException;
import java.sql.Time;
import java.util.Optional;
import java.util.Vector;

import Administration.Admin;
import DATABASE.Database;
import Enums.Day;
import Enums.RequestForm;
import Enums.RequestLanguage;
import Enums.RequestTypeStudent;
import Management.Manager;
import Study.Lesson;

public class test {
	public static void main(String args[]) throws Exception {
		Teacher t = (Teacher) Database.INSTANCE.users.get(1);
		t.run();
//		Student s = (Student) Database.INSTANCE.getStudents().get(0);
//		s.run();
	}
		
}

