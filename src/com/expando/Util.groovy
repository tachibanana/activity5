package com.expando

import com.expando.database.DBManager

class Util {
	
	private DBManager manager
	
	public Util(DBManager manager){
		this.manager = manager;
	}
	
	//PERSON LIST
	public void listCommand(){
		
		System.out.println('Name\t\tFavorite Food\t\tAge')
		System.out.println('--------------------------------------------')
		for(Person person : manager.getListOfPerson()){
			System.out.println("$person.name\t\t$person.favoriteFood\t\t\t$person.age")
		}
		
		System.out.println()
	}
	
	//DISPLAY COMMAND LIST
	public void listPersonCommand(int personId , List<Person> list){
		
		System.out.println('CommandName\tCommandValue')
		System.out.println('--------------------------------')
		for(Person person : list){
			if(person.id == personId){
				for(Command command : manager.getListOfCommand(personId)){
					
					System.out.println("$command.name\t\t$command.value")
					
					String name = command.name.toLowerCase()
					String value = command.value
					String personName = person.name
					
					person.metaClass."$name"{"$personName $value ITS AMAZING!"}

				}
			}
		}
		
		System.out.println()
	}

}
