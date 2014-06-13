package de.mytasks.domain;

import java.util.Comparator;

public class TasklistComparator implements Comparator<Tasklist> {
	@Override
	  public int compare(Tasklist t1, Tasklist t2) {
	    if (t1.getName() == null && t2.getName() == null) {
	      return 0;
	    }
	    if (t1.getName() == null) {
	      return 1;
	    }
	    if (t2.getName() == null) {
	      return -1;
	    }
	    return t1.getName().compareTo(t2.getName());
	  }

}
