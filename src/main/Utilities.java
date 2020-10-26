package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities class containing common methods like getting a number from console
 * within a certain range, printing a string etc.
 *
 * @author Andrej Gorochov D00218937
 */
public class Utilities
{

    /**
     * Gets user input from console with the specified query, making sure input
     * is not null.
     *
     * @param query the query prompt asking user for input.
     * @return non empty/null string gotten from user.
     */
    public static String getStringNotEmpty(String query)
    {
        Scanner input = new Scanner(System.in);
        System.out.print(query);
        String userString = input.nextLine().trim();
        while (userString.isEmpty())
        {
            System.out.println("Incorrect input. String cannot be empty. Try again.");
            System.out.print(query);
            userString = input.nextLine().trim();
        }
        return userString;
    }

    /**
     * Gets user input from console with the specified query, making sure input
     * is not null and it's length is not less than the specified length. Throws
     * an IllegalArgumentException if specified string length is 0 or less than
     * that.
     *
     * @param query the query prompt asking user for input.
     * @param minStringLength the minimum string length to be gotten from user.
     * @return non empty/null string of at least the specified size from user
     * from console.
     */
    public static String getStringNotEmpty(String query, int minStringLength)
    {
        if (minStringLength < 1)
        {
            throw new IllegalArgumentException("Minimum string length cannot be 0 or less!");
        }
        Scanner input = new Scanner(System.in);
        System.out.print(query);
        String userString = input.nextLine().trim();
        while (userString.length() < minStringLength)
        {
            System.out.println("Incorrect input. String cannot be less than " + minStringLength + " characters long. Try again.");
            System.out.print(query);
            userString = input.nextLine().trim();
        }
        return userString;
    }

    /**
     * Gets user input from console with the specified query, making sure input
     * is not null and it's length is between the specified lengths. Throws an
     * IllegalArgumentException if specified string length is 0 or less than
     * that. It as well throws an IllegalArgumentException if the min length is
     * greater than max length.
     *
     * @param query the query prompt asking user for input.
     * @param minStringLength the minimum string length to be gotten from user.
     * @param maxStringLength the maximum string length to be gotten from user.
     * @return non empty/null string of at least the specified size and at most
     * the specified size from user from console.
     */
    public static String getStringNotEmpty(String query, int minStringLength, int maxStringLength)
    {
        if (minStringLength < 1 || maxStringLength < 1)
        {
            throw new IllegalArgumentException("Max/Min String length cannot be 0 or less!");
        }
        if (minStringLength > maxStringLength)
        {
            throw new IllegalArgumentException("Min string length can't be greater than max string length.");
        }
        Scanner input = new Scanner(System.in);
        System.out.print(query);
        String userString = input.nextLine().trim();
        while (userString.length() < minStringLength || userString.length() > maxStringLength)
        {
            System.out.println("Incorrect input. Input cannot be less than " + minStringLength + " characters or greater than " + maxStringLength + " characters long. Try again.");
            System.out.print(query);
            userString = input.nextLine().trim();
        }
        return userString;
    }

    /**
     * Prints available menu options bordered by the specified string of a
     * certain length.
     *
     * @param borderString String combination of the menu border
     * @param borderLength amount of times the border string should be repeated
     * in the border
     * @param menuOptions the inside of the menu
     */
    public static void printMenu(String borderString, int borderLength, String menuOptions)
    {
        printString(borderString, borderLength);
        System.out.println(menuOptions);
        printString(borderString, borderLength);
        System.out.println("");
    }

    /**
     * Prints available menu options with a menu title bordered by the specified
     * string of a certain length.
     *
     * @param borderString String combination of the menu border
     * @param borderLength amount of times the border string should be repeated
     * in the border
     * @param menuOptions the inside of the menu
     * @param menuTitle the title of the menu displayed before options
     */
    public static void printMenu(String borderString, int borderLength, String menuOptions, String menuTitle)
    {
        printString(borderString, borderLength);
        System.out.println("\n" + menuTitle);
        printString(borderString, borderLength);
        System.out.println(menuOptions);
        printString(borderString, borderLength);
        System.out.println("");
    }

    /**
     * Prints a specified string the specified amount of times.
     *
     * @param s string to be printed
     * @param amount amount of times for the string to be repeated.
     */
    public static void printString(String s, int amount)
    {
        for (int i = 0; i < amount; ++i)
        {
            System.out.print(s);
        }
    }

    /**
     * Queries user for integer input within specified range. Catches
     * InputMismatchException in case user enters a string for the integer and
     * continues querying until answer within range provided. If max value is
     * greater than min value it throws a RunTimeException.
     *
     * @param query String query that will be asked of user.
     * @param min minimum value of range (included).
     * @param max maximum value of range (included).
     * @throws IllegalArgumentException if min is greater than max.
     * @return integer value from console within specified range.
     */
    public static int getInt(String query, int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Min is greater than max.");
        }
        else
        {
            Scanner input = new Scanner(System.in);
            //set to min to please the IDE due to errors saying it might not 
            //be initiliazed even though there is no way it wouldn't (unless 
            //an error or other exceptions occur).
            int num = min;
            boolean isCorrect;
            do
            {
                try
                {
                    System.out.print(query);
                    num = input.nextInt();
                    isCorrect = num >= min && num <= max;
                    if (!isCorrect)
                    {
                        System.out.println("Incorrect input. Try again. Input Range[" + min + "," + max + "]");
                    }
                }
                catch (InputMismatchException e)
                {
                    input.nextLine();
                    isCorrect = false;
                    System.out.println("Please use integer numbers.");
                }
            } while (!isCorrect);
            input.nextLine();
            return num;
        }
    }

    /**
     * Queries user for double number input within specified range. Catches
     * InputMismatchException in case user enters a string for the double and
     * continues querying until answer within range provided. If max value is
     * greater than min value it throws a RunTimeException.
     *
     * @param query String query that will be asked of user.
     * @param min minimum value of range (included).
     * @param max maximum value of range (included).
     * @throws IllegalArgumentException if min is greater than max.
     * @return double value from console within specified range.
     */
    public static double getDouble(String query, double min, double max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Min is greater than max.");
        }
        else
        {
            Scanner input = new Scanner(System.in);
            //set to min to please the IDE due to errors saying it might not 
            //be initiliazed even though there is no way it wouldn't (unless 
            //an error or other exceptions occur).
            double num = min;
            boolean isCorrect;
            do
            {
                try
                {
                    System.out.print(query);
                    num = input.nextDouble();
                    isCorrect = num >= min && num <= max;
                    if (!isCorrect)
                    {
                        System.out.println("Incorrect input. Try again. Input Range[" + min + "," + max + "]");
                    }
                }
                catch (InputMismatchException e)
                {
                    input.nextLine();
                    isCorrect = false;
                    System.out.println("Please use double/float numbers.");
                }
            } while (!isCorrect);
            input.nextLine();
            return num;
        }
    }

    /**
     * Gets either a yes or no answer from user from console.
     *
     * @param query query prompt asking the user for input.
     * @return true if user answered yes, false if user answered no.
     */
    public static boolean getYesNoAnswer(String query)
    {
        String answer = Utilities.getStringNotEmpty(query);
        while (answer.trim().toUpperCase().charAt(0) != 'Y' && answer.trim().toUpperCase().charAt(0) != 'N')
        {
            System.out.println("Unknown answer. Please try again... (Answer can be either Y for yes or N for no)");
            answer = Utilities.getStringNotEmpty(query);
        }
        return answer.trim().toUpperCase().charAt(0) == 'Y';
    }

    /**
     * Cuts the specified string at the cutting point and appends the other
     * specified string to the end of that new cut string. Throws an
     * IllegalArgumentException if string s is null. Throws an
     * IllegalArgumentException if appending string is null. Throws an
     * IllegalArgumentException if the cutting point is negative.
     *
     * @param s string to be cut.
     * @param cuttingPoint point at which string is to be cut.
     * @param appendedString string which is to be appended to the cut string
     * end.
     * @return the cut and appended string is returned.
     */
    public static String cutAndAppendString(String s, int cuttingPoint, String appendedString)
    {
        if (s == null)
        {
            throw new IllegalArgumentException("String s is null");
        }
        if (appendedString == null)
        {
            throw new IllegalArgumentException("String appendedString is null");
        }
        if (cuttingPoint < 0)
        {
            throw new IllegalArgumentException("Cutting point cannot be negative");
        }
        if (cuttingPoint >= s.length())
        {
            return s;
        }
        return s.substring(0, cuttingPoint) + appendedString;
    }

    /**
     * Checks whether a string has a valid email format (regex gotten from
     * https://howtodoinjava.com/regex/java-regex-validate-email-address/).
     *
     * @param email string to be tested for email format.
     * @return true if string matches the default regex format included in the
     * method for an email..
     */
    public static boolean isEmailValid(String email)
    {
        //regex from https://howtodoinjava.com/regex/java-regex-validate-email-address/
        return email.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    /**
     * Checks whether a string has a valid email format.
     *
     * @param phone string to be tested for phone format.
     * @return true if string matches the default regex format included in the
     * method for a phone number.
     */
    public static boolean isPhoneNumberValid(String phone)
    {
        Pattern pattern = Pattern.compile("^\\+?\\d+");
        Matcher matcher = pattern.matcher(phone.trim());
        return matcher.matches();
    }

    //checks the uppercase answer with the arraylist of answers
    /**
     * Gets user char answer and checks if it is included in the specified char
     * array. Keeps querying while user answer isn't part of the 'allowed
     * characters.
     *
     * @param query query prompt asking user for input.
     * @param checkChars allowed character set ArrayList
     * @return the char user answer in uppercase, where the char is a part of
     * the allowed chars list.
     */
    public static char getUserCharAnswerUppercase(String query, ArrayList<Character> checkChars)
    {
        if (checkChars.size() < 1)
        {
            throw new IllegalArgumentException("CheckChars cannot be empty!");
        }
        String answer = Utilities.getStringNotEmpty(query).trim().toUpperCase();
        while (!checkChars.contains(answer.charAt(0)))
        {
            System.out.println("First character not recognized. Please try again.");
            answer = Utilities.getStringNotEmpty(query).trim().toUpperCase();
        }
        return answer.charAt(0);
    }

    /**
     * Gets user char answer and checks if it is included in the specified char
     * array. Keeps querying while user answer isn't part of the 'allowed
     * characters.
     *
     * @param query query prompt asking user for input.
     * @param checkChars allowed character set array
     * @return the char user answer in uppercase, where the char is a part of
     * the allowed chars list.
     */
    public static char getUserCharAnswerUppercase(String query, char[] checkChars)
    {
        ArrayList<Character> chars = new ArrayList<>();
        for (char ch : checkChars)
        {
            chars.add(ch);
        }
        return getUserCharAnswerUppercase(query, chars);
    }

    /**
     * Gets the user answer and checks if it fits the default LocalDateTime
     * format specified. If it doesn't it keeps asking user for input until a
     * valid answer is specified.
     *
     * @param query query prompt asking user for input.
     * @param pattern pattern of required Local Date Time
     * @return the local date time object of whatever the user enters.
     */
    public static LocalDateTime getLocalDateTimeFromUser(String query, String pattern)
    {
        boolean isCorrect;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String userInput = "";
        do
        {
            try
            {
                userInput = getStringNotEmpty(query);

                //isCorrect = userInput.matches("[0-9]{4}-[]");
                LocalDateTime.parse(userInput.trim(), formatter);
//                if (!isCorrect)
//                {
//                    System.out.println("Incorrect input. Try again. Format expected: yyyy-mm-dd hh:MM:ss");
//                }
                isCorrect = true;
            }
            catch (DateTimeParseException e)
            {
                isCorrect = false;
                System.out.println("Incorrect input. Try again. Format expected: " + pattern);
            }
        } while (!isCorrect);
        return LocalDateTime.parse(userInput.trim(), formatter);
    }

    /**
     * Gets the user answer and checks if it fits the default LocalDateTime
     * format specified. If it doesn't it keeps asking user for input until a
     * valid answer is specified.
     *
     * @param query query prompt asking user for input.
     * @param formatterP date time formatter for the required local date time
     * @return the local date time object of whatever the user enters.
     */
    public static LocalDateTime getLocalDateTimeFromUser(String query, DateTimeFormatter formatterP)
    {
        boolean isCorrect;
        DateTimeFormatter formatter = formatterP;
        String userInput = "";
        do
        {
            try
            {
                userInput = getStringNotEmpty(query);

                //isCorrect = userInput.matches("[0-9]{4}-[]");
                LocalDateTime.parse(userInput.trim(), formatter);
//                if (!isCorrect)
//                {
//                    System.out.println("Incorrect input. Try again. Format expected: yyyy-mm-dd hh:MM:ss");
//                }
                isCorrect = true;
            }
            catch (DateTimeParseException e)
            {
                isCorrect = false;
                System.out.println("Incorrect input. Try again.");
            }
        } while (!isCorrect);
        return LocalDateTime.parse(userInput.trim(), formatter);
    }

    /**
     * Gets the the date and time from user and checks if it fits a given format
     * for LocalDateTime. If it doesn't it keeps asking user for input until a
     * valid answer is specified. It allows for a user to only specify a date
     * without the time.
     *
     * Pattern used "yyyy[':']['/']['.']['-']MM[':']['/']['.']['-']dd[['T'][
     * ]HH:mm:ss]".
     *
     * @param query query prompt asking user for input.
     * @return the local date time object of whatever the user enters.
     */
    public static LocalDateTime getLocalDateTimeFromUser(String query)
    {
        boolean isCorrect;
        String pattern = "yyyy[':']['/']['.']['-']MM[':']['/']['.']['-']dd[ HH:mm:ss]";
        //source where use found https://stackoverflow.com/a/49324132
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        String userInput = "";
        do
        {
            try
            {
                userInput = getStringNotEmpty(query);

                //isCorrect = userInput.matches("[0-9]{4}-[]");
                LocalDateTime.parse(userInput, formatter);
//                if (!isCorrect)
//                {
//                    System.out.println("Incorrect input. Try again. Format expected: yyyy-mm-dd hh:MM:ss");
//                }
                isCorrect = true;
            }
            catch (DateTimeParseException e)
            {
                isCorrect = false;
                if (e.getMessage().contains("could not be parsed: Invalid value for"))
                {
                    System.out.println("One or more of the entered values is invalid e.g the month cannot be 13, the hour cannot be 33. ");
                }
                System.out.println("Incorrect input. Try again. Format expected: yyyy-MM-dd HH:mm:ss");
            }
        } while (!isCorrect);
        return LocalDateTime.parse(userInput, formatter);
    }

    /**
     * Prompts user for input (enter) to continue execution.
     */
    public static void promptEnterToContinue()
    {
        System.out.println("\nPress the ENTER key to continue\n");
        new Scanner(System.in).nextLine();
    }
}
