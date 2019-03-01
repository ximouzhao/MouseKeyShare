package xyz.ximou.mousekeyshare.systeminfo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class SystemInfoTest {

    @Test
    void getSystemType() {
        //Assert.assertEquals(SystemInfo.getSystemType(),SystemTypeEnum.MAC_OS);
        Assert.assertEquals(SystemInfo.getSystemType(), SystemTypeEnum.WINDOWS);
    }
}