package com.soundcloud.bananiser;

import java.lang.reflect.Constructor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import com.soundcloud.bananiser.utilities.BananaUtility;

public class BananaJobs {
    private static final Class<?>[] TOOL_CONSTRUCTOR_SIGNATURE = new Class<?>[] { String[].class };
    private final Configuration config;

    public BananaJobs(Configuration config) {
        this.config = config;
    }

    public Job from(String[] args) {
        String className = args[0].substring(0, 1).toUpperCase()
                + args[0].substring(1);
        String utilitiesPackageName = this.getClass().getPackage().getName()
                + "." + "utilities";
        String utilityFqn = utilitiesPackageName + "." + className + "Utility";
        BananaUtility tool = instantiateTool(utilityFqn, args);
        return tool.createJob(config);
    }

    @SuppressWarnings("unchecked")
    private BananaUtility instantiateTool(String toolFqn, String[] args) {

        try {
            Class<? extends BananaUtility> toolClass = (Class<? extends BananaUtility>) Class
                    .forName(toolFqn);

            Constructor<? extends BananaUtility> constructor = toolClass
                    .getConstructor(TOOL_CONSTRUCTOR_SIGNATURE);
            BananaUtility tool = constructor.newInstance(new Object[] { args });
            return tool;

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
