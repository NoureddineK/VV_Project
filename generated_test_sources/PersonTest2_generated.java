

public class PersonTest2_generated {
    @org.junit.Test
    public void testAge() {
        classToTest.Person p = new classToTest.Person("Someone", 13);
        classToTest2.Person2 p2 = new classToTest2.Person2(p, 13);
        org.junit.Assert.assertEquals(13, p.getAge());
        org.junit.Assert.assertEquals("Someone", p.getName());
        org.junit.Assert.assertEquals(false, p.isAdult());
        org.junit.Assert.assertEquals(13, p2.getAge());
        org.junit.Assert.assertEquals(null, p2.getName());
        org.junit.Assert.assertEquals(classToTest.Person@415ef4d8, p2.getP());
        org.junit.Assert.assertEquals(false, p2.isAdult());
    }
}

