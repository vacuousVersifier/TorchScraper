package cmonster.cookies;

import java.io.File;
import java.util.Date;

public class Cookie {

	protected final String name;
	protected String value;
	protected final Date expires;
	protected final String path;
	protected final String domain;
	protected final boolean secure;
	protected final boolean httpOnly;
	protected final File cookieStore;

	/**
	 * Represents an unencrypted cookie
	 *
	 * @param name Name or key of the cookie
	 * @param value Value of the cookie
	 * @param expires The day and time the cookie expires
	 * @param path The location of the cookie
	 * @param domain The domain the cookie is for
	 * @param secure Is the cookie secure?
	 * @param httpOnly Is the cookie HTTP only?
	 * @param cookieStore Cookie's cookiestore
	 */
	public Cookie(String name, String value, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
		this.name = name;
		this.value = value;
		this.expires = expires;
		this.path = path;
		this.domain = domain;
		this.secure = secure;
		this.httpOnly = httpOnly;
		this.cookieStore = cookieStore;
	}

	public Cookie(String name, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
		this.name = name;
		this.expires = expires;
		this.path = path;
		this.domain = domain;
		this.secure = secure;
		this.httpOnly = httpOnly;
		this.cookieStore = cookieStore;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Date getExpires() {
		return expires;
	}

	public String getPath() {
		return path;
	}

	public String getDomain() {
		return domain;
	}

	public boolean isSecure() {
		return secure;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public File getCookieStore() {
		return cookieStore;
	}

	@Override
	public String toString() {
		return "Cookie [name=" + name + ", value=" + value + "]";
	}
}
