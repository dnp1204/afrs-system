package AFRS;

import java.util.ArrayList;
import java.util.Scanner;

public class RequestView {

    private static final String PROMPT = "> ";
    private static Scanner scan;
    private static ArrayList<String> response;
    private static String input;
    private static RequestController parser;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        parser = new RequestController();
        System.out.println("WELCOME TO THE AIRLINE FLIGHT RESERVATION SYSTEM");
        System.out.println("PLEASE INPUT A REQUEST FOLLOWED BY A SEMICOLON");
        do {
            input = "";
            do {
                System.out.print(PROMPT);
                input += scan.nextLine();

                if(!input.substring(input.length()-1).equals(";")) {
                    System.out.println("partial-request");
                }
            } while(!input.substring(input.length()-1).equals(";"));

            response = parser.parse(input);

            if (response.get(0).equals("quit")) {
                System.exit(0);
            }
            printResponse(response);
        } while(!response.get(0).equals("quit"));
    }

    private static void printResponse(ArrayList<String> response) {
        for (String str : response) {
            System.out.println(str);
        }
    }

}
