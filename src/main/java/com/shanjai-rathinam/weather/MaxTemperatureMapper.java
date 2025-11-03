package com.yourgithubusername.weather;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * Mapper for the Max Temperature analysis.
 * It reads weather data records, extracts the year and temperature,
 * and emits them as key-value pairs.
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    // NCDC data uses 9999 as a placeholder for missing temperature readings.
    private static final int MISSING_TEMPERATURE = 9999;

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        
        // Extract the year from the record. The data is in a fixed-width format.
        // Year is in positions 15-19 (0-indexed substring is 15 to 19).
        String year = line.substring(15, 19);

        int airTemperature;
        // The temperature value is in positions 87-92.
        // It can be preceded by a '+' or '-' sign.
        if (line.charAt(87) == '+') { // Check for explicit positive sign
            airTemperature = Integer.parseInt(line.substring(88, 92));
        } else {
            airTemperature = Integer.parseInt(line.substring(87, 92));
        }

        // The quality code is in position 92.
        // A value of '0', '1', '4', '5', or '9' indicates a valid reading.
        String quality = line.substring(92, 93);

        // We only process records with a valid temperature and a good quality code.
        if (airTemperature != MISSING_TEMPERATURE && quality.matches("[01459]")) {
            // Emit the year as the key and the temperature as the value.
            context.write(new Text(year), new IntWritable(airTemperature));
        }
    }
}