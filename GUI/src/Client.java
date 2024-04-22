
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author noura
 */
public class Client {
    Client(){
        
    }
    //ajouter un nouveau client
    public  void refreshClients(Statement statement){
        String query ="Select * from clients";
        DefaultTableModel model = (DefaultTableModel) GUI.ClientTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(3),result.getString(2),result.getString(4),result.getString(5)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }
        
    }
    public  void refreshClients(Statement statement,int cin,String prenom, String nom,String email,int cinEmp){
        String query ="Select * from clients where";
        if(cin!=0)query+=" cinClient="+cin+" and";
        if(prenom.compareTo("")!=0)query+=" pernomClient='"+prenom+"' and";
        if(nom.compareTo("")!=0)query+=" nomClient='"+nom+"' and";
        if(email.compareTo("")!=0 && isValidEmail(email))query+=" email='"+email+"' and";
        if(cinEmp!=0)query+=" cinEmployeAj="+cinEmp+" and";
        query+=" true";
        System.out.println(query);
        DefaultTableModel model = (DefaultTableModel) GUI.ClientTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(3),result.getString(2),result.getString(4),result.getString(5)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }
        
    }

    public boolean addNewClient(int cin,String nom,String prenom,String email,int cinEmp,Statement statement){
        
        if(nom.equals(""))return false;
        if(prenom.equals(""))return false;
        if(email.equals(""))return false;
        if(!isValidEmail(email))return false;
        
        String addQuery="INSERT INTO clients values("+cin+",'"+prenom+"','"+nom+"','"+email+"',"+cinEmp+")";
        System.out.println(addQuery);
         try{
            statement.executeUpdate(addQuery);
            refreshClients(statement);
            System.out.println("done");
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public boolean verif(Statement statement,int cin){
        String querry="SELECT * FROM clients  WHERE cinCLient ="+cin;
        System.out.println(querry);
        try{
            ResultSet result=statement.executeQuery(querry);
            if(result.next()){
                return true;
            }
            return false;
        }
        catch (Exception e){
            System.out.println("erreur verif client");
            return false;
        }
    }
    public boolean SuppClient(int CIN,Statement statement){
        String addQuery="DELETE FROM clients WHERE cinClient="+CIN;
         try{
             
            if (verif(statement,CIN)){
                 statement.executeUpdate(addQuery);
                 refreshClients(statement);
                 return true;
            }
            
        }
        catch (Exception e){
            System.out.println("erreur supp client"); 
        }
         return false;
    }
    public  boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
