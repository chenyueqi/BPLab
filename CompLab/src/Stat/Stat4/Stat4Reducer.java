/*
 * Reducer class for Stat4
 * Project Name: CompLab
 * Group Name: What the f**k
 * Created by: Wei Liu (lw_nju@outlook.com)
 * Time: 2016/7/5 21:59
*/

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

public class Stat4Reducer extends Reducer<Text, IntWritable, Text, Text>
{
    private MultipleOutputs out;
    
    protected void setup(Context context) throws IOException,InterruptedException
    {
        out = new MultipleOutputs<Text,Text>(context);
    }

    private String getfilename(String str)
    {
        String[] names = str.split("/");
        String ret = new String("");
        for(int i=0;i<names.length;i++)
        {
            if(i < 2)
                ret = ret + names[i];
            else
                ret = ret + "-" + names[i];
        }
        return ret;
    }

    private String get_time(Text text)
    {
        if(text.toString().contains("@"))
        {
            int time1 = Integer.parseInt(text.toString().split("#")[1].split("@")[0]);
            int time2 = time1 + 1;
            String IP = text.toString().split("#")[1].split("@")[1];

            return new String(time1+":00-"+time2+":00\t"+IP);
        }
        else
        {
            String IP = text.toString().split("#")[1];
            return new String(IP);
        }
    }

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
        String interfaces = new String();
        if(key.toString().contains("@"))
            interfaces = key.toString().split("@")[1];
        else
            interfaces = key.toString().split("#")[1];

		int sum =0;
        int time = 0;
		for (IntWritable val : values) {
			time += val.get();
            sum += 1; 
		}

		Text result = new Text();
		result.set(get_time(key)+":"+(double)time/(double)sum);
		out.write("Interface",result, new Text(""),getfilename(interfaces)+".txt");
    }
    protected void cleanup(Context context) throws IOException, InterruptedException
    {
        out.close();
    }
}
