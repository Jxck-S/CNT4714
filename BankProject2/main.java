import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.time.ZonedDateTime;
/* Name: Jack Sweeney
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 11, 2024
*/
class BankAccount {
    private int balance = 0;
    private Lock lock = new ReentrantLock();
    private Condition sufficientFunds = lock.newCondition();
    public int transactions = 0;
    public void transact(){
        this.transactions += 1;
    }
    public String balance_is(String indicator) {
        return String.format("(%s) Balance is $%d", indicator, this.balance);
    }
    public void deposit(int amount, String name) {
        lock.lock();
        try {
            balance += amount;
            this.transact();
            String agent_String = String.format("Agent %s deposits $%d", name, amount);
            System.out.printf("%-25s %-25s %-25s %25d\n", agent_String, "", this.balance_is("+"), transactions);
            sufficientFunds.signalAll(); // Signal the waiting
            double logAmt = 350.00;
            if (amount > logAmt){
                System.out.printf("\n*** Flagged Transaction - Despoistor Agent %s Made a Deposit in Excess of %.2f - See Flaggd Transaction Log.\n\n", name, logAmt);
                TransactionLogger.logTransaction(ZonedDateTime.now(), "Depositor", name, "deposit", (double)amount, transactions);
            }
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount, String name) {
        lock.lock();
        try {
            String agent_String = String.format("Agent %s withdraws $%d", name, amount);
            while (balance < amount) { // If the balance is insufficient, wait
                System.out.printf("%-25s %-25s (******) WITHDRAWAL BLOCKED! INSUFFICIENT FUNDS!! \n", "", agent_String);
                sufficientFunds.await(); //AWAITS MONEY
            }
            balance -= amount;
            this.transact();
            System.out.printf("%-25s %-25s %-25s %25d\n", "", agent_String, this.balance_is("-"), transactions);
            double logAmt = 75.00;
            if (amount > logAmt){
                System.out.printf("\n*** Flagged Transaction - Withdrawal Agent %s Made a Withdrawal in Excess of %.2f - See Flaggd Transaction Log.\n\n", name, logAmt);
                TransactionLogger.logTransaction(ZonedDateTime.now(), "\t\tWithdrawal", name, "withdrawawl", (double)amount, transactions);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reset
        } finally {
            lock.unlock();
        }
    }

    public void audit(Auditor aud){
            lock.lock();
            System.out.println("\n"+new String(new char[100]).replace("\0", "*")+"\n");
            int current_transactions_count = aud.current_transactions_count();
            int since_check = current_transactions_count - aud.last_check_count;
            System.out.println(aud.type + " FINDS CURRENT ACCOUNT BALANCE: $" + this.getBalance()+"\t Number of transactions since last "+ aud.type_short +" audit is: "+ since_check + "\n");
            System.out.println(new String(new char[100]).replace("\0", "*")+"\n");
            aud.last_check_count = current_transactions_count;
            lock.unlock();

    }


    public int getBalance() {
        return balance;
    }
}

class Depositor implements Runnable {
    private BankAccount account;
    private String name;

    public Depositor(BankAccount account, String name) {
        this.account = account;
        this.name = name;
    }

    public void run() {
        while (true) {
            int amount = (int) (Math.random() * 500) + 1;
            account.deposit(amount, name);
            try {
                long sleep_time = (long) (Math.random() * 200);
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Withdrawer implements Runnable {
    private BankAccount account;
    private String name;

    public Withdrawer(BankAccount account, String name) {
        this.account = account;
        this.name = name;
    }

    public void run() {
        while (true) {
            int amount = (int) (Math.random() * 99) + 1;
            account.withdraw(amount, name);
            try {
                Thread.sleep((long) (Math.random() * 50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Auditor implements Runnable {
    private BankAccount account;
    public String type;
    public String type_short;
    public int last_check_count = 0;

    public Auditor(BankAccount account, String type, String type_short) {
        this.account = account;
        this.type = type;
        this.type_short = type_short;
    }
    public int current_transactions_count(){
        return this.account.transactions;
    }
    public void run() {
        while (true) {
            account.audit(this);
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}

public class main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(17);
        BankAccount account = new BankAccount();
        //Start
        System.out.println("*** SIMULATION BEGINS...\n");
        System.out.printf("%-25s %-25s %-25s %-25s%n", "Deposit Agents", "Withdrawal Agents", "Balance", "Transaction");
        System.out.printf("%-25s %-25s %-25s %-25s%n", "---------------", "------------------", "------------------------", "--------------------------");

        //Add the runnables for each
        for (int i = 0; i < 5; i++) {
            executor.execute(new Depositor(account, "DT" + (i + 1)));
        }

        for (int i = 0; i < 10; i++) {
            executor.execute(new Withdrawer(account, "WT" + (i + 1)));
        }

        executor.execute(new Auditor(account, "INTERNAL BANK AUDITOR", "Internal"));
        executor.execute(new Auditor(account, "TREASURY DEPT AUDITOR", "Treasury"));

    }
}
