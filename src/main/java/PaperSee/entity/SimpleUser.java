package entity;

public class SimpleUser implements User {

	private String name;
	private String login;
	private String password;

	public SimpleUser() {
	}

	@Override
	public void setName(String string) {
		name = string;

	}

	@Override
	public void setLogin(String string) {
		login = string;
	}

	@Override
	public void setPassword(String string) {
		password = string;
	}

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
