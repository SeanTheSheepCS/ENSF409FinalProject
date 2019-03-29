package server.model;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The front end for the tool shop application
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class FrontEnd 
{
    /**
     * The main function for the program, handles all user input.
     * 
     * @param args unused
     */
    public static void main(String[] args)
    {
        superLoop();
    }
    
    /**
     * Displays the menu with choices numbered 1-6
     */
    private static void displayMenu()
    {
        System.out.println("Please enter one of the following numbers to perform a task.");
        System.out.println("1. List all tools");
        System.out.println("2. Search for a tool by tool name");
        System.out.println("3. Search for a tool by toolID");
        System.out.println("4. Check item quantity");
        System.out.println("5. Decrease item quantity");
        System.out.println("6. Quit");
    }
    
    /**
     * The superloop that the program should work through
     */
    private static void superLoop()
    {
        System.out.println("Welcome to the tool shop application.");
        Shop theShop = initializeShop();
        boolean shouldContinue = true;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while(shouldContinue)
        {
            displayMenu();
            int choice = getInput();
            switch(choice)
            {
                case 1: //Print all tools
                    System.out.println(theShop.getInventory());
                    break;
                case 2: //Search for tool by tool name
                    Item selectedTool = searchByName(theShop,stdin);
                    if(selectedTool != null)
                    {
                        System.out.println(selectedTool);
                    }
                    break;
                case 3: //Search for a tool by tool id
                    selectedTool = searchByID(theShop,stdin);
                    if(selectedTool != null)
                    {
                        System.out.println(selectedTool);
                    }
                    break;
                case 4: //Check a tool's quantity
                    selectedTool = searchByName(theShop,stdin);
                    if(selectedTool != null)
                    {
                        System.out.println(selectedTool.getQuantity() + " in stock.\n");
                    }
                    break;
                case 5: //Decrease an item's quantity
                    selectedTool = searchByName(theShop,stdin);
                    buy(theShop, selectedTool, stdin);
                    break;
                case 6: //Quit
                    shouldContinue = false;
                    break;
                case -1: 
                    System.out.println("Please enter a valid number (1-6).");
                    break;
            }
        }
        try
        {
            stdin.close();
        }
        catch(Exception e)
        {
            System.out.println("Some error occured when closing the input stream!");
        }
        System.out.println("\n\nSee you later.\n\n");
    }
    
    /**
     * Initializes the shop using the info stored in the files
     */
    private static Shop initializeShop()
    {
        Shop theShop = null;
        ArrayList<Item> itemList = new ArrayList<Item>();
        ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
        BufferedReader itemReader;
        BufferedReader supplierReader;
        
        try
        {
            // SUPPLIERS
            supplierReader = new BufferedReader(new InputStreamReader(new FileInputStream("suppliers.txt")));
            while(supplierReader.ready())
            {
                String supplierAsString = supplierReader.readLine();
                String[] componentsOfTheSupplier = supplierAsString.split(";");
                Supplier currentSupplier = initializeSupplier(componentsOfTheSupplier);
                supplierList.add(currentSupplier);
            }
            supplierReader.close();
            
            // ITEMS
            itemReader = new BufferedReader(new InputStreamReader(new FileInputStream("items.txt")));
            while(itemReader.ready())
            {
                String itemAsString = itemReader.readLine();
                String[] componentsOfTheItem = itemAsString.split(";");
                Item currentTool = initializeItem(componentsOfTheItem,supplierList);
                //Add it to the list
                itemList.add(currentTool);
            }
            itemReader.close();
            
            Inventory theInventory = new Inventory(itemList);
            theShop = new Shop(theInventory,supplierList);
        }
        catch (Exception e)
        {
            System.out.println("An error occurred while reading the files. Please make sure they are in the correct format. " + e);
            System.exit(1);
        }
        return theShop;
    }
    
    /**
     * generates a supplier object based on a string array with the components of the supplier.
     * 
     * @param componentsOfTheSupplier An array containing all of the components needed to build a supplier.
     * @return The supplier built
     */
    private static Supplier initializeSupplier(String[] componentsOfTheSupplier)
    {
        Supplier currentSupplier = new Supplier();
        
        //Supplier ID
        currentSupplier.setSupplierID(Integer.parseInt(componentsOfTheSupplier[0]));
        
        //Company Name
        currentSupplier.setCompanyName(componentsOfTheSupplier[1]);
        
        //Address
        currentSupplier.setAddress(componentsOfTheSupplier[2]);
        
        //Sales Contact
        currentSupplier.setSalesContact(componentsOfTheSupplier[3]);
        return currentSupplier;
    }
    
    /**
     * generates an item object based on a string array with the components of the item and a list of suppliers to associate the ID with.
     * 
     * @param componentsOfTheItem An array containing all of the components needed to build an item.
     * @param supplierList An arraylist containing all of the suppliers.
     * @return the item generated.
     */
    private static Item initializeItem(String[] componentsOfTheItem,ArrayList<Supplier> supplierList)
    {
        Item currentTool = new Item();
        
        //Item ID
        currentTool.setToolIDNumber(Integer.parseInt(componentsOfTheItem[0]));
        
        //Tool Name
        currentTool.setToolName(componentsOfTheItem[1]);
        
        //Quantity
        currentTool.setQuantity(Integer.parseInt(componentsOfTheItem[2]));
        
        //Price
        currentTool.setPrice(Double.parseDouble(componentsOfTheItem[3]));
        
        //Supplier ID number
        Supplier selectedSupplier = null;
        int supplierID = Integer.parseInt(componentsOfTheItem[4]);
        for(Supplier currentSupplier : supplierList)
        {
            if(currentSupplier.getSupplierID() == supplierID)
            {
                selectedSupplier = currentSupplier;
                break;
            }
        }
        if(selectedSupplier != null)
        {
            currentTool.setSupplier(selectedSupplier);
        }
        
        
        return currentTool;
    }
    
    /**
     * gets the input number for choice.
     * It should be between 1-6 although this function is not responsible for whatever input it gives.
     * 
     * @return the number the user has put in as their choice
     */
    private static int getInput()
    {
        BufferedReader stdin;
        stdin = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            String input = stdin.readLine();            
            int enteredNum = Integer.parseInt(input);
            return enteredNum;
        }
        catch (IOException ie)
        {
            return -1;
        }
        catch (NumberFormatException nfe)
        {
            
            return -1;
        }
    }
    
    /**
     * searches for a tool by name and then returns it
     * 
     * @param theShop the shop you should search for the item
     * @param stdin the input stream where you should ask for the name
     * @return the item with that name if an item with that name is present in the shop. Otherwise returns null
     */
    private static Item searchByName(Shop theShop, BufferedReader stdin)
    {
        System.out.println("Please enter the name of the desired item.");
        try
        {
            String name = stdin.readLine();
            System.out.println();
            Item desiredItem = theShop.searchByName(name);
            if(desiredItem == null)
            {
                System.out.println("Unable to find an item with that name.");
                return null;
            }
            else
            {
                return desiredItem;
            }
        }
        catch (IOException ie)
        {
            System.out.println("That name is not a valid name.");
            return null;
        }
    }
    
    /**
     * searches for a tool by ID and then returns it
     * 
     * @param theShop the shop you should search for the item
     * @param stdin the input stream where you should ask for the ID
     * @return the item with that ID if an item with that ID is present in the shop. Otherwise returns null
     */
    private static Item searchByID(Shop theShop, BufferedReader stdin)
    {
        System.out.println("Please enter the ID of the desired item.");
        try
        {
            String stringFormOfID = stdin.readLine();
            int identification = Integer.parseInt(stringFormOfID);
            System.out.println();
            Item desiredItem = theShop.searchByID(identification);
            if(desiredItem == null)
            {
                System.out.println("Unable to find an item with that ID.");
                return null;
            }
            else
            {
                return desiredItem;
            }
        }
        catch (IOException ie)
        {
            System.out.println("That ID number is not a valid ID number.");
            return null;
        }
    }
    
    /**
     * buys a certain amount of an item from the shop
     * 
     * @param theShop the shop you should buy the item from
     * @param selectedTool the tool to buy
     * @param stdin the input stream where you should ask for the quantity to remove
     */
    private static void buy(Shop theShop, Item selectedTool, BufferedReader stdin)
    {
        if(selectedTool != null)
        {
            try
            {
                System.out.println("How many of that item would you like to remove from stock?.");
                String stringFormOfQuantity = stdin.readLine();
                int quantity = Integer.parseInt(stringFormOfQuantity);
                if(quantity > 0)
                {
                    theShop.buy(selectedTool,quantity);
                }
                else
                {
                    System.out.println("Please remove an amount above zero.");
                }
            }
            catch (IOException ie)
            {
                System.out.println("That is not a valid number to remove.");
            }
        }
        else
        {
            return;
        }
    }
}
