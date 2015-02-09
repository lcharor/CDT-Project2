
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class GsharePredictor {
	public static String traceFileName;
	
	public static void gsharePredictor(String[] simulatorInitializer) throws IOException{
	
		int misprediction=0;
		int noOfPredictions=0;
		int n=0,m=0;
		PredictionTable predictionTable=new PredictionTable();
		//Scanning Command line and getting input
    	String type=simulatorInitializer[0];
		m=Integer.parseInt(simulatorInitializer[1]);
		n=Integer.parseInt(simulatorInitializer[2]);
		traceFileName=simulatorInitializer[simulatorInitializer.length-1];
		 String global_br_Hist ="";
	        for(int i=0;i<n;i++){
	      	  global_br_Hist='0'+global_br_Hist;
	        }
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
			         
						String first=indexbits.substring(0, n);
			            String second=indexbits.substring(n, m);
			            int XOR_output=Integer.parseInt(global_br_Hist,2)^ Integer.parseInt(first,2);
			            
			            String XOR_String=Integer.toBinaryString(XOR_output);
			       
			            if(XOR_String.length()<n)
			            {
			            	while(XOR_String.length()<n)
			            		XOR_String='0'+XOR_String;
			            }
			            String combined_string=XOR_String+second;
						int SetNo=0;
						SetNo=Integer.parseInt(combined_string, 2);
							
						
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
							global_br_Hist='1'+global_br_Hist;
							global_br_Hist=global_br_Hist.substring(0, n);
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
							global_br_Hist='0'+global_br_Hist;
							global_br_Hist=global_br_Hist.substring(0, n);
							
						}
						currLine=bufferedReader.readLine();								
						}
	//Printing Final Output	
		System.out.println("COMMAND");
		System.out.println("./sim gshare "+m+"\t"+n+"\t"+traceFileName);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:\t"+noOfPredictions);
		System.out.println("number of mispredictions:\t"+misprediction);
		float r=(((float) misprediction)/((float)noOfPredictions))*100;
		System.out.println("misprediction rate: \t"+String.format("%1.2f", r)+"%");
		printContents(pTable);
		
	}
	private static void printContents(ArrayList<Block> pTable){

		System.out.println("FINAL GSHARE CONTENTS");
		for(int i=0;i<pTable.size();i++){
			System.out.println(i+" \t "+pTable.get(i).getCount());
		}
	}
	
	
}
	
	
	
	
	
