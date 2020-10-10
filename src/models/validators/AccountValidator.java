package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Account;
import utils.DBUtil;

public class AccountValidator {
    public static List<String> validate(Account a, Boolean mail_duplicate_check_flag, Boolean code_duplicate_check_flag,
            Boolean password_check_flag) {
        List<String> errors = new ArrayList<String>();

        String mail_error = _validateMail(a.getMail(), mail_duplicate_check_flag);
        if (!mail_error.equals("")) {
            errors.add(mail_error);
        }

        String code_error = _validateCode(a.getCode(), code_duplicate_check_flag);
        if (!code_error.equals("")) {
            errors.add(code_error);
        }

        String name_error = _validateName(a.getName());
        if (!name_error.equals("")) {
            errors.add(name_error);
        }
        String password_error = _validatePassword(a.getPassword(), password_check_flag);
        if (!password_error.equals("")) {
            errors.add(password_error);
        }

        return errors;
    }

    // メールアドレス
    private static String _validateMail(String mail, Boolean mail_duplicate_check_flag) {
        // 必須入力チェック
        if (mail == null || mail.equals("")) {
            return "メールアドレスを入力してください。";
        }

        // すでに登録されているメールアドレスとの重複チェック
        if (mail_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long accounts_count = (long) em.createNamedQuery("checkRegisteredMail", Long.class)
                    .setParameter("mail", mail)
                    .getSingleResult();
            em.close();
            if (accounts_count > 0) {
                return "入力されたメールアドレスのアカウントはすでに存在しています。";
            }
        }

        return "";
    }

    // code（アカウントID）
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if (code == null || code.equals("")) {
            return "アカウントIDを入力してください。";
        }

        // すでに登録されているcode（アカウントID）との重複チェック
        if (code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long accounts_count = (long) em.createNamedQuery("checkRegisteredCode", Long.class)
                    .setParameter("code", code)
                    .getSingleResult();
            em.close();
            if (accounts_count > 0) {
                return "入力されたアカウントIDのアカウントはすでに存在しています。";
            }
        }

        return "";
    }

    // 名前の必須入力チェック
    private static String _validateName(String name) {
        if (name == null || name.equals("")) {
            return "名前を入力してください。";
        }

        return "";
    }

    // パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {
        // パスワードを変更する場合のみ実行
        if (password_check_flag && (password == null || password.equals(""))) {
            return "パスワードを入力してください。";
        }
        return "";
    }
}
