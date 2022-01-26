import za.co.mahlaza.research.grammarengine.base.models.template.Template;

import javax.swing.*;

public class ToCTeditorFrame extends JFrame {

    public static Template currTemplate;

    private int frameX;
    private int frameY;

    public ToCTeditorFrame(int frameX, int frameY) {
        super("ToCTeditor");
        this.frameX = frameX;
        this.frameY = frameY;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.frameX, this.frameY);
        setLocationRelativeTo(null);  // Center window on screen.
        setVisible(true);
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }
}
