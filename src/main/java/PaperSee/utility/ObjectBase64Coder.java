package utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Collection;

import utility.exception.ReadWriteCodeException;

/**
 * 
 * @author Andy
 * @see http://stackoverflow.com/questions/462077/how-to-generically-specify-a-
 *      serializable-list
 * @param <T>
 */
public class ObjectBase64Coder<T extends Serializable> {

	private ObjectBase64Coder() {
	}

	/**
	 * Return object of cookie with wrote BASE64 encode paper set as String
	 * value parameter. Return NULL if can not write paper set in cookie
	 * 
	 * @throws ReadWriteCodeException
	 */
	public static <T extends Collection<? extends Serializable>> String writeSetInString(
			T papers) throws ReadWriteCodeException {
		String selected = null;

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteArrayOutputStream);) {
			objectOutputStream.writeObject(papers);
			selected = Base64.getEncoder().encodeToString(
					byteArrayOutputStream.toByteArray());
		} catch (IOException e) {

			e.printStackTrace();
			ReadWriteCodeException n = new ReadWriteCodeException(
					"Failed to write set in Base64 code string");
			n.addSuppressed(e);
			throw n;
		}

		return selected;
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
		T papers = null;

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				Base64.getDecoder().decode(setString));
				ObjectInputStream objectInputStream = new ObjectInputStream(
						byteArrayInputStream);) {

			Object o = objectInputStream.readObject();
			papers = (T) o;
		} catch (Exception e) {
			e.printStackTrace();
			ReadWriteCodeException n = new ReadWriteCodeException(
					" Failed to read Set from Base64 code String");
			n.addSuppressed(e);
			throw n;
		}
		return papers;
	}

}
