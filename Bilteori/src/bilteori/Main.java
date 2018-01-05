package bilteori;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
public class Main extends Applet implements ActionListener, Runnable
{
    Image[] bilbg;
    boolean[] kontroll;
    boolean slump, test, ovning, färdig, nyrad;
    int[] svar, facit, frågprov, positionsave;
    String[] frågor, val, nyradString;
    int i = 0, c = 0, antfrågor, rätt, timer = 0, timershow, b = 0;
    Button next, back, prov, övning, omprov;
    String resultat;
    char jämförstring;
    Thread running;
    Image offscreen;
    Graphics bufferGraphics;
    CheckboxGroup svarGroup;
    Checkbox svar1,svar2,svar3,svar4;
    URL base;

    public void init()
    {
        setLayout(null);
        setSize(650,700);
        offscreen = createImage(650,700);
        bufferGraphics = offscreen.getGraphics();
        svarGroup = new CheckboxGroup();
        base = getDocumentBase();
        positionsave = new int[5];
        nyradString = new String[6];
        StringInit();
        ButtonInit();
        PicInit();
        variabler();
        FacitInit();

        running = new Thread(this);
        running.start();

    }
    public void paint(Graphics g)
    {
        bufferGraphics.clearRect(0,0,650,700);
        bufferGraphics.fillRect(0,0,650,5);
        bufferGraphics.fillRect(0,0,5,700);
        bufferGraphics.fillRect(0,695,650,5);
        bufferGraphics.fillRect(645,0,5,700);
        if(test || ovning)
        {
            if(nyrad)
                for(int a = 0; a <= b; a++)
                {
                    bufferGraphics.drawString(nyradString[a],50, 450 +(a*20));
                }
            else
                bufferGraphics.drawString(frågor[i], 50, 460);
            if(ovning)
                bufferGraphics.drawString("" + (i+1), 30, 440);
            else if(test)
                bufferGraphics.drawString("" + (c+1), 30, 440);
            if(test)
                bufferGraphics.drawImage(bilbg[frågprov[c]], 0, 0, this);
            else
                bufferGraphics.drawImage(bilbg[i], 0, 0, this);
            if(test)
                bufferGraphics.drawString("Tid kvar: " + timershow + "min",500, 440);
        }
        if(färdig)
        {
            bufferGraphics.drawString(resultat, 50, 250);
        }


        g.drawImage(offscreen,0,0,this);
    }
    public void update(Graphics g)
    {
        paint(g);
    }
    public void run()
    {
        while(true)
        {
            while(test)
            {
                timer++;
                timershow = (3600 - timer)/60;
                if(timer == 3600)
                {
                    färdig = true;
                    CheckAnswer();
                    test = false;
                    remove(next);
                    remove(back);
                    add(omprov);
                    omprov.addActionListener(this);
                    repaint();
                }
                repaint();
                try
                {
                    running.sleep(1000);
                }catch(InterruptedException e){}
            }
        }
    }
    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource() == omprov)
        {
            c = 0;
            timer = 0;
            test = true;
            färdig = false;

            for(int n = 0; n < 65; n++)
                svar[n] = 0;

            SlumpProv();
            remove(omprov);
            add(next);
            add(back);
            i = frågprov[c];
            RadioInit();
        }
        if(evt.getSource() == prov)
        {
            test = true;
            SlumpProv();
            i = frågprov[c];
            RadioInit();
            antfrågor = 65;
            remove(prov);
            remove(övning);
            add(next);
            add(back);
            next.addActionListener(this);
            back.addActionListener(this);
            RadByte();
            repaint();
        }
        if(evt.getSource() == övning)
        {
            ovning = true;
            RadioInit();

            antfrågor = 500;
            remove(prov);
            remove(övning);
            add(next);
            add(back);
            next.addActionListener(this);
            back.addActionListener(this);
            RadByte();
            repaint();
        }
        if(evt.getSource() == next)
        {
            SaveAnswer();
            if(test)
            {

                c++;
                if(c >= antfrågor)
                {
                    remove(svar1);
                    remove(svar2);
                    remove(svar3);
                    remove(svar4);
                    färdig = true;
                    CheckAnswer();
                    test = false;
                    remove(next);
                    remove(back);
                    add(omprov);
                    omprov.addActionListener(this);
                    repaint();

                }
                else if(c < antfrågor)
                {
                  i = frågprov[c];
                  RadByte();
                  RadioNull();
                  RadioInit();
                }

            }
            else
            {
                i++;
                RadByte();
                if(i > antfrågor)
                {
                    färdig = true;
                    CheckAnswer();
                    ovning = false;
                    remove(next);
                    remove(back);
                    repaint();
                }
                RadioNull();
                RadioInit();
            }


            repaint();
        }
        if(evt.getSource() == back)
        {
            if(test)
            {
                if(c > 0)
                {
                    c--;
                    i = frågprov[c];
                    RadByte();
                }
            }
            else
                if(i > 0)
                    i--;
            RadioNull();
            RadioInit();
            RadByte();
            repaint();
        }
    }
    public void SaveAnswer()
    {
        if(test)
        {
            if(svar1.getState())
                svar[c] = 1;
            else if(svar2.getState())
                svar[c] = 2;
            else if(svar3.getState())
                svar[c] = 3;
            else if(svar4.getState())
                svar[c] = 4;
        }
        else if(ovning)
        {
            if(svar1.getState())
                svar[i] = 1;
            else if(svar2.getState())
                svar[i] = 2;
            else if(svar3.getState())
                svar[i] = 3;
            else if(svar4.getState())
                svar[i] = 4;
        }
        
        
    }
    public void CheckAnswer()
    {
        for(int n = 0; n < antfrågor; n++)
        {
            if(test)
            {
                if(svar[n] == facit[frågprov[n]])
                    rätt++;


            }
            else if(ovning)
            {
                if(svar[n] == facit[n])
                {
                    kontroll[n] = true;
                    rätt++;
                }
                else
                    kontroll[n] = false;
            }
        }
        if(test)
        {
           if(rätt >= 52)
                resultat = "Grattis! Du klarade provet med " + rätt +"/65";
           else
                resultat = "Du klarade tyvärr inte provet: " + rätt +"/65";
        }
        else if(ovning)
        {
            resultat = "Du fick: " + rätt + "/500";
        }
    }
    public void StringInit()
    {
      frågor = new String[500];
        val = new String[2000];
        frågor[0] = "Vilket påstående om körfält är riktig?";
        val[0] = "Vägrenen är ett körfält.";
        val[1] = "Ett körfält behöver inte anges med vägmarkering. ";
        val[2] = "Ett körfält måste alltid anges med markering.";
        val[3] = "Det finns inget som heter körfält.";

        frågor[1] = "Du har körkort med prövotid. Vad gäller?";
        val[4] = "Jag måste göra om både kunskapsprovet och\nkörprovet om körkortet återkallas under periodtiden.";
        val[5] = "Jag behöver enbart göra om körprovet om körkortet\nåterkallas under periodtiden";
        val[6] = "Körkortet återkallas i två månader om man\nbötfälls för felparkering.";
        val[7] = "Vad är prövotid?";

     frågor[2] = "Vad innebär begreppet lätt lastbil?";
        val[8] = "Lastbil som har totalvikt på högst 3.5 ton";
        val[9] = "Lastbil som har en maxlastvikt på högst 3.5 ton";
        val[10] = "Lastbil som har en bruttovit på högst 3.5 ton";
        val[11] = "Lastbil som har en tjänstevikt på högst 3.5 ton";


     frågor[3] = "Vilket fordon får du köra med behörighet B?";
        val[12] = "Personbil med tillkopplad tungt släpfordon";
        val[13] = "Buss godkänd för 10 passagerare";
        val[14] = "Motorcykel med sidovagn";
        val[15] = "Lastbil med totalvikt på 3.1 ton";

     frågor[4] = "Vilken avgas saknar lukt och smak och påverkar hjärt och kärlsystem?";
        val[16] = "Kolmonoxid";
        val[17] = "Koldioxid";
        val[18] = "Kolväten";
        val[19] = "Kväveoxid";

     frågor[5] = "När är risken störst att du drabbas av kolmonoxid förgiftning?";
        val[20] = "När jag kör med fönstren öppna i hög hastighet.";
        val[21] = "När jag kör motorn på tomgång i\nett garage med dålig ventilation.";
        val[22] = "När jag kör sakta i en trafikkö inom tätbebyggt område.";
        val[23] = "När jag kör med fönstren öppna i låg hastighet.";

     frågor[6] = "Du kör en bil och har lastat en cykel i bagageutrymmet./Hur ska du göra för att undvika att få avgaser in i bilen?";
        val[24] = "Sätt på luftkonditionering och stäng fönstrerna.";
        val[25] = "Stäng av fläkten och veva ner fönstrerna en bit.";
        val[26] = "Stäng av luftkonditionering och stäng fönstrerna.";
        val[27] = "Ingenting";

        frågor[7] = "Vilket ämne i avgaserna bidrar mest till att öka växthuseffekten?";
        val[28] = "Kolväten";
        val[29] = "Kolmonoxid";
        val[30] = "Koldioxid";
        val[31] = "Syre";

     frågor[8] = "Du kör en personbil med katalysator./Vilket ämne i avgaserna minskas inte av katalysatorn?";
        val[32] = "Kolmonoxid";
        val[33] = "Koldioxid";
        val[34] = "Kväveoxid";
        val[35] = "Kolväten";

     frågor[9] = "Vilket ämne i avgaserna bidrar till försurningen i våra skogar och sjöar?";
        val[36] = "Kolväten";
        val[37] = "Kväveoxider";
        val[38] = "Kolmonoxid";
        val[39] = "Koldioxid";

     frågor[10] = "Hur kan du på bästa sätt minska utsläppen av koldioxid?";
        val[40] = "Genom att köra en bil med katalysator";
        val[41] = "Genom att köra på lågoctanig bensin";
        val[42] = "Genom att varmköra motorn till en\ntillräckligt varm temperatur före färd.";
        val[43] = "Genom att köra med låga varvtal.";

     frågor[11] = "Hur kan du på bästa sätt minska bränsleförbrukningen?";
        val[44] = "Jag försöker hela tiden köra på höga motorvarv.";
        val[45] = "Jag släpper gasen i god tid för att undvika bromsning.";
        val[46] = "Jag använder bilens farthållare.";
        val[47] = "Jag växlar till en lägre växel i alla uppförsbackar.";

     frågor[12] = "På vilket sätt kan du påverka bränsleförbrukningen i din bil?";
        val[48] = "Bränsleförbrukningen blir lägre om \n jag använder motorvärmare på vintern.";
        val[49] = "Bränsleförbrukningen blir lägre om \n jag kör på låga växlar.";
        val[50] = "Bränsleförbrukningen blir högre om \n bilen får regelbunden service.";
        val[51] = "Bränsleförbrukningen blir högre om \n jag har högt lufttryck i däcken.";

       frågor[13] = "Kan du spara bensin genom ditt val av växel?";
        val[52] = "Ja, genom att köra med så hög? växel som möjligt.";
        val[53] = "Ja, genom att köra med så låg växel som möjligt.";
        val[54] = "Val av växeln har ingen betydelse för bensinåtgången.";
        val[55] = "Endast om bilen är automatare";

        frågor[14] = "Vilken utrustning är du skyldig att medföra i bilen under körning?";
        val[56] = "Varningstriangel.";
        val[57] = "Bogserlina.";
        val[58] = "Hjälpstartkablar.";
        val[59] = "Reservhjul.";

     frågor[15] = "Vilket alternativ beskriver bäst mogna personers egenskaper som förare?";
        val[60] = "De kan tillämpa gällande regler i varje trafiksituation.";
        val[61] = "De har förståelse för andra trafikanters misstag och svårigheter.";
        val[62] = "De kan förkorta bromssträckan när de kör i högre hastighet.";
        val[63] = "De kan snabbt ta sig ur riskfyllda situationer genom att köra impulsivt.";

     frågor[16] = "Vad måste du först vara beredd på i den här situationen?";
        val[64] = "Fordon som kommer ut från höger efter övergångsstället.";
        val[65] = "Gående som går ut framför bussen.";
        val[66] = "Gående som går ut på övergångsstället.";
        val[67] = "Fordon som kommer ut från vänster efter övergångstället.";

     frågor[17] = "Vad är utmärkande för en förare med moget beteende?";
        val[68] = "Föraren har en impulsiv körstil.";
        val[69] = "Föraren har en offensiv körstil.";
        val[70] = "Föraren reagerar starkt på medtrafikanternas felbeteende.";
        val[71] = "Föraren har god självkännedom.";

     frågor[18] = "Varför blir äldre förare mer sällan inblandade i olyckor än unga förare?";
        val[72] = "De har mer erfarenhet.";
        val[73] = "De har mer prestigetänkande.";
        val[74] = "De kör oftare på natten.";
        val[75] = "De kör mer offensivt.";

       frågor[19] = "Hur påverkas reaktionssträckan av förarens ålder och erfarenhet?";
        val[76] = "En 18-årig har vanligtvis kortare reaktionssträcka än en 45-årig erfaren förare,pga bättre raktionsförmåga.";
        val[77] = "En 45-årig erfaren förare har kortare reaktionssträcka än en 18-årig, pga förmågan att förutse faror.";
        val[78] = "Reaktionssträckan påverkas inte av förarens ålder eller erfarenhet utan beror bara på hastigheten.";
        val[79] = "Ingen skillnad alls";

         frågor[20] = "Vad menas med grupptrycket?";
        val[80] = "Då du bli stressad när du ser en polis bil.";
        val[81] = "Då du blir irriterad av andra trafikanter";
        val[82] = "Då kamraterna påverkar,man kör på ett sätt som man normalt inte gör.";
        val[83] = "Då dina kamrater pressar dig att gå med i en grupp.";

         frågor[21] = "Vilket av följande alternativ är den vanligaste anledningen till singelolyckor?";
        val[84] = "Halkig väg.";
        val[85] = "Kraftig regn eller snö.";
        val[86] = "Föraren anpassar inte körning efter trafikförhållanden.";
        val[87] = "Slitna däck.";



     frågor[22] = "Vad kännetecknar förare som kör impulsivt?";
        val[88] = "De kör alltid med låg hastighet.";
        val[89] = "De kan plötsligt svänga eller byta körfält.";
        val[90] = "De kör med för hög hastighet";
        val[91] = "De har alltid dålig uppmärksamhet bakåt.";

        frågor[23] = "Hur påverkas körförmågan i allmänheten av stress?";
        val[92] = "Lite stress kan förbättra körförmågan.";
        val[93] = "Stress påverkar inte körförmågan.";
        val[94] = "All sorts stress försämrar körförmågan.";
        val[95] = "Körförmågan blir som bäst med för mycket stress.";

     frågor[24] = "Hur påverkas din körförmåga i allmänhet av stress?";
        val[96] = "Stress påverkar inte körförmågan";
        val[97] = "Mycket stress kan försämra körförmågan";
        val[98] = "Stress försämrar alltid körförmågan";
        val[99] = "Stress gör mig sömnig";

     frågor[25] = "Vilken typ av olyckor drabbar äldre förare oftare än andra förare?";
        val[100] = "Omkörningsolyckor";
        val[101] = "Singelolyckor";
        val[102] = "Korsningsolyckor";
        val[103] = "Olyckor där föraren är alkoholpåverkad";

     frågor[26] = "Var går gränsen för rattfylleri, promillehalten mätt i blodet?";
        val[104] = "0,2 promille";
        val[105] = "0,5 promille";
        val[106] = "0,8 promille";
        val[107] = "1,0 promille";

     frågor[27] = "Vad är gränsen för grovt rattfylleri.";
        val[108] = "0,2 promille i blodet eller 0,1 mg/liter utandningsluften.";
        val[109] = "1,0 promille i blodet eller 0,5 mg/liter utandningsluften.";
        val[110] = "1,5 promille i blodet eller 0,75 mg/liter utandningsluften.";
        val[111] = "2,0 promille i blodet eller 1,0 mg/liter utandningsluften.";

     frågor[28] = "Hur påverkar synen om du är alkohol påverkad?";
        val[112] = "Synskärp minskar och synfältet blir bredare";
        val[113] = "Synskärp starkas och synfält blir bredare";
        val[114] = "Synskärp starkas och blir lättare att användas";
        val[115] = "Synskärp minskas och synfältet blir smalare";

       frågor[29] = "En frisk man konsumerar 40-procentig sprit. /Hur många cl alkohol förbränner han på en timme?";
        val[116] = "1 cl";
        val[117] = "2 cl";
        val[118] = "3 cl";
        val[119] = "4 cl";

     frågor[30] = "En frisk man (70 kg) konsumerar 25 cl 40-procentig sprit./ Hur lång tid dröjer det innan kroppen förbränner innehållet?.";
        val[120] = "8 timmar";
        val[121] = "10 timmar";
        val[122] = "14 timmar";
        val[123] = "mer än 14 timmar";

     frågor[31] = "Kan man göra någonting för att påskynda förbränningen av alkoholen i kroppen?";
        val[124] = "Ja, man kan sova längre.";
        val[125] = "Ja man kan bada bastu.";
        val[126] = "Ja man kan ta en lång promenad";
        val[127] = "Nej, man kan inte öka förbränningen";

     frågor[32] = "Hur visar föraren bästa omdömet dagen efter att han/ har druckit en större mängd alkohol?";
        val[128] = "Då föraren börjar med att motionerar och sedan badar i vatten som skyndar på alkoholförbränningen.";
        val[129] = "Då föraren håller noga koll på hur mycket han dricker och sedan lista ut vilken tidpunkt åter kan köra.";
        val[130] = "Då föraren låter bilen stå hela dagen för att vara säker på att alkoholen inte inverkar vid körning";
        val[131] = "";

     frågor[33] = "Vilken grupp har störst andel dödade personbilsförare med alkohol i kroppen?";
        val[132] = "Kvinnor i ålder 18-24 år";
        val[133] = "Män i ålder 18-24 år";
        val[134] = "Kvinnor i ålder 30-45 år";
        val[135] = "Män i ålder 30-45 år.";

       frågor[34] = "Hur kan du veta om ett läkemedel är olämpligt att använda i samband med körning?";
        val[136] = "Läser den bifogade informationen om medicinen";
        val[137] = "Läser innehållets ekvation på förpackningen";
        val[138] = "Kontrollerar om förpackningen är märkt med ett rött kryss.";
        val[139] = "Kontrollerar om förpackningen är märkt med en överkorsad bil.";

         frågor[35] = "På vilket sätt ändras rörelseenergin om hastigheten/ höjs från 30km i timmen till 90km i timmen?";
        val[140] = "Rörelseenergin blir tre gånger längre";
        val[141] = "Rörelseenergin blir sex gånger längre";
        val[142] = "Rörelseenergin blir nio gånger längre";
        val[143] = "Rörelseenergin blir tolv gånger längre";

         frågor[36] = "Vad påverkar reaktionssträckans längd?";
        val[144] = "Fordonets hastighet.";
        val[145] = "Fordonets tyngd";
        val[146] = "Fordonets bromsar.";
        val[147] = "Fordonets däck.";



     frågor[37] = "Vilket av följande påverkar reaktionssträckans längd?";
        val[148] = "Väglaget";
        val[149] = "Fordonets skick";
        val[150] = "Förarens tillstånd";
        val[151] = "Väderleken";

        frågor[38] = "Vad händer med reaktionssträckan om du fördubblas hastigheten?";
        val[152] = "Reaktionssträckan förändras inte";
        val[153] = "Reaktionssträckan fördubblas";
        val[154] = "Reaktionssträckan blir 3 gånger så lång";
        val[155] = "Reaktionssträckan blir 4 gånger så lång";

     frågor[39] = "Du förutser en fara och minskar farten från 100km i timmen/ till 50km i timmen. Hur påverkas bromssträckan?";
        val[156] = "Den minskas till en femtedel.";
        val[157] = "Den minskas till en fjärdedel.";
        val[158] = "Den minskas till hälften.";
        val[159] = "Den minskas till en tredjedel.";

     frågor[40] = "Vad händer med bromssträckan om du ökar farten/ från 35km i timmen till 105km i timmen ?";
        val[160] = "Bromssträckan blir tre gånger längre";
        val[161] = "Bromssträckan blir sex gånger längre";
        val[162] = "Bromssträckan blir nio gånger längre";
        val[163] = "Bromssträckan blir tolv gånger längre";

     frågor[41] = "Du kör i 30 km i timmen och det är torrt väglag./ Bromssträckan är då ca 6 meter. Hur lång blir bromssträckan/ om du ökar hastigheten till 90km i timmen";
        val[164] = "Ca 18 meter";
        val[165] = "Ca 24 meter";
        val[166] = "Ca 36 meter";
        val[167] = "Ca 54 meter";

     frågor[42] = "Vem ansvarar för att en 14 åring använder bilbälten under färd?";
        val[168] = "Bilens ägare har alltid ansvaret";
        val[169] = "14-åringens föräldrar om de finns med i bilen.";
        val[170] = "Föraren har alltid ansvaret.";
        val[171] = "14 åringen själv eftersom passagerare över 10 år bär själva ansvaret.";


       frågor[43] = "Din bil har krockkudde på passagerarplatsen fram. Hur ska du placera bilbarnstol?";
        val[172] = "Det är förbjudet att placera bilbarnstol i bilen";
        val[173] = "Krockkudden kopplas alltid ur automatiskt när jag har bilbarnstol fram.";
        val[174] = "Jag måste vända på bilbarnstolen, om krockkudden är inkopplad";
        val[175] = "Jag måste placera bilbarnstolen i baksätet om krockkudden är inkopplad.";

         frågor[44] = "Vilken skyddsutrustning bör ett två-årigt barn i första hand använda när de åker bil?";
        val[176] = "Bilbälte";
        val[177] = "Bältesstol";
        val[178] = "Bälteskudde och bilbälte";
        val[179] = "Bälteskydd";

         frågor[45] = "Hur vet du att en bilbarnstol är godkänd som en skyddsutrustning?";
        val[180] = "Om bilbarnstol har märke A eller D";
        val[181] = "Om bilbarnstol har märke T eller E";
        val[182] = "Alla bilbarnstolar är godkända";
        val[183] = "";

         frågor[46] = "Vilket är de viktigaste skälet till att ha en bilbarnstol monterad bakåtvänd?";
        val[184] = "Barnet får den bekvämaste sittställningen";
        val[185] = "Barnen får bättre kontakt med övriga passagerare";
        val[186] = "Barnets huvud och nacke får bäst skyddet";
        val[187] = "Barnet klarar sig bättre från att bli illamående";

         frågor[47] = "När är det dags att byta bilbarnstolen till en bälteskudde?";
        val[188] = "När barnet kan sitta utan stöd.";
        val[189] = "När barnet har fyllt två år.";
        val[190] = "När barnets huvud är i höjd med stolens överkant.";
        val[191] = "";


         frågor[48] = "När får varningsblinkers användas?";
        val[192] = "Vid av och påstigning strax efter ett backkrön.";
        val[193] = "Vid motorhaveri";
        val[194] = " Vid parkering framför utfart till fastighet.";
        val[195] = "Vid av och pålastning inom 10 meter från vägkorsningen.";

         frågor[49] = "Vad kan bli följden om du lånar ut din bil till en person som/ du vet inte har körkort men som ändå kör bilen?";
        val[196] = "Enbart den som lånat bilen kan bli straffad";
        val[197] = "Både jag själv och den som lånat bilen kan bli straffade.";
        val[198] = "Enbart jag som äger till bilen kan bli straffad.";
        val[199] = "";


       frågor[50] = "Hur uppträder förare som har ett moget trafikuppträdande?";
        val[200] = "De visar hänsyn mot andra trafikanter";
        val[201] = "De tänker i första hand på sig själv";
        val[202] = "De håller alltid på sin rätt enligt trafikreglerna";
        val[203] = "";

     frågor[51] = "Vilka krav ställs på en privat handledare för att du ska kunna övningsköra med honom?";
        val[204] = "Handledaren måste ha körkort sedan tre år tillbaka.";
        val[205] = "Eleven har ansvar för körningen enligt lagen.";
        val[206] = " Handledaren måste vara godkänd och haft körkort i minst 5 år.";
        val[207] = "";


         frågor[52] = "Vilket fordon är mest miljövänlig för naturen?";
        val[208] = "Bil";
        val[209] = "Tåg";
        val[210] = "Cykel";
        val[211] = "Flyg";

         frågor[53] = "På vilken av bilderna sitter föraren säkrast för att skydda/ sig mot nackskador vid påkörning bakifrån?";
        val[212] = "Bild A";
        val[213] = "Bild B";
        val[214] = "Bild C";
        val[215] = "Bild D";

         frågor[54] = "Det är viktigt för miljön om du använder motorbromsen istället för /att bromsa med fotbromsen vid avfarter och korsningar. Vilken är/den viktigaste anledning för det?";
        val[216] = "Däcken slits mindre dessutom minskar spridningen av partiklar.";
        val[217] = "Motorn får högre arbetstemperatur vid inbromsningen och det minskar avgasutsläppen.";
        val[218] = "Bensin förbrukningen minskar och därmed även avgasutsläppen.";
        val[219] = "Bromsbeläggen slits inte och därmed sprids inga partiklar från de till naturen.";

         frågor[55] = "Hur uppfattar unga förare i allmänhet sin körförmåga?";
        val[220] = "De flesta unga kvinnor tror att de kör bättre än genomsnittsbilisten";
        val[221] = "De flesta unga män tror att de kör bättre än genomsnittsbilisten";
        val[222] = "Alla unga förare tror att de kör bättre än genomsnittsbilisten";
        val[223] = "";

         frågor[56] = "Vilken påföljd är vanligast att en förare får vid grovt rattfylleri?";
        val[224] = "Enbart körkortsåterkallelse";
        val[225] = "Körkortsåterkallelse och fängelse";
        val[226] = "Enbart fängelse";
        val[227] = "";


         frågor[57] = "Kortare bilresor medför större utsläpp av avgaser per km än / längre bilresor. Hur stor andel av samtliga bilresor i Sverige/är kortare än 5 km?";
        val[228] = "Ca 15 procent";
        val[229] = "Ca 30 procent";
        val[230] = "Ca 45 procent";
        val[231] = "Ca 60 procent";

         frågor[58] = "Begreppet lätt lastbil är högst på 3,5 ton. Vilken vikt anses?";
        val[232] = "Totalvikt";
        val[233] = "Maxlast";
        val[234] = "Tjänstevikt";
        val[235] = "Bruttovikt";

        frågor[59] = "Hos de flesta förare har typen av passagerare /betydelse för körsättet. Vilken typ av passagerare har mest/ positiv effekt hos förare i de flesta åldersgrupper?";
        val[236] = "Jämnåriga kamrater";
        val[237] = "Barn";
        val[238] = "Föräldrar";
        val[239] = "Kamrater som är äldre";

        frågor[60] = "Du startar bilen en kall vintermorgon för att åka till jobbet./När börjar katalysatorn arbeta?";
        val[240] = "När katalysatorn är varm";
        val[241] = "När motorn är varm";
        val[242] = "När motorn startar";
        val[243] = "När jag kör under 3000v/min";

         frågor[61] = "Biltrafiken bidrar till att växthusgaserna ökar i atmosfären./Hur påverkas jorden av ökningen?";
        val[244] = "Medeltemperaturen höjs";
        val[245] = "Magnetfältet försvagas";
        val[246] = "Medeltemperaturen sänks";
        val[247] = "Magnetfältet stärks";

        frågor[62] = "Vilken av de här förarna löper störst risk att dödas i trafiken?";
        val[248] = "En 20 årig man";
        val[249] = "En 20 årig kvinna";
        val[250] = "En 35 årig kvinna";
        val[251] = "En 50 årig man";

        frågor[63] = "Nya bensindrivna bilar måste ha viss utrustning som/minskar miljöpåverkan. Vilken?";
        val[252] = "Katalysatorer";
        val[253] = "Partikelfilter";
        val[254] = "Bullerreducerande däck";
        val[255] = "Motorvärmare";

        frågor[64] = "Hur lång sträcka kör du på 1 sekund när du kör i 90km i timmen?";
        val[256] = "25 meter";
        val[257] = "12 meter";
        val[258] = "9 meter";
        val[259] = "35 meter";

        frågor[65] = "Kan en bilförare bli straffad för rattfylleri om han eller hon/har 0,1 promille alkohol i blodet?";
        val[260] = "Ja, eftersom gränsen för rattfylleri går vid 0 promille";
        val[261] = "Ja, om föraren bedöms olämplig som bilförare";
        val[262] = "Nej";
        val[263] = "Ja, men endast om föraren anses vållande till en trafikolycka";

         frågor[66] = "Vad kan vara orsaken till att en förare får förlängd reaktionstid?";
        val[264] = "Att det är halt väglag";
        val[265] = "Att bilen är i dåligt skick";
        val[266] = "Att föraren är sjuk eller trött";
        val[267] = "";

        frågor[67] = "Hur mycket av koldioxider från bilens avgaser tar en katalysator bort?";
        val[268] = "Ingenting";
        val[269] = "Alltihop";
        val[270] = "Hälften";
        val[271] = "25 %";

        frågor[68] = "Vad kallas den sträcka från att du upptäcker ett hinder tills bilen står stilla?";
        val[272] = "Stoppsträcka";
        val[273] = "Reaktionssträcka";
        val[274] = "Bromssträcka";
        val[275] = "";

         frågor[69] = "Du har en takbox på bilen. Påverkar det bränsleförbrukningen?";
        val[276] = "Ja, en takbox ökar alltid bränsleförbrukningen.";
        val[277] = "Ja, den ökar men enbart om boxen är lastad.";
        val[278] = "Nej, den påverkar inte";
        val[279] = "";

        frågor[70] = "Vilket alternativ anger rätt rangordning på trafik anvisningar?";
        val[280] = "Polismans tecken, trafikregler, trafiksignal, vägmärken.";
        val[281] = "Polismans tecken, trafiksignal, trafikregler, vägmärken.";
        val[282] = "Polismans tecken, vägmärken, trafiksignal, trafikregler";
        val[283] = "Polismans tecken, trafiksignal, vägmärken, trafikregler";

        frågor[71] = "Vad innebär anvisningarna?";
        val[284] = "Jag får köra eftersom jag har företräde mot mötande enligt vägmärket.";
        val[285] = "Jag ska stanna eftersom trafiksignalen visar rött.";
        val[286] = "Jag får köra men ska lämna företräde mot för mötande enligt vägmärket.";
        val[287] = "";

        frågor[72] = "Vad ska du göra när du i backspegeln ser en polismotorcykel som/signalerar först med helljuset och därefter växelvis med rött och/blått ljus?";
        val[288] = "Sänka hastigheten.";
        val[289] = "Öka hastigheten.";
        val[290] = "Lämna fri väg.";
        val[291] = "Stanna.";

        frågor[73] = "Vad innebär polisens tecken?";
        val[292] = "Stopp enbart för trafik som kommer framifrån";
        val[293] = "Minska hastigheten";
        val[294] = "Stopp för trafik som både kommer bakifrån och framifrån";
        val[295] = "Stopp för trafik som kommer bakifrån";

         frågor[74] = "Vad innebär polisens tecken?";
        val[296] = "Mötande ska stanna och du ska stanna";
        val[297] = "Mötande ska stanna och du ska köra";
        val[298] = "Båda ska köra men långsamt";
        val[299] = "";

        frågor[75] = "Vad innebär polismannens tecken?";
        val[300] = "Mötande ska stanna och jag får köra.";
        val[301] = "Jag ska minska hastigheten.";
        val[302] = "Jag ska stanna och mötande får köra.";
        val[303] = "Både mötande och jag ska stanna.";

        frågor[76] = "Vilket påstående är rätt om ett defensiv körsätt?";
        val[304] = "Jag lämnar alltid företräde till all korsande trafik.";
        val[305] = "Jag är alltid beredd att anpassa mig till situationer som uppstår.";
        val[306] = "Jag kör med låg hastighet i alla situationer";
        val[307] = "Jag kör offensivt";

        frågor[77] = "Du ska svänga vänster i korsningen. Vilket påstående är sant eller riktigt?";
        val[308] = "Jag har vänjningsplikt mot alla fordon på den korsande vägen.";
        val[309] = "Jag måste stanna innan jag svänger ut i korsningen.";
        val[310] = "Jag har väjningsplikt mot fordon som kommer från höger i korsningen.";
        val[311] = "";

         frågor[78] = "Du tänker fortsätta att köra rakt fram. /Gäller högerregel i någon av korsningarna?";
        val[312] = "Ja, men endast i korsningen på bild A";
        val[313] = "Ja, men endast i korsningen på bild B";
        val[314] = "Ja, i både korsningen på bild A och B.";
        val[315] = "Nej, inte i någon av korsningarna";

        frågor[79] = "Du ska köra rakt fram och cyklisten kör sakta. Vad ska du göra?";
        val[316] = "Jag kör först eftersom jag ska rakt fram";
        val[317] = ". Jag kör först eftersom jag kör på den större vägen";
        val[318] = "Jag låter cyklisten köra först eftersom hon kommer från höger";
        val[319] = "";

        frågor[80] = "Vilket påstående är riktigt om närmaste korsningen på bilden?";
        val[320] = "Vägmerket är bara för korsande trafik, därför ska jag ha uppmärksamhet och vara beredd på att stanna.";
        val[321] = "Vägmerket betyder att jag kör på huvudled, uppmärksamhet behövs inte i den här typ av korsning.";
        val[322] = "Eftersom högerregel gäller anpassar jag farten och gör mig beredd på att eventuellt stanna";
        val[323] = "";

        frågor[81] = "Du har precis passerat det här vägmärket. /Vad gäller i korsningen du kommer till om inget annat anges?";
        val[324] = "Jag ska låta trafik från höger köra före mig.";
        val[325] = "All korsande trafik skall låta mig köra först.";
        val[326] = " Jag ska låta all korsande trafik köra före mig.";
        val[327] = "Jag ska låta trafik från vänster köra före mig.";

        frågor[82] = "Vad gäller i korsningen när du ska fortsätta rakt fram?";
        val[328] = "Jag har väjningsplikt enligt högerregeln";
        val[329] = "Jag kör först eftersom jag ska rakt fram";
        val[330] = "Föraren i bilen som kommer från höger har väjningsplikt eftersom han kommer från en enskild väg.";
        val[331] = "Jag kör först eftersom jag kör på en allmän väg.";

        frågor[83] = "Du ska svänga vänster i korsningen. Vilka har du väjningsplikt mot?";
        val[332] = "Enbart fordon från höger samt gående som är på väg över gatan?";
        val[333] = "Enbart gående som är på väg över gatan.";
        val[334] = "Endast fordon från vänster samt gående som är på väg över gatan.";
        val[335] = "Fordon från vänster och höger samt gående som är på väg över gatan.";

        frågor[84] = "Du ska fortsätta rakt fram. Gäller högerregeln i någon av korsningarna?";
        val[336] = "Ja, men enbart i korsning A";
        val[337] = "Ja, men enbart i korsning B";
        val[338] = "Ja, i korsning A och B";
        val[339] = "Nej";

        frågor[85] = "Du ska svänga till höger i korsningen. Vilket påstående är riktigt?";
        val[340] = "Jag har väjningsplikt mot alla fordon på den korsande vägen.";
        val[341] = "Jag måste iaktta försiktighet innan jag svänger ut i korsningen.";
        val[342] = "Jag har väjningsplikt mot fordon som kommer fån vänster i korsningen";
        val[343] = "Jag måste alltid stanna innan jag svänger ut i korsningen";

        frågor[86] = "Stopplinjen saknas i den här situationen. Var ska du stanna?";
        val[344] = "Omedelbart före vägmärket";
        val[345] = "Omedelbart före spårområdet";
        val[346] = "30 meter före spårområdet";
        val[347] = "";

        frågor[87] = "Vad gäller i korsningen på bilden?";
        val[348] = "Jag måste svänga till vänster.";
        val[349] = "Jag måste svänga till höger.";
        val[350] = "Jag får svänga både till höger och till vänster.";
        val[351] = "";

        frågor[88] = "Du närmar dig en korsning där detta vägmärke finns uppsatt./ Hur ska du uppträda?";
        val[352] = "Jag behåller farten och gör sedan en hastig inbromsning vid väjningslinjen";
        val[353] = "Jag saktar in i god tid och måste stanna vid väjningslinjen";
        val[354] = "Jag saktar in i god tid och stannar om det behövs.";
        val[355] = "";

        frågor[89] = "Vad innebär vägmärket?";
        val[356] = "Jag har väjningsplikt mot fordon som kommer från höger och vänster.";
        val[357] = "Föraren på anslutande väg har väjningsplikt.";
        val[258] = "Jag har väjningsplikt mot fordon som kommer från höger.";
        val[359] = "";

        frågor[90] = "Du ska svänga vänster i korsningen. Vad gäller?";
        val[360] = "Jag har väjningsplikt mot all korsande trafik";
        val[361] = "Jag har väjningsplikt mot mötande trafik";
        val[362] = "Jag har väjningsplikt mot trafik från höger";
        val[363] = "All korsande trafik har väjningsplikt mot mig";

        frågor[91] = "Du ska svänga vänster direkt efter övergångsstället. Vad gäller?";
        val[364] = "All korsande trafik ska låta mig köra först.";
        val[365] = "Jag ska låta trafik från höger köra före mig.";
        val[366] = "Jag ska låta trafik från vänster köra före mig.";
        val[367] = "Jag ska låta all korsande trafik köra före mig";

         frågor[92] = "Du ska svänga till vänster. Är du skyldig att lämna/ fordon från höger företräde i någon av korsningarna?";
        val[368] = "Ja i korsning på bild A";
        val[369] = "Ja i korsning på bild B";
        val[370] = "Ja i korsning på bilderna A och B";
        val[371] = " Nej";

        frågor[93] = "Du skall fortsätta rakt fram i korsningen. Hur ska du uppträda?";
        val[372] = "Du måste lämna bussens företräde.";
        val[373] = "För att inte hindra bussen ökar du farten.";
        val[374] = "Du ska anpassa farten så att kunna stanna om bussen svänger.";
        val[375] = "";

        frågor[94] = "Du ska fortsätta rakt fram. Vad gäller?";
        val[376] = "Jag låter cyklisten köra först eftersom hon ska svänga.";
        val[377] = "Jag kör före cyklisten eftersom det är tillåtet att köra före henne.";
        val[378] = "Jag kör före cyklisten men är beredd på att stanna eftersom hon kanske svänger.";
        val[379] = "Jag låter cyklisten köra först eftersom högerregel gäller.";

         frågor[95] = "På vilken av de två situationer gäller högerregel?";
        val[380] = "På bild A";
        val[381] = "På bild B";
        val[382] = "Ingen";
        val[383] = "";

        frågor[96] = "Är du skyldig att ge bussen möjlighet att lämna/ hållplatsen och köra före dig i någon av situationerna?";
        val[384] = "Ja, men enbart i situation A";
        val[385] = "Ja, men enbart i situation B";
        val[386] = "Ja, i båda situationerna";
        val[387] = "Nej";

        frågor[97] = "Vilket är den viktigaste anledning till att anpassa hastigheten efter detta vägmärke?";
        val[388] = "Vägmärken kan saknas eller vara ändrade.";
        val[389] = "Vägen kan vara i sämre skick";
        val[390] = "Det kan finnas vägarbetare på vägen.";
        val[391] = "Vägmärken kan saknas eller vara missvisande.";

        frågor[98] = "Vilket påstående om vårt seende är riktigt?";
        val[392] = "Direktseendet utgör en riktigt liten del av synfältet.";
        val[393] = "Direktseendet utgör en riktigt stor del av synfältet.";
        val[394] = "Med periferiseendet ser man skarpt i hela synfältet.";
        val[395] = "";

        frågor[99] = "Varför måste du ha en rörlig blick när du kör bil?";
        val[396] = "Därför att jag endast i en liten del av mitt synfält kan upptäcka föremål som är i rörelse.";
        val[397] = "Därför endast en lite den av mitt synfält kan bedöma hastighet och avstånd.";
        val[398] = "Därför att jag endast i en liten del av mitt synfält kan se färger.";
        val[399] = "";

         frågor[100] = "När är risken störst att en förare kan råka ut för feltolkningar och synvillor?";
        val[400] = "Vid körning i snöyra med helljuset tänt.";
        val[401] = "Vid körning på en motorväg en varm sommardag.";
        val[402] = "Vid körning på en rak grusväg en varm (torr) sommardag.";
        val[403] = "";
       // frågor från 0 - 100 ovan//

        frågor[101] = "Vad är sant om oerfarna förares avsöknings-beteende/ i jämförelse med erfarna förare?";
        val[404] = "Oerfarna förare har ett längre och bredare avsökningsområde";
        val[405] = "Oerfarna förare har blicken riktad närmare bilen";
        val[406] = "Deras avsökningsbeteende skiljer sig inte åt";
        val[407] = "";

        frågor[102] = "Vilken är den vanligaste orsaken till att risksituationer uppstår i trafiken?";
        val[408] = "Brister hos föraren uppmärksamhet";
        val[409] = "Brister i trafikmiljön";
        val[410] = "Brister på fordonet.";
        val[411] = "";

     frågor[103] = "Du har villkor glasögon på körkortet. Vad gäller när du ska köra bil?";
        val[412] = "Jag måste ha glasögon med mig i bilen men använder de enbart vid behov, t.ex. i mörker.";
        val[413] = "Jag behöver använda glasögon eller linser.";
        val[414] = "Jag måste använda glasögon eller linser.";
        val[415] = "";

        frågor[104] = "Vad betyder tilläggstavlan?";
        val[416] = "Nedsatt rörelseförmåga";
        val[417] = "Nedsatt hörsel";
        val[418] = "Nedsatt syn";
        val[419] = "";

        frågor[105] = "Vad betyder tilläggstavlan?";
        val[420] = "Nedsatt rörelseförmåga";
        val[421] = "Nedsatt hörsel";
        val[422] = "Nedsatt syn";
        val[423] = "";

        frågor[106] = "Du ska köra om ekipaget på bilden. Hur bör du göra?";
        val[424] = "Jag kör långsamt förbi och är beredd att bromsa.";
        val[425] = "Jag blinkar med helljuset,för att påkalla uppmärksamhet.";
        val[426] = "Jag ger ljudsignal för att påkalla uppmärksamhet.";
        val[427] = "Jag ökar farten för att snabbt komma förbi.";

        frågor[107] = "Vad måste du i första hand ha beredskap för i denna situation?";
        val[428] = "Att bilen från vänster i korsningen kan köra ut framför mig";
        val[429] = "Att ett barn kan springa ut framför den parkerade bilen";
        val[430] = "Att gående kan gå ut på övergångstället";
        val[431] = "Att en bil kan komma från höger i korsningen.";

        frågor[108] = "Vad är mest förrädisk i denna situation?";
        val[432] = "Att det är dålig sikt i korsningen.";
        val[433] = "Att det kan vara halt väglag";
        val[434] = "Att cyklister på cykelbanan är svåra att se.";
        val[435] = "";

        frågor[109] = "Vad bör du särskilt tänka på när en skolskjuts blinkar enligt bilden?";
        val[436] = "Inga barn får passera vägen förrän bussen åkt.";
        val[437] = "Det kan komma barn springande.";
        val[438] = "Eftersom skolskylten blinkar måste jag lämna företräde åt bussen.";
        val[439] = "Inga barn får stiga av bussen förrän skolskylten slutar att blinka.";

        frågor[110] = "Taxin har stannat vid vägkanten och lyktorna på/ skylten blinkar gult. Hur ska du uppträda?";
        val[440] = "Jag måste släppa fram taxin när den ska starta från vägkanten.";
        val[441] = "Jag måste stanna eftersom lyktorna på skylten blinkar.";
        val[442] = "Jag får passera taxin utan att stanna om jag kör långsamt.";
        val[443] = "Jag måste ge ljudsignal innan jag passerar taxin.";

        frågor[111] = "Du ska köra ut på gatan på bilden. Vad är mest förrädisk i den här situationen?";
        val[444] = "Fordon som kan komma från vänster på gatan.";
        val[445] = "Bilarna på parkeringen som kan backa ut.";
        val[446] = "Fordon som kan komma från höger på gatan.";
        val[447] = "Fotgängare som kan komma på gångbanan.";

        frågor[112] = "Har du väjningsplikt mot fotgängaren i den här situationen?";
        val[448] = "Nej, eftersom det finns trafiksignal";
        val[449] = "Ja, eftersom övergångstället är obevakat";
        val[450] = "Nej, eftersom fotgängaren ännu inte är ute på övergångstället";
        val[451] = "";

        frågor[113] = "Hur ska du uppträda i den här situationen?";
        val[452] = "Jag ska sakta in eller stanna för att visa att jag tänker väja.";
        val[453] = "Jag kan köra vidare utan att stanna eftersom jag ser att fotogängaren har sett mig";
        val[454] = "Jag kan köra vidare eftersom fotogängaren befinner sig på mittrefugen";
        val[455] = "";

        frågor[114] = "Får du med din personbil köra på en gata där detta vägmärke finns uppsatt?";
        val[456] = "Ja, men endast i 30 km/h";
        val[457] = "Nej, endast varutransport får köra här";
        val[458] = "Nej, trafik med motorfordon är förbjudet";
        val[459] = "Ja, men bara med lugn gångfart";

        frågor[115] = "Efter vilket vägmärke måste du hålla lägst hastighet?";
        val[460] = "Vägmärke A";
        val[461] = "Vägmärke B";
        val[462] = "Vägmärke C";
        val[463] = "Vägmärke D";

        frågor[116] = "Vad är sant angående parkering på en gårdsgata?";
        val[464] = "Jag måste ha särskilt tillstånd för att få parkera på en gårdsgata.";
        val[465] = "På en gårdsgata får jag parkera var som helst om jag inte hindrar annan trafik.";
        val[466] = "På en gårdsgata får jag parkera endast på markerade parkeringsplatser.";
        val[467] = "Man får aldrig parkera på en gårdsgata.";

        frågor[117] = "Du ska fortsätta rakt fram. Vilka fordon har du väjningsplikt mot i korsningen?";
        val[468] = "Endast fordon från vänster";
        val[469] = "Endast fordon från höger";
        val[470] = "Fordon från vänster och höger";
        val[471] = "";

        frågor[118] = "Du ska köra en släkt som bor i denna väg där vägmärket är uppsatt. /Får du köra i denna här vägen?";
        val[472] = "Ja i gångfart";
        val[473] = "Ja i 30 km/h";
        val[474] = "Ja i 50 km/h";
        val[475] = "";

        frågor[119] = "Vilket av följande påståenden är riktig vid passerande av järnvägkorsning?";
        val[476] = "Så fort ett tåg passerar så kan jag köra vidare även om ljudsignalen har fortsatt att ljuda";
        val[477] = "Jag ska köra så sakta som möjligt";
        val[478] = "Oavsett hur korsningen ser ut så måste jag kontrollera så att jag kan passera utan risk";
        val[479] = "När korsningen är bevakad kan jag passera utan risk.";

        frågor[120] = "Vad är det i första hand som avgör hur fort du ska/ köra när du passerar järnvägkorsningen på bilden?";
        val[480] = "Hastighetsbestämmelse på vägen";
        val[481] = "Sikten i järnkorsningen";
        val[482] = "Säkerhets anordning i Järnkorsningen";
        val[483] = "";

        frågor[121] = "På bilden ser du skolpatrull. Vad är rätt?";
        val[484] = "Alla trafikanter är skyldiga att följa skolpatrullens anvisningar.";
        val[485] = "Deras uppgift är att stoppa trafiken på gatan.";
        val[486] = "Deras uppgift är att hjälpa barnen som ska till och från skolan.";
        val[487] = "Skolpatrullen har polismans befogenhet.";

       frågor[122] = "Vad kallar man ett körsätt där föraren anpassar sig/ efter medtrafikanterna och planerar sin körning?";
        val[488] = "Defensiv";
        val[489] = "Offensiv";
        val[490] = "Stressigt";
        val[491] = "Impulsiv";

        frågor[123] = "Vad är riktigt om den järnvägskorsning som vägmärket varnar för?";
        val[492] = "Korsningen har bommar som alltid är nedfällda när ett tåg närmar sig.";
        val[493] = "Korsningen har bommar,men dessa kan vara uppfällda trots att ett tåg närmar sig.";
        val[494] = "Korsningen saknar bommar, så jag måste själv kontrollera att inget tåg närmar sig.";
        val[495] = "";

        frågor[124] = "Du ska följa huvudledens fortsättning i korsningen./ Är du skyldig att använda körriktningsvisare?";
        val[496] = "Ja, alltid";
        val[497] = "Ja, men enbart när det finns annan trafik i korsningen.";
        val[498] = "Nej";
        val[499] = "";

        frågor[125] = "Hur gör du för att visa de bakomvarande dina avsiktar när du tänker stanna?";
        val[500] = "Jag använder mig av motorbromsen";
        val[501] = "Jag intar bromsberedskap";
        val[502] = "Jag bromsar i god tid före situationen";
        val[503] = " Jag väntar med inbromsningen och gör ett markerat stopp";

        frågor[126] = "Vad ska du göra i den här situationen?";
        val[504] = "Jag stannar bakom polisbilen när den stannar";
        val[505] = "Jag svänger in till kanten och stannar vid första möjliga tillfälle";
        val[506] = "Jag kör förbi polisbilen och stannar därefter vid kanten";
        val[507] = "";

        frågor[127] = "Hur ska du uppträda mot fotgängaren i den här situationen?";
        val[508] = "Jag kör, eftersom han är skyldig att vänta på mig.";
        val[509] = "Jag saktar in eller stannar för att visa att jag tänker låta honom gå först.";
        val[510] = "Jag kör, eftersom vi har ögonkontakt och jag ser att han tänker vänta.";
        val[511] = "";

        frågor[128] = "Du kör inne på en stor parkeringsplats och ska fortsätta rakt fram./ Bilen som kommer från höger kör sakta. Vad ska du göra?";
        val[512] = "Jag låter den andra bilen köra först eftersom den kommer från höger.";
        val[513] = "Jag kör först eftersom den andra bilen kommer från höger.";
        val[514] = "Jag kör först eftersom jag ska köra rakt fram";
        val[515] = "";

        frågor[129] = "Du ska fortsätta rakt fram. Gäller högerregeln i någon av korsningarna?";
        val[516] = "Ja, men enbart i korsning A";
        val[517] = "Ja, men enbart i korsning B";
        val[518] = "Ja, i både korsningarna";
        val[519] = "Nej.";

        frågor[130] = "Denna kontrollampa tänds under körning. Vad ska du göra?";
        val[520] = "Du ska stänga av motorn omedelbart för att oljan är slut";
        val[521] = "Du behöver inte stanna för att motorn är full med olja";
        val[522] = "Kylarvätskan är slut.";
        val[523] = "";

        frågor[131] = "Vad kan hända om du kopplar startkablarna fel/ mellan två fordons batteri för hjälpstart?";
        val[524] = "Batteriet kan explodera";
        val[525] = "Glödlamporna kan gå sönder";
        val[526] = "Båda fordon kan bli strömförande";
        val[527] = "Startmotorn kan börja gå baklänges";

        frågor[132] = "Får du köra en bil där färdbromsen är ur funktion om/ parkeringsbromsen fungerar bra?";
        val[528] = "Nej jag måste anlita en bärgningsbil.";
        val[529] = "Ja, men endast i gångfart";
        val[530] = "Ja, men endast närmaste vägen till en verkstad.";
        val[531] = "Nej, jag måste bogsera bilen med en bogseringslina.";

        frågor[133] = "Varför rekommenderas regelbundet byte av bromsvätskan?";
        val[532] = "Det är risk att den får paraffinutfällningar med tiden";
        val[533] = "Den drar till sig dammpartiklar";
        val[534] = "Den blir trögflytande med tiden";
        val[435] = "Den drar till sig vatten vilket sänker kokpunkten";

        frågor[134] = "Vilken typ av vätska finns i den inringade behållaren på bilden?";
        val[536] = "Glykol blandat med vatten";
        val[537] = "Bromsvätska";
        val[538] = "Styrservoolja";
        val[539] = "Spolarvätska";

        frågor[135] = "Din bil, som utrustade med bromsservosystem ska bogseras med/avstängd motor. Vilken effekt kan du förvänta dig?";
        val[540] = "Bromsen slutar fungera.";
        val[541] = "Det blir lätt att trycka ner bromspedalen";
        val[542] = " Det blir hårdare att trycka ner bromspedalen";
        val[543] = "";

        frågor[136] = "Vilken fördel har ABS-bromsar jämfört med bromsar utan ABS?";
        val[544] = "Vid hård bromsning är risken för sladd mindre med ABS";
        val[545] = "Bromssträckan blir alltid kortare med ABS";
        val[546] = "Bromssystemet är underhållsfritt eftersom ABS-systemet är slutet";
        val[547] = "";

       frågor[137] = "Vad är fördelen med ABS-bromsar?";
        val[548] = "Att förslitningen på bromsarna minskar.";
        val[549] = "Att man inte behöver trycka så hårt på bromspedalen.";
        val[550] = "Att bilen går att styra även vid maximal inbromsning.";
        val[551] = "Att bromsarna låser sig vid kraftiga inbromsningar och därmed ger kortare bromssträcka.";

        frågor[138] = "Hur kan du med hjälp av en kraftig inbromsning /avgöra om ABS bromsarna fungerar?";
        val[552] = "Jag hör att det tjuter om däcken";
        val[553] = "Jag känner vibrationer i bromspedalen";
        val[554] = "Jag märker att hjulen låser sig";
        val[555] = "";

        frågor[139] = "När måste du ha vinterdäck på din personbil?";
        val[556] = "1 December till 31 Mars även när det inte är vinterväglag.";
        val[557] = "1 November till 31 Mars även när det inte är vinterväglag";
        val[558] = "1 Oktober till 30 April när det är vinterväglag.";
        val[559] = "1 December till 31 Mars när det är vinterväglag";

        frågor[140] = "Du kör en lätt lastbil i januari och det är halt väglag. /Måste lastbilen ha vinterdäck?";
        val[560] = "Ja";
        val[561] = "Nej";
        val[562] = "Ja, enbart om lastbilen har tillkopplat släp.";
        val[563] = "";

        frågor[141] = "Du tänker köra bil i februari när väglaget är som bilden./ Vad är det minsta tillåtna mönsterdjup på däcken?";
        val[564] = "3,0 mm";
        val[565] = "1,6 mm";
        val[566] = "1,0 mm";
        val[567] = "6,0 mm";

        frågor[142] = "Vilket alternativ anger tidsgränserna för användande av dubbdäck?";
        val[568] = "Från första november till och med sista mars";
        val[569] = "Från första oktober till och med sista april";
        val[570] = "Från första oktober till och med Annandag påsk";
        val[571] = "Från första november till och med första söndagen efter påsk";

       frågor[143] = "Vad betyder att den här varningslampan lyser?";
        val[572] = "Att bromsvätskenivån i ABS är låg";
        val[573] = "Att ABS är kopplat";
        val[574] = "Att ABS-bromsar är ur funktion";
        val[575] = "";

        frågor[144] = "Det är september månad och det snöar kraftigt. /Det är minus grader och snön ligger kvar på vägen. /Får du ha dubbdäck på bilen när du kör?";
        val[576] = "Ja, efter tillstånd av vägverket.";
        val[577] = "Ja, efter tillstånd av polisen.";
        val[578] = "Nej, inte förrän den första oktober.";
        val[579] = "Ja, eftersom det är vinterväglag.";

        frågor[145] = "Varför är däcket ojämnt slitet?";
        val[580] = "För lågt lufttryck";
        val[581] = "Fel på hjulinställning";
        val[582] = "Obalans";
        val[583] = "Fel material";

        frågor[146] = "Vilken däck ska du förse din bil när det finns risk för halka?";
        val[584] = "Däck som märkta med M S";
        val[585] = "Däck som är märkta med K T";
        val[586] = "Däck som är märkta med A B";
        val[587] = "Däck som är märkta med S F";

        frågor[147] = "Vilket är minsta tillåtna mönsterdjup på en /personbilsdäck som används på sommartid?";
        val[588] = "1,6 mm";
        val[589] = "1,0 mm";
        val[590] = "0,6 mm";
        val[591] = "2,0 mm";

         frågor[148] = "Vilken begränsning har ett så kallat nödhjul (Temporary spare)?";
        val[592] = "Däcket får aldrig monteras på bilen om övriga däck är dubbade.";
        val[593] = "Däcket åldras snabbare än vanliga däck.";
        val[594] = "Däcket får aldrig köras i högre hastighet än vad som anges på däcket.";
        val[595] = "Däcket får endast användas på torr vägbana.";

        frågor[149] = "Vad är viktigt att tänka på när din bil blir bogserad med motorn avslagen?";
        val[596] = "Jag ska låta en låg växel ligga i.";
        val[597] = "Det kan bli svårare att styra.";
        val[598] = "Belysningen fungerar inte.";
        val[599] = "Bromsarna kan sluta att fungera.";

       frågor[150] = "Hur stort får avståndet mellan fordonet högst /vara om bogseringslinan saknar markering?";
        val[600] = "2 meter";
        val[601] = "3 meter";
        val[602] = "4 meter";
        val[603] = "5 meter";

        frågor[151] = "Vilken är den högsta tillåtna hastighet vid bogsering med bogseringslina?";
        val[604] = "30km/h";
        val[605] = "40km/h";
        val[606] = "50km/h";
        val[607] = "60km/h";

         frågor[152] = "Hur mycket är det största tillåtna fordonsbredd med lasten inräknad?";
        val[608] = "280 cm";
        val[609] = "240 cm";
        val[610] = "260 cm";
        val[611] = "220 cm";

        frågor[153] = "Din personbil är 180cm bred. Hur mycket får lasten som mest /skjuta ut utanför bilens ena sida där vid färd på allmän väg?";
        val[612] = "Högst 20 cm";
        val[613] = "Högst 10 cm";
        val[614] = "Högst 30 cm";
        val[615] = "Högst 40 cm";

        frågor[154] = "Du ska transportera en säng på takräcket på din bil./ Bilen är 160 cm bred och du ska köra på allmän väg. /Hur bred får sängen maximalt vara?";
        val[616] = "200 cm";
        val[617] = "180 cm";
        val[618] = "160 cm";
        val[619] = "220 cm";

        frågor[155] = "Du kör i mörker och du har last som skjuter ut framåt så/ mycket att du måste markera den. Hur ska markeringen se ut?";
        val[620] = "Vit lyckta och vit reflex";
        val[621] = "Röd och gul flagga";
        val[622] = "Röd lykta och röd reflex";
        val[623] = "Enbart röda reflexer.";

        frågor[156] = "Du har last på bilen som skjuter ut 70cm framför bilen. Måste lasten märkas ut?";
        val[624] = "Ja, alltid";
        val[625] = "Ja, om det inte syns tydligt.";
        val[626] = "Nej";
        val[627] = "";

         frågor[157] = "Du kör en bil som har maxlast 360 kg. Du har passagerare som/ väger ungefär 280kg. Hur mycket kvar får du lasta maximalt?";
        val[628] = "30 kg";
        val[629] = "80 kg";
        val[630] = "150 kg";
        val[631] = "";

        frågor[158] = "Registreringsbeviset tillhör din bil. Hur mycket väger bilen med full last?";
        val[632] = "1050 kg";
        val[633] = "1370 kg";
        val[634] = "1400 kg";
        val[635] = "";

        frågor[159] = "Du är ensam i personbilen och har lastat 200kg bagage. Hur stor är bruttovikten?";
        val[636] = "1370 kg";
        val[637] = "390 kg";
        val[638] = "1180 kg";
        val[639] = "";

        frågor[160] = "Vilket alternativt är riktigt?";
        val[640] = "Tjänstevikt + maxlast = totalvikt";
        val[641] = "Tjänstevikt + totalvikt = maxlast";
        val[642] = "Tjänstevikt + totalvikt = bruttovikt";
        val[643] = "Totalvikt + maxlast = tjänstevikt";

         frågor[161] = "Anses släpet som lätt släpfordon i något av dessa alternativ?";
        val[644] = "Ja, i alternativ A";
        val[645] = "Ja, i alternativ B";
        val[646] = "Ja, i alternativ C";
        val[647] = "Nej";

        frågor[162] = "Du tänker koppla en släpvang till din bil. Du har körkort med/ behörighet B. Får du dra denna släpvagn med din bil?";
        val[648] = "Ja, om släpvagnens last är inte mer än 430 kg.";
        val[649] = "Nej";
        val[650] = "Ja, om släpvagnen är olastad";
        val[651] = "";

        frågor[163] = "Du ska koppla ett släpvagn till denna bil. /Vilken regel gäller om du har B behörighet i ditt körkort?";
        val[652] = "Du får inte koppla släpvagn som har totalvikt över 750 kg.";
        val[653] = "Du får koppla ett släpvagn som totalvikt 1150 kg";
        val[654] = "Du får koppla en släpvagn som har en totalvikt över 1150 kg förutsatt att den är olastad.";
        val[655] = "Du får koppla till en släpvagn som har totalvikt över 1150 kg, inte överskrida släpvagnens vikt.";

        frågor[164] = "Vilken av fordonskombinationerna får du köra med behörighet B?";
        val[656] = "Personbil med tjänstevikt 1300kg som drar en släpvagn med totalvikt 1200 kg.";
        val[657] = "Personbil med tjänstevikt 1100kg som drar en släpvagn med totalvikt 1200 kg.";
        val[658] = "Personbil med totalvikt 1400kg som drar en släpvagn med totalvikt 1400kg.";
        val[659] = "Personbil med totalvikt 2000kg som drar en släpvagn med totalvikt 1600kg.";

        frågor[165] = "En person har BE behörighet i sitt körkort och kör den bil vars /registreringsbevis du ser här ovan. Vilket eller vilka av /följande släpvagnar får han dra med denna bil?";
        val[660] = "Ett släp med Tjänstevikt 950 kg och last 750 kg";
        val[661] = "Ett släp med Totalvikt 1760 kg";
        val[662] = "Ett släp som har Bruttovikt 1810 kg";
        val[663] = "Ett släp med Bruttovikt 1500 kg";

        frågor[166] = "Du kopplar en lastad släpvagn till din bil. Hur kan du enklast ändra kultrycket?";
        val[664] = "Jag fördelar om lasten på släpet";
        val[665] = "Jag ökar lufttrycket i släpets däck";
        val[666] = "Jag minskar lufttrycket i släpets däck";
        val[667] = "Jag fördelar om lasten i bilen";

        frågor[167] = "Vilket fordon skall baktill ha sådan triangelformade reflexer som bilden visar?";
        val[668] = "Släpvagn";
        val[669] = "Motorredskap klass I";
        val[670] = "Lastbil";
        val[671] = "Traktor";

        frågor[168] = "Du ska kontrollbesiktiga din personbil med registreringsnummer ABC 223. /Vilken period gäller?";
        val[672] = "1:a Januari till 31:a Maj";
        val[673] = "1:a Januari till 31:a Mars";
        val[674] = "1:a Februari till 30 Juni";
        val[675] = "1:a December till 31:a Mars";

         frågor[169] = "Du ska kontrollbesiktiga din personbil med registreringsnummer OGM 435./ Vilken besiktningsperiod gäller?";
        val[676] = "1 maj till 30 september";
        val[677] = "1 mars till 31 maj";
        val[678] = "1 mars till 31 juli";
        val[679] = "1 maj till 31 juli";

        frågor[170] = "Vid kontrollbesiktningen på bilprovningen har din bil ett fel som måste åtgärdas./ Bilen blir underkänd men du behöver inteåterkomma för ny besiktning/ om du åtgärdar felet snarast.Vad kan följden bli om du inte /rättar till felet och du blir stoppad i en poliskontroll.";
        val[680] = "Polisen kan ålägga mig att låta bilprovningen göra en ny kontrollbesiktning.";
        val[681] = "Bilen får körförbud.";
        val[682] = "Polisen kan ålägga mig att åtgärda felet inom tre dagar och därefter kontakta polisen för kontroll.";
        val[683] = "";

        frågor[171] = "Vilken besiktningsperiod är lämplig för att besikta /bilen om registreringsnumret sluter med 4.";
        val[684] = "Från februari till juni";
        val[685] = "Från april till augusti";
        val[686] = "Från februari till juli";
        val[687] = "";

        frågor[172] = "Du har inte låtit besikta din personbil inom den tidsperiod som gäller. /Vad händer?";
        val[688] = "Jag får använda bilen tills jag får besked om körförbud från vägverket.";
        val[689] = "Jag får använda bilen tills jag får besked om körförbud från polisen";
        val[690] = "Bilen får automatiskt körförbud när perioden är slut.";
        val[691] = "";

        frågor[173] = "Varningslampan för färdbromsen tänds under färd. Vad ska du göra?";
        val[692] = "Jag kör till närmast verkstad";
        val[693] = "Jag åtgärdar felet vid nästa service";
        val[694] = "Jag stannar omedelbart för att kontrollera bromsvätskenivån";
        val[695] = "Jag fyller på bromsvätska vid nästa tankning";

         frågor[174] = "Du har köpt en bil 1 mars. Hur lång tid har du på dig att anmäla/ ägarbytet till vägverket för att den ska ha registretats den 1 mars?";
        val[696] = "7 dagar";
        val[697] = "20 dagar";
        val[698] = "14 dagar";
        val[699] = "10 dagar";

        frågor[175] = "Du ändrar din bil så att den inte överensstämmer med registreringsbeviset. /När måste du registreringsbesikta bilen?";
        val[700] = "Inom 1 vecka";
        val[701] = "Inom 1 månad";
        val[702] = "Inom 3 veckor";
        val[703] = "Inom 3 månader";

         frågor[176] = "På vilken av följande situationer måste fordonet inställas /för en ny registreringsbesiktning?";
        val[704] = "Om fordonet försätts med dragordning";
        val[705] = "Om fordonet försetts med extra ljus";
        val[706] = "Om fordonet försetts med en barnstol";
        val[707] = "";

        frågor[177] = "Vilken försäkring måste finnas för att en personbil ska få användas i trafik?";
        val[708] = "Trafikförsäkring";
        val[709] = "Halvförsäkring";
        val[710] = "Helförsäkring";
        val[711] = "Vagnskadeförsäkring";

         frågor[178] = "Du tvingas väja från ett djur och kör i diket med din bil och bilen skadas./ Vilken försäkring måste du ha för att ha ersättning ur din/ egen försäkring för reparationskostnader och bärgning?";
        val[712] = "Halvförsäkring";
        val[713] = "Trafikförsäkring";
        val[714] = "Helförsäkring";
        val[715] = "Hemförsäkring";

        frågor[179] = "Vilken vätska fyller man i kylen om motortemperaturen blir för hög?";
        val[716] = "Glykol och vatten.";
        val[717] = "Olje och vatten";
        val[718] = "Destillerat vatten.";
        val[719] = "";

        frågor[180] = "Var fyller du på motorolja?";
        val[720] = "I behållare A";
        val[721] = "I behållare B";
        val[722] = "I behållare C";
        val[723] = "I behållare D";

        frågor[181] = "Det är vinterväglag och du kör en personbil med dubbdäck och du ska koppla en/ släpvagn till din bil. Vilka däck måste du ha på släpvagnen?";
        val[724] = "Sommardäck";
        val[725] = "Dubbdäck";
        val[726] = "Vinterdäck";
        val[727] = "Friktionsdäck";

        frågor[182] = "Du kör i dagsljus och har last som skjuter mer än 1 meter bakåt./ Hur ska markeringen se ut?";
        val[728] = "Gul flagga";
        val[729] = "Röd flagga";
        val[730] = "Rödgul flagga";
        val[731] = "";

         frågor[183] = "Du bör ha de däck som har störst mönsterdjup på bakhjulen på bilen. Varför?";
        val[732] = "Bakhjulen belastas mest av last i bagageutrymmet.";
        val[733] = "Köregenskaperna blir bättre om bakhjulen har bra grepp.";
        val[734] = "Vid kraftig inbromsning belastas bakhjulen mest.";
        val[735] = "";

        frågor[184] = "På en säkerhetskontroll upptäcker du att bromsvätskenivån är för låg./ Vad kan orsaken vara?";
        val[736] = "Vätskan har förbrukats så som den gör när man bromsar";
        val[737] = "Vätskan har avdunstat.";
        val[738] = "Bromsledningarna har gått sönder så att vätskan har läckt ut.";
        val[739] = "";

        frågor[185] = "Varför bör du kontrollera lufttrycket i däcken innan du kör bil?";
        val[740] = "För att lagen kräver det.";
        val[741] = "För att det inte får vara för högt lufttryck när jag kör bil.";
        val[742] = "För att för högt lufttryck är både bättre för min ekonomi och miljön.";
        val[743] = "";

        frågor[186] = "Hur kan du veta att bromsservosystemet fungerar?";
        val[744] = "Jag trycker på bromspedalen, starta motorn och förvissar mig att bromspedalen inte sjunker.";
        val[745] = "Jag trycker på bromspedalen, starta motorn och förvissar mig att bromspedalen sjunker.";
        val[746] = "Jag förvissar mig om att det finns tillräckligt med bromsvätska.";
        val[747] = "Jag bromsar och förvissar mig om att däcken låser sig.";

         frågor[187] = "Får du köra med odubbade vinterdäck på sommaren?";
        val[749] = "Nej";
        val[749] = "Ja, men det är olämpligt ur miljösynpunkt och kan försämra bilens köregenskaper.";
        val[750] = "Ja, men enbart tillfälligt, t.ex. som reservdäck vid punktering.";
        val[751] = "";

        frågor[188] = "Vad innebär det att en bil är utrustad med ett tvåkrets bromssystem?";
        val[752] = "Att bilen är utrustad med både färdbroms och parkeringsbroms";
        val[753] = "Att bilen går att bromsa hjälpligt även om ett av bromsrören går sönder";
        val[754] = "Att bilens hjul aldrig låser sig vid hårda inbromsningar";
        val[755] = "Att bilen är utrustad med både färdbroms och bromsservo";

        frågor[189] = "Du ska köra hem ett lass grus med din släpkärra. Släpkärrans totalvikt/ är 1500 kg och tjänstevikten 350 kg. Du har körkort med behörighet B./ Med vilken bil får du dra släpkärran?";
        val[756] = "En bil som har tjänstevikt 1500 kg och totalvikt 1900 kg.";
        val[757] = "En bil som har tjänstevikten 1290 kg och totalvikt1700 kg.";
        val[758] = "En bil som har tjänstevikt 1800 kg och totalvikt 2400";
        val[759] = "";

        frågor[190] = "Vilken är den största tillåtna hastighet efter detta vägmärke om inget annat anges?";
        val[760] = "30 km/h";
        val[761] = "50 km/h";
        val[762] = "70 km/h";
        val[763] = "";

        frågor[191] = "Vilken är den högsta tillåtna hastighet efter detta vägmärke om inget annat anges?";
        val[764] = "30 km/h";
        val[765] = "50 km/h";
        val[766] = "70 km/h";
        val[767] = "90 km/h";

         frågor[192] = "Vilket av trafiksignaler vid något alternativ anger /att det är tillåtet att svänga till vänster?";
        val[768] = "Ja, enbart på bild A";
        val[769] = "Ja, enbart på bild B";
        val[770] = "Ja, både på bild A och B";
        val[771] = "Nej, varken på bild A eller B";

        frågor[193] = "Du ska svänga vänster. I vilken situation kan fordon som /korsar din färdväg kan ha grönt ljus samtidigt?";
        val[772] = "Bild A ";
        val[773] = "Bild B";
        val[774] = "Varken Bild A eller B";
        val[775] = "";

        frågor[194] = "Du har grönt ljus och ska svänga till vänster i korsningen./ Hur ska du uppträda?";
        val[776] = "Jag kör sakta framåt medan mötande trafik passerar";
        val[777] = "Jag stannar vid stopplinjen och väntar medan mötande trafik passerar";
        val[778] = "Jag kör eftersom mötande trafik har rött ljus";
        val[779] = "Jag kör eftersom mötande trafik ska lämna företräde";

         frågor[195] = "Du ska svänga till vänster. Fotgängaren är på väg att korsa/ gatan och har grönt ljus samtidigt som dig. Vad gäller?";
        val[780] = "Jag måste lämna fotgängaren företräde även om hon/han inte hunnit lämna refugen";
        val[781] = "Jag får inte stanna i korsningen när jag har grönt ljus";
        val[782] = "Jag behöver inte lämna fotgängaren företräde,personen är skyldig att stanna på refugen i mitten av gatan";
        val[783] = "";

        frågor[196] = "Du kör i 50km i timmen och befinner dig i situationen /på bilden. Signalen ändras plötsligt till fast gult ljus./ Hur ska du uppträda?";
        val[784] = "Jag kör vidare eftersom att jag inte kan stanna på ett betryggande sätt.";
        val[785] = "Jag kör som om det är på väg att bli grönt ljus.";
        val[786] = "Jag stannar eftersom att fast gult ljus betyder stopp.";
        val[787] = "";

        frågor[197] = "Du ska köra rakt fram i vägkorsningen. Trafiksignalen är ur funktion./ Har du väjningsplikt mot någon trafik i korsningen?";
        val[788] = "Nej";
        val[789] = "Ja, mot trafik både från vänster och höger";
        val[790] = "Ja, men enbart mot trafik från höger";
        val[791] = "Ja, men enbart från trafik från vänster.";

        frågor[198] = "Signalen på bilden visar blinkande gult ljus. /Vilket påstående om situationen på bilden är riktig?";
        val[792] = "Högerregeln gäller vid korsningen.";
        val[793] = "Om du har stannat vid stopplinjen får du inte köra igång förrän du har grönt ljus.";
        val[794] = "Du behöver inte stanna eftersom signalen gäller före vägmärket.";
        val[795] = "Du måste stanna vid stopplinjen även om det inte kommer några fordon på den korsande vägen.";

        frågor[199] = "Du ska fortsätta rakt fram. Vad är viktigast att/ du först har beredskap för i denna situation?";
        val[796] = "Att cyklisten i mitt körfält svänger vänster";
        val[797] = "Att mötande fordon svänger vänster i korsningen";
        val[798] = "Att jag kanske måste stanna för fotgängarna på övergångsstället";
        val[799] = ". Att fordon från höger i korsningen kör mot rött ljus";

         frågor[200] = "Du ska svänga höger. Vilken dold fara måste du vara beredd på i denna situation?";
        val[800] = "Gående som går ut på övergångstället framför mig.";
        val[801] = "Cyklister som kommer bakifrån på cykelbanan";
        val[802] = "Cyklister som kommer framifrån på cykelbanan.";
        val[803] = "Fordon som kommer framifrån och ska svänga vänster i korsningen";
       // frågor från 100 - 200 ovan //

        frågor[201] = "Du ska fortsätta rakt fram. Vad är viktigast att vara beredd på i den här situationen?";
        val[804] = "Att föraren framför ångrar sig och fortsätter rakt fram.";
        val[805] = "Att föraren framför stannar";
        val[806] = "Att föraren i vänster körfält byter till mitt körfält i korsningen.";
        val[807] = "";

        frågor[202] = "Du ska svänga höger i korsningen. Vad gäller?";
        val[808] = "Jag får köra men jag har väjningsplikt mot trafiken på den korsande vägen";
        val[809] = "Jag måste stanna eftersom trafiksignalen visar rött ljus";
        val[810] = "Jag måste stanna eftersom fordon på korsande vägen har grönt ljus.";
        val[811] = "Jag måste stanna eftersom korsande vägen har huvudled";

        frågor[203] = "Ett tåg har precis kört förbi. Får du passera järnvägsstationen i den här situationen?";
        val[812] = "Ja, om det inte kommer något tåg";
        val[813] = "Ja, om sikten är bra";
        val[814] = "Nej, eftersom det är rött ljus.";
        val[815] = "Ja, eftersom bommarna är uppe.";

         frågor[204] = "För vilka gäller det här vägmärket?";
        val[816] = "Trafiken som ska svänga till höger";
        val[817] = "Trafiken som ska svänga till vänster";
        val[818] = "Trafiken som ska köra rakt fram";
        val[819] = "Vägmärket gäller för alla riktningar";

        frågor[205] = "Får du svänga till höger?";
        val[820] = "Ja";
        val[821] = "Nej";
        val[822] = "";
        val[823] = "";

        frågor[206] = "Vilka fordon får köra på ett körfält som är markerat /med det här vägmärket om inget annat anges?";
        val[824] = "Bara cyklar, mopeder och fordon i linjetrafik";
        val[825] = "Bara fordon i linjetrafik";
        val[826] = "Bara LGF fordon och fordon i linjetrafik";
        val[827] = "Bara cyklar, mopeder, traktorer och fordon i linjetrafik";

        frågor[207] = "Det är lördag och klockan är 08.00. Du ska strax svänga till höger./ Får du använda körfältet om den är markerad med A?";
        val[828] = "Nej, eftersom körfältet enbart är avsedd till bussar i linjetrafik oavsett tid.";
        val[829] = "Ja, eftersom körfältet är reserverad för bussen i linjetrafik mellan kl. 10.00 och 14.00";
        val[830] = "Nej, eftersom körfältet är reserverad för bussen i linjetrafik mellan kl. 07.00 och 09.00";
        val[831] = "";

        frågor[208] = "Får du i någon av de här situationerna överskrida den heldragna linjen?";
        val[832] = "Ja, men enbart i A och B";
        val[833] = "Ja men enbart i A och C";
        val[834] = "Ja, men enbart i B och C";
        val[835] = "Ja, i samtliga situationer";

        frågor[209] = "Lastbilen på bilden är uppställd på grund av arbete./ Får du köra över spärrlinjen för att köra förbi lastbilen?";
        val[836] = "Ja";
        val[837] = "Nej";
        val[838] = "";
        val[839] = "";

        frågor[210] = "Får du köra om en cykel före ett obevakat övergångsställe?";
        val[840] = "Ja";
        val[841] = "Nej";
        val[842] = "";
        val[843] = "";

        frågor[211] = "Du kör på en gata med två körfält med din körriktning./ Varför är det förbjudet att köra om i den här situationen?";
        val[844] = "Mittlinjen";
        val[845] = "Korsningen";
        val[846] = "Framförvarande fordon kan byta körfält";
        val[847] = "Övergångsstället";

        frågor[212] = "I vilket fall anses det att du har parkerat?";
        val[848] = "När jag har stannat och sitter kvar i bilen för att vänta på någon som utför ett ärende.";
        val[849] = " När jag har stannat bilen för att släppa av en passagerare";
        val[850] = "När jag har stannat bilen för att ta upp en passagerare";
        val[851] = "När jag har stannat bilen för att lasta ur gods";

        frågor[213] = "Får du parkera som den inringade bilen?";
        val[852] = "Ja, men endast om jag har särskilt parkerings tillstånd";
        val[853] = "Nej, eftersom jag måste parkera på högersida i färdriktningen.";
        val[854] = "Ja, eftersom det är en allmän parkeringsplats.";
        val[855] = "Nej, eftersom det är för nära till en korsning";

        frågor[214] = "Vad gäller efter detta märke?";
        val[856] = "Du får inte stanna";
        val[857] = "Du får inte parkera";
        val[858] = "Du får parkera";
        val[859] = "Du får stanna och parkera";

        frågor[215] = "Vad innebär vägmärkets kombination?";
        val[860] = "Det är förbjudet att stanna efter vägmärket";
        val[861] = "Det är förbjudet att parkera efter detta vägmärke";
        val[862] = "Det är förbjudet att parkera före detta vägmärke";
        val[863] = "Det är förbjudet att stanna före detta vägmärke";

        frågor[216] = "Du parkerar din bil lördag kl. 20.00. När får du senast hämta den?";
        val[864] = "Söndag kl. 7.00";
        val[865] = "Söndag kl. 24.00";
        val[866] = "Måndag kl. 7.00";
        val[867] = "Lördag kl. 24.00";

        frågor[217] = "Vilket påstående är riktigt?";
        val[868] = "Det är tillåtet att parkera helgfria lördagar kl. 09.00.";
        val[869] = "Det är tillåtet att parkera söndagar kl. 08.00.";
        val[870] = "Det är tillåtet att parkera vardagar kl. 15.00";
        val[871] = "";

        frågor[218] = "Vilket påstående är riktigt?";
        val[872] = "Du får stanna måndag kl. 15.00.";
        val[873] = "Du får stanna söndag kl. 10.00.";
        val[874] = "Du får stanna söndag kl. 15.00.";
        val[875] = "";

        frågor[219] = "Vilket påstående är riktigt?";
        val[876] = "Det är förbjudet att parkera vardagar kl. 20.00";
        val[877] = "Det är förbjudet att stanna söndagar kl 16.00";
        val[878] = "Det är förbjudet att stanna vardagar kl.15.00";
        val[879] = "";

        frågor[220] = "Vilket alternativ stämmer?";
        val[880] = "Du får inte stanna måndag kl. 15.00";
        val[881] = "Du får inte parkera lördag kl. 20.00";
        val[882] = "Du får inte stanna lördag kl. 18.00";
        val[883] = "";

        frågor[221] = "Vilken alternativ stämmer inte?";
        val[884] = "Du får stanna måndag kl. 15.00";
        val[885] = "Du får parkera måndag kl. 20.00";
        val[886] = " Du får stanna söndag kl. 16.00";
        val[887] = "";

       frågor[222] = "Du har parkerat bilen lördag kl. 20.00. När måste du senast hämta bilen?";
        val[888] = "Måndag kl.10.00";
        val[889] = "Måndag kl 9.00";
        val[890] = "Lördag kl. 15.00";
        val[891] = "Söndag kl. 10.00";

        frågor[223] = "Du parkerar din bil en måndag klockan 07.00. När måste du senast hämta bilen?";
        val[892] = "Måndag kl 09.00";
        val[893] = "Måndag kl 10.00";
        val[894] = "Tisdag kl. 08.00";
        val[895] = "Tisdag kl. 10.00";

        frågor[224] = "Du parkerar din bil måndag klockan 13.00 och följande dag är en vardag./ När måste du senast hämta bilen?";
        val[896] = "Måndag kl 15.00";
        val[897] = "Tisdag kl 8.00";
        val[898] = "Tisdag kl 10.00";
        val[899] = "";

        frågor[225] = "Du parkerar din bil onsdag kl. 16.00 och följande dag är en vardag./När måste du senast hämta bilen?";
        val[900] = "Onsdag kl. 18.00";
        val[901] = "Torsdag kl. 10.00";
        val[902] = "Torsdag kl. 8.00";
        val[903] = "";

        frågor[226] = "Vilka får parkera på en parkeringsplats för rörelsehindrade?";
        val[904] = "Den som är rullstolsbunden";
        val[905] = "Rörelsehindrade med särskilt tillstånd";
        val[906] = "Alla som är rörelsehindrade";
        val[907] = "En skåpbil som plockar upp rörelsehindrade";

        frågor[227] = "Vilket avstånd måste det minst vara före ett övergångställe när du ska parkera?";
        val[908] = "10 meter";
        val[909] = "20 meter";
        val[910] = "30 meter";
        val[911] = "40 meter";

        frågor[228] = "Vilken bil har parkerat rätt?";
        val[912] = "Den ljusa bilen";
        val[913] = "Den mörka bilen";
        val[914] = "Båda bilarna";
        val[915] = "Ingen";

        frågor[229] = "I vilken bild eller i vilka av bilderna har den gröna bilen parkerat rätt?";
        val[916] = "Endast i bild A";
        val[917] = "Endast i bild B";
        val[918] = "Endast i bild C";
        val[919] = "Endast i bild A och B";

        frågor[230] = "Vilket avstånd måste det minst vara före en vägkorsning när du ska parkera?";
        val[920] = "8 meter";
        val[921] = "10 meter";
        val[922] = "12 meter";
        val[923] = "30 meter";

        frågor[231] = "På vilken av dessa platser är det förbjudet att stanna /för att släppa av en passagerare?";
        val[924] = "På en huvudled.";
        val[925] = "Där bilen skymmer ett vägmärke.";
        val[926] = "På körbanan bredvid en parkerad bil.";
        val[927] = "Framför en infart.";

        frågor[232] = "På vilken av följande plats får du inte stanna?";
        val[928] = "På huvudled utanför tättbebyggt område.";
        val[929] = "På vanlig väg.";
        val[930] = "I vägport eller tunnel.";
        val[931] = "";

        frågor[233] = "Du ska hämta en passagerare. Får du stanna som föraren i den här bilen gjort?";
        val[932] = "Ja, men högst 1 minut";
        val[933] = "Ja, om jag på så sätt kan underlätta för övrig trafik ";
        val[934] = "Nej";
        val[935] = "Ja, eftersom gatan är smal";

        frågor[234] = "Får du stanna som bilen på bilden?";
        val[936] = "Ja, om avståndet till mittlinje är 1 meter";
        val[937] = "Ja, om avståndet till mittlinje är 2 meter";
        val[938] = "Ja, om avståndet till mittlinje är 3 meter";
        val[939] = "Nej";

        frågor[235] = "Du har fått motorhaveri på din bil på en plats där det är förbjudet att stanna. /Vilket påstående är riktigt?";
        val[940] = "Jag måste flytta bilen om den står på en väg där högsta tillåtna hastigheten är 70km/h eller högre.";
        val[941] = "Jag behöver inte flytta bilen om jag sätter ut en varningstriangel";
        val[942] = "Jag måste flytta bilen om den står på en väg där högsta tillåtna hastigheten är 50km/h eller lägre.";
        val[943] = "Jag måste flytta på bilen snarast möjligt oavsett vilken hastighet som gäller på vägen";

        frågor[236] = "Får du parkera din bil så som på bilden?";
        val[944] = "Ja, jag får parkera som på bilden.";
        val[945] = "Nej, jag får inte parkera som på bilden.";
        val[946] = "Ja, jag får parkera här men bara om jag slagit på varningsblinken.";
        val[947] = "";

        frågor[237] = "Du behöver stanna för att släppa av en passagerare. Vad gäller här?";
        val[948] = "Jag får stanna intill trottoarkanten.";
        val[949] = "Jag får stanna intill den heldragna linjen.";
        val[950] = "Jag får inte stanna.";
        val[951] = "";

        frågor[238] = "Du tänker parkera nära en järnvägskorsning. /Hur långt måste det minst vara till korsningen för att du ska få parkera?";
        val[952] = "5 meter";
        val[953] = "10 meter";
        val[954] = "30 meter";
        val[955] = "20 meter";

        frågor[239] = "Får du parkera bilen på denna väg?";
        val[956] = "Du får parkera i 5 minuter";
        val[957] = "Du får parkera i 10 minuter";
        val[958] = "Nej det får du inte";
        val[959] = "Du får parkera om du slår på varningsblinkers";

        frågor[240] = "Vilket vägmärke innebär bland annat att parkeringen är förbjuden?";
        val[960] = "Vägmärke på bild A";
        val[961] = "Vägmärke på bild B";
        val[962] = "Vägmärke på bild C";
        val[963] = "Vägmärke på bild D";

        frågor[241] = "Vilket alternativ är rätt?";
        val[964] = "Du får parkera bredvid ett fordon som har stannat.";
        val[965] = "Du får stanna bredvid ett parkerat fordon för att släppa av passagerare.";
        val[966] = "Du får stanna så att du skymmer ett vägmärke eller trafiksignal";
        val[967] = "";

        frågor[242] = "Är det tillåtet att stanna eller parkera vid en busshållplats?";
        val[968] = "Ja, jag får parkera om jag sitter kvar i bilen.";
        val[969] = "Ja, om jag inte hindrar någon buss och jag stannar för att lasta ur eller i någon gods.";
        val[970] = "Ja, om jag inte hindrar någon buss och jag stannar för att låta passagerare stiga ur eller i bilen.";
        val[971] = "Nej, jag får aldrig stanna vid en busshållsplats.";

       frågor[243] = "Du vill parkera när en busshållplats som saknar markering./Inom vilket område är det förbjudet att parkera?";
        val[972] = "Från 12 meter före till 12 meter efter hållplatsmärket.";
        val[973] = "Från 24 meter före till direkt efter hållplatsmärket";
        val[974] = "Från 20 meter före till 5 meter efter hållplatsmärket.";
        val[975] = "Från 10 meter före till 10 meter efter hållplatsmärket.";

        frågor[244] = "Du parkerar din bil den 31 augusti kl 18.30 och planerar att/ åka iväg den 1 september kl 9.00. På vilken sida /av vägen ska du parkera?";
        val[976] = "På den sida som har jämna husnummer.";
        val[977] = "På den sida som har ojämna husnummer.";
        val[978] = "Spelar ingen roll";
        val[979] = "";

        frågor[245] = "Du tänker parkera din bil på kvällen den 28 januari /och hämta den på morgonen den 29 januari./ Får du parkera där vägmärket gäller?";
        val[980] = "Ja, på den sida av gatan som har ojämna husnummer";
        val[981] = "Ja, på den sida av gatan som har jämna husnummer";
        val[982] = "Ja, på båda sidorna av gatan";
        val[983] = "Nej";

        frågor[246] = "Du tänker parkera din bil på kvällen den 15 mars och ska hämta /den på morgonen den 16 mars. Var ska du parkera?";
        val[984] = "På den sidan av gatan som har jämna husnummer.";
        val[985] = "På den sida av gatan som har ojämna husnummer.";
        val[986] = "Mellan ett ojämnt och ett jämnt husnummer";
        val[987] = "";

        frågor[247] = "När är det särskilt viktigt att du kollar döda vinkeln?";
        val[988] = "När jag stannar på rött ljus.";
        val[989] = "När jag accelererar på en motorväg";
        val[990] = "När jag startar från vägkanten och kör";
        val[991] = "";

         frågor[248] = "Du ska svänga till höger i korsningen. /Vilken dold fara måste du räkna med före svängen?";
        val[992] = "Fordon som kommer från vänster i korsningen";
        val[993] = "Fotgängare på övergångstället";
        val[994] = "Fordon som kommer från höger i korsningen";
        val[995] = "Fordon som kommer bakifrån i kollektivfältet.";

        frågor[249] = "I vilken eller vilka av situationerna gäller max /70 km i timmen när du passerat vägmärket med ortsnamnet?";
        val[996] = "Enbart i situation A";
        val[997] = "Enbart i situation B";
        val[998] = "I båda situationerna";
        val[999] = "Ingen utav de";


        frågor[250] = "Du ser ett fordon med ett hängande skylt där bak. Vad betyder skylten?";
        val[1000] = "Det finns en olycka.";
        val[1001] = "Det är en grönsaksbil.";
        val[1002] = "Det är ett långsamgående fordon";
        val[1003] = "Detta är ett släpfordon";

       frågor[251] = "Vilken fordonskombination ska ha denna skylt baktill?";
        val[1004] = "Buss med släpvagn";
        val[1005] = "Lastbil med släpvagn";
        val[1006] = "Personbil med släpvagn";
        val[1007] = "Traktor med släpvagn";

        frågor[252] = "För vilket av följande fordon eller fordonskombinationer är hastigheten begränsad/ till 80 km i timmen på en väg där dessa vägmärken finns uppsatta?";
        val[1008] = "Lätt lastbil";
        val[1009] = "Tung lastbil";
        val[1010] = "Personbil med tillkopplad bromsad släpvagn";
        val[1011] = "Buss med totalvikt över 3,5 ton";

     frågor[253] = "Du kopplar en lätt släpvagn som är försedd med bromsar till /din personbil. Vilken är högsta tillåtna hastighet?";
        val[1012] = "30 km/h";
        val[1013] = "80 km/h";
        val[1014] = "90 km/h";
        val[1015] = "50 km/h";

        frågor[254] = "Vilken högsta tillåtna hastighet gäller för närmast framförvarande fordon?";
        val[1016] = "70 km/h";
        val[1017] = "80 km/h";
        val[1018] = "90 km/h";
        val[1019] = "110 km/h";

        frågor[255] = "Vad menas med situationsanpassad hastighet?";
        val[1020] = "Att köra sakta genom alla korsningar";
        val[1021] = "Att anpassa hastigheten till bakomvarande trafik, även om jag då överträder hastighetsbegränsningen.";
        val[1022] = "Att alltid köra i högsta tillåtna hastighet.";
        val[1023] = "Att köra i den hastighet som trafiksäkerheten kräver,utan att överträda gällande hastighetsbegränsning.";

        frågor[256] = "Vad är oftast orsaken till att en singelolycka inträffar?";
        val[1024] = "Att det är halt väglag.";
        val[1025] = "Det regnar eller snöar kraftigt.";
        val[1026] = "Att föraren inte anpassar körningen efter förhållandena.";
        val[1027] = "Att däcken är slitna.";

        frågor[257] = "Vad är mest förrädiskt i denna situation?";
        val[1028] = "Utfarten på vänster sida från gårdsplanet";
        val[1029] = "Utfarten från höger före ladan";
        val[1030] = "Utfarten från höger efter ladan";
        val[1031] = "";

        frågor[258] = "Risken för fartblindhet är bl.a beroende av miljön. /Vilket av nedanstående trafikmiljö ökar risken?";
        val[1032] = "Vid körnings av tyst bil.";
        val[1033] = "Vid körnings på kvällstid";
        val[1034] = "Vid körnings i tätort";
        val[1035] = "";

        frågor[259] = "Hur påverkas du av fartblindhet?";
        val[1036] = "Jag kör fortare än vad jag är medveten om.";
        val[1037] = "Jag upplever att trafiken i min omgivning kör långsammare än vad jag själv gör.";
        val[1038] = "Jag kör långsammare än vad jag är medveten om.";
        val[1039] = "";

        frågor[260] = "Hur påverkas förmågan att utnyttja periferiseendet om du ökar hastigheten?";
        val[1040] = "Jag får lättare att upptäcka föremål som kommer från sidan";
        val[1041] = "Det påverkar inte";
        val[1042] = "Jag får svårare att upptäcka föremål som kommer från varje sidan";
        val[1043] = "";

        frågor[261] = "Kan risken för tunnelseende påverkas av hastigheten?";
        val[1044] = "Nej, hastigheten har inget samband med tunnelseende.";
        val[1045] = "Nej, tunnelseende beror på sjuklig förändring i ögat.";
        val[1046] = "Ja, hög hastighet ökar risken för tunnelseende";
        val[1047] = "Ja, hög hastighet minskar risken för tunnelseende.";

        frågor[262] = "Du ska svänga vänster ut på vägen. Lastbilen blinkar till höger. /Hur ska du uppträda?";
        val[1048] = "Jag kan köra direkt eftersom lastbilen blinkar till höger";
        val[1049] = "Jag väntar till att lastbilen har genomfört svängen";
        val[1050] = "Jag kör när lastbilen påbörjat svängen eftersom jag då kan vara säker att den inte ska fortsätta rakt fram.";
        val[1051] = "";

        frågor[263] = "Vilket alternativ beskriver bäst hur du ska uppträda när du svängt/ in på en huvudled där högsta tillåtna hastigheten är 90 km i timmen?";
        val[1052] = "Jag är särskilt uppmärksammad bakåt och accelererar.";
        val[1053] = "Jag accelererar försiktigt för att inte skada miljön oavsett hur trafiksituationen ser ut.";
        val[1054] = "Jag använder alltid vägrenen som accelerationsfält.";
        val[1055] = "";

        frågor[264] = "Vilken är den lämpligaste placeringen vid högersväng i korsningen på bilden?";
        val[1056] = "Jag ska placera bilen väl till höger på vägrenen.";
        val[1057] = "Jag ska placera bilen nära mittlinjen.";
        val[1058] = "Jag ska placera bilen väl till höger i körfältet.";
        val[1059] = " Jag ska placera bilen mitt i körfältet.";

        frågor[265] = "Du ska svänga till Hult. Vad ska du göra?";
        val[1060] = "Jag placerar mig intill mittlinjen för att svänga till vänster";
        val[1061] = "Jag placerar mig mitt på körfältet för att kunna svänga vänster eller höger";
        val[1062] = "Jag placerar mig till kantlinjen för att svänga till Hult";
        val[1063] = "";

        frågor[266] = "Du har råkat hamna i det vänstra körfältet. Din avsikt var /egentligen att köra rakt fram. Hur ska jag göra i den här situationen?";
        val[1064] = "Svänga till vänster.";
        val[1065] = "Byta körfält och köra rakt fram.";
        val[1066] = "Fortsätta rakt fram";
        val[1067] = "";

        frågor[267] = "Du tänker köra in på vägen till vänster. Vad är lämpligast i denna situation?";
        val[1068] = "Eftersom sikten är begränsad svänger jag till höger, vänder och kör rakt fram över korsningen";
        val[1069] = "Jag blinkar till vänster i god tid och placerar mig nära mitten för att svänga in på vägen";
        val[1070] = "Jag kör nära högra kanten, stannar och därefter svänger vänster";
        val[1071] = "";

        frågor[268] = "Du ska köra in på vägen till vänster. Vilket alternativ är /det säkraste sättet i den här situationen?";
        val[1072] = "Att jag kör nära höger kanten, stannar där och sen svänga vänster.";
        val[1073] = "Att jag svänger till vänster enligt skylten.";
        val[1074] = "Att jag fortsätter vägen rakt fram, vänder och gör en högersväng";
        val[1075] = "Att jag saktar in, släpper förbi bakomvarande trafik och därefter svänger jag till vänster.";

        frågor[269] = "Du kör på en smal och krokig landsväg, med mycket trafik och/ upptäcker att du måste vända. Vilket är vanligtvis säkraste/ sättet att vända i denna situation?";
        val[1076] = "Att göra en U-sväng på landsvägen.";
        val[1077] = "Att svänga in på en mindre väg och vända där.";
        val[1078] = "Att svänga in på en mindre väg och sedan backa ut på landsvägen.";
        val[1079] = " Att från landsvägen backa in på en korsande väg.";

        frågor[270] = "Du närmar dig en korsning på landsväg och ska svänga vänster. /Vilket mötande fordon är det svårast att bedömda hastigheten på?";
        val[1080] = "Traktor";
        val[1081] = "Motorcykel";
        val[1082] = "Lastbil";
        val[1083] = "Personbil";

        frågor[271] = "Du kör på en landsväg och tänker köra om en lastbil. /Vilket påstående beskriver bäst hur du ska förbereda omkörningen?";
        val[1084] = "Jag placerar mig tätt bakom lastbilen för att minska omkörningssträckan.";
        val[1085] = "Jag placerar mig närmare mittlinjen och håller bra avstånd från lastbilen för att kunna se långt fram.";
        val[1086] = "Jag blinkar med helljuset för att varna mötande trafik.";
        val[1087] = "Jag ger ljudsignal för att få lastbilen att gå ut på vägrenen.";

        frågor[272] = "Vilket påstående om vägrenen är riktigt?";
        val[1088] = "Vägrenen är avsedd för bl.a. oskyddade trafikanter.";
        val[1089] = "Vägrenen är ett körfält.";
        val[1090] = "Samtliga trafikkanter får använda vägrenen men ingen har skyldighet att göra det.";
        val[1091] = "Jag är alltid skyldig att använda vägrenen om jag blir omkörd.";

        frågor[273] = "Hur kan du som nybliven körkortstagare på bästa /sätt undvika att hamna i risksituationer?";
        val[1092] = "Jag kör i intensiv trafik för att träna på svåra situationer.";
        val[1093] = "Jag kör långsamt hela tiden.";
        val[1094] = "Jag har stora säkerhetsmarginaler.";
        val[1095] = "Jag bromsar ofta.";

        frågor[274] = "Vilken är den vanligaste orsaken till påkörning bakifrån?";
        val[1096] = "För kort avstånd mellan fordonen";
        val[1097] = "Onormalt lång reaktionstid hos föraren";
        val[1098] = "Dåliga bromsar";
        val[1099] = "Dåliga däck eller fel typ av däck";

        frågor[275] = "Många farliga situationer uppstår när förare gör omkörning trots/ att det finns mötande trafik. Vad kan orsaken vara?";
        val[1100] = "Svårt att bedöma hur långt bort och hur fort mötande fordon kör.";
        val[1101] = "Svårt att bedöma hur fort framför varande fordon kör.";
        val[1102] = "Svårt att bedöma den egna hastigheten.";
        val[1103] = "";

        frågor[276] = "Du tänker köra om lastbilen. Vad gäller?";
        val[1104] = "Jag måste ge ljudsignal";
        val[1105] = "Lastbilen måste köra ut på vägrenen";
        val[1106] = "Jag måste vänta med att köra om tills det blir fritt från mötande trafik";
        val[1107] = "Mötande måste köra ut på vägren";

        frågor[277] = "Du har börjat köra om lastbilen. Vad ska du göra i den här situationen?";
        val[1108] = "Jag ökar farten för att köra om så fort som möjligt.";
        val[1109] = "Jag avbryter omkörning och kör tillbaka till högra körfältet.";
        val[1110] = "Jag ska blinka med helljuset för att varna mötande trafik.";
        val[1111] = "";

        frågor[278] = "Får du köra om strax före backkrön där sikten är skymd /om vägen har ett körfält i vardera riktning?";
        val[1112] = "Nej";
        val[1113] = "Ja, om fordonet jag vill köra om ger tecken att det inte finns mötande trafik.";
        val[1114] = "Ja, om fordonet jag vill köra om är en traktor med släp.";
        val[1115] = "";

        frågor[279] = "Du tänker köra om bilen framför. När kan du börja omkörningen?";
        val[1116] = "När bilen som jag vill köra om blinkar åt höger";
        val[1117] = "När bilen som jag vill köra om kör ut på vägrenen";
        val[1118] = "När bilen som jag vill köra om sänker hastigheten";
        val[1119] = "När jag själv förvissat mig om att det är klart för omkörningen";

        frågor[280] = "Du tänker köra om lastbilen. Får du köra över mittlinjen /med vänster hjulpar samtidigt som duhar mötande trafik?";
        val[1120] = "Nej";
        val[1121] = "Ja, eftersom lastbilen kör på vägrenen";
        val[1122] = "Ja, eftersom mötande bilar kan köra ut på vägrenen";
        val[1123] = "";

        frågor[281] = "Du kör 90 och detta är högsta tillåtna hastighet på vägen./ Får du andvända vägrenen för att underlätta för omkörningen?";
        val[1124] = "Ja eftersom jag överblickar vägen framför mig.";
        val[1125] = "Nej det är endast tillåtet på vägarna där hastigheten är 70 km/h";
        val[1126] = "Nej eftersom bilarna bakom kör fortare än tillåtet hastighet";
        val[1127] = "";

        frågor[282] = "Du kör i 80 km i timmen och bilen bakom dig vill köra om dig./ Vad bör du göra?";
        val[1128] = "Placera bilen på vägrenen";
        val[1129] = "Öka farten";
        val[1130] = "Placera bilen intill kantlinjen";
        val[1131] = "Bromsa";

        frågor[283] = "Hur ska du uppträda när du blir omkörd?";
        val[1132] = "Jag är skyldig att försöka hindra omkörningen eftersom den som kör bryter mot trafikreglerna";
        val[1133] = "Jag är skyldig att köra ut på vägrenen om jag blir omkörd";
        val[1134] = "Jag är skyldig att underlätta omkörningen även om han bryter mot trafikreglerna";
        val[1135] = "";

        frågor[284] = "Vilken av dessa situationer är det lämpligt att /använda vägrenen för att underlätta en omkörning?";
        val[1136] = "Bild A";
        val[1137] = "Bild B";
        val[1138] = "Bild C";
        val[1139] = "Bild D";

        frågor[285] = "Du ser att en bil kör i körfältet till höger om ditt. /Ni kör med samma hastighet. Vad gäller när körfälten strax går ihop?";
        val[1140] = "Pilarna i mitt körfält betyder att jag har väjningsplikt mot den andra bilen.";
        val[1141] = "Jag har enligt högerregel väjningsplikt mot den andra bilen.";
        val[1142] = "Pilarna i mitt körfält betyder att jag ska köra före den andra bilen.";
        val[1143] = "Vi ska visa varandra hänsyn och anpassa körningen för ett säkert samspel.";

        frågor[286] = "Du kör på vägrenen. Vilken regel gäller om du ska in på körbanan igen?";
        val[1144] = "Jag har väjningsplikt mot fordonen på körbanan.";
        val[1145] = "Fordon på körbanan har väjningsplikt mot mig när jag blinkar vänster.";
        val[1146] = "";
        val[1147] = "";

        frågor[287] = "Vilken regel gäller när en förare ska köra in på körbanan frånvägrenen?";
        val[1148] = "Högerregeln ska tillämpas.";
        val[1149] = "Kugghjulsprincipen ska tillämpas.";
        val[1150] = "Förare på vägrenen har väjningsplikt.";
        val[1151] = "Förare på körbanan har väjningsplikt.";

        frågor[288] = "I vilken av situationerna är omkörning förbuden?";
        val[1152] = "I situation A";
        val[1153] = "I situation B";
        val[1154] = "I situation C";
        val[1155] = "I situation D";

        frågor[289] = "Du kör på motorväg. Vid vilket tillfälle får du inte köra om?";
        val[1156] = "Då en bakomvarande påbörjat en omkörning";
        val[1157] = "Strax före en kurva";
        val[1158] = "Strax före backkrön";
        val[1159] = "";

        frågor[290] = "Du bogserar en bil med bogserlina./ Vilket vägmärke visar vilken väg som du inte får köra in på?";
        val[1160] = "Vägmärke A";
        val[1161] = "Vägmärke B";
        val[1162] = "Vägmärke C";
        val[1163] = "Vägmärke D";

        frågor[291] = "Vilket av följande vägmärken innebär bland annat att det är förbjudet att vända?";
        val[1164] = "Vägmärke A";
        val[1165] = "Vägmärke B";
        val[1166] = "Vägmärke C";
        val[1167] = "Vägmärke D";

        frågor[292] = "Vad är viktigt att veta när du planerar att köra om fordonet på bilden?";
        val[1168] = "Fordonet framför mig är en lastbil utan släpvagn";
        val[1169] = "Fordonet framför mig är en lastbil med släpvagn";
        val[1170] = "Fordonet framför mig är ett långsamt gående fordon";
        val[1171] = "";

       frågor[293] = "Finns det några skillnader mellan en motortrafikled och en motorväg?";
        val[1172] = "Ja, på motortrafikled är det tillåtet at stanna.";
        val[1173] = "Ja, på motortrafikled är det tillåtet att vända.";
        val[1174] = "Ja, på motortrafikled kan mötande trafik förekomma på samma körbana.";
        val[1175] = "Ja, på motortrafikled kan det finnas korsningar på samma plan.";

        frågor[294] = "Du lämnar en motorväg och kommer in på väg som är /försedd med detta vägmärke. Vad innebär det för dig?";
        val[1176] = "Att mötande trafik får förekomma på samma körbana.";
        val[1177] = "Cykel och moped trafik får förekomma.";
        val[1178] = "Korsande trafik får förekomma.";
        val[1179] = "";

        frågor[295] = "Du kör på en motorväg och ser en person som liftar vid/vägkanten. Får du stanna för att ta upp liftaren?";
        val[1180] = "Nej, det är förbjudet att stanna på en motorväg.";
        val[1181] = "Ja, om jag slår på varningsblinkern.";
        val[1182] = "Ja, om hastigheten på motorvägen inte överstiger 90km/h";
        val[1183] = "";

        frågor[296] = "Vilken högsta tillåtna hastighet gäller när du/ passerar detta vägmärke om inget annat anges";
        val[1184] = "50 km/h";
        val[1185] = "70 km/h";
        val[1186] = "90 km/h";
        val[1187] = "110 km/h";

        frågor[297] = "Vilka av samtliga fordon får köra på motorväg?";
        val[1188] = "Tung lastbil, bil och lätt motorcykel";
        val[1189] = "Buss, bil och traktor";
        val[1190] = "Tung motorcykel, bil och traktor";
        val[1191] = "Bil, lastbil och moped";

         frågor[298] = "Du kör på motorväg och närmar dig en anslutning till ett/accelerationsfält som en förare är på väg att lämna. Vad gäller?";
        val[1192] = "Jag behöver inte anpassa hastigheten för att inkörningen underlättas.";
        val[1193] = "Jag måste byta till vänster körfält";
        val[1194] = "Jag måste anpassa hastigheten för att inkörningen underlättas.";
        val[1195] = "";

        frågor[299] = "Du ska från motorvägen bogsera en bil till närmaste avfart. /Får du använda vägrenen?";
        val[1196] = "Ja, men enbart om båda bilarna har varningsblinkers påslagen.";
        val[1197] = "Nej, det är enbart bärgningsbil som får använda vägrenen.";
        val[1198] = "Nej, jag får aldrig köra på vägrenen på motorväg.";
        val[1199] = "Ja, jag måste använda vägrenen.";

       frågor[300] = "Du möter en bil som kör på fel sida av vägen. Hur bör du göra?";
        val[1200] = "Jag styr åt till vänster för att undvika kollision";
        val[1201] = "Jag bromsar och styr till höger";
        val[1202] = "Jag inväntar att den andra bilen byter till sitt rätta körfält.";
        val[1203] = "";

        frågor[301] = "Vad bör du i första hand ha beredskap för i den här situationen?";
        val[1204] = "Möte med omkörande fordon";
        val[1205] = "Gående efter vägkanten";
        val[1206] = "Cyklisten i mitt körfält";
        val[1207] = "";

         frågor[302] = "Vilken högsta tillåtna hastighet gäller för /lastbilen framför dig när den kör på motorväg?";
        val[1208] = "50 km/h";
        val[1209] = "70 km/h";
        val[1210] = "90 km/h";
        val[1211] = "110 km/h";

        frågor[303] = "Du kör på en påfart till en motorväg. Hur skall du uppträda här?";
        val[1212] = "Jag accelererar på påfartssträckan så att jag anpassar hastigheten till motorvägstrafikens hastighet.";
        val[1213] = "Jag väntar med att öka farten tills jag lämnat accelerationsfältet.";
        val[1214] = "Jag minskar farten för att kunna stanna på slutet av accelerationsfältet.";
        val[1215] = "";

        frågor[304] = "Du närmar dig backkrönet på bilden. /Var ska du placera bilen i den här situationen?";
        val[1216] = "Nära vägens mitt för att inte riskera att köra på gående och cyklister på vägens högra sida.";
        val[1217] = "Väl till höger för att ha god marginal i sidled om jag får möte.";
        val[1218] = "Placeringen har ingen betydelse om jag anpassat hastigheten efter situationen?";
        val[1219] = "";

        frågor[305] = "Du kör i en kö och bilen bakom håller för kort avstånd. /Vad är detbästa du kan göra för att öka säkerheten?";
        val[1220] = "Jag ökar farten så att avståndet till bilen bakom mig blir längre.";
        val[1221] = "Jag bromsar kraftigt så att föraren i bilen bakom mig ska öka avståndet till min bil.";
        val[1222] = "Jag lättar på gasen och ökar avståndet till bilen framför mig.";
        val[1223] = "";

        frågor[306] = "Får du i den här situationen köra om fordonet som kör sakta framför dig?";
        val[1224] = "Ja, eftersom det inte kommer mötande trafik";
        val[1225] = "Ja, eftersom fordonet kör långsamt";
        val[1226] = "Ja, eftersom fordonet är försett med LGF-skylt";
        val[1227] = "Nej";

         frågor[307] = "Bilen framför dig kör mycket långsamt. Får du köra om i denna situation?";
        val[1228] = "Ja, eftersom bilen hindrar trafiken genom sitt körsätt";
        val[1229] = "Ja, om jag inte överskrider spärrlinjen med något hjul";
        val[1230] = "Nej";
        val[1231] = "";

        frågor[308] = "Innebär något av vägmärkena att högsta tillåtna /hastigheten är 110 km i timmen om inget annat anges";
        val[1232] = "Ja, men enbart vägmärke A";
        val[1233] = "Ja, men enbart vägmärke B";
        val[1234] = "Ja, i bägge vägmärkena";
        val[1235] = "Nej";

        frågor[309] = "Du kör i 90 km i timmen en solig sommardag. Ungefär hur stort/ avstånd bör du minst hålla till framförvarande fordon?";
        val[1236] = "90 m";
        val[1237] = "50 m";
        val[1238] = "30 m";
        val[1239] = "70 m";

        frågor[310] = "Du kör på motorväg. Får du köra om flera fordon på en följd?";
        val[1240] = "Ja, det är tillåtet.";
        val[1241] = "Nej, bara ett fordon åt gången";
        val[1242] = "Ja, men bara om jag ger ljudsignal.";
        val[1243] = "";

         frågor[311] = "Hur många människor har årligen blivit dödade i trafiken de senaste fem åren?";
        val[1244] = "200-300 personer";
        val[1245] = "500-600 personer";
        val[1246] = "900-1000 personer";
        val[1247] = "1200-1300 personer";

        frågor[312] = "Vilken belysning måste finnas framtill på en personbil?";
        val[1248] = "Helljus, halvljus, parkeringsljus och körriktningsvisare.";
        val[1249] = "Helljus, halvljus, körriktningsvisare och dimljus";
        val[1250] = "Helljus, varselljus, dimljus och körriktningsvisare.";
        val[1251] = "";

        frågor[313] = "Får du köra bil med endast parkeringsljus.";
        val[1252] = "Ja, om du kör på dagen och sikten är bra";
        val[1253] = "Ja, om vägen är dimmig och disig.";
        val[1254] = "Nej.";
        val[1255] = "Ja, om det inte finns några mötande fordon";

        frågor[314] = "Du stannar vid vägkanten och väntar på en passagerare som/dröjer. Det är mörkt och vägen saknar belysning./ Vilket ljus ska du ha tänt i bilen?";
        val[1256] = "Parkeringsljus";
        val[1257] = "Halvljus";
        val[1258] = "Varningsblinkers";
        val[1259] = "Helljus";

        frågor[315] = "När ska parkeringsljus användas under mörker?";
        val[1260] = "Vid körning i tunnel.";
        val[1261] = "Vid körning i tätort.";
        val[1262] = "Vid ofrivilligt stop på vägen för att exempelvis byta hjul.";
        val[1263] = "Vid parkering på en gata med tillfredsställande gatubelysning.";

         frågor[316] = "Vilka belysning får inte användas samtidigt vid färd av personbil?";
        val[1264] = "Helljus och dimljus";
        val[1265] = "Halvljus och dimljus";
        val[1266] = "Halvljus och parkeringsljus";
        val[1267] = "Helljus och parkeringsljus";

        frågor[317] = "En mörk kväll kör du på en landsväg. Du får möte med en annan personbil/ och bländar av till halvljus. När bör du slå påhelljuset igen?";
        val[1268] = "Strax efter mötet";
        val[1269] = "Vid själva mötet";
        val[1270] = "Ca 30 m efter mötet";
        val[1271] = "Ca 30 m före mötet";

        frågor[318] = "Du kör i mörker. I vilket alternativ är det förbjudet att använda helljus?";
        val[1272] = "När jag passerar ett parkerat fordon.";
        val[1273] = "Vid möte med cyklist";
        val[1274] = "Vid möte med gående";
        val[1275] = "";

         frågor[319] = "Vilken belysning får du använda när du kör i dimma under dagtid?";
        val[1276] = "Parkeringsljus";
        val[1277] = "Dimljus tillsammans med halvljus";
        val[1278] = "Dimljus tillsammans med parkeringsljus";
        val[1279] = "";

        frågor[320] = "Genom en kontrollbesiktning upptäcker du att halvljuset/ har gått sönder och du ska köra till en verkstad under/ dagen och vädret är bra. Vilket ljus ska du använda?";
        val[1280] = " Dimljus";
        val[1281] = "Parkeringsljus";
        val[1282] = "Helljus";
        val[1283] = "";

        frågor[321] = "I vilken situation får du inte ha dimbakljuset tänt?";
        val[1284] = "Vid lätt duggregn i mörker";
        val[1285] = "Vid dimma i dagsljus";
        val[1286] = "Vid dimma i mörker";
        val[1287] = "Vid kraftig snörök i dagsljuset";

        frågor[322] = "I vilken av dessa situationer kan du vänta längst med/ att blända av till halvljus före ett möte i mörker?";
        val[1288] = "I situation A";
        val[1289] = "I situation B";
        val[1290] = "I situation C";
        val[1291] = "I situation D";

        frågor[323] = "På bilden ovan ser du dimma (snö) över vägen. Vilken ljus bör du använda?";
        val[1292] = "Parkeringsljus tillsammans med helljus";
        val[1293] = "Parkeringsljus tillsammans med halvljus";
        val[1294] = "Parkeringsljus tillsammans med varselljus";
        val[1295] = "Dimljus tillsammans med halvljus";

         frågor[324] = "Det är mörkt och dimmigt. Vilket ljus får du inte använda?";
        val[1296] = "Helljus.";
        val[1297] = "Halvljus";
        val[1298] = "Dimljus";
        val[1299] = "";

        frågor[325] = "Hur reagerar bilen om du lastar den tungt i bagageluckan?";
        val[1300] = "Om bilen är överstyrd kan den bli understyrd";
        val[1301] = "Om bilen är understyrd kan den bil normalstyrd";
        val[1302] = "Om bilen är normalstyrd kan den bli överstyrd";
        val[1303] = "";

          frågor[326] = "När du styr i en kurva känner du att bilen svänger mer än/ vad som motsvarar av rattens vridning. Vad kan orsaken vara?";
        val[1304] = "Bilen är baktung";
        val[1305] = "Bilen är framtung";
        val[1306] = "Bilens styrservo fungerar inte";
        val[1307] = "Bilens framdäck är i betydligt sämre skick än bakdäcken";

        frågor[327] = "Vad innebär att en personbil är understyrd?";
        val[1308] = "Personbilen vill svänga mer än vad föraren tänkt sig.";
        val[1309] = "Personbilen svänger lika mycket som rattutslag.";
        val[1310] = "Personbilen vill fortsätta rakt fram i kurvor.";
        val[1311] = "";

         frågor[328] = "Vad ska du göra om du råkar ut för vattenplaning?";
        val[1312] = "Bromsa hårt och styra ut mot högerkanten.";
        val[1313] = "Släppa gasen så att farten sjunker och undvik stora rattrörelser.";
        val[1314] = "Öka farten snabb och styra över åt det håll där det är minst vatten.";
        val[1315] = "Bromsa hårt och håll ratten stilla.";

        frågor[329] = "Hur ska du göra för att undvika vattenplaning?";
        val[1316] = "Köra med tillräckligt breda däck";
        val[1317] = "Köra med däck som har minst 3.0 mm mönsterdjup";
        val[1318] = "Köra med tillräckligt låg hastighet.";
        val[1319] = "";

        frågor[330] = "Du kör i 70 km i timmen på en landsväg. Höger hjulpar har /hamnat utanför asfalten. Hur bör du göra?";
        val[1320] = "Jag släpper gasen och styr försiktigt upp på körbanan.";
        val[1321] = "Jag bromsar mjukt och vrider kraftigt på ratten för att snabbt komma upp på körbanan.";
        val[1322] = "Jag bromsar hårt och styr försiktigt upp på körbanan.";
        val[1323] = "Jag behåller hastigheten och styr försiktigt upp på körbanan.";

        frågor[331] = "Du kör i halt vinterväg. /Vid vilket tillfälle kan du räkna med att det är extra halt?";
        val[1324] = "Då du kör över backkrön.";
        val[1325] = "Då du kör genom en kurva.";
        val[1326] = "Då du ökar farten kraftigt.";
        val[1327] = "";

        frågor[332] = "Du kör en bil utan ABS bromsar. /Hur bör du göra om du får sladd i halt väglag?";
        val[1328] = "Jag frikopplar och styr mot vägens riktning";
        val[1329] = "Jag bromsar hårt och styr mot vägen riktning";
        val[1330] = "Jag bromsar hårt och styr mot vägen riktning";
        val[1331] = "Jag frikopplar och bromsar hårt";

         frågor[333] = "Om du ska byta ut alla fyra hjul. Var ska du lägga de två bästa?";
        val[1332] = "På främre axlar";
        val[1333] = "Valfritt";
        val[1334] = "På bakre axlar";
        val[1335] = "";

        frågor[334] = "Du har börjat svänga in på en lokalväg. Hur bör du göra i det här väglaget?";
        val[1336] = "Jag bromsar hårt samtidigt som jag snabbt förflyttar mig åt höger för att hamna på rätt körfält";
        val[1337] = "Jag styr snabbt ut på vägren för att inte fastna i snödrivor";
        val[1338] = "Jag sänker hastigheten och styr försiktigt åt höger för att inte få sladd";
        val[1339] = "";

        frågor[335] = "Du har fel på bilen på en landsväg. Var ska du sätta/ varningstriangeln för att varna andra trafikanter?";
        val[1340] = "Över bilens tak så att detta syns bra";
        val[1341] = "5-10 meter före bilen";
        val[1342] = "Så att trafikanterna varnas i god tid";
        val[1343] = "Direkt bakom bilen";

        frågor[336] = "Du har blivit tvungen att stanna på en väg eller vägren./ I vilken situation måste du ställa ut en godkänd varningstriangel?";
        val[1344] = "Där den tillåtna hastigheten är 50km/h";
        val[1345] = "Där den tillåtna hastigheten är mer än 50km/h";
        val[1346] = "Där den tillåtna hastigheten är 30km/h";
        val[1347] = "";

         frågor[337] = "En trafikolycka inträffar när en bil som kör om dig krockar/ med mötande fordon. Måste du stanna på olycksplatsen?";
        val[1348] = "Ja, men endast om mitt fordon blir skadat.";
        val[1349] = "Ja, alltid";
        val[1350] = "Ja, men endast om jag avser att vittna om olyckan.";
        val[1351] = "Nej";

        frågor[338] = "Du har som förare blivit inblandad i en trafikolycka./ Måste du uppge namn och adress om någon annan/ inbladad i samma olycka frågar efter det.";
        val[1352] = "Ja, men endast om mitt fordon blir skadat.";
        val[1353] = "Ja, alltid";
        val[1354] = "Ja, men endast om jag avser att vittna om olyckan.";
        val[1355] = "Nej";

        frågor[339] = "För vilket trafikbrott återkallas normalt körkortet?";
        val[1356] = "Vid smitning från trafikolycka.";
        val[1357] = "Vid enstaka felparkering.";
        val[1258] = "När en förare kört över spärrlinje.";
        val[1359] = "";

        frågor[340] = "Du har kommit till en trafikolycka./ Hur ska du hjälpa en person som är medvetslös och slutat andas?";
        val[1360] = "Jag ger en konstgjord andning, lägger personens ben högt samt lägger en filt på personen";
        val[1361] = "Jag ger konstgjord andning och när personen andas lägger jag personen på ryggen";
        val[1362] = "Jag ser till att personen har fri luftväg,ger konstgjord andning samt lägger personen i sidoläge ";
        val[1363] = "";

        frågor[341] = "Du har kommit till en trafikolycka där en man som är inblandad/ i trafikolyckan är chockad. Hur hjälper du honom bäst?";
        val[1364] = "Jag lägger honom med huvudet lågt, benen högt, talar lugnt och lägger varma kläder på honom.";
        val[1365] = "Jag låter honom arbeta på olycksplatsen så att han får annat att tänka på.";
        val[1366] = "Jag sätter honom bortvänd från olycksplatsen.";
        val[1367] = "";

         frågor[342] = "Du kör bil och är trött. Kan du bli straffad om polisen stoppar dig?";
        val[1368] = "Ja, alltid";
        val[1369] = "Nej";
        val[1370] = "Ja, om det händer en olycka på grund av trötthet ";
        val[1371] = "";

        frågor[343] = "Vid vilken tid på dygnet är det farligast att passera ett viltstråk?";
        val[1372] = "Vid gryning och skymning då djuren förflyttar sig mest.";
        val[1373] = "Nattetid eftersom djuren söker efter föda då.";
        val[1374] = "Mitt på dagen eftersom djuren är mest är aktiva då.";
        val[1375] = "";

        frågor[344] = "Hur kan du bl.a. märka att börjar bli trött under en körning?";
        val[1376] = "Jag känner mig frusen.";
        val[1377] = "Jag känner mig hungrig.";
        val[1378] = "Jag känner mig törstig.";
        val[1379] = "Jag känner mig varm.";

         frågor[345] = "Du har kört på och skadat ett rådjur så /att det blir liggande på vägen. Vad måste du göra?";
        val[1380] = "Kontakta polisen men enbart om en människa har skadats";
        val[1381] = "Kontakta ditt försäkringsbolag";
        val[1382] = "Låta djuret ligga kvar tills polis eller markägare kommer till platsen";
        val[1383] = "Varna andra trafikanten och om möjligt flytta djuret";

        frågor[346] = "Var ska du placera din bil när du i mörker möter ett annat fordon?";
        val[1384] = "Intill kantlinjen";
        val[1385] = "Intill mittlinjen";
        val[1386] = "På vägrenen";
        val[1387] = "Mitt i körfältet";

        frågor[347] = "Du möter en bil när du kör på en landsväg. Det är mörkt och vägen/ saknar belysning.Vart ska du rikta blicken för att/ inte bli bländad och för att göra mötet säkert?";
        val[1388] = "Långt fram utmed höger vägkant";
        val[1389] = "I backspegeln";
        val[1390] = "Till vänster om strålkastarna på den mötande bilen";
        val[1391] = "Mitt emellan strålkastarna på den mötande bilen";

        frågor[348] = "Bilar blir allt säkrare och utrustas med allt fler/ säkerhetssystem t.ex. ABS-bromsar och antisladdsystem. /Kan det på något sätt vara negativt för säkerheten?";
        val[1392] = "Nej, alla säkerhetssystem är enbart positiva";
        val[1393] = "Ja, det kan medföra att förarna tar större risker";
        val[1394] = "Ja, bilen blir svårare att manövrera";
        val[1395] = "Nej, säkerhetssystem gör att förarna kan koncentrera sig mer på trafiken.";

        frågor[349] = "Hur kan jag som förare minska risken för att trafikolyckor inträffar?";
        val[1396] = "Vara utvilad när du kör.";
        val[1397] = "Jag ser till att bilen är i bra skick.";
        val[1398] = "Jag väljer en väg där vägegenskaperna är bra.";
        val[1399] = "Jag ser till att samtliga passagerare använder bilbälte.";

         frågor[350] = "Det är en solig vinterdag. Var är det mest halt väglag?";
        val[1400] = "På en skuggad väg";
        val[1401] = "På ett backrön";
        val[1402] = "I en kurva.";
        val[1403] = "";

        frågor[351] = "Du lånar en bakhjulsdriven bil istället för den framhjulsdrivna/ som du är van att köra. Vilken skillnad är viktig att/ känna till när du ska köra i halt väglag?";
        val[1404] = "En bakhjulsdriven bil sladdar oftast med bakvagnen";
        val[1405] = "En bakhjulsdriven bil sladdar oftast med framvagn";
        val[1406] = "En bakhjulsdriven bil har oftast bättre väggrepp i motlut.";
        val[1407] = "";

        frågor[352] = "Vilken av dessa djur måste du ringa polisen om du har kört över?";
        val[1408] = "Rådjur";
        val[1409] = "Hare";
        val[1410] = "Grävling";
        val[1411] = "Räv";

        frågor[353] = "Du har kommit ifatt den vita bilen och vill köra om. Hur ska du göra?";
        val[1412] = "Jag lägger mig tätt bakom den vita bilen så att föraren upptäcker mig och tvingas köra ut på vägrenen";
        val[1413] = "Jag planerar omkörningen så att jag kan köra om utan att den vita bilen behöver köra ut på vägrenen";
        val[1414] = "Jag blinkar med helljuset för att få bilen att köra ut på vägrenen, vilket han är skyldig att göra.";
        val[1415] = "";

        frågor[354] = "Du kör en bil utrustad med ABS-bromsar. Vilket alternativ beskriver/ bäst hur du ska agera för att få kortast möjliga bromssträcka?";
        val[1416] = "Jag bromsar hårt, frikopplar och styr i vägens riktning";
        val[1417] = "Jag bromsar först hårt, frikopplar och lättar sedan trycket på bromsen för att undvika att hjulen låser sig";
        val[1418] = "Jag bromsar mjukt, frikopplar och styr i vägens riktning";
        val[1419] = "Jag bromsar först mjukt, frikopplar och ökar sedan trycket på bromsen för att undvika att hjulen låser sig";

        frågor[355] = "En person har fått andningstillstånd och saknar puls./ Du måste göra s.k. hjärt- och lungräddning. Hur ska du göra?";
        val[1420] = "Jag ska göra lika många inblåsningar som bröstkompressioner";
        val[1421] = "Jag ska göra dubbelt så många inblåsningar som bröstkompressioner";
        val[1422] = "Jag ska göra enbart bröstkompressioner";
        val[1423] = "Jag ska göra många fler bröstkompressioner än inblåsningar";

        frågor[356] = "Vad kan hända om du lastar släpvagnen/ så att det medför ett för lågt kultryck";
        val[1424] = "Släpvagnen kan börja åla";
        val[1425] = "Slitage på bilens bakdäck kan öka.";
        val[1426] = "Släpvagnens bromsar fungerar sämre";
        val[1427] = "Bilens halvljus kan bli bländande";

        frågor[357] = "Du har krockat och en person har blivit svårt skadad/ vid olyckan. Din bil har blivit stående så att den/ hindrar andra trafikanter. Vad bör du göra?";
        val[1428] = "Jag får aldrig flytta bilen när någon har blivit svårt skadat.";
        val[1429] = "Jag bör flytta bilen om det är möjligt, men enbart om den utgör en fara för andra trafikanter.";
        val[1430] = "Jag bör flytta bilen om det är möjligt, även om den inte utgör en fara för andra trafikanter.";
        val[1431] = "";

        frågor[358] = "Vilket ljus skall du ha tänt i den här situationen vid körning i mörker?";
        val[1432] = "Parkeringsljus";
        val[1433] = "Halvljus";
        val[1434] = "Helljus";
        val[1435] = "Varsellljus";

        frågor[359] = "Du kör en bil som är full lastad. När du styr i en kurva/ känner du att bilen svänger mer än vad som motsvarar av /rattens vridning. Vad kan du göra att åt detta problem?";
        val[1436] = "Jag fördelar lasten så att bilen blir lättare där bak";
        val[1437] = "Jag byter ut framhjulen till bättre hjul.";
        val[1438] = "Jag ökar lufttrycket i framhjulen";
        val[1439] = "";

        frågor[360] = "Vilket påstående är rätt angående trafikolyckor?";
        val[1440] = "De flesta dödsolyckor inträffar inom tätbebyggt område, eftersom det är tät och snabb trafik där.";
        val[1441] = "De flesta dödsolyckor inträffar hos förare som är över 65 år eftersom de har sämre reaktionsförmåga.";
        val[1442] = "De flesta dödsolyckor inträffar utanför tätbebyggt område, eftersom hastigheten är hög där.";
        val[1443] = "";

        frågor[361] = "Hur långt före faran är detta vägmärke uppsatt?";
        val[1444] = "50-150 m";
        val[1445] = "150-250 m";
        val[1446] = "250-350 m";
        val[1447] = "1-2 km";

        frågor[362] = "Vad betyder följande vägmärke?";
        val[1448] = "Dålig väggrepp och slirig körbana";
        val[1449] = "Risk för sladd och längre bromssträcka";
        val[1450] = "Gropig väg och risk för stenskott.";
        val[1451] = "";

        frågor[363] = "Vilket vägmärke anger att högerregel gäller?";
        val[1452] = "Vägmärke A";
        val[1453] = "Vägmärke B";
        val[1454] = "Vägmärke C";
        val[1455] = "Vägmärke D";

        frågor[364] = "Anger något av märkena att du närmar dig en plats där högerregel gäller?";
        val[1456] = "Vägmärke A";
        val[1457] = "Vägmärke B";
        val[1458] = "Vägmärke C";
        val[1459] = "Nej";

        frågor[365] = "Du kör på huvudled. /Vilken vägmärke anger att det kan finnas komplicerade korsning?";
        val[1460] = "Vägmärke A";
        val[1461] = "Vägmärke B";
        val[1462] = "Vägmärke C";
        val[1463] = "Vägmärke D";

        frågor[366] = "Du kör på huvudled. /Vilket av vägmärkena varnar för en farlig korsning?";
        val[1464] = "Vägmärke A";
        val[1465] = "Vägmärke B";
        val[1466] = "Vägmärke C";
        val[1467] = "Vägmärke D";

        frågor[367] = "Vad innebär den inringade vägmärkeskombinationen?";
        val[1468] = "Det finns en järnvägskorsning med bommar ca 150-250 meter längre fram.";
        val[1469] = "Det finns en järnvägskorsning utan bommar ca 50 meter längre fram";
        val[1470] = "Det finns en järnvägskorsning med bommar ca 50 meter längre fram";
        val[1471] = "Det finns en järnvägskorsning utan bommar ca 150-250 meter längre fram.";

        frågor[368] = "Vad innebär vägmärkeskombinationen?";
        val[1472] = "400 m efter vägmärkena kommer det en farlig vänsterkurva och därefter kurvig väg i 2,0 km";
        val[1473] = "Strax efter vägmärkena kommer det en farlig högerkurva";
        val[1474] = "400 m efter vägmärkena kommer det en farlig vänsterkurva och därefter kurvig väg i 1,6 km";
        val[1475] = "400 m efter vägmärkena kommer det en farlig högerkurva och därefter kurvig väg i 2,0 km";

        frågor[369] = "Du kommer till detta vägmärke en varm sommardag. /Varför bör du sänka hastigheten?";
        val[1476] = "Vägsträckan kan vara hal på grund av asfalten.";
        val[1477] = "Vägsträckan är särskild olycksdrabbad.";
        val[1478] = "Vägsträckan har en eller flera tvära kurvor efter vägmärket.";
        val[1479] = "";

        frågor[370] = "Vid vilken skylt skall du ha lägsta hastighet?";
        val[1480] = "Vid skylt A";
        val[1481] = "Vid skylt B";
        val[1482] = "Vid skylt C";
        val[1483] = "Ingen";

        frågor[371] = "Vad anger vägmärket?";
        val[1484] = "Korsande järnväg med bommar.";
        val[1485] = "Korsande järnväg med ett spår.";
        val[1486] = "Korsande järnväg med flera spår.";
        val[1487] = "Korsande järnväg utan bommar.";

        frågor[372] = "Vad ska du göra då du kommer fram till följande vägmärke?";
        val[1488] = "Du ska lämna de gående företräde genom att sänka hastigheten i god tid eller genom att stanna.";
        val[1489] = "Du bör ej lämna företräde då gående ej börjat korsa vägen (står stilla vid kanten).";
        val[1490] = "Ingenting";
        val[1491] = "";

        frågor[373] = "Du kör på landsväg. Vilket av vägmärkena förbereder/ dig på att det kommer ett övergångsställe längre fram?";
        val[1492] = "Vägmärke A";
        val[1493] = "Vägmärke B";
        val[1494] = "Vägmärke C";
        val[1495] = "Vägmärke D";

        frågor[374] = "Du kör på en enkelriktad gata och ser detta vägmärke längre fram. /Vad innebär märket?";
        val[1496] = "Vägen smalnar strax av";
        val[1497] = "Jag kan strax få mötande trafik";
        val[1498] = "Jag ska lämna företräde åt mötande fordon";
        val[1499] = "Varning för korsande trafik";

        frågor[375] = "Vilket av följande vägmärken är ett varningsmärke?";
        val[1500] = "Vägmärke A";
        val[1501] = "Vägmärke B";
        val[1502] = "Vägmärke C";
        val[1503] = "Vägmärke D";

        frågor[376] = "Vilken av de här märkerna är förbudsmärke?";
        val[1504] = "Vägmärke A";
        val[1505] = "Vägmärke B";
        val[1506] = "Vägmärke C";
        val[1507] = "Vägmärke D";

        frågor[377] = "Vilken information ger vägmärket om gatan rakt fram?";
        val[1508] = "Återvändsgata";
        val[1509] = "Förbud att stanna och parkera";
        val[1510] = "Förbud mot fordonstrafik";
        val[1511] = "Förbud mot infart mot fordon.";

        frågor[378] = "Vilket påstående om vägmärkerna är riktigt?";
        val[1512] = "Vägmärke A innebär att vägen är fri från fordonstrafik.";
        val[1513] = "Vägmärke B innebär att infart med motordrivna fordon är förbjuden , men att det är tillåtet att cykla på vägen.";
        val[1514] = "Vägmärke B innebär att vägen är enkelriktad.";
        val[1515] = "Vägmärke A innebär att vägen är enkelriktad";

        frågor[379] = "Vad innebär vägmärket?";
        val[1516] = "Endast cyklister och förare av moped klass 2 får passera vägmärket.";
        val[1517] = "All fordonstrafik är förbjuden på vägen.";
        val[1518] = "Samtliga fordon får passera märket de backas in på vägen.";
        val[1519] = "Vägen är enkelriktad.";

        frågor[380] = "Vilka fordon får inte köra in på den väg där detta märke är uppsatt.";
        val[1520] = "Endast personbilar";
        val[1521] = "Alla motordrivna fordon";
        val[1522] = "Motordrivet fordon som har flera hjul än två";
        val[1523] = "";

        frågor[381] = "Vad innebär vägmärket?";
        val[1524] = "Förbud mot omkörning av motordrivna fordon med två hjul.";
        val[1525] = "Förbud mot trafik med större fordon än personbilen.";
        val[1526] = "Förbud mot omkörning av personbilen.";
        val[1527] = "Förbud mot trafik med motordrivna fordon med flera än två hjul.";

        frågor[382] = "Vilket av följande vägmärken upphör att gälla vid nästa korsning?";
        val[1528] = "Vägmärke A";
        val[1529] = "Vägmärke B";
        val[1530] = "Vägmärke C";
        val[1531] = "";

        frågor[383] = "Du kör en bil med tillkopplad enaxlad släpvagn. /Vilket vägmärke får du inte köra förbi?";
        val[1532] = "Vägmärke A";
        val[1533] = "Vägmärke B";
        val[1534] = "Vägmärke C";
        val[1435] = "Vägmärke D";

        frågor[384] = "Vad innebär det inringade vägmärket?";
        val[1536] = "Trafiken på bron är enkelriktad";
        val[1537] = "Endast ett fordon i taget får vistas på bron";
        val[1538] = "Mötande trafik ska lämna mig företräde om utrymmet inte räcker till för möte";
        val[1539] = "Jag ska lämna företräde till mötande trafik om utrymmet inte räcker till för möte";

        frågor[385] = "Vilket vägmärke anger att du är skyldig att/ lämna företräde för den mötande trafiken?";
        val[1540] = "Vägmärke A";
        val[1541] = "Vägmärke B";
        val[1542] = "Vägmärke C";
        val[1543] = "Vägmärke D";

        frågor[386] = "Vad innebär vägmärket?";
        val[1544] = "Förbud att köra om alla motordrivna fordon.";
        val[1545] = "Förbud att köra om motordrivna fordon med fler hjul än två.";
        val[1546] = "Enbart förbud att köra om personbilar.";
        val[1547] = "";

        frågor[387] = "Hur långt gäller det här förbudsmärket?";
        val[1548] = "Så länge heldragen linje finns";
        val[1549] = "150 – 250 meter";
        val[1550] = "Tills det kommer ett slutmärke";
        val[1551] = "Tills nästa korsning";

        frågor[388] = "Du kommer strax ikapp fordonet på bilden. Får du köra om den?";
        val[1552] = "Ja, eftersom fordonet framför är på vägrenen";
        val[1553] = "Ja, om jag inte med något hjul passerar spärr linjen";
        val[1554] = "Nej";
        val[1555] = "Ja, eftersom fordonet framför är ett långsamt gående fordon.";

        frågor[389] = "Vilket av dessa vägmärken kräver ett/ slutmärke för att förbjudet inte längre ska gälla?";
        val[1556] = "Vägmärke A";
        val[1557] = "Vägmärke B";
        val[1558] = "Vägmärke C";
        val[1559] = "Vägmärke D";

        frågor[390] = "Vilket av vägmärkena innebär att du måste svänga /till vänster i korsningen?";
        val[1560] = "Vägmärke A";
        val[1561] = "Vägmärke B";
        val[1562] = "Vägmärke C";
        val[1563] = "";

        frågor[391] = "Vilka av dessa vägmärken innebär att du inte får fortsätta rakt fram?";
        val[1564] = "Vägmärke A";
        val[1565] = "Vägmärke B";
        val[1566] = "Vägmärke C";
        val[1567] = "Vägmärke D";

        frågor[392] = "Vilket vägmärke innebär att mötande fordon ska lämna dig företräde?";
        val[1568] = "Vägmärke A";
        val[1569] = "Vägmärke B";
        val[1570] = "Vägmärke C";
        val[1571] = "Vägmärke D";

       frågor[393] = "Vad betyder det här vägmärket";
        val[1572] = "Vägvisare till enskild väg och bara de som har tillstånd får köra här.";
        val[1573] = "Vägvisare till enskild väg som saknar vägmärken";
        val[1574] = "Vägvisare till tättbebyggt område.";
        val[1575] = "";

        frågor[394] = "Du kommer till en vägkorsning där detta vägmärke finns uppsatt./ Vad betyder märket?";
        val[1576] = "Det är en allmän väg";
        val[1577] = "Det är en tillfällig trafikomläggning på grund av vägarbete";
        val[1578] = "Jag måste svänga vänster.";
        val[1579] = "Det är en enskild väg";

        frågor[395] = "Vad innebär vägmärket?";
        val[1580] = "Jag kör på en väg som tillfälligt ersätter riksväg 58";
        val[1581] = "Jag kör på en väg som leder till riksväg 58.";
        val[1582] = "Jag kör på riksväg 58.";
        val[1583] = "";

        frågor[396] = "Vad innebär följande vägmärke?";
        val[1584] = "Vägvisare till enskild väg";
        val[1585] = "Vägvisare till allmän väg";
        val[1586] = "Vägvisare till lokal väg";
        val[1587] = "";

        frågor[397] = "Vad innebär det här vägmärket?";
        val[1588] = "Det är olämpligt att köra fortare än 30km/h";
        val[1589] = "Det är förbjudet att köra fortare än 30km/h";
        val[1590] = "Det är påbjudet att köra i 30km/h";
        val[1591] = "";

         frågor[398] = "Vilka veckodagar gäller den anvisade hastighetsbegränsningen?";
        val[1592] = "Lördagar";
        val[1593] = "Söndagar";
        val[1594] = "Vardagar utom dag före söndag och helgdag.";
        val[1595] = "Alla dagar.";

        frågor[399] = "Vad varnar den inringade skylten för?";
        val[1596] = "Vägarbete.";
        val[1597] = "Svag vägkant.";
        val[1598] = "Backkrön.";
        val[1599] = "Fast hinder";

        frågor[400] = "På väg mot Nyköping så upptäcker du att vägen är avspärrad/ på grund av vägarbete. Vilken skylt är uppsatt för/ att visa förbifart till Nyköping?";
        val[1600] = "Skylt 1";
        val[1601] = "Skylt 2";
        val[1602] = "Skylt 3";
        val[1603] = "Skylt 4";

        frågor[401] = "Du kommer med din bil fram till en plats där det /både finns vita och orangea linjer på körbanan. /Vad innebär detta för dig?";
        val[1604] = "Jag ska följa de vita linjerna";
        val[1605] = "Jag ska följa de orangea linjerna.";
        val[1606] = "Jag kan välja att följa antingen de vita eller de orangea linjerna";
        val[1607] = "";

         frågor[402] = "Vilket av följande påståenden är riktigt?";
        val[1608] = "Om jag fortsätter i det högra körfältet så måste jag svänga till höger mot LÄNNAN";
        val[1609] = "Eftersom vägmarkeringar saknas är vägen inte uppdelad i körfält.";
        val[1610] = "Om jag placerar mig i det vänstra körfältet så måste jag svänga till vänster mot NYNÄSHAMN";
        val[1611] = "Om jag ska fortsätta mot E4 så kan jag stanna kvar i det högra körfältet.";

        frågor[403] = "Vilken väg är hastigheten lägst?";
        val[1612] = "Vid väg A";
        val[1613] = "Vid väg B";
        val[1614] = "Vid väg C";
        val[1615] = "";

        frågor[404] = "Vad innebär vägmarkeringen som finns rakt framför dig i höger körfält?";
        val[1616] = "Vägen smalnar av längre fram";
        val[1617] = "Jag närmar mig en korsning där jag har väjningsplikt";
        val[1618] = "Vägen är huvudled även efter nästa korsning.";
        val[1619] = "Vägen får snart två körfält i min färdriktning.";

        frågor[405] = "Du ser följande vägmarkering intill vägens kant./ I vilken situation är det förbjudet att stanna och parkera?";
        val[1620] = "I situationen på bild A";
        val[1621] = "I situationen på bild B";
        val[1622] = "I situationen på bild C";
        val[1623] = "";

        frågor[406] = "Vad innebär vägmärket?";
        val[1624] = "Den som kör i vänster körfält ska som regel väja för trafiken i höger körfält";
        val[1625] = "Förarna i båda körfälten ska ömsesidigt anpassa sig till att det blir ett körfält";
        val[1626] = "Den som kör i höger körfält ska som regel väja för trafiken i vänster körfält";
        val[1627] = "";

         frågor[407] = "Kan det här vägmärket få någon särskild/ betydelse när det är halt väglag?";
        val[1628] = "Ja, där vinden är kraftig blir vägbanan sträv med bra väggrepp.";
        val[1629] = "Nej, det innebär endast farlig sidvind från vänster";
        val[1630] = "Ja, i halt väglag kan plötslig sidvind göra att jag får sladd";
        val[1631] = "";

        frågor[408] = "Får du svänga vänster i korsningen?";
        val[1632] = "Nej jag måste köra rakt fram";
        val[1633] = "Ja, det är tillåtet";
        val[1634] = "Nej, jag måste svänga till höger";
        val[1635] = "";

        frågor[409] = "Vilken av dessa skyltar skall du vara försiktig/ för att inte glida av vägen på halt väglaget.";
        val[1636] = "Skylt A";
        val[1637] = "Skylt B";
        val[1638] = "Skylt C";
        val[1639] = "Skylt D";

        frågor[410] = "Vem bör stanna på mötesplatsen om utrymmet inte räcker till för att mötas";
        val[1640] = "Den som kommer först till mötesplatsen";
        val[1641] = "Den som har skylten på sin sida av vägen";
        val[1642] = "";
        val[1643] = "";

         frågor[411] = "Är omkörningen tillåten i någon av situationerna på bilderna?";
        val[1644] = "Ja, i situation A";
        val[1645] = "Ja, i situation B";
        val[1646] = "Ja, i situation C";
        val[1647] = "Ja, i situation D";

        frågor[412] = "Får du köra lätt lastbil med tillkopplad/ släpvagn där detta vägmärke gäller?";
        val[1648] = "Ja, men enbart om både bilen och släpvagnen är olastade";
        val[1649] = "Ja, vägmärket anger minsta tillåtna avstånd mellan motordrivna fordon";
        val[1650] = "Nej, vägmärket anger att det inte är tillåtet med släpvagn";
        val[1651] = "Nej, vägen är enbart avsedd för personbilar";

        frågor[413] = "Vilka tre fordon får du köra om med detta vägmärke?";
        val[1652] = "Moped med två hjul, cykel motorcykel med två hjul";
        val[1653] = "Personbil, Lätt motorcykel, Moped med två hjul";
        val[1654] = "Långsam gående fordon, Cykel, Motorcykel med sidovagn";
        val[1655] = "";

        frågor[414] = "Denna skylt kan finnas i en tunnel. Vad innebär den?";
        val[1656] = "Den visar väg till brandsläckaren";
        val[1657] = "Den visar väg till nödtelefon";
        val[1658] = "Den visar väg till utrymningsplats";
        val[1659] = "Den visar väg till gående";

        frågor[415] = "Vilket av följande påståenden är riktigt?";
        val[1660] = "Om jag ska fortsätta mot väg 73 så kan jag stanna kvar i det högra körfältet.";
        val[1661] = "Om jag ska fortsätta mot väg 73 så måste jag flytta till ett av de vänstra körfälten";
        val[1662] = "Om jag ska fortsätta mot LÄNNA så måste jag placera bilen till höger på vägrenen";
        val[1663] = "";

         frågor[416] = "Vilka veckodagar sker flest dödsolyckor i/ trafiken där de omkomna är yngre än 30 år?";
        val[1664] = "Onsdagar och torsdagar";
        val[1665] = "Fredagar, lördagar och söndagar";
        val[1666] = "Måndagar och tisdagar";
        val[1667] = "";

        frågor[417] = "Hur lång sträcka kör du i 3 sekunder när hastigheten är 110 km i timmen";
        val[1668] = "90 meter.";
        val[1669] = "60 meter.";
        val[1670] = "130 meter.";
        val[1671] = "150 meter.";

        frågor[418] = "Olika körsätt innebär att körningen påverkar miljön olika mycket./ Körsättet varierar också med kombinationen av förare och passagerare./ Med vilken av dessa kombinationer är det troligt att körsättet påverkar/ miljön mest negativt?";
        val[1672] = "Både föraren och passagerarna är unga kvinnor.";
        val[1673] = "Föraren är en medelålders man och passagerarna är kvinnor.";
        val[1674] = "Föraren är en medelålders kvinna och passagerarna är män.";
        val[1675] = "Både föraren och passagerarna är unga män";

         frågor[419] = "Får en ambulans med sirener och blåljus påslagna passera/ en trafiksignal som lyser rött?";
        val[1676] = "Ja";
        val[1677] = "Nej";
        val[1678] = "Ba vah?";
        val[1679] = "";

        frågor[420] = "Varför bör du ur miljösynpunkt inte tvätta bilen/ på en asfalterad garageuppfart som lutar ut mot gatan?";
        val[1680] = "Gatubrunnar blir tilltäppta av feta oljerester från biltvätten.";
        val[1681] = "Bilschampot skadar den kemiska processen i reningsverket när vattnet i gatubrunnarna kommer dit.";
        val[1682] = "Smutsvatten och oljerester rinner ut i gatubrunnarna och vidare ut i vattendragen.";
        val[1683] = "Asfalten löses upp av tvättmedlet";

        frågor[421] = "Vilken av de här förarna löper störst risk att dödas i trafiken?";
        val[1684] = "En 18 årig kvinna";
        val[1685] = "En 30 årig kvinna";
        val[1686] = "En 45 årig kvinna";
        val[1687] = "En 60 årig kvinna";

        frågor[422] = "När du för första gången får körkort har du en prövotid. Hur lång är den?";
        val[1688] = "1 år";
        val[1689] = "2 år";
        val[1690] = "3 år";
        val[1691] = "5 år";

        frågor[423] = "Vad innebär linjen mitt på vägen?";
        val[1692] = "Den markerar körbanans mitt";
        val[1693] = "Den anger att högsta tillåtna hastighet är 70km/h";
        val[1694] = "Den förvarnar om spärrlinjen längre fram";
        val[1695] = "Det är förbjudet att överskrida linjen";

         frågor[424] = "Du ska svänga vänster och kör på en enkelriktad väg. /Vilket körfält ska du ta?";
        val[1696] = "Vänstra körfältet";
        val[1697] = "Högra körfältet";
        val[1698] = "Du kan välja antigen högra eller vänstra körfältet";
        val[1699] = "";

        frågor[425] = "Vilken av dessa personer kan lättast /påverkas av grupptryck vid bilkörning?";
        val[1700] = "Den som har bra självförtroende";
        val[1701] = "Den som har lång erfarenhet av bilkörning";
        val[1702] = "Den som har dåligt självförtroende";
        val[1703] = "";

          frågor[426] = "Du tänker köra om cyklisten. Hur ska du göra?";
        val[1704] = "Jag väntar efter backkrönet med att köra om";
        val[1705] = "Jag kör om direkt om jag kan göra det utan att överträda mittlinjen.";
        val[1706] = " Jag kör om direkt men ser till att lämna tillräckligt vingelutrymme för cyklisten";
        val[1707] = "Jag tutar för att uppmärksamma cyklisten på att jag tänker köra om";

        frågor[427] = "Vad ska du kontrollera för trafiksäkerheten /efter du har tvättat din bil på biltvätt?";
        val[1708] = "Elsystemet.";
        val[1709] = "Bromsarna.";
        val[1710] = "Servosystemet.";
        val[1711] = "Kylarvätskan.";

         frågor[428] = "I vilken av de här situationerna får du inte/ stanna för att släppa av en passagerare?";
        val[1712] = "Bild A";
        val[1713] = "Bild B";
        val[1714] = "Bild C";
        val[1715] = "Ingen av de";

        frågor[429] = "Hur kan ditt avstånd till framförvarande /fordon öka trafiksäkerheten?";
        val[1716] = "Ett kort avstånd ökar säkerheten när jag ska göra en omkörning";
        val[1717] = "Ett långt avstånd underlättar omkörning för trafik bakom mig";
        val[1718] = "Ett kort avstånd hindrar farliga omkörningar";
        val[1719] = "";

        frågor[430] = "Du ska till kiosken som ligger 2 km längre bort./ Hur ska du ta dig dit om du ska tänka på miljön?";
        val[1720] = "Jag tar min bil som har haft motorvärmare";
        val[1721] = "Jag cyklar";
        val[1722] = "Jag tar min eldrivna moped";
        val[1723] = "Jag åker med min granne som också ska dit";

        frågor[431] = "Vad är riktigt om katalysatorn i en bensindriven bil?";
        val[1724] = "Den renar inte avgaserna från koldioxid";
        val[1725] = "Den renar avgaserna effektivast när motorn är kall";
        val[1726] = "Den renar avgaserna från alla skadliga ämnen";
        val[1727] = "Den gör att bränslet används effektivare.";

        frågor[432] = "Många gående skadas på övergångställe i tätort under mörker./ Vad kan orsaken vara?";
        val[1728] = "Gående har svårt att upptäcka bilar under mörker";
        val[1729] = "Förare har svårt att bedöma avstånd under mörker";
        val[1730] = "Förarna tror att de ser alla gående och är därför inte uppmärksamma.";
        val[1731] = "";

         frågor[433] = "Du kommer till ett obevakat övergångställe där en/ grupp dagisbarn just skall gå över. Hur ska du uppträda?";
        val[1732] = "Om det finns en lucka mellan barnen, så får jag passera med stor försiktighet.";
        val[1733] = "Jag har väjningsplikt och stannar och väntar tills alla barn passerat.";
        val[1734] = " Jag stannar och vinkar fram barnen.";
        val[1735] = "";

        frågor[434] = "Du ska köra in till en cirkulationsplats, vad gäller?";
        val[1736] = "Jag måste alltid stanna på en cirkulationsplats";
        val[1737] = "Fordonen i cirkulationsplatsen har väjningsplikt till mig";
        val[1738] = "Jag har väjningsplikt till fordonen i cirkulationsplatsen";
        val[1739] = "";

        frågor[435] = "Grupptrycket kan påverka hur du uppträder som förare./ Är det bra eller dåligt?";
        val[1740] = "Det är alltid dåligt med grupptryck";
        val[1741] = "Det är alltid bra med grupptryck";
        val[1742] = "Gruppens värderingar avgör om det är bra eller dåligt med grupptryck";
        val[1743] = "";

        frågor[436] = "För att minska risken att få en pisksnärtsskada (whiplash)/ måste du ha huvudstödet (nackskyddet) rätt inställt./ Vad krävs dessutom?";
        val[1744] = "Att bilen är utrustad med krockkudde.";
        val[1745] = "Att bilen har ABS-bromsar";
        val[1746] = "Att jag använder bilbälte på rätt sätt.";
        val[1747] = "Att bilen har antisladdsystem.";

         frågor[437] = "Kan man få körkortet återkallat om man upprepade/ gånger omhändertas av polisen för fylleri, /även om man inte kört bil i berusat tillstånd?";
        val[1748] = "Ja, körkortet kan återkallas";
        val[1749] = "Nej, körkortet kan enbart återkallas om man kör bil i berusat tillstånd.";
        val[1750] = "";
        val[1751] = "";

        frågor[438] = "Många unga förare omkommer i trafiken på helgnätter/ under sommaren. Vilken är den vanligaste orsaken?";
        val[1752] = "Att de har druckit alkohol.";
        val[1753] = "Att de omkommer i en omkörningsolycka .";
        val[1754] = "Att de råkar ut för en viltolycka.";
        val[1755] = "Att de är trötta.";

        frågor[439] = "Vilket påstående är sant om katalysatorn i en bensindriven bil?";
        val[1756] = "Katalysatorn renar avgaserna effektivast när motorn är kall.";
        val[1757] = "Katalysatorns höga arbetstemperatur gör att lättantändliga föremål i närheten kan börja brinna";
        val[1758] = "Katalysatorn gör att bränslet används effektivare.";
        val[1759] = "Katalysatorn kan lätt överhettas om motorns tomgångsvarvtal är felinställt";

        frågor[440] = "Kamraterna till en ung man visar tydligt att de inte accepterar att/ han kör bil när han är onykter. Den unge mannen påverkas av deras åsikter/ och kör därför aldrig mer bil när han är berusad. Vad kallas detta?";
        val[1760] = "Imitationsinlärning";
        val[1761] = "Bortträngning";
        val[1762] = "Sannolikhetsinlärning";
        val[1763] = "Grupptryck";

        frågor[441] = "Var kan denna skylt finnas och vilken information ger den.";
        val[1764] = "Skylten kan finnas vid vägarbeten och anger alternativ färdväg för vissa tunga fordon förbi vägarbetet";
        val[1765] = "Skylten placeras på tunga fordon och anger fordonets vikter";
        val[1766] = "Skylten placeras på fordon lastade med farligt gods och anger lastens innehåll";
        val[1767] = "Skylten kan sitta som tilläggstavla, anger rekommenderad färdväg för fordon lastade med farligt gods";

         frågor[442] = "Du stannar vid en järnvägkorsning där bommarna är på väg ner./ Tåget som ska passera är ett mycket långt godståg./ Hur bör du göra med tanke på miljön?";
        val[1768] = "Stänga av motorn";
        val[1769] = "Ha motorn på tomgång och ettans växel ilagd";
        val[1770] = "Ha kopplingen i dragläge för att snabbt komma igång när tåget passerat.";
        val[1771] = "Ha motorn på tomgång, växeln i friläge och parkeringsbromsen åtdragen";

        frågor[443] = "Hur stor andel av de förare som har dödats i /singelolyckor har varit alkoholpåverkade?";
        val[1772] = "Ca en fjärdedel";
        val[1773] = "Ca en tredjedel";
        val[1774] = "Ca två tredjedelar";
        val[1775] = "Ca hälften";

        frågor[444] = "Är det någon skillnad mellan män och kvinnor när det/ gäller risken att få nackskador av pisksnärtstyp/ (whiplash) vid en trafikolycka?";
        val[1776] = "Ja, män löper större risk att få nackskador.";
        val[1777] = "Nej, det är ingen skillnad";
        val[1778] = "Ja, kvinnor löper större risk att få nackskador.";
        val[1779] = "";

         frågor[445] = "Vem är det som till sist avgör om du ska köra bil/ eller inte, när du använder ett läkemedel?";
        val[1780] = "Apotekspersonal som lämnat ut läkemedlet.";
        val[1781] = "Jag själv som använder läkemedlet.";
        val[1782] = "Läkaren som skrivit ut läkemedlet.";
        val[1783] = "Vägverket";

        frågor[446] = "Alkohol kan påverka synen även om alkoholhalten i blodet/ är mindre än 0.2 promille. Vad innebär detta för föraren?";
        val[1784] = "Känsligare för bländning";
        val[1785] = "Synfältet ökar";
        val[1786] = "Mörkerseendet förbättras";
        val[1787] = "Synskärpan ökar";

        frågor[447] = "Hur lång bör en person minst vara för att sitta på/ en plats i framsätet där det finns krockkudde (airbag)";
        val[1788] = "100 cm";
        val[1789] = "120 cm";
        val[1790] = "140 cm";
        val[1791] = "160 cm";

        frågor[448] = "Hur ska bilbältet sitta för att göra mest nytta?";
        val[1792] = "Bältet ska sitta löst mot kroppen";
        val[1793] = " På axeln ska bältet ligga så långt från halsen som möjligt";
        val[1794] = "Bältet ska vara väl åtdraget mot kroppen";
        val[1795] = "Jag ska helst ha en vadderad jacka mellan mig och bältet";

        frågor[449] = "Hur kan du med ditt vägval minska påverkan på miljön?";
        val[1796] = "Jag väljer en väg där jag ofta måste köra med låga växlar.";
        val[1797] = "Jag väljer en väg där jag får stanna ofta så att motorn får gå på mycket tomgång.";
        val[1798] = "Jag väljer en väg där jag måste växla mycket";
        val[1799] = "Jag väljer en väg där jag kan hålla jämn fart och där jag behöver stanna få gånger.";

         frågor[450] = "Du kör bil på en väg med heldragen kantlinje. Du upptäcker att/ bilen bakom försöker köra om dig på en plats där det är förbjudet/ att köra om. Vad ska du göra för att minska faran?";
        val[1800] = "Jag placerar bilen nära kantlinjen för att underlätta omkörningen.";
        val[1801] = "Jag placera bilen nära mittlinjen för att visa att omkörningen inte är lämplig.";
        val[1802] = "Jag ökar farten för att förhindra omkörningen.";
        val[1803] = "";


        frågor[451] = "Får du svänga till höger när trafikljuset ser ut som på bilden?";
        val[1804] = "Får du svänga till höger när trafikljuset ser ut som på bilden?";
        val[1805] = "Nej, du måste vänta på att extrasignalen lyser grönt.";
        val[1806] = "Ja, jag får svänga utan att stanna eftersom huvudsignalen lyser grönt.";
        val[1807] = "";

        frågor[452] = "Vad gäller vid körning på motorväg?";
        val[1808] = "Det är tillåtet att köra moped klass I";
        val[1809] = "Det är inte tillåtet att köra med lägre hastighet än 40km/h.";
        val[1810] = "Den högsta tillåtna hastighet på motorväg är 110km/h om inget annat anges.";
        val[1811] = "";

        frågor[453] = "Vilken insats från oss förare skulle öka trafiksäkerheten mest?";
        val[1812] = "Om alla körde nya bilar";
        val[1813] = "Om alla anpassade hastigheten till varje situation";
        val[1814] = "Om alla gjorde säkerhetskontroll före varje körning.";
        val[1815] = "Om alla fixerade blicken långt fram vid körning";

         frågor[454] = "Vad ska du fylla på i bilbatteriet om nivån/ är för låg i någon av cellerna?";
        val[1816] = "Svavelsyra.";
        val[1817] = "Destillerat vatten.";
        val[1818] = "T-sprit";
        val[1819] = "Glykol.";

        frågor[455] = "Vad betyder vägmärket?";
        val[1820] = "Vägvisning till en riksväg";
        val[1821] = "Vägnummermärke vid omledning av trafiken";
        val[1822] = "Nummer på trafikplatsen";
        val[1823] = "";

        frågor[456] = "Varför är det stor risk att älgar finns /nära vägarna under sommarmånaderna maj-juni?";
        val[1824] = "Älgjakten börjar";
        val[1825] = "Förra årets älgkalvar stöts bort av älgkorna";
        val[1826] = "Tillgången på mat ökar";
        val[1827] = "Värmen gör att älgarna söker sig till öppna ytor";

        frågor[457] = "Hur ska du göra när du kör om ett /väghållningsfordon som används vid ett vägarbete?";
        val[1828] = "Jag ska alltid köra om till vänster";
        val[1829] = "Jag ska alltid köra om till höger";
        val[1830] = "Jag får köra om antigen till vänster eller till höger beroende på situationen";
        val[1831] = "";

        frågor[458] = "Vad innebär den siffra som anges på en personbils kontrollmärke?";
        val[1832] = "Den månad som bilen ska besiktigas";
        val[1833] = "Den månad som bilens trafikförsäkring ska betalas";
        val[1834] = "Den månad som bilens fordonsskatt ska betalas";
        val[1835] = "";

        frågor[459] = "Hur kan du minska bränsleförbrukningen på din bil?";
        val[1836] = "Jag kör med lågt lufttryck i däcken";
        val[1837] = "Jag blandar karburatorsprit i bensinen vintertid";
        val[1838] = "Jag stänger av luftkonditioneringen när den inte behövs";
        val[1839] = "";

        frågor[460] = "Finns det ett samband mellan innebörden av /detta vägmärke och miljöpåverkan?";
        val[1840] = "Ja, här kan jag tanka biobränsle";
        val[1841] = "Ja, här finns särskilt anordnade parkeringsplatser i anslutning till kollektivtrafik";
        val[1842] = "Ja, här finns parkeringsplatser med anslutningar till motorvärmare";
        val[1843] = "Nej, vägmärket betyder parkeringsplats enbart för taxibilar";

        frågor[461] = "Vilket är det mest miljövänliga transportmedlet/ när du ska åka 30 mil?";
        val[1844] = "Flyg";
        val[1845] = "Motorcykel";
        val[1846] = "Personbil";
        val[1847] = "Tåg";

        frågor[462] = "En person har köpt en ny personbil som uppfyller kraven/ för miljöklass El. Vilken kostnad slipper personen de/ fem första åren genom detta val av bil?";
        val[1848] = "Kostnad för fordonsskatt";
        val[1849] = "Kostnad för kontrollbesiktning";
        val[1850] = "Kostnad för trafikförsäkring";
        val[1851] = "Kostnad för helförsäkring";

        frågor[463] = "Du kör på en motorväg och ser en person som liftar/ vid vägkanten. Får du stanna för att ta upp liftaren?";
        val[1852] = "Ja, om jag slår på varningsblinkern";
        val[1853] = "Nej, det är förbjudet att stanna på en motorväg";
        val[1854] = "Ja, om hastigheten på motorvägen inte överstiger 90 km/h";
        val[1855] = "";

        frågor[464] = "Är bilen parkerad rätt i någon av situationerna?";
        val[1856] = "Nej";
        val[1857] = "Ja, men enbart i situation B";
        val[1858] = "Ja i båda situationerna";
        val[1859] = "Ja, men enbart i situation A";

        frågor[465] = "Vilken typ av fordon har du framför dig?";
        val[1860] = "Ett fordon lastat med farligt gods";
        val[1861] = "En lastbil med totalvikt över 3,5 ton med tillkopplad släpvagn";
        val[1862] = "En lastbil med totalvikt över 3,5 ton utan tillkopplad släpvagn";
        val[1863] = "Ett långsamtgående fordon ";

        frågor[466] = "Vad betyder det när den här kontrollampan tänds under körning?";
        val[1864] = "De låsningsfria bromsarna är inkopplade";
        val[1865] = "Det är något fel på de låsningsfria bromsarna";
        val[1866] = "Bromsvätskenivån i de låsningsfria bromsarna är för låg";
        val[1867] = "";

        frågor[467] = "Varför är det viktigt att du som bilförare har bra självkontroll?";
        val[1868] = "Jag kan förutse andras misstag och därmed undvika olyckor.";
        val[1869] = "Jag kan reagera snabbt med bromsen när det är nödvändigt.";
        val[1870] = "Jag kan bevara mitt lugn även i stressiga trafiksituationer.";
        val[1871] = "";

        frågor[468] = "Du har hamnat på avfarten av misstag i stället för/ att köra rakt fram till nästa avfart. Hur ska du göra?";
        val[1872] = "Fortsätta på avfarten.";
        val[1873] = "Öka farten och köra över spärrområdet för att komma tillbaka till körfältet rakt fram.";
        val[1874] = "Backa för att komma tillbaka till körfältet rakt fram.";
        val[1875] = "Bromsa kraftigt och försiktigt köra tillbaka till körfältet rakt fram.";

        frågor[469] = "Vilket är det lämpligaste sättet att vända i korsningen på bilden?";
        val[1876] = "Jag svänger vänster i korsningen, stannar och backar rakt genom korsningen.";
        val[1877] = "Jag svänger vänster i korsningen, stannar och backar rund hörn A.";
        val[1878] = "Jag svänger höger i korsningen, stannar och backar rakt genom korsningen.";
        val[1879] = "Jag kör rakt fram i korsningen, stannar och backar runt hörn B.";

        frågor[470] = "Du vill parkera där vägmärkena gäller./ Klockan är 19.00 och bilen ska stå parkerad/ två timmar. Vad gäller?";
        val[1880] = "Jag får parkera här utan att betala avgift.";
        val[1881] = "Jag får parkera här men jag ska betala avgift.";
        val[1882] = "Jag får inte parkera här.";
        val[1883] = "";

        frågor[471] = "Personbilen på bilden är av 1996 års modell./ När får den automatiskt körförbud om ägaren inte/ betalar fordonskatt i tid?";
        val[1884] = "1 februari.";
        val[1885] = "1 mars.";
        val[1886] = "1 oktober";
        val[1887] = "1 december";

       frågor[472] = "En del personer söker efter äventyr och spänning i livet./ Hur brukar sådana personer bete sig när de kör bil?";
        val[1888] = "De samspelar tydligt med sina medtrafikanter.";
        val[1889] = "De följer alltid trafikreglerna korrekt.";
        val[1890] = "De planerar sin körning för att undvika kraftiga inbromsningar.";
        val[1891] = "De tar större risker och visar mindre hänsyn än andra.";

        frågor[473] = "Hur bör du köra för att minska bilens avgasutsläpp?";
        val[1892] = "Jag undviker onödiga stopp genom att planera körningen";
        val[1893] = "Jag använder motorbromsning så lite som möjligt";
        val[1894] = "Jag kör med kort avstånd till framförvarande";
        val[1895] = "Jag accelererar snabbt och med full gas efter varje växling";

        frågor[474] = "Bilar ska besiktas inom en viss period. Inställelsemånaden/ bestäms av sista siffran i registreringsnumret./ Hur lång är besiktningsperioden?";
        val[1896] = "Inställningsmånaden + 2 månader före + 2 månader efter";
        val[1897] = "Enbart inställelsemånaden";
        val[1898] = "Inställelsemånaden + 2 månader före";
        val[1899] = "Inställelsemånaden + 1 månad före + 1 månad efter";

        frågor[475] = "Du kommer som första person fram till en trafikolycka./ Vad är det första du bör göra?";
        val[1900] = "Varna andra";
        val[1901] = "Överblicka situationen";
        val[1902] = "Larma 112";
        val[1903] = "Ge första hjälpen åt skadade";

        frågor[476] = "Signalen visar blinkande gult sken. Vad innebär det?";
        val[1904] = "Signalen förstärker varningen för spårvagn";
        val[1905] = "Signalen gäller endast för spårvagnsföraren";
        val[1906] = "Jag måste stanna innan jag passerar korsningen";
        val[1907] = "Jag får inte passera förrän signalen släcks.";

        frågor[477] = "Vilken är den vanligaste påföljden för den som /kör bil och har något narkotiskt ämne i blodet?";
        val[1908] = "Prövotiden på körkortet förlängs med ett år";
        val[1909] = "Böter eller fängelse och återkallat körkort";
        val[1910] = "Återkallat körkort i högst en månad";
        val[1911] = "Varning i körkortsregistret";

        frågor[478] = "Du ska på landsväg svänga till vänster i en fyrvägskorsning./ Vilka fordon är det störst risk att du krockar med?";
        val[1912] = "Mötande fordon";
        val[1913] = "Fordon från vänster";
        val[1914] = "Fordon bakifrån";
        val[1915] = "Fordon från höger";

        frågor[479] = "Du kör i 50 km i timmen./ Hur många meter rullar ditt fordon på en sekund?";
        val[1916] = "Ca 5 m";
        val[1917] = "Ca 9 m";
        val[1918] = "Ca 14 m";
        val[1919] = "Ca 24 m";

        frågor[480] = "Nästan hälften av alla bilresor i Sverige är kortare/ än 5 kilometer. Påverkar resans längd mängden /avgasutsläpp per kilometer?";
        val[1920] = "Nej, resans längd gör ingen skillnad på mängden avgasutsläpp per kilometer";
        val[1921] = "Ja, en kort bilresa medför större mängd avgasutsläpp per kilometer än en lång bilresa";
        val[1922] = "Ja, en lång bilresa medför större mängd avgasutsläpp per kilometer än en kort bilresa";
        val[1923] = "";

        frågor[481] = "Vilken av de här förarna visar exempel på säkert samspel i trafiken?";
        val[1924] = "En förare som saktar in för att visa att han har väjningsplikt";
        val[1925] = "En förare som alltid håller på sin rätt enligt reglerna";
        val[1926] = "En förare som överträder hastighetsgränsen för att följa trafikrytmen";
        val[1927] = "";

        frågor[482] = "Vilken av dessa åtgärder är den bästa om du/ vill minska utsläppen av koldioxid från din bil?";
        val[1928] = "Jag kör på lägsta möjliga växel";
        val[1929] = "Jag kör på högsta möjliga växel";
        val[1930] = "Jag kör med lågoktanig bensin";
        val[1931] = "Jag kör med högoktanig bensin";

        frågor[483] = "En person säger om sig själv att han aldrig gör/ misstag i trafiken. Vilket begrepp anger den förare/ som beskriver sig själv på detta sätt?";
        val[1932] = "Mogen";
        val[1933] = "Impulsiv";
        val[1934] = "Defensiv";
        val[1935] = "Omogen";

        frågor[484] = "Vilken tid på dygnet sker flest dödsolyckor?";
        val[1936] = "00.00 – 02.00";
        val[1937] = "06.00 – 08.00";
        val[1938] = "11.00 – 14.00";
        val[1939] = "18.00 – 20.00";

        frågor[485] = "Hur länge bör du använda motorvärmare innan/ du startar bilen när temperaturen är 0o C?";
        val[1940] = "Ca 1 timme";
        val[1941] = "Ca 3 timmar";
        val[1942] = "Ca 5 timmar";
        val[1943] = "Ca 10 timmar";

        frågor[486] = "Du ska köra från Malmö till Göteborg med din bil av/ årsmodell 2004. Det är mars månad och väglaget är/ som på bilden. Vilka däck får du ha på bilen?";
        val[1944] = "Dubbdäck med 2 mm mönsterdjup";
        val[1945] = "Sommardäck med 6 mm mönsterdjup";
        val[1946] = "Odubbade vinterdäck med 4 mm mönsterdjup";
        val[1947] = "";

        frågor[487] = "Var går minsta gränsen för rattfylleri om/ polisen skall straffa dig?";
        val[1948] = "0.2 promille";
        val[1949] = "0.5 promille";
        val[1950] = "Oavsett hur mycket alkohol du har i blodet, polisen kan straffa dig om du kör på ett riskabelt sätt.";
        val[1951] = "";

        frågor[488] = "Vid vilken situation gäller hastigheten 110km i timmen?";
        val[1952] = "I situation på bild A";
        val[1953] = "I situation på bild B";
        val[1954] = "I båda situationerna";
        val[1955] = "";

        frågor[489] = "Hur påverkas körningen om du pratar i mobiltelefon när du kör bil?";
        val[1956] = "Reaktionstiden blir kortare";
        val[1957] = "Jag gör mindre och lugnare rattrörelser";
        val[1958] = "Jag har sämre uppsikt bakåt i sidospeglarna";
        val[1959] = "Stoppsträckan blir kortare";

        frågor[490] = "Hur påverkas körningen när du pratar i mobiltelefonen?";
        val[1960] = "Reaktionstiden blir längre";
        val[1961] = "Stoppsträckan blir kortare";
        val[1962] = "Föraren har bättre uppsikt i backspegeln";
        val[1963] = "Rattrörelserna blir mer stabila";

        frågor[491] = "Vilket påstående om reaktionstiden är riktig?";
        val[1964] = "Den blir längre när föraren måste välja mellan olika alternativ.";
        val[1965] = "Den blir kortare i låga hastigheter";
        val[1966] = "Den blir längre i halt väglag";
        val[1967] = "";

        frågor[492] = "Du hör och ser en ambulans under utryckning/ som närmar sig bakifrån. Vad ska du göra?";
        val[1968] = "Jag måste alltid köra in till kanten och stanna.";
        val[1969] = "Jag måste lämna fri väg, genom att minska hastigheten, köra åt sidan eller stanna om det behövs.";
        val[1970] = "Jag ska stanna omedelbart.";
        val[1971] = "Jag ska köra på som vanligt för att inte förvirra föraren i utryckningsfordonet.";

       frågor[493] = "Bilderna visar baksidan på olika vägmärken./ Vilket vägmärke anger stopplikt vid vägkorsning?";
        val[1972] = "Vägmärke A";
        val[1973] = "Vägmärke B";
        val[1974] = "Vägmärke C";
        val[1975] = "Vägmärke D";

        frågor[494] = "Partiklar i luften är ett allvarligt miljöproblem i tätorterna./ Partiklarna kommer bland annat från vägbanan och frigörs/ när bildäck nöter på vägen. Vilken typ av däck /sliter loss mest partiklar?";
        val[1976] = "Högfartsdäck";
        val[1977] = "Friktionsdäck";
        val[1978] = "Dubbdäck";
        val[1979] = "Sommardäck";

        frågor[495] = "Du ska köra rakt fram. /Gäller högerregeln i någon av situationerna?";
        val[1980] = "Ja, men enbart i situation A";
        val[1981] = "Ja, men enbart i situation B";
        val[1982] = "Ja i båda situationerna";
        val[1983] = "Nej";

        frågor[496] = "Vad måste du ha med dig när du kör personbil?";
        val[1984] = "Bilens registreringsbevis";
        val[1985] = "Körkortet";
        val[1986] = "Försäkringshandlingar för bilen";
        val[1987] = "";

        frågor[497] = "Vilka får trafikera körfält A där vägmärket gäller?";
        val[1988] = "Fordon i linjetrafik och cyklister";
        val[1989] = "Bussar och tunga lastbilar";
        val[1990] = "Endast bussar";
        val[1991] = "Endast fordon i linjetrafik";

         frågor[498] = "På de här vägarna är hastigheten begränsad till 90 km i timmen./ På vilken väg är det mest motiverat att hålla lägre hastighet därför att/ vilda djur kan springa ut på vägen?";
        val[1992] = "Vägen på bild A";
        val[1993] = "Vägen på bild B";
        val[1994] = "Vägen på bild C";
        val[1995] = "Vägen på bild D";

        frågor[499] = "Vad kännetecknar förare som kör impulsivt?";
        val[1996] = "De har god handlingsberedskap";
        val[1997] = "De kör hindrande långsamt";
        val[1998] = "De planerar sin körning";
        val[1999] = "De handlar först och tänker sedan";













    }
    public void RadioInit()
    {
        svar1 = new Checkbox(val[i*4], svarGroup, false);
        svar2 = new Checkbox(val[(i*4)+1], svarGroup, false);
        svar3 = new Checkbox(val[(i*4)+2], svarGroup, false);
        svar4 = new Checkbox(val[(i*4)+3], svarGroup, false);
        svar1.setBounds(20,560,600,30);
        svar2.setBounds(20,590,600,30);
        svar3.setBounds(20,620,600,30);
        svar4.setBounds(20,650,600,30);
        add(svar1);
        add(svar2);
        add(svar3);
        add(svar4);
    }
    public void RadioNull()
    {
        remove(svar1);
        remove(svar2);
        remove(svar3);
        remove(svar4);
    }
    public void ButtonInit()
    {
        if(!test)
        {
            next = new Button("Nästa");
            back = new Button("Föregående");
            prov = new Button("Prov");
            övning = new Button("Frågor");
            omprov = new Button("Omprov");
            next.setBounds(500,450,80,30);
            back.setBounds(500,490,80,30);
            prov.setBounds(230,400,80,30);
            övning.setBounds(330,400,80,30);
            omprov.setBounds(50,300,80,30);
            add(prov);
            add(övning);
            prov.addActionListener(this);
            övning.addActionListener(this);
        }



    }
    public void PicInit()
    {
        bilbg = new Image[500];
        for(int a = 0; a < 500; a++)
        {
            bilbg[a] = getImage(base, a + ".gif");
        }
    }
    public void SlumpProv()
    {
            for(int n = 0; n < 65; n++)
            {
               int a = (int)(500*Math.random());
               for(int b = 0; b < n+1; b++)
               {
                    if(a == frågprov[b])
                        slump = true;
               }
               if(!slump)
                   frågprov[n] = a;
               else
               {
                   n = n-1;
                   slump = false;
               }

            }

    }
    public void RadByte()
    {
        jämförstring = '/';
        b = 0;
        nyrad = false;
        for(int a = 0; a < frågor[i].length(); a++)
        {
            if(jämförstring == frågor[i].charAt(a))
            {
               nyrad = true;
               positionsave[b+1] = a;
               if(b>=1)
                   nyradString[b] = frågor[i].substring(positionsave[b]+1,a);
               else
                   nyradString[b] = frågor[i].substring(0,a);
               b++;
            }
        }
        if(b>=1)
        {
            nyradString[b] = frågor[i].substring(positionsave[b]+1,frågor[i].length());
        }
    }
    public void variabler()
    {
        facit = new int[500];
        svar = new int[500];
        frågprov = new int[65];
    }
    public void FacitInit()
    {
        //Här skriver du det

        for(int n = 0; n < 500; n++)
        {
            facit[n] = 1;
        }

    }

}

