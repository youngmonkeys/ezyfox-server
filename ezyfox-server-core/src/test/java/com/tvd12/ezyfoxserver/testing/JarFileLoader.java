package com.tvd12.ezyfoxserver.testing;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
public class JarFileLoader extends URLClassLoader
{
    public JarFileLoader (URL[] urls)
    {
        super (urls);
    }
    public void addFile (String path) throws MalformedURLException
    {
        String urlPath = "jar:file://" + path + "!/";
        addURL (new URL (urlPath));
    }
    @SuppressWarnings("resource")
	public static void main (String args [])
    {
        try
        {
            System.out.println ("First attempt...");
            Class.forName ("com.tvd12.example.extension.VideoPokerAppEntryLoader");
        }
        catch (Exception ex)
        {
            System.out.println ("Failed.");
        }
        try
        {
            URL urls [] = {};
            JarFileLoader cl = new JarFileLoader (urls);
            cl.addFile ("/Users/tavandung12/Documents/tvd12/java/deploy/ezyfox-server/apps/entries/videopoker/example-extension-0.0.1-SNAPSHOT.jar");
            System.out.println ("Second attempt...");
            cl.loadClass ("com.tvd12.example.extension.VideoPokerAppEntryLoader");
            System.out.println ("Success!");
        }
        catch (Exception ex)
        {
            System.out.println ("Failed.");
            ex.printStackTrace ();
        }
    }
}