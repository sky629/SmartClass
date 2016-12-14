package kr.ac.mju.bean;
/**
 * 
 * @author 3조
 *	공지의 정보를 갖는 Bean Class
 */
public class MessageBean {
	private int id;
	private String className;
	private String messageTitle;
	private String messageDate;
	
	public MessageBean(){}
	
	public MessageBean(int id, String className, String messageTitle, String messageDate){
		this.id=id;
		this.className=className;
		this.messageTitle=messageTitle;
		this.messageDate=messageDate;
	}

	public int getId() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public String getMessageDate() {
		return messageDate;
	}
	
}
