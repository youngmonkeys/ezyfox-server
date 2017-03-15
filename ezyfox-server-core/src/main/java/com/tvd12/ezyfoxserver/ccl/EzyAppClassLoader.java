/**
 * 
 */
package com.tvd12.ezyfoxserver.ccl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.util.EzyEntryFiles;

import lombok.Getter;

/**
 * @author tavandung12
 *
 */
public class EzyAppClassLoader extends URLClassLoader {
    
    @Getter
    protected Logger logger;
    
    /**
     * @param urls
     * @param parent
     */
    public EzyAppClassLoader(File directory, ClassLoader parent) {
        super(getURLsByPath(directory), parent);
        this.initialize(directory);
    }
    
    private void initialize(File directory) {
        this.logger = LoggerFactory.getLogger(getClass());
    }
    
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        getLogger().debug("loadClass({})", name);
        return super.loadClass(name);
    }
    
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        getLogger().debug("findClass({})", name);
        return super.findClass(name);
    }
    
    /* (non-Javadoc)
     * @see java.net.URLClassLoader#findResource(java.lang.String)
     */
    @Override
    public URL findResource(String name) {
        getLogger().info("findResource({})", name);
        return super.findResource(name);
    }
    
    private static URL[] getURLsByPath(File directory) {
        try {
            return new EzyEntryFiles().directory(directory).getURLs();
        } catch (IOException e) {
            throw new IllegalStateException("can not load classes from path: " + directory, e);
        }
    }
    
}
