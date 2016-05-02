package utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.Cookie;

import entity.Paper;
import utility.exception.ReadWriteCodeException;

/**
 * 
 * @author Andy
 * @see http://stackoverflow.com/questions/462077/how-to-generically-specify-a-
 *      serializable-list
 * @param <T>
 */
public class CookiePaperSetUtil<T extends Serializable> {

	private CookiePaperSetUtil() {
	}

	/**
	 * Return object of cookie with wrote BASE64 encode paper set as String
	 * value parameter. Return NULL if can not write paper set in cookie
	 * 
	 * @throws ReadWriteCodeException
	 */
	public static <T extends Collection<? extends Serializable>> String writeSetInString(
			T papers) throws ReadWriteCodeException {

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteArrayOutputStream);) {

			objectOutputStream.writeObject(papers);
			return Base64.getEncoder().encodeToString(
					byteArrayOutputStream.toByteArray());

		} catch (IOException e) {
			throw new ReadWriteCodeException(
					"Failed to write set in Base64 code string", e);
		}

	}

	/**
	 * Return papers set BASE64 decode from cookie String value parameter.
	 * Return NULL if can not read paper set from cookie.
	 * 
	 * @throws ReadWriteCodeException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Collection<? extends Serializable>> T readSetFromString(
			String setString) throws ReadWriteCodeException {

		try (ObjectInputStream objectInputStream = new ObjectInputStream(
				new ByteArrayInputStream(Base64.getDecoder().decode(setString)))) {
			return (T) objectInputStream.readObject();
		} catch (IllegalArgumentException | IOException
				| ClassNotFoundException e) {
			throw new ReadWriteCodeException(
					" Failed to read Set from Base64 code String", e);
		}

	}

	/**
	 * Return set of papers obtain from user saved as cookies in browser. If
	 * cookie not found return NULL.
	 * 
	 * @throws ReadWriteCodeException
	 * 
	 */
	public static Set<Paper> getPapersFromCookie(Cookie[] cookies,
			String COOKIE_NAME) throws ReadWriteCodeException {

		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(COOKIE_NAME)) {
					return CookiePaperSetUtil.readSetFromString(cookie
							.getValue());
				}
		return null;
	}

	/**
	 * Return object of cookie with wrote BASE64 encode paper set as String
	 * value parameter. Return NULL if can not write paper set in cookie
	 * 
	 * @throws ReadWriteCodeException
	 */
	public static Cookie writePapersInCookie(Set<Paper> papers,
			String cookieName, String path) throws ReadWriteCodeException {
		String cookieSetCode = CookiePaperSetUtil.writeSetInString(papers);

		Cookie selected = new Cookie(cookieName, cookieSetCode);
		selected.setPath(path);
		return selected;

	}

}
