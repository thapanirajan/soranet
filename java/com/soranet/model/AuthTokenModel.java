package com.soranet.model;

import java.time.LocalDateTime;

public class AuthTokenModel {
	private int tokenId;
	private int userId;
	private String token;
	private LocalDateTime expiryDate;
	private LocalDateTime createdAt;

	public AuthTokenModel(int tokenId, int userId, String token, LocalDateTime expiryDate, LocalDateTime createdAt) {
		super();
		this.tokenId = tokenId;
		this.userId = userId;
		this.token = token;
		this.expiryDate = expiryDate;
		this.createdAt = createdAt;
	}

	// Getters and Setters
	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}