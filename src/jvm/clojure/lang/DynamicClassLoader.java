/**
 *   Copyright (c) Rich Hickey. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Common Public License 1.0 (http://opensource.org/licenses/cpl.php)
 *   which can be found in the file CPL.TXT at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

/* rich Aug 21, 2007 */

package clojure.lang;

import java.util.HashMap;
import java.net.URLClassLoader;
import java.net.URL;

//todo: possibly extend URLClassLoader?

public class DynamicClassLoader extends URLClassLoader{
HashMap<Integer, Object[]> constantVals = new HashMap<Integer, Object[]>();
HashMap<String, byte[]> map = new HashMap<String, byte[]>();
static final URL[] EMPTY_URLS = new URL[]{};

public DynamicClassLoader(){
	super(EMPTY_URLS,Thread.currentThread().getContextClassLoader());
	//super(Compiler.class.getClassLoader());
}

public DynamicClassLoader(ClassLoader parent){
	super(EMPTY_URLS,parent);
}

public Class defineClass(String name, byte[] bytes){
	return defineClass(name, bytes, 0, bytes.length);
}

public void addBytecode(String className, byte[] bytes){
	if(map.containsKey(className))
		throw new IllegalStateException(String.format("Class %s already present", className));
	map.put(className, bytes);
}

protected Class<?> findClass(String name) throws ClassNotFoundException{
	byte[] bytes = map.get(name);
	if(bytes != null)
		return defineClass(name, bytes, 0, bytes.length);
	return super.findClass(name);
	//throw new ClassNotFoundException(name);
}

public void registerConstants(int id, Object[] val){
	constantVals.put(id, val);
}

public Object[] getConstants(int id){
	return constantVals.get(id);
}

public void addURL(URL url){
	super.addURL(url);
}

}
