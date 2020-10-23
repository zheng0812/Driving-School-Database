/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Imports
import java.sql.*;
import java.util.Scanner; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Calvin Zheng and Vamsi Garghi
 */
public class JavaConnectDB {
    
   
    public static void main(String args[]) throws SQLException {
        //datebase url
        String dbURL = "jdbc:oracle:thin:@akka.d.umn.edu:1521:XE";
        
        //user enters username and password
        String username;
        String password;
        Scanner usernameline = new Scanner(System.in);
        System.out.println("Enter Username:");
        username = usernameline.nextLine();  
        Scanner passwordline= new Scanner(System.in);
        System.out.println("Enter password:");
        password = passwordline.nextLine();
        Connection dbCon = null;
        Boolean connect = true;
        
        //This will try to connect the database url with user provided username and password
        try{ 
            dbCon = DriverManager.getConnection(dbURL, username, password);          
        }
        catch(SQLException ex) {
             System.out.println(ex);
             connect = false;
         }
    //Checks to see if Connection is True. If true then start the query process    
    if(connect == true){    
    //Swtich loop for the four Queries
        String switchnum;  
        Boolean again = true; 
        
        while(again == true){
         
        //User enters what query they want. This will loop until user enters Exit    
            Scanner switchnumline = new Scanner(System.in);  
            System.out.println("Enter:\n \t1(Scheduling a Road Test)\n\t2(Scheduling a Driving Lesson)\n"
                + "\t3(Find the percentage of fail score of a specific school)\n\t4(Finds the list of student assoicated with a instructor)\n"
                    + "\tExit(Exit the Program)\n");
        switchnum = switchnumline.nextLine();
        
        switch (switchnum){
            case "1":
                insertRoadTest(dbCon);
                break;                
            case "2":
                insertDrivingLesson(dbCon);
                break;
            case "3": 
                SelectPercentageFail(dbCon);
                break;
            case "4":
                SelectNotYetPassed(dbCon);
                break;
            case "Exit":
                again = false;
                break;
            }
        
        }
    }
       
    }  
    
    //Schedules a Road Test
    public static void insertRoadTest(Connection dbCon) throws SQLException{
        
    String RoadTest_ID,Test_Date,Score,StateExaminer_ID,StateExaminer_name,Stein_id,vin ;
    //RoadTest_ID
     Scanner RoadTest_IDline = new Scanner(System.in);
     System.out.println("Enter RoadTest ID:");
     RoadTest_ID = RoadTest_IDline.nextLine();
    //Test_Date
    Scanner Test_Dateline = new Scanner(System.in);
     System.out.println("Enter Test Date(MM/DD/YYYY):");
     Test_Date = Test_Dateline.nextLine();
    //Score
    Scanner Scoreline = new Scanner(System.in);
     System.out.println("Enter Score(Pass/Fail):");
     Score = Scoreline.nextLine();
    //State Examiner ID
    Scanner StateExaminer_IDline = new Scanner(System.in);
     System.out.println("Enter State Examiner ID:");
     StateExaminer_ID = StateExaminer_IDline.nextLine();
    //StateExaminer name
    Scanner StateExaminer_nameline = new Scanner(System.in);
     System.out.println("Enter State Examiner Name:");
     StateExaminer_name = StateExaminer_nameline.nextLine();
    //Stein ID
    Scanner Stein_IDline = new Scanner(System.in);
     System.out.println("Enter Stein ID:");
     Stein_id = Stein_IDline.nextLine();
    //Vehicle Vin
    Scanner vinline = new Scanner(System.in);
     System.out.println("Enter Vehicle Vin:");
     vin = vinline.nextLine();
    
    //Inserts road test in the database 
    try{
     //create statement
    Statement insert = dbCon.createStatement();
    insert.executeUpdate("Insert into Road_Test Values('"+RoadTest_ID+"',TO_DATE('"+Test_Date+"', 'MM/DD/YYYY'), '"+Score+"', "+StateExaminer_ID+", '"+StateExaminer_name+"', '"+Stein_id+"', '"+vin+"')");
    System.out.println("Scheduled a Road Test!");
    }
    catch(SQLException ex) {
             System.out.println(ex);
    }
    
    
    }
    
    //Schedules a Driving Lesson
    public static void insertDrivingLesson(Connection dbCon) throws SQLException{
      String lesson_id,date_lesson,stein_id,instructor_ssn,vin;
     //Lesson ID 
     Scanner lesson_idline = new Scanner(System.in);
     System.out.println("Enter Lesson ID:");
     lesson_id = lesson_idline.nextLine();
     //Date Lesson
     Scanner date_lessonline = new Scanner(System.in);
     System.out.println("Enter Date of Lesson(MM/DD/YYYY):");
     date_lesson = date_lessonline.nextLine();
     //stein_id
     Scanner stein_idline = new Scanner(System.in);
     System.out.println("Enter Stein ID of the Student:");
     stein_id = stein_idline.nextLine();
     //Instructor_SSN 
     Scanner instructor_ssnline = new Scanner(System.in);
     System.out.println("Enter Instructor's SSN:");
     instructor_ssn = instructor_ssnline.nextLine();
        try{
         //create statement
        Statement st = dbCon.createStatement();
        st.executeUpdate("Insert into Lesson Values( "+lesson_id+ ", TO_DATE('"+date_lesson+"', 'MM/DD/YYYY'), '"+stein_id+"', "+instructor_ssn+")");
        System.out.println("Scheduled a Driving Lesson!\n");
        }
           catch(SQLException ex) {
             System.out.println(ex);
        }
    
    }
 //This function will find the fail scores of different schools   
 public static void SelectPercentageFail(Connection dbCon) throws SQLException{
     //User enters a specific driving schoool id
     String DrivingSchool_ID;
      Scanner DrivingSchool_IDline = new Scanner(System.in);
     System.out.println("Enter Driving School ID:");
     DrivingSchool_ID = DrivingSchool_IDline.nextLine();
    //my select query 3 
    String query ="Select DISTINCT( Select COUNT(Road_Test.Score)\n" +
    "FROM Instructor,Driving_School,Student,Road_Test\n" +
    "where Instructor.drivingschool_id = Driving_School.drivingschool_id\n" +
    "AND Instructor.instructor_ssn= Student.instructor_ssn\n" +
    "AND Student.Stein_ID = Road_Test.Stein_id\n" +
    "AND Driving_School.drivingschool_id = '"+DrivingSchool_ID+"'\n" +
    "AND Road_Test.Score = 'Fail')/(Select COUNT(Road_Test.Score)\n" +
    "FROM Instructor,Driving_School,Student,Road_Test\n" +
    "where Instructor.drivingschool_id = Driving_School.drivingschool_id\n" +
    "AND Instructor.instructor_ssn= Student.instructor_ssn\n" +
    "AND Driving_School.drivingschool_id = '"+DrivingSchool_ID+"'\n" +
    "AND Student.Stein_ID = Road_Test.Stein_id)*100 as Score From Road_Test";
            //getting PreparedStatment to execute query
            try{
            //prepare stament to execute query
            Statement state = dbCon.prepareStatement(query);
           
            //Resultset returned by query
            ResultSet selectresult = state.executeQuery(query);
          
           while (selectresult.next()) {
                System.out.println("Fail Percentage for the School is "+ selectresult.getString(1)+"%\n");
            }
           }
            catch(SQLException ex) {
             System.out.println(ex);
            }
 }
 
 //This function will find students that have not yet passed their Road Test for Instructors 
  public static void SelectNotYetPassed(Connection dbCon) throws SQLException{
     //User enters a specific instructor ID
     String Instructor_ID;
      Scanner Instructor_IDline = new Scanner(System.in);
     System.out.println("Enter Instructor SSN:");
     Instructor_ID = Instructor_IDline.nextLine();
     
    //my select query 3 
    String query ="Select Student_name FROM (\n" +
    "Select Student.Stein_ID,Student_name,MAX(Road_Test.Test_Date)\n" +
    "FROM Student, Instructor,Road_Test\n" +
    "Where Student.instructor_ssn = Instructor.instructor_ssn\n" +
    "AND Student.Stein_ID = Road_Test.Stein_id\n" +
    "AND Instructor.instructor_ssn = '"+Instructor_ID+"'\n" +
    "AND NOT EXISTS (SELECT Score FROM Road_Test WHERE Score = 'Pass' AND Student.Stein_ID = Road_Test.Stein_id)\n" +
    "Group BY Student.Stein_ID,Student_name)";
 
            try{
              //Loop for printing result numbers
              int rownum = 1;
              //prepare stament to execute query
              Statement state = dbCon.prepareStatement(query);
           
            //Resultset returned by query
                ResultSet selectresult = state.executeQuery(query);
                System.out.println("Students that have not yet passed are:");
            //prints out query
                while (selectresult.next()) {
                System.out.println(rownum + "." + selectresult.getString(1));
                rownum++;
                }
                System.out.println("\n");
            }
            catch(SQLException ex) {
             System.out.println(ex);
            }
 }
   
}
