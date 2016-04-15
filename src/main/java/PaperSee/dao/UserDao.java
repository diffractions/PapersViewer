package dao;

import dao.exceptions.DaoSystemException;
import entity.User;

public interface UserDao {
	User selectByLogin(String login) throws DaoSystemException;
}
