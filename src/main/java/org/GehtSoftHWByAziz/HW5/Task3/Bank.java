package org.GehtSoftHWByAziz.HW5.Task3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Bank {
  ArrayList<Account> accounts;
  Random random = new Random();

  public Bank(int numberOfAccounts, long minBalance, long maxBalance) {
    this.accounts = new ArrayList<>();
    for (int i = 0; i < numberOfAccounts; ++i) {
      Long balance = random.nextLong(maxBalance - minBalance + 1) + minBalance;
      accounts.add(new Account(balance));
    }
  }

  public int pickRandomAccountId() {
   return random.nextInt(accounts.size());

  }
  public long getAccountBalance(int accountId) {
  return this.accounts.get(accountId).getBalance();
  }
  public void setAccountBalance(int accountId, long newBalance) {
    this.accounts.get(accountId).setBalance(newBalance);
  }
  public BigInteger getSumOfAllAccounts() {
    BigInteger bigInteger = new BigInteger("0");
    for (var account : this.accounts) {
      bigInteger = bigInteger.add(BigInteger.valueOf(account.getBalance()));
    }
  return bigInteger;
  }

}
