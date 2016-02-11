package entity;

import java.io.Externalizable;

public interface Paper  extends Externalizable {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

}
