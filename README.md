in order to update the .jar file please use the following:

kotlinc main.kt -include-runtime -d main.jar

in order to run the code please use the following:

java -jar main.jar 0 <packagename> <name> <layoutname>
ex:  java -jar main.jar 0 ae.adnoc.gov.ui lpg_item_details fragment_lpg_item_details

where: 
    // arg[0]: isFragment 0:false else 1
    // arg[1]: package name
    // arg[2]: file name
    //args[3]: layout name
	
