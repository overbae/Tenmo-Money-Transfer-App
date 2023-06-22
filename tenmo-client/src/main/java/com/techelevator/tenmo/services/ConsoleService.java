package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {
    private final Scanner scanner = new Scanner(System.in);
    private final RestTemplate restTemplate = new RestTemplate();


    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);

        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    // greeting is printed and colored.
    public void printGreeting() {
        String PURPLE_BOLD = "\033[1;35m";
        String RESET = "\033[0m";
        System.out.println(PURPLE_BOLD);
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
        System.out.println(RESET);

    }

    // displays the login menu with some color added to it
    public void printLoginMenu() {
        String CYAN_BOLD = "\033[1;36m";
        String RESET = "\033[0m";
        System.out.println();
        System.out.println(CYAN_BOLD + "1: Register" + RESET);
        System.out.println(CYAN_BOLD + "2: Login" + RESET);
        System.out.println(CYAN_BOLD + "0: Exit" + RESET);
        System.out.println();
    }

    // this just displays the menu and I used some colors just to make it look nicer.
    public void printMainMenu(User user) {
        String CYAN_BOLD = "\033[1;36m";
        String RESET = "\033[0m";

        System.out.println(CYAN_BOLD + "Welcome, " + user.getUsername() + "!" + RESET);
        System.out.println();
        System.out.println(CYAN_BOLD + "1: View your current balance" + RESET);
        System.out.println(CYAN_BOLD + "2: View your past transfers" + RESET);
        System.out.println(CYAN_BOLD + "3: View your pending requests" + RESET);
        System.out.println(CYAN_BOLD + "4: Send TE bucks" + RESET);
        System.out.println(CYAN_BOLD + "5: Request TE bucks" + RESET);
        System.out.println(CYAN_BOLD + "0: Exit" + RESET);
        System.out.println();
    }

    public void printAllAccounts(List<Account> accounts) {
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    // Prints the current account balance
    public void printAccountBalance(Account account) {
        System.out.println("Your current account balance is: $" + account.getBalance());
    }

    // Displays the available users for making transfers or requests
    public void displayAvailableUsers(List<User> users, int id) {
        System.out.println("User ID || Username");
        for (User user : users) {
            if (user.getId() == id) {
                continue;
            }
            // Prints the user ID and username in the specified format
            System.out.println(user.getId() + " || " + user.getUsername());
        }
    }


    // Prints the header and formatting for the transfer history table
    public void printTransferHistory(List<Transfer> transfers, List<String> users, Account currentAccount) {
        System.out.println("--------------------------------------------------");
        System.out.println("                  Transfers");
        System.out.println("                                              ");
        System.out.println("ID             From/To               Amount");
        System.out.println("--------------------------------------------------");

        // Iterates through the transfers list and prints each transfer's details
        for (int i = 0; i < transfers.size(); i++) {
            Transfer transfer = transfers.get(i);
            String fromTo = transfer.getAccountFrom() == currentAccount.getAccountID() && transfer.getTransferTypeId() > 1 ? "From: " : "To: ";
            String username = users.get(i);

            // Formats the from/to, username, and amount columns
            String formattedFromTo = String.format("%-8s", fromTo);
            String formattedUsername = String.format("%-15s", username);
            String formattedAmount = String.format("%10s", "$" + String.format("%.2f", transfer.getAmount()));

            // Prints the transfer details in a formatted manner
            System.out.printf("%-9d %-15s %-10s%n", transfer.getTransferId(), formattedFromTo + formattedUsername, formattedAmount);
        }

        System.out.println("--------------------------------------------------");
    }


    // Prints the pending transfers
    public void printPendingTransfers(List<Transfer> transfers, List<String> users, Account currentAccount) {
        System.out.println("-------------------------------------------\n" +
                "              Pending Transfers\n");
        System.out.printf("ID %17s %17s%n", "From/To", "Amount");
        System.out.println("-------------------------------------------");
        int i = 0;
        for (Transfer transfer : transfers) {
            String fromTo = transfer.getAccountFrom() == currentAccount.getAccountID() && transfer.getTransferTypeId() == 1 ? "From: " : "To: ";
            String username = users.get(i++);
            // Prints the transfer ID, from/to information, and amount in the specified format
            System.out.printf("%d %16s %16s%n", transfer.getTransferId(), fromTo + username, "$" + transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
    }

    // Prints the details of a transfer
    public void printTransferDetails(Transfer transfer, String userFrom, String userTo) {
        String transferType = transfer.getTransferTypeId() == 1 ? "Request" : "Send";
        String transferStatus = "";
        if (transfer.getTransferStatusId() == 1) {
            transferStatus = "Pending";
        }
        if (transfer.getTransferStatusId() == 2) {
            transferStatus = "Approved";
        }
        if (transfer.getTransferStatusId() == 3) {
            transferStatus = "Rejected";
        }

        // Prints the transfer ID, account from/to information, transfer type, transfer status, and amount
        System.out.println("Transfer Id: " + transfer.getTransferId() +
                "\nAccount From: " + userFrom +
                "\nAccount To: " + userTo +
                "\nTransfer Type: " + transferType +
                "\nTransfer Status: " + transferStatus +
                "\nAmount: $" + transfer.getAmount());
    }


    public void printPendingOptions() {
        System.out.println("---------------------\n" +
                "1: Approve\n" +
                "2: Reject\n" +
                "0: Don't approve or reject\n" +
                "---------------------\n");
    }

    // Prints a success message for a transfer request
    public void printRequestSuccess(int accountFrom, BigDecimal amount, int transferId) {
        System.out.println("Success! Your transfer request was sent to account " + accountFrom + " of the amount of $" + amount);
        System.out.println("You will receive a status update in your transfers history.");
        System.out.println("Your transfer ID is #" + transferId);
    }

    // Prints a success message for a transfer send
    public void printSendSuccess(int accountTo, BigDecimal amount, int transferId) {
        System.out.println("Success! Your transfer was sent to account " + accountTo + " with the approved amount of $" + amount);
        System.out.println("Your transfer ID is #" + transferId);
    }

    // Prompts the user for their credentials and returns them as UserCredentials
    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    // Prompts the user for a string input based on the given prompt
    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Prompts the user for an integer input based on the given prompt
    public int promptForInt(String prompt) {
        System.out.print(prompt);

        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    // Prompts the user for a BigDecimal input based on the given prompt
    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {

            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    // Pauses the program execution and waits for the user to press Enter
    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Prints an error message
    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }
}
