import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.*;  
import java.util.Scanner;

class ThreadDemo extends Thread {
    Thread t;
   private String threadName;
   
   ThreadDemo( String name){
       threadName = name;
   }
    
    static void updateProgress(double progressPercentage) {
    final int width = 50; // progress bar width in chars

    System.out.print("\r[");
    int i = 0;
    for (; i <= (int)(progressPercentage*width); i++) {
      System.out.print(".");
    }
    for (; i < width; i++) {
      System.out.print(" ");
    }
    System.out.print("]");
  }
    
   public void run() {
      try {
      for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
        updateProgress(progressPercentage);
        Thread.sleep(20);
      }
    } catch (InterruptedException e) {}
   }
   
   public void start ()
   {
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}




public class api {
 
	public static void main(String[] args) {
		try {
            
            ThreadDemo T1 = new ThreadDemo( "Thread-1");
            
            
            System.out.println("Enter article to search: ");
            Scanner so=new Scanner(System.in);
            
            String word=so.nextLine();
            T1.start();
            String[] wordArray = word.split(" ");
            word = "";
            for(String s : wordArray) {
                word = word + s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase() + "+";
            }
            word = word.substring(0,word.length()-1);
            
            
            
            String url_final="https://en.wikipedia.org/w/api.php?action=query&titles="+word+"&format=json";
			URL url = new URL(url_final);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "",ans="";
            T1.t.join();
			while (null != (strTemp = br.readLine())) {
				ans=ans+strTemp;
			}

            int index = ans.indexOf("\"pageid\":");
            ans = ans.substring(index + "pageid:".length()+2);
            
            String pageid = ans.substring(0,ans.indexOf(","));
            
            ans="http://en.wikipedia.org/wiki/index.html?curid="+pageid+"\n";
            
            FileOutputStream fout=new FileOutputStream("javaWikiSearchDefaultLogFile",true);  
            String str="\n";
            byte b1[]=str.getBytes();//converting string into byte array  
          fout.write(b1);  
          
            
           byte b[]=ans.getBytes();//converting string into byte array  
          fout.write(b);  
          fout.close(); 

          System.out.println("\nLink can be found in javaWikiSearchDefaultLogFile file.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}