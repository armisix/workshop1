/**
 * 
 */
package com.superscores.android.common.utils;

import android.accounts.Account;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Patterns;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * @author Pongpat Ratanaamornpin
 * 
 */
public class AccountUtils {

    public static String[] getEmails(@NonNull Context context) {

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        HashSet<String> emailMap = new HashSet<>();
        Account[] accounts = android.accounts.AccountManager.get(context)
                                                            .getAccounts();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name)
                            .matches()) {
                emailMap.add(account.name);
            }
        }

        return emailMap.toArray(new String[emailMap.size()]);
    }
}