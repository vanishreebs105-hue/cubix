package cubix_backend.model;

public class CubeStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Cubix: CubeState Demo ===\n");

        CubeState cube = new CubeState();
        System.out.println("Is solved? " + cube.isSolved());
        System.out.println("\nSolved cube layout:");
        cube.print();

        System.out.println("-- Scrambling one sticker --");
        cube.setSticker(0, (byte) 1);
        System.out.println("Is solved now? " + cube.isSolved());
    }
}