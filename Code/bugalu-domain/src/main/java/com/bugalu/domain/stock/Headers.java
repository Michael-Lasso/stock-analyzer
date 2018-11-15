package com.bugalu.domain.stock;

public class Headers {
	private String header1;
	private String header2;
	private String header3;
	private String header4;
	private String header5;

	public String getHeader1() {
		return header1;
	}

	public void setHeader1(String header1) {
		this.header1 = header1;
	}

	public String getHeader2() {
		return header2;
	}

	public void setHeader2(String header2) {
		this.header2 = header2;
	}

	public String getHeader3() {
		return header3;
	}

	public void setHeader3(String header3) {
		this.header3 = header3;
	}

	public String getHeader4() {
		return header4;
	}

	public void setHeader4(String header4) {
		this.header4 = header4;
	}

	public String getHeader5() {
		return header5;
	}

	public void setHeader5(String header5) {
		this.header5 = header5;
	}

	public Headers(String header1, String header2, String header3, String header4, String header5) {
		super();
		this.header1 = header1;
		this.header2 = header2;
		this.header3 = header3;
		this.header4 = header4;
		this.header5 = header5;
	}

	public Headers() {
		super();
	}

	@Override
	public String toString() {
		return "Headers [header1=" + header1 + ", header2=" + header2 + ", header3=" + header3 + ", header4=" + header4
				+ ", header5=" + header5 + "]";
	}

}
