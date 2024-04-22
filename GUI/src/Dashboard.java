/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ousse
 */
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Dashboard {
    protected int clients=0;
    protected int locations=0;
    protected int appartTot=0;
    protected int appartDispo=0;
    protected String revTot;
    
    public void update(Statement statement,int penalite){
        updateClients(statement);
        updateRevTot(statement,penalite);
        updateLocations(statement);
        updateAppartTot(statement);
        updateAppartDispo(statement);
        topAppartements(statement);
        cliendFid(statement);
    }
    private void updateClients(Statement statement){
        String querry="SELECT count(*) FROM clients";
        try{
            ResultSet result=statement.executeQuery(querry);
            result.next();
            clients=result.getInt(1);
        }
        catch(Exception e) {
            System.out.print("erreur update cleints dash");
        }
    }
    private void updateRevTot(Statement statement,int penalite){
        String querry="SELECT sum(prix*datediff(date_fin,date_deb)+datediff(date_retour,date_fin)*"+penalite+") FROM location l,appartement a WHERE a.idAppartement=l.idAppartement "+getMonthInterval();
        try{
            System.out.println(querry);
            ResultSet result=statement.executeQuery(querry);
            result.next();
            revTot=result.getString(1);
        }
        catch(Exception e) {
            System.out.print("erreur update rev tot dash");
        }
    }
    private void updateAppartDispo(Statement statement){
        String querry="SELECT count(*) FROM appartement WHERE état='libre'";
        try{
            ResultSet result=statement.executeQuery(querry);
            result.next();
            appartDispo=result.getInt(1);
        }
        catch(Exception e) {
            System.out.print("erreur update appart dispo dash");
        }
    }
    
    private void updateAppartTot(Statement statement){
        String querry="SELECT count(*) FROM appartement";
        try{
            ResultSet result=statement.executeQuery(querry);
            result.next();
            appartTot=result.getInt(1);
        }
        catch(Exception e) {
            System.out.print("erreur update appart tot dash");
        }
    }
    private void updateLocations(Statement statement){
        String querry="SELECT count(*) FROM location WHERE date_deb "+getMonthInterval();
        try{
            ResultSet result=statement.executeQuery(querry);
            result.next();
            locations=result.getInt(1);
        }
        catch(Exception e) {
            System.out.print("erreur update location dash");
        }
    }
    public void topAppartements(Statement statement){
        String query="select location.idAppartement , adresse , nb_chambre , count(*) 'n_loc' from location , appartement where location.idAppartement=appartement.idAppartement group by idAppartement order by n_loc DESC";
        DefaultTableModel model = (DefaultTableModel) GUI.appartementTop.getModel();
        model.setRowCount(0);
        try {
            ResultSet result=statement.executeQuery(query);
            while(result.next()){
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(4)});

            }
            model.setRowCount(3);
        }
        catch(Exception e){
            System.out.println("erreur top appartement");
        }
    }
    public void cliendFid(Statement statement){
                String query="select location.cinClient , nomClient , prenomClient , count(*) 'n_loc' from location , clients where location.cinClient=clients.cinClient group by cinClient order by n_loc DESC";
        DefaultTableModel model = (DefaultTableModel) GUI.client_fid.getModel();
        model.setRowCount(0);
        try {
            ResultSet result=statement.executeQuery(query);
            while(result.next()){
                model.addRow(new Object[]{result.getString(1),result.getString(2),result.getString(3),result.getString(4)});

            }
            model.setRowCount(3);
        }
        catch(Exception e){
            System.out.println("erreur top client");
        }
    }
    private String getMonthInterval(){
        int month;
        int daysInMonth;
        int year;
        int[] daysArray = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            SimpleDateFormat formatter= new SimpleDateFormat("MMYYYY");
            Date date =new Date(System.currentTimeMillis());
        month=Integer.valueOf(formatter.format(date).substring(0,2));
        year=Integer.valueOf(formatter.format(date).substring(2,6));
        daysInMonth = daysArray[month - 1];
        if (month==2 && isLeapYear(year)) {
            daysInMonth ++;
        }
        return "between '"+String.valueOf(year)+"-"+String.valueOf(month)+"-1' and '"+String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(daysInMonth)+"'";
        }
    private boolean isLeapYear(int year){
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0)
                    return true;
                 else
                    return false;
            }
        else
            return true;
        }
        else
            return false;
    }
}
