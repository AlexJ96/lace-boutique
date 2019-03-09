package api.utils;

public enum Responses {
	
	MISSING_EMAIL_OR_PASSWORD("Both your email and password are required to login."),
	INVALID_EMAIL("The email address you provided is invalid."),
	INVALID_PASSWORD("The password you provided is invalid."),
	INVAID_EMAIL_OR_PASSWORD("The email address or password you provided is invalid."),
	FAILURE_CREATING_ACCOUNT("We couldn't create your account at this moment in time, please try again later."),
	EMAILS_DO_NOT_MATCH("The emails you provided do not match."),
	INSECURE_PASSWORD("The password you selected is not secure enough."),
	PASSWORDS_DO_NOT_MATCH("The passwords you provided do not match."),
	ACCOUNT_ALREADY_EXISTS("There is already an account for that email address, if you have forgotten your password please request a reset."),
	ACCOUNT_CREATED("Account successfully created!"),
	FAILED_TO_SAVE_ACCOUNT("Oops we couldn't update your account, please try again.")
	
	;
	
	
	private String response;
	
	Responses(String response) {
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}

}
