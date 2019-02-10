package Software_Testing.VV_Project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fahim MERZOUK & Noureddine KADRI 
 *
 */
public class Observer {
	public static Map<String, Object> observations = new HashMap<String, Object>();

	public static void observe(String name, Object object) {
		observations.put(name, object);
	}
}
