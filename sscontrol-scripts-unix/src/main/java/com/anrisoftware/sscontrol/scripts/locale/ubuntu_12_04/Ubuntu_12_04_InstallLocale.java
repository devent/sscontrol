/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04;

import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger.CHARSET_KEY;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger.DPKG_RECONFIGURE_COMMAND_KEY;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger.INSTALL_COMMAND_KEY;
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger.LOCALES_DIRECTORY_KEY;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocale;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocaleSettings;
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuLocaleSettingsFactory;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Ubuntu 12.04</i> locale installer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ubuntu_12_04_InstallLocale extends UbuntuInstallLocale {

    private static final String COMMAND_KEY = "command";

    private static final String PACKAGES_KEY = "packages";

    private final Threads threads;

    private final Object parent;

    private final Ubuntu_12_04_InstallLocaleLogger log;

    @Inject
    private InstallPackagesFactory installPackagesFactory;

    @Inject
    private UbuntuLocaleSettingsFactory ubuntuLocaleSettingsFactory;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    private TemplateResource generateLocaleResource;

    /**
     * @see Ubuntu_12_04_InstallLocaleFactory#create(Map, Object, Threads)
     */
    @Inject
    Ubuntu_12_04_InstallLocale(Ubuntu_12_04_InstallLocaleLogger log,
            @Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        super(args);
        this.log = log;
        this.parent = parent;
        this.threads = threads;
        log.checkInstallCommand(args, parent);
        log.checkSystem(args, parent);
        log.checkCharset(args, parent);
        log.checkDpkgReconfigureCommand(args, parent);
        log.checkLocalesDirectory(args, parent);
    }

    @Inject
    public final void setTemplates(TemplatesFactory factory) {
        Templates templates = factory.create(this.getClass().getSimpleName());
        this.generateLocaleResource = templates.getResource("generate_locale");
    }

    @Override
    public Ubuntu_12_04_InstallLocale call() throws Exception {
        UbuntuLocaleSettings localeSettings;
        for (PosixLocale locale : getLocales()) {
            localeSettings = ubuntuLocaleSettingsFactory.create(locale);
            installLocale(localeSettings);
            attachLocale(localeSettings);
            generateLocale(localeSettings);
        }
        return this;

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

    private void installLocale(UbuntuLocaleSettings settings)
            throws ScriptException {
        List<String> packages = settings.getPackages();
        try {
            installPackages(packages);
        } catch (Exception e) {
            throw log.errorInstallPackages(this, e, packages);
        }
    }

    private void installPackages(List<String> packages) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>(getArgs());
        args.put(PACKAGES_KEY, packages);
        args.put(COMMAND_KEY, getArgs().get(INSTALL_COMMAND_KEY));
        installPackagesFactory.create(args, parent, threads).call();
    }

    private void attachLocale(UbuntuLocaleSettings settings)
            throws ScriptException {
        File dir = (File) getArgs().get(LOCALES_DIRECTORY_KEY);
        File file = new File(dir, settings.getSupportedLocaleFileName());
        String name = settings.getConvertName();
        Charset charset = (Charset) getArgs().get(CHARSET_KEY);
        List<String> lines = getLocales(file, charset);
        String locale = name + " " + settings.getConvertedCharset();
        addLocaleIfNotFound(lines, locale);
        writeLocales(file, charset, lines);
    }

    private void addLocaleIfNotFound(List<String> lines, String locale) {
        for (int i = 0; i < lines.size(); i++) {
            if (startsWith(lines.get(i), locale)) {
                return;
            }
        }
        lines.add(locale);
    }

    private void writeLocales(File file, Charset charset, List<String> lines)
            throws ScriptException {
        try {
            FileUtils.writeLines(file, charset.name(), lines);
        } catch (IOException e) {
            throw log.errorAttachLocale(this, e, file);
        }
    }

    private List<String> getLocales(File file, Charset charset)
            throws ScriptException {
        List<String> lines = new ArrayList<String>();
        if (file.isFile()) {
            lines = loadLocales(file, charset);
        }
        return lines;
    }

    private List<String> loadLocales(File file, Charset charset)
            throws ScriptException {
        try {
            return FileUtils.readLines(file, charset);
        } catch (IOException e) {
            throw log.errorLoadLocales(this, e, file);
        }
    }

    private void generateLocale(UbuntuLocaleSettings settings)
            throws ScriptException {
        try {
            generateLocale0();
        } catch (Exception e) {
            throw log.errorGenerateLocale(this, e, settings.getLocale());
        }
    }

    private void generateLocale0() throws Exception {
        Map<String, Object> args = new HashMap<String, Object>(getArgs());
        args.put(COMMAND_KEY, getArgs().get(DPKG_RECONFIGURE_COMMAND_KEY));
        scriptExecFactory.create(args, parent, threads, generateLocaleResource,
                "dpkgReconfigureLocale").call();
    }

}
