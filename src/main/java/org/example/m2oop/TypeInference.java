package org.example.m2oop;

public class TypeInference {
    static void main() {
        // Type inference is detecting type of expression at compile time

        // var keyword (since Java 10)

        var text = "Hello World!"; // after compile -> String text = "Hello World"
        System.out.println(text);

        var price = 12.4D; // after compile -> double price = 12.4;
        System.out.println(price);
        // price = "A"; // we cannot do it
    }
}
