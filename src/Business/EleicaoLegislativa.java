package Business;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 *  Classe representante de uma eleição legislativa.
 * 
 *  @author joaocosta
 */
public class EleicaoLegislativa extends Eleicao {

  public EleicaoLegislativa(Calendar data, int id) {
    super(data, id);
  }
  

  @Override
  public String toString () {
    return "[LEGISLATIVA] " + super.toString();
  }
  
  //get the highest value in the 2D table and remove it!
//returning the row=(party) to which it belongs
	public static int getMaxElement(double [][]dTable, int pN, int pM)
	{
	double maxValue = dTable[0][0];

        int i =0, j =0;
	for (int sn = 0; sn < pN; sn++) {
		for (int sm = 0; sm < pM; sm++) {
		if(dTable[sm][sn] > maxValue){
			maxValue = dTable[sm][sn];
			i =sm;
			j = sn;
			}
		  }
		}
	dTable[i][j] = 0.0; // zero the highest element for next run
	return j;
	}

// Build a d'Hondt Table
    public static void DrawDhondtTable(double [][]dTable ,int pN, int pM) {
		String row = "";
		//DecimalFormat df = new DecimalFormat("##.##");		
		DecimalFormat df = new DecimalFormat();		
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		for (int n = 0; n < pN; n++) {
			for (int m = 0; m < pM; m++) {
//				row = row + Math.round(dTable[m][n]) +", ";
				row = row + df.format(dTable[m][n]) + ", ";
			}
			row = row + "\n";
		}

		System.out.println(row);
	}

    public List<Integer> alocarMandatos(List<Lista> lista1) {
        
	int chairs = 3;
        List<Lista> lista = new ArrayList<Lista>(Arrays.asList(new Lista(1, 16), new Lista(2, 38), new Lista(3, 14),new Lista(4, 29),new Lista(5, 8),new Lista(6, 1),new Lista(7, 1),new Lista(8, 1)));
        
	double []votes =  new double[]  {16, 38, 14, 29, 8, 1, 1, 1};

	int [] allocated = new int[lista.size()];
	double [][] dhondtTable = new double [chairs][lista.size()];
	System.out.println("Dhondt Table chairs="+chairs+ " parties= " +lista.size()+ "\n");
// Build D'Hondt Table
	for (int m = 0; m < chairs; m++) {
		for (int n = 0; n < lista.size(); n++) {
			if (m == 0)
				dhondtTable[m][n] = lista.get(n).votos;
			else
				dhondtTable[m][n] = dhondtTable[0][n]/(m+1);
		}
	}
//Print Table
        System.out.println("Table for D'Hondt");
	DrawDhondtTable(dhondtTable,lista.size(), chairs);

/*Allocate Ministers using D'Hondt
 * iterate through the number of cabinet posts to allocate
 * find the highest value in the D'Hondt Table 
 */
	int o = 0;
	for (int c= 1; c < chairs+1; c++) {
		o = getMaxElement(dhondtTable,lista.size(), chairs);
		DrawDhondtTable(dhondtTable,lista.size(), chairs);
	 	System.out.println(" Seat " + c + " for " + lista.get(o).idLista);
		allocated[o] = allocated[o] + 1;
	}

	DrawDhondtTable(dhondtTable,lista.size(), chairs);

//Output Results
        List<Integer> mandatos = new ArrayList<Integer>();
	for (int p=0; p < lista.size(); p++) {
            mandatos.add(allocated[p]);
	}
	
        return mandatos;
    }
}
