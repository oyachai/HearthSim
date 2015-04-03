package com.hearthsim.io;

import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class to read in a HearthSim parameter file
 *
 * Format of the file:
 *
 * ------------------------------------------------
 * Param1Name = Value1
 * Pardm2Name = Value2
 * ...
 *
 * #ParamXName = ValueX : This line is ignored because of the # at the beginning of the line
 * ------------------------------------------------
 *
 *
 */
public class ParamFile {


    private ParamFile() {}

    /**
     * Constructor
     *
     * @param setupFilePath The file path to the deck list to be read
     * @throws IOException
     */
    public ParamFile(Path paramFilePath) throws HSInvalidParamFileException, IOException {
        this.paramFilePath_ = paramFilePath;
        this.params_ = new HashMap<>();
        this.read(paramFilePath);
    }

    /**
     * Constructor
     *
     * @param setupFilePath The file path to the deck list to be read
     * @throws IOException
     */
    public static ParamFile fromHashMap(HashMap<String, String> hashMap) {
        ParamFile toRet = new ParamFile();
        toRet.params_ = hashMap;
        return toRet;
    }


    /**
     * Read the given file
     *
     * @param setupFilePath The file path to the deck list to be read
     * @throws IOException
     */
    public void read(Path paramFilePath) throws HSInvalidParamFileException, IOException {
        try(BufferedReader br = Files.newBufferedReader(paramFilePath, StandardCharsets.UTF_8)) {
            for (String line; (line = br.readLine()) != null; ) {
                String cleanedLine = line.replaceAll("\\s+","");
                if (cleanedLine.equals("") || cleanedLine.startsWith("#"))
                    continue;
                //Store everything as Strings for now
                String[] tokens = line.replaceAll("\\s+","").split("=");
                if (tokens.length != 2) {
                    throw new HSInvalidParamFileException("Invalid line in param file " + paramFilePath + ": " + line);
                }
                params_.put(tokens[0], tokens[1]);
            }
        }
    }

    /**
     * Get an int valued parameter
     *
     * @param paramKey
     * @return
     * @throws HSParamNotFoundException
     */
    public int getInt(String paramKey) throws HSParamNotFoundException {
        if (!params_.containsKey(paramKey))
            throw new HSParamNotFoundException("Param not found: File = " + paramFilePath_ + ", param = " + paramKey);
        return Integer.parseInt(params_.get(paramKey));
    }

    /**
     * Get an int valued parameter
     *
     * @param paramKey
     * @param defaultValue
     * @return
     */
    public int getInt(String paramKey, int defaultValue) {
        try {
            return this.getInt(paramKey);
        } catch (HSParamNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Get a double valued parameter
     *
     * @param paramKey
     * @return
     * @throws HSParamNotFoundException
     */
    public double getDouble(String paramKey) throws HSParamNotFoundException {
        if (!params_.containsKey(paramKey))
            throw new HSParamNotFoundException("Param not found: File = " + paramFilePath_ + ", param = " + paramKey);
        return Double.parseDouble(params_.get(paramKey));
    }

    /**
     * Get a double valued parameter
     *
     * @param paramKey
     * @param defaultValue
     * @return
     */
    public double getDouble(String paramKey, double defaultValue) {
        try {
            return this.getDouble(paramKey);
        } catch (HSParamNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Get a String valued parameter
     *
     * @param paramKey
     * @return
     * @throws HSParamNotFoundException
     */
    public String getString(String paramKey) throws HSParamNotFoundException {
        if (!params_.containsKey(paramKey))
            throw new HSParamNotFoundException("Param not found: File = " + paramFilePath_ + ", param = " + paramKey);
        return params_.get(paramKey);
    }

    /**
     * Get a String valued parameter
     *
     * @param paramKey
     * @param defaultValue
     * @return
     */
    public String getString(String paramKey, String defaultValue) {
        try {
            return this.getString(paramKey);
        } catch (HSParamNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Get a Boolean valued parameter
     *
     * @param paramKey
     * @return
     * @throws HSParamNotFoundException
     */
    public boolean getBoolean(String paramKey) throws HSParamNotFoundException {
        if (!params_.containsKey(paramKey))
            throw new HSParamNotFoundException("Param not found: File = " + paramFilePath_ + ", param = " + paramKey);
        return Boolean.parseBoolean(params_.get(paramKey));
    }

    /**
     * Get a String valued parameter
     *
     * @param paramKey
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String paramKey, boolean defaultValue) {
        try {
            return this.getBoolean(paramKey);
        } catch (HSParamNotFoundException e) {
            return defaultValue;
        }
    }


    public Set<String> getKeysContaining(String partialKey) {
        TreeSet<String> toRet = new TreeSet<>();
        for (String key : params_.keySet()) {
            if (key.contains(partialKey)) {
                toRet.add(key);
            }
        }
        return toRet;
    }

    Map<String, String> params_;
    Path paramFilePath_;

}
