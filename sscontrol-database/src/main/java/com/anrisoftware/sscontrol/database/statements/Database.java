package com.anrisoftware.sscontrol.database.statements;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Defines the database name, character set and collate.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Database implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 4244380138329390017L;

	private final DatabaseLogger log;

	private final String name;

	private String characterSet;

	private String collate;

	private final List<URI> sqlImports;

	/**
	 * Sets the name of the database.
	 * 
	 * @param logger
	 *            the {@link DatabaseLogger} for logging messages.
	 * 
	 * @param name
	 *            the name of the database.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	@Inject
	Database(DatabaseLogger logger, @Assisted String name) {
		this.log = logger;
		log.checkName(this, name);
		this.name = name;
		this.sqlImports = new ArrayList<URI>();
	}

	/**
	 * Sets additional arguments for the database.
	 * 
	 * @param args
	 *            the arguments {@link Map}.
	 * 
	 * @see #setCharacterSet(String)
	 * @see #setCollate(String)
	 */
	public void setArguments(Map<String, String> args) {
		if (args.containsKey("character_set")) {
			setCharacterSet(args.get("character_set"));
		}
		if (args.containsKey("collate")) {
			setCollate(args.get("collate"));
		}
	}

	/**
	 * Adds a SQL script to import in the database from the specified URI.
	 * 
	 * @param str
	 *            the string to be parsed as URI.
	 * 
	 * @throws URISyntaxException
	 *             If the given string violates RFC&nbsp;2396, as augmented by
	 *             {@link URI#URI(String)}
	 */
	public void import_sql(String str) throws URISyntaxException {
		import_sql(new URI(str));
	}

	/**
	 * Adds a SQL script to import in the database from the specified local
	 * file.
	 * 
	 * @param file
	 *            the local {@link File} of the script.
	 * 
	 * @throws NullPointerException
	 *             if the specified file is {@code null}.
	 */
	public void import_sql(File file) {
		log.checkFile(this, file);
		import_sql(file.toURI());
	}

	/**
	 * Adds a SQL script to import in the database from the specified URL.
	 * 
	 * @param url
	 *            the {@link URL} of the script.
	 * 
	 * @throws URISyntaxException
	 *             if this URL cannot be converted to a URI.
	 * 
	 * @throws NullPointerException
	 *             if the specified URL is {@code null}.
	 * 
	 * @see URL#toURI()
	 */
	public void import_sql(URL url) throws URISyntaxException {
		log.checkURL(this, url);
		import_sql(url.toURI());
	}

	/**
	 * Adds a SQL script to import in the database from the specified URI.
	 * 
	 * @param uri
	 *            the {@link URI} of the script.
	 * 
	 * @throws NullPointerException
	 *             if the specified URI is {@code null}.
	 */
	public void import_sql(URI uri) {
		log.checkURI(this, uri);
		sqlImports.add(uri);
		log.sqlScriptAdd(this, uri);
	}

	/**
	 * Returns the name of the database.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the default character set for the database.
	 * 
	 * @param set
	 *            the character set.
	 * 
	 * @throws NullPointerException
	 *             if the specified character set is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified character set is empty.
	 */
	public void setCharacterSet(String set) {
		log.checkCharacterSet(this, set);
		this.characterSet = set;
		log.characterSetSet(this, set);
	}

	/**
	 * Returns the default character set for the database.
	 */
	public String getCharacterSet() {
		return characterSet;
	}

	/**
	 * Sets the default collate for the database.
	 * 
	 * @param set
	 *            the collate.
	 * 
	 * @throws NullPointerException
	 *             if the specified collate is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified collate is empty.
	 */
	public void setCollate(String collate) {
		log.checkCollate(this, collate);
		this.collate = collate;
		log.collateSet(this, collate);
	}

	/**
	 * Returns the default collate for the database.
	 */
	public String getCollate() {
		return collate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("character set", characterSet)
				.append("collate", collate).append("imports", sqlImports)
				.toString();
	}

}
