package streams;

/**
 * Created by Bely Oleg on 18.11.2018.
 */
public class People {
    public enum Sex {
        MAN,
        WOMEN
    }

    private final String name;
    private final Integer age;
    private final Sex sex;

    public People(String name, Integer age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof People)) return false;

        People people = (People) o;

        if (getName() != null ? !getName().equals(people.getName()) : people.getName() != null) return false;
        if (getAge() != null ? !getAge().equals(people.getAge()) : people.getAge() != null) return false;
        return getSex() == people.getSex();
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getAge() != null ? getAge().hashCode() : 0);
        result = 31 * result + (getSex() != null ? getSex().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
