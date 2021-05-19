package com.axis.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class CustomerAddress {

	@DynamoDBAttribute
	private String custAddrId;
	@DynamoDBAttribute
	private String recepientName;
	@DynamoDBAttribute
	private String pinCode;
	@DynamoDBAttribute
	private String address;

	@DynamoDBAttribute
	private String recepientPhoneNumber;

	public CustomerAddress() {
		super();
	}

	public CustomerAddress(String userId, String custAddrId, String recepientName, String pinCode, String address) {
		super();
		this.custAddrId = custAddrId;
		this.recepientName = recepientName;
		this.pinCode = pinCode;
		this.address = address;
	}

	public String getCustAddrId() {
		return custAddrId;
	}

	public void setCustAddrId(String custAddrId) {
		this.custAddrId = custAddrId;
	}

	public String getRecepientName() {
		return recepientName;
	}

	public void setRecepientName(String recepientName) {
		this.recepientName = recepientName;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRecepientPhoneNumber() {
		return recepientPhoneNumber;
	}

	public void setRecepientPhoneNumber(String recepientPhoneNumber) {
		this.recepientPhoneNumber = recepientPhoneNumber;
	}

	@Override
	public String toString() {
		return "CustomerAddress [ custAddrId=" + custAddrId + ", recepientName=" + recepientName + ", pinCode="
				+ pinCode + ", address=" + address + "]";
	}

	@Override
	public boolean equals(Object obj) {
		CustomerAddress other = (CustomerAddress) obj;
		return other.custAddrId.equals(other.getCustAddrId());
	}

}
