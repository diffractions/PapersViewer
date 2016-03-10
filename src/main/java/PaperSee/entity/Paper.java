package entity;

import java.io.Externalizable;

public interface Paper  extends Externalizable, Cloneable {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public Object clone() throws CloneNotSupportedException ;
}
