package com.yourgithubusername.weather;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * The main driver class for the MapReduce job.
 * It configures and launches the job.
 */
public class MaxTemperatureDriver {

    public static void main(String[] args) throws Exception {
        // Check if the correct number of arguments (input and output paths) are provided.
        if (args.length != 2) {
            System.err.println("Usage: MaxTemperatureDriver <input path> <output path>");
            System.exit(-1);
        }

        // Create a new MapReduce job instance.
        Job job = Job.getInstance();
        job.setJarByClass(MaxTemperatureDriver.class);
        job.setJobName("Max Temperature Analysis");

        // Set the input and output paths from the command-line arguments.
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Set the Mapper and Reducer classes for the job.
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        // A Combiner is a great optimization. It runs the Reducer logic on the map nodes
        // to reduce the amount of data shuffled across the network.
        job.setCombinerClass(MaxTemperatureReducer.class);

        // Specify the final output key and value types for the job.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // Wait for the job to complete and exit with a status code.
        // A status code of 0 indicates success, 1 indicates failure.
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}