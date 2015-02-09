import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class BiModalPredictor {
	public static String traceFileName;
	
	public static void bimodalPredictor(String[] simulatorInitializer) throws IOException{
		int misprediction=0;
		int noOfPredictions=0;
		int m=0;
		PredictionTable predictionTable=new PredictionTable();
		//Scanning Command line and getting input
		
		m=Integer.parseInt(simulatorInitializer[1]);
		traceFileName=simulatorInitializer[simulatorInitializer.length-1];
		
		//making prediction table
		ArrayList<Block> pTable=predictionTable.PredictionTableMaker(m);
		//scanning trace file
		BufferedReader bufferedReader=new BufferedReader(new FileReader(traceFileName));
		String currLine=bufferedReader.readLine();
		
		while(currLine !=null)
		{
			noOfPredictions++;
			String branchAddress=currLine.substring(0,currLine.indexOf(' '));
			String outcome=currLine.substring(currLine.indexOf(' ')+1);
			String branchAddBin=Long.toBinaryString(Long.parseLong(branchAddress,16));

			if(branchAddBin.length()<32){
				while(branchAddBin.length()<32){
				branchAddBin='0'+branchAddBin;
			       }
		       }
					//Calculating Index
						
						String indexbits=branchAddBin.substring(branchAddBin.length()-m-2,branchAddBin.length()-2);	
			            int SetNo=0;
						/*for(int j=0;j<indexbits.length();j++)
					    {
					    	int k=(indexbits.charAt(indexbits.length()-j-1))-48;
					       	SetNo=SetNo+(k*((int)(Math.pow(2,j))));
					    }*/
						SetNo=Integer.parseInt(indexbits, 2);
						
						
					//Updating Prediction Table	
						if(outcome.equalsIgnoreCase("t")){
							int k=pTable.get(SetNo).count;
							if(k>1){
							if(k!=3)
									pTable.get(SetNo).setCount(k+1);
							}
							else
							{
							misprediction++;
							pTable.get(SetNo).setCount(k+1);
							}
						}
						if(outcome.equalsIgnoreCase("n")){
						 int k=pTable.get(SetNo).count;
							if(k<2){
								if(k!=0)
									pTable.get(SetNo).setCount(k-1);
							}
							else{
								misprediction++;
								pTable.get(SetNo).setCount(k-1);
							}
							
						}
						currLine=bufferedReader.readLine();								
						}
	//Printing Final Output	
		System.out.println("COMMAND");
		System.out.println("./sim bimodal "+m+"\t"+traceFileName);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:\t"+noOfPredictions);
		System.out.println("number of mispredictions:\t"+misprediction);
		float r=(((float) misprediction)/((float)noOfPredictions))*100;
		System.out.println("misprediction rate: \t"+String.format("%1.2f", r)+"%");
		printContents(pTable);
		
	}
	private static void printContents(ArrayList<Block> pTable){

		System.out.println("Final BiModal Contents");
		for(int i=0;i<pTable.size();i++){
			System.out.println(i+" \t "+pTable.get(i).getCount());
		}
	}
}
