/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ousse
 */
import java.sql.*;
public class User {
    private int CIN;
    private String passwd;
    private String prenom;
    public int getCin(){
        return CIN;
    }
    public User(){
        CIN=0;
        passwd="";           
    }
    public User(int CIN,String passwd){
        this.CIN=CIN;
        this.passwd=passwd;
    }
    public String getPrenom(){
        return prenom;
    }
    public boolean verif(Statement statement){
        String querry="call verify_emp("+CIN+",'"+passwd+"')";
        try{
            ResultSet result=statement.executeQuery(querry);
            if (result.next()){
                prenom=result.getString(1);
                return true;
            }
      
            return false;
        }
        catch (Exception e){
            System.out.println("claas user verif error");
            return false;
        }
        
    }
    
}
