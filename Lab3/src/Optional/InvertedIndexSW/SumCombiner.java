/*
 * Combiner class for InvertedIndex Table
 * Project Name: Inverted Index Table for Hadoop (Lab2)
 * Group Name: What the f**k
 * Created: Yueqi Chen (Yueqichen.0x0@gmail.com)
 * Time: 2016/4/22/21:36
*/

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SumCombiner extends Reducer<Text, IntWritable, Text, IntWritable>
{
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
	int sum = 0;
	for(IntWritable val : values)
	    sum += val.get();

	IntWritable result = new IntWritable();
	result.set(sum);
	context.write(key, result);

	/*OutputFormat: <word#filename, wordnumber>*/
    }
}
