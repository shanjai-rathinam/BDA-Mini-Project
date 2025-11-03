package com.yourgithubusername.weather;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * Reducer for the Max Temperature analysis.
 * It takes a year and a list of all temperatures recorded for that year,
 * and finds the maximum value.
 */
public class MaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        
        int maxValue = Integer.MIN_VALUE;

        // Iterate through all temperature values for the given year (the key).
        for (IntWritable value : values) {
            maxValue = Math.max(maxValue, value.get());
        }
        
        // Emit the year (key) and its calculated maximum temperature.
        context.write(key, new IntWritable(maxValue));
    }
}