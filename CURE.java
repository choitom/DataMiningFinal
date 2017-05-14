import java.io.*;
import java.util.*;

public class CURE{
	private County[] counties;
	private County[] samples;
	
	public CURE(ArrayList<County> c, double fraction){
		int sample_size = (int)(c.size() * fraction);
		this.counties = new County[c.size()];
		this.samples = new County[sample_size];
		
		int index = 0;
		for(County county : c){
			counties[index++] = new County(county.getID(), county.getVector());
		}
		randomSamples(sample_size);
	}
	
	public void cluster(){
		
	}
	
	// randomly shuffle county array and select the first 'sample_size' counties
	private void randomSamples(int sample_size){
		int index;
		Random random = new Random();
		for(int i = counties.length-1; i > 0; i--){
			index = random.nextInt(i+1);
			County temp = counties[index];
			counties[index] = counties[i];
			counties[i] = temp;
		}
		
		for(int i = 0; i < sample_size; i++){
			samples[i] = counties[i];
		}
	}
}