package com.chunarevsa.Website.payload;

import javax.validation.Valid;

import com.chunarevsa.Website.entity.payload.DeviceInfo;

// TODO: для всех реквество сделать валидацию
public class LoginRequest {
	
   private String username;
   private String email;
   private String password;

   @Valid
   private DeviceInfo deviceInfo;

	public LoginRequest() {
	}

	public LoginRequest(String username, String email, String password, DeviceInfo deviceInfo) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.deviceInfo = deviceInfo;
	} 

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DeviceInfo getDeviceInfo() {
		return this.deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	@Override
	public String toString() {
		return "{" +
			" username='" + getUsername() + "'" +
			", email='" + getEmail() + "'" +
			", password='" + getPassword() + "'" +
			", deviceInfo='" + getDeviceInfo() + "'" +
			"}";
	} 


}
