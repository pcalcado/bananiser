package com.soundcloud.bananiser;

import java.lang.reflect.Constructor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import com.soundcloud.bananiser.mr.utilities.BananaUtility;

public class BananaJobs {
    private static final Class<?>[] TOOL_CONSTRUCTOR_SIGNATURE = new Class<?>[] { String[].class };
    private final Configuration config;

    public BananaJobs(Configuration config) {
        this.config = config;
    }

    public Job from(String[] args) {
        BananaUtility tool = instantiateTool(utiilityClassFqn(args), args);
        return tool.createJob(config);
    }

    private String utiilityClassFqn(String[] args) {
        String utilityName = args[0].toLowerCase();
        String myPackage = this.getClass().getPackage().getName();
        String utilitiesPackageName = String.format("%s.mr.utilities.%s",
                myPackage, utilityName);
        String className = utilityName.substring(0, 1).toUpperCase()
                + utilityName.substring(1);
        String utilityFqn = String.format("%s.%sUtility", utilitiesPackageName,
                className);
        return utilityFqn;
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
