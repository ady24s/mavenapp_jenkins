package com.devops.mav;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple tests for Jenkins Pipeline Demo
 */
public class MainTest {
    
    @Test
    void testCalculatorAdd() {
        Main.Calculator calc = new Main.Calculator();
        int result = calc.add(5, 3);
        assertEquals(8, result, "5 + 3 should equal 8");
        System.out.println("✅ Addition test passed!");
    }
    
    @Test
    void testCalculatorMultiply() {
        Main.Calculator calc = new Main.Calculator();
        int result = calc.multiply(10, 2);
        assertEquals(20, result, "10 * 2 should equal 20");
        System.out.println("✅ Multiplication test passed!");
    }
    
    @Test
    void testMainMethod() {
        // Test that main method runs without errors
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Main method should run without exceptions");
        System.out.println("✅ Main method test passed!");
    }
}