package cn.ucai.superkache.bean;


import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

/**
 * EMUser entity. @author MyEclipse Persistence Tools
 */
public class User extends Location implements java.io.Serializable {
	private static final long serialVersionUID = 6848921231724157394L;

	// Fields

	/**
	 * 
	 */
	private Integer muserId=0;
	private String muserName;
	private String muserPassword;
	private String muserNick;
	private Integer muserUnreadMsgCount=0;

	private String header;
	// Constructors

	/** default constructor */
	public User() {
	}
	
	public User(boolean result, int msg) {
		this.setResult(result);
		this.setMsg(msg);
	}

	/** minimal constructor */
	public User(Integer MUserId, String MUserName, String MUserPassword, String MUserNick) {
		this.muserId = MUserId;
		this.muserName = MUserName;
		this.muserPassword = MUserPassword;
		this.muserNick = MUserNick;
	}

	/** full constructor */
	public User(Integer MUserId, String MUserName, String MUserPassword, String MUserNick,
			Integer MUserUnreadMsgCount) {
		this(MUserId, MUserName, MUserPassword, MUserNick);
		this.muserUnreadMsgCount = MUserUnreadMsgCount;
	}

	// Property accessors
	public Integer getMUserId() {
		return this.muserId;
	}

	public void setMUserId(Integer MUserId) {
		this.muserId = MUserId;
	}

	public String getMUserName() {
		return this.muserName;
	}

	public void setMUserName(String MUserName) {
		this.muserName = MUserName;
	}

	public String getMUserPassword() {
		return this.muserPassword;
	}

	public void setMUserPassword(String MUserPassword) {
		this.muserPassword = MUserPassword;
	}

	public String getMUserNick() {
		return this.muserNick;
	}

	public void setMUserNick(String MUserNick) {
		this.muserNick = MUserNick;
	}

	public Integer getMUserUnreadMsgCount() {
		return this.muserUnreadMsgCount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setMUserUnreadMsgCount(Integer MUserUnreadMsgCount) {
		this.muserUnreadMsgCount = MUserUnreadMsgCount;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return Objects.equals(muserId, user.muserId) &&
				Objects.equals(muserName, user.muserName) &&
				Objects.equals(muserPassword, user.muserPassword) &&
				Objects.equals(muserNick, user.muserNick) &&
				Objects.equals(muserUnreadMsgCount, user.muserUnreadMsgCount) &&
				Objects.equals(getHeader(), user.getHeader());
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public int hashCode() {
		return Objects.hash(muserId, muserName, muserPassword, muserNick, muserUnreadMsgCount, getHeader());
	}

	@Override
	public String toString() {
		return "EMUser [MUserId=" + muserId + ", MUserName=" + muserName
				+ ", MUserPassword=" + muserPassword + ", MUserNick="
				+ muserNick + ", MUserUnreadMsgCount=" + muserUnreadMsgCount + ", header="+header
				+ "]";
	}
	

}