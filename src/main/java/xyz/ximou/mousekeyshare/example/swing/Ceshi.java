package xyz.ximou.mousekeyshare.example.swing;

import javax.swing.*;

import com.sun.awt.AWTUtilities;
 
/*导入 AWTUtilities可能会提示：访问限制：由于对必需的库 C:\Program Files\Java\jre6\lib\rt.jar 具有一定限制，因此无法访问类型 AWTUtilities，请到eclipse进行设置：窗口》首选项》java》编译器》错误警告   选择 代码样式》访问外层不可访问成员》   把错误修改成功 警告
*/
 
public class Ceshi extends JFrame
{
    public Ceshi()
    {
        JLabel label=new JLabel("这是一个标签！");
        label.setOpaque(false);
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label);
 
        setSize(600,400);       
 
       setLocationRelativeTo(null);
 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setUndecorated(true);
       //Opacity最大值为1.0f，也就是什么也不透明，取值不能大于1.0f
        AWTUtilities.setWindowOpacity(this,0.5f);
        setVisible(true);
        
    }
    public static void main(String[] args)
    {
        new Ceshi();
    }
 
}
 
 