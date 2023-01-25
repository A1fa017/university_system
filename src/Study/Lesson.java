package Study;

import java.io.*;
import java.sql.Time;
import java.util.Objects;

import Enums.Day;
import Enums.Format;
import Enums.LessonType;

public class Lesson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Course course = new Course();
	public Day day;
	public String teacher;
	public Time start;
	public Time end;
	public Format format;
	public LessonType type;
	public int room;
	
	public Lesson() {
		
	}
	public Lesson(Course course, Day day, String teacher, Time start, Time end, Format format, LessonType type) {
		this.course = course;
		this.day = day;
		this.teacher = teacher;
		this.start = start;
		this.end = end;
		this.format = format;
		this.type = type;
	}
	public Lesson(Course course, Day day, String teacher, Time start, Time end, Format format, LessonType type, int room) {
		this(course, day, teacher, start, end, format, type);
		this.room = room;
	}
	@Override
	public int hashCode() {
		return Objects.hash(course, day, end, format, start, teacher, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lesson other = (Lesson) obj;
		return Objects.equals(course, other.course) && day == other.day && Objects.equals(end, other.end)
				&& format == other.format && Objects.equals(start, other.start)
				&& Objects.equals(teacher, other.teacher) && type == other.type;
	}
	public String toString() {
		return "[Course: " + course.name + ", Day: " + day + ", Teacher: " + teacher + ", Start time: " + start + ", End time: " + end + 
				", Format: " + format + ", Lesson type: " + type + "]"; 
	}
}
