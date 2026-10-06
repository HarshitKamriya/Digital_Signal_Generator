import data.DataType;
import data.DigitalDataGenerator;

public class Main {
    public static void main(String[] args) {
        // testing digital data generator
        DigitalDataGenerator generator = new DigitalDataGenerator();

        String randomData = generator.generate(12,DataType.RANDOM);

        String fixedfourData = generator.generate(12, DataType.FOUR_ZEROS);
        String fixedeightData = generator.generate(12, DataType.EIGHT_ZEROS);

        System.out.println("Random Data: ");
        System.out.println(randomData);

        System.out.println();

        System.out.println("Data with 4 consecutive zero : ");
        System.out.println(fixedfourData);
        System.out.println();

        System.out.println("Data with 8 consecutive zero : ");
        System.out.println(fixedeightData);
    }
}
