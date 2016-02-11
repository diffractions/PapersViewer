package utility.exception;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import entity.Paper;

public class PaperMarkedHashSet<T extends Paper> extends HashSet<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PaperMarkedHashSet() {
		super();
	}

	/**
	 * @param c
	 */
	public PaperMarkedHashSet(Collection<? extends T> c) {
		super(c);
	}

	@Override
	public boolean add(T e) {
		Iterator<T> iter = iterator();
		// System.out.println(">>>>>>>>>>>" + size());
		// System.out.println(">>>>>>>>>>>" + this);
		// System.out.println(">>>>>>>>>>> e "+e);

		while (iter.hasNext()) {
			Paper next = iter.next();

			boolean b = next.getId() == e.getId()
					&& next.getName().equals(e.getName());
			// System.out.println(">>>>"+ b);
			if (b) {
				return false;
			}
		}
		// System.out.println(">>>>>>>>>> ADD" );
		return super.add(e);
	}



}
