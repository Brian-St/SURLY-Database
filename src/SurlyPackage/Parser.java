package SurlyPackage;

import java.util.*;

public class Parser {
	
	public void Parse(String line,Database _DB) {
		if (line.charAt(line.length() - 1) == ';'){
            line = line.substring(0, line.length() - 1);
        }
		String[] words = line.split(" ");
		String[] wordsToken = Parser.Tokenize(words);
		switch (wordsToken[0]) {
			case "RELATION":
				Attribute[] _attributes = new Attribute[(wordsToken.length - 2)/3];
				int attrib_INCR = 0;
				for (int i = 2; i < wordsToken.length;i += 3)
				{
					_attributes[attrib_INCR] = new Attribute(wordsToken[i], wordsToken[i + 1], wordsToken[i + 2]);
					attrib_INCR++;
				}
				
				Relation _rel = new Relation(wordsToken[1],_attributes);
				
				_DB.add(_rel);
				break;
				
			case "INSERT":
				boolean valid = false;
				for(Relation r : _DB.relations)
				{
					if (r.name.equals(wordsToken[1]))
					{
						valid = true;
						String[] _data = new String[wordsToken.length - 2];
						for (int i = 2; i < wordsToken.length; i++)
						{
							_data[i-2] = wordsToken[i]; 
						}
						if(_data.length == r.attribs.size())
						{
							boolean success = true;
							int iterator = 0;
							for (String s: _data)
							{
								success = true;
								try {
									int foo = Integer.parseInt(s);
									if(!r.attribs.get(iterator).displayType.contains("NUM"))
									{
										success = false;
										System.out.println("Attribute Type Mismatch: It's supposed to be a " + r.attribs.get(iterator).displayType + " Silly");
										return;
									}
								} catch (NumberFormatException e){
								}
								iterator++;
							}
							if(success)
							{
								Tuple t = new Tuple(_data);
								r.Insert(t);
							}
							else
							{
								System.out.println("Attribute Type Mismatch!");
							}
						}
						else
						{
							System.out.println("Wack Command Yo! Number of Attributes Entered Doesn't Match Relation You Are Attempting to Insert Into");
						}
					}
				}
				if(!valid)
				{
					System.out.println("There Aint No Relations That Match");
				}
				break;
				
			case "PRINT":
				int printed = 0;
				for(Relation r: _DB.relations)
				{
					for(int i = 0; i < wordsToken.length; i++)
					{
						if(r.name.equals(wordsToken[i]))
						{
							r.Print();
							printed++;
						    wordsToken[i] = null;
						}
					}
				}
				if(printed != wordsToken.length-1)
				{
					System.out.println("At Least One Relation Doesn't Exist");
				}
				break;
				
			case "DESTROY":
				Iterator<Relation> rIter = _DB.relations.iterator();
				while(rIter.hasNext())
				{
					if(rIter.next().name.equals(wordsToken[1]))
					{
						rIter.remove();
					}
				}
				break;
				
			case "DELETE":
				if(wordsToken.length > 2)
				{
					for(Relation r : _DB.relations)
					{
						if (r.name.equals(wordsToken[1]))
						{
							
							String[] _cons = Arrays.copyOfRange(wordsToken, 3, wordsToken.length);
							Relation w = new Relation();
							w.DeleteWhere(r,_cons);
						}
					}
				}
				
				else
				{
					for(Relation r : _DB.relations)
					{
						if (r.name.equals(wordsToken[1]))
						{
							r.rows.clear();
						}
					}
				}
				break;
				
			default:
				if(wordsToken.length > 1)
				{
					Relation tempR = new Relation();
					switch (wordsToken[2])
					{
						case "SELECT":
							System.out.println("Select");
							
							for (Relation r : _DB.relations)
							{
								if(r.name.equals(wordsToken[3]))
									{
										if(wordsToken.length > 4)
										{
											String[] cons = Arrays.copyOfRange(wordsToken, 5, wordsToken.length);
											tempR.Select(wordsToken[0], r, cons).Print();
										}
										
										else {
											r.Print();
										}
									}
							}
							
							break;
						
						case "PROJECT":
							System.out.println("Project");
							for (Relation r : _DB.relations)
							{
								if(r.name.equals(wordsToken[wordsToken.length-1]))
									{
										if(wordsToken.length > 4 )
										{
											String[] projAttribs = Arrays.copyOfRange(wordsToken, 3, wordsToken.length-2);
											Attribute[] attribs = new Attribute[projAttribs.length];
											for(Attribute a : r.attribs)
											{
												for(int i = 0; i < projAttribs.length; i++)
												{
													if(a.name.equals(projAttribs[i]))
														attribs[i] = a;
												}
											}
											tempR.Project(wordsToken[0], r, attribs).Print();;
										}
										
										else {
										}
									}
							}
							break;
						
						case "JOIN":
							System.out.println("Join");
							int a = 0;
							for(Relation r : _DB.relations)
							{
								if(r.name.equals(wordsToken[3]))
								{
									break;
								}
								a++;
							}
							
							int b = 0;
							for(Relation r : _DB.relations)
							{
								if(r.name.equals(wordsToken[4]))
								{
									break;
								}
								b++;
							}
							tempR.Join(wordsToken[0], _DB.relations.get(a), _DB.relations.get(b), wordsToken[wordsToken.length-1]);
							break;
							
						default:
							System.out.println("Non-Valid Command Jim --->" + wordsToken[2]);
					} 
				}
				else
				{
					System.out.println("yeet fix this");
				}
				break;
			}	
		return;
	}
	
	public static String[] Tokenize(String[] line) {
		String complexWord = "";
		LinkedList<String> newWords = new LinkedList<String>();
		
		for(int i = 0; i < line.length; i++)
		{
			if(line[i].contains("("))
			{
				newWords.add(line[i].replace("(",""));
			}
			else if(line[i].contains(","))
			{
				newWords.add(line[i].replace(",",""));
			}
			else if(line[i].contains(")"))
			{
				String s = line[i].replace(")", "");
				s = s.replace(";", "");
				newWords.add(s);
			}
			else if(line[i].contains(";"))
			{
				newWords.add(line[i].replace(";",""));
			}
			
			else if(line[i].contains("'"))
			{
				complexWord += (line[i] + " ");
				while (!line[i+1].contains("'"))
				{
					complexWord += (line[i + 1] + " ");
					i++;
				}
				complexWord += line[i + 1];
				i++;
				complexWord = complexWord.replaceAll("'", "");
				newWords.add(complexWord);
				complexWord = "";
				
			}
			
			else if((line[i].contains("=") || line[i].contains("<") || line[i].contains(">")) && (i > 1))
			{
				newWords.set(newWords.indexOf(newWords.getLast()), newWords.getLast() + " " + line[i] + " " + line[i+1]);
				i++;
			}
			
			else
			{
				newWords.add(line[i]);
			}
			
		}
		
		String[] words_formatted = new String[newWords.size()];
		
		int i = 0;
		for(String s : newWords)
		{
			words_formatted[i] = s;
			i++;
		}
		return words_formatted; 
	}
}
