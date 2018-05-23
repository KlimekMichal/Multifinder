import java.lang.Math;
import java.lang.String;
import java.util.Scanner;
public class LeagueMatchingAlgorithm {


    public static void main(String[] args) {
        double wynik;
        boolean czymoz;
        double pktrnk;
        double rolepoint;
        double allpoints;
        Scanner s = new Scanner(System.in);
        LeagueMatchingAlgorithm kek = new LeagueMatchingAlgorithm();
        wynik=kek.agePoints(21,21);
        pktrnk=kek.rankPoints("Diamond 1", "Diamond 1");
        rolepoint=kek.rolePoints("Bot", "Support", "Fill", "Fill");
        allpoints=kek.SumOfPoints(21,21,"Diamond 1", "Diamond 1","Bot", "Support", "Fill", "Fill");
        System.out.println("wynik wiek: "+ wynik);
        System.out.println("wynik rank: "+ pktrnk);
        System.out.println("wynik pozyc: " + rolepoint);
        System.out.println("Wynik: " + allpoints);
        s.close();
    }
    //Ogólnie to wiek ma mniejsze znaczenie kiedy ma się 20+ lat oboje jeżeli któryś ma poniżej to jest kara do różnicy wieku//
    private double agePoints(int ageA, int ageB){
        double mult=0,points;
        if(ageA>=20 && ageB>=20) {
            mult=1;
        } else { mult=3; }
        int dif = Math.abs(ageA-ageB);
        points=20-(dif*mult);
        if(points<0)
            return 0;
        else
            return points;
    }
    //Słaby gracz nie może grać z dobrym graczem bo prowadzi to do nierównej gry dla przeciwników i ich samych + sama gra nie pozwala na wspólną grę rankingową graczy których różnica rang jest zbyt duża//
    //Tutaj są uproszczone restrykcje wprowadzone bezpośrednio w grze
    private boolean isDuoPossible(String rankA, String rankB) {
        if(rankA.contains("Bronze")) {
            if (rankB.contains("Bronze") || rankB.contains("Silver"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Silver")) {
            if (rankB.contains("Bronze") || rankB.contains("Silver") || rankB.contains("Gold"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Gold")) {
            if (rankB.contains("Platinum") || rankB.contains("Silver") || rankB.contains("Gold"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Platinum")) {
            if (rankB.contains("Platinum") || rankB.contains("Diamond") || rankB.contains("Gold"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Diamond")) {
            if (rankB.contains("Platinum") || rankB.contains("Diamond") || rankB.contains("Master"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Master")) {
            if (rankB.contains("Diamond") || rankB.contains("Master") || rankB.contains("Challenger"))
                return true;
            else
                return false;
        }
        if(rankA.contains("Challenger")) {
            if (rankB.contains("Master") || rankB.contains("Challenger"))
                return true;
            else
                return false;
        }
        return false;
    }
    // Dla ułatwienia liczenia różnicy pomiędzy rankami zamieniam je na double
    private double turnRankInPoints(String rank) {
        if (rank.contains("Bronze")) {
            return 0;
        }
        if (rank.contains("Silver")) {
            return 10;
        }
        if (rank.contains("Gold")) {
            return 20;
        }
        if (rank.contains("Platinum")) {
            return 30;
        }
        if (rank.contains("Diamond")) {
            return 40;
        }
        if (rank.contains("Master")) {
            return 48;
        }
        if (rank.contains("Challanger")) {
            return 53;
        }
        return -10;
    }
    //Jak już się okaże że mogą ze sobą grać to i tak pozostaje różnica rangi np.
    // Gracz z rangą Bronze 5 jest dużo gorszym graczem niż Silver 1, mimo że gra dopuszcza granie we dwóch w takiej kombinacji,
    // to bez wspólnego startu gra nigdy nie dopuści by tych dwóch graczy znalazło się w tym samym meczu
    private double rankPoints(String rankA, String rankB) {
        double pkta,pktb;
        pkta=turnRankInPoints(rankA);
        pktb=turnRankInPoints(rankB);
        boolean s=isDuoPossible(rankA, rankB);
        if(s==true) {
            String a = rankA.replaceAll("\\D+", "");
            String b = rankB.replaceAll("\\D+", "");     //Wyciąganie dywizji gold 1 := 1, diamond 3 := 3
            double points = 20-Math.abs((Math.abs(-5+Integer.parseInt(a))+pkta)-(Math.abs(-5+Integer.parseInt(b))+pktb));
            return points;
        } else
            return (-100);
    }
    // w drużynie jest 5 ról -Top,Jungle,Mid,Bot,Support //
    // żeby móc grać w dwójkę należy pokryć 3 role//
    //każdy gracz ma do dyspozycji rolę preferowaną i rolę poboczną//
    //Gdy np mecz nie może wystartować bo nie ma kogoś kto ma daną rolę na roli preferowanej to czyjaś rola preferowana jest ignorowana i gra przydziela mu rolę poboczną"
    private boolean isThereRoleCover(String mainA, String mainB, String secA, String secB) {
        if( (mainA.compareTo(mainB)==0 || mainA.compareTo(secB)==0) && (secA.compareTo(mainB)==0 || secA.compareTo(secB)==0))
            return false;
        else
            return true;
    }
    //Niektóre role bardzo dobrze ze sobą współpracują gdy gracze mają możliwość wcześniej ustalonej komunikacji głosowej, która jest dostępna tylko dla graczy którzy decydują się grać w drużynie//
	private double rolePoints(String mainA, String mainB, String secA, String secB) {
        int dodatek=0;
		if(isThereRoleCover(mainA, mainB, secA, secB)==true) {
		    if(mainA.contains("Support") && mainB.contains("Bot") || mainB.contains("Support") && mainA.contains("Bot"))
		        return 20;
		    if((mainA.contains("Jungle") || (mainB.contains("Top") && mainB.contains("Mid"))) || (mainB.contains("Jungle") || (mainA.contains("Top") && mainA.contains("Mid"))) )
		        return 20;
		    if(mainA.contains("Support") || mainB.contains("Support") || secA.contains("Support") || secB.contains("Support"))
                dodatek = dodatek + 5;
            if (mainA.contains("Fill") || mainB.contains("Fill") || secA.contains("Fill") || secB.contains("Fill"))
                dodatek = dodatek + 10;

        }
        return 5+dodatek;
	}
    // Podliczanka max to chyba 60pkt im mniej tym gorzej
    private double SumOfPoints(int ageA, int ageB,String rankA, String rankB, String mainA,String mainB, String secA, String secB){
        double a = agePoints(ageA , ageB) + rankPoints(rankA,rankB) + rolePoints(mainA, mainB, secA, secB);
        return a;
    }

}
