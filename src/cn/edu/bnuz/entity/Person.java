package cn.edu.bnuz.entity;

/**
 * 实体类
 */
public class Person implements Cloneable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private boolean selected;

    public Person() {
    }

    public Person(Person person) {
        this.id = person.id;
        this.name = person.name;
        this.phone = person.phone;
        this.email = person.email;
        this.selected = person.selected;
    }

    public Person(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

    @Override
    public String toString() {
        return id+","+name+","+phone+","+email+"\n";
    }
}
