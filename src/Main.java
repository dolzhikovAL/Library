import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество людей пришедших в библиотеку");
        int peopleCount =/* scanner.nextInt();*/ 20;
        System.out.println("Введите количество мест в библиотеке ");
        int maxAmount = /*scanner.nextInt();*/ 4;
        Library library = new Library(new Semaphore(maxAmount), peopleCount);

        library.startTreads(library);
    }
}
