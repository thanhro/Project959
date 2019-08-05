package vn.thanhld.server959.response;

public class Response {
	private int code;
	private ResponseData data;
	private String messege;

	public Response() {
		super();
	}

	public Response(int code, ResponseData data, String messege) {
		super();
		this.code = code;
		this.data = data;
		this.messege = messege;
	}

	public Response(int code, String messege) {
		super();
		this.code = code;
		this.messege = messege;
	}

	public Response(int code, ResponseData data) {
		super();
		this.code = code;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}

	public String getMessege() {
		return messege;
	}

	public void setMessege(String messege) {
		this.messege = messege;
	}

}
