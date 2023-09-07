import java.io.*;

public class Main {
    private static final String[] FIELDS = {"фамилия", "имя", "отчество", "дата рождения (ДД.ММ.ГГГГ)",
            "номер телефона", "пол ('m' / 'f')"};

    public static void main(String[] args) {
        System.out.println("Выберите действие: ");
        System.out.println("1 - Ввести новые данные и сохранить в файл");
        System.out.println("2 - Прочитать данные из файла");

        int choice = getUserChoice();

        if (choice == 1) {
            enterDataAndSaveToFile();
        } else if (choice == 2) {
            readAllDataFromFile();
        } else {
            System.out.println("Некорректный выбор. Попробуйте снова.");
        }
    }
    private static int getUserChoice() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        try {
            choice = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
        return choice;
    }
    private static String getUserInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
        return input;
    }

    private static void enterDataAndSaveToFile() {
        String[] userData = new String[FIELDS.length];

        for (int i = 0; i < FIELDS.length; i++) {
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Введите " + FIELDS[i] + ": ");
                userData[i] = getUserInput();

                try {
                    validateData(FIELDS[i], userData[i]);
                    validInput = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }

        System.out.println("Данные:");
        printUserData(userData);

        writeToFile(userData);
    }

    private static void printUserData(String[] userData) {
        for (int i = 0; i < FIELDS.length; i++) {
            System.out.println(FIELDS[i] + ": " + userData[i]);
        }
    }

    private static void writeToFile(String[] userData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"))) {
            for (int i = 0; i < FIELDS.length; i++) {
                writer.write(FIELDS[i] + ": " + userData[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static void readAllDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static void readSpecificDataFromFile() {
        System.out.println("Выберите параметр для чтения:");
        for (int i = 0; i < FIELDS.length; i++) {
            System.out.println((i + 1) + " - " + FIELDS[i]);
        }

        int fieldChoice = getUserChoice();

        if (fieldChoice < 1 || fieldChoice > FIELDS.length) {
            System.out.println("Некорректный выбор. Попробуйте снова.");
            return;
        }
    }

    private static void validateData(String field, String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " не может быть пустым!");
        }

        if (field.equals("фамилия") || field.equals("имя") || field.equals("отчество")) {
            if (value.matches(".*\\d+.*")) {
                throw new IllegalArgumentException("Поле " + field + " не может содержать числа!");
            }
            if (value.matches(".*\\p{Punct}+.*")) {
                throw new IllegalArgumentException("Поле " + field + " не может содержать знаки пунктуации!");
            }
        }

        if (field.equals("дата рождения (ДД.ММ.ГГГГ)") && !value.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new IllegalArgumentException("Некорректный формат даты рождения!");
        }

        if (field.equals("номер телефона") && !value.matches("\\d+")) {
            throw new IllegalArgumentException("Некорректный формат номера телефона!");
        }

        if (field.equals("пол ('m' / 'f')") && !value.matches("[mf]")) {
            throw new IllegalArgumentException("Некорректный формат пола!");
        }
    }
}