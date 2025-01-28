package SurlyPackage;
import java.util.*;

public class Tuple {
	public LinkedList<String> data = new LinkedList<String>();
	
	public Tuple(String[] _data){
		for (String s : _data) {
			data.add(s);
		}
	}
	
	public Tuple(LinkedList<String> data){
        this.data = data;
    }
}
