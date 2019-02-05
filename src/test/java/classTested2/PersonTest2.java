package classTested2;

import org.junit.Test;

import classToTest.Person;
import classToTest2.Person2;


public class PersonTest2 {

	@Test
	public void testAge() {
		Person p = new Person("Someone", 13);
		Person2 p2 = new Person2(p, 13);
	}
}
