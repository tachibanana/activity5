package com.expando

import com.expando.database.DBManager
import com.expando.database.PostgreSQL
import com.expando.database.ServerConnection

class Main {
	
	private static String currentPerson = 'SQL';
	private static int id = 0;
	private static List<Person> listOfPerson = null
	static main(args){
		
		//initialization
		
		Scanner scanner = new Scanner(System.in)
		boolean flag = true
		//set database connection
		ServerConnection serverConn = new PostgreSQL(dbname:'sampledb')
		DBManager manager = new DBManager(serverConn)
		
		//use database
		manager.openConnection()
		
		//LIST OF PERSON
		listOfPerson = manager.getListOfPerson()
		
		//Util
		Util util = new Util(manager)
		
		
		//INITIAL INFO
		System.out.println('INFORMATION : Expando Meta Class. For more info. go to help option.')
		System.out.println("INFORMATION : SQL Connection state is ${serverConn.getConnectionStatus()}")
		System.out.println("INFORMATION : SQL Connection URL is ${serverConn.getConnectionURL()}\n")
		
		while(flag){
			
			//SET USER INPUT
			System.out.print(currentPerson + '>')
			String userInput = scanner.nextLine()
			userInput.trim().toLowerCase()
			
			//AS BACK
			if(userInput.equals('exit')){
				if(currentPerson.equals('SQL')){
					flag = false
					System.out.println("INFORMATION : Closing .... \n")
				}else{
					currentPerson = 'SQL'
					id = 0
				}
			
			//LIST OF PERSON
			}else if(userInput.equals('list')){
				util.listCommand()
			
			//LIST OF COMMAND
			}else if(userInput.equals('commands')){
				if(currentPerson.equals('SQL')){
					System.out.println('Error : Please select a person.\n')
				}else{
					util.listPersonCommand(id , listOfPerson)
				}
				
			//ADD COMMAND
			}else if(userInput.equals('add')){
					try{
						if(!currentPerson.equals('SQL')){
							String name = ''
							String value = ''
							
							System.out.print('Command Name :')
							name = new Scanner(System.in).nextLine()
							
							System.out.print('Command Value :')
							value = new Scanner(System.in).nextLine()
							
							//ADD METHOD
							System.out.println(manager.addCommand(id , name , value))
						}else
							System.out.println('Error : Please select a person.\n')
						
					}catch(Exception e){
						System.out.println("ERROR : Invalid command.")
					}
			//REMOVE PERSON
			}else if(userInput.equals('drop')){
				String confirm = ''
				if(!currentPerson.equals('SQL')){
					
					System.out.print("Are you sure you want to delete $currentPerson ? (y/n).")
					confirm = new Scanner(System.in).nextLine()
					
					if(confirm.equals('y')){
						System.out.println("You've successfully delete $currentPerson.")
						manager.removePerson(id)
						currentPerson = 'SQL'
						id = 0
						
					}else{
						System.out.println('INFORMATION : You canceled.')
					}
				}else
					System.out.println('Error : Please select a person.\n')
				
			
			//REMOVE COMMAND
			}else if(userInput.equals('remove')){
				try{
					if(!currentPerson.equals('SQL')){
						String command = ''
						System.out.println('INFORMATION : Your are in delete mode.')
						System.out.println('INFORMATION : command name is case sensitive.')
						System.out.print('Command Name: ')
						command = new Scanner(System.in).nextLine()
						
						//REMOVE COMMAND
						System.out.println(manager.removeCommand(id , command))
					}else
						System.out.println('Error : Please select a person.\n')
					
				}catch(Exception e){
					System.out.println("ERROR : Command can't find.")
				}
				
			//CREATE PERSON			
			}else if(userInput.equals('create')){
				try{
					String name = ''
					String favorite = ''
					int age = 0
					String yesOrNo = ''
					
					System.out.print('Enter Name :')
					name = new Scanner(System.in).nextLine()
					System.out.print('Enter Favorite food :')
					favorite = new Scanner(System.in).nextLine()
					try{
						System.out.print('Enter Age :')
						age = new Scanner(System.in).nextInt()
					}catch(Exception e){
						age = 0
					}
					System.out.print('CONFIRMATION : Are you sure you want to add this person (y/n). :')
					yesOrNo = new Scanner(System.in).nextLine()
					
					if(yesOrNo == 'y'){
						System.out.println(manager.addPerson(name , favorite , age))		
					}else{
						System.out.println('INFORMATION : You canceled.')
					}
					
				}catch(Exception e){
				}					
			//WITH DYNAMIC INPUT (NOT SINGLE INPUT)
			}else if(userInput.split(' ').length > 0){
				String[] data = userInput.split(' ')
				
				//USE AND SET PERSON
				if(data[0].equals('use')){
					//FIND VALID PERSON
					
					for(Person person : manager.getListOfPerson()){
						//VALID PERSON
						if(data[1].toLowerCase().equals(person.name.toLowerCase())){
							System.out.println("INFORMATION : You are now connected to person \'$person.name\'\n")
							currentPerson = person.name
							id = person.id
						//INVALID PERSON
						}else{
						
						}
							
					}
				
				//DO COMMAND	
				}else if(data[0].equals('do')){
					try{
						if(!currentPerson.equals('SQL')){
							Person person = manager.getPerson(id , listOfPerson)
							String command = data[1]
							System.out.println(person."$command"() + '\n')
						}else{
							System.out.println('Error : Please select a person.\n')
						}
						
					}catch(Exception e){
						System.out.println("ERROR : $currentPerson can\'t do the command.")
					}
				}
				
			}
		}
	}

}
