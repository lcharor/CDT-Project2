import java.io.IOException;


public class sim {
	public static String traceFileName;
	public static void main(String[] simulatorInitializer) throws IOException{
		String type=simulatorInitializer[0];
		
		if(type.equalsIgnoreCase("bimodal")){
			BiModalPredictor.bimodalPredictor(simulatorInitializer);
		}
		if(type.equalsIgnoreCase("gshare")){
			
			GsharePredictor.gsharePredictor(simulatorInitializer);
		}
		if(type.equalsIgnoreCase("hybrid")){
			
			HybridPredictor.hybridPredictor(simulatorInitializer);
		}
	}
}
