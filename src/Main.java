import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc_input = new Scanner(System.in);
        System.out.print("Введите выражение : ");
        String input = sc_input.nextLine();
        sc_input.close();
        System.out.println("Ответ : " + calc(input));
    }
    public static String calc(String input) throws Exception {
        int value_1, value_2;
        int result = 0;
        boolean isArab;
        String[] actions = {"+","-","/","*"};
        String[] regexActions = {"\\+","-","/","\\*"};
        String output;

        int actionIndex = -1;
        int actionCounter = 0;
        for (int i = 0; i < actions.length; i++) {
            if (input.contains(actions[i])) {       // Поиск оператора
                actionIndex = i;
            }
        }

        if (actionIndex == -1) {                    // Операторов нет
            throw new Exception("Строка не является математической операцией");
        }

        for (int i = 0 ; i < input.length() ; i++) {  // Подсчет количества операторов
            switch (input.charAt(i)) {
                case '+', '-', '*', '/' -> actionCounter++;
            }
        }

        if (actionCounter > 1) {                    // Операторов больше 1го
            throw new Exception("Больше одного оператора");
        }

        String[] values = input.split(regexActions[actionIndex]); //Разбивка строки на операнды

        Scanner sc = new Scanner(values[0]);                      // Считываем первую операнду
        if (sc.hasNextInt()) {                                    // Первая операнда - арабское число
            value_1 = sc.nextInt();
            isArab = true;
        } else {                                                  // Первая операнда - римское число
            value_1 = Converter.translateToArab(sc.next());
            isArab = false;
        }

        sc = new Scanner(values[1]);                    // Считываем вторую операнду
        if (isArab && sc.hasNextInt()) {                // Вторая и первая операнды - арабские числа
            value_2 = sc.nextInt();
        } else if (!isArab && !sc.hasNextInt()) {       // Вторая и первая операнды - римские числа
            value_2 = Converter.translateToArab(sc.next());
        } else {
            throw new Exception("Разные системы счисления");
        }

        if (value_1 < 1 || value_2 < 1 || value_1 > 10 || value_2 > 10) {       // Проверка : введено число от 1 до 10
            throw new Exception("Операнда не соответствует заданию");
        }

        switch (actions[actionIndex]) {                 // Выполнение математических операций
            case "+" -> result = value_1 + value_2;
            case "-" -> result = value_1 - value_2;
            case "*" -> result = value_1 * value_2;
            case "/" -> result = value_1 / value_2;
        }

        if (isArab) {
            output = Integer.toString(result);
        } else {
            if (result < 1) {
                throw new Exception("В римской системе нет отрицательных чисел");
            } else {
                output = Converter.translateToRoman(result);
            }
        }
        return output;
    }
}

class Converter {       //Конвентер арабских и римских чисел
    static String translateToRoman(int number) {
        int[] roman_value_list = new int[]{100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] roman_char_list = new String[]{"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < roman_value_list.length; i += 1) {
            while (number >= roman_value_list[i]){
                number -= roman_value_list[i];
                res.append(roman_char_list[i]);
            }
        }
        return res.toString();
    }
    static int translateToArab(String roman_numeral) throws Exception {
        Map<Character, Integer> roman_char_dict = new HashMap<>();
        roman_char_dict.put('I', 1);
        roman_char_dict.put('V', 5);
        roman_char_dict.put('X', 10);
        int res = 0;
        for (int i = 0; i < roman_numeral.length(); i += 1) {
            try {
                if (i == 0 || roman_char_dict.get(roman_numeral.charAt(i)) <= roman_char_dict.get(roman_numeral.charAt(i - 1)))
                    res += roman_char_dict.get(roman_numeral.charAt(i));
                else
                    res += roman_char_dict.get(roman_numeral.charAt(i)) - 2 * roman_char_dict.get(roman_numeral.charAt(i - 1));
            } catch (NullPointerException e) {
                throw new Exception("Неверное римское число");
            }
        }
        return res;
    }
}