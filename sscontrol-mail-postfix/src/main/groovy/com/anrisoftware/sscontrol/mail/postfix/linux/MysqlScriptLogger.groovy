package com.anrisoftware.sscontrol.mail.postfix.linux

import static org.apache.commons.lang3.StringUtils.repeat

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link MysqlScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MysqlScriptLogger extends AbstractLogger {

	static final String REHASH_FILE = "Rehash file '{}', worker {} for {}."
	static final String REHASH_FILE_INFO = "Rehash file '{}'."
	static final PASSWORD_MASK = "*"
	static final String ALIAS_TABLE = "Alias table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String ALIAS_TABLE1 = "Alias table created for {}, worker {}."
	static final String ALIAS_TABLE2 = "Alias table created for script '{}'."
	static final String DOMAINS_TABLE = "Domains table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String DOMAINS_TABLE1 = "Domains table created for {}, worker {}."
	static final String DOMAINS_TABLE2 = "Domains table created for script '{}'."
	static final String USERS_TABLE = "Users table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String USERS_TABLE1 = "Users table created for {}, worker {}."
	static final String USERS_TABLE2 = "Users table created for script '{}'."
	static final String DEPLOYED_DOMAINS = "Deployed domains for {}, worker {}."
	static final String DEPLOYED_DOMAINS2 = "Deployed domains for script '{}'."
	static final String DEPLOYED_ALIASES = "Deployed aliases for {}, worker {}."
	static final String DEPLOYED_ALIASES2 = "Deployed aliases for script '{}'."
	static final String DEPLOYED_USERS = "Deployed users for {}, worker {}."
	static final String DEPLOYED_USERS2 = "Deployed users for script '{}'."

	/**
	 * Create logger for {@link MysqlScript}.
	 */
	MysqlScriptLogger() {
		super(MysqlScript.class)
	}

	void deployedAliasesTable(MysqlScript script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace ALIAS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug ALIAS_TABLE1, script, workerstr
		} else {
			log.info ALIAS_TABLE2, script.name
		}
	}

	void deployedDomainsTable(MysqlScript script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace DOMAINS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug DOMAINS_TABLE1, script, workerstr
		} else {
			log.info DOMAINS_TABLE2, script.name
		}
	}

	void deployedUsersTable(MysqlScript script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace USERS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug USERS_TABLE1, script, workerstr
		} else {
			log.info USERS_TABLE2, script.name
		}
	}

	void deployedDomainsData(MysqlScript script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_DOMAINS, script, worker) :
				log.info(DEPLOYED_DOMAINS2, script.name)
	}

	void deployedAliasesData(MysqlScript script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_ALIASES, script, worker) :
				log.info(DEPLOYED_ALIASES2, script.name)
	}

	void deployedUsersData(MysqlScript script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_USERS, script, worker) :
				log.info(DEPLOYED_USERS2, script.name)
	}

	void rehashFileDone(def script, def file, def worker) {
		log.debugEnabled ? log.debug(REHASH_FILE, file, worker, script) :
				log.info(REHASH_FILE_INFO, file)
	}

	String replacePassword(MysqlScript script, String string) {
		def password = script.service.database.password
		int length = password.length()
		string.replace(password, PASSWORD_MASK * length)
	}
}
