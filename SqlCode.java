package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SqlCode {


    private Connection postGresConn = null;

    public SqlCode() {
        loadDriver();
        getConnection();
    }

    public void loadDriver()
    {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("Couldn't find driver class!");
            cnfe.printStackTrace();
            System.exit(1);
        }
    }

    public Connection getConnection() {
        try {
            postGresConn = DriverManager.getConnection("jdbc:postgresql://pgsql3.mif/studentu", "ruki9058", "") ;
        }
        catch (SQLException sqle) {
            System.out.println("Couldn't connect to database!");
            sqle.printStackTrace();
            return null ;
        }


        return postGresConn ;
    }

    public void closeConnection() {
        try {
            postGresConn.close();
        } catch (SQLException e) {
            System.out.println("Error while closing connection to database");
        }
    }

    public List<List> GetDatabase(String query) throws Exception{
        Statement stmt = null;
        ResultSet rs = null;
        List<List> result = new LinkedList<List>();

        try
        {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next())
            {
                List<String> row = new LinkedList<String>();
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++)
                {
                    row.add(rs.getString(i));
                }
                result.add(row);
            }
        }
        catch (SQLException e)
        {
            if (!e.getMessage().equals("No results were returned by the query."))
            {
                throw new SQLException(e.getMessage());
            }
        }
        finally
        {
            try {
                if(null != rs)
                {
                    rs.close();
                }
                if(null != stmt)
                {
                    stmt.close();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Unexpected SQL Error");
            }
        }
        return result;
    }

    public void IsLast(int info) throws SQLException {
        MainCode main = new MainCode();
        List<List> obj = new LinkedList<List>();
        String string = null;
        String stringGyv = "nothing";
        List<Integer> Id = new LinkedList();
        Scanner in = new Scanner(System.in);
        int ID;
        try {
            postGresConn.setAutoCommit(false);

            obj = GetDatabase("SELECT Pareigos FROM ruki9058.Priziuretojai WHERE ID = " + info + ";");
            for (List col : obj) {
                string = (String) col.get(0);
            }

            GetDatabase("DELETE FROM ruki9058.Priziuretojai WHERE ID = " + info + " ;");

            if (string != null && string.equals("Pgr. Priziuretojas")) {
                obj = GetDatabase("SELECT ID, Vardas FROM ruki9058.Gyvunai WHERE Pgr_priziuretojo_ID  = " + info + ";");
                for (List col : obj) {
                    stringGyv = (String) "Gyvuno ID: " + col.get(0) + " vardas: " + col.get(1) + "\n";
                }
                if (!stringGyv.equals("nothing")) {
                    System.out.println("Pries atleidziant pgr. priziuretoja priskirkite siems gyvunams nauja priziuretoja (iveskite jo ID)");
                    for (List col : obj) {
                        stringGyv = (String) "Gyvuno ID: " + col.get(0) + " vardas: " + col.get(1);
                        System.out.println(stringGyv);
                    }
                    obj = GetDatabase("SELECT ID FROM ruki9058.Priziuretojai WHERE Pareigos = 'Pgr. Priziuretojas';");
                    for (List col : obj) {
                        Id.add(Integer.valueOf(String.valueOf(col.get(0))));
                    }
                    if (!Id.isEmpty()) {
                        while (true) {
                            try {
                                ID = in.nextInt();
                                in.nextLine();
                                if (Id.contains(ID)) {
                                    break;
                                } else {
                                    main.showPriziuretojai();
                                    System.out.println("Ivestas ID privalo egzistuoti:");
                                    continue;
                                }
                            } catch (Exception ex) {
                                System.out.println("ID privalo buti ir priklausyti Pgr. Priziuretojui :");
                                in.nextLine();
                            }
                        }
                        try {
                            GetDatabase("UPDATE ruki9058.Gyvunai SET Pgr_priziuretojo_ID = " + ID + "  WHERE Pgr_priziuretojo_ID  = " + info + ";");
                        } catch (SQLException ee) {
                            ee.printStackTrace();
                            postGresConn.rollback();
                            throw ee;
                        }
                    } else {
                        throw new SQLException("Negalima atleisti paskutinio pagrindinio priziuretojo, nes nera, kam palikti gyvunu!");
                    }
                }
            }
            postGresConn.commit();
            System.out.println("Pakeitimai ivykdyti sekmingai");
        } catch (SQLException e) {
            postGresConn.rollback();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postGresConn.setAutoCommit(true);
        }
    }

}
