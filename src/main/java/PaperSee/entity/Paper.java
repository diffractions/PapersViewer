package entity;

import java.io.Externalizable;
import java.util.Map;
import dao.exceptions.DaoSystemException; 

public interface Paper extends Externalizable, Cloneable {

	public String getId();

	public void setId(String id);

	public String getName();

	public void setName(String name);

	public Object clone() throws CloneNotSupportedException;

	 public Map<String, String> getProporties() throws DaoSystemException;
	 public void setProporties(Map<String, String> proporties) throws DaoSystemException;

}
