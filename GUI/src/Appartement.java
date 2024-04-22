
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author noura
 */
public class Appartement {
    Appartement(){
        
    }
    
     public  void refreshAppartement(Statement statement){
        String query ="Select * from appartement";
        DefaultTableModel model = (DefaultTableModel) GUI.appartementTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }
        
    }
    public void refreshAppartement(int idAppart,float prix,String adresse,int nbSalleDeBain,float superficie,int nb_chambre,String etat,Statement statement,int cinEmp){
        String query ="Select * from appartement where";
        if(idAppart!=0)query+=" idAppartement="+idAppart+" and";
        if(prix!=0)query+=" prix="+prix+" and";
        if(adresse.compareTo("")!=0)query+=" adresse='"+adresse+"' and";
        if(nbSalleDeBain!=0)query+=" nbSalleDeBain="+nbSalleDeBain+" and";
        if(superficie!=0)query+=" superficie="+superficie+" and";
        if(nb_chambre!=0)query+=" nb_chambre="+nb_chambre+" and";
        if(cinEmp!=0)query+=" cinEmployeAj="+cinEmp+" and";
        if(etat.compareTo("tous")!=0)query+=" état='"+etat+"' and";
        query+=" true";
        DefaultTableModel model = (DefaultTableModel) GUI.appartementTable.getModel();
        model.setRowCount(0);
        try{
            ResultSet result=statement.executeQuery(query);
            
            while(result.next()){
                
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8)});
            }
        }
        catch (Exception e){
            System.out.println("najjamtech nzid fel tableau");
        }       
         
     }


    public boolean addNewAppartement(float prix,String adresse,int nbSalleDeBain,float superficie,int nb_chambre,String etat,int CinEmp,Statement statement){
        if(adresse.equals(""))return false;
        if(etat.equals("tous"))return false;
        String addQuery="INSERT INTO appartement(prix,adresse,nbSalleDeBain,superficie,nb_chambre,état,cinEmployeAj) VALUES ("+prix+",'"+adresse+"',"+nbSalleDeBain+","+superficie+","+nb_chambre+",'"+etat+"',"+CinEmp+")";
        System.out.println(addQuery);
         try{
            statement.executeUpdate(addQuery);
            refreshAppartement(statement);
            System.out.println("done");
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public boolean verif(Statement statement,int idAppart){
        String querry="SELECT * FROM appartement  WHERE idAppartement="+idAppart;
        try{
            ResultSet result=statement.executeQuery(querry);
            if(result.next()){
                return true;
            }
            return false;
        }
        catch (Exception e){
            System.out.println("erreur verif appartement");
            return false;
        }
    } 
    public boolean SuppAppartement(int idAppart,Statement statement){
        String addQuery="DELETE FROM appartement WHERE idAppartement="+idAppart;
        System.out.println(addQuery);
         try{
             
            if (verif(statement,idAppart)){
                 statement.executeUpdate(addQuery);
                 refreshAppartement(statement);
                 return true;
            }
            
        }
        catch (Exception e){
            System.out.println("erreur supp app"); 
        }
         return false;
    }
    public void modifAppartement(int idAppart,float prix,String adresse,int nbSalleDeBain,float superficie,int nb_chambre,String etat,Statement statement){
        String query="update appartement set ";
        if (prix!=0)query+="prix="+prix+", ";
        if(adresse.compareTo("")!=0)query+="adresse='"+adresse+"', ";
        if(nbSalleDeBain!=0)query+="nbSalleDeBain="+nbSalleDeBain+", ";
        if(superficie!=0)query+="superficie="+superficie+", ";
        query+="état='"+etat+"' where idAppartement="+idAppart;
        try{
             
            if (verif(statement,idAppart)){
                 statement.executeUpdate(query);
                 refreshAppartement(statement);
            }
            
        }
        catch (Exception e){
            System.out.println("erreur modif app"); 
        }
    }
    public boolean verifDispo(int id_appart,String date_deb,String date_fin ,Statement statement){
        String query="Select date_deb , date_fin ,état from appartement a, location l where a.idAppartement= "+id_appart+" and a.idAppartement=l.idAppartement";
        try{System.out.println(query);
        
            ResultSet result=statement.executeQuery(query);
            System.out.println("2");
            
            if(!result.next()) return true;
            if(result.getString(3).compareTo("rénovation")==0)return false;
            System.out.println("3");
            System.out.println("31");
            do{
                String date_debLocation=result.getString(1);
                String date_finLocation=result.getString(2);
                if (date_deb.compareTo(date_debLocation)>0 && date_deb.compareTo(date_finLocation)<0)return false;
                System.out.println("4");
                if (date_fin.compareTo(date_debLocation)>0 && date_fin.compareTo(date_finLocation)<0)return false;
                System.out.println("5");
                if (date_debLocation.compareTo(date_deb)>0 && date_debLocation.compareTo(date_fin)<0)return false;
                System.out.println("6");
            }while(result.next());
        } 
         catch(Exception e){
                 System.out.println("erreur verifDispo app");
             return false;
        }
        return true;
    }
    public void refreshEtatAppart(Statement statement){
        SimpleDateFormat formatter= new SimpleDateFormat("YYYY-MM-dd");
        Date date =new Date(System.currentTimeMillis());
        String query="update appartement set état='loué' where idAppartement in (Select idAppartement from location l where '"+formatter.format(date)+"'> date_deb and date_retour=NULL)";
        try{
            statement.executeUpdate(query);}
        catch(Exception e){
            System.out.println("erreur refreshEtatAppartement");
        }
    }
    
}


