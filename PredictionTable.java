import java.util.ArrayList;


public class PredictionTable {
int m;

public ArrayList<Block> PredictionTableMaker(int m) {
	int p=(int) Math.pow(2,m);
	
	ArrayList<Block> predictionTable=new ArrayList<Block>();
	for(int i=0;i<p;i++)
	{
		
		Block block=new Block();
		block.count=2;
		predictionTable.add(block);
		
	}
	return predictionTable;
	
}


}
