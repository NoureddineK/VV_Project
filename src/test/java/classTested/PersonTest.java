package classTested;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import classToTest.Person;


public class PersonTest {
	@BeforeClass
	public static void executedBeforeAllTests() {
		System.out.println("No test executed yet");
	}

	@After
	public void executedAfterEachMethod() {
		System.out.println("Test finished");
	}

	@AfterClass
	public static void executedAtTheEnd() {
		System.out.println("All tests executed");
	}

	@Test
	public void testAge() {
		Person p = new Person("Someone", 13);
		assertFalse(p.isAdult());
	}
}
