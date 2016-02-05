package org.csf.pojo;

public class Country extends TableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int key;
	String name;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
