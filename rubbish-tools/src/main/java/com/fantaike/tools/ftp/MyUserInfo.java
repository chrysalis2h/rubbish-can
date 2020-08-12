package com.fantaike.tools.ftp;

import com.jcraft.jsch.UserInfo;

/**
 *
 * @author Luxin.xiao
 * @date 2018/9/6 15:44
 */
public class MyUserInfo implements UserInfo {
    private String passphrase = null;

    public MyUserInfo(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public String getPassphrase() {
        return passphrase;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return true;
    }

    @Override
    public boolean promptPassword(String s) {
        return true;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String s) {
        System.out.println(s);
    }
}
