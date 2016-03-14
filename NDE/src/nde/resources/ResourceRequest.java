/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nde.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Emperor
 */
public class ResourceRequest {

    /**
     * A {@code Map} containing all the loaded resources.
     */
    private Map<String, String> resources = new HashMap<String, String>();

    /**
     * The resource file.
     */
    private File resource;

    /**
     * Constructs a new ResourceRequest instance.
     * @param file The resource file to load from.
     */
    public ResourceRequest(File file) {
       this.resource = file;
    }

    /**
     * Initializes the resource request {@code Map}.
     */
    public void init() throws FileNotFoundException, IOException {
        if (!resource.exists()) {
            throw new RuntimeException("The requested resource file did not exist!");
        }
        BufferedReader fr = new BufferedReader(new FileReader(resource));
        String s;
        while ((s = fr.readLine()) != null) {
            if (s.startsWith("#")) {
                continue;
            }
            try {
                String[] mappings = s.split("=");
                if (mappings.length == 1) {
                    continue;
                }
                resources.put(mappings[0], mappings[1]);
            } catch (Exception e) {
                e.printStackTrace();
                //Logger.getLogger(ResourceRequest.class.getName(), "Exception thrown: " + e);
            }
        }
    }

    /**
     * Gets a string from the currently loaded resource file.
     * @param key The key string.
     * @return The resource value string.
     */
    public String getResource(String key) {
        return resources.get(key);
    }

    public void setResource(String key, String value) {
        resources.put(key, value);
    }
}
