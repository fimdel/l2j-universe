/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.utils.Language;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Author: VISTALL
 * Date:  19:27/29.12.2010
 */
public final class StringHolder extends AbstractHolder {
    private static final StringHolder _instance = new StringHolder();
    private Map<Language, Map<String, String>> _strings = new HashMap<Language, Map<String, String>>();

    public static StringHolder getInstance() {
        return _instance;
    }

    private StringHolder() {

    }

    public String getNullable(Player player, String name) {
        Language lang = player == null ? Language.ENGLISH : player.getLanguage();
        return get(lang, name);
    }

    public String getNotNull(Player player, String name) {
        Language lang = player == null ? Language.ENGLISH : player.getLanguage();

        String text = get(lang, name);
        if (text == null && player != null) {
            text = "Not find string: " + name + "; for lang: " + lang;
            _strings.get(lang).put(name, text);
        }

        return text;
    }

    public String getNotNull(Language lang, String name) {
        String text = get(lang, name);
        if (text == null) {
            text = "Not find string: " + name + "; for lang: " + lang;
            _strings.get(lang).put(name, text);
        }

        return text;
    }

    public String getNullable(Language lang, String name) {
        return get(lang, name);
    }

    public String getSkillName(Language lang, Skill skill) {
        return getSkillName(lang, skill.getDisplayId(), skill.getDisplayLevel());
    }

    public String getSkillName(Player player, Skill skill) {
        return getSkillName(player, skill.getDisplayId(), skill.getDisplayLevel());
    }

    public String getSkillName(Language lang, int id, int level) {
        String skillName = getNullable(lang, "skill_name_id_" + id + "_level_" + level);
        return skillName == null ? "" : skillName;
    }

    public String getSkillName(Player player, int id, int level) {
        Language lang = player == null ? Language.ENGLISH : player.getLanguage();
        return getSkillName(lang, id, level);
    }

    public String getItemName(Language lang, int id) {
        String itemName = getNullable(lang, "item_name_id_" + id);
        return itemName == null ? "" : itemName;
    }

    public String getItemName(Player player, int id) {
        Language lang = player == null ? Language.ENGLISH : player.getLanguage();
        return getItemName(lang, id);
    }

    private String get(Language lang, String address) {
        Map<String, String> strings = _strings.get(lang);

        return strings.get(address);
    }

    public void load() {
        for (Language lang : Language.VALUES) {
            _strings.put(lang, new HashMap<String, String>());

            File f = new File(Config.DATAPACK_ROOT, "data/string/strings_" + lang.getShortName() + ".properties");
            if (!f.exists()) {
                warn("Not find file: " + f.getAbsolutePath());
                continue;
            }

            LineNumberReader reader = null;
            try {
                reader = new LineNumberReader(new FileReader(f));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#"))
                        continue;

                    StringTokenizer token = new StringTokenizer(line, "=");
                    if (token.countTokens() < 2) {
                        error("Error on line: " + line + "; file: " + f.getName());
                        continue;
                    }

                    String name = token.nextToken();
                    String value = token.nextToken();
                    while (token.hasMoreTokens())
                        value += "=" + token.nextToken();

                    Map<String, String> strings = _strings.get(lang);

                    strings.put(name, value);
                }
            } catch (Exception e) {
                error("Exception: " + e, e);
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }

        log();
    }

    public void reload() {
        clear();
        load();
    }

    @Override
    public void log() {
        for (Map.Entry<Language, Map<String, String>> entry : _strings.entrySet())
            info("load strings: " + entry.getValue().size() + " for lang: " + entry.getKey());
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {
        _strings.clear();
    }
}
