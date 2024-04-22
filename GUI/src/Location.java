/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ousse
 */
public class Location {
    Location(){
        
    }
    public void refreshLocation(Statement statement){
        String query="Select * from Location";
        DefaultTableModel model = (DefaultTableModel) GUI.locationTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(5),result.getString(4),result.getString(6),result.getString(7)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }
    }
    public void refreshLocation(int idLocation,String date_deb,String date_fin,int CIN_client,int idAppart,Statement statement,int cinEmp){
        String query ="Select * from appartement where";
        if(idLocation!=0)query+=" idlocation="+idLocation+" and";
        if(isValidDate(date_deb) && date_deb.compareTo("")!=0)query+=" date_deb="+date_deb+" and";
        if(isValidDate(date_fin) && date_fin.compareTo("")!=0)query+=" date_fin='"+date_fin+"' and";
        if(CIN_client!=0)query+=" cinClient="+CIN_client+" and";
        if(idAppart!=0)query+=" idAppartement="+idAppart+" and";
        if(cinEmp!=0)query+=" cinEmployeAj="+cinEmp+" and";
        query+=" true";
        DefaultTableModel model = (DefaultTableModel) GUI.locationTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(5),result.getString(4),result.getString(6),result.getString(7)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }       
         
     }
    public boolean addNewLocation(String date_deb,String date_fin,int cinClient,int idAppartement,int cinEmp,Statement statement){
        
        String addQuery="INSERT INTO location(date_deb,date_fin,cinClient,idAppartement,cinEmploye) values('"+date_deb+"','"+date_fin+"',"+cinClient+","+idAppartement+","+cinEmp+")";
        if(!GUI.clientP.verif(statement, cinClient))return false;
        if(!GUI.appartementP.verifDispo(idAppartement, date_deb, date_fin, statement))return false;
        if(isValidDate(date_fin) && isValidDate(date_deb) && date_deb.compareTo(date_fin)>0)return false;
        System.out.println(addQuery);
         try{
            statement.executeUpdate(addQuery);
            refreshLocation(statement);
            System.out.println("done");
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
      public boolean verif(Statement statement,int idLocation){
        String querry="SELECT * FROM location  WHERE idLocation="+idLocation;
        try{
            ResultSet result=statement.executeQuery(querry);
            if(result.next()){
                return true;
            }
            return false;
        }
        catch (Exception e){
            System.out.println("erreur verif location");
            return false;
        }
    } 
      public boolean SuppLocation(int idLocation,Statement statement){
        String addQuery="DELETE FROM location WHERE idLocation="+idLocation;
        System.out.println(addQuery);
         try{
             
            if (verif(statement,idLocation)){
                 statement.executeUpdate(addQuery);
                 refreshLocation(statement);
                 return true;
            }
            
        }
        catch (Exception e){
            System.out.println("erreur supp app"); 
        }
         return false;
    }
      public  void modifLocation(int idLocation,String date_deb,String date_fin,int CINclient,int idAppart, Statement statement){
        String query="update location set ";
        if (date_deb.compareTo("")!=0)query+="date_deb="+date_deb+", ";
        if(date_fin.compareTo("")!=0)query+="date_fin='"+date_fin+"', ";
        if(idAppart!=0)query+="idAppartement="+idAppart+", ";
        if(CINclient!=0)query+="cinClient="+CINclient;
        try{
             
            if (verif(statement,idAppart)){
                 statement.executeUpdate(query);
                 refreshLocation(statement);
            }
            
        }
        catch (Exception e){
            System.out.println("erreur modif app"); 
        }
    }
    public void finaliserLocation(int idLocation,Statement statement){
        SimpleDateFormat formatter= new SimpleDateFormat("YYYY-MM-dd");
        Date date =new Date(System.currentTimeMillis());
        try{
        String query="Update location set date_retour='"+formatter.format(date)+"' where idLocation="+idLocation;
        System.out.println(query);
        statement.executeUpdate(query);
        query="update appartement set état='libre' where idAppartement in (select idAppartement from location where idLocation="+idLocation;
        statement.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println("erreur finaliserLocation");
        }
     }

    public static boolean isValidDate(String dateStr) {
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
