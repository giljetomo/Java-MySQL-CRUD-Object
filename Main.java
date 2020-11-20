package connectionDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import DBConstants.DBConstants;


public class Main {

	public static final String INSERT_STUDENT = 
			"INSERT INTO STUDENTS (firstName, lastName) VALUES (?,?)";
	public static final String SELECT_STUDENTS =
			"SELECT * FROM STUDENTS";
	public static final String DELETE_STUDENT = 
			"DELETE FROM STUDENTS WHERE UPPER(firstName) = ? and UPPER(lastName) = ?";
	
	public static void main(String[] args) {
		
//		createTable();
		systemControl: while(true) {
			
			int choice = printMenu();
			
			switch (choice) {
			case 1: {
				Student newStudent = getUserInput();
				insertRow(newStudent);
				break;
			} 
			case 2: {
				displayTableContents();
				break;
			}
			case 3: {
				Student s = getUserInput();
				deleteStudent(s);
				break;
			}
			case 4: {
				System.out.println("GOOD BYE!");
				break systemControl;
			}
			default:
				System.err.println("Invalid option!\n");
			}
		}
	}

	private static void deleteStudent(Student s) {

		try {
			
			Connection conn = getConnection();
			PreparedStatement deleteStudent = conn.prepareStatement(DELETE_STUDENT);
			deleteStudent.setString(1, s.getFirstName().toUpperCase());
			deleteStudent.setString(2, s.getLastName().toUpperCase());
			
			int num = deleteStudent.executeUpdate();
			
			System.out.println(num + " record(s) deleted.\n");
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static Student getUserInput() {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter first name: ");
		String fName = input.nextLine();

		System.out.println("Enter last name name: ");
		String lName = input.nextLine();
		
		return new Student(fName, lName);
	}

	private static int printMenu() {
		Scanner input = new Scanner(System.in);
		System.out.println("SELECT AN OPTION: \n"
				+ "1 - Insert a student record\n"
				+ "2 - Display student records\n"
				+ "3 - Delete a student record\n"
				+ "4 - Quit\n");
		return input.nextInt();
	}

	private static void displayTableContents() {
		try {
			Connection conn = getConnection();
			PreparedStatement selectStatement = conn.prepareStatement(SELECT_STUDENTS);
			ResultSet rs = selectStatement.executeQuery();
			int counter = 0;
			while(rs.next()) {
				System.out.println(++counter + ". " + rs.getString("firstName") + ", " + rs.getString("lastName"));
			}
			System.out.println();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void insertRow(Student s) {

		try {
			Connection conn = getConnection();
			PreparedStatement insertStatement = conn.prepareStatement(INSERT_STUDENT);
			insertStatement.setString(1, s.getFirstName());
			insertStatement.setString(2, s.getLastName());
			int num = insertStatement.executeUpdate();
			
			System.out.println("\n" + num + " record added.\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void createTable() {
		
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			//CREATE TABLE
			stmt.executeUpdate("CREATE TABLE students ("
					+ "id int(10) primary key auto_increment,"
					+ "firstName varchar(255),"
					+ "lastName varchar(255));");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static Connection getConnection() {
		try {
			return DriverManager.getConnection(DBConstants.URL, DBConstants.USERNAME, DBConstants.PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
