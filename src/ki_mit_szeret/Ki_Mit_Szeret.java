/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ki_mit_szeret;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Ki_Mit_Szeret {
    
    static class Tetel{
        public int ki;
        public int mit;
    }
    public static void main(String[] args) throws Exception{

        
        Class.forName("com.mysql.jdbc.Driver");
        Connection kapcsolat=DriverManager.getConnection("jdbc:mysql://localhost","root","");
        Statement utasitas1=kapcsolat.createStatement();
        PreparedStatement utasitas2=kapcsolat.prepareStatement("INSERT INTO ki(nev) VALUES(?);");
        utasitas1.execute("CREATE DATABASE IF NOT EXISTS micimacko CHARACTER SET UTF8 COLLATE utf8_general_ci;");
        utasitas1.execute("USE micimacko;");
        utasitas1.execute("CREATE TABLE IF NOT EXISTS ki(\n" +
"			id INT AUTO_INCREMENT PRIMARY KEY, \n" +
"			nev VARCHAR(12) CHARACTER SET UTF8 COLLATE utf8_general_ci) CHARACTER SET UTF8 COLLATE utf8_general_ci;");
        utasitas1.execute("CREATE TABLE IF NOT EXISTS mit(\n" +
"			id INT AUTO_INCREMENT PRIMARY KEY, \n" +
"			megnevezes VARCHAR(12) CHARACTER SET UTF8 COLLATE utf8_general_ci) CHARACTER SET UTF8 COLLATE utf8_general_ci;");
        utasitas1.execute("		 CREATE TABLE IF NOT EXISTS ki_mit_szeret(\n" +
"			ki_id INT,\n" +
"			mit_id INT,\n" +
"			FOREIGN KEY (ki_id) REFERENCES ki(id),\n" +
"			FOREIGN KEY (mit_id) REFERENCES mit(id));");
        String[] kik={"Micimackó","Füles","Malacka","Kanga","Tigris","Nyuszi","Bagoly","Zsebibaba","Róbert Gida"};
        String[] mik={
            "málna",
            "méz",
            "körte",
            "alma",
            "banán",
            "szilva",
            "barack",
            "szamóca",
            "meggy",
            "cseresznye",
            "víz",
            "füge",
            "datolya",
            "citrom",
            "narancs",            
        };
        for (String kik1 : kik) {
            utasitas2.setString(1, kik1);
            utasitas2.execute();
        }
        utasitas2=kapcsolat.prepareStatement("INSERT INTO mit(megnevezes) VALUES(?);");
        for (String mik1 : mik) {
            utasitas2.setString(1, mik1);
            utasitas2.execute();
        }        
        ArrayList<Tetel> ki_mit_szeret=new ArrayList();
        
        int mennyi=(int)Math.floor(Math.random()*(kik.length*mik.length)+1);
        //System.out.println("Ki, mit szeret");
        
        utasitas2=kapcsolat.prepareStatement("INSERT INTO ki_mit_szeret(ki_id,mit_id) VALUES(?,?);");
        for(int i=0;i<mennyi;i++){
            int ki=(int)Math.floor(Math.random()*kik.length)+1;
            int mit=(int)Math.floor(Math.random()*kik.length)+1;
            Tetel t=new Tetel();
            t.ki=ki;
            t.mit=mit;
            boolean egyforma=false;
            for(int j=0;j<ki_mit_szeret.size();j++){
                Tetel a=ki_mit_szeret.get(j);
                if(a.ki==t.ki&&a.mit==t.mit){
                    egyforma=true;
                    break;
                }
            }
            if(!egyforma){
                ki_mit_szeret.add(t);
                //System.out.printf("%d\t%d\n", ki,mit);
                utasitas2.setInt(1, ki);
                utasitas2.setInt(2, mit);
                utasitas2.execute();
            }   
        }
        ResultSet eredmeny=utasitas1.executeQuery("SELECT megnevezes FROM ki,mit,ki_mit_szeret WHERE ki.id=ki_id AND mit.id=mit_id AND nev=\"MICIMACKÓ\";");
        while(eredmeny.next()){
            System.out.println(eredmeny.getString("megnevezes"));
        }
        System.out.println();
        eredmeny=utasitas1.executeQuery("SELECT DISTINCT megnevezes FROM (ki INNER JOIN ki_mit_szeret ON ki.id=ki_id) INNER JOIN mit ON mit.id=mit_id WHERE nev<>\"MICIMACKÓ\" AND megnevezes NOT IN (SELECT megnevezes FROM ki,mit,ki_mit_szeret WHERE ki.id=ki_id AND mit.id=mit_id AND nev=\"MICIMACKÓ\");");
        while(eredmeny.next()){
            System.out.println(eredmeny.getString(1));
        }
    }
    
}