/**
 * 
 */
package com.tvd12.ezyfoxserver.ccl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfox.util.EzyDirectories;

/**
 * @author tavandung12
 *
 */
public class EzyAppClassLoader extends URLClassLoader {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * @param urls
     * @param parent
     */
    public EzyAppClassLoader(File directory, ClassLoader parent) {
        super(getURLsByPath(directory), parent);
    }
    
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        logger.debug("loadClass({})", name);
        return super.loadClass(name);
    }
    
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        logger.debug("findClass({})", name);
        return super.findClass(name);
    }
    
    /* (non-Javadoc)
     * @see java.net.URLClassLoader#findResource(java.lang.String)
     */
    @Override
    public URL findResource(String name) {
        logger.info("findResource({})", name);
        return super.findResource(name);
    }
    
    private static URL[] getURLsByPath(File directory) {
        return getURLsByPath(new EzyDirectories().directory(directory));
    }
    
    private static URL[] getURLsByPath(EzyDirectories directories) {
        try {
            return directories.getURLs();
        } catch (Exception e) {
            throw new IllegalStateException("can not load classes from path: " + directories, e);
        }
    }
}
