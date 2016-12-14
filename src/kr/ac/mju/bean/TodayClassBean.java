package kr.ac.mju.bean;
/**
 * 
 * @author 3조
 *	홈화면의 오늘 수업을 들어야할 목록의 아이템 하나당의 정보를 가진 Bean Class
 */
public class TodayClassBean {
	private int id;
	private String className;
	private String classDuration;
	private String pName;
	private String cno;
	
	public TodayClassBean(){}
	
	public TodayClassBean(int id, String className, String classDuration, String pName, String cno){
		this.id = id;
		this.className = className;
		this.classDuration = classDuration;
		this.pName = pName;
		this.cno = cno;
	}

	public int getId() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	public String getClassDuration() {
		return classDuration;
	}

	public String getpName() {
		return pName;
	}
	
	public String getCno() {
		return cno;
	}
	
}