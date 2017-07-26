package Crawlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result {
	public  String title;
	public  String link;
	public  Map<String, String> predicates;


	public Result(String s)
	{
		this.title=s;
		predicates=new HashMap<String, String>();
		predicates.put("urrri", s);

	}
	public void setLink(String l)
	{
		this.link=l;
	}
	public Result(String s, ArrayList<String> pr, ArrayList<String> obj)
	{
		this.title=s;
		predicates=new HashMap<String, String>();
		for(int i=0;i<pr.size();i++)
			for(int j=0;j<obj.size();j++)
				predicates.put(pr.get(i),obj.get(j));
		

		


	}

}
