package l2p.gameserver.scripts;

import l2p.commons.compiler.Compiler;
import l2p.commons.compiler.MemoryClassLoader;
import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Scripts {
    public static final Map<Integer, List<ScriptClassAndMethod>> dialogAppends = new HashMap<Integer, List<ScriptClassAndMethod>>();
    public static final Map<String, ScriptClassAndMethod> onAction = new HashMap<String, ScriptClassAndMethod>();
    public static final Map<String, ScriptClassAndMethod> onActionShift = new HashMap<String, ScriptClassAndMethod>();
    private static final Logger _log = LoggerFactory.getLogger(Scripts.class);
    private static final Scripts _instance = new Scripts();
    private final Compiler compiler = new Compiler();
    private final Map<String, Class<?>> _classes = new TreeMap<String, Class<?>>();

    private Scripts() {
        load();
    }

    public static final Scripts getInstance() {
        return _instance;
    }

    /**
     * Вызывается при загрузке сервера. Загрузает все скрипты в data/scripts. Не инициирует объекты и обработчики.
     *
     * @return true, если загрузка прошла успешно
     */
    private void load() {
        _log.info("Scripts: Loading...");

        List<Class<?>> classes = new ArrayList<Class<?>>();

        boolean result = false;

        /*File f = new File("scripts.jar");
          if (f.exists()) {
              JarInputStream stream = null;
              try {
                  stream = new JarInputStream(new FileInputStream(f));
                  JarEntry entry = null;
                  while ((entry = stream.getNextJarEntry()) != null) {
                      // Вложенные класс
                      if (entry.getName().contains(ClassUtils.INNER_CLASS_SEPARATOR) || !entry.getName().endsWith(".class"))
                          continue;

                      String name = entry.getName().replace(".class", "").replace("/", ".");

                      Class<?> clazz = Class.forName(name);
                      if (Modifier.isAbstract(clazz.getModifiers()))
                          continue;
                      classes.add(clazz);
                  }
                  result = true;
              } catch (Exception e) {
                  _log.error("Fail to load scripts.jar!", e);
                  classes.clear();
              } finally {
                  IOUtils.closeQuietly(stream);
              }
          }

          if (!result) */
        result = load(classes, "", false);

        if (!result) {
            _log.error("Scripts: Failed loading scripts!");
            Runtime.getRuntime().exit(0);
            return;
        }

        _log.info("Scripts: Loaded " + classes.size() + " classes.");

        Class<?> clazz;
        for (int i = 0; i < classes.size(); i++) {
            clazz = classes.get(i);
            _classes.put(clazz.getName(), clazz);
        }
    }

    /**
     * Вызывается при загрузке сервера. Инициализирует объекты и обработчики.
     */
    public void init() {
        for (Class<?> clazz : _classes.values()) {
            addHandlers(clazz);

            if (Config.DONTLOADQUEST)
                if (ClassUtils.isAssignable(clazz, Quest.class))
                    continue;

            if (ClassUtils.isAssignable(clazz, ScriptFile.class))
                try {
                    ((ScriptFile) clazz.newInstance()).onLoad();
                } catch (Exception e) {
                    _log.error("Scripts: Failed running " + clazz.getName() + ".onLoad()", e);
                }
        }
    }

    /**
     * Перезагрузить все скрипты в data/scripts
     *
     * @return true, если скрипты перезагружены успешно
     */
    public boolean reload() {
        _log.info("Scripts: Reloading...");

        return reload("");
    }

    /**
     * Перезагрузить все скрипты в data/scripts/target
     *
     * @param target путь до класса, или каталога со скриптами
     * @return true, если скрипты перезагружены успешно
     */
    public boolean reload(String target) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        if (load(classes, target, true))
            _log.info("Scripts: Reloaded " + classes.size() + " classes.");
        else {
            _log.error("Scripts: Failed reloading script(s): " + target + "!");
            return false;
        }

        Class<?> clazz, prevClazz;
        for (int i = 0; i < classes.size(); i++) {
            clazz = classes.get(i);

            prevClazz = _classes.put(clazz.getName(), clazz);
            if (prevClazz != null) {
                if (ClassUtils.isAssignable(prevClazz, ScriptFile.class))
                    try {
                        ((ScriptFile) prevClazz.newInstance()).onReload();
                    } catch (Exception e) {
                        _log.error("Scripts: Failed running " + prevClazz.getName() + ".onReload()", e);
                    }

                removeHandlers(prevClazz);
            }

            if (Config.DONTLOADQUEST)
                if (ClassUtils.isAssignable(clazz, Quest.class))
                    continue;

            if (ClassUtils.isAssignable(clazz, ScriptFile.class))
                try {
                    ((ScriptFile) clazz.newInstance()).onLoad();
                } catch (Exception e) {
                    _log.error("Scripts: Failed running " + clazz.getName() + ".onLoad()", e);
                }

            addHandlers(clazz);
        }

        return true;
    }

    /**
     * Вызывается при завершении работы сервера
     */
    public void shutdown() {
        for (Class<?> clazz : _classes.values()) {
            if (ClassUtils.isAssignable(clazz, Quest.class))
                continue;

            if (ClassUtils.isAssignable(clazz, ScriptFile.class))
                try {
                    ((ScriptFile) clazz.newInstance()).onShutdown();
                } catch (Exception e) {
                    _log.error("Scripts: Failed running " + clazz.getName() + ".onShutdown()", e);
                }
        }
    }

    /**
     * Перезагрузить все скрипты в data/scripts/target
     *
     * @param classes , для загруженных скриптов
     * @param target  путь до класса, или каталога со скриптами
     * @return true, если загрузка прошла успешно
     */
    private boolean load(List<Class<?>> classes, String target, boolean reload) {
        Collection<File> scriptFiles = Collections.emptyList();

        File file = new File(Config.DATAPACK_ROOT, "data/scripts/" + target.replace(".", "/") + ".java");
        if (file.isFile()) {
            scriptFiles = new ArrayList<File>(1);
            scriptFiles.add(file);
        } else {
            file = new File(Config.DATAPACK_ROOT, "data/scripts/" + target);
            if (file.isDirectory())
                scriptFiles = FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter(".java"), FileFilterUtils.directoryFileFilter());
        }

        if (scriptFiles.isEmpty())
            return false;

        if (!reload) {
            ClassLoader cl = getClass().getClassLoader();
            for (Iterator<File> it = scriptFiles.iterator(); it.hasNext(); ) {
                File scriptFile = it.next();
                String className = scriptFile.getPath().substring(file.getPath().length() + 1, scriptFile.getPath().length() - 5).replaceAll("\\\\", ".");
                Class z = null;
                try {
                    z = cl.loadClass(className);
                } catch (ClassNotFoundException ignored) {
                }

                if (z != null) {
                    if (Modifier.isAbstract(z.getModifiers()))
                        continue;
                    classes.add(z);
                    it.remove();
                }
            }
        }

        Class<?> clazz;
        boolean success;

        if (success = compiler.compile(scriptFiles)) {
            MemoryClassLoader classLoader = compiler.getClassLoader();
            for (String name : classLoader.getLoadedClasses()) {
                // Вложенные класс
                if (name.contains(ClassUtils.INNER_CLASS_SEPARATOR))
                    continue;

                try {
                    clazz = classLoader.loadClass(name);
                    if (Modifier.isAbstract(clazz.getModifiers()))
                        continue;
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    success = false;
                    _log.error("Scripts: Can't load script class: " + name, e);
                }
            }
            classLoader.clear();
        }

        return success;
    }

    private void addHandlers(Class<?> clazz) {
        try {
            for (Method method : clazz.getMethods())
                if (method.getName().contains("DialogAppend_")) {
                    Integer id = Integer.parseInt(method.getName().substring(13));
                    List<ScriptClassAndMethod> handlers = dialogAppends.get(id);
                    if (handlers == null) {
                        handlers = new ArrayList<ScriptClassAndMethod>();
                        dialogAppends.put(id, handlers);
                    }
                    handlers.add(new ScriptClassAndMethod(clazz.getName(), method.getName()));
                } else if (method.getName().contains("OnAction_")) {
                    String name = method.getName().substring(9);
                    onAction.put(name, new ScriptClassAndMethod(clazz.getName(), method.getName()));
                } else if (method.getName().contains("OnActionShift_")) {
                    String name = method.getName().substring(14);
                    onActionShift.put(name, new ScriptClassAndMethod(clazz.getName(), method.getName()));
                }
        } catch (Exception e) {
            _log.error("", e);
        }
    }

    private void removeHandlers(Class<?> script) {
        try {
            for (List<ScriptClassAndMethod> entry : dialogAppends.values()) {
                List<ScriptClassAndMethod> toRemove = new ArrayList<ScriptClassAndMethod>();
                for (ScriptClassAndMethod sc : entry)
                    if (sc.className.equals(script.getName()))
                        toRemove.add(sc);
                for (ScriptClassAndMethod sc : toRemove)
                    entry.remove(sc);
            }

            List<String> toRemove = new ArrayList<String>();
            for (Map.Entry<String, ScriptClassAndMethod> entry : onAction.entrySet())
                if (entry.getValue().className.equals(script.getName()))
                    toRemove.add(entry.getKey());
            for (String key : toRemove)
                onAction.remove(key);

            toRemove = new ArrayList<String>();
            for (Map.Entry<String, ScriptClassAndMethod> entry : onActionShift.entrySet())
                if (entry.getValue().className.equals(script.getName()))
                    toRemove.add(entry.getKey());
            for (String key : toRemove)
                onActionShift.remove(key);
        } catch (Exception e) {
            _log.error("", e);
        }
    }

    public Object callScripts(String className, String methodName) {
        return callScripts(null, className, methodName, null, null);
    }

    public Object callScripts(String className, String methodName, Object[] args) {
        return callScripts(null, className, methodName, args, null);
    }

    public Object callScripts(String className, String methodName, Map<String, Object> variables) {
        return callScripts(null, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, variables);
    }

    public Object callScripts(String className, String methodName, Object[] args, Map<String, Object> variables) {
        return callScripts(null, className, methodName, args, variables);
    }

    public Object callScripts(Player caller, String className, String methodName) {
        return callScripts(caller, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public Object callScripts(Player caller, String className, String methodName, Object[] args) {
        return callScripts(caller, className, methodName, args, null);
    }

    public Object callScripts(Player caller, String className, String methodName, Map<String, Object> variables) {
        return callScripts(caller, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, variables);
    }

    public Object callScripts(Player caller, String className, String methodName, Object[] args, Map<String, Object> variables) {
        Object o;
        Class<?> clazz;

        clazz = _classes.get(className);
        if (clazz == null) {
            _log.error("Script class " + className + " not found!");
            return null;
        }

        try {
            o = clazz.newInstance();
        } catch (Exception e) {
            _log.error("Scripts: Failed creating instance of " + clazz.getName(), e);
            return null;
        }

        if (variables != null && !variables.isEmpty())
            for (Map.Entry<String, Object> param : variables.entrySet())
                try {
                    FieldUtils.writeField(o, param.getKey(), param.getValue());
                } catch (Exception e) {
                    _log.error("Scripts: Failed setting fields for " + clazz.getName(), e);
                }

        if (caller != null)
            try {
                Field field = null;
                if ((field = FieldUtils.getField(clazz, "self")) != null)
                    FieldUtils.writeField(field, o, caller.getRef());
            } catch (Exception e) {
                _log.error("Scripts: Failed setting field for " + clazz.getName(), e);
            }

        Object ret = null;
        try {
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++)
                parameterTypes[i] = args[i] != null ? args[i].getClass() : null;

            ret = MethodUtils.invokeMethod(o, methodName, args, parameterTypes);
        } catch (NoSuchMethodException nsme) {
            _log.error("Scripts: No such method " + clazz.getName() + "." + methodName + "()!");
        } catch (InvocationTargetException ite) {
            _log.error("Scripts: Error while calling " + clazz.getName() + "." + methodName + "()", ite.getTargetException());
        } catch (Exception e) {
            _log.error("Scripts: Failed calling " + clazz.getName() + "." + methodName + "()", e);
        }

        return ret;
    }

    public Map<String, Class<?>> getClasses() {
        return _classes;
    }

    public static class ScriptClassAndMethod {
        public final String className;
        public final String methodName;

        public ScriptClassAndMethod(String className, String methodName) {
            this.className = className;
            this.methodName = methodName;
        }
    }
}
