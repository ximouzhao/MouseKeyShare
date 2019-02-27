package xyz.ximou.mousekeyshare.systeminfo;

public class SystemInfo {
    static SystemType systemType=null;
    static {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if (osName.startsWith("Mac OS")) {
            systemType= SystemType.MAC_OS;
        } else if (osName.startsWith("Windows")) {
            systemType= SystemType.WINDOWS;
        } else if(osName.startsWith("Linux")){
            systemType= SystemType.LINUX;
        }else{
            systemType= SystemType.OTHER;
        }
    }
    public static SystemType getSystemType(){
        return systemType;
    }
}
