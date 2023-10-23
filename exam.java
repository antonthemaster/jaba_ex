import java.io.*;
import java.util.*;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void decreaseQuantity() {
        quantity--;
    }
}

class ToyStore {
    private List<Toy> toys;

    public ToyStore() {
        toys = new ArrayList<>();
    }

    public void addToy(int id, String name, int quantity, double weight) {
        toys.add(new Toy(id, name, quantity, weight));
    }

    public void updateWeight(int toyId, double weight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(weight);
                break;
            }
        }
    }

    public Toy drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        if (totalWeight == 0) return null;

        double randomValue = Math.random() * totalWeight;
        double cumulativeWeight = 0;
        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomValue <= cumulativeWeight) {
                Toy selectedToy = new Toy(toy.getId(), toy.getName(), 1, toy.getWeight());
                toy.decreaseQuantity();
                return selectedToy;
            }
        }

        return null;
    }

    public void saveToyToFile(Toy toy) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("winners.txt", true))) {
            writer.println("ID: " + toy.getId() + ", Name: " + toy.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ToyStoreDemo {
    public static void main(String[] args) {
        ToyStore store = new ToyStore();

        // Добавление игрушек
        store.addToy(1, "Teddy Bear", 10, 30);
        store.addToy(2, "LEGO Set", 5, 20);
        store.addToy(3, "Doll", 15, 50);

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Draw a Toy");
            System.out.println("2. Update Toy Weight");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Toy selectedToy = store.drawToy();
                    if (selectedToy != null) {
                        System.out.println("Congratulations! You won a " + selectedToy.getName());
                        store.saveToyToFile(selectedToy);
                    } else {
                        System.out.println("Sorry, no toys left to draw.");
                    }
                    break;
                case 2:
                    System.out.print("Enter the Toy ID to update weight: ");
                    int toyId = scanner.nextInt();
                    System.out.print("Enter the new weight (in %): ");
                    double weight = scanner.nextDouble();
                    store.updateWeight(toyId, weight);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        } while (choice != 3);
    }
}
