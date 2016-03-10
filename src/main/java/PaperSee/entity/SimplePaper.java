package entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SimplePaper implements Paper {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		// System.out.println("IN HASH CODE: " + result);
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		// System.out.println("IN EQUALS: ");
		// System.out.println(">>>  this : " + this);
		// System.out.println(">>>  obj  : " +obj);

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj.hashCode() != this.hashCode()) {
			return false;
		}

		if (!(obj instanceof SimplePaper)) {
			return false;
		}

		SimplePaper other = (SimplePaper) obj;
		if (this.id != other.id) {
			return false;
		}

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}

		// System.out.println("TRUE");
		return true;
	}

	/**
	 * 
	 */
	private static transient final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", hash=" + hashCode() + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	public SimplePaper(int id, String name) {
		// System.out.println(">>>  FULL PAPER CONSTRUCTOR");
		this.id = id;
		this.name = name;
	}

	/**
	 * empty constructor only to correct work serializable
	 */
	public SimplePaper() {
		// System.out.println(">>>  EMPTY PAPER CONSTRUCTOR");
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.write(getId());
		out.writeObject(getName());

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		setId(in.read());
		setName((String) in.readObject());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
