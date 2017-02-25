/**
 * 
 */
package com.tvd12.ezyfoxserver.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

/**
 * @author tavandung12
 *
 */
public class EzyEntryFiles {

    private File directory;
    
    public URL[] getURLs() throws IOException {
        return getURLs(getFiles());
    }
    
    private Collection<File> getFiles() {
        return FileUtils.listFiles(directory, null, true);
    }
    
    private URL[] getURLs(Collection<File> files) throws IOException {
        return FileUtils.toURLs(files.toArray(new File[files.size()]));
    }
    
    public EzyEntryFiles directory(File directory) {
        this.directory = directory;
        return this;
    }
    
}
