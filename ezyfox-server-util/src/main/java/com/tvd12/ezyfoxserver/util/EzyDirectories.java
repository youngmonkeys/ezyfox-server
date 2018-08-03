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
public class EzyDirectories {

    private File directory;
    
    public URL[] getURLs() throws IOException {
        return getURLs(getFiles(null));
    }
    
    public URL[] getURLs(String[] extensions) throws IOException {
    		return getURLs(extensions, true);
    }
    
    public URL[] getURLs(String[] extensions, boolean recursive) throws IOException {
    		return getURLs(getFiles(extensions, recursive));
    }
    
    public Collection<File> getFiles() {
    		return getFiles(null);
    	}
    
    public Collection<File> getFiles(String[] extensions) {
    		return getFiles(extensions, true);
    }
    
    public Collection<File> getFiles(String[] extensions, boolean recursive) {
        return FileUtils.listFiles(directory, extensions, recursive);
    }
    
    public String printTree( boolean printFile) {
    		return EzyFolderTreePrinter.builder()
    			.printFile(printFile)
    			.build()
    			.print(directory);
    }
    
    private URL[] getURLs(Collection<File> files) throws IOException {
        return FileUtils.toURLs(files.toArray(new File[files.size()]));
    }
    
    public EzyDirectories directory(File directory) {
        this.directory = directory;
        return this;
    }
    
    public EzyDirectories directory(String directoryPath) {
        return directory(new File(directoryPath));
    }
    
    @Override
    public String toString() {
    		return directory.toString();
    }
    
}
