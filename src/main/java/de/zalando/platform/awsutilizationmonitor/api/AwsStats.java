package de.zalando.platform.awsutilizationmonitor.api;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

/**
 * @author jloeffler
 *
 */
public class AwsStats {
    
	private Hashtable<AwsResourceType, ArrayList<AwsResource>> resources = new Hashtable<AwsResourceType, ArrayList<AwsResource>>();
    
    /**
     * Add a new resource to statistics set.
     * 
     * @param res AWS resource to be added to statistics result set.
     */
    public void add(AwsResource res) {
    	if (res == null)
    		return;
    	
    	if (res.getResourceType() == null)
    		res.setResourceType(AwsResourceType.Unknown);
    	
    	ArrayList<AwsResource> list = this.resources.get(res.getResourceType()); 
    	if (list == null) {
    		list = new ArrayList<AwsResource> ();
    		list.add(res);
    		this.resources.put(res.getResourceType(), list);
    	} else {
    		list.add(res);    		
    	}    	
    }

	/**
     * Remove all collected information about resources.
     */
    public void clear() {
    	resources.clear();
    }

	/**
     * Generate some sample data to test application.
     * @param maxItems amount of test items inserted in collection.
     */
    public void generateSampleData(int maxItems) {
    	String[] appNames = new String[] {"FeedTheWorld", "EmpireStrikesBack", "Imperator", "HelloWorld", "ShoppingCart", "AwsChecker"};
        String[] owners = new String[] {"Mickey Mouse", "Max Mustermann", "Ironman", "Hackweek", "Superman", "Mr. Bond", "Overlord"};
        String[] regions = new String[] {"EU_WEST_1", "EU_WEST_2" };
        
        Hashtable<AwsResourceType, String> prefixes = new Hashtable<AwsResourceType, String>();
        prefixes.put(AwsResourceType.EC2, "app_");
        prefixes.put(AwsResourceType.SimpleDB, "db_");
        prefixes.put(AwsResourceType.RDS, "db_");
        prefixes.put(AwsResourceType.S3, "bucket_");
        prefixes.put(AwsResourceType.DynamoDB, "db_");
        prefixes.put(AwsResourceType.ElastiCache, "cache_");
        
        Random r = new Random();
        
    	for (int i = 0; i < maxItems ; i++) {
    		AwsResourceType resourceType = AwsResourceType.values()[r.nextInt(AwsResourceType.values().length - 1)];
    		String prefix = "";
    		if (prefixes.containsKey(resourceType)) {
    			prefix = prefixes.get(resourceType);
    		}
    		String appName = prefix + appNames[r.nextInt(appNames.length - 1)];
    		String owner = owners[r.nextInt(owners.length - 1)];
    		String region = regions[r.nextInt(regions.length - 1)];
    		
            add(new AwsResource( appName, owner, resourceType, region, "Sample data" ));
         }
    }

    /**
     * @return all resources in one list.
     */
    public AwsResource[] getAllResources() {
    	ArrayList<AwsResource> results = new ArrayList<AwsResource>();
    	
    	for (ArrayList<AwsResource> list : this.resources.values() ) {
    		results.addAll(list);
    	}
   	
    	return results.toArray(new AwsResource[results.size()]);
    }
 
    /**
     * Returns all owners sorted alphabetically.
     * 
     * @return sorted list of all owners.
     */
    public String[] getOwners() {
        AwsResource[] list = getAllResources();
    	ArrayList<String> results = new ArrayList<String>();

        for (AwsResource res : list) {
    		if (!results.contains(res.getOwner())) {
    			results.add(res.getOwner());
    		}
    	}
        
        results.sort(null);
   	
    	return results.toArray(new String[results.size()]);
    }
    
    /**
     * Returns all regions sorted alphabetically.
     * 
     * @return sorted list of all regions.
     */
    public String[] getRegions() {
        AwsResource[] list = getAllResources();
    	ArrayList<String> results = new ArrayList<String>();

        for (AwsResource res : list) {
    		if (!results.contains(res.getRegion())) {
    			results.add(res.getRegion());
    		}
    	}
        
        results.sort(null);
   	
    	return results.toArray(new String[results.size()]);
    }
    
    /**
     * Get the resource with the specified resource name.
     * 
     * @param resourceName Name of the resource that should be returned
     * @return Resource that matches the specified resource name. Returns null if resource cannot be found.
     */
    public AwsResource getResource(String resourceName) {
    	AwsResource[] list = getAllResources();
    	
        for (AwsResource res : list) {
    		if (res.getName().equalsIgnoreCase(resourceName)) {
    			return res;
    		}
    	}
    	
    	return null;
    }   
    
    /**
	 * @return the resources
	 */
	public Hashtable<AwsResourceType, ArrayList<AwsResource>> getResources() {
		return resources;
	}
    
    /**
     * @return all resources in one list for a given resource type.
     * 
     * @param resourceType resourceType e.g. EC2, S3, SimpleDB
     */
    public AwsResource[] getResources(AwsResourceType resourceType) {
    	ArrayList<AwsResource> results = this.resources.get(resourceType);
    	
    	if (results == null) {
    		return new AwsResource[0];
    	} else {
    		return results.toArray(new AwsResource[results.size()]);
    	}
    }

    /**
     * Get the resources of the specified owner.
     * 
     * @param ownerName Name of the owner
     * @return Resources that belong to the specified owner.
     */
    public AwsResource[] getResourcesByOwner(String ownerName) {
        AwsResource[] list = getAllResources();
        ArrayList<AwsResource> results = new ArrayList<AwsResource>();
    	
        for (AwsResource res : list) {
    		if (res.getOwner().equalsIgnoreCase(ownerName)) {
    			results.add(res);
    		}
    	}
    	
    	return results.toArray(new AwsResource[results.size()]);
    } 
    
    /**
     * Get the resources of the specified region.
     * 
     * @param region Name of the region e.g. "eu-west-1"
     * @return Resources that belong to the specified region.
     */
    public AwsResource[] getResourcesByRegion(String region) {
        AwsResource[] list = getAllResources();
        ArrayList<AwsResource> results = new ArrayList<AwsResource>();
    	
        for (AwsResource res : list) {
    		if (res.getRegion().equalsIgnoreCase(region)) {
    			results.add(res);
    		}
    	}
    	
    	return results.toArray(new AwsResource[results.size()]);
    } 
    
    /**
     * Returns all used resource types (e.g. EC2, S3) of used resources.
     * 
     * @return sorted list of all used resource types.
     */
    public AwsResourceType[] getUsedResourceTypes() {
    	ArrayList<AwsResourceType> results = new ArrayList<AwsResourceType>();

        //iterate through Hashtable keys Enumeration
        Enumeration<AwsResourceType> e = this.resources.keys();
        while(e.hasMoreElements())
          results.add(e.nextElement());
        
        results.sort(null);
   	
    	return results.toArray(new AwsResourceType[results.size()]);
    }
    
    /**
     * Searches all resources that match the specified pattern.
     * 
     * @param searchPattern pattern to search for
     * @return list of all matching resources
     */
    public AwsResource[] searchResource(String searchPattern) {
        AwsResource[] list = getAllResources();
        ArrayList<AwsResource> results = new ArrayList<AwsResource>();
        String s = searchPattern.toLowerCase();
        
        for (AwsResource res : list) {
    		if (res.getName().toLowerCase().contains(s)
    				|| res.getOwner().toLowerCase().contains(s)
    				|| res.getRegion().toLowerCase().contains(s)
    				|| res.getResourceType().toString().toLowerCase().contains(s)) {
    			results.add(res);
    		}
    	}
    	
    	return results.toArray(new AwsResource[results.size()]);
    }
    
    /**
	 * @param resources the resources to set
	 */
	public void setResources(
			Hashtable<AwsResourceType, ArrayList<AwsResource>> resources) {
		this.resources = resources;
	}    
}
