package edu.albany.cs.transWeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.base.Utils;


public class ResultItem {
	public int index;
	public double score;
	public String date;
	public ArrayList<Integer> Stations;
	public ArrayList<Integer> timeSlots;
	public ArrayList<Integer> detectIdxList=new ArrayList<Integer>();
	public Comparator<ResultItem> comparator=null;
    
	public ResultItem(int index,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots) {
		this.index=index;
		this.score=-1.0;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		
	}
	
	public ResultItem(int index,double score,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots,ArrayList<ResultItem> groundTrue) {
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		IsDetected(groundTrue);
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		//System.out.println(this.detectIdxList);
		
	}
	
	/*public ResultItem(int index,double score,String date,ArrayList<Integer> Stations,ArrayList<Integer> timeSlots,ArrayList<ResultItem> groundTrue) {
		this.index=index;
		this.score=score;
		this.date=date;
		this.Stations=Stations;
		this.timeSlots=timeSlots;
		
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		//System.out.println(this.detectIdxList);
		
	}*/
	public ResultItem(){
		this.comparator=new Comparator<ResultItem>() {
	        public int compare(ResultItem item1, ResultItem item2)
	        {
	        	if(item1.score > item2.score){  //larger return 1
	        		return -1;
	        	}else if(item1.score==item2.score){
	        		return 0;
	        	}else{
	            return  1;
	        	}
	        }
	    };
		
	}
	public ArrayList<Integer> IsDetected(ArrayList<ResultItem> groundTrue){
		
		for(ResultItem ri:groundTrue){
			//System.out.println(ri.date+" "+this.date);
			if(!ri.date.equals(this.date)){
				//System.out.println(ri.date+" "+this.date);
				continue;
			}else{
				ArrayList<Integer> intList= intersection(ri.Stations,this.Stations);				
				if(intList.size()>0){
					ArrayList<Integer> intSlotList= intersection(ri.timeSlots, this.timeSlots);
					if(intSlotList.size()>0){
						//System.err.println(ri.index+" >>"+ri.timeSlots+ " "+this.timeSlots);
						this.detectIdxList.add(ri.index);
					}
				}
			}
		}
		return Stations;
	}
	 public static <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
		 ArrayList<T> list = new ArrayList<T>();		
	        for (T t : list1) {
	            if(list2.contains(t)) {
	                list.add(t);
	            }
	        }
	        //System.out.println(this.date+" "+list.size()+""+list1 +"¡¡"+list2);
	        return list;
	    }
	
	 public static void printItem(ResultItem resultItem){
		 System.out.print("\nResultItem: "+resultItem.index+" "+resultItem.score+" ");
		 System.out.print(ArrayUtils.toString(resultItem.Stations)+ " ");
		 System.out.print(ArrayUtils.toString(resultItem.timeSlots));
		 System.out.print(ArrayUtils.toString(resultItem.date));
		 
	 }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
