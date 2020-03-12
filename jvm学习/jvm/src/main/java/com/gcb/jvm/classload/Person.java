package com.gcb.jvm.classload;

public class Person implements Cloneable{

    private Person person;

//    private classloadTest1 c;

    public void setPerson(Object object) {
        this.person = (Person) object;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person1 = new Person();
        person1.setPerson(new Person());
        Object clone = person1.clone();
        System.out.println(clone);
        if (clone == person1){
            System.out.println(1);
        }
    }


}
