package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListHelper {
	
	static public String[] intersect(String[] arrayA, String[] arrayB) {
		List<String> listB = new ArrayList<>(Arrays.asList(arrayB));
	    List<String> listC = new ArrayList<String>();
	    for(String a : arrayA) {
	        if(listB.contains(a) && !listC.contains(a)) {
	            listC.add(a);
	        }
	    }
	    return listC.toArray(new String[0]);
	}

}
