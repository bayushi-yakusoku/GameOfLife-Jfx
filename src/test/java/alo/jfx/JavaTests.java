package alo.jfx;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class JavaTests {
    Energy energy = new Energy();
    Vegetable vegetable = new Vegetable();
    Bamboo bamboo = new Bamboo();

    @Test
    public void test() {
        System.out.println("test");

        bamboo.setName("bb");
        System.out.println(bamboo.toString());

        vegetable = new Bamboo();

        List<Vegetable> listeCourse = new ArrayList<>();

        testList(listeCourse);

        for (Vegetable veg :
                listeCourse) {
            System.out.println(veg);
        }
    }

    private void testList(List<? super Vegetable> legumes) {
        Vegetable concombre = new Vegetable();
        concombre.setName("Concombre");

        legumes.add(concombre);
    }

}

class Energy {
    int quantity;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append("Name=" + name);
        output.append(", Quantity=" + quantity);
        return output.toString();
    }
}

class Vegetable extends Energy {
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(super.toString());
        output.append(", Color=" + color);
        return output.toString();
    }
}

class Bamboo extends Vegetable {
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(super.toString());
        output.append(", Size=" + size);
        return output.toString();
    }
}
