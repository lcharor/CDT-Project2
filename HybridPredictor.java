

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HybridPredictor {
	public static String traceFileName;
	public static void hybridPredictor(String[] simulatorInitializer) throws IOException{
		//initializing variables
		int misprediction=0;
		int noOfPredictions=0;
		int k=Integer.parseInt(simulatorInitializer[1]);
		int M1=Integer.parseInt(simulatorInitializer[2]);
		int N=Integer.parseInt(simulatorInitializer[3]);
		int M2=Integer.parseInt(simulatorInitializer[4]);
		traceFileName=simulatorInitializer[5];
		PredictionTable predictionTable=new PredictionTable();
		//making Chooser table
		ArrayList<Block> chooser_table=predictionTable.PredictionTableMaker(k);
		int p=(int) Math.pow(2, k);
		for(int i=0;i<p;i++){
			chooser_table.get(i).setCount(1);
		}
		//making  global history table for gshare predictor
		 String global_br_Hist ="";
	        for(int i=0;i<N;i++){
	      	  global_br_Hist='0'+global_br_Hist;
	        }
		//making prediction table for gshare
	    ArrayList<Block> gshare_pred_table=predictionTable.PredictionTableMaker(M1);
	    //making prediction table for bimodel
	    ArrayList<Block> bimodal_pred_table=predictionTable.PredictionTableMaker(M2);
	  //scanning trace file
		BufferedReader bufferedReader=new BufferedReader(new FileReader(traceFileName));
		String currLine=bufferedReader.readLine();
		while(currLine!=null){
			noOfPredictions++;
			String branchAddress=currLine.substring(0,currLine.indexOf(' '));
			String Actual_outcome=currLine.substring(currLine.indexOf(' ')+1);
			
			String branchAddBin=Long.toBinaryString(Long.parseLong(branchAddress,16));
			
			if(branchAddBin.length()<32){
				while(branchAddBin.length()<32){
				branchAddBin='0'+branchAddBin;
			       }
		       }
	
			//step 1--find gshare outcome and bimodal outcome
			//--->first find gshare outcome
			String indexbits_gshare=branchAddBin.substring(branchAddBin.length()-M1-2,branchAddBin.length()-2);	
	        
			String first=indexbits_gshare.substring(0, N);
			
            String second=indexbits_gshare.substring(N, M1);
            
            int XOR_output=Integer.parseInt(global_br_Hist,2)^ Integer.parseInt(first,2);
           
            String XOR_String=Integer.toBinaryString(XOR_output);
           
            if(XOR_String.length()<N)
            {
            	while(XOR_String.length()<N)
            		XOR_String='0'+XOR_String;
            }
            
            String combined_string=XOR_String+second;
			
			int SetNo_gshare=Integer.parseInt(combined_string, 2);
			int gshare_outcome=gshare_pred_table.get(SetNo_gshare).count;
			int z=gshare_outcome;
			
		  //--->find bimodal outcome
			String indexbits_bimodal=branchAddBin.substring(branchAddBin.length()-M2-2,branchAddBin.length()-2);	
            
            int SetNo_bimodal=Integer.parseInt(indexbits_bimodal, 2);
            
          //Updating Prediction Table	
           int  bimodal_outcome=bimodal_pred_table.get(SetNo_bimodal).count;
            int y=bimodal_outcome;
            
         
			
			//step2--getting count from chooser table to predict which predictor to use
			String indexbits_chooser=branchAddBin.substring(branchAddBin.length()-k-2,branchAddBin.length()-2);
			
			int SetNo_chooser=Integer.parseInt(indexbits_chooser, 2);
			int chooser_tabe_counter=chooser_table.get(SetNo_chooser).count;
		
			//step -3 taking decision on the basis of chooser counter
			//step 4-5  combined 
			
			if(chooser_tabe_counter>1){
				//we will use gshare predictor
						
			//Updating Prediction Table	
				
				if(Actual_outcome.equalsIgnoreCase("t")){
					
					if(z>1){
					if(z!=3)
							gshare_pred_table.get(SetNo_gshare).setCount(z+1);
							
					}
					else
					{
					misprediction++;
					gshare_pred_table.get(SetNo_gshare).setCount(z+1);
				
					}
					global_br_Hist='1'+global_br_Hist;
					global_br_Hist=global_br_Hist.substring(0, N);
				}
				if(Actual_outcome.equalsIgnoreCase("n")){
				
					if(z<2){
						if(z!=0)
							gshare_pred_table.get(SetNo_gshare).setCount(z-1);
						
					}
					else{
						misprediction++;
						gshare_pred_table.get(SetNo_gshare).setCount(z-1);
						
					}
					global_br_Hist='0'+global_br_Hist;
					global_br_Hist=global_br_Hist.substring(0, N);
					
				}
			}
			else{
				//we will use bimodal predictor
				
				
				  //Updating Prediction Table	
				if(Actual_outcome.equalsIgnoreCase("t")){
					
					if(y>1){
					if(y!=3)
							bimodal_pred_table.get(SetNo_bimodal).setCount(y+1);
							
					}
					else
					{
					misprediction++;
					bimodal_pred_table.get(SetNo_bimodal).setCount(y+1);
					
					}
					global_br_Hist='1'+global_br_Hist;
					global_br_Hist=global_br_Hist.substring(0, N);
				}
				if(Actual_outcome.equalsIgnoreCase("n")){
				
					if(y<2){
						if(y!=0)
							bimodal_pred_table.get(SetNo_bimodal).setCount(y-1);
						
					}
					else{
						misprediction++;
						bimodal_pred_table.get(SetNo_bimodal).setCount(y-1);
						
					}
					global_br_Hist='0'+global_br_Hist;
					global_br_Hist=global_br_Hist.substring(0, N);
				}
				
			}
			
			//step 6--updating chooser count
			//outcome   gshare  bimodal
			if(Actual_outcome.equalsIgnoreCase("t")){
				if(gshare_outcome>1 && bimodal_outcome<2){
					//increment chooser by one
					if(chooser_tabe_counter!=3)
					chooser_table.get(SetNo_chooser).setCount(chooser_tabe_counter+1);
				}
				if(gshare_outcome<2 && bimodal_outcome>1){
					//decrement chooser
					if(chooser_tabe_counter!=0)
						chooser_table.get(SetNo_chooser).setCount(chooser_tabe_counter-1);
				}
				
			}
			if(Actual_outcome.equalsIgnoreCase("n")){
				if(gshare_outcome<2 && bimodal_outcome>1){
					//increment chooser by one
					if(chooser_tabe_counter!=3)
						chooser_table.get(SetNo_chooser).setCount(chooser_tabe_counter+1);
				}
				if(gshare_outcome>1 && bimodal_outcome<2){
					//decrement chooser 
					if(chooser_tabe_counter!=0)
						chooser_table.get(SetNo_chooser).setCount(chooser_tabe_counter-1);
				}
				
			}
			
			currLine=bufferedReader.readLine();
		}
		System.out.println("COMMAND");
		System.out.println("./sim hybrid "+k+"\t"+M1+"\t"+N+"\t"+M2+"\t"+traceFileName);
		System.out.println("OUTPUT");
		System.out.println("number of predictions: \t"+noOfPredictions);
		System.out.println("number of mispredictions: \t"+misprediction);
		float r=(((float) misprediction)/((float)noOfPredictions))*100;
		System.out.println("misprediction rate: \t"+String.format("%1.2f", r)+"%");
		System.out.println("FINAL CHOOSER CONTENTS");
		printContents(chooser_table);
		System.out.println("FINAL GSHARE CONTENTS");
		printContents(gshare_pred_table);
		System.out.println("FINAL BIMODAL CONTENTS");
		printContents(bimodal_pred_table);
	}
	private static void printContents(ArrayList<Block> printTable){

		
		for(int i=0;i<printTable.size();i++){
			System.out.println(i+" \t "+printTable.get(i).getCount());
		}
	}
}
