package Software_Testing.VV_Project;


class Person {
    private java.lang.String name;

    private int age;

    public Person(java.lang.String name, int age) {
        this.name = name;
        this.age = age;
    }

    public java.lang.String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isAdult() {
        return (age) >= 18;
    }
}

