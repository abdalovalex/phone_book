package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common
{
    /**
     * Проверка на id
     *
     * @param value String идентификатор
     * @return boolean
     */
    public static boolean isId(String value)
    {
        Pattern regexp = Pattern.compile("^[1-9][0-9]*$");
        Matcher matched = regexp.matcher(value);

        return matched.find();
    }

    /**
     * Проверка на целое число, может быть отрицательным
     *
     * @param value String проверяемое число
     * @return boolean
     */
    public static boolean isNumeric(String value)
    {
        Pattern regexp = Pattern.compile("^(-{0,1}(?!0)[0-9]+)|0$");
        Matcher matched = regexp.matcher(value);

        return matched.find();
    }

    /**
     * Преобразование строки в число
     *
     * @param value String исходная строка
     * @return int
     */
    public static int stingToInt(String value)
    {
        return Integer.parseInt(value);
    }
}
