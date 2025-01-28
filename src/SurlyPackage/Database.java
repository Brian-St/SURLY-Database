package SurlyPackage;

import java.util.LinkedList;

public class Database {
	LinkedList<Relation> relations = new LinkedList<Relation>();
	
	String name;
	public Database(String _name) {
		this.name = _name;
	}
	

	public void add(Relation rel)
	{
		relations.add(rel);
	}
	
	public void remove(Relation rel)
    {
        relations.remove(rel);
        System.out.println("relation destroyed");
    }
}
