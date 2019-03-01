package xyz.ximou.mousekeyshare.systeminfo;

public class SystemInfo {
    static SystemTypeEnum systemType=null;
    static {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if (osName.startsWith("Mac OS")) {
            systemType= SystemTypeEnum.MAC_OS;
        } else if (osName.startsWith("Windows")) {
            systemType= SystemTypeEnum.WINDOWS;
        } else if(osName.startsWith("Linux")){
            systemType= SystemTypeEnum.LINUX;
        }else{
            systemType= SystemTypeEnum.OTHER;
        }
    }
    public static SystemTypeEnum getSystemType(){
        return systemType;
    }
}
