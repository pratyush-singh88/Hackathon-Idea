package com.bangalorewest.bagtracker.dto;

public class BlockChainLoginResponse {
	private Data data;

	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClassPojo [data = " + data + ", status = " + status + "]";
	}

	/**
	 * @return the data
	 */
	public final Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public final void setData(Data data) {
		this.data = data;
	}

}
