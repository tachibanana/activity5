package com.expando.database
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import com.expando.Command
import com.expando.Person


class DBManager {

	ServerConnection sc
	Connection conn = null
	Statement st = null
	
	public DBManager(ServerConnection sc){
		
		this.sc = sc
	}
	
	public void openConnection() throws Exception{
		conn = sc.getConnection()
	}
	
	//CREATE TABLE tblperson
	public void createTable(){
		try{
			DatabaseMetaData dbm = conn.getMetaData()
			ResultSet rs = dbm.getTables(null , null , 'tblperson', null)
			if(!rs.next()){
				String sql = 'CREATE TABLE tblperson(' +
							 'id SERIAL PRIMARY KEY NOT NULL,' +
							 'name VARCHAR(45) NOT NULL,' +
							 'favoriteFood VARCHAR(45) NOT NULL,' +
							 'age INT NOT NULL);'
				
				st = conn.createStatement(sql)
				st.executeUpdate()
				
				System.out.println('INFORMATION: Table person was re-created and ready to use.')
			}else
				System.out.println('INFORMATION: Table person is ready to use.')
		}catch(Exception e){
			System.out.println('ERROR: Something error in SQL connection.')
		}
	}
	
	//ADD PERSON
	public String addPerson(String name , String favoriteFood , int age){
		try{
			
			String sql = 'INSERT INTO tblperson(name , favoriteFood , age) VALUES(?,?,?)'
			PreparedStatement pst = conn.prepareStatement(sql)
			pst.setString(1 , name)
			pst.setString(2 , favoriteFood)
			pst.setInt(3 , age)
			pst.executeUpdate()
			
			return "INFORMATION : You've successfully add a person $name to list.\n"
		}catch(Exception e){
			return "ERROR : Sorry the system can't add $name. Please try a gain later.\n"
		}
	}
	
	//REMOVE PERSON
	public String removePerson(int id){
		try{
			String sql = 'DELETE FROM tblperson WHERE id = ?'
			PreparedStatement pst = conn.prepareStatement(sql)
			pst.setInt(1 , id)
			
			return 'INFORMATION : You performed a delete command.\n'
			
		}catch(Exception e){
		
		}
	}
	
	//ADD COMMAND
	public String addCommand(int personId , String commandName , String commandValue){
		try{
			
			String sql = 'INSERT INTO tblcommand(personId , commandName , commandValue) VALUES(?,?,?)'
			PreparedStatement pst = conn.prepareStatement(sql)
			pst.setInt(1 , personId)
			pst.setString(2 , commandName)
			pst.setString(3 , commandValue)
			pst.executeUpdate()
			
			return 'INFORMATION : You added 1 command.\n'
		}catch(Exception e){
			return 'ERROR : Sorry we can\'t add command. Please try a gain later.'
		}
	}
	
	//REMOVE COMMAND
	public String removeCommand(int personId , String commandName){
		try{
			String sql = 'DELETE FROM tblcommand WHERE personId = ? AND commandName = ?'
			PreparedStatement pst = conn.prepareStatement(sql)
			pst.setInt(1 , personId)
			pst.setString(2 , commandName)
			pst.executeUpdate()
			
			return 'INFORMATION : You performed a delete command.\n'
			
		}catch(Exception e){
		
		}
	}
	
	//LIST OF PERSON
	public List<Person> getListOfPerson(){
		List<Person> listOfPerson = new ArrayList<Person>()
		
		String sql = 'SELECT * FROM tblperson'
		PreparedStatement pst = conn.prepareStatement(sql)
		ResultSet rs = pst.executeQuery()
		
		while(rs.next()){
			listOfPerson.add(new Person(id:rs.getInt('id') ,
				name:rs.getString('name'),
				favoriteFood:rs.getString('favoriteFood'),
				age:rs.getInt('age')))
		}
		
		return listOfPerson
		
	}
	
	//RETURN LIST OF COMMAND
	public List<Command> getListOfCommand(int personId){
		try{
			List<Command> commandList = new ArrayList<Command>()
			String sql = 'SELECT c.commandName as name, c.commandValue as value FROM tblperson as p JOIN tblcommand as c ON p.id = c.personId WHERE p.id = ?'
						 
			PreparedStatement pst = conn.prepareStatement(sql)
			pst.setInt(1 , personId)
			ResultSet rs = pst.executeQuery()
			
			while(rs.next()){
				commandList.add(new Command(name:rs.getString('name') ,
					value:rs.getString('value')))
			}
			
			return commandList
			
		}catch(Exception e){
			System.out.println('ERROR : SQL Connection error.')
			e.printStackTrace()
		}
		
	}
	
	//MAKE USE OF THIS PERSON
	public getPerson(int personId , List<Person> list){
	
		for(Person p : list){
			if(personId == p.id){
				return p
			}
		}
		
		return null;
		
	}
	


}
