import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.*;  
import java.io.File;


public class Mappa
{
    private Creature[] ismon;   //Contiene la squadra del giocatore
    private Creature[][] box; //Is the player box
    private TeamList team = new TeamList();
    private Bag bag = new Bag();

    private Scanner muovi = new Scanner(System.in);         //Usato per gli input di movimento ed interazione con eventi
    private Scanner scegli = new Scanner(System.in);        //Usato per selezionare una scelta
    private Scanner continua = new Scanner(System.in);  //Usato per mettere una pausa tra gli output

    private FileReader readermaps;  //Legge il File delle mappe
    private FileReader readerevents;    //Legge il File degli eventi
    private FileReader readercommevents;    //Legge il File degli eventi comuni
    private FileReader readertiles;     //Legge il File dei Tileset
    private FileReader readercolors;    //Legge il File dei Colori
    private FileReader readergifts; //Legge il File dei pokémon ricevuti

    private Properties propmaps;    
    private Properties propevents;
    private Properties propcommevents;
    private Properties proptiles;
    private Properties propcolors;
    private Properties propgifts;

    private String tiles;   //I tileset e le loro informazioni
    private String colors;  //I colori usati nelle mappe
    private String reset = "\u001B[0m"; //Resetta il testo al colore originale
    private String eventi;
    private String runeventi;
    private String stepeventi;
    private String mongifts;

    private String home; //il percorso per i file del programma
    private String fs; //I separatori del percorso

    private int nMappe;
    private VMaps[] mappeviste;

    private char map[][];
    private int mapx;
    private int mapy;
    private String colorimappa;
    private int mapid = 0;                      //Usato per tenere in check in quale mappa si trova il giocatore
    private int[] var = new int[200];           //Variabili usabili in ogni mappa
    private int[][] mapvar;             //Variabili locali usabili in una specifica mappa
    private String[] wordvar = new String[200]; //Variabili globali per tenere informazioni testuali
    private String salvaTesto = null;   //Keeps text to be shown during a choice

    private int px;
    private int py;
    private char direction = '^'; //Keeps track of the direction the player is facing. Helps when the player has to interact with multiple touch events adiacent to him

    private char playericon = '$';  //Il carattere che rappresenta il Giocatore e la sua posizione nella mappa
    private char steptile = '.';    //Il tile che viene messo sulle vecchie coordinate del Giocatore quando questi si sposta

    private boolean ifCheck = true;    //usato per controllare se la condizione richiesta da un 'IF' è stata raggiunta
    private boolean loopCheck = false; //usato per controllare se è necessario effetturare un loop
    private int ifSkip = -1;
    private int elseSkip = -1;
    private int loopID = -1;

    private boolean swim = false;

	public Mappa(Creature[] ismon)throws Exception
	{
        this.ismon = ismon;
        this.box = new Creature[30][30];

        home = System.getProperty("user.dir");
        home = home.substring(0, home.length()-5);
        fs = File.separator;

        nMappe = new File(home + fs + "data" + fs + "maps").list().length-1;
        mapvar = new int[nMappe][100];
        //mappeviste = new VMaps[nMappe];

        try
        {
            readertiles = new FileReader(new File(home + fs + "data" + fs + "tiles.txt"));
            readercolors = new FileReader(new File(home + fs + "data" + fs + "colors.txt"));
            readercommevents = new FileReader(new File(home + fs + "data" + fs + "mapevents" + fs + "commonevents.txt"));
            readergifts = new FileReader(new File(home + fs + "data" + fs + "giftmon.txt"));
        }
        catch(FileNotFoundException fnfe)
        {
        }

        propcolors = new Properties();
        proptiles = new Properties();
        propcommevents = new Properties();
        propgifts = new Properties();

        try
        {
            proptiles.load(readertiles);
            propcolors.load(readercolors);
            propcommevents.load(readercommevents);
            propgifts.load(readergifts);
        }
        catch(IOException ioe)
        {
        }

        tiles = proptiles.getProperty("tiles");
        colors = propcolors.getProperty("color");
        mongifts = propgifts.getProperty("gift");

        mapid = 0;
        px = 5;
        py = 8;
        changeMap(mapid);
    }

    //Questa funzione è chiamata ogni volta che si deve cambiare la mappa in cui si trova il giocatore
    public void changeMap(int mapid)
    {
        int i = 0;
        int j = 0;

        //Vengono richiamati i File contenenti la Mappa e gli eventi al suo interno

        this.mapid = mapid;

        /*if(mappeviste[mapid] != null)
        {
            map = mappeviste[mapid].getMap();
            colorimappa = mappeviste[mapid].getColori();
            eventi = mappeviste[mapid].getEventi();
            runeventi = mappeviste[mapid].getRunEventi();
            stepeventi = mappeviste[mapid].getStepEventi();
            steptile = mappeviste[mapid].getStepTile();
            mapx = map.length;
            mapy = map[0].length;
        }*/
        //else
        //{
            try
            {
                readermaps = new FileReader(new File(home + fs + "data" + fs + "maps" + fs + "MAP_" + mapid + ".txt"));
                readerevents = new FileReader(new File(home + fs + "data" + fs + "mapevents" + fs + "EVENTS_" + mapid + ".txt"));

            }
            catch(FileNotFoundException fnfe)
            {
            }

            propmaps = new Properties();
            propevents = new Properties();

            try
            {
                propmaps.load(readermaps);
                proptiles.load(readertiles);
                propevents.load(readerevents);
                propcolors.load(readercolors);
            }
            catch(IOException ioe)
            {
            }

            String mappa = propmaps.getProperty("map");
            colorimappa = propmaps.getProperty("color");
            eventi = propevents.getProperty("touchevents");
            runeventi = propevents.getProperty("runningevents");
            stepeventi = propevents.getProperty("stepevents");
            steptile = propmaps.getProperty("steptile").charAt(0);

            //Controlla quale riga della mappa è la più lunga, così da usarla come riferimento
            //per la lunghezza della Mappa
            for(i = 0; i < mappa.split(" space").length; i++)
            {
                if(mappa.split(" space")[i].length() > mappa.split(" space")[j].length())
                {
                    j = i;
                }
            }

            mapx = mappa.split(" space")[j].length();
            mapy = mappa.split(" space").length;
            map = new char[mapx][mapy];

            //Salva "in map[][]" la mappa in cui si trova il giocatore
            for(j = 0; j < mapy; j++)
            {
                String mapLineX = mappa.split(" space")[j];

                for(i = 0; i < mapLineX.length(); i++)
                {
                    map[i][j] = mapLineX.charAt(i);
                }
            }
        //}
        steptile = map[px][py];
        mapMovement(mapid);
    }

    //Questa funzione quando richiamata stampa su schermo la Mappa in cui ci si trova
    public void drawMap(char[][] map, int mapx, int mapy)
    {
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 

        for(int j = 0; j < mapy; j++)
        {
            for(int i = 0; i < mapx; i++)
            {   
                for(int k = 0; k < colorimappa.split(" ;").length; k++)
                {
                    String coloretile = colorimappa.split(" ;")[k];
                    if((coloretile.split(" : ")[0]).charAt(0) == map[i][j])
                    {
                        int nColor = Integer.parseInt(coloretile.split(" : ")[1]);
                        String color = colors.split(" ;")[nColor].split(" ")[1];
                        String mapTile = color.substring(1) + map[i][j] + reset;

                        System.out.print(mapTile);
                    }
                }
            }
            System.out.println("");
        }

        System.out.println("\n  Facing: " + direction);
        System.out.print("\n    > ");
    }

    //Quando IF è richiamato dai File degli Eventi, questa funzione viene chiamata a sua volta
    //IF viene usato per controllare se le variabili globali (var[]) or le variabili locali(mapvar[][]) hanno un certo valore
    //o per controllare se il giocatore abbia una certa quantità di uno Strumento nella sua Borsa
    public void ifCondition(String evento)
    {
        //Se prima del numero della variabile c'è una 'L', vuol dire che si deve controllare
        //una variabile locale

        ifCheck = false;

        int varIndex = 0;   //L'indice della variabile
        String varOperation = "";    //L'operazione da effettuare sulla variabile
        int varValue = 0;   //Il valore con cui paragonare la variabile

        try
        {
            varIndex = Integer.parseInt(evento.split("\\(")[1].split(" ")[1]);
            varOperation = evento.split("\\(")[1].split("\\)")[0].split(" ")[2];
            varValue = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0].split(" ")[3]);
        }
        catch(NumberFormatException nFE)
        {
        }

        int varCondition = 0;   //Questa variabile prenderà il valore di "var" o "mapvar" per fare il Check

        String typeIf = evento.split("\\(")[1].split(" ")[0];
        if(typeIf.equals("SVAR"))
        {
            String s = wordvar[varIndex];
            String v = evento.split("\\(")[1].split("\\)")[0].split(" ")[3];
            if(s.equals(v))
            {
                ifCheck = true;
            }
        }
        else
        {
            switch(typeIf)
            {
                case "LVAR":
                    varCondition = mapvar[mapid][varIndex];
                    break;

                case "VAR":
                    varCondition = var[varIndex];
                    break;

                case "ITEM":
                    varCondition = bag.checkItem(varIndex);
                    break;

                case "OWABILITY":
                    varOperation = "";
                    String owaction = evento.split("\\(")[1].split("\\)")[0].split(" ")[1];
                    for(int i = 0; i < ismon.length; i++)
                    {
                        if(ismon[i] != null && ismon[i].getOverworldAction().equals(owaction))
                        {
                            ifCheck = true;
                            break;
                        }
                    }
                    break;

                case "TILE":
                    varOperation = "";
                    String e = evento.split("\\(")[1].split("\\)")[0];
                    char tile = e.split(" ")[2].charAt(0);
                    String x = e.split(" ")[1].split("-")[0];
                    String y = e.split(" ")[1].split("-")[1];

                    int tx = 0, ty = 0;

                    if(x.split("\\[")[0].equals("$v"))
                    {
                        String xc = x.split("\\[")[1].split("\\]")[0];
                        tx = ((xc.charAt(0) == 'M' || xc.charAt(0) == 'm') ? mapvar[mapid][Integer.parseInt(xc.substring(1))] : var[Integer.parseInt(xc)]);
                    }
                    else
                    {
                        tx = Integer.parseInt(x);
                    }
                    if(y.split("\\[")[0].equals("$v"))
                    {
                        String yc = y.split("\\[")[1].split("\\]")[0];
                        ty = ((yc.charAt(0) == 'M' || yc.charAt(0) == 'm') ? mapvar[mapid][Integer.parseInt(yc.substring(1))] : var[Integer.parseInt(yc)]);
                    }
                    else
                    {
                        ty = Integer.parseInt(y);
                    }

                    if(map[tx][ty] == tile)
                    {
                        ifCheck = true;
                    }
                    break;
            }

            //Qui si controlla se è richiesto che la variabile sia Maggiore, Minore o Uguale ad uno specifico numero
            switch(varOperation)
            {
                case ">":
                    if(varCondition > varValue)
                    {
                        ifCheck = true;
                    }
                    break;

                case ">=":
                    if(varCondition >= varValue)
                    {
                        ifCheck = true;
                    }
                    break;

                case "<":
                    if(varCondition < varValue)
                    {
                        ifCheck = true;
                    }
                    break;

                case "<=":
                    if(varCondition <= varValue)
                    {
                        ifCheck = true;
                    }
                    break;

                case "==":
                    if(varCondition == varValue)
                    {
                        ifCheck = true;
                    }
                    break;

                case "!=":
                    if(varCondition != varValue)
                    {
                        ifCheck = true;
                    }
                    break;
            }
        }

        if(!ifCheck)
        {
            ifSkip = Integer.parseInt(evento.split("\\) ")[1]); //Tiene conto dell'ID dell' IF, nel caso sia necessario saltare il suo contenuto
        }
    }

    //Questa funzione è richiamata quando è necessario cambiare il valore di una variabile globale o locale
    public void changeVar(String evento)
    {   
        //Tiene il tipo di variabile da modificare
        String varType = evento.split("\\(")[0];
        //Tiene l'indice della variabile
        int varindex = Integer.parseInt(evento.split("\\(")[1].split(" ")[0]);
        //Tiene il valore per cui la variabile deve essere modificata
        int varChange = 0;
        //Tiene l'operazione che deve essere eseguita sulla variabile
        String modifier = evento.split("\\(")[1].split(" ")[1];

        String value = evento.split("\\(")[1].split("\\)")[0].split(" ")[2];

        if(varType.equals("SVAR"))
        {
            if(value.split("_")[0].equals("MON"))
            {   
                String monId = value.split("_")[1];
                if(monId.split("\\[")[0].equals("$v"))
                {
                    String varID = value.split("\\[")[1].split("\\]")[0];
                    varChange = (varID.charAt(0) == 'm' || varID.charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID.substring(1))] : var[Integer.parseInt(varID)];
                }
                else
                {
                    varChange = Integer.parseInt(monId.split("\\{")[0]);
                }
                switch(value.split("\\{")[1].split("\\}")[0])
                {
                    case "NAME":
                        wordvar[varindex] = ((ismon[varChange] != null) ? ismon[varChange].getName() : "");
                        break;

                    case "NICKNAME":
                        wordvar[varindex] = ((ismon[varChange] != null) ? ismon[varChange].getNickname() : "");
                        break;

                    case "OWABILITY":
                        wordvar[varindex] = ((ismon[varChange] != null) ? ismon[varChange].getOverworldAction() : "");
                        break;

                    default:
                        wordvar[varindex] = value;
                        break;
                }
            }
            else
            {
                wordvar[varindex] = value;
            }
        }
        else
        {   
            if(value.split("_")[0].equals("MON"))
            {   
                String monId = value.split("_")[1];
                if(monId.split("\\[")[0].equals("$v"))
                {
                    String varID = value.split("\\[")[1].split("\\]")[0];
                    varChange = (varID.charAt(0) == 'm' || varID.charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID.substring(1))] : var[Integer.parseInt(varID)];
                }
                else
                {
                    varChange = Integer.parseInt(monId);
                }
                switch(value.split("\\{")[1].split("\\}")[0])
                {
                    case "INDEX":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getIndex() : 0);
                        break;

                    case "LEVEL":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getLevel() : 0);
                        break;

                    case "HP":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[0] : 0);
                        break;
                        
                    case "cHP":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getCurrentHP() : 0);
                        break;

                    case "ATK":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[1] : 0);
                        break;

                    case "DEF":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[2] : 0);
                        break;

                    case "SPA":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[3] : 0);
                        break;

                    case "SPD":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[4] : 0);
                        break;

                    case "SPE":
                        varChange = ((ismon[varChange] != null) ? ismon[varChange].getStats()[5] : 0);
                        break;

                    case "FULL":
                        varChange = ((ismon[varChange] != null) ? 1 : 0);
                        break;
                }
            }
            else if(value.split("\\[")[0].equals("RANDOM"))
            {
                Random rand = new Random();
                String random = value.split("\\[")[1].split("]")[0];
                int max = Integer.parseInt(random.split("-")[1]);
                int min = Integer.parseInt(random.split("-")[0]);

                varChange = rand.nextInt((max - min) + 1) + min;
            }
            else if(value.split("\\[")[0].equals("$v"))
            {
                String varID = value.split("\\[")[1].split("]")[0]; 
                varChange = (varID.charAt(0) == 'm' || varID.charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID.substring(1))] : var[Integer.parseInt(varID)];
            }
            else if(value.equals("px") || value.equals("playerX"))
            {
                varChange = px;
            }
            else if(value.equals("py") || value.equals("playerY"))
            {
                varChange = py;
            } 
            else
            {
                varChange = Integer.parseInt(value);
            }
            //Costante per cui il 'varChange' viene moltiplicato
            int uguale = 1;

            //Controlla il tipo di operazione da eseguire sulla variabile
            switch(modifier)
            {
                //Somma, il valore non viene toccato
                case "+=":
                    break;

                //Differenza, il valore viene reso negativo
                case "-=":
                    varChange *= -1;
                    break;

                //Uguaglianza, 'uguale' è settato a 0
                case "=":
                    uguale = 0;
                    break;
            }

            //Controlla quale tipo di Variabile deve essere modificata
            switch(varType)
            {
                case "VAR":
                    var[varindex] *= uguale;
                    var[varindex] += varChange;
                    break;

                case "LVAR":
                    mapvar[mapid][varindex] *= uguale;
                    mapvar[mapid][varindex] += varChange;                                                       
                    break;
            }
        }
    }

    //Shows text when prompted. It can also show the value of a Global, String or Map Variable
    public void showText(String text)
    {
        String[] varID = new String[text.split("\\$v").length-1];

        text += " ";
        String subtext = "";

        String currentID = ""; //This contains var by which the text is split in the current loop
        String previousID = ""; //This contains the var by which the text was split in the previous loop
        String nextID = ""; //This contains the var by which the text will be split in the next loop

        System.out.println("\n");

        //If there's no variable inside the text, we show it normally
        if(varID.length > 0)
        {   
            //We take the IDs of the variables inside the text
            for(int i = 0; i < varID.length; i++)
            {
                varID[i] = text.split("\\$v")[i+1].split("\\[")[1].split("]")[0];
            }

            for(int i = 0; i < varID.length; i++)
            {
                currentID = "\\$v\\[" + varID[i] + "]";
                try
                {
                    nextID = "\\$v\\[" + varID[i+1] + "]";
                }
                catch(Exception e)
                {
                }
                
                //If there's only one variable, we skip the loop
                if(varID.length == 1)
                {
                    try
                    {
                        System.out.print(text.split(currentID)[0] + ((varID[i].charAt(0) == 'm' || varID[i].charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID[i].substring(1))] : ((varID[i].charAt(0) == 's' || varID[i].charAt(0) == 'S') ? wordvar[Integer.parseInt(varID[i].substring(1))] : var[Integer.parseInt(varID[i])])) + text.split(currentID)[1]);
                    }
                    catch(Exception e)
                    {
                        System.out.print(text);
                    }
                    break;
                }

                if(i == varID.length-1)
                {
                    try
                    {
                        System.out.print(text.split(previousID)[1].split(currentID)[0] + ((varID[i].charAt(0) == 'm' || varID[i].charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID[i].substring(1))] : ((varID[i].charAt(0) == 's' || varID[i].charAt(0) == 'S') ? wordvar[Integer.parseInt(varID[i].substring(1))] : var[Integer.parseInt(varID[i])]))  + text.split(currentID)[1]);
                    }
                    catch(Exception e)
                    {
                        subtext = text.split(previousID)[1];
                        subtext = (subtext.charAt(subtext.length()-1) == ' ') ? subtext.substring(0, subtext.length()-1) : subtext;
                        System.out.print(subtext);
                    }
                }
                else
                {
                    if(i == 0)
                    { 
                        subtext = text;
                    }
                    else
                    {
                        subtext = text.split(previousID)[1];
                    }

                    subtext = subtext.split(currentID)[0];

                    try
                    {
                        System.out.print(subtext + ((varID[i].charAt(0) == 'm' || varID[i].charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(varID[i].substring(1))] : ((varID[i].charAt(0) == 's' || varID[i].charAt(0) == 'S') ? wordvar[Integer.parseInt(varID[i].substring(1))] : var[Integer.parseInt(varID[i])])) );
                    }
                    catch(Exception e)
                    {   
                        if(i < varID.length-1)
                        {
                            subtext = (i == 0) ? text.split(nextID)[0] : text.split(previousID)[1].split(nextID)[0];
                            subtext = (subtext.charAt(subtext.length()-1) == ' ') ? subtext.substring(0, subtext.length()-1) : subtext;
                            System.out.print(subtext);
                        }
                        else
                        {
                            System.out.print(text.split(previousID)[1]);
                        }
                    }
                }

                previousID = currentID;
            }
        }
        else
        {
            System.out.print(text);
        }

        System.out.print("\n");
    }

    public void playerChoice(String evChoice)
    {
        boolean ripeti = true;
        do
        {
            String[] evChoices = new String[(evChoice.split("_")).length];

            System.out.print("            ");
            for(int i = 0; i < evChoices.length; i++)
            {
                evChoices[i] = evChoice.split("_")[i].split("\\[")[0];
                System.out.print(evChoices[i]);

                if(i != evChoices.length-1)
                {
                    if(i > 0 && i % 2 == 0)
                    {
                        System.out.print("\n\n            ");
                    }
                    else
                    {
                        System.out.print("     ");
                    }
                }
            }

            System.out.print("\n\n                > ");
            char scelta = scegli.next().charAt(0);

            String decisione = Character.toString(scelta);

            try
            {
                Integer.parseInt(decisione);

                if(Integer.parseInt(decisione)-1 > evChoices.length || Integer.parseInt(decisione)-1 < 0)
                {
                    drawMap(map, mapx, mapy);
                    showText(salvaTesto);
                }
                else
                {
                    String sceltaFatta = evChoice.split("_")[Integer.parseInt(decisione)-1].split("\\[")[1].split("\\]")[0];
                    listEvents(sceltaFatta);
                    ripeti = false;
                }
            }
            catch(Exception e)
            {
                drawMap(map, mapx, mapy);
                showText(salvaTesto);
            }
        }
        while(ripeti);
    }

    //Questa funzione contiene le possibili azioni effettuabili da running, step and touch events
    public void listEvents(String evento)
    {
        //Se le condizione di un IF non sono rispettate, gli eventi al suo interno vengono saltati
        if(!ifCheck)
        {
            if(evento.equals("ENDIF " + ifSkip) || evento.equals("ELSE(" + ifSkip + ")") || evento.equals("ENDELSE " + elseSkip))
            {
                if(!evento.equals("ELSE(" + ifSkip + ")"))
                {
                    ifSkip = -1;
                }
                ifCheck = true;
            }
        }

        if(ifCheck)
        {
            String eventtype = evento.split("\\(")[0];
            switch(eventtype)
            {
                //Adds an Item to the Player's Bag
                case "ADDITEM":
                    int item = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0].split(", ")[0]);
                    int amount = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0].split(", ")[1]);
                    bag.addItem(item, amount);
                    break;

                //Runs a Common Event, an event can be ran in every map
                case "COMMONEVENT":
                    int commonEventID = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0]);
                    String commonEvent = propcommevents.getProperty("commonevents").split("/")[commonEventID];

                    for(int i = 0; i < (commonEvent.split(" : ")).length; i++)
                    {
                        listEvents(commonEvent.split(" : ")[i]);

                        if(loopCheck)
                        {
                            for(i = i; !commonEvent.split(" : ")[i].equals("LOOP() " + loopID); i--)
                            {
                                //If the conditions inside WHEN are true, the contents inside this LOOP are repeated
                            }
                        }
                        loopCheck = false;
                    }
                    break;

                //Adds a Mon to the player's team. If the team is already full, it is sent to the Box
                case "GIFTMON":
                    int g = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0]);
                    String gift = mongifts.split(";")[g];
                    int j;
                    for(j = 0; j < ismon.length; j++)
                    {
                        if(ismon[j] == null)
                        {
                            break;
                        }
                    }
                    
                    int index = Integer.parseInt(gift.split(", ")[0]);
                    int gender = Integer.parseInt(gift.split(", ")[1]);
                    String nickname = gift.split(", ")[2];
                    int level = Integer.parseInt(gift.split(", ")[3]);
                    int ability = Integer.parseInt(gift.split(", ")[4]);

                    int[] tp = new int[gift.split(", ")[5].split(" ").length];
                    for(int i = 0; i < tp.length; i++)
                    {
                        tp[i] = Integer.parseInt(gift.split(", ")[5].split(" ")[i]);
                    }

                    int nature = Integer.parseInt(gift.split(", ")[6]);
                    int characteristic = Integer.parseInt(gift.split(", ")[7]);

                    int[] mosse = new int[gift.split(", ")[8].split(" ").length];
                    for(int i = 0; i < mosse.length; i++)
                    {
                        mosse[i] = Integer.parseInt(gift.split(", ")[8].split(" ")[i]);
                    }

                    if(j < ismon.length)
                    {
                        ismon[j] = new Creature(index, gender, nickname, level, ability, tp, nature, characteristic, mosse);
                        break;
                    }
                    else
                    {
                        for(int i = 0; i < box.length; i++)
                        {
                            for(j = 0; j < box[i].length; j++)
                            {
                                if(box[i][j] == null)
                                {
                                    box[i][j] = new Creature(index, gender, nickname, level, ability, tp, nature, characteristic, mosse);
                                    break;
                                }
                            }
                        }
                    }
                    break;

                //Makes a series of events run if the conditions inside it are met
                case "IF":
                    ifCondition(evento);
                    break;

                //Makes a series of events run if the conditions inside an IF with the same ID are not met
                case "ELSE":
                    int elseID = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0]);
                    if(elseID != ifSkip)
                    {
                        ifCheck = false;
                        elseSkip = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0]);
                    }
                    break;

                //Contains the conditions for a loop to repeat itself
                case "WHEN":
                    ifCondition(evento);
                    if(ifCheck)
                    {
                        loopCheck = true;
                        loopID = Integer.parseInt(evento.split("\\) ")[1]);
                    }
                    else
                    {
                        ifCheck = true;
                    }
                    break;

                //Displays text on the Screen
                case "TEXT":
                    String text = evento.split("\\(")[1].split("\\)")[0];
                    showText(text);
                    /*
                    System.out.print("\n  " + evento.split("\\(")[1].split("\\)")[0]);*/
                    salvaTesto = "\n" + text + "\n";
                    continua.nextLine();
                    break;

                //Changes a tile of the map
                case "CHANGE":
                    int changex = 0;
                    int changey = 0;

                    String x = evento.split("\\(")[1].split("-")[0];
                    if(x.split("\\[")[0].equals("$v"))
                    {
                        int value = 0;
                        String v = x.split("\\[")[1].split("\\]")[0];
                        value = ((v.charAt(0) == 'm' || v.charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(v.substring(1))] : var[Integer.parseInt(v)]);
                        changex = value;
                    }
                    else
                    {
                        changex = Integer.parseInt(x);
                    }

                    String y = evento.split("\\(")[1].split("-")[1].split(" ")[0];
                    if(y.split("\\[")[0].equals("$v"))
                    {
                        int value = 0;
                        String v = y.split("\\[")[1].split("\\]")[0];
                        value = ((v.charAt(0) == 'm' || v.charAt(0) == 'M') ? mapvar[mapid][Integer.parseInt(v.substring(1))] : var[Integer.parseInt(v)]);
                        changey = value;
                    }
                    else
                    {
                        changey = Integer.parseInt(y);
                    }
                    char mapchange = evento.split("\\(")[1].split(" ")[1].charAt(0);
                    map[changex][changey] = (changex == px && changey == py) ? map[changex][changey] : mapchange;
                    break;

                //Alters the value of a Variable
                case "VAR": case "LVAR": case "SVAR":
                    changeVar(evento);
                    break;

                //Refreshes the game as a whole
                case "REFRESH":
                    runningEvents();
                    drawMap(map, mapx, mapy);
                    break;

                //Refreshes only the Map
                case "REFRESHMAP":
                    drawMap(map, mapx, mapy);
                    break;

                //Repeats the Runevents for this map
                case "REFRESHRUNEVENTS":
                    runningEvents();
                    break;

                //Pauses the game
                case "WAIT":
                    int wait = Integer.parseInt(evento.split("\\(")[1].split("\\)")[0]);
                    try
                    {
                        Thread.sleep(wait);
                    }
                    catch(Exception ie)
                    {
                    }
                    break;

                //Makes the player make a choice. One event can be ran depending on their choice.
                case "CHOICE":
                    playerChoice(evento.split("CHOICE\\(")[1]);
                    break;

                //Changes the Map the player is in
                case "MAP":
                    int mapIDNew = Integer.parseInt(evento.split("\\)")[0].split("\\(")[1].split(" ")[0]);
                    map[px][py] = steptile;
                    px = Integer.parseInt(evento.split("\\)")[0].split("\\(")[1].split(" ")[1].split("-")[0]);
                    py = Integer.parseInt(evento.split("\\)")[0].split("\\(")[1].split(" ")[1].split("-")[1]);
                    //mappeviste[mapid] = new VMaps(map, colorimappa, eventi, runeventi, stepeventi, '.');
                    changeMap(mapIDNew);
                    break;

                //Starts the battle with a Trainer
                case "T-BATTLE":
                    Creature[] ismonE = new Creature[6];
                    int teamindex = Integer.parseInt(evento.split("\\)")[0].split("\\(")[1]);
                    String eTeam = team.getTeam(teamindex);
                    for(int i = 0; i < eTeam.split(" ; ").length; i++)
                    {
                        ismonE[i] = new Creature(eTeam.split(" ; ")[i]);
                    }
                    Fight fight = new Fight(ismon, ismonE);
                    break;

                default:
                    break;
            }
        }
    }

    //Questa funzione controlla i "running events", ovvero quegli eventi che sono ripetuti all'infinito
    public void runningEvents()
    {
        if(runeventi.length() == 1)
        {
        }
        else
        {
            for(int i = 0; i < runeventi.split(" : ").length; i++)
            {
                String runevento = runeventi.split(" : ")[i];
                listEvents(runevento);

                if(loopCheck)
                {
                    for(i = i; !runeventi.split(" : ")[i].equals("LOOP() " + loopID); i--)
                    {
                        //If the conditions inside WHEN are true, the contents inside this LOOP are repeated, so we reduce the 'i'
                        //Counter until we find a 'LOOP' command with the same ID as the 'WHEN' command that set 'loopCheck' to True.
                    }
                }
                loopCheck = false;
            }
        }
    }

    //Questa funzione controlla gli "step events", eventi che si attivano quando il giocatore si trova ad una
    //specifica coordinata
    public void stepEvents()
    {
        int i;
        int j;

        if(stepeventi.length() == 1)
        {
        }
        else
        {
            String stepeventoAction;

            for(i = 0; i < stepeventi.split(" /").length; i++)
            {
                String stepeventoPos = stepeventi.split(" /")[i].split(" : ")[0];

                if(px == Integer.parseInt(stepeventoPos.split("-")[0]) && py == Integer.parseInt(stepeventoPos.split("-")[1]))
                {
                    for(j = 1; j < stepeventi.split(" /")[i].split(" : ").length; j++)
                    {
                        stepeventoAction = stepeventi.split(" /")[i].split(" : ")[j];

                        listEvents(stepeventoAction);

                        if(loopCheck)
                        {
                            for(j = j; !stepeventi.split(" /")[i].split(" : ")[j].equals("LOOP() " + loopID); j--)
                            {
                                //If the conditions inside WHEN are true, the contents inside this LOOP are repeated
                            }
                        }
                        loopCheck = false;
                    }
                    break;
                }
            }
        }
    }

    //Questa funzione controlla i "touch events", eventi che si attivano quando il giocatore interagisce con uno specifica
    //coordinata a cui esso è adiacente 
    public void touchEvents(int mapx, int mapy)
    {
        int i;
        int j;

        String evento = null;

        for(i = 0; i < eventi.split("/").length; i++)
        {
            evento = eventi.split("/")[i];

            int eX = Integer.parseInt(evento.split(" : ")[0].split("-")[0]);
            int eY = Integer.parseInt(evento.split(" : ")[0].split("-")[1]);

            int eveX = 0, eveY = 0;
            switch(direction)
            {
                case '^':
                    eveY-=1;
                    break;

                case '<':
                    eveX-=1;
                    break;

                case '>':
                    eveX+=1;
                    break;

                case 'v':
                    eveY+=1;
                    break;
            }

            if(px+eveX == eX && py+eveY == eY/*(Math.abs(eX-px) == 1 && eY == py) || (Math.abs(eY-py) == 1 && eX == px)*/)
            {
                for(int k = 1; k < evento.split(" : ").length; k++)
                {
                    listEvents(evento.split(" : ")[k]);

                    if(loopCheck)
                    {
                        for(k = k; !evento.split(" : ")[k].equals("LOOP() " + loopID); k--)
                        {
                                //If the conditions inside WHEN are true, the contents inside this LOOP are repeated
                        }
                    }
                    loopCheck = false;
                }
                break;
            }
        }
    }

    public void mapMovement(int mapid)
    {
        boolean game = true;

    	char movimento = 'f';

        map[px][py] = playericon;
        ifCheck = true;

        runningEvents();
        drawMap(map, mapx, mapy);

        do
        {
            int xPlus = 0; int yPlus = 0;
            movimento = muovi.next().charAt(0);

            switch(movimento)
            {
                case 'w': case 'W':
                    yPlus = -1;
                    direction = '^';
                    break;

                case 'a': case 'A':
                    xPlus = -1;
                    direction = '<';
                    break;

                case 's': case 'S':
                    yPlus = 1;
                    direction = 'v';
                    break;

                case 'd': case 'D':
                    xPlus = 1;
                    direction = '>';
                    break;

                case 'f': case 'F':
                    ifCheck = true;
                    touchEvents(mapx, mapy);
                    break;

                case 't': case 'T':
                    Menu goMenu = new Menu(ismon, bag);
                    break;

                case 'e': case 'E':
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    System.exit(0);
                    break;

                default:
                    break;
            }

            tilesEffect(xPlus, yPlus);

            //Runs the runEvents
            ifCheck = true;
            runningEvents();

            //Draws the map
            drawMap(map, mapx, mapy);

            //Runs possible stepEvents
            ifCheck = true;
            stepEvents();
        }
        while(true);
	}

    //Questa funziona è chiamata quando il Giocatore si sposta nella mappa
    //Controlla le proprietà del Tile su cui il giocatore sta per camminare
    public void tilesEffect(int xPlus, int yPlus)
    {
        int i;
        int j;

        String tileffect = "WALK";

        boolean repeat = false;

        do
        {
            for(i = 0; i < tiles.split(" ;").length; i++)
            {
                if(tiles.split(" ;")[i].charAt(0) == map[px+xPlus][py+yPlus])
                {
                    tileffect = tiles.split(" ;")[i].split(" ")[1];
                    break;
                }
            }

            switch(tileffect)
            {
                case "CUT":
                    swim = false;
                    for(i = 0; i < ismon.length; i++)
                    {
                        if(ismon[i] != null && ismon[i].getOverworldAction().equals("CUT"))
                        {
                            map[px+xPlus][py+yPlus] = '.';
                            drawMap(map, mapx, mapy);
                            System.out.print(ismon[i].getNickname() + " cut the obstacle!");
                            continua.nextLine();
                            break;
                        }
                    }
                    break;

                case "STOP":
                    repeat = false;
                    break;

                default: case "WALK": case "THROUGH":
                    swim = false;
                    map[px][py] = steptile;
                    steptile = map[px+xPlus][py+yPlus];
                    px += xPlus; py += yPlus;
                    repeat = false;
                    break;

                case "SLIP":
                    swim = false;
                    map[px][py] = steptile;
                    steptile = map[px+xPlus][py+yPlus];
                    px += xPlus; py += yPlus;
                    repeat = true;
                    map[px][py] = playericon;
                    drawMap(map, mapx, mapy);
                    try
                    {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException ie)
                    {
                    }
                    break;

                case "SWIM":
                    for(i = 0; i < ismon.length; i++)
                    {
                        if(ismon[i] != null && ismon[i].getOverworldAction().equals("SWIM"))
                        {
                            map[px][py] = steptile;
                            steptile = map[px+xPlus][py+yPlus];
                            px += xPlus; py += yPlus;
                            if(!swim)
                            {
                                map[px][py] = playericon;
                                drawMap(map, mapx, mapy);
                                System.out.print(ismon[i].getNickname() + " lends his back to you!");
                                continua.nextLine();
                                swim = true;
                            }
                            repeat = false;
                            break;
                        }
                    }
            }

            if(!tileffect.equals("THROUGH"))
            {
                map[px][py] = playericon;
            }
        }
        while(repeat);
    }
}