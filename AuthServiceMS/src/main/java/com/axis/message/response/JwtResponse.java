package com.axis.message.response;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String username;
	private String userrole;
	private String userId;

	public JwtResponse(String accessToken, String username, String userrole) {
		this.token = accessToken;
		this.username = username;
		this.userrole = userrole;
	}

	public JwtResponse(String accessToken, String username, String userrole, String userId) {
		this.token = accessToken;
		this.username = username;
		this.userrole = userrole;
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}