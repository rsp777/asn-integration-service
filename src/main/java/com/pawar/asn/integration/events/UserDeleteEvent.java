package com.pawar.asn.integration.events;

public class UserDeleteEvent {
	private Long userId;
	
	public UserDeleteEvent() {
	}
	
	
	public UserDeleteEvent(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "UserDeleteEvent [userId=" + userId + "]";
	}

}
