package kr.ac.mju.bean;
/**
 * 
 * @author 3조
 *	출석현황의 정보를 가지고 있는 Bean Class
 */
public class ClassCheckBean {
	private int id;
	private String classDateTime;
	private String attendanceCheck;
	
	public ClassCheckBean(){}
	
	public ClassCheckBean(int id, String classDateTime, String attendanceCheck){
		this.id=id;
		this.classDateTime=classDateTime;
		this.attendanceCheck=attendanceCheck;
	}

	public int getId() {
		return id;
	}

	public String getClassDateTime() {
		return classDateTime;
	}

	public String getAttendanceCheck() {
		return attendanceCheck;
	}
	
}
