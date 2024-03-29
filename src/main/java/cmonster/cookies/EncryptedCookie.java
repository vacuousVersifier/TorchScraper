package cmonster.cookies;

import java.io.File;
import java.util.Date;

public class EncryptedCookie extends Cookie {

    protected final byte[] encryptedValue;

    public EncryptedCookie(String name, byte[] encryptedValue, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
        super(name, expires, path, domain, secure, httpOnly, cookieStore);
        this.encryptedValue = encryptedValue;
        this.value = "(encrypted)";
    }

    public byte[] getEncryptedValue() {
        return encryptedValue;
    }

    @Override
    public String toString() {
        return "Cookie [name=" + name + " (encrypted)]";
    }

}
