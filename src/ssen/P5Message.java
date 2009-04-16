package ssen;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author nvdesai
 *
 */
@SuppressWarnings("serial")
public class P5Message implements Serializable {
	
	/*
	 * name of the message
	 */
	private String name;
	
	/*
	 * message parameters [name -> values]
	 */
	private HashMap<String, String> params = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}
	
	public String getParam(String paramName) {
		return params.get(paramName);
	}
	
	public void setParam(String paramName, String value) {
		params.put(paramName, value);
	}

}
