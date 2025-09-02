package com.devops.mav;

/**
 * Simple DevOps Maven Application for Jenkins Pipeline Demo
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🚀 DevOps Maven App - Jenkins Pipeline Demo");
        System.out.println("✅ Application compiled successfully!");
        System.out.println("✅ Running on Java: " + System.getProperty("java.version"));
        System.out.println("✅ Build completed successfully!");
        
        // Simple functionality demo
        Calculator calc = new Calculator();
        System.out.println("📊 Calculator Demo: 5 + 3 = " + calc.add(5, 3));
        System.out.println("📊 Calculator Demo: 10 * 2 = " + calc.multiply(10, 2));
        
        System.out.println("🎉 DevOps Pipeline Objective Complete!");
    }
    
    public static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
        
        public int multiply(int a, int b) {
            return a * b;
        }
    }
}