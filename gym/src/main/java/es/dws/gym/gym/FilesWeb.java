package es.dws.gym.gym;

/**
 * This class defines constants representing file paths used for storing
 * and retrieving data related to users, reviews, memberships, and classes.
 */
public class FilesWeb {

    // File path for storing user data as a map
    public static final String USERSMAPFILE = "files/userMap.json";

    // File path for storing review data as a map
    public static final String REVIEWMAPFILE = "files/reviewMap.json";

    // File path for storing the review ID counter
    public static final String REVIEWIDCONTFILE = "files/reviewContId.json";

    // File path for storing membership data as a map
    public static final String MEMBRERSHIPSMAPFILE = "files/membrershipsMap.json";

    // File path for storing the membership ID counter
    public static final String MEMBRERSHIPSIDFILE = "files/membrershipsID.json";

    // File path for storing gym class data as a map
    public static final String CLASSMAPFILE = "files/classMap.json";

    // File path for storing the gym class ID counter (note: possible typo in "file/")
    public static final String CLASSIDFILE = "file/classID.json"; // Should this be "files/classID.json"?
}

