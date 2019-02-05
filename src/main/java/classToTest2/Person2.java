package classToTest2;

import classToTest.Person;

public class Person2 {
    private String name;
    private int age;
    private Person p;
    
    
    public Person2(Person p, int age) {
    	this.p = p;
        this.name = name;
        this.age = age;
    }

    public Person getP() {
		return p;
	}

	public void setP(Person p) {
		this.p = p;
	}

	public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isAdult() {
        return age >= 18;
    }
}
