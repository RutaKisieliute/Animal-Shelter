package com.company;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainCode {
    SqlCode sql = new SqlCode();
    List<List> obj = new LinkedList<List>();
    Scanner in = new Scanner(System.in);;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    int ID;
    String vardas;
    String pavarde;
    String pareigos;
    String telefonoNumeris;
    String elPastas;
    String adresas;
    String rusis;
    String veisle;
    String spalva;
    String lytis;
    int pgrPriziuretojoID;
    int medicininesInfoID;
    String sveikatosBukle;
    Date paskutinesMedApziurosData;
    Date paskutinesVakcinacijosData;
    String sterializacijosStatusas;
    int info;
    String string;
    List<Integer> Id = new LinkedList();
    int option;


    public void UserInterface () throws SQLException {
        do {
            System.out.println("pasirinkite ka norite atlikti:");
            System.out.println("1 - Perziureti gyvunu informacija");
            System.out.println("2 - Perziureti gyvuno medicinine informacija\"");
            System.out.println("3 - Perziureti priziuretoju informacija\"");
            System.out.println("4 - Uzregistruoti naja gyvuna");
            System.out.println("5 - Uzregistruoti nauja darbuotoja");
            System.out.println("6 - Atleisti darbuotoja");
            System.out.println("7 - Atnaujinti gyvuno medicinine informacija");
            System.out.println("8 - Paziureti tam tikro Pgr. priziuretojo gyvunus");
            System.out.println("9 - Uzdaryti programa");

            while (true) {
                try {
                    option = in.nextInt();
                    in.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("pasirinkimas privalo buti skaicius, iveskite dar karta:");
                    in.nextLine();
                }
            }
            switch (option) {
                case 1:
                    showGyvunai();
                    break;
                case 2:
                    showMedicinineInfo();
                    break;
                case 3:
                    showPriziuretojai();
                    break;
                case 4:
                    showGyvunai();
                    showMedicinineInfo();
                    addGyvunaiAndMedInfo();
                    break;
                case 5:
                    showPriziuretojai();
                    addPPriziuretojas();
                    break;
                case 6:
                    showPriziuretojai();
                    deletePriziuretojai();
                    break;
                case 7:
                    showMedicinineInfo();
                    updateMedicinineInfo();
                    break;
                case 8:
                    searchGyvunai();
                    break;
                case 9:
                    System.out.println("Atsijungiama nuo programos");
                    break;
                default:
                    System.out.println("Jusu pasirinkimas nera validus, rinkites dar karta");
            }
        }while(option != 9);
    }

    public void  showGyvunai() {
        List<List> obj = new LinkedList<List>();
        System.out.println("Gyvunai lentele:");

        try {
            obj = sql.GetDatabase("Select * from ruki9058.Gyvunai;");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|   ID   |      Vardas      |    Rusis    |       Veisle       |      Spalva      |   Lytis   | Pgr_priziuretojo_ID | Medicinines_info_ID | Globejo_ID |");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (List col : obj) {
                System.out.printf("| %-6s | %-16s | %-11s | %-18s | %-16s | %-9s | %-19s | %-19s | %-10s | \n", col.get(0), col.get(1), col.get(2), col.get(3), col.get(4), col.get(5), col.get(6), col.get(7), col.get(8));
            }
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void  showPriziuretojai() {
        List<List> obj = new LinkedList<List>();
        System.out.println("Priziuretoju lentele:");

        try {
            obj = sql.GetDatabase("Select * from ruki9058.Priziuretojai;");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|   ID   |      Vardas      |      Pavarde      |        Pareigos        | Telefono_numeris |         El_pastas         |          Adresas          | ");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");
            for (List col : obj) {
                System.out.printf("| %-6s | %-16s | %-17s | %-22s | %-16s | %-25s | %-25s | \n", col.get(0), col.get(1), col.get(2), col.get(3), col.get(4), col.get(5), col.get(6));
            }
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void  showMedicinineInfo() {
        List<List> obj = new LinkedList<List>();
        System.out.println("Medicinines informacijos lentele:");

        try {
            obj = sql.GetDatabase("Select * from ruki9058.Medicinine_informacija;;");
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            System.out.println("|   ID   | Sveikatos_bukle | Paskutines_med_apziuros_data | Paskutines_vakcinacijos_data | Sterializacijos_statusas |");
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            for (List col : obj) {
                System.out.printf("| %-6s | %-15s | %-28s | %-28s | %-24s | \n", col.get(0), col.get(1), col.get(2), col.get(3), col.get(4));
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addPPriziuretojas(){
        System.out.println("Naujo priziuretojo duomenys");

        try {
            System.out.println("Iveskite darbuotojo ID: ");
            while (true) {
                try {
                    ID = in.nextInt();
                    in.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("ID privalo buti skaicius, iveskite dar karta:");
                    in.nextLine();
                }
            }
            System.out.println("Iveskite darbuotojo varda: ");
            vardas = in.nextLine();
            System.out.println("Iveskite darbuotojo pavarde: ");
            pavarde = in.nextLine();
            System.out.println("Iveskite darbuotojo pareigas: ");
            pareigos = in.nextLine();
            System.out.println("Iveskite darbuotojo telefono numeri (370 formatu, + rasyti nereikia): ");
            while (true) {
                try {
                    telefonoNumeris =  '+'  + Long.toString( in.nextLong());
                    in.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("Telefono numeris privalo buti skaicius, iveskite dar karta:");
                    in.nextLine();
                }
            }
            System.out.println("Iveskite darbuotojo el pasta: ");
            elPastas = in.nextLine();
            System.out.println("Iveskite darbuotojo adresa: ");
            adresas = in.nextLine();

            sql.GetDatabase("INSERT INTO ruki9058.Priziuretojai VALUES('"+ ID +"','"+ vardas +"', '"+  pavarde +"', '"+  pareigos +"', '"+  telefonoNumeris +"', '"+  elPastas +"', '"+  adresas + "');");
            System.out.println("Priziuretojo informacija ivesta teisingai");
        }
        catch (Exception e) {
            while(true){
                if (e.getMessage().contains("violates check constraint \"val_pastas\"")) {
                    System.out.println("Iveskite el pasta dar karta (privalomas formatas: smth@smth.smt) ");
                    elPastas = in.nextLine();
                    try {
                        sql.GetDatabase("INSERT INTO ruki9058.Priziuretojai VALUES('" + ID + "','" + vardas + "', '" + pavarde + "', '" + pareigos + "', '" + telefonoNumeris + "', '" + elPastas + "', '" + adresas + "');");
                        System.out.println("Priziuretojo informacija ivesta teisingai");
                        break;
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                        if (!ex.getMessage().contains("violates check constraint \"val_pastas\"")) {
                            e = ex;
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            while(true){
                if (e.getMessage().contains("violates check constraint \"val_tel_numeris\"")) {
                    System.out.println("Iveskite telefono numeri dar karta (privalomas formatas: prasideda 370) ");
                    while (true) {
                        try {
                            telefonoNumeris =  '+'  + Long.toString( in.nextLong());
                            in.nextLine();
                            break;
                        } catch (Exception ee) {
                            System.out.println("Telefono numeris privalo buti skaicius, iveskite dar karta:");
                            in.nextLine();
                        }
                    }
                    try {
                        sql.GetDatabase("INSERT INTO ruki9058.Priziuretojai VALUES('" + ID + "','" + vardas + "', '" + pavarde + "', '" + pareigos + "', '" + telefonoNumeris + "', '" + elPastas + "', '" + adresas + "');");
                        System.out.println("Priziuretojo informacija ivesta teisingai");
                        break;
                    } catch (Exception ex) {
                        if (!ex.getMessage().contains("violates check constraint \"val_tel_numeris\"")) {
                            e = ex;
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            while (e.getMessage().contains("duplicate key value violates unique constraint \"priziuretojai_pkey\"")) {
                System.out.println("Sis ID jau egzistuoja, iveskite kita:");
                while (true) {
                    try {
                        ID = in.nextInt();
                        in.nextLine();
                        break;
                    } catch (Exception ex) {
                        System.out.println("ID privalo buti skaicius:");
                        in.nextLine();
                    }
                }
                try {
                    sql.GetDatabase("INSERT INTO ruki9058.Priziuretojai VALUES('" + ID + "','" + vardas + "', '" + pavarde + "', '" + pareigos + "', '" + telefonoNumeris + "', '" + elPastas + "', '" + adresas + "');");
                    System.out.println("Priziuretojo informacija ivesta teisingai");
                    break;
                } catch (Exception ex) {
                    e = ex;
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    public void addGyvunaiAndMedInfo() throws SQLException{
        System.out.println("Naujo gyvuno duomenys");

        try {
            System.out.println("Iveskite gyvuno ID: ");
            while (true) {
                try {
                    ID = in.nextInt();
                    in.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("ID privalo buti skaicius, iveskite dar karta:");
                    in.nextLine();
                }
            }
            System.out.println("Iveskite gyvuno varda: ");
            vardas = in.nextLine();
            System.out.println("Iveskite gyvuno rusis ");
            rusis = in.nextLine();
            System.out.println("Iveskite gyvuno veisle: ");
            veisle = in.nextLine();
            System.out.println("Iveskite gyvuno spalva: ");
            spalva = in.nextLine();
            System.out.println("Iveskite gyvuno lytis ");
            lytis = in.nextLine();
            System.out.println("Iveskite gyvuno pgr_priziuretojo_ID ");
            obj  = sql.GetDatabase("SELECT ID FROM ruki9058.Priziuretojai WHERE Pareigos = 'Pgr. Priziuretojas';");
            for (List col : obj) {
                Id.add(Integer.valueOf(String.valueOf(col.get(0))));
            }
            while (true) {
                try {
                    pgrPriziuretojoID = in.nextInt();
                    in.nextLine();
                    if(Id.contains(pgrPriziuretojoID)){
                        break;
                    }
                    else{
                        showPriziuretojai();
                        System.out.println("Ivestas ID privalo egzistuoti:");
                        continue;
                    }
                } catch (Exception ex) {
                    System.out.println("ID privalo buti ir priklausyti Pgr. Priziuretojui :");
                    in.nextLine();
                }
            }
            System.out.println("Iveskite gyvuno Medicinines_info_ID ");
            while (true) {
                try {
                    medicininesInfoID = in.nextInt();
                    in.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("ID privalo buti skaicius, iveskite dar karta:");
                    in.nextLine();
                }
            }
            System.out.println("Iveskite gyvuno sveikatos bukle: ");
            sveikatosBukle = in.nextLine();
            System.out.println("Iveskite gyvuno paskutines med apziurosdata: ");
            paskutinesMedApziurosData = validateDateInput();
            System.out.println("Iveskite gyvuno paskutines vakcinacijos data: ");
            paskutinesVakcinacijosData = validateDateInput();
            System.out.println("Iveskite gyvuno sterializacijos statusas: ");
            sterializacijosStatusas = in.nextLine();

            sql.GetDatabase("INSERT INTO ruki9058.Medicinine_informacija VALUES('"+ medicininesInfoID +"','"+ sveikatosBukle +"', '"+  paskutinesMedApziurosData +"', '"+  paskutinesVakcinacijosData +"', '"+  sterializacijosStatusas +"');");
            sql.GetDatabase("INSERT INTO ruki9058.Gyvunai VALUES('"+ ID +"','"+ vardas +"', '"+  rusis +"', '"+  veisle +"', '"+  spalva +"', '"+  lytis +"', '"+  pgrPriziuretojoID + "', '"+  medicininesInfoID + "', NULL);");
            System.out.println("Priziuretojo informacija ivesta teisingai");
        }
        catch (Exception e) {
            while(true){
                if (e.getMessage().contains("violates check constraint \"val_apziuros_data\"")) {
                    System.out.println("Iveskite paskutines med apziuros data dar karta (data negali buti ateityje) ");
                    while (true) {
                        try {
                            paskutinesMedApziurosData =  validateDateInput();
                            break;
                        } catch (Exception ee) {
                            System.out.println("Telefono numeris privalo buti skaicius, iveskite dar karta:");
                            in.nextLine();
                        }
                    }
                    try {
                        sql.GetDatabase("INSERT INTO ruki9058.Medicinine_informacija VALUES('"+ medicininesInfoID +"','"+ sveikatosBukle +"', '"+  paskutinesMedApziurosData +"', '"+  paskutinesVakcinacijosData +"', '"+  sterializacijosStatusas +"');");
                        sql.GetDatabase("INSERT INTO ruki9058.Gyvunai VALUES('"+ ID +"','"+ vardas +"', '"+  rusis +"', '"+  veisle +"', '"+  spalva +"', '"+  lytis +"', '"+  pgrPriziuretojoID + "', '"+  medicininesInfoID + "', NULL);");
                        System.out.println("Priziuretojo informacija ivesta teisingai");
                        break;
                    } catch (Exception ex) {
                        if (!ex.getMessage().contains("violates check constraint \"val_apziuros_data\"")) {
                            e = ex;
                            break;
                        }
                    }
                } else {
                    System.out.println("Error: " + e.getMessage());
                    break;
                }
            }
            while(true){
                if (e.getMessage().contains("violates check constraint \"val_vakcinacijos_data\"")) {
                    System.out.println("Iveskite paskutines vakcinacijos data dar karta (data negali buti senesne, nei paskutine apziuros data) ");
                    while (true) {
                        try {
                            paskutinesVakcinacijosData =  validateDateInput();
                            break;
                        } catch (Exception ee) {
                            System.out.println("Telefono numeris privalo buti skaicius, iveskite dar karta:");
                            in.nextLine();
                        }
                    }
                    try {
                        sql.GetDatabase("INSERT INTO ruki9058.Medicinine_informacija VALUES('"+ medicininesInfoID +"','"+ sveikatosBukle +"', '"+  paskutinesMedApziurosData +"', '"+  paskutinesVakcinacijosData +"', '"+  sterializacijosStatusas +"');");
                        sql.GetDatabase("INSERT INTO ruki9058.Gyvunai VALUES('"+ ID +"','"+ vardas +"', '"+  rusis +"', '"+  veisle +"', '"+  spalva +"', '"+  lytis +"', '"+  pgrPriziuretojoID + "', '"+  medicininesInfoID + "', NULL);");
                        System.out.println("Priziuretojo informacija ivesta teisingai");
                        break;
                    } catch (Exception ex) {
                        if (!ex.getMessage().contains("violates check constraint \"val_vakcinacijos_data\"")) {
                            e = ex;
                            System.out.println("Error: " + e.getMessage());
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            while (e.getMessage().contains("duplicate key value violates unique constraint \"medicinine_informacija_pkey\"")) {
                System.out.println("Sis medicininis ID jau egzistuoja, iveskite kita:");
                while (true) {
                    try {
                        medicininesInfoID = in.nextInt();
                        in.nextLine();
                        break;
                    } catch (Exception ex) {
                        System.out.println("ID privalo buti skaicius:");
                        in.nextLine();
                    }
                }
                try {
                    sql.GetDatabase("INSERT INTO ruki9058.Medicinine_informacija VALUES('"+ medicininesInfoID +"','"+ sveikatosBukle +"', '"+  paskutinesMedApziurosData +"', '"+  paskutinesVakcinacijosData +"', '"+  sterializacijosStatusas +"');");
                    sql.GetDatabase("INSERT INTO ruki9058.Gyvunai VALUES('"+ ID +"','"+ vardas +"', '"+  rusis +"', '"+  veisle +"', '"+  spalva +"', '"+  lytis +"', '"+  pgrPriziuretojoID + "', '"+  medicininesInfoID + "', NULL);");
                    System.out.println("Informacija ivesta teisingai");
                    break;
                } catch (Exception ex) {
                    e = ex;
                    System.out.println("Error: " + e.getMessage());
                }
            }
            while (e.getMessage().contains("duplicate key value violates unique constraint \"gyvunai_pkey\"")) {
                System.out.println("Sis Gyvuno ID jau egzistuoja, iveskite kita:");
                while (true) {
                    try {
                        ID = in.nextInt();
                        in.nextLine();
                        break;
                    } catch (Exception ex) {
                        System.out.println("ID privalo buti skaicius:");
                        in.nextLine();
                    }
                }
                try {
                    sql.GetDatabase("INSERT INTO ruki9058.Gyvunai VALUES('"+ ID +"','"+ vardas +"', '"+  rusis +"', '"+  veisle +"', '"+  spalva +"', '"+  lytis +"', '"+  pgrPriziuretojoID + "', '"+  medicininesInfoID + "', NULL);");
                    System.out.println("Informacija ivesta teisingai");
                    break;
                } catch (Exception ex) {
                    e = ex;
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private Date validateDateInput() {
        while (true) {
            String input = in.nextLine();
            try {
                date = dateFormat.parse(input);
                break;
            } catch (ParseException e) {
                System.out.println("Blogas datos formatas. Formatas privalo buti: yyyy-MM-dd:");
            }
        }
        return date;
    }

    public void deletePriziuretojai (){
        String stringGyv = "nothing";
        System.out.println("Iveskite ID priziuretojo, kuri norite atleisti: ");
        while (true) {
            try {
                info = in.nextInt();
                in.nextLine();
                break;
            } catch (Exception ex) {
                System.out.println("ID privalo buti skaicius:");
                in.nextLine();
            }
        }

        try {
            sql.IsLast(info);
        } catch (SQLException s) {
            System.out.println(s);
        } catch (Exception ee) {
        }
    }

    public void updateMedicinineInfo (){
        System.out.println("Iveskite ID medicininio iraso, kuri norite atnaujinti: ");
        try {
            obj  = sql.GetDatabase("SELECT ID FROM ruki9058.Medicinine_informacija;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (List col : obj) {
            Id.add(Integer.valueOf(String.valueOf(col.get(0))));
        }
        while (true) {
            try {
                info = in.nextInt();
                in.nextLine();
                if(Id.contains(info)){
                    break;
                }
                else {
                    System.out.println("ID privalo egzistuoti:");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("ID privalo buti skaicius ir egzistuoti:");
                in.nextLine();
            }
        }
        System.out.println("pasirinkite ka norite atnaujinti:");
        System.out.println("1 - svekatos bukle ir paskutines apziuros data");
        System.out.println("2 - paskutines vakcinacijos data");
        System.out.println("3 - sterializacijos statusa");
        while (true) {
            try {
                option = in.nextInt();
                in.nextLine();
                if(option > 0 && option < 4){
                    break;
                }
                else{
                    System.out.println("Pasirinkimas privalo egzistuoti:");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("pasirinkimas privalo buti skaicius, iveskite dar karta:");
                in.nextLine();
            }
        }
        switch (option) {
            case 1 :
                System.out.println("Iveskite gyvuno sveikatos bukle: ");
                sveikatosBukle = in.nextLine();
                System.out.println("Iveskite gyvuno paskutines med apziuros data: ");
                paskutinesMedApziurosData = validateDateInput();
                try {
                    sql.GetDatabase("UPDATE ruki9058.Medicinine_informacija SET Sveikatos_bukle = '" + sveikatosBukle + "', Paskutines_med_apziuros_data = '" + paskutinesMedApziurosData + "'  WHERE ID = "+ info + ";");
                    System.out.println("Atnaujinta sekmingai");
                } catch (Exception e) {
                    if (e.getMessage().contains("violates check constraint \"val_apziuros_data\"")) {
                        System.out.println("Ivestis negalima, paskutines med apziuros data negali buti ateityje");
                    }
                    else if (e.getMessage().contains("violates check constraint \"val_vakcinacijos_data\"")) {
                        System.out.println("Ivestis negalima, paskutine apziuros data negali buti ankstesne, nei paskutines vakcinacijos data");
                    }
                    else {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                System.out.println("Iveskite gyvuno paskutines vakcinacijos data: ");
                paskutinesVakcinacijosData = validateDateInput();
                try {
                    sql.GetDatabase("UPDATE ruki9058.Medicinine_informacija SET Paskutines_vakcinacijos_data = '" + paskutinesVakcinacijosData + "' WHERE ID = "+ info + ";");
                    System.out.println("Atnaujinta sekmingai");
                } catch (Exception e) {
                    if (e.getMessage().contains("violates check constraint \"val_vakcinacijos_data\"")) {
                        System.out.println("Ivestis negalima, paskutines vakcinacijos data negali buti senesne, nei paskutine apziuros data");
                    }
                    else {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                System.out.println("Iveskite gyvuno sterializacijos statusas: ");
                sterializacijosStatusas = in.nextLine();
                try {
                    sql.GetDatabase("UPDATE ruki9058.Medicinine_informacija SET Sterializacijos_statusas = '" + sterializacijosStatusas + "' WHERE ID = "+ info + ";");
                    System.out.println("Atnaujinta sekmingai");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void searchGyvunai (){
        System.out.println("Iveskite Pgr. priziuretojo ID, kurio gyvunus norite matyti: ");
        while (true) {
            try {
                ID = in.nextInt();
                in.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("ID privalo buti skaicius, iveskite dar karta:");
                in.nextLine();
            }
        }
        try {
            obj = sql.GetDatabase("SELECT * FROM ruki9058.Gyvunai WHERE Pgr_priziuretojo_ID  = " + ID +";");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|   ID   |      Vardas      |    Rusis    |       Veisle       |      Spalva      |   Lytis   | Pgr_priziuretojo_ID | Medicinines_info_ID | Globejo_ID |");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (List col : obj) {
                System.out.printf("| %-6s | %-16s | %-11s | %-18s | %-16s | %-9s | %-19s | %-19s | %-10s | \n", col.get(0), col.get(1), col.get(2), col.get(3), col.get(4), col.get(5), col.get(6), col.get(7), col.get(8));
            }
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}