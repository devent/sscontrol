package com.anrisoftware.sscontrol.httpd.nginx.nginxconfig;

import static org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Accumulate directives for locations with the same name.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class NginxConfigList extends ArrayList<String> {

    static final Pattern LOCATION_SECTION_PATTERN = Pattern
            .compile("(?m)^location (.*) \\{$");

    private final String newLine;

    NginxConfigList() {
        this.newLine = System.getProperty("line.separator");
    }

    @Override
    public boolean add(String string) {
        LocationEntry entry = createLocationEntry(string, -1);
        if (entry != null) {
            LocationEntry thisentry = findLocationEntry(this, entry.getName());
            if (thisentry != null) {
                entry = insertLocation(entry, thisentry);
                super.set(entry.getIndex(), entry.getString());
                return false;
            }
        }
        return super.add(string);
    }

    private LocationEntry findLocationEntry(List<String> list, String name) {
        for (int i = 0; i < size(); i++) {
            LocationEntry entry = createLocationEntry(get(i), i);
            if (entry != null && entry.getName().equals(name)) {
                return entry;
            }
        }
        return null;
    }

    private LocationEntry insertLocation(LocationEntry entry,
            LocationEntry thisentry) {
        thisentry.getBody();
        return thisentry.insertBody(entry.getBody(),
                thisentry.getLocationBodyEnd());
    }

    private LocationEntry createLocationEntry(String string, int index) {
        String[] lines = splitByWholeSeparatorPreserveAllTokens(string, newLine);
        for (int i = 0; i < lines.length; i++) {
            if (LOCATION_SECTION_PATTERN.matcher(lines[i]).matches()) {
                return new LocationEntry(lines, index, i, newLine);
            }
        }
        return null;
    }
}

class LocationEntry {

    private final Pattern SECTION_START_PATTERN = Pattern.compile("(?m)^.*\\{");

    private final Pattern SECTION_END_PATTERN = Pattern.compile("(?m)^\\s*\\}");

    private final List<String> lines;

    private final int lineIndex;

    private final String newLine;

    private final int index;

    private int locationBodyEnd;

    public LocationEntry(String[] lines, int index, int lineIndex,
            String newLine) {
        this.lines = Arrays.asList(lines);
        this.index = index;
        this.lineIndex = lineIndex;
        this.newLine = newLine;
    }

    public LocationEntry insertBody(Collection<String> body, int offset) {
        List<String> list = new ArrayList<String>();
        list.addAll(lines);
        list.addAll(offset, body);
        return new LocationEntry(list.toArray(new String[] {}), index,
                lineIndex, newLine);
    }

    public String getName() {
        Matcher matcher = NginxConfigList.LOCATION_SECTION_PATTERN
                .matcher(lines.get(lineIndex));
        matcher.find();
        return matcher.group(1);
    }

    public Collection<String> getBody() {
        List<String> body = new ArrayList<String>();
        int level = 0;
        int i;
        for (i = lineIndex + 1; i < lines.size(); i++) {
            if (SECTION_START_PATTERN.matcher(lines.get(i)).find()) {
                level++;
            }
            if (SECTION_END_PATTERN.matcher(lines.get(i)).find()) {
                level--;
                if (level < 0) {
                    break;
                }
            }
            body.add(lines.get(i));
        }
        this.locationBodyEnd = i;
        return body;
    }

    public String getString() {
        return StringUtils.join(lines, newLine);
    }

    public int getIndex() {
        return index;
    }

    public int getLocationBodyEnd() {
        return locationBodyEnd;
    }
}
