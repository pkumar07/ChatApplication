public class Sender implements java.io.Serializable{
    
    byte[] data;
    String msg;
    String senderName;
    int index;

    
    public Sender(byte[] data, String msg, String senderName, int index) { 
    this.data = data;
    this.msg = msg;
    this.senderName = senderName;
    this.index = index;
    }
    
    public byte[] getData() {
        return data;
    }
    
    

    public String getMsg() {
        return msg;
    }
    
  

     public String getName() {
        return senderName;
    }
    
 
    
    public int getIndex(){
        return index;
    }
    

}