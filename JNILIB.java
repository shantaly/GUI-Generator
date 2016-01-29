/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/

/**
 *
 * @author Shantaly
 */
/*JNI java layer that uses the JNILIB.c that connects to the ParameterManager Library*/
public class JNILIB {
    static {
        System.loadLibrary("JNIpm");
    }

    public native int createPM(int size);
    
    public native int destroy();

	public native int parseFrom(String fp, char comment);

	public native int manage(String pname, int ptype, int required);

	public native int hasValue(String pname);

	public native int getIntValue(String pname);

	public native double getDoubleValue(String pname);

	public native boolean getBoolValue(String pname);

	public native String getStrValue(String pname);

	public native String getListValue(String pname);

   
}
