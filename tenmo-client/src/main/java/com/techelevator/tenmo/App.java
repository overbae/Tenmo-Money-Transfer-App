package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final int TRANSFER_TYPE_REQUEST = 1;
    private final int TRANSFER_TYPE_SEND = 2;
    private final int TRANSFER_STATUS_APPROVED = 2;

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }


    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        accountService.setAuthenticatedUser(currentUser);
        transferService.setAuthenticatedUser(currentUser);
        userService.setAuthenticatedUser(currentUser);
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu(currentUser.getUser());
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                System.exit(1);
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        consoleService.printAccountBalance(accountService.getUserAccount());
    }

    private void viewTransferHistory() {
        // Retrieve the current account, transfer list, and associated usernames
        Account currentAccount = accountService.getUserAccount();
        List<Transfer> transfers = accountService.getTransfers();
        List<String> userFromTransfers = userService.getUsersFromTransfers(transfers, currentAccount);

        // Print the transfer history with formatted details
        consoleService.printTransferHistory(transfers, userFromTransfers, currentAccount);

        // Prompt the user to enter a transfer ID for detailed view
        int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");

        // Check if the transfer ID is valid, otherwise return to the main menu
        if (!transferService.isTransferValid(transferId)) {
            mainMenu();
        }

        // Retrieve the transfer details and associated usernames
        Transfer transfer = transferService.getTransferById(transferId);
        String userFrom = userService.getUsernameFromAccount(transfer.getAccountTo());
        String userTo = userService.getUsernameFromAccount(transfer.getAccountFrom());

        // Print the detailed transfer information
        consoleService.printTransferDetails(transfer, userFrom, userTo);
    }
    // this method retrieves pending transfers, displays them to the user along with relevant
    // information, and prompts the user to enter the transfer ID.
    // It will determine if the ID entered is valid, display the options and retrieve the
    // transfer and recipient's account details.
    private void viewPendingRequests() {
        List<Transfer> transfers = accountService.getPendingTransfers();
        List<String> usersFromTransfers = new ArrayList<>();
        Account currentAccount = accountService.getUserAccount();
        for (Transfer transfer : transfers) {
            String username = userService.getUsernameFromAccount(transfer.getAccountTo());
            usersFromTransfers.add(username);
        }

        consoleService.printPendingTransfers(transfers, usersFromTransfers, currentAccount);

        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel):");

        if (!transferService.isTransferValid(transferId)) {
            mainMenu();
        }

        consoleService.printPendingOptions();


        Transfer transfer = transferService.getTransferById(transferId);
        Account receiverAccount = accountService.getAccountById(transfer.getAccountTo());
        boolean isAmountInRange = accountService.isAmountInAccountRange(transfer.getAmount(), currentAccount.getBalance());

        startPendingTransferProcess(transfer, currentAccount, receiverAccount, isAmountInRange);

    }

    private void sendBucks() {
        beginTransferProcess(TRANSFER_TYPE_SEND);
    }


    private void requestBucks() {
        beginTransferProcess(TRANSFER_TYPE_REQUEST);
    }

   // this method handles the processing of a pending transfer by allowing the user to choose whether
   // to approve or reject the transfer. It updates the transfer status, performs necessary actions based
   // on the choice and account balance, updates the accounts, and provides feedback to the user.
    public void startPendingTransferProcess(Transfer transfer, Account currentAccount, Account receiverAccount,
                                            boolean isAmountInRange) {
        int transferChoice = consoleService.promptForInt("Please choose an option: ");

        if (!transferService.isTransferChoiceValid(transferChoice)) {
            mainMenu();
        }
        final int TRANSFER_APPROVE = 1;
        final int TRANSFER_REJECT = 2;
        if (transferChoice == TRANSFER_APPROVE) {
            transfer.setTransferStatusId(TRANSFER_STATUS_APPROVED);
        }

        if (transferChoice == TRANSFER_REJECT) {
            int TRANSFER_STATUS_REJECTED = 3;
            transfer.setTransferStatusId(TRANSFER_STATUS_REJECTED);
            transferService.updateTransfer(transfer);
            System.out.println("You have rejected the transfer with ID " + transfer.getTransferId());
            mainMenu();
        }

        if (isAmountInRange) {
            System.out.println("Not enough money to accept this request.");
            System.out.println("Returning to main menu...");
            mainMenu();
        }

        boolean wasAccepted = transferService.updateTransfer(transfer);

        if (!wasAccepted) {
            System.out.println("We have problems processing your request. Please try again later.");
            mainMenu();
        }

        currentAccount.sendMoney(transfer.getAmount());
        receiverAccount.receiveMoney(transfer.getAmount());

        accountService.updateAccount(currentAccount);
        accountService.updateAccount(receiverAccount);
        System.out.println("Success! Your transfer request for ID " + transfer.getTransferId() + " was processed correctly!");
        mainMenu();

    }
    // This method handles the process of initiating a money transfer or request between users.
    // It prompts for user input, validates the input, performs the necessary actions based on the transfer type,
    // and provides feedback to the user.
    private void beginTransferProcess(int transferType) {
        User current = currentUser.getUser();
        String RED_BOLD = "\033[1;31m";
        String RESET = "\033[0m";

        List<User> users = userService.getAllUsers();
        consoleService.displayAvailableUsers(users, current.getId());

        boolean isTransferTypeRequest = (transferType == TRANSFER_TYPE_REQUEST);

        String transferTypeString = (isTransferTypeRequest) ? "request" : "send";
        int targetUser = consoleService.promptForInt("Please enter the ID of the user you want to " + transferTypeString +
                " money or enter 0 to cancel the transfer:  ");

        if (!userService.isUserValid(targetUser, current)) {
            mainMenu();
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of money you want to " + transferTypeString + " or 0 to cancel: ");
        if (!userService.isAmountValid(amount)) {
            mainMenu();
        }


        System.out.println((isTransferTypeRequest) ? "Requesting money..." : "Sending money...");

        Account currentAccount = accountService.getUserAccount();
        Account targetAccount = accountService.getAccount(targetUser);
        Transfer transfer = new Transfer();


        if (isTransferTypeRequest) {
            int TRANSFER_STATUS_PENDING = 1;
            transferService.setTransferDetails(transfer, TRANSFER_TYPE_REQUEST, TRANSFER_STATUS_PENDING,
                    targetAccount.getAccountID(), currentAccount.getAccountID(), amount);
        } else {
            if (accountService.isAmountInAccountRange(amount, currentAccount.getBalance())) {
                System.out.println("Transfer was not completed." + " Reason:" + RED_BOLD + " Not enough money in account." + RESET);
                mainMenu();
            }
            transferService.setTransferDetails(transfer, TRANSFER_TYPE_SEND, TRANSFER_STATUS_APPROVED,
                    targetAccount.getAccountID(), currentAccount.getAccountID(), amount);
            targetAccount.receiveMoney(amount);
            currentAccount.sendMoney(amount);

            accountService.updateAccount(targetAccount);
            accountService.updateAccount(currentAccount);
        }


        Transfer returnedTransfer = transferService.createTransfer(transfer);
        int transferId = returnedTransfer.getTransferId();

        if (returnedTransfer.getTransferId() == 0) {
            System.out.println("Transfer could not be completed. Try again later");
            mainMenu();
        }

        if (isTransferTypeRequest) {
            consoleService.printRequestSuccess(targetUser, amount, transferId);
        } else {
            consoleService.printSendSuccess(targetUser, amount, transferId);
        }
        mainMenu();
    }
}
// comment for a push