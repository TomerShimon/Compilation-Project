package VAR;
import SYMBOL_TABLE.*;



public class VAR {
    public int offset_fp;
    public String name;
    public boolean is_global;

    public VAR(int offset_fp, String name, boolean is_global){
        this.offset_fp = offset_fp;
        this.name = name;
        this.is_global = is_global;
    }
}