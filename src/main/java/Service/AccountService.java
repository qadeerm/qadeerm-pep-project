package Service;
import Model.Account;

import java.util.*;

import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO;
    //private MessageService messageService;

    public AccountService(){
        this.accountDAO=new AccountDAO();
        //this.messageService = new MessageService();

    }
    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO=accountDAO;
    }
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAaccounts();
    }
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }
    public Account getAccount(Account account) {
        return accountDAO.getAccountByName(account.getUsername());
    }
    public Account getAccountById(int id) {
        return accountDAO.getAccountById(id);
    }
}    

