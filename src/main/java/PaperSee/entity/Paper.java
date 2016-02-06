package entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Paper implements Externalizable {
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return " [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paper other = (Paper) obj;
		if (id != other.id)
			return false;
		return true;
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
	public Paper(int id, String name) {
		 System.out.println(">>>  FULL PAPER CONSTRUCTOR");
		this.id = id;
		this.name = name;
	}

	/**
	 * empty constructor only to correct work serializable
	 */
	public Paper() {
		 System.out.println(">>>  EMPTY PAPER CONSTRUCTOR");
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.write(getId());

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		setId(in.read());

	}

}
