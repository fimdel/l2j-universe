/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package l2p.commons.crypt;

import org.apache.commons.io.FileUtils;

import java.io.*;

class Datapack
{
	private String _dpPath;
	private String _cryptPath;
	private long _crypted = 0;
	private boolean _excludeMode = false;

	private static final String[] EXCLUDES = {"events", "geo", "other", "scripts", "services", "html-ru", "html-en", "xsd", "droplist"};
	
	public static void main(String[] s)
	{
		try
		{
			if (s.length < 2)
			{
				System.out.println("Usage: Datapack {DP_PATH} {CRYPT_PATH}\nWhere {DP_PATH is where datapack files are, and {CRYPT_PATH} is where crypted files will be placed.");
				return;
			}
			Datapack dp = new Datapack(s[0], s[1]);
			dp.crypt();
		}
		catch(Exception e)
		{}
	}
	
	Datapack(String dpPath, String cryptPath)
	{
		if (!dpPath.endsWith("/") && !dpPath.endsWith("\\"))
			dpPath += '/';
		if (!cryptPath.endsWith("/") && !cryptPath.endsWith("\\"))
			cryptPath += '/';
		_dpPath = dpPath;
		_cryptPath = cryptPath;
	}
	
	private void crypt()
	{
		try
		{
			System.out.println("Deleeting directory...");
			FileUtils.deleteDirectory(new File(_cryptPath));
			System.out.println("Crypting...");
			crypt(_dpPath);
			System.out.println("Crypted " + _crypted + " files...");
		}
		catch(Exception e)
		{}
	}

	private void crypt(String path)
	{
		if (!path.endsWith("/"))
			path += "/";
		
		File dir = new File(path);
		
		if (dir == null)
			return;
		
		File[] files = dir.listFiles();
		
		if (files == null)
			return;

		for (File file : files)
		{
			// Recursive parsing
			if (file.isDirectory())
			{
				boolean localExclude = false;
				for (String exclude : EXCLUDES)
				{
					if (exclude.equalsIgnoreCase(file.getName()))
					{
						_excludeMode = true;
						localExclude = true;
						break;
					}
				}
				crypt(path + file.getName());
				if (localExclude)
					_excludeMode = false;
			}
			// Allow to crypt only HTML & XML files
			else if (!_excludeMode && file.isFile() && (file.getName().endsWith(".xml")) || !_excludeMode && file.isFile() && (file.getName().endsWith(".htm")))
			{
				++_crypted;
				FileOutputStream output;
				try
				{
					FileInputStream input = new FileInputStream(file);
					(new File(_cryptPath + path.substring(_dpPath.length()))).mkdirs();
					output = new FileOutputStream(_cryptPath + path.substring(_dpPath.length()) + file.getName(), false);
					output.write((byte)0x00);
					CryptUtil.encrypt(input, output);
					input.close();
					output.close();
				}
				catch (Exception e)
				{
					System.out.println("Error during crypting file: " + e);
					System.exit(1);
				}
				if (_crypted % 1000 == 0)
					System.out.println("Crypted " + _crypted + " files...");
			}
			else
			{
				try
				{
					(new File(_cryptPath + path.substring(_dpPath.length()))).mkdirs();
					FileInputStream input = new FileInputStream(file);
					FileOutputStream output = new FileOutputStream(_cryptPath + path.substring(_dpPath.length()) + file.getName(), false);
					byte[] buffer = new byte[1024];
					int num;
					while ((num = input.read(buffer)) >= 0)
						output.write(buffer, 0, num);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}
}
