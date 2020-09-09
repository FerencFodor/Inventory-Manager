package com.fodor;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Inventory inventory = new Inventory();
    private static DataParser parser = new DataParser();
    private static boolean didQuit;
    private static int idCounter = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        File file = new File("Data.txt");
        didQuit = false;

        //Read File

        try {
            if(!file.createNewFile()){
                if(file.length() > 0){
                    inventory = new Inventory(parser.JSONToMap(FileReaderConcat(file)));
                }
            } else {
                System.out.println("Data.txt does not exist. Creating new one...");
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        //Loop

        do {
            if(inventory.GetProductCount() <= 0) {
                System.out.println("Inventory is empty. Please add and Item");
            }

            System.out.print("> ");

            String output;
            if((output = parseCommand(input.nextLine())) == null) {
                System.out.println("Unknown command");
                continue;
            }
            System.out.flush();

            System.out.println(output);
        } while (!didQuit);

        //Write File

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(parser.MapToJSON(inventory.getInventory()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String FileReaderConcat(File file) throws IOException {
        String fileContent = "";
        FileReader reader = new FileReader(file);
        int i;
        while((i = reader.read())!= -1){
            fileContent += (char)i;
        }
        reader.close();
        return fileContent;
    }

    public static String parseCommand(String cmd){
        String command = cmd.contains(" ") ? cmd.substring(0, cmd.indexOf(" ")) : cmd;

        if ("QUIT".equals(command.toUpperCase())) {
            didQuit = true;
            return "Closing program...";
        } else if ("ADD".equals(command.toUpperCase())) {
            return AddProductToInv(cmd);
        } else if ("REMOVE".equals(command.toUpperCase())) {
            int id = Integer.parseInt(cmd.substring(cmd.indexOf(" ") + 1));
            inventory.RemoveProduct(id);
            return String.format(" Product removed, ID:%d", id);
        } else if ("SHOW".equals(command.toUpperCase())) {
            return inventory.PrintList();
        } else if ("TOTAL".equals(command.toUpperCase())) {
            return String.format("Total value: %.2f", inventory.CalculateTotalValue());
        } else if ("EDIT".equals(command.toUpperCase())) {
            return EditProductInInv(cmd);
        } else if ("HELP".equals(command.toUpperCase())) {//region help
            return "ADD [NAME ID PRICE QUANTITY]\n" +
                    "\tAdds product to inventory.\n" +
                    "\t(if ID is -1, it will assign the value of ID Counter)\n" +

                    "REMOVE [ID]\n" +
                    "\tRemoves product from inventory.\n" +

                    "SHOW\n" +
                    "\tShow the content of inventory.\n" +

                    "TOTAL\n" +
                    "\tPrints the total value of inventory.\n" +

                    "EDIT [NAME|PRICE|QUANTITY ID VALUE]\n" +
                    "\tEdits the products name/price/quantity\n" +

                    "HELP\n" +
                    "\t Prints out command list";
            //endregion
        }
        return null;
    }

    private static String EditProductInInv(String cmd) {
        String[] arguments = cmd.split(" ");
        if(arguments[1].equalsIgnoreCase("name")){
            inventory.EditName(Integer.parseInt(arguments[2]), arguments[3]);
        } else if(arguments[1].equalsIgnoreCase("price")) {
            inventory.EditPrice(Integer.parseInt(arguments[2]), Float.parseFloat(arguments[3]));
        } else if(arguments[1].equalsIgnoreCase("quantity")) {
            inventory.EditQuantity(Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]));
        } else {
            return "The value you want to change does not exists or cannot be edited(ID)";
        }
        return  "Product edited.";
    }

    private static String AddProductToInv(String cmd) {
        String[] args = cmd.split(" ");
        args = Arrays.copyOfRange(args,1, args.length);
        StringBuilder name = new StringBuilder();
        for  (int i = 0;i < args.length-3; i++){
            name.append(args[i]).append(" ");
        }
        name.delete(name.lastIndexOf(" "),name.lastIndexOf(" "));

        int id;
        try {
            id = Integer.parseInt(args[args.length-3]);
        } catch (NumberFormatException nfe){
            return "Error: Invalid arguments!";
        }

        id = id<0 ? idCounter++:id;
        float price = Float.parseFloat(args[args.length-2]);
        int quantity = Integer.parseInt(args[args.length-1]);

        inventory.AddProduct(name.toString(), id, price, quantity);
        return String.format("Product added:\n" +
                        "\tID: %d\n" +
                        "\tNAME: %s\n" +
                        "\tPRICE: €%.2f\n" +
                        "\tQUANTITY: %d",
                id, name.toString(), price, quantity);
    }
}
