package SurlyPackage;

import java.util.*;

public class Relation {
	String name;
	LinkedList<Attribute> attribs = new LinkedList<Attribute>();
	LinkedList<Tuple> rows = new LinkedList<Tuple>();
	
	/***************************************CONSTRUCTORS***********************************/
	public Relation(String _name, Attribute[] _attributes)
	{
		this.name = _name;
		for(Attribute a : _attributes)
		{
			attribs.add(a);
		}
		
	}
	
	public Relation(Relation r)
	{
		this.attribs = r.attribs;
		this.rows = r.rows;
	}
	
	public Relation(String __name, LinkedList<Attribute> _attribs){
        this.name = __name;
        this.attribs = _attribs;
    }
	
	public Relation()
	{
		this.name = "Temp";
		this.attribs = null;
	}
	/***************************************INSERT***********************************/
	public void Insert(Tuple t) {
		rows.add(t);
	}
	
	/***************************************PRINT***********************************/

	public void Print() {
	       
        System.out.println("Relation " + this.name);
       
        String attribNames = "|";
       
        for(int i = 0; i < attribs.size(); i++)
        {
            if (attribs.get(i).name.length() <= Integer.parseInt(attribs.get(i).formatSpacing)){
                attribNames += attribs.get(i).name;
            } else {
                int diff = attribs.get(i).name.length() - Integer.parseInt(attribs.get(i).formatSpacing) + 1;
                String abrvName = attribs.get(i).name.substring(0, attribs.get(i).name.length() - diff);
                attribNames += abrvName + ".";
            }
            int whiteSpace = Integer.parseInt(attribs.get(i).formatSpacing);
            int remainingWhiteSpace = whiteSpace - attribs.get(i).name.length();
           
            for(int j = 0; j < remainingWhiteSpace; j++)
                {
                    attribNames += " ";
                }
            attribNames += "|";
        }  
        System.out.println(attribNames);
       
        String border = "+";
        for(int h = 0; h < attribs.size(); h++)
        {
            for(int b = 0; b < Integer.parseInt(attribs.get(h).formatSpacing); b++)
                {
                    border += "-";
                }
            border += "+";
        }
       
        System.out.println(border);
        for(Tuple t : rows)
        {
            String tString = "|";
            String endBit = "+";
            for (int i = 0; i < attribs.size(); i++)
            {
                for (int j = 0; j < Integer.parseInt(attribs.get(i).formatSpacing); j++)
                {
                    if (j < t.data.get(i).length())
                    {
                        tString += t.data.get(i).charAt(j);                    
                    }
                   
                    else
                    {
                        tString += " ";
                    }
                    endBit += "-";
                }
                tString += "|";
                endBit += "+";
            }
        System.out.println(tString + "\n" + endBit);
        }
    }
	
	/***************************************DELETE***********************************/
	
	
	public void DeleteWhere(Relation rel, String[] Conditions)
	{
		Relation rTemp = Select("temp", rel, Conditions);
		for(int i = 0; i < rTemp.rows.size(); i++)
		{
			if(rel.rows.contains(rTemp.rows.get(i)))
			{
				rel.rows.remove(rel.rows.indexOf(rTemp.rows.get(i)));
			}
		}
	}
	
	
	/***************************************PROJECT***********************************/
    public Relation Project(String projName, Relation relName, Attribute[] projAttrib){
           LinkedList<Attribute> testAttributes = new LinkedList<Attribute>();
           LinkedList<Attribute> projAttributes = new LinkedList<Attribute>();
           LinkedList<Tuple> projRows = new LinkedList<Tuple>();
           LinkedList<String> projData = new LinkedList<String>();
           LinkedList<Integer> attIndex = new LinkedList<Integer>();
           Tuple newTuple = new Tuple(projData);
           for (int i = 0; i < projAttrib.length; i++){
               testAttributes.add(projAttrib[i]);
           }
          
           for (int i = 0; i < projAttrib.length; i++){
               if (testAttributes.get(i).equals(projAttrib[i])){
                   attIndex.add(relName.attribs.indexOf(projAttrib[i]));
               }
           }
           Collections.sort(attIndex);
           for (Attribute a: relName.attribs){
               if(testAttributes.contains(a)){
                   projAttributes.add(a);
                   for (Tuple t: relName.rows){
                       for (Integer i: attIndex){
                           projData.add(t.data.get(i));
                       }
                       newTuple = new Tuple(projData);
                       projData = new LinkedList<String>();
                       if (!containsTuple(projRows, newTuple)){
                           projRows.add(newTuple);
                       }
                   }
               }
           }
           Relation projRelation = new Relation(projName, projAttributes);
           for (Tuple t: projRows){
               projRelation.Insert(t);
           }
           return projRelation;
       }
      
       public boolean containsTuple(LinkedList<Tuple> tuples, Tuple tup){
           boolean ret = false;
           for (Tuple t: tuples){
               if (t.data.get(0).equals(tup.data.get(0))){
                   ret =  true;
               }
           }  
           return ret;
       }
	 /***************************************JOIN***********************************/
	public Relation Join(String joinName, Relation relationA, Relation relationB, String joinCondition){
        LinkedList<Attribute> joinAttributes = new LinkedList<Attribute>();
        LinkedList<Tuple> joinRows = new LinkedList<Tuple>();
        String[] splitCondition = joinCondition.split(" ");
        Tuple joinTuple = new Tuple(new String[0]);
 
        int joinCondIndex1 = 0;
        int joinCondIndex2 = 0;
       
        for (Attribute a: relationA.attribs){
            if (splitCondition[0].equals(a.name)){
                joinCondIndex1 = relationA.attribs.indexOf(a);
            }
            joinAttributes.add(a);
        }
        for (Attribute a: relationB.attribs){
            if (splitCondition[2].equals(a.name)){
                joinCondIndex2 = relationB.attribs.indexOf(a);
            }
            joinAttributes.add(a);
        }
       
        for (Tuple t1: relationA.rows){
            for (Tuple t2: relationB.rows){
                if (t1.data.get(joinCondIndex1).equals(t2.data.get(joinCondIndex2))){
                    for (String s1: t1.data){
                        joinTuple.data.add(s1);
                    }
                    for (String s2: t2.data){
                        joinTuple.data.add(s2);
                    }
                    joinRows.add(joinTuple);
                    joinTuple = new Tuple(new String[0]);
                }
            }
        }
        Relation joinRelation = new Relation(joinName, joinAttributes);
        for (Tuple t: joinRows){
            joinRelation.Insert(t);
        }
        joinRelation.Print();
        return joinRelation;
    }
	
	/***************************************SELECT***********************************/
    public Relation Select(String tempName, Relation rel, String[] conditions){
           LinkedList<Relation> tempDB = new LinkedList<Relation>();
           LinkedList<Attribute> selectAttributes = new LinkedList<Attribute>();
          
           Relation newRelation = new Relation(tempName, selectAttributes);
           //create a relation for every condition
           for (int i = 0; i < conditions.length; i+=2){  
               tempDB.add(SelectLoop(tempName, rel, conditions[i]));
           }
          
           for (Attribute a: tempDB.get(0).attribs){
               selectAttributes.add(a);
           }
          
           if (conditions.length == 1){
               return tempDB.get(0);
           }
          
    
           for (int i = 0; i < conditions.length; i++){
               if (tempDB.size() == 1){
                   return tempDB.get(0);
               }
               if (conditions[i].equals("OR")){
                   for (Tuple t: tempDB.get(0).rows){
                       newRelation.Insert(t);
                   }
                   for (Tuple t: tempDB.get(1).rows){
                       newRelation.Insert(t);
                   }
                   if (tempDB.size() == 1){
                       return tempDB.get(0);
                   }
                   tempDB.remove(0);
                   tempDB.remove(0);
                   tempDB.addFirst(newRelation);
               } else if (conditions[i].equals("AND")){
                   Relation r1 = tempDB.get(0);
                   Relation r2 = tempDB.get(1);
                   newRelation = new Relation(tempName, selectAttributes);
                   try {
                       for (Tuple t1: r1.rows){
                           for (Tuple t2 : r2.rows){
                               if (t1 == t2){
                                   newRelation.Insert(t1);
                               }
                           }
                       }
                   } catch (ConcurrentModificationException e){
                       System.out.println();
                   }
                   if (tempDB.size() == 1){
                       return tempDB.get(0);
                   }
                   tempDB.remove(0);
                   tempDB.remove(0);
                   tempDB.addFirst(newRelation);
               }
              
           }
           return tempDB.get(0);
       }
  
  
  
   public Relation SelectLoop(String tempName, Relation r, String conditions){
       LinkedList<Attribute> tempAttributes = new LinkedList<Attribute>();
       LinkedList<Tuple> tempRows = new LinkedList<Tuple>();
       String[] splitCondition = conditions.split(" ");
       int condNumber = 0;
       //System.out.println(Arrays.toString(splitCondition));
       if (isNum(splitCondition[2])){
           condNumber = Integer.parseInt(splitCondition[2]);
       }
      
       for (Attribute a: r.attribs){  
           tempAttributes.add(a);
           if (splitCondition[0].equals(a.name)){
               int attIndex = r.attribs.indexOf(a);
               for (Tuple t: r.rows){
                   //System.out.println("t.data: " + t.data);
                   if (splitCondition[1].equals("=")){
                       //change member to contains
                       if (splitCondition[2].equals(t.data.get(attIndex)) && !tempRows.contains(t.data.get(attIndex))){                              
                           tempRows.add(t);
                       }
                   } else if (splitCondition[1].equals("!=")) {
                       if (!splitCondition[2].equals(t.data.get(attIndex)) && !tempRows.contains(t.data.get(attIndex))){  
                           tempRows.add(t);
                       }
                   } else if (splitCondition[1].equals(">=")) {
                       if (Integer.parseInt(t.data.get(attIndex)) >= condNumber){
                           tempRows.add(t);
                       }
                   } else if (splitCondition[1].equals("<=")) {
                       if (Integer.parseInt(t.data.get(attIndex)) <= condNumber){
                           tempRows.add(t);
                       }
                   } else if (splitCondition[1].equals(">")) {
                       if (Integer.parseInt(t.data.get(attIndex)) > condNumber){
                           tempRows.add(t);
                       }
                   } else if (splitCondition[1].equals("<")) {
                       if (Integer.parseInt(t.data.get(attIndex)) < condNumber){
                           tempRows.add(t);
                       }
                   }
               }
           }
       }
       Relation tempRelation = new Relation(tempName, tempAttributes);
       for (Tuple t: tempRows){
           tempRelation.Insert(t);
       }
       // System.out.println("tempAttributes: " + Arrays.toString(tempAttributes.toArray()));
       //tempRelation.Print();
       return tempRelation;
   }
	
	public boolean memberAttribute(LinkedList<Attribute> attributes, String s){
        for (Attribute a: attributes){
            if (a.name.equals(s)){
                return true;
            }
        }
        return false;
    }
	
	public boolean memberTuple(LinkedList<Tuple> rows, String s){
        for (Tuple t: rows){
            for (String str: t.data){
                if (str.equals(s)){
                    return true;
                }
            }
        }
        return false;
    }
	
	public Tuple popFirst(Tuple t, int attIndex){
        for (int i = 0; i < attIndex; i++){
            t.data.removeFirst();
        }
        return t;
    }
	
	public static boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Double.parseDouble(strNum);
        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }
}
