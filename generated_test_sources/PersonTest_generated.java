

public class PersonTest_generated {
    @org.junit.BeforeClass
    public static void executedBeforeAllTests() {
        java.lang.System.out.println("No test executed yet");
    }

    @org.junit.After
    public void executedAfterEachMethod() {
        java.lang.System.out.println("Test finished");
    }

    @org.junit.AfterClass
    public static void executedAtTheEnd() {
        java.lang.System.out.println("All tests executed");
    }

    @org.junit.Test
    public void testAge() {
        classToTest.Person p = new classToTest.Person("Someone", 13);
        org.junit.Assert.assertFalse(p.isAdult());
        org.junit.Assert.assertEquals(13, p.getAge());
        org.junit.Assert.assertEquals("Someone", p.getName());
        org.junit.Assert.assertEquals(false, p.isAdult());
    }
}

