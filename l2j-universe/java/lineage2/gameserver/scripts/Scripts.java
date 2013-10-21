/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import lineage2.commons.compiler.Compiler;
import lineage2.commons.compiler.MemoryClassLoader;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.Quest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Scripts
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Scripts.class);
	
	/**
	 * Field _instance.
	 */
	private static final Scripts _instance = new Scripts();
	
	/**
	 * Method getInstance.
	 * @return Scripts
	 */
	public static final Scripts getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field dialogAppends.
	 */
	public static final Map<Integer, List<ScriptClassAndMethod>> dialogAppends = new HashMap<>();
	/**
	 * Field onAction.
	 */
	public static final Map<String, ScriptClassAndMethod> onAction = new HashMap<>();
	/**
	 * Field onActionShift.
	 */
	public static final Map<String, ScriptClassAndMethod> onActionShift = new HashMap<>();
	
	/**
	 * Field compiler.
	 */
	private final Compiler compiler = new Compiler();
	/**
	 * Field _classes.
	 */
	private final Map<String, Class<?>> _classes = new TreeMap<>();
	
	/**
	 * Constructor for Scripts.
	 */
	private Scripts()
	{
		load();
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		_log.info("Scripts: Loading...");
		
		List<Class<?>> classes = new ArrayList<>();
		
		boolean result = false;
		
		File f = new File("../libs/lineage2-scripts.jar");
		if (f.exists())
		{
			JarInputStream stream = null;
			try
			{
				stream = new JarInputStream(new FileInputStream(f));
				JarEntry entry = null;
				while ((entry = stream.getNextJarEntry()) != null)
				{
					if (entry.getName().contains(ClassUtils.INNER_CLASS_SEPARATOR) || !entry.getName().endsWith(".class"))
					{
						continue;
					}
					
					String name = entry.getName().replace(".class", "").replace("/", ".");
					
					Class<?> clazz = Class.forName(name);
					if (Modifier.isAbstract(clazz.getModifiers()))
					{
						continue;
					}
					classes.add(clazz);
				}
				result = true;
			}
			catch (Exception e)
			{
				_log.error("Fail to load scripts.jar!", e);
				classes.clear();
			}
			finally
			{
				IOUtils.closeQuietly(stream);
			}
		}
		
		if (!result)
		{
			result = load(classes, "");
		}
		
		if (!result)
		{
			_log.error("Scripts: Failed loading scripts!");
			Runtime.getRuntime().exit(0);
			return;
		}
		
		_log.info("Scripts: Loaded " + classes.size() + " classes.");
		
		Class<?> clazz;
		for (int i = 0; i < classes.size(); i++)
		{
			clazz = classes.get(i);
			_classes.put(clazz.getName(), clazz);
		}
	}
	
	/**
	 * Method init.
	 */
	public void init()
	{
		for (Class<?> clazz : _classes.values())
		{
			addHandlers(clazz);
			
			if (Config.DONTLOADQUEST)
			{
				if (ClassUtils.isAssignable(clazz, Quest.class))
				{
					continue;
				}
			}
			
			if (ClassUtils.isAssignable(clazz, ScriptFile.class))
			{
				try
				{
					((ScriptFile) clazz.newInstance()).onLoad();
				}
				catch (Exception e)
				{
					_log.error("Scripts: Failed running " + clazz.getName() + ".onLoad()", e);
				}
			}
		}
	}
	
	/**
	 * Method reload.
	 * @return boolean
	 */
	public boolean reload()
	{
		_log.info("Scripts: Reloading...");
		
		return reload("");
	}
	
	/**
	 * Method reload.
	 * @param target String
	 * @return boolean
	 */
	public boolean reload(String target)
	{
		List<Class<?>> classes = new ArrayList<>();
		
		if (load(classes, target))
		{
			_log.info("Scripts: Reloaded " + classes.size() + " classes.");
		}
		else
		{
			_log.error("Scripts: Failed reloading script(s): " + target + "!");
			return false;
		}
		
		Class<?> clazz, prevClazz;
		for (int i = 0; i < classes.size(); i++)
		{
			clazz = classes.get(i);
			
			prevClazz = _classes.put(clazz.getName(), clazz);
			if (prevClazz != null)
			{
				if (ClassUtils.isAssignable(prevClazz, ScriptFile.class))
				{
					try
					{
						((ScriptFile) prevClazz.newInstance()).onReload();
					}
					catch (Exception e)
					{
						_log.error("Scripts: Failed running " + prevClazz.getName() + ".onReload()", e);
					}
				}
				
				removeHandlers(prevClazz);
			}
			
			if (Config.DONTLOADQUEST)
			{
				if (ClassUtils.isAssignable(clazz, Quest.class))
				{
					continue;
				}
			}
			
			if (ClassUtils.isAssignable(clazz, ScriptFile.class))
			{
				try
				{
					((ScriptFile) clazz.newInstance()).onLoad();
				}
				catch (Exception e)
				{
					_log.error("Scripts: Failed running " + clazz.getName() + ".onLoad()", e);
				}
			}
			
			addHandlers(clazz);
		}
		
		return true;
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		for (Class<?> clazz : _classes.values())
		{
			if (ClassUtils.isAssignable(clazz, Quest.class))
			{
				continue;
			}
			
			if (ClassUtils.isAssignable(clazz, ScriptFile.class))
			{
				try
				{
					((ScriptFile) clazz.newInstance()).onShutdown();
				}
				catch (Exception e)
				{
					_log.error("Scripts: Failed running " + clazz.getName() + ".onShutdown()", e);
				}
			}
		}
	}
	
	/**
	 * Method load.
	 * @param classes List<Class<?>>
	 * @param target String
	 * @return boolean
	 */
	private boolean load(List<Class<?>> classes, String target)
	{
		Collection<File> scriptFiles = Collections.emptyList();
		
		File file = new File(Config.DATAPACK_ROOT, "data/scripts/" + target.replace(".", "/") + ".java");
		if (file.isFile())
		{
			scriptFiles = new ArrayList<>(1);
			scriptFiles.add(file);
		}
		else
		{
			file = new File(Config.DATAPACK_ROOT, "data/scripts/" + target);
			if (file.isDirectory())
			{
				scriptFiles = FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter(".java"), FileFilterUtils.directoryFileFilter());
			}
		}
		
		if (scriptFiles.isEmpty())
		{
			return false;
		}
		
		Class<?> clazz;
		boolean success = compiler.compile(scriptFiles);
		if (success)
		{
			MemoryClassLoader classLoader = compiler.getClassLoader();
			for (String name : classLoader.getLoadedClasses())
			{
				if (name.contains(ClassUtils.INNER_CLASS_SEPARATOR))
				{
					continue;
				}
				
				try
				{
					clazz = classLoader.loadClass(name);
					if (Modifier.isAbstract(clazz.getModifiers()))
					{
						continue;
					}
					classes.add(clazz);
				}
				catch (ClassNotFoundException e)
				{
					success = false;
					_log.error("Scripts: Can't load script class: " + name, e);
				}
			}
			classLoader.clear();
		}
		
		return success;
	}
	
	/**
	 * Method addHandlers.
	 * @param clazz Class<?>
	 */
	private void addHandlers(Class<?> clazz)
	{
		try
		{
			for (Method method : clazz.getMethods())
			{
				if (method.getName().contains("DialogAppend_"))
				{
					Integer id = Integer.parseInt(method.getName().substring(13));
					List<ScriptClassAndMethod> handlers = dialogAppends.get(id);
					if (handlers == null)
					{
						handlers = new ArrayList<>();
						dialogAppends.put(id, handlers);
					}
					handlers.add(new ScriptClassAndMethod(clazz.getName(), method.getName()));
				}
				else if (method.getName().contains("OnAction_"))
				{
					String name = method.getName().substring(9);
					onAction.put(name, new ScriptClassAndMethod(clazz.getName(), method.getName()));
				}
				else if (method.getName().contains("OnActionShift_"))
				{
					String name = method.getName().substring(14);
					onActionShift.put(name, new ScriptClassAndMethod(clazz.getName(), method.getName()));
				}
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method removeHandlers.
	 * @param script Class<?>
	 */
	private void removeHandlers(Class<?> script)
	{
		try
		{
			for (List<ScriptClassAndMethod> entry : dialogAppends.values())
			{
				List<ScriptClassAndMethod> toRemove = new ArrayList<>();
				for (ScriptClassAndMethod sc : entry)
				{
					if (sc.className.equals(script.getName()))
					{
						toRemove.add(sc);
					}
				}
				for (ScriptClassAndMethod sc : toRemove)
				{
					entry.remove(sc);
				}
			}
			
			List<String> toRemove = new ArrayList<>();
			for (Map.Entry<String, ScriptClassAndMethod> entry : onAction.entrySet())
			{
				if (entry.getValue().className.equals(script.getName()))
				{
					toRemove.add(entry.getKey());
				}
			}
			for (String key : toRemove)
			{
				onAction.remove(key);
			}
			
			toRemove = new ArrayList<>();
			for (Map.Entry<String, ScriptClassAndMethod> entry : onActionShift.entrySet())
			{
				if (entry.getValue().className.equals(script.getName()))
				{
					toRemove.add(entry.getKey());
				}
			}
			for (String key : toRemove)
			{
				onActionShift.remove(key);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @return Object
	 */
	public Object callScripts(String className, String methodName)
	{
		return callScripts(null, className, methodName, null, null);
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @return Object
	 */
	public Object callScripts(String className, String methodName, Object[] args)
	{
		return callScripts(null, className, methodName, args, null);
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public Object callScripts(String className, String methodName, Map<String, Object> variables)
	{
		return callScripts(null, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, variables);
	}
	
	/**
	 * Method callScripts.
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public Object callScripts(String className, String methodName, Object[] args, Map<String, Object> variables)
	{
		return callScripts(null, className, methodName, args, variables);
	}
	
	/**
	 * Method callScripts.
	 * @param caller Player
	 * @param className String
	 * @param methodName String
	 * @return Object
	 */
	public Object callScripts(Player caller, String className, String methodName)
	{
		return callScripts(caller, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
	}
	
	/**
	 * Method callScripts.
	 * @param caller Player
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @return Object
	 */
	public Object callScripts(Player caller, String className, String methodName, Object[] args)
	{
		return callScripts(caller, className, methodName, args, null);
	}
	
	/**
	 * Method callScripts.
	 * @param caller Player
	 * @param className String
	 * @param methodName String
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public Object callScripts(Player caller, String className, String methodName, Map<String, Object> variables)
	{
		return callScripts(caller, className, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, variables);
	}
	
	/**
	 * Method callScripts.
	 * @param caller Player
	 * @param className String
	 * @param methodName String
	 * @param args Object[]
	 * @param variables Map<String,Object>
	 * @return Object
	 */
	public Object callScripts(Player caller, String className, String methodName, Object[] args, Map<String, Object> variables)
	{
		Object o;
		Class<?> clazz;
		
		clazz = _classes.get(className);
		if (clazz == null)
		{
			_log.error("Script class " + className + " not found!");
			return null;
		}
		
		try
		{
			o = clazz.newInstance();
		}
		catch (Exception e)
		{
			_log.error("Scripts: Failed creating instance of " + clazz.getName(), e);
			return null;
		}
		
		if ((variables != null) && !variables.isEmpty())
		{
			for (Map.Entry<String, Object> param : variables.entrySet())
			{
				try
				{
					FieldUtils.writeField(o, param.getKey(), param.getValue());
				}
				catch (Exception e)
				{
					_log.error("Scripts: Failed setting fields for " + clazz.getName(), e);
				}
			}
		}
		
		if (caller != null)
		{
			try
			{
				Field field = null;
				if ((field = FieldUtils.getField(clazz, "self")) != null)
				{
					FieldUtils.writeField(field, o, caller.getRef());
				}
			}
			catch (Exception e)
			{
				_log.error("Scripts: Failed setting field for " + clazz.getName(), e);
			}
		}
		
		Object ret = null;
		try
		{
			Class<?>[] parameterTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++)
			{
				parameterTypes[i] = args[i] != null ? args[i].getClass() : null;
			}
			
			ret = MethodUtils.invokeMethod(o, methodName, args, parameterTypes);
		}
		catch (NoSuchMethodException nsme)
		{
			_log.error("Scripts: No such method " + clazz.getName() + "." + methodName + "()!");
		}
		catch (InvocationTargetException ite)
		{
			_log.error("Scripts: Error while calling " + clazz.getName() + "." + methodName + "()", ite.getTargetException());
		}
		catch (Exception e)
		{
			_log.error("Scripts: Failed calling " + clazz.getName() + "." + methodName + "()", e);
		}
		
		return ret;
	}
	
	/**
	 * Method getClasses.
	 * @return Map<String,Class<?>>
	 */
	public Map<String, Class<?>> getClasses()
	{
		return _classes;
	}
	
	/**
	 * @author Mobius
	 */
	public static class ScriptClassAndMethod
	{
		/**
		 * Field className.
		 */
		public final String className;
		/**
		 * Field methodName.
		 */
		public final String methodName;
		
		/**
		 * Constructor for ScriptClassAndMethod.
		 * @param className String
		 * @param methodName String
		 */
		public ScriptClassAndMethod(String className, String methodName)
		{
			this.className = className;
			this.methodName = methodName;
		}
	}
}