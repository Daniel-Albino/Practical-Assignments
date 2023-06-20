package pt.isec.tp_pd.utils;

import java.util.Scanner;

public final class PAInput {
    private PAInput() {}

    private static Scanner sc;

    static {
        resetScanner();
    }

    public static void resetScanner() {
        sc = new Scanner(System.in);
    }

    public static String readString(String titulo) {
        String value;
        do {
            if (titulo != null)
                System.out.print(titulo);
            value = sc.nextLine().trim();
        } while (value.isBlank());
        return value;
    }

    public static int readInt(String titulo) {
        while (true) {
            if (titulo != null)
                System.out.print(titulo);
            if (sc.hasNextInt()) {
                int intValue = sc.nextInt();
                sc.nextLine();
                return intValue;
            } else
                sc.nextLine();
        }
    }

    public static long readLong(String titulo) {
        while (true) {
            if (titulo != null)
                System.out.print(titulo);
            if (sc.hasNextInt()) {
                long longValue = sc.nextInt();
                sc.nextLine();
                return longValue;
            } else
                sc.nextLine();
        }
    }

    public static double readDouble(String titulo) {
        while (true) {
            if (titulo != null)
                System.out.print(titulo);
            if (sc.hasNextDouble()) {
                double doubleValue = sc.nextDouble();
                sc.nextLine();
                return doubleValue;
            } else
                sc.nextLine();
        }
    }



    public static boolean readBoolean(String titulo) {
        while (true) {
            if (titulo != null)
                System.out.print(titulo);
            if (sc.hasNextDouble()) {
                boolean booleanValue = sc.nextBoolean();
                sc.nextLine();
                return booleanValue;
            } else
                sc.nextLine();
        }
    }

    public static int chooseOption(String titulo, String ... opcoes) {
        int op = -1;
        do {
            if (titulo != null)
                System.out.println(System.lineSeparator()+titulo);
            System.out.println();
            for(int i = 0; i < opcoes.length; i++) {
                System.out.printf("%3d - %s\n",i+1,opcoes[i]);
            }
            System.out.print("\nOpção: ");
            if (sc.hasNextInt())
                op = sc.nextInt();
            sc.nextLine();
        } while (op < 1 || op > opcoes.length);
        return op;
    }

}

