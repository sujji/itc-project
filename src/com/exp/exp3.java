package com.exp;

public class exp3 {

	/**
	 * @param args
	 */


	public static void main(String[] args) {
//	    String line = "FDW EXOTIC DREAM 125GMSX3";
//	    String[] arr = line.split("\\d+", 2);
//	    String pt1 = arr[0].trim();
//	    String pt2 = line.substring(pt1.length() + 1).trim();
//	    System.out.println(pt1);
//	    System.out.println(pt2);
////	    
//	    String line1 = "VIVEL ALOE VERA 3IN1 FW 100ML";
//	    String[] arr1 = line1.split("\\d+", 2);
//	    String pt11 = arr1[0].trim();
//	    String pt21 = line1.substring(pt11.length() + 1).trim();
//	    System.out.println(pt11);
//	    System.out.println(pt21);
	    
//	    String line1 = "fiama di wills pure rio splash bathing bar, 125g (pack of 3)";
//	    System.out.println(line1.contains("ml"));
	    
//		String product_desc="ENGAGE XX3 COLOGNE 150ML"
//		.toLowerCase()	    		
//		.replaceAll("x3", " pack of 3");
//System.out.println(product_desc);
//System.out.println("ENGAGE xyxX3 COLOGNE 150ML".toLowerCase().replaceAll("(?<![xy])x3", "multiplied by 3"));

	    String product_desc="FDW Energizing sport SHOWERGEL 250ML"
				.toLowerCase()
				.replaceAll(" x ", "x")			
				.replaceAll("-", " ")			
				.replaceAll(" gms", "gms")	
				.replaceAll("gms", "gm")					
				.replaceAll("gm", "g")	
				.replaceAll("\\*2g", "g\\*2")
				.replaceAll("\\*3g", "g\\*3")
				.replaceAll("\\*4g", "g\\*4")
				.replaceAll("x2g", "gx2")
				.replaceAll("x3g", "gx3")
				.replaceAll("x4g", "gx4")
				.replaceAll("(?<!x)x3", " pack of 3 ")
				.replaceAll("(?<!x)x4", " pack of 4 ")
				.replaceAll("\\*2", " pack of 2 ")
				.replaceAll("\\*3", " pack of 3 ")
				.replaceAll("\\*4", " pack of 4 ")					
				.replaceAll("fdw", "fiama di wills")
				.replaceAll("showergel", "shower gel")
				.replaceAll("fw","face wash")
				.replaceAll("mix", "mixed")
				.replaceAll("carton", "")
				.replaceAll("cart", "")
				.replaceAll("detoilette", "de toilette")
				.replaceAll("moisturizer", "moisturiser")
				.replaceAll("aloevera", "aloe vera")
				.replaceAll("aleovera", "aloe vera")
				.replaceAll("\\(", " ")
				.replaceAll("\\)", " ")
				.replaceAll("fdw", "fiama di wills")
				.replaceAll("vln", "vivel love nourish")
				.replaceAll("obutter", "olive butter") 
				.replaceAll("sbutter", "shea butter")
				.replaceAll("\\+", " ")
				.replaceAll("\\%", " ")
				.replaceAll("facewash", "face wash")
				.replaceAll("fw", "face wash")
				.replaceAll("cdr", "Colour Damage Repair")
				.replaceAll("ahf", "Anti Hair Fall")
				.replaceAll("tdc", "Total Damage Control ")
				.replaceAll("vcr", "vivel cell renew")
				.replaceAll("vivel cr", "vivel cell renew")						
				.replaceAll("hw", "hand wash")
				.replaceAll("  ", " ")
				.toLowerCase();
	    System.out.println(product_desc.replaceAll("\\d+ml", ""));
	    
	}
}
