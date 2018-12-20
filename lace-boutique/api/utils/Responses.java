package api.utils;

public enum Responses {
	
	MISSING_EMAIL_OR_PASSWORD("Both your email and password are required to login."),
	INVALID_EMAIL("The email address you provided is invalid."),
	INVALID_PASSWORD("The password you provided is invalid."),
	INVAID_EMAIL_OR_PASSWORD("The email address or password you provided is invaid."),
	FAILURE_CREATING_ACCOUNT("We couldn't create your account at this moment in time, please try again later."),
	EMAILS_DO_NOT_MATCH("The emails you provided do not match."),
	INSECURE_PASSWORD("The password you selected is not secure enough."),
	PASSWORDS_DO_NOT_MATCH("The passwords you provided do not match."),
	
	
	;
	
	
	private String response;
	
	Responses(String response) {
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}

}
